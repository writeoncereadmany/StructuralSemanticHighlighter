package com.writeoncereadmany.semantichighlighting.demo;

public class NestedFunctionCalls
{
    public String nestedConstruction()
    {
        return doStuff("a", doStuff("b", "c", doStuff("d", doStuff(doStuff("e"), "f")), "g"));
    }

    public String doStuff(String... args)
    {
        return String.join(", ", args);
    }
}
