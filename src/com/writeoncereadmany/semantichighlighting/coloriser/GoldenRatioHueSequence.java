package com.writeoncereadmany.semantichighlighting.coloriser;

public class GoldenRatioHueSequence implements ColorSequence
{
    private static float GOLDEN_RATIO = (float) ((1 + Math.sqrt(5)) / 2);
    private static float GOLDEN_RATIO_COMPLEMENT = 1 / GOLDEN_RATIO;

    @Override
    public float nextHue(float previousHue) {
        return previousHue + GOLDEN_RATIO_COMPLEMENT;
    }
}
