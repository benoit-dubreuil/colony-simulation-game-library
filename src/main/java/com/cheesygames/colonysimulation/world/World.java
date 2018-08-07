package com.cheesygames.colonysimulation.world;

import com.cheesygames.colonysimulation.math.direction.Direction3D;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.chunk.Chunk;
import com.cheesygames.colonysimulation.world.chunk.EmptyChunk;
import com.cheesygames.colonysimulation.world.chunk.IChunkVoxelData;
import com.cheesygames.colonysimulation.world.chunk.mesh.BlockMeshGenerator;
import com.cheesygames.colonysimulation.world.chunk.mesh.IChunkMeshGenerator;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;
import com.cheesygames.colonysimulation.world.generation.GradientWorldGenerator;
import com.cheesygames.colonysimulation.world.generation.IWorldGenerator;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

import java.util.*;

/**
 * A world holds multiple chunks of voxels.
 */
public class World extends AbstractWorldEventEmitter {

    public static final float VOXEL_HALF_EXTENT = 0.5f;

    private static final Vector3i DEFAULT_CHUNK_SIZE_BITS = new Vector3i(5, 5, 5);
    private static final Vector3i DEFAULT_CHUNK_SIZE = new Vector3i(1 << DEFAULT_CHUNK_SIZE_BITS.x, 1 << DEFAULT_CHUNK_SIZE_BITS.y, 1 << DEFAULT_CHUNK_SIZE_BITS.z);

    private Map<Vector3i, Chunk> m_chunks;
    private IChunkMeshGenerator m_meshGenerator;
    private IWorldGenerator m_worldGenerator;
    private boolean m_isWorldGenerated;
    private Vector3i m_chunkSizeBits;
    private Vector3i m_chunkSize;
    private Set<Chunk> m_chunksToRedraw;

    public World() {
        super();
        this.m_chunks = new HashMap<>();
        this.m_meshGenerator = new BlockMeshGenerator();
        this.m_worldGenerator = new GradientWorldGenerator(new Vector3f(-2, 16, -5));
        this.m_chunkSizeBits = new Vector3i(DEFAULT_CHUNK_SIZE_BITS);
        this.m_chunkSize = new Vector3i(DEFAULT_CHUNK_SIZE);
        this.m_chunksToRedraw = new HashSet<>();

        assert FastMath.isPowerOfTwo(m_chunkSize.getX());
        assert FastMath.isPowerOfTwo(m_chunkSize.getY());
        assert FastMath.isPowerOfTwo(m_chunkSize.getZ());
    }

    public void generateWorld() {
        m_worldGenerator.generateWorld();
        m_chunksToRedraw.addAll(m_chunks.values());

        m_isWorldGenerated = true;
    }

    /**
     * Redraws, i.e. reconstructs, the mesh of chunks that need their mesh to be redrawn. It also checks if those chunks are still not empty.
     */
    public void redrawChunksThatNeedIt() {
        if (!m_chunksToRedraw.isEmpty()) {
            Iterator<Chunk> chunksToRedrawIterator = m_chunksToRedraw.iterator();

            while (chunksToRedrawIterator.hasNext()) {
                Chunk chunkToRedraw = chunksToRedrawIterator.next();

                if (chunkToRedraw.computeIsEmpty()) {
                    m_chunks.remove(chunkToRedraw.getIndex());
                    chunkIsEmpty(chunkToRedraw);
                }
                else {
                    boolean wasMeshNullBefore = chunkToRedraw.getMesh() == null;
                    m_meshGenerator.generateMesh(chunkToRedraw);
                    chunkRedrawn(chunkToRedraw, wasMeshNullBefore);
                }

                chunksToRedrawIterator.remove();
            }
        }
    }

    /**
     * Adds the supplied chunk to the list of chunks that need to be redrawn. It only does so if it is already in the world, a.k.a. if the world contains the specified chunk.
     *
     * @param chunk The chunk that needs to be redrawn.
     */
    public void redrawChunk(Chunk chunk) {
        if (m_chunks.containsKey(chunk.getIndex())) {
            m_chunksToRedraw.add(chunk);
        }
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

            Vector3i redrawAdjacentChunkIndex = new Vector3i();

            for (Direction3D adjacentChunkDirection : Direction3D.ORTHOGONALS) {
                redrawAdjacentChunkIndex.set(chunk.getIndex());
                redrawAdjacentChunkIndex.addLocal(adjacentChunkDirection.getDirection());

                if (m_chunks.containsKey(redrawAdjacentChunkIndex)) {
                    m_chunksToRedraw.add(m_chunks.get(redrawAdjacentChunkIndex));
                }
            }

            m_chunksToRedraw.add(chunk);

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
     * Gets the chunk's starting position on the X axis according to its index.
     *
     * @param chunkIndexX The chunk's index on the X axis.
     *
     * @return The starting absolute (world) position of the chunk on the X axis.
     */
    public int getChunkStartPositionX(int chunkIndexX) {
        return chunkIndexX << m_chunkSizeBits.x;
    }

    /**
     * Gets the chunk's starting position on the Y axis according to its index.
     *
     * @param chunkIndexY The chunk's index on the Y axis.
     *
     * @return The starting absolute (world) position of the chunk on the Y axis.
     */
    public int getChunkStartPositionY(int chunkIndexY) {
        return chunkIndexY << m_chunkSizeBits.y;
    }

    /**
     * Gets the chunk's starting position on the Z axis according to its index.
     *
     * @param chunkIndexZ The chunk's index on the Z axis.
     *
     * @return The starting absolute (world) position of the chunk on the Z axis.
     */
    public int getChunkStartPositionZ(int chunkIndexZ) {
        return chunkIndexZ << m_chunkSizeBits.z;
    }

    /**
     * Gets the chunk's starting position according to its index. The method is local, meaning that the supplied chunk's starting position will be
     * <pre>
     * {@link Vector3i#set(int, int, int)} and returned.
     * </pre>
     *
     * @param chunkIndex         The chunk's index.
     * @param chunkStartPosition The chunk's starting position that will be modified and returned.
     *
     * @return The starting absolute (world) position of the chunk, which is the same reference as the given parameter.
     */
    public Vector3i getChunkStartPositionLocal(Vector3i chunkIndex, Vector3i chunkStartPosition) {
        return chunkStartPosition.set(getChunkStartPositionX(chunkIndex.x), getChunkStartPositionY(chunkIndex.y), getChunkStartPositionZ(chunkIndex.z));
    }

    /**
     * Gets the chunk's starting position according to its index.
     *
     * @param chunkIndex The chunk's index.
     *
     * @return The starting absolute (world) position of the chunk.
     */
    public Vector3i getChunkStartPosition(Vector3i chunkIndex) {
        return getChunkStartPositionLocal(chunkIndex, new Vector3i());
    }

    /**
     * Gets the voxel chunk's relative index from its absolute (world) index on the X axis.
     *
     * @param absoluteVoxelIndexX The voxel absolute (world) index on the X axis.
     *
     * @return The voxel chunk's relative index on the X axis.
     */
    public int getVoxelRelativeIndexX(int absoluteVoxelIndexX) {
        return absoluteVoxelIndexX - (getChunkIndex(absoluteVoxelIndexX, m_chunkSizeBits.x) << m_chunkSizeBits.x);
    }

    /**
     * Gets the voxel chunk's relative index from its absolute (world) index on the Y axis.
     *
     * @param absoluteVoxelIndexY The voxel absolute (world) index on the Y axis.
     *
     * @return The voxel chunk's relative index on the Y axis.
     */
    public int getVoxelRelativeIndexY(int absoluteVoxelIndexY) {
        return absoluteVoxelIndexY - (getChunkIndex(absoluteVoxelIndexY, m_chunkSizeBits.y) << m_chunkSizeBits.y);
    }

    /**
     * Gets the voxel chunk's relative index from its absolute (world) index on the Z axis.
     *
     * @param absoluteVoxelIndexZ The voxel absolute (world) index on the Z axis.
     *
     * @return The voxel chunk's relative index on the Z axis.
     */
    public int getVoxelRelativeIndexZ(int absoluteVoxelIndexZ) {
        return absoluteVoxelIndexZ - (getChunkIndex(absoluteVoxelIndexZ, m_chunkSizeBits.z) << m_chunkSizeBits.z);
    }

    /**
     * Gets the voxel chunk's relative (chunk) index from its absolute (world) index. The method is local, meaning that the supplied parameter relativeVoxelIndex will be modified
     * and then returned.
     *
     * @param absoluteVoxelIndex The voxel absolute (world) index.
     * @param relativeVoxelIndex The voxel relative (chunk) index to modify and return.
     *
     * @return The voxel chunk's relative index, which is the reference to the supplied voxel relative index.
     */
    public Vector3i getVoxelRelativeIndexLocal(Vector3i absoluteVoxelIndex, Vector3i relativeVoxelIndex) {
        return relativeVoxelIndex.set(getVoxelRelativeIndexX(absoluteVoxelIndex.x), getVoxelRelativeIndexY(absoluteVoxelIndex.y), getVoxelRelativeIndexZ(absoluteVoxelIndex.z));
    }

    /**
     * Gets the voxel chunk's relative (chunk) index from its absolute (world) index.
     *
     * @param absoluteVoxelIndex The voxel absolute (world) index.
     *
     * @return The voxel chunk's relative index.
     */
    public Vector3i getVoxelRelativeIndex(Vector3i absoluteVoxelIndex) {
        return getVoxelRelativeIndexLocal(absoluteVoxelIndex, new Vector3i());
    }

    /**
     * Gets the absolute index of a chunk relative position according to its chunk's index. In other words, it gives an integer world position.
     *
     * @param chunkIndex         The chunk's index.
     * @param chunkRelativeIndex The chunk relative index, a.k.a. the index in the chunk's data multidimensional array.
     * @param chunkSizeBits      The bit shift count for the chunk size. This allows to not mention the axis.
     *
     * @return The absolute index.
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
     * @return The absolute index for the specified axis.
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
     * @return The absolute index for the specified axis.
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
     * @return The absolute index for the specified axis.
     */
    public int getAbsoluteIndexZ(int chunkIndex, int chunkRelativeIndex) {
        return getAbsoluteIndex(chunkIndex, chunkRelativeIndex, m_chunkSizeBits.z);
    }

    public Chunk getChunkAt(Vector3i index) {
        return m_chunks.get(index);
    }

    /**
     * Gets the {@link Chunk} at the supplied index. If it does not exist, then get the {@link EmptyChunk#DEFAULT_EMPTY_CHUNK}.
     *
     * @param index The index at which to get the {@link Chunk}.
     *
     * @return The {@link Chunk} at the supplied index if it exists. Otherwise, return {@link EmptyChunk#DEFAULT_EMPTY_CHUNK}.
     */
    public IChunkVoxelData getOrEmptyChunkAt(Vector3i index) {
        Chunk chunk = m_chunks.get(index);
        return chunk != null ? chunk : EmptyChunk.DEFAULT_EMPTY_CHUNK;
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
