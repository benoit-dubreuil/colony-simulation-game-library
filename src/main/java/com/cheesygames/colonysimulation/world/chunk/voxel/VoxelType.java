package com.cheesygames.colonysimulation.world.chunk.voxel;

/**
 * All voxel types.
 */
public enum VoxelType {
    AIR {
        @Override
        public boolean isSolid() {
            return false;
        }
    },
    SOLID;

    public boolean isSolid() {
        return true;
    }
}
