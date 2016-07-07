package com.writeoncereadmany.semantichighlighting.coloriser;

import java.awt.*;

public class GoldenRatioHueSequence implements ColorSequence
{
    private static float GOLDEN_RATIO = (float) ((1 + Math.sqrt(5)) / 2);
    private static float GOLDEN_RATIO_COMPLEMENT = 1 / GOLDEN_RATIO;

    @Override
    public Color nextColor(Color previousColor)
    {
        final float[] hsb = new float[3];
        Color.RGBtoHSB(previousColor.getRed(), previousColor.getGreen(), previousColor.getBlue(), hsb);
        hsb[0] = hsb[0] + GOLDEN_RATIO_COMPLEMENT;
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }
}
