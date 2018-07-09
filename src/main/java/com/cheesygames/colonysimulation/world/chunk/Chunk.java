package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.World;
import com.cheesygames.colonysimulation.world.WorldGenerator;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;
import com.jme3.scene.Mesh;

/**
 * A world chunk consisting of voxels. Its size on all X, Y and Z axes must be a power of 2.
 */
public class Chunk extends AbstractChunk {

    private VoxelType[][][] m_voxels;
    private Mesh m_mesh;

    public Chunk(World world, Vector3i index) {
        super(world, index);
    }

    /**
     * Generates this world chunk's voxel data according to the supplied generator.
     *
     * @param generator The generator used to generate the voxel data.
     */
    public void generateData(WorldGenerator generator) {
        final Vector3i chunkSize = getSize();
        m_voxels = new VoxelType[chunkSize.x][chunkSize.y][chunkSize.z];

        for (int x = 0; x < chunkSize.x; ++x) {
            for (int y = 0; y < chunkSize.y; ++y) {
                for (int z = 0; z < chunkSize.z; ++z) {
                    m_voxels[x][y][z] = generator.generateVoxel(x, y, z);
                }
            }
        }
    }

    @Override
    public VoxelType getVoxelAt(int x, int y, int z) {
        return m_voxels[x][y][z];
    }

    public Mesh getMesh() {
        return m_mesh;
    }

    public void setMesh(Mesh mesh) {
        m_mesh = mesh;
    }
}
