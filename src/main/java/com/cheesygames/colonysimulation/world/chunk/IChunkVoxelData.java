package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.math.direction.Direction3D;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;

public interface IChunkVoxelData {

    VoxelType getVoxelAt(int x, int y, int z);

    default VoxelType getVoxelAt(Vector3i index) {
        return getVoxelAt(index.x, index.y, index.z);
    }

    /**
     * Gets a voxel at the specified side of the chunk at the given coordinates.
     *
     * @param direction The direction that defines the side. Orthogonal directions are accepted.
     * @param sideX     The X index projected onto the 2D side of the chunk from the inside.
     * @param sideY     The Y index projected onto the 2D side of the chunk from the inside.
     *
     * @return The voxel at the specified side of the chunk at the given coordinates.
     */
    VoxelType getSideVoxelAt(Direction3D direction, int sideX, int sideY);
}
