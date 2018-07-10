package com.cheesygames.colonysimulation.world;

import com.cheesygames.colonysimulation.math.MathExt;
import com.cheesygames.colonysimulation.math.direction.Direction3D;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.chunk.*;
import com.jme3.math.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class World {

    private static final Vector3i DEFAULT_CHUNK_SIZE = new Vector3i(32, 32, 32);

    private Map<Vector3i, Chunk> m_chunks;
    private IMeshGenerator m_meshGenerator;
    private IWorldGenerator m_worldGenerator;
    private Vector3i m_chunkSize;

    public World() {
        this.m_chunks = new HashMap<>();
        this.m_meshGenerator = new BlockMeshGenerator();
        this.m_worldGenerator = new GradientWorldGenerator(new Vector3f(16, 16, 16));
        this.m_chunkSize = new Vector3i(DEFAULT_CHUNK_SIZE);

        assert MathExt.isPowerOfTwo(m_chunkSize.getX());
        assert MathExt.isPowerOfTwo(m_chunkSize.getY());
        assert MathExt.isPowerOfTwo(m_chunkSize.getZ());
    }

    public void generateWorld() {
        m_worldGenerator.generateWorld(this);
    }

    public void generateMeshes() {
        for (Chunk chunk : m_chunks.values()) {
            chunk.setMesh(m_meshGenerator.generateMesh(this, chunk));
        }
    }

    public Map<Vector3i, Chunk> getChunks() {
        return m_chunks;
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

    public Vector3i getChunkSize() {
        return m_chunkSize;
    }
}
