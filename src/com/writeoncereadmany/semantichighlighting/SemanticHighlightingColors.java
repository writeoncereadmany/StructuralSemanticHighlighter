package com.writeoncereadmany.semantichighlighting;

import com.writeoncereadmany.semantichighlighting.coloriser.ColorScheme;

import java.awt.*;

public class SemanticHighlightingColors
{
    public static final ColorScheme COLOR_SCHEME = new ColorScheme(new Color(255, 240, 128));

    public static final Color CLASS_COLOR = COLOR_SCHEME.getSeedColor();
    public static final Color PUBLIC_METHOD_COLOR = COLOR_SCHEME.withHue(0.3f);
    public static final Color PRIVATE_METHOD_COLOR = COLOR_SCHEME.withHue(0f);
    public static final Color CANONICAL_CONSTRUCTOR_COLOR = COLOR_SCHEME.withHue(0.65f);
    public static final Color AUXILIARY_CONSTRUCTOR_COLOR = COLOR_SCHEME.withHue(0.75f);
    public static final Color UNKNOWN_COLOR = new Color(255, 255, 255);
}
