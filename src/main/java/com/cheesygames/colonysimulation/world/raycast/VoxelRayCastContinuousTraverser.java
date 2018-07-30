package com.cheesygames.colonysimulation.world.raycast;

import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.World;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;

import java.util.function.BiFunction;

/**
 * Acts like a real-time {@link java.util.Iterator}. It is used by a {@link com.cheesygames.colonysimulation.math.bounding.VoxelRay} to continuously traverse the {@link World}
 * voxels in an efficient manner.
 * <p>
 * The user supplies a {@link BiFunction} that acts as the break condition of the {@link com.cheesygames.colonysimulation.math.bounding.VoxelRay} and as what to do whilst
 * traversing the world. The break condition takes the absolute voxel index, the voxel type and returns a boolean that signifies if the voxel traversal should stop (true) or not
 * (false). Bear in mind to not modify the absolute index, as it will interfere will the ray cast.
 */
public class VoxelRayCastContinuousTraverser extends AbstractVoxelRayCastContinuousTraverser {

    protected BiFunction<Vector3i, VoxelType, Boolean> m_returnCondition;

    public VoxelRayCastContinuousTraverser() {
        super();
    }

    public VoxelRayCastContinuousTraverser(World world) {
        super(world);
    }

    public VoxelRayCastContinuousTraverser(BiFunction<Vector3i, VoxelType, Boolean> returnCondition) {
        super();
        m_returnCondition = returnCondition;
    }

    public VoxelRayCastContinuousTraverser(World world, BiFunction<Vector3i, VoxelType, Boolean> returnCondition) {
        super(world);
        m_returnCondition = returnCondition;
    }

    @Override
    protected boolean applyReturnCondition(Vector3i absoluteVoxelIndex) {
        return m_returnCondition.apply(absoluteVoxelIndex, getVoxalAtAbsoluteVoxelIndex(absoluteVoxelIndex));
    }

    public BiFunction<Vector3i, VoxelType, Boolean> getReturnCondition() {
        return m_returnCondition;
    }

    public void setReturnCondition(BiFunction<Vector3i, VoxelType, Boolean> returnCondition) {
        m_returnCondition = returnCondition;
    }
}
