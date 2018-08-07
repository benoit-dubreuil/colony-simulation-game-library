package com.cheesygames.colonysimulation.reflection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Allows for a conventional way to query an enum's array of values without creating a new array each time.
 *
 * @param <E> The enum to cache its values.
 */
public interface IEnumCachedValues<E extends Enum<E>> {

    Map<Class<? extends Enum<?>>, Enum<?>[]> enumValues = new ConcurrentHashMap<>();

    /**
     * Cache the supplied enum class's values.
     *
     * @param enumClass The enum class to cache its values.
     */
    static void cacheValues(Class<? extends Enum<?>> enumClass) {
        // This check is done only because it is a concurrent hash map.
        if (!enumValues.containsKey(enumClass)) {
            enumValues.put(enumClass, enumClass.getEnumConstants());
        }
    }

    /**
     * Gets the cached array of the supplied enum values from the method values().
     *
     * @param <E>       The class (type) of the enum.
     * @param enumClass The class instance of the enum.
     *
     * @return A cached array containing all enum values for the supplied enum.
     */
    static <E extends Enum<E>> E[] getCachedValues(Class<E> enumClass) {
        return (E[]) enumValues.get(enumClass);
    }

    /**
     * Gets the cached array of this enum's values from the method values().
     *
     * @return A cached array containing all enum values for this enum.
     */
    default E[] getCachedValues() {
        return (E[]) enumValues.get(getClass());
    }
}
