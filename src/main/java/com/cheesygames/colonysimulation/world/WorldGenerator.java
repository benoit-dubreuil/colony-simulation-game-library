package com.cheesygames.colonysimulation.world;

import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.chunk.Chunk;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;

import java.util.Map;

// TODO
public class WorldGenerator {

    public void generateWorld(Map<Vector3i, Chunk> chunks) {
        Vector3i index = new Vector3i();
        generateChunk(chunks, index);
    }

    private void generateChunk(Map<Vector3i, Chunk> chunks, Vector3i index) {
        Chunk chunk = new Chunk(index, 32);
        chunk.generateData(this);

        chunks.put(index, chunk);
    }

    public VoxelType generateVoxel(int x, int y, int z) {
        return VoxelType.SOLID;
    }
}
