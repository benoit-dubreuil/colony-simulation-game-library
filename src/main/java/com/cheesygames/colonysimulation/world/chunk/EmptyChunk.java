package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.math.direction.Direction3D;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;

public class EmptyChunk implements IChunkVoxelData {

    public static final EmptyChunk DEFAULT_EMPTY_CHUNK = new EmptyChunk();

    @Override
    public VoxelType getVoxelAt(int x, int y, int z) {
        return VoxelType.AIR;
    }

    @Override
    public VoxelType getVoxelFromSide(Direction3D direction, int x, int y, int z) {
        return VoxelType.AIR;
    }
}
