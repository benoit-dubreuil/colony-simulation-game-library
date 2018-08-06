package com.cheesygames.colonysimulation.math.vector.polygon;

import com.cheesygames.colonysimulation.math.vector.Vector3i;

import java.util.List;

/**
 * Interface for polygons of 3 dimensions, which their components are defined as integers.
 */
public interface IPolygon3i {

    /**
     * Computes the centroid point of a collection of position vectors. The supplied vectors must form a convex polygon.
     *
     * @param vectors The vectors to compute the centroid with.
     *
     * @return The computed centroid.
     */
    static Vector3i computeCentroid(List<Vector3i> vectors) {
        int areaTotal = 0;

        Vector3i centroid = new Vector3i();
        Vector3i origin = vectors.get(0);

        for (int i = 2, length = vectors.size(); i < length; ++i) {
            Vector3i vector = vectors.get(i);
            Vector3i previous = vectors.get(i - 1);
            Vector3i firstEdge = vector.subtract(origin);
            Vector3i secondEdge = vector.subtract(previous);

            // TODO : floor?
            int a = (int) (firstEdge.crossLocal(secondEdge).length() / 2);

            centroid.x += a * (origin.x + previous.x + vector.x) / 3;
            centroid.y += a * (origin.y + previous.y + vector.y) / 3;
            centroid.z += a * (origin.z + previous.z + vector.z) / 3;

            areaTotal += a;
        }

        centroid.x /= areaTotal;
        centroid.y /= areaTotal;
        centroid.z /= areaTotal;

        return centroid;
    }

    /**
     * Snaps the supplied point to the polygon.
     *
     * @param point the point to snap onto the polygon.
     *
     * @return The supplied point snapped onto the polygon.
     */
    Vector3i snapPointToPolygonLocal(Vector3i point);

    /**
     * Snaps the supplied point to the polygon.
     *
     * @param point the point to snap onto the polygon.
     *
     * @return The supplied point cloned and snapped onto the polygon.
     */
    default Vector3i snapPointToPolygon(Vector3i point) {
        return snapPointToPolygonLocal(point.clone());
    }

    Vector3i getPointToPolygon(Vector3i point);

    Vector3i getCentroid();
}
