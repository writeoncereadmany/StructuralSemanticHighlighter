package com.writeoncereadmany.semantichighlighting;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.*;
import com.writeoncereadmany.semantichighlighting.coloriser.Coloriser;
import com.writeoncereadmany.semantichighlighting.coloriser.TextAttributesDescriptor;
import com.writeoncereadmany.semantichighlighting.psinspections.ConstructorInspections;
import com.writeoncereadmany.semantichighlighting.psinspections.StatementInspections;
import com.writeoncereadmany.semantichighlighting.util.Optionals;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class SemanticHighlighter extends JavaElementVisitor implements Annotator
{
    private static final boolean SHOW_REFERENCE_SCOPES = true;

    private static final List<Function<PsiElement, Optional<String>>> INTRODUCE_NEW_LEVEL = asList(
            level(element -> element instanceof PsiLoopStatement, "LOOP"),
            level(element -> element instanceof PsiIfStatement &&
                             !StatementInspections.isElseIf((PsiIfStatement)element), "IF"),
            level(element -> element instanceof PsiParenthesizedExpression, "PARENS"),
            level(element -> element instanceof PsiLambdaExpression, "LAMBDA"),
            level(element -> element instanceof PsiExpressionList &&
                             element.getParent() instanceof PsiCallExpression &&
                             ((PsiExpressionList)element).getExpressions().length > 0, "CALL"),
            level(element -> element instanceof PsiTypeParameterList &&
                             ((PsiTypeParameterList)element).getTypeParameters().length > 0, "TYPE_PARAMS"),
            level(element -> element instanceof PsiReferenceParameterList &&
                             ((PsiReferenceParameterList)element).getTypeArguments().length > 0, "TYPE_ARGS"));

    private static Function<PsiElement, Optional<String>> level(Predicate<PsiElement> condition, String type)
    {
        return element -> condition.test(element) ? Optional.of(type) : Optional.empty();
    }

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
                    highlight(expression, getHighlightFor(definition).embolden());
                }
                else
                {
                    highlight(expression, getHighlightFor(definition).italicise());
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
    public void visitPackageStatement(PsiPackageStatement statement) {
        highlight(statement, getHighlightFor(statement));
    }

    @Override
    public void visitImportStatement(PsiImportStatement statement) {
        highlight(statement, getHighlightFor(statement));
    }

    @Override
    public void visitImportStaticStatement(PsiImportStaticStatement statement) {
        highlight(statement, getHighlightFor(statement));
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

    private void highlight(PsiElement element, TextAttributesDescriptor attributes)
    {
        Annotation annotation = currentAnnotationHolder.createInfoAnnotation(element, null);
        annotation.setTextAttributes(Coloriser.fromDescriptor(attributes));
    }

    private TextAttributesDescriptor getHighlightFor(final PsiElement element)
    {
        // terminators
        if(element instanceof PsiMethod)
        {
            return getMethodHighlight((PsiMethod)element);
        }
        else if(element instanceof PsiClass && !(element instanceof PsiTypeParameter))
        {
            return new TextAttributesDescriptor("CLASS", SemanticHighlightingColors.CLASS_HUE);
        }
        final PsiElement parentElement = element.getParent();


        if(element instanceof PsiKeyword)
        {
            if(SHOW_REFERENCE_SCOPES && element.textMatches(PsiKeyword.THIS))
            {
                return new TextAttributesDescriptor("CLASS", SemanticHighlightingColors.CLASS_HUE);
            }
            return getHighlightFor(parentElement).fade();
        }

        if(element instanceof PsiComment)
        {
            if(parentElement instanceof PsiCodeBlock)
            {
                return getHighlightFor(nextConcreteElement(element).orElse(parentElement)).fade();
            }
            else
            {
                return getHighlightFor(parentElement).fade();
            }
        }

        Optional<String> newLevel = INTRODUCE_NEW_LEVEL.stream()
                .map(f -> f.apply(element))
                .flatMap(Optionals::stream)
                .findFirst();
        if(newLevel.isPresent())
        {
            return getHighlightFor(parentElement).addLevel(newLevel.get());
        }

        if(parentElement == null)
        {
            return new TextAttributesDescriptor("UNKNOWN", SemanticHighlightingColors.UNKNOWN_HUE);
        }
        return getHighlightFor(parentElement);
    }

    private Optional<PsiElement> nextConcreteElement(PsiElement element) {
        if (element == null) {
            return Optional.empty();
        }

        PsiElement nextElement = element.getNextSibling();

        if(nextElement instanceof PsiWhiteSpace) {
            return nextConcreteElement(nextElement);
        } else {
            return Optional.of(nextElement);
        }
    }

    public TextAttributesDescriptor getMethodHighlight(final PsiMethod method)
    {
        if(method.isConstructor())
        {
            if(ConstructorInspections.isAuxiliaryConstructor(method))
            {
                return new TextAttributesDescriptor("AUXILIARY_CONSTRUCTOR", SemanticHighlightingColors.AUXILIARY_CONSTRUCTOR_HUE);
            }
            else
            {
                return new TextAttributesDescriptor("CANONICAL_CONSTRUCTOR", SemanticHighlightingColors.CANONICAL_CONSTRUCTOR_HUE);
            }
        }
        else
        {
            if(method.getModifierList().hasModifierProperty(PsiModifier.PUBLIC))
            {
                return new TextAttributesDescriptor("PUBLIC_METHOD", SemanticHighlightingColors.PUBLIC_METHOD_HUE);
            }
            else
            {
                return new TextAttributesDescriptor("PRIVATE_METHOD", SemanticHighlightingColors.PRIVATE_METHOD_HUE);
            }
        }
    }
}
