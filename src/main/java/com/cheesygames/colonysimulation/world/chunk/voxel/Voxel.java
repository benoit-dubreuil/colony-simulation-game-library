package com.cheesygames.colonysimulation.world.chunk.voxel;

/**
 * Data holder for a single voxel.
 */
public class Voxel {

    public VoxelType voxelType;
    public int light;

    public Voxel() {
        voxelType = VoxelType.AIR;
    }
}
