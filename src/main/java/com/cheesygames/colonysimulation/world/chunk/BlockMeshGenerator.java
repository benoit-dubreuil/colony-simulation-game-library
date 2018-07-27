package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.math.MeshBufferUtils;
import com.cheesygames.colonysimulation.math.direction.Direction3D;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.World;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Block / bloxel mesh generator.
 */
public class BlockMeshGenerator implements IChunkMeshGenerator {

    private static final Vector3f[] FRONT_CUBE_FACE = {
        new Vector3f(-World.VOXEL_HALF_EXTENT, World.VOXEL_HALF_EXTENT, World.VOXEL_HALF_EXTENT),
        new Vector3f(-World.VOXEL_HALF_EXTENT, -World.VOXEL_HALF_EXTENT, World.VOXEL_HALF_EXTENT),
        new Vector3f(World.VOXEL_HALF_EXTENT, -World.VOXEL_HALF_EXTENT, World.VOXEL_HALF_EXTENT),
        new Vector3f(World.VOXEL_HALF_EXTENT, -World.VOXEL_HALF_EXTENT, World.VOXEL_HALF_EXTENT),
        new Vector3f(World.VOXEL_HALF_EXTENT, World.VOXEL_HALF_EXTENT, World.VOXEL_HALF_EXTENT),
        new Vector3f(-World.VOXEL_HALF_EXTENT, World.VOXEL_HALF_EXTENT, World.VOXEL_HALF_EXTENT) };

    @Override
    public Mesh generateMesh(World world, Chunk chunk) {
        Mesh mesh = new ChunkMesh();

        Vector3i chunkSize = chunk.getSize();
        Map<Direction3D, IChunkVoxelData> adjacentChunks = world.getAdjacentChunks(chunk.getIndex());

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();

        for (int x = 0; x < chunkSize.x; ++x) {
            for (int y = 0; y < chunkSize.y; ++y) {
                for (int z = 0; z < chunkSize.z; ++z) {
                    generateVoxelMesh(chunk, adjacentChunks, x, y, z, vertices, normals);
                }
            }
        }

        MeshBufferUtils.setMeshBuffer(mesh, VertexBuffer.Type.Position, MeshBufferUtils.createPositionBuffer(vertices));
        MeshBufferUtils.setMeshBuffer(mesh, VertexBuffer.Type.Normal, MeshBufferUtils.createNormalBuffer(normals));
        mesh.updateBound();

        return mesh;
    }

    @Override
    public void generateVoxelMesh(Chunk chunk, Map<Direction3D, IChunkVoxelData> adjacentChunks, int x, int y, int z, List<Vector3f> vertices, List<Vector3f> normals) {
        if (chunk.getVoxelAt(x, y, z).isSolid()) {
            for (Direction3D cubeFace : Direction3D.ORTHOGONALS) {
                VoxelType adjacentVoxelType;
                int adjacentVoxelX = x + cubeFace.getDirectionX();
                int adjacentVoxelY = y + cubeFace.getDirectionY();
                int adjacentVoxelZ = z + cubeFace.getDirectionZ();

                if (adjacentVoxelX < 0 || adjacentVoxelX >= chunk.getSize().x || adjacentVoxelY < 0 || adjacentVoxelY >= chunk.getSize().y || adjacentVoxelZ < 0
                    || adjacentVoxelZ >= chunk.getSize().z) {
                    adjacentVoxelType = adjacentChunks.get(cubeFace).getVoxelFromPositiveSide(cubeFace.getOpposite(), x, y, z);
                }
                else {
                    adjacentVoxelType = chunk.getVoxelAt(adjacentVoxelX, adjacentVoxelY, adjacentVoxelZ);
                }

                if (!adjacentVoxelType.isSolid()) {
                    for (Vector3f frontCubeFaceVertex : FRONT_CUBE_FACE) {
                        vertices.add(cubeFace.swizzleAccordingToDirection(frontCubeFaceVertex).addLocal(x, y, z));
                        normals.add(cubeFace.getDirection().toVector3f());
                    }
                }
            }
        }
    }
}
