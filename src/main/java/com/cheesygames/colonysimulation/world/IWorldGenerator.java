package com.cheesygames.colonysimulation.world;

import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;

/**
 * Interface for world generators. Allows the user to generate a world, a chunk and a simple voxel.
 */
public interface IWorldGenerator {

    void generateWorld(World world);

    void generateChunk(World world, Vector3i index);

    VoxelType generateVoxel(int x, int y, int z);
}
