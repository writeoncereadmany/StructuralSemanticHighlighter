package com.writeoncereadmany.semantichighlighting;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.*;
import com.writeoncereadmany.semantichighlighting.coloriser.Coloriser;
import com.writeoncereadmany.semantichighlighting.coloriser.TextAttributesDescriptor;
import com.writeoncereadmany.semantichighlighting.psinspections.ConstructorInspections;
import com.writeoncereadmany.semantichighlighting.psinspections.StatementInspections;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static co.unruly.control.Predicates.not;
import static co.unruly.control.casts.Casts.instanceOf;
import static co.unruly.control.result.Match.ifIs;
import static co.unruly.control.result.Match.match;
import static com.writeoncereadmany.semantichighlighting.Level.*;
import static java.util.Optional.empty;

public class SemanticHighlighter extends JavaElementVisitor implements Annotator
{
    private static final boolean SHOW_REFERENCE_SCOPES = true;


    private static Function<PsiElement, Optional<Level>> introduceLevel = match(
        ifIs(instanceOf(PsiLoopStatement.class),
            then(LOOP)),
        ifIs(instanceOf(PsiIfStatement.class,
                not(StatementInspections::isElseIf)),
            then(IF)),
        ifIs(instanceOf(PsiParenthesizedExpression.class),
            then(PARENS)),
        ifIs(instanceOf(PsiLambdaExpression.class),
            then(LAMBDA)),
        ifIs(instanceOf(PsiExpressionList.class,
                nonEmpty(PsiExpressionList::getExpressions),
                hasParent(PsiCallExpression.class)),
            then(CALL)),
        ifIs(instanceOf(PsiTypeParameterList.class,
                nonEmpty(PsiTypeParameterList::getTypeParameters)),
            then(TYPE_PARAMS)),
        ifIs(instanceOf(PsiReferenceParameterList.class,
                nonEmpty(PsiReferenceParameterList::getTypeArguments)),
            then(TYPE_ARGS))
    ).otherwise(__ -> Optional.empty());

    private static <T extends PsiElement> Predicate<T> hasParent(Class<? extends PsiElement> psiCallExpressionClass) {
        return t -> psiCallExpressionClass.isAssignableFrom(t.getParent().getClass());
    }

    private static <T> Predicate<T> nonEmpty(Function<T, ? extends Object[]> arrayExtractor) {
        return  t -> arrayExtractor.apply(t).length > 0;
    }

    static <T, S> Function<T, Optional<S>> then(S s) {
        return __ -> Optional.of(s);
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

        PsiElement parent = element.getParent();

        if(!(parent instanceof PsiFile)) {
            highlight(parent, getHighlightFor(parent));
        }
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

        Optional<Level> newLevel = introduceLevel.apply(element);

        if(newLevel.isPresent())
        {
            return getHighlightFor(parentElement).addLevel(newLevel.get().name());
        }

        if(parentElement == null)
        {
            return new TextAttributesDescriptor("UNKNOWN", SemanticHighlightingColors.UNKNOWN_HUE);
        }
        return getHighlightFor(parentElement);
    }

    private Optional<PsiElement> nextConcreteElement(PsiElement element) {
        if (element == null) {
            return empty();
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
