package com.cheesygames.colonysimulation.world;

import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.chunk.Chunk;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;
import com.jme3.math.Vector3f;

import java.util.Map;

// TODO
public class WorldGenerator {

    public void generateWorld(World world) {
        Vector3i index = new Vector3i();
        generateChunk(world, index);
    }

    private void generateChunk(World world, Vector3i index) {
        Chunk chunk = new Chunk(world, index);
        chunk.generateData(this);

        world.getChunks().put(index, chunk);
    }

    public VoxelType generateVoxel(int x, int y, int z) {
        Vector3f gradient = new Vector3f(-16, 16, -16);
        float gradientValue = new Vector3f(x, y, z).addLocal(-32, 0, -32).dot(gradient) / gradient.dot(gradient);

        return gradientValue < 1f ? VoxelType.SOLID : VoxelType.AIR;
    }
}
