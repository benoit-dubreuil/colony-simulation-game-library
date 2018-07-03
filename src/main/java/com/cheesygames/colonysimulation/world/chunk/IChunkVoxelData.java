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
     * Gets a voxel from the side specified by its direction. The direction defines the origin of the coordinate system.
     *
     * @param direction The direction that defines the side. Orthogonal directions are accepted.
     * @param x     The X coordinate of the voxel to get. If the direction affects this coordinate's axis, then it is an offset from the direction's side.
     * @param y     The Y coordinate of the voxel to get. If the direction affects this coordinate's axis, then it is an offset from the direction's side.
     * @param z     The Z coordinate of the voxel to get. If the direction affects this coordinate's axis, then it is an offset from the direction's side.
     *
     * @return The voxel at the specified side of the chunk at the given coordinates, whilst keeping in mind that the direction defines the origin of the coordinate system.
     */
    VoxelType getVoxelFromSide(Direction3D direction, int x, int y, int z);
}
