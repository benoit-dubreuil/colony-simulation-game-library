package com.cheesygames.colonysimulation.math.vector.polygon;

import com.cheesygames.colonysimulation.math.MathExt;
import com.cheesygames.colonysimulation.math.MeshBufferUtils;
import com.cheesygames.colonysimulation.math.bounding.AABB;
import com.cheesygames.colonysimulation.math.bounding.octree.IOctreeEntity;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * An area that consists of a collection of vertices that forms a convex polygon. This polygon is aligned along an axis. Its normal is perpendicular to the axis for alignment.
 */
public class Polygon3f implements IPolygon3f, IOctreeEntity {

    /**
     * All vertices must be on a shared axis but visually it looks more like a plane.
     */
    private List<Vector3f> m_vertices;
    private Vector3f m_centroid;
    private Vector3f m_normal;
    private float m_area;
    private AABB m_aabb;

    public Polygon3f() {
        this.m_vertices = new ArrayList<>();
    }

    /**
     * Adds the supplied vertex to the convex polygon. It is supposed that the given vertex is aligned along the same axis as the other vertices in the way that the polygon can be
     * represented in 2D.
     *
     * @param vertex The vertex to add to the convex polygon.
     *
     * @return The newly added vertex.
     */
    public Vector3f addVertex(Vector3f vertex) {
        m_vertices.add(vertex);
        return vertex;
    }

    /**
     * Computes every member variables that are dependant to the vertices and then set them. It includes the normal, the centroid and the AABB.
     */
    public void computeLocalMetadata() {
        trimToSize();
        setCentroid(computeCentroid());
        setNormal(computeNormal());
        setAABB(computeAABB());
        setArea(computeArea());
    }

    /**
     * Trims the unnecessary allocated memory that was kept for convenience.
     */
    public void trimToSize() {
        ((ArrayList<Vector3f>) m_vertices).trimToSize();
    }

    /**
     * Computes the normal of the polygon without setting it.
     *
     * @return The computed polygon normal.
     */
    public Vector3f computeNormal() {
        return MeshBufferUtils.computeTriangleNormal(m_vertices.get(0), m_vertices.get(1), m_vertices.get(2));
    }

    /**
     * Computes the polygon's AABB without setting it.
     *
     * @return The computed AABB.
     */
    public AABB computeAABB() {
        Vector3f min = m_vertices.get(0).clone();
        Vector3f max = m_vertices.get(0).clone();

        for (int i = 1; i < m_vertices.size(); ++i) {
            min.minLocal(m_vertices.get(i));
            max.maxLocal(m_vertices.get(i));
        }

        return new AABB(min, max);
    }

    public Vector3f setCentroid(Vector3f centroid) {
        return this.m_centroid = centroid;
    }

    public Vector3f setNormal(Vector3f normal) {
        return this.m_normal = normal;
    }

    @Override
    public float getPointToPolygonLength(Vector3f point) {
        // Doing a projection, move along...
        return point.distance(snapPointToPolygon(point));
    }

    @Override
    public Vector3f snapPointToPolygonLocal(Vector3f point) {
        // Doing a projection, move along...

        point.subtractLocal(getNormal().mult(point.subtract(getCentroid()).dot(getNormal())));
        Vector3f closestEdgeVertexRight = new Vector3f();
        Vector3f closestEdgeVertexLeft = new Vector3f();
        float shortestLengthToProjectedPoint = Float.POSITIVE_INFINITY;

        if (getSumAreaOfPoint(point) <= getArea() + MathExt.FLOAT_BIG_EPSILON) {
            // It's inside
            return point;
        }
        else {
            // It's outside
            for (int i = 1; i < m_vertices.size(); ++i) {
                Vector3f edgeVertexRight = m_vertices.get(i - 1);
                Vector3f edgeVertexLeft = m_vertices.get(i);

                float lengthToProjectedPoint = edgeVertexLeft.add(edgeVertexRight).divideLocal(2).distance(point);

                if (lengthToProjectedPoint < shortestLengthToProjectedPoint) {
                    shortestLengthToProjectedPoint = lengthToProjectedPoint;
                    closestEdgeVertexLeft = edgeVertexLeft;
                    closestEdgeVertexRight = edgeVertexRight;
                }
            }

            return MathExt.projectPointOnLine(point, closestEdgeVertexRight, closestEdgeVertexLeft);
        }
    }

    /**
     * Computes the area of the polygon.
     *
     * @return The area of a polygon.
     */
    public float computeArea() {
        Vector3f total = new Vector3f();
        Vector3f crossTmp = new Vector3f();

        for (int i = 0, size = m_vertices.size(); i < size; ++i) {
            int j = (i + 1) % size;
            crossTmp.set(m_vertices.get(i)).crossLocal(m_vertices.get(j));
            total.addLocal(crossTmp);
        }

        return FastMath.abs(total.dot(getNormal()) / 2);
    }

    /**
     * Gets the sum of all triangles with point as their tip.
     *
     * @param point The point to use.
     *
     * @return The sum of the areas of the triangles.
     */
    public float getSumAreaOfPoint(Vector3f point) {
        float total = 0;

        for (int i = 0, size = m_vertices.size(); i < size; ++i) {
            int j = (i + 1) % size;
            total += FastMath.abs(MathExt.funnelCross2D(point, m_vertices.get(i), m_vertices.get(j)) / 2);
        }

        return total;
    }

    @Override
    public Vector3f getCentroid() {
        return m_centroid;
    }

    @Override
    public List<Vector3f> getVertices() {
        return m_vertices;
    }

    public Vector3f getNormal() {
        return m_normal;
    }

    @Override
    public AABB getAABB() {
        return m_aabb;
    }

    public void setAABB(AABB aabb) {
        this.m_aabb = aabb;
    }

    public float getArea() {
        return m_area;
    }

    public void setArea(float area) {
        m_area = area;
    }

    @Override
    public String toString() {
        return "Polygon [" + "Area=" + m_area + ", " + "Centroid=" + m_centroid + ", " + "Normal=" + m_normal + ", " + "AABB=" + '\n' + '\t' + m_aabb + ", " + "Vertices=" + '\n'
            + '\t' + m_vertices + ']';
    }
}
