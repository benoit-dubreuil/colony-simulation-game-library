package com.cheesygames.colonysimulation.world;

import com.cheesygames.colonysimulation.math.direction.Direction3D;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.chunk.Chunk;
import com.cheesygames.colonysimulation.world.chunk.EmptyChunk;
import com.cheesygames.colonysimulation.world.chunk.IChunkVoxelData;

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

    public Map<Direction3D, IChunkVoxelData> getAdjacentChunks(Vector3i index) {
        Map<Direction3D, IChunkVoxelData> adjacentChunks = new HashMap<>(Direction3D.ORTHOGONALS.length);
        Vector3i adjacentChunkIndex = new Vector3i();

        for (Direction3D direction : Direction3D.ORTHOGONALS) {
            adjacentChunkIndex.set(index);
            IChunkVoxelData chunk = m_chunks.get(adjacentChunkIndex.addLocal(direction.getDirection()));
            adjacentChunks.put(direction, chunk != null ? chunk : EmptyChunk.DEFAULT_EMPTY_CHUNK);
        }

        return adjacentChunks;
    }
}
