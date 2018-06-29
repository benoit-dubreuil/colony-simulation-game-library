package com.cheesygames.colonysimulation.math.bounding;

import com.cheesygames.colonysimulation.math.bounding.octree.IOctreeEntity;
import com.jme3.math.Vector3f;

/**
 * Axis aligned bounding box. Used for intersection.
 */
public class AABB implements IOctreeEntity {

    private Vector3f m_min;
    private Vector3f m_center;
    private Vector3f m_max;

    /**
     * Creates an AABB from a minimum and a maximum. Both of them are kept as references. Use clone() on the constructor parameters if you don't want that.
     *
     * @param min The minimum corner of the AABB. It's kept as a reference.
     * @param max The maximum corner of the AABB. It's kept as a reference.
     */
    public AABB(Vector3f min, Vector3f max) {
        this.m_min = min;
        this.m_center = min.add(max).divideLocal(2f);
        this.m_max = max;
    }

    /**
     * Creates an AABB from a center and lengths on all 3 axes (X, Y, Z). The center is kept as reference. Use clone() on the constructor parameter if you don't want that.
     *
     * @param center  The center of the AABB. It's kept as a reference.
     * @param radiusX The radius  on the X axis.
     * @param radiusY The radius on the Y axis.
     * @param radiusZ The radius on the Z axis.
     */
    public AABB(Vector3f center, float radiusX, float radiusY, float radiusZ) {
        this.m_min = center.subtract(radiusX, radiusY, radiusZ);
        this.m_center = center;
        this.m_max = center.add(radiusX, radiusY, radiusZ);
    }

    /**
     * Creates an AABB from a center and length on all 3 axes (X, Y, Z). The center is kept as reference. Use clone() on the constructor parameter if you don't want that.
     *
     * @param center The center of the AABB. It's kept as a reference.
     * @param radius The radius on all axes.
     */
    public AABB(Vector3f center, float radius) {
        this.m_min = center.subtract(radius);
        this.m_center = center;
        this.m_max = center.add(radius);
    }

    /**
     * A factory that creates a AABB based on it's dimensions and it's center
     *
     * @param center  the center of the AABB
     * @param lengthX the width of the AABB
     * @param lengthY the height of the AABB
     * @param lengthZ the depth of the AABB
     *
     * @return a newly created AABB based on the given dimensions
     * @see AABB#constructFromDimensions(Vector3f, float)
     */
    public static AABB constructFromDimensions(Vector3f center, float lengthX, float lengthY, float lengthZ) {
        return new AABB(center, lengthX / 2, lengthY / 2, lengthZ / 2);
    }

    /**
     * A factory that creates a cube AABB based on it's dimensions and it's center
     *
     * @param center the center of the AABB
     * @param length the width of one of the AABB's side
     *
     * @return a newly created AABB based on the given dimensions
     * @see AABB#constructFromDimensions(Vector3f, float, float, float)
     */
    public static AABB constructFromDimensions(Vector3f center, float length) {
        return new AABB(center, length / 2);
    }

    /**
     * Checks if this AABB and the other AABB intersect. Two AABBs intersect with each other if any of them is partially or completely inside the other one.
     *
     * @param other The other AABB to test the intersection with.
     *
     * @return If the two AABBs intersect.
     */
    public boolean isIntersecting(AABB other) {
        return m_max.x > other.m_min.x && m_min.x < other.m_max.x && m_max.y > other.m_min.y && m_min.y < other.m_max.y && m_max.z > other.m_min.z && m_min.z < other.m_max.z;
    }

    /**
     * Checks if a point is inside this AABB.
     *
     * @param point The point to test the intersection with.
     *
     * @return If the point is inside the AABB.
     */
    public boolean isPointIn(Vector3f point) {
        return m_max.x >= point.x && m_min.x <= point.x && m_max.y >= point.y && m_min.y <= point.y && m_max.z >= point.z && m_min.z <= point.z;
    }

    /**
     * Checks if this AABB and the other AABB intersect or touch. Two AABBs intersect with each other if any of them is partially or completely inside the other one. Two AABBs
     * touch only if one of their edges touch and that they are not intersecting.
     *
     * @param other The other AABB to test the intersection or touching with.
     *
     * @return If the two AABBs intersect or touch.
     */
    public boolean isIntersectingOrTouching(AABB other) {
        return m_max.x >= other.m_min.x && m_min.x <= other.m_max.x && m_max.y >= other.m_min.y && m_min.y <= other.m_max.y && m_max.z >= other.m_min.z && m_min.z <= other.m_max.z;
    }

    /**
     * Checks if this AABB and the other AABB are touching. Two AABBs touch only if one of their edges touch and that they are not intersecting.
     *
     * @param other The other AABB to test the touching with.
     *
     * @return If the two rectangles touch.
     */
    public boolean isTouching(AABB other) {
        return !isIntersecting(other) && isIntersectingOrTouching(other);
    }

    public Vector3f setMin(Vector3f min) {
        return this.m_min.set(min);
    }

    public Vector3f setCenter(Vector3f center) {
        return this.m_center.set(center);
    }

    public Vector3f setMax(Vector3f max) {
        return this.m_max.set(max);
    }

    public boolean equals(AABB other) {
        return m_max.equals(other.m_max) && m_center.equals(other.m_center) && m_min.equals(other.m_min);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AABB other = (AABB) obj;
        return equals(other);

    }

    public Vector3f getMin() {
        return m_min;
    }

    public Vector3f getCenter() {
        return m_center;
    }

    public Vector3f getMax() {
        return m_max;
    }

    @Override
    public AABB getAABB() {
        return this;
    }

    @Override
    public String toString() {
        return "AABB{" + "min=" + m_min + ", center=" + m_center + ", max=" + m_max + '}';
    }
}
