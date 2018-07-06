package com.cheesygames.colonysimulation.world;

import com.cheesygames.colonysimulation.math.direction.Direction3D;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.chunk.*;

import java.util.HashMap;
import java.util.Map;

public class World {

    private Map<Vector3i, Chunk> m_chunks;
    private IMeshGenerator m_meshGenerator;
    private WorldGenerator m_worldGenerator;

    public World() {
        this.m_chunks = new HashMap<>();
        this.m_meshGenerator = new BlockMeshGenerator();
        this.m_worldGenerator = new WorldGenerator();
    }

    public void generateWorld() {
        m_worldGenerator.generateWorld(m_chunks);
    }

    public void generateMeshes() {
        for (Chunk chunk : m_chunks.values()) {
            chunk.setMesh(m_meshGenerator.generateMesh(this, chunk));
        }
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
