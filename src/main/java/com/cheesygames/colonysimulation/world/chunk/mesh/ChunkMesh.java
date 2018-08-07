package com.cheesygames.colonysimulation.world.chunk.mesh;

import com.cheesygames.colonysimulation.GameGlobal;
import com.jme3.bounding.BoundingBox;
import com.jme3.scene.Mesh;

/**
 * A mesh optimized for chunks.
 */
public class ChunkMesh extends Mesh {

    public ChunkMesh() {
        getBound().setCenter(GameGlobal.world.getChunkSize().x / 2f, GameGlobal.world.getChunkSize().y / 2f, GameGlobal.world.getChunkSize().z / 2f);

        ((BoundingBox) getBound()).setXExtent(GameGlobal.world.getChunkSize().x / 2f);
        ((BoundingBox) getBound()).setYExtent(GameGlobal.world.getChunkSize().y / 2f);
        ((BoundingBox) getBound()).setZExtent(GameGlobal.world.getChunkSize().z / 2f);
    }

    @Override
    public void updateBound() {
    }
}
