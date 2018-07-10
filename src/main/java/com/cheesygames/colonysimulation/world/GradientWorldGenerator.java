package com.cheesygames.colonysimulation.world;

import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.chunk.Chunk;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;
import com.jme3.math.Vector3f;

import java.util.Map;

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
        Vector3i index = new Vector3i();
        generateChunk(world, index);
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
