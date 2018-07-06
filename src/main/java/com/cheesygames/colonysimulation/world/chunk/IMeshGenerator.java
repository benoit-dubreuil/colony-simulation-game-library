package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.world.World;
import com.jme3.scene.Mesh;

public interface IMeshGenerator {

    Mesh generateMesh(World world, Chunk chunk);
}
