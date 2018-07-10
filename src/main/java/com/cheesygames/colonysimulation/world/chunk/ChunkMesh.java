package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.GameGlobal;
import com.jme3.bounding.BoundingBox;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;

/**
 * A mesh optimized for chunks.
 */
public class ChunkMesh extends Mesh {

    @Override
    public void updateBound() {
        VertexBuffer posBuf = getBuffer(VertexBuffer.Type.Position);
        if (posBuf != null) {
            ((BoundingBox) getBound()).setXExtent(GameGlobal.world.getChunkSize().x / 2f);
            ((BoundingBox) getBound()).setYExtent(GameGlobal.world.getChunkSize().y / 2f);
            ((BoundingBox) getBound()).setZExtent(GameGlobal.world.getChunkSize().z / 2f);
        }
    }
}
