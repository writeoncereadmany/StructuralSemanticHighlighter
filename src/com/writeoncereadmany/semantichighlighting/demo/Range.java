package com.writeoncereadmany.semantichighlighting.demo;

public class Range
{
    private final int start;
    private final int end;

    public Range(int start, int end)
    {
        if(start >= end)
        {
            throw new IllegalArgumentException("Start must come before end");
        }
        this.start = start;
        this.end = end;
    }

    public Range(int count)
    {
        this(1, count);
    }

    public Range()
    {
        this.start = 0;
        this.end = 0;
    }

    public int size()
    {
        return calculateSize();
    }

    private int calculateSize()
    {
        return end - start;
    }
}
