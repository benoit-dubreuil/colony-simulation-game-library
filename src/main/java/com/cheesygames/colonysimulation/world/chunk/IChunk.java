package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.GameGlobal;
import com.cheesygames.colonysimulation.math.MathExt;
import com.cheesygames.colonysimulation.math.direction.Direction3D;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;
import com.jme3.math.Vector3f;

/**
 * Interface for a world chunk. A chunk supposedly holds a 3D array of voxel data and has an index.
 */
public interface IChunk extends IChunkVoxelData {

    /**
     * Computes a new {@link Vector3f} that is the absolute center of the chunk.
     *
     * @return A newly created {@link Vector3f} that is the absolute center of the chunk.
     */
    default Vector3f computeCenter() {
        return new Vector3f(GameGlobal.world.getAbsoluteIndexX(getIndex().x, 0) + getSize().x / 2f + 0.5f,
            GameGlobal.world.getAbsoluteIndexY(getIndex().y, 0) + getSize().y / 2f + 0.5f,
            GameGlobal.world.getAbsoluteIndexZ(getIndex().z, 0) + getSize().z / 2f + 0.5f);
    }

    @Override
    default VoxelType getVoxelFromSide(Direction3D direction, int x, int y, int z) {
        return getVoxelAt((1 - MathExt.indexifyNormalZeroPositive(direction.getDirectionX())) * (getSize().x - 1) + ((direction.getDirectionX() | 1) * x),
            (1 - MathExt.indexifyNormalZeroPositive(direction.getDirectionY())) * (getSize().y - 1) + ((direction.getDirectionY() | 1) * y),
            (1 - MathExt.indexifyNormalZeroPositive(direction.getDirectionZ())) * (getSize().z - 1) + ((direction.getDirectionZ() | 1) * z));
    }

    Vector3i getIndex();

    Vector3i getSize();
}
