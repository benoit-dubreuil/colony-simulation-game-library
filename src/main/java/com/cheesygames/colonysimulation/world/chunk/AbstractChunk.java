package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.math.MathExt;
import com.cheesygames.colonysimulation.math.vector.Vector3i;

public abstract class AbstractChunk implements IChunk {

    protected Vector3i m_index;
    protected Vector3i m_size;

    /**
     * Creates a {@link Chunk} object.
     *
     * @param index The chunk's index in the world.
     * @param sizeX The size of the chunk on the X axis. Must be a power of two.
     * @param sizeY The size of the chunk on the Y axis. Must be a power of two.
     * @param sizeZ The size of the chunk on the Z axis. Must be a power of two.
     */
    public AbstractChunk(Vector3i index, int sizeX, int sizeY, int sizeZ) {
        this.m_index = index;
        this.m_size= new Vector3i(sizeX, sizeY, sizeZ);

        assert MathExt.isPowerOfTwo(sizeX);
        assert MathExt.isPowerOfTwo(sizeY);
        assert MathExt.isPowerOfTwo(sizeZ);
    }

    /**
     * Creates a {@link Chunk} object.
     *
     * @param index The chunk's index in the world.
     * @param size The size of the chunk on all the X, Y and Z axes. Must be a power of two.
     */
    public AbstractChunk(Vector3i index, int size) {
        this.m_index = index;
        this.m_size = new Vector3i(size);

        assert MathExt.isPowerOfTwo(size);
    }

    @Override
    public Vector3i getIndex() {
        return m_index;
    }

    @Override
    public Vector3i getSize() {
        return m_size;
    }
}
