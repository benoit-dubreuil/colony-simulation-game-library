package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.World;
import com.jme3.scene.Mesh;

public abstract class MeshGenerator {

    public abstract Mesh generateMesh(World world, Vector3i index);
}
