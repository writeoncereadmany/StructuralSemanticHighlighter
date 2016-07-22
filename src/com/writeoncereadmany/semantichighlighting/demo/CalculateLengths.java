package com.writeoncereadmany.semantichighlighting.demo;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

public class CalculateLengths {

    public static Function<List<String>, Map<String, Integer>> getLengthCalcFunction()
    {
        return list -> list.stream().collect(Collectors.toMap(identity(), String::length));
    }
}
