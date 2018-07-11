package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.GameGlobal;
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

    /**
     * Computes a new {@link Vector3i} that is the absolute position (first voxel index) of the chunk.
     *
     * @return A newly created {@link Vector3i} that is the absolute position (first voxel index) of the chunk.
     */
    default Vector3i computePositionIndex() {
        return new Vector3i(GameGlobal.world.getAbsoluteIndexX(getIndex().x, 0),
            GameGlobal.world.getAbsoluteIndexY(getIndex().y, 0),
            GameGlobal.world.getAbsoluteIndexZ(getIndex().z, 0));
    }

    @Override
    default VoxelType getVoxelFromPositiveSide(Direction3D direction, int x, int y, int z) {
        return getVoxelAt(direction.getDirectionX() * direction.getDirectionX() * (getSize().x - 1) + ((-(direction.getDirectionX() * direction.getDirectionX()) | 1) * x),
            direction.getDirectionY() * direction.getDirectionY() * (getSize().y - 1) + ((-(direction.getDirectionY() * direction.getDirectionY()) | 1) * y),
            direction.getDirectionZ() * direction.getDirectionZ() * (getSize().z - 1) + ((-(direction.getDirectionZ() * direction.getDirectionZ()) | 1) * z));
    }

    Vector3i getIndex();

    Vector3i getSize();
}
