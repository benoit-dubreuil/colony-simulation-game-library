package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.GameGlobal;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;
import com.cheesygames.colonysimulation.world.generation.IWorldGenerator;
import com.jme3.scene.Mesh;

/**
 * A world chunk consisting of voxels. Its size on all X, Y and Z axes must be a power of 2.
 */
public class Chunk extends AbstractChunk {

    private VoxelType[][][] m_voxels;
    private Mesh m_mesh;
    private boolean m_isEmpty;

    public Chunk(Vector3i index) {
        super(index);
        this.m_isEmpty = true;
    }

    /**
     * Generates this world chunk's voxel data according to the supplied generator.
     *
     * @param generator The generator used to generate the voxel data.
     */
    public void generateData(IWorldGenerator generator) {
        final Vector3i chunkSize = getSize();
        m_voxels = new VoxelType[chunkSize.x][chunkSize.y][chunkSize.z];

        for (int x = 0; x < chunkSize.x; ++x) {
            for (int y = 0; y < chunkSize.y; ++y) {
                for (int z = 0; z < chunkSize.z; ++z) {
                    m_voxels[x][y][z] = generator.generateVoxel(GameGlobal.world.getAbsoluteIndexX(m_index.x, x),
                        GameGlobal.world.getAbsoluteIndexY(m_index.y, y),
                        GameGlobal.world.getAbsoluteIndexZ(m_index.z, z));

                    m_isEmpty &= (m_voxels[x][y][z] == VoxelType.AIR);
                }
            }
        }
    }

    /**
     * Checks if the chunk is empty and overrides the previous {@link #isEmpty()} state.
     *
     * @return True if the chunk is empty, false otherwise.
     */
    public boolean computeIsEmpty() {
        boolean isEmpty = true;
        Vector3i chunkSize = getSize();

        for (int x = 0; x < chunkSize.x; ++x) {
            for (int y = 0; y < chunkSize.y; ++y) {
                for (int z = 0; z < chunkSize.z; ++z) {
                    isEmpty &= m_voxels[x][y][z] != VoxelType.SOLID;
                }
            }
        }

        return m_isEmpty = isEmpty;
    }

    @Override
    public VoxelType getVoxelAt(Vector3i voxelIndex) {
        return m_voxels[voxelIndex.x][voxelIndex.y][voxelIndex.z];
    }

    @Override
    public VoxelType getVoxelAt(int x, int y, int z) {
        return m_voxels[x][y][z];
    }

    @Override
    public boolean isEmpty() {
        return m_isEmpty;
    }

    public void setEmpty(boolean empty) {
        m_isEmpty = empty;
    }

    public Mesh getMesh() {
        return m_mesh;
    }

    public void setMesh(Mesh mesh) {
        m_mesh = mesh;
    }

    public void setVoxelAt(VoxelType voxelType, Vector3i voxelIndex) {
        m_voxels[voxelIndex.x][voxelIndex.y][voxelIndex.z] = voxelType;
    }

    public void setVoxelAt(VoxelType voxelType, int x, int y, int z) {
        m_voxels[x][y][z] = voxelType;
    }
}
