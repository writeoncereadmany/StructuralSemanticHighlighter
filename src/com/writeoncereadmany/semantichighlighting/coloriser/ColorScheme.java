package com.writeoncereadmany.semantichighlighting.coloriser;


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
    private final Color seedColor;

    public ColorScheme(Color seedColor)
    {
        this.seedColor = seedColor;
    }

    public Color getSeedColor()
    {
        return seedColor;
    }

    public Color withHue(float hue)
    {
        final float[] hsb = new float[3];
        Color.RGBtoHSB(seedColor.getRed(), seedColor.getGreen(), seedColor.getBlue(), hsb);
        return Color.getHSBColor(hue, hsb[1], hsb[2]);
    }
}
