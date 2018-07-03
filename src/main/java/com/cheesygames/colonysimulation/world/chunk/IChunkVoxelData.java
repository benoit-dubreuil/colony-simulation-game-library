package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.math.direction.Direction3D;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;

public interface IChunkVoxelData {

    VoxelType getVoxelAt(int x, int y, int z);

    default VoxelType getVoxelAt(Vector3i index) {
        return getVoxelAt(index.x, index.y, index.z);
    }

    VoxelType getSideVoxelAt(Direction3D direction, int x, int y);
}
