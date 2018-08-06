package com.cheesygames.colonysimulation.math.bounding.ray;

import com.cheesygames.colonysimulation.math.vector.Vector3i;

import java.util.function.Function;

/**
 * Defines an interface for {@link VoxelRay#} ray cast return condition.
 */
public interface VoxelRayOnTraversing extends Function<Vector3i, Boolean> {

    /**
     * Allows the return condition to do some preparations when the ray cast is starting.
     */
    void startRayCast();
}
