package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.math.MathExt;
import com.cheesygames.colonysimulation.math.direction.Direction3D;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;

/**
 * Interface for a world chunk. A chunk supposedly holds a 3D array of voxel data and has an index.
 */
public interface IChunk extends IChunkVoxelData {

    @Override
    default VoxelType getVoxelFromSide(Direction3D direction, int x, int y, int z) {
        return getVoxelAt((1 - MathExt.indexifyNormalZeroPositive(direction.getDirectionX())) * (getSize().x - 1) + ((direction.getDirectionX() | 1) * x),
            (1 - MathExt.indexifyNormalZeroPositive(direction.getDirectionY())) * (getSize().y - 1) + ((direction.getDirectionY() | 1) * y),
            (1 - MathExt.indexifyNormalZeroPositive(direction.getDirectionZ())) * (getSize().z - 1) + ((direction.getDirectionZ() | 1) * z));
    }

    Vector3i getIndex();

    Vector3i getSize();
}
