package com.cheesygames.colonysimulation.math.vector.polygon;

import com.cheesygames.colonysimulation.math.MeshBufferUtils;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;

import java.util.List;

/**
 * Interface for polygons of 3 dimensions, which their components are defined as floats.
 */
public interface IPolygon3f {

    /**
     * Computes the centroid point of a collection of position vectors. The supplied vectors must form a convex polygon.
     *
     * @param vectors The vectors to compute the centroid with.
     *
     * @return The computed centroid.
     */
    static Vector3f computeCentroid(List<Vector3f> vectors) {
        float areaTotal;

        TempVars tempVars = TempVars.get();

        Vector3f centroid = new Vector3f();
        Vector3f total = tempVars.vect1.zero();
        Vector3f origin = vectors.get(0);

        for (int i = 2, length = vectors.size(); i < length; ++i) {
            Vector3f vector = vectors.get(i);
            Vector3f previous = vectors.get(i - 1);
            Vector3f firstEdge = tempVars.vect2.set(vector).subtractLocal(origin);
            Vector3f secondEdge = tempVars.vect3.set(vector).subtractLocal(previous);

            firstEdge.crossLocal(secondEdge);
            total.addLocal(firstEdge);

            centroid.addLocal(origin.add(previous).addLocal(vector).multLocal(firstEdge.length() / 2f).divideLocal(3));
        }

        tempVars.release();

        areaTotal = FastMath.abs(total.dot(MeshBufferUtils.computeTriangleNormal(vectors.get(0), vectors.get(1), vectors.get(2))) / 2);

        return centroid.divideLocal(areaTotal);
    }

    /**
     * Computes the centroid of the polygon without setting it.
     *
     * @return The computed polygon's centroid.
     */
    default Vector3f computeCentroid() {
        return computeCentroid(getVertices());
    }

    float getPointToPolygonLength(Vector3f point);

    /**
     * Snaps the supplied point to the polygon.
     *
     * @param point the point to snap onto the polygon.
     *
     * @return The supplied point cloned and snapped onto the polygon.
     */
    Vector3f snapPointToPolygonLocal(Vector3f point);

    /**
     * Snaps the supplied point to the polygon.
     *
     * @param point the point to snap onto the polygon.
     *
     * @return The supplied point snapped onto the polygon.
     */
    default Vector3f snapPointToPolygon(Vector3f point) {
        return snapPointToPolygonLocal(point.clone());
    }

    Vector3f getCentroid();

    List<Vector3f> getVertices();
}
