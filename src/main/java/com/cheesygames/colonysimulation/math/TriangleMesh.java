package com.cheesygames.colonysimulation.math;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;

/**
 * A mesh with 3 vertices that represent a triangle.
 */
public class TriangleMesh extends Mesh {

    private Vector3f m_v1;
    private Vector3f m_v2;
    private Vector3f m_v3;

    /**
     * Do not use. Only for serialization.
     */
    public TriangleMesh() {
    }

    public TriangleMesh(Vector3f v1, Vector3f v2, Vector3f v3) {
        updateGeometry(v1, v2, v3);
    }

    protected void updateGeometry(Vector3f v1, Vector3f v2, Vector3f v3) {
        this.m_v1 = v1;
        this.m_v2 = v2;
        this.m_v3 = v3;

        MeshBufferUtils.setMeshBuffer(this, VertexBuffer.Type.Position, MeshBufferUtils.createPositionBuffer(v1, v2, v3));
        MeshBufferUtils.setMeshBuffer(this, VertexBuffer.Type.Normal, MeshBufferUtils.createNormalBufferFromVertices(v1, v2, v3));

        updateBound();
    }
}
