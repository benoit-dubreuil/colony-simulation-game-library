package com.cheesygames.colonysimulation.math;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

/**
 * A static utility class for holding constants and static functions for mesh buffers.
 */
public final class MeshBufferUtils {

    public static final int VERTICES_PER_LINE = 2;
    public static final int VERTICES_PER_TRIANGLE = 3;
    public static final int TRIANGLES_PER_QUAD = 2;
    public static final int VERTICES_PER_QUAD = VERTICES_PER_TRIANGLE * TRIANGLES_PER_QUAD;
    public static final int SHARED_VERTICES_PER_QUAD = 4;
    public static final int SHARED_VERTICES_PER_CUBE = 8;

    public static final int POSITION_BUFFER_COMPONENT_COUNT = 3;
    public static final int INDEX_BUFFER_COMPONENT_COUNT = 3;
    public static final int COLOR_RGBA_BUFFER_COMPONENT_COUNT = 4;
    public static final int NORMAL_BUFFER_COMPONENT_COUNT = 3;
    public static final int TEXTURE_BUFFER_COMPONENT_COUNT = 2;

    private MeshBufferUtils() {
    }

    public static Vector3f computeTriangleNormal(Vector3f[] vertices) {
        return computeTriangleNormal(vertices[0], vertices[1], vertices[2]);
    }

    public static Vector3f computeTriangleNormal(Vector3f p1, Vector3f p2, Vector3f p3) {
        return p2.subtract(p1).crossLocal(p3.subtract(p1)).normalizeLocal();
    }

    /**
     * Sets the mesh buffer according to its type. This is a convivial method to simplify the operation of setting a buffer to a mesh and thus, it uses the default number of
     * components and the default buffer types.
     *
     * @param mesh       The mesh to set its buffer.
     * @param bufferType The GPU type of the buffer, i.e. what will the buffer be used for.
     * @param buffer     The actual buffer that will be set in the mesh.
     * @param <B>        The actual type of the buffer.
     */
    public static <B extends Buffer> void setMeshBuffer(Mesh mesh, VertexBuffer.Type bufferType, B buffer) {
        switch (bufferType) {
            case Position:
                mesh.setBuffer(bufferType, POSITION_BUFFER_COMPONENT_COUNT, (FloatBuffer) buffer);
                break;
            case Index:
                mesh.setBuffer(bufferType, INDEX_BUFFER_COMPONENT_COUNT, (IntBuffer) buffer);
                break;
            case Normal:
                mesh.setBuffer(bufferType, NORMAL_BUFFER_COMPONENT_COUNT, (FloatBuffer) buffer);
                break;
            case Color:
                mesh.setBuffer(bufferType, COLOR_RGBA_BUFFER_COMPONENT_COUNT, (FloatBuffer) buffer);
                break;
            case TexCoord:
                mesh.setBuffer(bufferType, TEXTURE_BUFFER_COMPONENT_COUNT, (FloatBuffer) buffer);
                break;
        }
    }

    /**
     * Creates a position buffer and fills it with the supplied vertices.
     *
     * @param vertices The vertices to add to the newly created vertex buffer.
     *
     * @return A new vertex buffer filled with the supplied vertices.
     */
    public static FloatBuffer createPositionBuffer(List<Vector3f> vertices) {
        return createPositionBuffer(vertices, vertices.size());
    }

    /**
     * Creates a position buffer and fills it with the supplied vertices. Unlike {@link MeshBufferUtils#createPositionBuffer(List)}, it takes an Integer limit that limits the range
     * of vertices it'll read
     *
     * @param vertices The vertices to add to the newly created vertex buffer.
     * @param limit    The amount of vertices to take
     *
     * @return A new vertex buffer filled with the supplied vertices.
     */
    public static FloatBuffer createPositionBuffer(List<Vector3f> vertices, int limit) {
        FloatBuffer positionBuffer = (FloatBuffer) VertexBuffer.createBuffer(VertexBuffer.Format.Float, POSITION_BUFFER_COMPONENT_COUNT, vertices.size());

        for (int i = 0; i < limit; ++i) {
            Vector3f position = vertices.get(i);
            positionBuffer.put(position.x).put(position.y).put(position.z);
        }

        return positionBuffer;
    }

    /**
     * Creates a position buffer and fills it with the supplied vertices.
     *
     * @param vertices The vertices to add to the newly created vertex buffer.
     *
     * @return A new vertex buffer filled with the supplied vertices.
     */
    public static FloatBuffer createPositionBuffer(Vector3f... vertices) {
        return createPositionBuffer(vertices.length, vertices);
    }

    /**
     * Creates a position buffer and fills it with the supplied vertices. Unlike {@link MeshBufferUtils#createPositionBuffer(Vector3f...)}, it takes an Integer limit that limits
     * the range of vertices it'll read
     *
     * @param vertices The vertices to add to the newly created vertex buffer.
     * @param limit    The amount of vertices to take
     *
     * @return A new vertex buffer filled with the supplied vertices.
     */
    public static FloatBuffer createPositionBuffer(int limit, Vector3f... vertices) {
        FloatBuffer positionBuffer = (FloatBuffer) VertexBuffer.createBuffer(VertexBuffer.Format.Float, POSITION_BUFFER_COMPONENT_COUNT, vertices.length);

        for (int i = 0; i < limit; ++i) {
            Vector3f position = vertices[i];
            positionBuffer.put(position.x).put(position.y).put(position.z);
        }

        return positionBuffer;
    }

    /**
     * Creates an index buffer and fills it incrementally, which is an optional index buffer method for when triangles aren't shared.
     *
     * @param triangleCount The number of triangles.
     *
     * @return A new index buffer filled incrementally.
     */
    public static IntBuffer createIndexBuffer(int triangleCount) {
        IntBuffer indexBuffer = (IntBuffer) VertexBuffer.createBuffer(VertexBuffer.Format.UnsignedInt, INDEX_BUFFER_COMPONENT_COUNT, triangleCount);

        for (int i = 0; i < triangleCount; ++i) {
            indexBuffer.put(i);
        }

        return indexBuffer;
    }

    /**
     * Creates a normal buffer from a list of normals and fills it with that supplied list.
     *
     * @param normals The normals to create the buffer from.
     *
     * @return A newly created normal buffer made up of the supplied normals.
     */
    public static FloatBuffer createNormalBuffer(List<Vector3f> normals) {
        FloatBuffer normalBuffer = (FloatBuffer) VertexBuffer.createBuffer(VertexBuffer.Format.Float, NORMAL_BUFFER_COMPONENT_COUNT, normals.size());

        for (int i = 0; i < normals.size(); ++i) {
            Vector3f position = normals.get(i);
            normalBuffer.put(position.x).put(position.y).put(position.z);
        }

        return normalBuffer;
    }

    /**
     * Creates a normal buffer from a vertex list, supposing that the vertices are not shared.
     *
     * @param vertices The vertices used to construct the normals
     *
     * @return A newly created normal buffer.
     */
    public static FloatBuffer createNormalBufferFromVertices(List<Vector3f> vertices) {
        FloatBuffer normalBuffer = (FloatBuffer) VertexBuffer.createBuffer(VertexBuffer.Format.Float, NORMAL_BUFFER_COMPONENT_COUNT, vertices.size());

        for (int i = VERTICES_PER_TRIANGLE - 1; i < vertices.size(); i += VERTICES_PER_TRIANGLE) {
            Vector3f normal = computeTriangleNormal(vertices.get(i - 2), vertices.get(i - 1), vertices.get(i));

            for (int j = 0; j < NORMAL_BUFFER_COMPONENT_COUNT; ++j) {
                normalBuffer.put(normal.x).put(normal.y).put(normal.z);
            }
        }

        return normalBuffer;
    }

    /**
     * Creates a normal buffer from a vertex list, supposing that the vertices are not shared.
     *
     * @param vertices The vertices used to construct the normals
     *
     * @return A newly created normal buffer.
     */
    public static FloatBuffer createNormalBufferFromVertices(Vector3f... vertices) {
        FloatBuffer normalBuffer = (FloatBuffer) VertexBuffer.createBuffer(VertexBuffer.Format.Float, NORMAL_BUFFER_COMPONENT_COUNT, vertices.length);

        for (int i = VERTICES_PER_TRIANGLE - 1; i < vertices.length; i += VERTICES_PER_TRIANGLE) {
            Vector3f normal = computeTriangleNormal(vertices[i - 2], vertices[i - 1], vertices[i]);

            for (int j = 0; j < NORMAL_BUFFER_COMPONENT_COUNT; ++j) {
                normalBuffer.put(normal.x).put(normal.y).put(normal.z);
            }
        }

        return normalBuffer;
    }
}
