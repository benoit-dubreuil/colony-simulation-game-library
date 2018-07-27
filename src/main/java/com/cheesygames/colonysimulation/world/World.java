package com.cheesygames.colonysimulation.world;

import com.cheesygames.colonysimulation.math.direction.Direction3D;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.chunk.*;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

import java.util.HashMap;
import java.util.Map;

/**
 * A world holds multiple chunks of voxels.
 */
public class World {

    public static final float VOXEL_HALF_EXTENT = 0.5f;

    private static final Vector3i DEFAULT_CHUNK_SIZE_BITS = new Vector3i(5, 5, 5);
    private static final Vector3i DEFAULT_CHUNK_SIZE = new Vector3i(1 << DEFAULT_CHUNK_SIZE_BITS.x, 1 << DEFAULT_CHUNK_SIZE_BITS.y, 1 << DEFAULT_CHUNK_SIZE_BITS.z);

    private Map<Vector3i, Chunk> m_chunks;
    private IChunkMeshGenerator m_meshGenerator;
    private IWorldGenerator m_worldGenerator;
    private boolean m_isWorldGenerated;
    private boolean m_areMeshesGenerated;
    private Vector3i m_chunkSizeBits;
    private Vector3i m_chunkSize;

    public World() {
        this.m_chunks = new HashMap<>();
        this.m_meshGenerator = new BlockMeshGenerator();
        this.m_worldGenerator = new GradientWorldGenerator(new Vector3f(-2, 16, -5));
        this.m_chunkSizeBits = new Vector3i(DEFAULT_CHUNK_SIZE_BITS);
        this.m_chunkSize = new Vector3i(DEFAULT_CHUNK_SIZE);

        assert FastMath.isPowerOfTwo(m_chunkSize.getX());
        assert FastMath.isPowerOfTwo(m_chunkSize.getY());
        assert FastMath.isPowerOfTwo(m_chunkSize.getZ());
    }

    public void generateWorld() {
        m_worldGenerator.generateWorld(this);
        m_isWorldGenerated = true;
    }

    public void generateMeshes() {
        for (Chunk chunk : m_chunks.values()) {
            chunk.setMesh(m_meshGenerator.generateMesh(this, chunk));
        }

        m_areMeshesGenerated = true;
    }

    /**
     * Adds the supplied chunk to the world, if there is no chunk at the supplied chunk's index and if it is not empty.
     *
     * @param chunk The chunk to add to the world.
     *
     * @return True if it was added successfully, false otherwise.
     */
    public boolean addChunk(Chunk chunk) {
        if (!m_chunks.containsKey(chunk.getIndex()) && !chunk.isEmpty()) {
            m_chunks.put(chunk.getIndex(), chunk);

            return true;
        }

        assert false;

        return false;
    }

    /**
     * Adds the supplied chunk to the world, if there is no chunk at the supplied chunk's index and if it is not empty. If the chunk is successfully added, then its index is set to
     * the supplied index.
     *
     * @param index The index where to add the chunk.
     * @param chunk The chunk to add to the world.
     *
     * @return True if it was added successfully, false otherwise.
     */
    public boolean addChunk(Vector3i index, Chunk chunk) {
        if (!m_chunks.containsKey(index) && !chunk.isEmpty()) {
            chunk.setIndex(index);
            m_chunks.put(index, chunk);

            return true;
        }

        assert false;

        return false;
    }

    /**
     * Sets the voxel type in the chunk specified by its supplied index at the given relative indices.
     *
     * @param voxelType      The voxel type to set at the specified coordinates.
     * @param chunkIndex     The chunk's index.
     * @param chunkRelativeX The chunk's voxel data index on the X axis.
     * @param chunkRelativeY The chunk's voxel data index on the Y axis.
     * @param chunkRelativeZ The chunk's voxel data index on the Z axis.
     */
    public void setVoxelAt(VoxelType voxelType, Vector3i chunkIndex, int chunkRelativeX, int chunkRelativeY, int chunkRelativeZ) {
        assert isWorldGenerated();

        Chunk chunk = m_chunks.get(chunkIndex);
        boolean chunkDoesNotExist = (chunk == null);

        if (chunkDoesNotExist) {
            m_worldGenerator.createChunk(chunkIndex);
        }

        chunk.setVoxelAt(voxelType, chunkRelativeX, chunkRelativeY, chunkRelativeZ);

        if (chunkDoesNotExist) {
            addChunk(chunk);
        }
    }

    public Map<Vector3i, Chunk> getChunks() {
        return m_chunks;
    }

    /**
     * Gets the chunk index based upon the supplied absolute voxel index. The indices are axis independent, meaning that it does matter which is axis is chosen, as long as the
     * parameters belong to the same one.
     *
     * @param absoluteVoxelIndex The absolute voxel index in the world.
     * @param chunkSizeBits      The bit shift count for the chunk size. This allows to not mention the axis.
     *
     * @return The chunk's singular dimensional index.
     */
    private static int getChunkIndex(int absoluteVoxelIndex, int chunkSizeBits) {
        return absoluteVoxelIndex >> chunkSizeBits;
    }

    /**
     * Gets the chunk index based upon the supplied absolute voxel index. The method is local, meaning that the given chunkIndex's components will be set to different values and
     * that it will be returned, even though it's still the same reference.
     *
     * @param absoluteVoxelIndex The absolute voxel index in the world.
     * @param chunkIndex         The chunk index to modify and return.
     *
     * @return The modified chunk index.
     */
    public Vector3i getChunkIndexLocal(Vector3i absoluteVoxelIndex, Vector3i chunkIndex) {
        return chunkIndex.set(getChunkIndex(absoluteVoxelIndex.x, m_chunkSizeBits.x),
            getChunkIndex(absoluteVoxelIndex.y, m_chunkSizeBits.y),
            getChunkIndex(absoluteVoxelIndex.z, m_chunkSizeBits.z));
    }

    /**
     * Gets the chunk index based upon the supplied absolute voxel index.
     *
     * @param absoluteVoxelIndex The absolute voxel index in the world.
     *
     * @return The chunk index.
     */
    public Vector3i getChunkIndex(Vector3i absoluteVoxelIndex) {
        return getChunkIndexLocal(absoluteVoxelIndex, new Vector3i());
    }

    /**
     * Converts locally a world decimal position into a voxel absolute index. The method is local because the parameter voxelIndex is {@link Vector3i#set(int, int, int)} and then
     * returned, so the reference is actually the same.
     *
     * @param worldPosition      The decimal world position to convert.
     * @param absoluteVoxelIndex The absolute voxel index to modify and return.
     *
     * @return The modified absolute voxel index that was computed from the world position.
     */
    public Vector3i worldPositionToVoxelIndexLocal(Vector3f worldPosition, Vector3i absoluteVoxelIndex) {
        return absoluteVoxelIndex.set((int) Math.floor(worldPosition.x + VOXEL_HALF_EXTENT),
            (int) Math.floor(worldPosition.y + VOXEL_HALF_EXTENT),
            (int) Math.floor(worldPosition.z + VOXEL_HALF_EXTENT));
    }

    /**
     * Converts a world decimal position into a voxel absolute index.
     *
     * @param worldPosition The decimal world position to convert.
     *
     * @return The absolute voxel index that was computed from the world position.
     */
    public Vector3i worldPositionToVoxelIndex(Vector3f worldPosition) {
        return worldPositionToVoxelIndexLocal(worldPosition, new Vector3i());
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

    public boolean isWorldGenerated() {
        return m_isWorldGenerated;
    }

    public boolean isAreMeshesGenerated() {
        return m_areMeshesGenerated;
    }

    public Vector3i getChunkSizeBits() {
        return m_chunkSizeBits;
    }

    public Vector3i getChunkSize() {
        return m_chunkSize;
    }

    public IChunkMeshGenerator getMeshGenerator() {
        return m_meshGenerator;
    }

    public IWorldGenerator getWorldGenerator() {
        return m_worldGenerator;
    }
}
