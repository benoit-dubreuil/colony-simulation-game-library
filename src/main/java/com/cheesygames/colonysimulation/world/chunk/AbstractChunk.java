package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.GameGlobal;
import com.cheesygames.colonysimulation.math.vector.Vector3i;

public abstract class AbstractChunk implements IChunk {

    protected Vector3i m_index;

    /**
     * Creates a {@link Chunk} object.
     *
     * @param index The chunk's index in the world.
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
}
