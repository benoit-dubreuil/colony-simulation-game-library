package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.math.MathExt;
import com.cheesygames.colonysimulation.math.direction.Direction3D;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;

/**
 * Interface for a world chunk. A chunk supposedly holds a 3D array of voxel data.
 */
public interface IChunk extends IChunkVoxelData {

    // TODO
    @Override
    default VoxelType getSideVoxelAt(Direction3D direction, int x, int y) {
        return getVoxelAt(MathExt.indexifyNormal(direction.getDirectionX()) * getSize().x - 1,
            MathExt.indexifyNormal(direction.getDirectionY()) * getSize().y - 1,
            MathExt.indexifyNormal(direction.getDirectionZ()) * getSize().z - 1);
    }

    Vector3i getIndex();

    Vector3i getSize();
}
