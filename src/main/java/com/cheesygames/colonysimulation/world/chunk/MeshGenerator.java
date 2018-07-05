package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.math.direction.Direction3D;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.World;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;
import com.jme3.scene.Mesh;

import java.util.Map;

public class MeshGenerator {

    private Vector3i m_tmpIndex;

    public MeshGenerator() {
        this.m_tmpIndex = new Vector3i();
    }

    public Mesh generateMesh(World world, Vector3i index) {
        // TODO

        m_tmpIndex.set(index);
        Mesh mesh = new Mesh();

        Chunk chunk = world.getChunkAt(index);
        Vector3i chunkSize = chunk.getSize();

        Map<Direction3D, IChunkVoxelData> adjacentChunks = world.getAdjacentChunks(index);

        for (int x = 0; x < chunkSize.x; ++x) {
            for (int y = 0; y < chunkSize.y; ++y) {
                for (int z = 0; z < chunkSize.z; ++z) {
                    for (Direction3D cubeFace : Direction3D.ORTHOGONALS) {
                        VoxelType adjacentVoxelType;
                        int adjacentVoxelX = x + cubeFace.getDirectionX();
                        int adjacentVoxelY = y + cubeFace.getDirectionY();
                        int adjacentVoxelZ = z + cubeFace.getDirectionZ();

                        if (adjacentVoxelX <= 0 || adjacentVoxelX >= chunkSize.x || adjacentVoxelY <= 0 || adjacentVoxelY >= chunkSize.y || adjacentVoxelZ <= 0
                            || adjacentVoxelZ >= chunkSize.z) {
                            adjacentVoxelType = adjacentChunks.get(cubeFace.getDirection()).getVoxelFromSide(cubeFace.getOpposite(), x, y, z);
                        }
                        else {
                            adjacentVoxelType = chunk.getVoxelAt(adjacentVoxelX, adjacentVoxelY, adjacentVoxelZ);
                        }


                    }
                }
            }
        }

        return null;
    }
}
