package com.cheesygames.colonysimulation.lambda;

/**
 * Same as {@link java.util.function.BiFunction} but with 3 parameters instead of 2.
 *
 * @param <P1> The type of the first argument to the function.
 * @param <P2> The type of the second argument to the function.
 * @param <P3> The type of the third argument to the function.
 * @param <R>  The type of the result of the function.
 */
@FunctionalInterface
public interface TriFunction<P1, P2, P3, R> {

    /**
     * Applies this function to then given arguments.
     *
     * @param param1 The first function argument.
     * @param param2 The second function argument.
     * @param param3 The third function argument.
     *
     * @return The function result.
     */
    R apply(P1 param1, P2 param2, P3 param3);
}
