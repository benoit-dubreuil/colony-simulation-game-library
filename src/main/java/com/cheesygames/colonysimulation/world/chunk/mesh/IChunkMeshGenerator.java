package com.cheesygames.colonysimulation.world.chunk.mesh;

import com.cheesygames.colonysimulation.math.direction.Direction3D;
import com.cheesygames.colonysimulation.world.World;
import com.cheesygames.colonysimulation.world.chunk.Chunk;
import com.cheesygames.colonysimulation.world.chunk.IChunkVoxelData;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;

import java.util.List;
import java.util.Map;

/**
 * Interface for chunk mesh generators.
 */
public interface IChunkMeshGenerator {

    Mesh generateMesh(Chunk chunk);

    void generateVoxelMesh(Chunk chunk, Map<Direction3D, IChunkVoxelData> adjacentChunks, int x, int y, int z, List<Vector3f> vertices, List<Vector3f> normals);
}
