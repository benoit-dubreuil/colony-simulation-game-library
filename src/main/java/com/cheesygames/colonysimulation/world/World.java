package com.cheesygames.colonysimulation.world;

import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.chunk.Chunk;

import java.util.HashMap;
import java.util.Map;

public class World {

    private Map<Vector3i, Chunk> m_chunks;

    public World() {
        this.m_chunks = new HashMap<>();
    }

    public Chunk getChunkAt(Vector3i index) {
        return m_chunks.get(index);
    }
}
