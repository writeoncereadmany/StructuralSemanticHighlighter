package com.writeoncereadmany.semantichighlighting.coloriser;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TextAttributesDescriptor
{
    public final String baseString;
    public final float baseColor;
    public final List<String> levels;
    public final boolean fade;
    public final boolean bold;
    public final boolean italic;

    public TextAttributesDescriptor(String baseString, float baseColor) {
        this(baseString, baseColor, Collections.emptyList(), false, false, false);
    }

    public TextAttributesDescriptor(String baseString, float baseColor, List<String> depth, boolean fade, boolean bold, boolean italic) {
        this.baseString = baseString;
        this.baseColor = baseColor;
        this.levels = depth;
        this.fade = fade;
        this.bold = bold;

        this.italic = italic;
    }

    public TextAttributesDescriptor addLevel(String levelName)
    {
        List<String> newListOfLevels = new ArrayList<>(levels);
        newListOfLevels.add(levelName);
        return new TextAttributesDescriptor(baseString, this.baseColor, newListOfLevels, this.fade, this.bold, this.italic);
    }

    public TextAttributesDescriptor fade()
    {
        return new TextAttributesDescriptor(baseString, this.baseColor, this.levels, true, this.bold, this.italic);
    }

    public TextAttributesDescriptor embolden()
    {
        return new TextAttributesDescriptor(baseString, this.baseColor, this.levels, this.fade, true, this.italic);
    }

    public TextAttributesDescriptor italicise()
    {
        return new TextAttributesDescriptor(baseString, this.baseColor, this.levels, this.fade, this.bold, true);
    }

    public int fontType() {
        return (bold ? Font.BOLD : Font.PLAIN) |
               (italic ? Font.ITALIC : Font.PLAIN);
    }

    @Override
    public String toString()
    {
        final StringBuilder stringBuilder = new StringBuilder(baseString);
        levels.forEach(level -> stringBuilder.append("_").append(level));
        stringBuilder.append("_ROTx").append(levels.size());
        if(fade) { stringBuilder.append("_DARK"); }
        if(bold) { stringBuilder.append("_BOLD"); }
        if(italic) { stringBuilder.append("_ITALIC"); }

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextAttributesDescriptor that = (TextAttributesDescriptor) o;
        return fade == that.fade &&
                bold == that.bold &&
                italic == that.italic &&
                Objects.equals(baseString, that.baseString) &&
                Objects.equals(baseColor, that.baseColor) &&
                Objects.equals(levels, that.levels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseString, baseColor, levels, fade, bold, italic);
    }
}
