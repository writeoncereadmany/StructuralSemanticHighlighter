package com.writeoncereadmany.semantichighlighting;

import java.util.function.Predicate;

/**
 * Created by tomj on 31/03/2017.
 */
public interface FunctionalDoobries {

    static <T> Predicate<T> not(Predicate<T> test) {
        return test.negate();
    }
}
