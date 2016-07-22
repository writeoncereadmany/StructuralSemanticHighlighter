package com.writeoncereadmany.semantichighlighting.demo;

public class ForLoop
{
    private int accumulator = 0;

    public void accumulateATriangle(final int size)
    {
        System.out.println("About to add a triangle of size " + size);
        for(int i = 1; i <= size; i++)
        {
            System.out.println("On row " + i + " of " + size);
            accumulator += (i + size);
        }
        System.out.println("Just added a triangle of size " + size + ", total accumulated is now " + accumulator);
    }
}
