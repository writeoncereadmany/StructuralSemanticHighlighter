package com.writeoncereadmany.semantichighlighting.psinspections;

import com.intellij.psi.*;

public class ConstructorInspections
{
    /**
     * An auxiliary constructor is one which calls another constructor as its only statement.
     * In general, a class should only ever have a maximum of one canonical (ie, non-auxiliary) constructor,
     * which is responsible for initialisation and maintaining class invariants.
     * A constructor which calls another constructor and then does further initialisation is technically known as "a mess"
     */
    public static boolean isAuxiliaryConstructor(PsiMethod method)
    {
        PsiCodeBlock body = method.getBody();
        if(body != null)
        {
            PsiStatement[] statements = body.getStatements();

            if(statements.length == 1)
            {
                final PsiStatement firstStatement = statements[0];
                if(firstStatement instanceof PsiExpressionStatement)
                {
                    final PsiExpression expression = ((PsiExpressionStatement) firstStatement).getExpression();
                    if(expression instanceof PsiMethodCallExpression)
                    {
                        PsiReferenceExpression methodExpression = ((PsiMethodCallExpression) expression).getMethodExpression();
                        final PsiElement methodName = methodExpression.getReferenceNameElement();
                        if(methodName != null)
                        {
                            return methodName.textMatches(PsiKeyword.THIS);
                        }
                    }
                }
            }
        }
        // if we can't inspect the code of a constructor, then assume it's canonical: we only really care about this
        // for our own code, for which we have the full source by definition
        return false;
    }
}
