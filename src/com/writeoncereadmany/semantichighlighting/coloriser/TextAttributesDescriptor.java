package com.writeoncereadmany.semantichighlighting.coloriser;

import java.awt.*;
import java.util.Objects;

public class TextAttributesDescriptor
{
    public final String baseString;
    public final Color baseColor;
    public final int depth;
    public final boolean fade;
    public final boolean bold;
    public final boolean italic;

    public TextAttributesDescriptor(String baseString, Color baseColor) {
        this(baseString, baseColor, 0, false, false, false);
    }

    public TextAttributesDescriptor(String baseString, Color baseColor, int depth, boolean fade, boolean bold, boolean italic) {
        this.baseString = baseString;
        this.baseColor = baseColor;
        this.depth = depth;
        this.fade = fade;
        this.bold = bold;

        this.italic = italic;
    }

    public TextAttributesDescriptor addLevel()
    {
        return new TextAttributesDescriptor(baseString, this.baseColor, this.depth + 1, this.fade, this.bold, this.italic);
    }

    public TextAttributesDescriptor fade()
    {
        return new TextAttributesDescriptor(baseString, this.baseColor, this.depth, true, this.bold, this.italic);
    }

    public TextAttributesDescriptor embolden()
    {
        return new TextAttributesDescriptor(baseString, this.baseColor, this.depth, this.fade, true, this.italic);
    }

    public TextAttributesDescriptor italicise()
    {
        return new TextAttributesDescriptor(baseString, this.baseColor, this.depth, this.fade, this.bold, true);
    }

    @Override
    public String toString()
    {
        final StringBuilder stringBuilder = new StringBuilder(baseString);
        stringBuilder.append("_ROTx").append(depth);
        if(fade) { stringBuilder.append("_DARK"); }
        if(bold) { stringBuilder.append("_BOLD"); }
        if(italic) { stringBuilder.append("_ITALIC"); }

        return stringBuilder.toString();
    }

    public int fontType() {
        return (bold ? Font.BOLD : Font.PLAIN) &
               (italic ? Font.ITALIC : Font.PLAIN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextAttributesDescriptor that = (TextAttributesDescriptor) o;
        return depth == that.depth &&
                fade == that.fade &&
                bold == that.bold &&
                italic == that.italic &&
                Objects.equals(baseString, that.baseString) &&
                Objects.equals(baseColor, that.baseColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseString, baseColor, depth, fade, bold, italic);
    }
}
