package com.cheesygames.colonysimulation.world.chunk.voxel;

/**
 * Data holder for a single voxel.
 */
public class Voxel {

    public static final Voxel EMPTY_VOXEL = new Voxel();

    public VoxelType voxelType;
    public int light;

    public Voxel() {
        voxelType = VoxelType.AIR;
    }

    public Voxel(VoxelType voxelType) {
        this.voxelType = voxelType;
    }
}
