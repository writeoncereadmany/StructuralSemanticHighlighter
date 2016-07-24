package com.writeoncereadmany.semantichighlighting.coloriser;

import java.awt.*;

public interface ColorSequence
{
    Color nextColor(Color previousColor);
}
