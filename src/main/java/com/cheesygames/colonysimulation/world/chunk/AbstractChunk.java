package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.World;

public abstract class AbstractChunk implements IChunk {

    protected World m_world;
    protected Vector3i m_index;

    /**
     * Creates a {@link Chunk} object.
     *
     * @param world The world owning the chunk.
     * @param index The chunk's index in the world.
     */
    public AbstractChunk(World world, Vector3i index) {
        this.m_world = world;
        this.m_index = index;
    }

    @Override
    public Vector3i getIndex() {
        return m_index;
    }

    @Override
    public Vector3i getSize() {
        return m_world.getChunkSize();
    }
}
