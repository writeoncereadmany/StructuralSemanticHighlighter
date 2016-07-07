package com.writeoncereadmany.semantichighlighting;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.writeoncereadmany.semantichighlighting.coloriser.ColorScheme;

import java.awt.*;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/**
 * Created by tom on 23/06/2016.
 */
public class SemanticHighlightingColors
{
    public static final ColorScheme COLOR_SCHEME = new ColorScheme(new Color(255, 240, 128));

    public static final TextAttributesKey CLASS = TextAttributesKey.createTextAttributesKey("CLASS", new TextAttributes(COLOR_SCHEME.getSeedColor(), null, null, EffectType.BOXED, Font.PLAIN));
    public static final TextAttributesKey PUBLIC_METHOD = TextAttributesKey.createTextAttributesKey("PUBLIC_METHOD", new TextAttributes(COLOR_SCHEME.withHue(0.3f), null, null, EffectType.BOXED, Font.PLAIN));
    public static final TextAttributesKey PRIVATE_METHOD = TextAttributesKey.createTextAttributesKey("PRIVATE_METHOD", new TextAttributes(COLOR_SCHEME.withHue(0f), null, null, EffectType.BOXED, Font.PLAIN));
    public static final TextAttributesKey CANONICAL_CONSTRUCTOR = TextAttributesKey.createTextAttributesKey("CANONICAL_CONSTRUCTOR", new TextAttributes(COLOR_SCHEME.withHue(0.65f), null, null, EffectType.BOXED, Font.PLAIN));
    public static final TextAttributesKey AUXILIARY_CONSTRUCTOR = TextAttributesKey.createTextAttributesKey("AUXILIARY_CONSTRUCTOR", new TextAttributes(COLOR_SCHEME.withHue(0.75f), null, null, EffectType.BOXED, Font.PLAIN));
    public static final TextAttributesKey FROM_ANOTHER_FILE = TextAttributesKey.createTextAttributesKey("FROM_ANOTHER_FILE", new TextAttributes(COLOR_SCHEME.withHue(0.1f), null, null, EffectType.BOXED, Font.PLAIN));
    public static final TextAttributesKey UNKNOWN = TextAttributesKey.createTextAttributesKey("UNKNOWN", new TextAttributes(new Color(255, 255, 255), null, null, EffectType.BOXED, Font.PLAIN));
}
