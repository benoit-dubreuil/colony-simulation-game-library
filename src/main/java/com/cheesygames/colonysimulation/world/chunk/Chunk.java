package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.math.direction.Direction3D;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.WorldGenerator;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;
import com.jme3.scene.Mesh;

/**
 * A world chunk consisting of voxels. Its size on all X, Y and Z axes must be a power of 2.
 */
public class Chunk extends AbstractChunk {

    private VoxelType[][][] m_voxels;
    private Mesh m_mesh;

    public Chunk(Vector3i index, int sizeX, int sizeY, int sizeZ) {
        super(index, sizeX, sizeY, sizeZ);
    }

    public Chunk(Vector3i index, int size) {
        super(index, size);
    }

    /**
     * Generates this world chunk's voxel data according to the supplied generator.
     *
     * @param generator The generator used to generate the voxel data.
     */
    public void generateData(WorldGenerator generator) {
        m_voxels = new VoxelType[m_size.x][m_size.y][m_size.z];

        // TODO : Tmp
        for (int x = 0; x < m_size.x; ++x) {
            for (int y = 0; y < m_size.y; ++y) {
                for (int z = 0; z < m_size.z; ++z) {
                    m_voxels[x][y][z] = VoxelType.SOLID;
                }
            }
        }
    }

    @Override
    public VoxelType getVoxelAt(int x, int y, int z) {
        return m_voxels[x][y][z];
    }
}
