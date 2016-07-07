package com.writeoncereadmany.semantichighlighting.coloriser;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("UseJBColor")
public class Coloriser
{
    private static Pattern ROTATION_COUNT = Pattern.compile("_ROTx(\\d+)");
    private static ColorSequence COLOR_SEQUENCE = new GoldenRatioHueSequence();

    public static TextAttributesKey darken(final TextAttributesKey key)
    {
        if(key.getExternalName().contains("_DARK"))
        {
            return key;
        }
        return TextAttributesKey.createTextAttributesKey(key.getExternalName() + "_DARK", darken(key.getDefaultAttributes()));
    }

    public static TextAttributesKey embolden(final TextAttributesKey key)
    {
        if(key.getExternalName().contains("_BOLD"))
        {
            return key;
        }
        return TextAttributesKey.createTextAttributesKey(key.getExternalName() + "_BOLD", embolden(key.getDefaultAttributes()));
    }


    public static TextAttributesKey italicise(final TextAttributesKey key)
    {
        if(key.getExternalName().contains("_ITALIC"))
        {
            return key;
        }
        return TextAttributesKey.createTextAttributesKey(key.getExternalName() + "_ITALIC", italicise(key.getDefaultAttributes()));
    }

    private static TextAttributes darken(final TextAttributes attributes)
    {
        return new TextAttributes(
                darken(attributes.getForegroundColor()),
                attributes.getBackgroundColor(),
                attributes.getEffectColor(),
                attributes.getEffectType(),
                attributes.getFontType());
    }

    private static TextAttributes embolden(final TextAttributes attributes)
    {
        return new TextAttributes(
                attributes.getForegroundColor(),
                attributes.getBackgroundColor(),
                attributes.getEffectColor(),
                attributes.getEffectType(),
                Font.BOLD);
    }

    private static TextAttributes italicise(final TextAttributes attributes)
    {
        return new TextAttributes(
                attributes.getForegroundColor(),
                attributes.getBackgroundColor(),
                attributes.getEffectColor(),
                attributes.getEffectType(),
                Font.ITALIC);
    }

    private static Color darken(final Color foregroundColor)
    {
        return new Color((int)(foregroundColor.getRed() * 0.65), (int)(foregroundColor.getGreen() * 0.65), (int)(foregroundColor.getBlue() * 0.65));
    }

    public static TextAttributesKey rotate(final TextAttributesKey key)
    {
        return TextAttributesKey.createTextAttributesKey(rotateName(key.getExternalName()), rotate(key.getDefaultAttributes()));
    }

    public static String rotateName(final String key)
    {
        Matcher matcher = ROTATION_COUNT.matcher(key);
        if(matcher.find())
        {
            String rotationCount = matcher.group(1);
            int incrementedRotationCount = Integer.parseInt(rotationCount) + 1;
            return key.replace(rotationCount, Integer.toString(incrementedRotationCount));
        }
        else
        {
            return key + "_ROTx1";
        }
    }

    private static TextAttributes rotate(final TextAttributes attributes)
    {
        return new TextAttributes(
                rotate(attributes.getForegroundColor()),
                attributes.getBackgroundColor(),
                attributes.getEffectColor(),
                attributes.getEffectType(),
                attributes.getFontType());
    }

    private static Color rotate(Color color)
    {
        return COLOR_SEQUENCE.nextColor(color);
    }
}
