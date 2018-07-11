package com.cheesygames.colonysimulation.world;

import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.chunk.Chunk;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;
import com.jme3.math.Vector3f;

/**
 * A basic {@link IWorldGenerator} that generates voxels according to a gradient. It simply creates one single chunk at (0, 0, 0).
 */
public class GradientWorldGenerator implements IWorldGenerator {

    private Vector3f m_gradient;
    private float m_gradientLengthSquared;

    public GradientWorldGenerator(Vector3f gradient) {
        this.m_gradient = new Vector3f(gradient);
        this.m_gradientLengthSquared = m_gradient.lengthSquared();
    }

    @Override
    public void generateWorld(World world) {
        Vector3i minIndex = new Vector3i(-2, -2, -2);
        Vector3i maxIndex = new Vector3i(2, 2, 2);

        for (int x = minIndex.x; x <= maxIndex.x; ++x) {
            for (int y = minIndex.y; y <= maxIndex.y; ++y) {
                for (int z = minIndex.z; z <= maxIndex.z; ++z) {
                    generateChunk(world, new Vector3i(x, y, z));
                }
            }
        }
    }

    @Override
    public void generateChunk(World world, Vector3i index) {
        Chunk chunk = new Chunk(index);
        chunk.generateData(this);

        world.getChunks().put(index, chunk);
    }

    @Override
    public VoxelType generateVoxel(int x, int y, int z) {
        float gradientValue = new Vector3f(x, y, z).dot(m_gradient) / m_gradientLengthSquared;

        return gradientValue < 1f ? VoxelType.SOLID : VoxelType.AIR;
    }
}
