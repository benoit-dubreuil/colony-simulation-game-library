package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.world.World;
import com.jme3.scene.Mesh;

/**
 * Interface for chunk mesh generators.
 */
public interface IChunkMeshGenerator {

    Mesh generateMesh(World world, Chunk chunk);
}
