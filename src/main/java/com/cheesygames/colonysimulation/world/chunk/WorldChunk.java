package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.math.MathExt;

/**
 * A world chunk consisting of voxels. Its size on all X, Y and Z axes must be a power of 2.
 */
public class WorldChunk {

    private int sizeX;
    private int sizeY;
    private int sizeZ;

    /**
     * Creates a {@link WorldChunk} object.
     *
     * @param sizeX The size of the chunk on the X axis. Must be a power of two.
     * @param sizeY The size of the chunk on the Y axis. Must be a power of two.
     * @param sizeZ The size of the chunk on the Z axis. Must be a power of two.
     */
    public WorldChunk(int sizeX, int sizeY, int sizeZ) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;

        assert MathExt.isPowerOfTwo(sizeX);
        assert MathExt.isPowerOfTwo(sizeY);
        assert MathExt.isPowerOfTwo(sizeZ);
    }

    /**
     * Creates a {@link WorldChunk} object.
     *
     * @param size The size of the chunk on all the X, Y and Z axes. Must be a power of two.
     */
    public WorldChunk(int size) {
        this.sizeX = size;
        this.sizeY = size;
        this.sizeZ = size;

        assert MathExt.isPowerOfTwo(size);
    }
}
