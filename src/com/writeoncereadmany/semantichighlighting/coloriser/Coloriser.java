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

    public static TextAttributesKey fromDescriptor(TextAttributesDescriptor descriptor) {
        if(!cache.containsKey(descriptor))
        {
            calculateTextAttributesKey(descriptor);
        }
        return cache.get(descriptor);
    }

    private static void calculateTextAttributesKey(TextAttributesDescriptor descriptor) {
        Color color = times(descriptor.levels.size(), descriptor.baseColor, colorSequence::nextColor);
        if(descriptor.fade)
        {
            color = fadeTo(color, 0.65);
        }
        TextAttributesKey key = TextAttributesKey.createTextAttributesKey(descriptor.toString(), new TextAttributes(color, null, null, EffectType.BOXED, descriptor.fontType()));
        cache.put(descriptor, key);
    }

    @NotNull
    private static Color fadeTo(Color color, double fadeFactor) {
        return new Color((int)(color.getRed() * fadeFactor), (int)(color.getGreen() * fadeFactor), (int)(color.getBlue() * fadeFactor));
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
