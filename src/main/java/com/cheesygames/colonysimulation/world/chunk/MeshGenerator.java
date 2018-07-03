package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.World;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;
import com.jme3.scene.Mesh;

public class MeshGenerator {

    private Vector3i m_tmpIndex;

    public MeshGenerator() {
        this.m_tmpIndex = new Vector3i();
    }

    public Mesh generateMesh(World world, Vector3i index) {
        // TODO

        m_tmpIndex.set(index);
        Chunk chunk = world.getChunkAt(index);
        Vector3i chunkSize = chunk.getSize();
        Mesh mesh = new Mesh();

        for (int x = 0; x < chunkSize.x; ++x) {
            for (int y = 0; y < chunkSize.y; ++y) {
                for (int z = 0; z < chunkSize.z; ++z) {
           //         m_voxels[x][y][z] = VoxelType.SOLID;
                }
            }
        }

        return null;
    }
}
