package com.writeoncereadmany.semantichighlighting.psinspections;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIfStatement;
import com.intellij.psi.PsiKeyword;
import com.intellij.psi.PsiWhiteSpace;

/**
 * Created by tom on 08/07/2016.
 */
public class StatementInspections
{
    public static boolean isElseIf(final PsiIfStatement statement)
    {
        final PsiElement precedingElement = statement.getPrevSibling();
        if(!(precedingElement instanceof PsiWhiteSpace))
        {
            return false;
        }
        final PsiElement elementBeforeWhitespace = precedingElement.getPrevSibling();
        if(elementBeforeWhitespace instanceof PsiKeyword)
        {
            PsiKeyword keyword = (PsiKeyword)elementBeforeWhitespace;
            if(keyword.textMatches(PsiKeyword.ELSE))
            {
                return true;
            }
        }

        return false;
    }

}
