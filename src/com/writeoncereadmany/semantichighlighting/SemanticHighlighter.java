package com.writeoncereadmany.semantichighlighting;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.*;
import com.writeoncereadmany.semantichighlighting.coloriser.Coloriser;
import com.writeoncereadmany.semantichighlighting.psinspections.ConstructorInspections;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

import static java.util.Arrays.asList;

public class SemanticHighlighter extends JavaElementVisitor implements Annotator
{
    private static final boolean SHOW_REFERENCE_SCOPES = true;

    private static final List<Predicate<PsiElement>> INTRODUCE_NEW_LEVEL = asList(
            element -> element instanceof PsiLoopStatement,
            element -> element instanceof PsiIfStatement,
            element -> element instanceof PsiParenthesizedExpression,
            element -> element instanceof PsiLambdaExpression,
            element -> element instanceof PsiExpressionList &&
                       element.getParent() instanceof PsiCallExpression &&
                       ((PsiExpressionList)element).getExpressions().length > 0,
            element -> element instanceof PsiTypeParameterList &&
                       ((PsiTypeParameterList)element).getTypeParameters().length > 0,
            element -> element instanceof PsiReferenceParameterList &&
                       ((PsiReferenceParameterList)element).getTypeArguments().length > 0);

    private AnnotationHolder currentAnnotationHolder;

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder)
    {
        this.currentAnnotationHolder = holder;
        element.accept(this);
    }

    @Override
    public void visitReferenceExpression(PsiReferenceExpression expression)
    {
        if(!SHOW_REFERENCE_SCOPES)
        {
            return;
        }
        PsiReference reference = expression.getReference();
        if(reference != null)
        {
            PsiElement definition = reference.resolve();
            if (definition != null &&
                definition instanceof PsiVariable &&
                !((PsiVariable) definition).hasModifierProperty(PsiModifier.STATIC))
            {
                if(((PsiVariable)definition).hasModifierProperty(PsiModifier.FINAL))
                {
                    highlight(expression, Coloriser.embolden(getHighlightFor(definition)));
                }
                else
                {
                    highlight(expression, Coloriser.italicise(getHighlightFor(definition)));
                }
            }
        }
    }

    @Override
    public void visitReferenceParameterList(PsiReferenceParameterList list) {
        if(list.getTypeArguments().length > 0)
        {
            highlight(list, getHighlightFor(list));
        }
    }

    @Override
    public void visitForStatement(PsiForStatement statement)
    {
        highlight(statement, getHighlightFor(statement));
    }

    @Override
    public void visitForeachStatement(PsiForeachStatement statement) {
        highlight(statement, getHighlightFor(statement));
    }

    @Override
    public void visitDoWhileStatement(PsiDoWhileStatement statement) {
        highlight(statement, getHighlightFor(statement));
    }

    @Override
    public void visitWhileStatement(PsiWhileStatement statement) {
        highlight(statement, getHighlightFor(statement));
    }

    @Override
    public void visitIfStatement(PsiIfStatement statement) {
        highlight(statement, getHighlightFor(statement));
    }

    @Override
    public void visitParameterList(PsiParameterList list)
    {
        highlight(list, getHighlightFor(list));
    }

    @Override
    public void visitExpressionList(PsiExpressionList list)
    {
        highlight(list, getHighlightFor(list));
    }

    @Override
    public void visitTypeParameterList(PsiTypeParameterList list) {
        highlight(list, getHighlightFor(list));
    }

    @Override
    public void visitMethod(PsiMethod method)
    {
        highlight(method, getHighlightFor(method));
    }

    @Override
    public void visitClass(PsiClass aClass)
    {
        highlight(aClass, getHighlightFor(aClass));
    }

    @Override
    public void visitParenthesizedExpression(PsiParenthesizedExpression expression)
    {
        highlight(expression, getHighlightFor(expression));
    }

    @Override
    public void visitKeyword(PsiKeyword keyword)
    {
        highlight(keyword, getHighlightFor(keyword));
    }

    @Override
    public void visitComment(PsiComment comment)
    {
        highlight(comment, getHighlightFor(comment));
    }

    @Override
    public void visitLambdaExpression(PsiLambdaExpression expression) {
        highlight(expression, getHighlightFor(expression));
    }

    private void highlight(PsiElement element, TextAttributesKey attributes)
    {
        Annotation annotation = currentAnnotationHolder.createInfoAnnotation(element, null);
        annotation.setTextAttributes(attributes);
    }

    private TextAttributesKey getHighlightFor(final PsiElement element)
    {
        // terminators
        if(element instanceof PsiMethod)
        {
            return getMethodHighlight((PsiMethod)element);
        }
        else if(element instanceof PsiClass && !(element instanceof PsiTypeParameter))
        {
            return SemanticHighlightingColors.CLASS;
        }
        final PsiElement parentElement = element.getParent();

        // modifiers
        if(element instanceof PsiKeyword || element instanceof PsiComment)
        {
            if(SHOW_REFERENCE_SCOPES && element.textMatches(PsiKeyword.THIS))
            {
                return SemanticHighlightingColors.CLASS;
            }
            return Coloriser.darken(getHighlightFor(parentElement));
        }

        if(INTRODUCE_NEW_LEVEL.stream().anyMatch(predicate -> predicate.test(element)))
        {
            return Coloriser.rotate(getHighlightFor(parentElement));
        }

        // otherwise recur

        if(parentElement == null)
        {
            return SemanticHighlightingColors.UNKNOWN;
        }
        return getHighlightFor(parentElement);
    }

    public TextAttributesKey getMethodHighlight(final PsiMethod method)
    {
        if(method.isConstructor())
        {
            if(ConstructorInspections.isAuxiliaryConstructor(method))
            {
                return SemanticHighlightingColors.AUXILIARY_CONSTRUCTOR;
            }
            else
            {
                return SemanticHighlightingColors.CANONICAL_CONSTRUCTOR;
            }
        }
        else
        {
            if(method.getModifierList().hasModifierProperty(PsiModifier.PUBLIC))
            {
                return SemanticHighlightingColors.PUBLIC_METHOD;
            }
            else
            {
                return SemanticHighlightingColors.PRIVATE_METHOD;
            }
        }
    }
}
