package com.writeoncereadmany.semantichighlighting.util;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Created by tom on 24/07/2016.
 */
public class Optionals
{
    public static <T> Stream<T> stream(Optional<T> maybe)
    {
        return fold(maybe, Stream::of, Stream::empty);
    }

    public static <T, R> R fold(Optional<T> maybe, Function<T, R> onPresent, Supplier<R> onAbsent)
    {
        return maybe.map(onPresent).orElseGet(onAbsent);
    }
}
