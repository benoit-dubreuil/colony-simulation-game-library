package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.math.direction.Direction3D;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;

/**
 * Interface for a data structure that act like a chunk. It can either be a real chunk or a fake one. As example for a fake chunk, see {@link EmptyChunk}.
 */
public interface IChunkVoxelData {

    VoxelType getVoxelAt(int x, int y, int z);

    default VoxelType getVoxelAt(Vector3i index) {
        return getVoxelAt(index.x, index.y, index.z);
    }

    /**
     * Gets a voxel from the side specified by the absolute direction, i.e. negative directions are converted to positive directions. The direction defines the origin of the
     * coordinate system.
     *
     * @param direction The direction that defines the side. Orthogonal directions are accepted.
     * @param x         The X coordinate of the voxel to get. If the direction affects this coordinate's axis, then it is an offset from the absolute direction's side.
     * @param y         The Y coordinate of the voxel to get. If the direction affects this coordinate's axis, then it is an offset from the absolute direction's side.
     * @param z         The Z coordinate of the voxel to get. If the direction affects this coordinate's axis, then it is an offset from the absolute direction's side.
     *
     * @return The voxel at the specified side of the chunk at the given coordinates, whilst keeping in mind that the absolute direction defines the origin of the coordinate
     * system.
     */
    VoxelType getVoxelFromPositiveSide(Direction3D direction, int x, int y, int z);

    /**
     * Checks if the chunk is empty, i.e. if it's filled with {@link VoxelType#AIR}.
     *
     * @return True if the chunk is empty, false otherwise.
     */
    boolean isEmpty();
}
