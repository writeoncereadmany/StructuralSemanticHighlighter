package com.writeoncereadmany.semantichighlighting.coloriser;


import com.intellij.ui.JBColor;

import java.awt.*;

/**
 * Straightforward way of extracting saturation/brightness from a sample color to use repeatedly
 * Note that HSB sat/brightness is somewhat crude: for uniform lightness across hues,
 * we'll need to use some color space stuff. That's a to-do later sort of problem though
 *
 * Blues/purples particularly are coming out darker than yellows/greens :(
 */
public class ColorScheme
{
    public static final float LIGHT_THEME_FADE_MODIFIER = 1.5f;
    public static final float DARK_THEME_FADE_MODIFIER = 0.65f;
    private final float lightThemeSaturation;
    private final float lightThemeBrightness;
    private final float darkThemeSaturation;
    private final float darkThemeBrightness;

    public ColorScheme(Color seedColor, Color color)
    {
        final float[] lightTheme = new float[3];
        final float[] darkTheme = new float[3];

        Color.RGBtoHSB(seedColor.getRed(), seedColor.getGreen(), seedColor.getBlue(), lightTheme);
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), darkTheme);

        lightThemeSaturation = lightTheme[1];
        lightThemeBrightness = lightTheme[2];
        darkThemeSaturation= darkTheme[1];
        darkThemeBrightness = darkTheme[2];
    }

    public Color withHue(float hue, boolean fade)
    {
        return new JBColor(Color.getHSBColor(hue, lightThemeSaturation, lightThemeBrightness * (fade ? LIGHT_THEME_FADE_MODIFIER : 1f)),
                           Color.getHSBColor(hue, darkThemeSaturation, darkThemeBrightness * (fade ? DARK_THEME_FADE_MODIFIER : 1f)));
    }
}
