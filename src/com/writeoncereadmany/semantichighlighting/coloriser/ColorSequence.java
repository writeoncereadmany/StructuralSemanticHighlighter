package com.writeoncereadmany.semantichighlighting.coloriser;

import java.awt.*;

public interface ColorSequence
{
    public Color nextColor(Color previousColor);
}
