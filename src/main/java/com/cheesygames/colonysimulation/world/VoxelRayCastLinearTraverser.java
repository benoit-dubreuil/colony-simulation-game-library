package com.cheesygames.colonysimulation.world;

import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.chunk.Chunk;
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
public class VoxelRayCastLinearTraverser implements Function<Vector3i, Boolean> {

    private World m_world;
    private Vector3i m_chunkIndex;
    private Chunk m_chunk;
    private BiFunction<Vector3i, VoxelType, Boolean> m_returnCondition;

    public VoxelRayCastLinearTraverser() {
        m_chunkIndex = new Vector3i();
    }

    public VoxelRayCastLinearTraverser(World world) {
        m_world = world;
        m_chunkIndex = new Vector3i();
    }

    public VoxelRayCastLinearTraverser(BiFunction<Vector3i, VoxelType, Boolean> returnCondition) {
        m_chunkIndex = new Vector3i();
        m_returnCondition = returnCondition;
    }

    public VoxelRayCastLinearTraverser(World world, BiFunction<Vector3i, VoxelType, Boolean> returnCondition) {
        m_world = world;
        m_chunkIndex = new Vector3i();
        m_returnCondition = returnCondition;
    }

    @Override
    public Boolean apply(Vector3i absoluteVoxelIndex) {
        int oldChunkIndexX = m_chunkIndex.x;
        int oldChunkIndexY = m_chunkIndex.y;
        int oldChunkIndexZ = m_chunkIndex.z;

        m_world.getChunkIndexLocal(absoluteVoxelIndex, m_chunkIndex);

        if (!m_chunkIndex.equals(oldChunkIndexX, oldChunkIndexY, oldChunkIndexZ) || m_chunk == null) {
            m_chunk = m_world.getChunkAt(m_chunkIndex);
        }

        return m_returnCondition.apply(absoluteVoxelIndex,
            m_chunk.getVoxelAt(m_world.getVoxelRelativeIndexX(absoluteVoxelIndex.x),
                m_world.getVoxelRelativeIndexY(absoluteVoxelIndex.y),
                m_world.getVoxelRelativeIndexZ(absoluteVoxelIndex.z)));
    }

    public World getWorld() {
        return m_world;
    }

    public void setWorld(World world) {
        m_world = world;
    }

    public BiFunction<Vector3i, VoxelType, Boolean> getReturnCondition() {
        return m_returnCondition;
    }

    public void setReturnCondition(BiFunction<Vector3i, VoxelType, Boolean> returnCondition) {
        m_returnCondition = returnCondition;
    }
}
