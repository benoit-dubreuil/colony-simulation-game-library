package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.math.direction.Direction3D;
import com.cheesygames.colonysimulation.world.chunk.voxel.Voxel;

/**
 * A chunk that is totally empty and meant to be used as it is. It doesn't have an index. One of its purpose is to act as an adjacent chunk when building the mesh of a real chunk.
 */
public class EmptyChunk implements IChunkVoxelData {

    public static final EmptyChunk DEFAULT_EMPTY_CHUNK = new EmptyChunk();

    @Override
    public Voxel getVoxelAt(int x, int y, int z) {
        return Voxel.EMPTY_VOXEL;
    }

    @Override
    public Voxel getVoxelFromPositiveSide(Direction3D direction, int x, int y, int z) {
        return Voxel.EMPTY_VOXEL;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}
