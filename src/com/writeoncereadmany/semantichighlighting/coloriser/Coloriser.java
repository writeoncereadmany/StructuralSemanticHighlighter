package com.writeoncereadmany.semantichighlighting.coloriser;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Coloriser
{
    private static final Map<TextAttributesDescriptor, TextAttributesKey> cache = new HashMap<>();

    private static final ColorSequence colorSequence = new GoldenRatioHueSequence();
    private static final ColorScheme colorScheme = new ColorScheme(new Color(112, 96, 0), new Color(255, 240, 128));

    public static TextAttributesKey fromDescriptor(TextAttributesDescriptor descriptor) {
        if(!cache.containsKey(descriptor))
        {
            calculateTextAttributesKey(descriptor);
        }
        return cache.get(descriptor);
    }

    private static void calculateTextAttributesKey(TextAttributesDescriptor descriptor) {
        float color = times(descriptor.levels.size(), descriptor.baseColor, colorSequence::nextHue);
        TextAttributesKey key = TextAttributesKey.createTextAttributesKey(descriptor.toString(), new TextAttributes(colorScheme.withHue(color, descriptor.fade), null, null, EffectType.BOXED, descriptor.fontType()));
        cache.put(descriptor, key);
    }

    private static <T> T times(int count, T initialValue, Function<T, T> applicator)
    {
        if(count == 0)
        {
            return initialValue;
        }
        return times(count - 1, applicator.apply(initialValue), applicator);
    }
}
