package com.cheesygames.colonysimulation.world.raycast;

import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.World;
import com.cheesygames.colonysimulation.world.chunk.IChunkVoxelData;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;

import java.util.function.Function;

/**
 * Acts like a real-time {@link java.util.Iterator}. It is used by a {@link com.cheesygames.colonysimulation.math.bounding.VoxelRay} to continuously traverse the {@link World}
 * voxels in an efficient manner.
 * <p>
 * The user must override the {@link #applyReturnCondition(Vector3i)} method that acts as the break condition of the {@link com.cheesygames.colonysimulation.math.bounding.VoxelRay}
 * and as what to do whilst traversing the world. The break condition takes the absolute voxel index, the voxel type and returns a boolean that signifies if the voxel traversal
 * should stop (true) or not (false). Bear in mind to not modify the absolute index, as it will interfere will the ray cast.
 */
public abstract class AbstractVoxelRayCastContinuousTraverser implements Function<Vector3i, Boolean> {

    protected World m_world;
    protected Vector3i m_chunkIndex;
    protected IChunkVoxelData m_chunk;

    public AbstractVoxelRayCastContinuousTraverser() {
        m_chunkIndex = new Vector3i();
    }

    public AbstractVoxelRayCastContinuousTraverser(World world) {
        m_world = world;
        m_chunkIndex = new Vector3i();
    }

    @Override
    public Boolean apply(Vector3i absoluteVoxelIndex) {
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
     * Gets the voxel at the supplied voxel absolute (world) index. The absolute (world) index must be within the bounds of the member variable {@link #m_chunk}.
     *
     * @param absoluteVoxelIndex The absolute (world) index at which to get the voxel.
     *
     * @return The voxel at the supplied absolute (world) index.
     */
    protected VoxelType getVoxalAtAbsoluteVoxelIndex(Vector3i absoluteVoxelIndex) {
        return m_chunk.getVoxelAt(m_world.getVoxelRelativeIndexX(absoluteVoxelIndex.x),
            m_world.getVoxelRelativeIndexY(absoluteVoxelIndex.y),
            m_world.getVoxelRelativeIndexZ(absoluteVoxelIndex.z));
    }

    /**
     * Applies the return condition, which decides what to do with the traversed voxel and if the voxel traversal should continue.
     *
     * @param absoluteVoxelIndex The absolute (world) index at which to get the voxel.
     *
     * @return True if the voxel traversing should stop, false otherwise.
     */
    protected abstract boolean applyReturnCondition(Vector3i absoluteVoxelIndex);

    public World getWorld() {
        return m_world;
    }

    public void setWorld(World world) {
        m_world = world;
    }
}
