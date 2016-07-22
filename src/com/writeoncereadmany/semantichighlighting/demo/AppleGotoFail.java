package com.writeoncereadmany.semantichighlighting.demo;

public class AppleGotoFail
{

    public void processThing(int thing)
    {
        if(thing == 2)
            gotoFail();
        if(thing == 3)
            gotoFail();
        if(thing == 5)
            gotoFail();
        if(thing == 7)
            gotoFail();
            gotoFail();
        if(thing == 11)
            gotoFail();
        if(thing == 13)
            gotoFail();
    }

    public void gotoFail()
    {
        throw new RuntimeException();
    }
}