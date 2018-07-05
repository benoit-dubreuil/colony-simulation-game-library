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

public class BlockMeshGenerator extends MeshGenerator {

    private static final Vector3f[] FRONT_CUBE_FACE = { new Vector3f(-0.5f, 0.5f, -0.5f), new Vector3f(0.5f, 0.5f, -0.5f), new Vector3f(0.5f, -0.5f, -0.5f),
        new Vector3f(-0.5f, -0.5f, -0.5f) };

    @Override
    public Mesh generateMesh(World world, Vector3i index) {
        Mesh mesh = new Mesh();

        Chunk chunk = world.getChunkAt(index);
        Vector3i chunkSize = chunk.getSize();

        Map<Direction3D, IChunkVoxelData> adjacentChunks = world.getAdjacentChunks(index);

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();

        for (int x = 0; x < chunkSize.x; ++x) {
            for (int y = 0; y < chunkSize.y; ++y) {
                for (int z = 0; z < chunkSize.z; ++z) {
                    if (chunk.getVoxelAt(x, y, z).isSolid()) {
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

                            if (!adjacentVoxelType.isSolid()) {
                                for (Vector3f frontCubeFaceVertex : FRONT_CUBE_FACE) {
                                    vertices.add(cubeFace.swizzleAccordingToDirection(frontCubeFaceVertex));
                                    normals.add(cubeFace.getDirection().toVector3f());
                                }
                            }
                        }
                    }
                }
            }
        }

        MeshBufferUtils.setMeshBuffer(mesh, VertexBuffer.Type.Position, MeshBufferUtils.createPositionBuffer(vertices));
        MeshBufferUtils.setMeshBuffer(mesh, VertexBuffer.Type.Normal, MeshBufferUtils.createNormalBuffer(normals));
        mesh.updateBound();

        return mesh;
    }
}
