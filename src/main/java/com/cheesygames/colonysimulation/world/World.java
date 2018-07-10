package com.cheesygames.colonysimulation.world;

import com.cheesygames.colonysimulation.math.MathExt;
import com.cheesygames.colonysimulation.math.direction.Direction3D;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.chunk.*;
import com.jme3.math.Vector3f;

import java.util.HashMap;
import java.util.Map;

/**
 * A world holds multiple chunks of voxels.
 */
public class World {

    private static final Vector3i DEFAULT_CHUNK_SIZE_BITS = new Vector3i(5, 5, 5);
    private static final Vector3i DEFAULT_CHUNK_SIZE = new Vector3i(1 << DEFAULT_CHUNK_SIZE_BITS.x, 1 << DEFAULT_CHUNK_SIZE_BITS.y, 1 << DEFAULT_CHUNK_SIZE_BITS.z);

    private Map<Vector3i, Chunk> m_chunks;
    private IChunkMeshGenerator m_meshGenerator;
    private IWorldGenerator m_worldGenerator;
    private Vector3i m_chunkSizeBits;
    private Vector3i m_chunkSize;

    public World() {
        this.m_chunks = new HashMap<>();
        this.m_meshGenerator = new BlockMeshGenerator();
        this.m_worldGenerator = new GradientWorldGenerator(new Vector3f(-2, 16, -5));
        this.m_chunkSizeBits = new Vector3i(DEFAULT_CHUNK_SIZE_BITS);
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

    /**
     * Gets the absolute index of a chunk relative position according to its chunk's index. In other words, it gives an integer world position.
     *
     * @param chunkIndex         The chunk's index.
     * @param chunkRelativeIndex The chunk relative index, a.k.a. the index in the chunk's data multidimensional array.
     * @param chunkSizeBits      The bit shift count for the chunk size. This allows to not mention the axis.
     *
     * @retur The absolute index.
     */
    private int getAbsoluteIndex(int chunkIndex, int chunkRelativeIndex, int chunkSizeBits) {
        return (chunkIndex << chunkSizeBits) + chunkRelativeIndex;
    }

    /**
     * Gets the absolute index of a chunk relative position according to its chunk's index and the specified axis. In other words, it gives an integer world position on an axis.
     *
     * @param chunkIndex         The chunk's index for the specified axis.
     * @param chunkRelativeIndex The chunk relative index, a.k.a. the axis index in the chunk's data multidimensional array.
     *
     * @retur The absolute index for the specified axis.
     */
    public int getAbsoluteIndexX(int chunkIndex, int chunkRelativeIndex) {
        return getAbsoluteIndex(chunkIndex, chunkRelativeIndex, m_chunkSizeBits.x);
    }

    /**
     * Gets the absolute index of a chunk relative position according to its chunk's index and the specified axis. In other words, it gives an integer world position on an axis.
     *
     * @param chunkIndex         The chunk's index for the specified axis.
     * @param chunkRelativeIndex The chunk relative index, a.k.a. the axis index in the chunk's data multidimensional array.
     *
     * @retur The absolute index for the specified axis.
     */
    public int getAbsoluteIndexY(int chunkIndex, int chunkRelativeIndex) {
        return getAbsoluteIndex(chunkIndex, chunkRelativeIndex, m_chunkSizeBits.y);
    }

    /**
     * Gets the absolute index of a chunk relative position according to its chunk's index and the specified axis. In other words, it gives an integer world position on an axis.
     *
     * @param chunkIndex         The chunk's index for the specified axis.
     * @param chunkRelativeIndex The chunk relative index, a.k.a. the axis index in the chunk's data multidimensional array.
     *
     * @retur The absolute index for the specified axis.
     */
    public int getAbsoluteIndexZ(int chunkIndex, int chunkRelativeIndex) {
        return getAbsoluteIndex(chunkIndex, chunkRelativeIndex, m_chunkSizeBits.z);
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

    public Vector3i getChunkSizeBits() {
        return m_chunkSizeBits;
    }

    public Vector3i getChunkSize() {
        return m_chunkSize;
    }
}
