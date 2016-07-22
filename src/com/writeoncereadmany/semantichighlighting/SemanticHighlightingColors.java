package com.writeoncereadmany.semantichighlighting;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.writeoncereadmany.semantichighlighting.coloriser.ColorScheme;

import java.awt.*;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class SemanticHighlightingColors
{
    public static final ColorScheme COLOR_SCHEME = new ColorScheme(new Color(255, 240, 128));

    public static final Color CLASS_COLOR = COLOR_SCHEME.getSeedColor();
    public static final Color PUBLIC_METHOD_COLOR = COLOR_SCHEME.withHue(0.3f);
    public static final Color PRIVATE_METHOD_COLOR = COLOR_SCHEME.withHue(0f);
    public static final Color CANONICAL_CONSTRUCTOR_COLOR = COLOR_SCHEME.withHue(0.65f);
    public static final Color AUXILIARY_CONSTRUCTOR_COLOR = COLOR_SCHEME.withHue(0.75f);
    public static final Color UNKNOWN_COLOR = new Color(255, 255, 255);

    public static final TextAttributesKey CLASS = TextAttributesKey.createTextAttributesKey("CLASS", new TextAttributes(CLASS_COLOR, null, null, EffectType.BOXED, Font.PLAIN));
    public static final TextAttributesKey PUBLIC_METHOD = TextAttributesKey.createTextAttributesKey("PUBLIC_METHOD", new TextAttributes(PUBLIC_METHOD_COLOR, null, null, EffectType.BOXED, Font.PLAIN));
    public static final TextAttributesKey PRIVATE_METHOD = TextAttributesKey.createTextAttributesKey("PRIVATE_METHOD", new TextAttributes(PRIVATE_METHOD_COLOR, null, null, EffectType.BOXED, Font.PLAIN));
    public static final TextAttributesKey CANONICAL_CONSTRUCTOR = TextAttributesKey.createTextAttributesKey("CANONICAL_CONSTRUCTOR", new TextAttributes(CANONICAL_CONSTRUCTOR_COLOR, null, null, EffectType.BOXED, Font.PLAIN));
    public static final TextAttributesKey AUXILIARY_CONSTRUCTOR = TextAttributesKey.createTextAttributesKey("AUXILIARY_CONSTRUCTOR", new TextAttributes(AUXILIARY_CONSTRUCTOR_COLOR, null, null, EffectType.BOXED, Font.PLAIN));
    public static final TextAttributesKey UNKNOWN = TextAttributesKey.createTextAttributesKey("UNKNOWN", new TextAttributes(UNKNOWN_COLOR, null, null, EffectType.BOXED, Font.PLAIN));
}
