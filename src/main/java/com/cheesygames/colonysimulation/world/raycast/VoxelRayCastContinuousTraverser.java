package com.cheesygames.colonysimulation.world.raycast;

import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.World;
import com.cheesygames.colonysimulation.world.chunk.IChunkVoxelData;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Acts like a real-time {@link java.util.Iterator}. It is used by a {@link com.cheesygames.colonysimulation.math.bounding.VoxelRay} to continuously traverse the {@link World}
 * voxels in an efficient manner.
 * <p>
 * The user supplies a {@link BiFunction} that acts as the break condition of the {@link com.cheesygames.colonysimulation.math.bounding.VoxelRay} and as what to do whilst
 * traversing the world. The break condition takes the absolute voxel index, the voxel type and returns a boolean that signifies if the voxel traversal should stop (true) or not
 * (false). Bear in mind to not modify the absolute index, as it will interfere will the ray cast.
 */
public class VoxelRayCastContinuousTraverser implements Function<Vector3i, Boolean> {

    protected World m_world;
    protected Vector3i m_chunkIndex;
    protected IChunkVoxelData m_chunk;
    protected Vector3i m_relativeVoxelIndex;
    protected BiFunction<Vector3i, VoxelType, Boolean> m_returnCondition;

    public VoxelRayCastContinuousTraverser() {
        m_chunkIndex = new Vector3i();
        m_relativeVoxelIndex = new Vector3i();
    }

    public VoxelRayCastContinuousTraverser(World world) {
        this();
        m_world = world;
    }

    public VoxelRayCastContinuousTraverser(BiFunction<Vector3i, VoxelType, Boolean> returnCondition) {
        this();
        m_returnCondition = returnCondition;
    }

    public VoxelRayCastContinuousTraverser(World world, BiFunction<Vector3i, VoxelType, Boolean> returnCondition) {
        this(world);
        m_returnCondition = returnCondition;
    }

    @Override
    public Boolean apply(Vector3i absoluteVoxelIndex) {
        m_world.getVoxelRelativeIndexLocal(absoluteVoxelIndex, m_relativeVoxelIndex);
        detectChunk(absoluteVoxelIndex);

        return applyReturnCondition(absoluteVoxelIndex);
    }

    /**
     * Detects the chunk in which the traversing voxel is. If it is different from the precedent chunk or if there is no precedent chunk, then set it according to the absolute
     * (world) voxel index's parent chunk.
     *
     * @param absoluteVoxelIndex The absolute (world) index from which to get the chunk.
     */
    protected void detectChunk(Vector3i absoluteVoxelIndex) {
        int oldChunkIndexX = m_chunkIndex.x;
        int oldChunkIndexY = m_chunkIndex.y;
        int oldChunkIndexZ = m_chunkIndex.z;

        m_world.getChunkIndexLocal(absoluteVoxelIndex, m_chunkIndex);

        if (!m_chunkIndex.equals(oldChunkIndexX, oldChunkIndexY, oldChunkIndexZ) || m_chunk == null) {
            m_chunk = m_world.getOrEmptyChunkAt(m_chunkIndex);
        }
    }

    /**
     * Applies the return condition, which decides what to do with the traversed voxel and if the voxel traversal should continue.
     *
     * @param absoluteVoxelIndex The absolute (world) index at which to get the voxel.
     *
     * @return True if the voxel traversing should stop, false otherwise.
     */
    protected boolean applyReturnCondition(Vector3i absoluteVoxelIndex) {
        return m_returnCondition.apply(absoluteVoxelIndex, m_chunk.getVoxelAt(m_relativeVoxelIndex));
    }

    public BiFunction<Vector3i, VoxelType, Boolean> getReturnCondition() {
        return m_returnCondition;
    }

    public void setReturnCondition(BiFunction<Vector3i, VoxelType, Boolean> returnCondition) {
        m_returnCondition = returnCondition;
    }

    public World getWorld() {
        return m_world;
    }

    public void setWorld(World world) {
        m_world = world;
    }

    /**
     * Gets the last traversed voxel's chunk index. By last voxel, it means that it can locally change for each traversed voxel.
     *
     * @return The last traversed voxel's chunk index.
     */
    public Vector3i getChunkIndex() {
        return m_chunkIndex;
    }

    /**
     * Gets the last traversed voxel's chunk. By last voxel, it means that it can locally change for each traversed voxel.
     *
     * @return The last traversed voxel's chunk.
     */
    public IChunkVoxelData getChunk() {
        return m_chunk;
    }

    /**
     * Gets the currently traversing voxel's (or last traversed voxel if the ray cast has finished) chunk relative index. Do not modify the returned value, as it is changed locally
     * for each traversed voxel.
     *
     * @return The currently traversing voxel's chunk relative index.
     */
    public Vector3i getRelativeVoxelIndex() {
        return m_relativeVoxelIndex;
    }
}
