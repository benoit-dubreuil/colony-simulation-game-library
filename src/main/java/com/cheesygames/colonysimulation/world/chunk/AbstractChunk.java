package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.GameGlobal;
import com.cheesygames.colonysimulation.math.vector.Vector3i;

/**
 * Holds universal data for any type of chunk.
 */
public abstract class AbstractChunk implements IChunk {

    protected Vector3i m_index;

    /**
     * Creates a {@link Chunk} object with a zero {@link Vector3i}.
     */
    public AbstractChunk() {
        this.m_index = new Vector3i();
    }

    /**
     * Creates a {@link Chunk} object.
     *
     * @param index The chunk's index in the world. The reference is kept as a member variable.
     */
    public AbstractChunk(Vector3i index) {
        this.m_index = index;
    }

    @Override
    public Vector3i getIndex() {
        return m_index;
    }

    @Override
    public Vector3i getSize() {
        return GameGlobal.world.getChunkSize();
    }

    /**
     * Sets the value of the index, not the reference.
     *
     * @param index The value to set to the index of this chunk.
     */
    public void setIndex(Vector3i index) {
        m_index.set(index);
    }
}
