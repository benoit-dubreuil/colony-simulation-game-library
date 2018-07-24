package com.cheesygames.colonysimulation.math.bounding;

import com.cheesygames.colonysimulation.math.MathExt;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.World;
import com.jme3.scene.plugins.blender.math.Vector3d;

/**
 * Ray for ray casting inside a voxel world. Each voxel is considered as a cube within this ray.
 */
public class VoxelRay {

    private Vector3d m_start;
    private Vector3d m_direction;
    private double m_length;
    private int m_voxelDistance;

    /**
     * Constructs a {@link VoxelRay} from two points : start and end. The start point is kept as reference.
     *
     * @param start The absolute starting position of the ray. Is kept as reference.
     * @param end   The absolute ending position of the ray.
     */
    public VoxelRay(Vector3d start, Vector3d end) {
        this.m_start = start;
        this.m_direction = end.subtract(start);
        this.m_length = m_direction.length();
        this.m_direction.normalizeLocal();
    }

    /**
     * Constructs a {@link VoxelRay} from a start, a direction and a length. The start and direction vectors are kept as references.
     *
     * @param start     The absolute starting position of the ray. Is kept as reference.
     * @param direction The direction of the ray, which is kept as reference. Must be normalized.
     * @param length    The length of the ray.
     */
    public VoxelRay(Vector3d start, Vector3d direction, double length) {
        this.m_start = start;
        this.m_direction = direction;
        this.m_length = length;
    }

    public Vector3i rayCastLocal(World world, Vector3i index) {

        return null;
    }

    /**
     * Computes the relative position of a given absolute position according to the other parameters.
     *
     * @param direction The direction of the ray.
     * @param position  The absolute euclidean position.
     * @param index     The absolute position's index.
     * @param extent    The extend of a voxel, which is the equivalent for a cube of sphere's radius.
     *
     * @return The relative position, which is between [-halfExtent, halfExtent]. Zero is the center of a voxel.
     */
    private static double computeRelativePosition(double direction, double position, int index, double extent) {
        return MathExt.getSignZeroPositive(direction) * (position - index * extent);
    }

    /**
     * Computes the relative position of a given absolute position according to the other parameters.
     *
     * @param direction The direction of the ray.
     * @param position  The absolute euclidean position.
     * @param index     The absolute position's index.
     * @param extent    The extend of a voxel, which is the equivalent for a cube of sphere's radius.
     *
     * @return The relative position, which is between [-halfExtent, halfExtent] per axis. Zero is the center of a voxel.
     */
    private static Vector3d computeRelativePosition(Vector3d direction, Vector3d position, Vector3i index, double extent) {
        return new Vector3d(computeRelativePosition(direction.x, position.x, index.x, extent),
            computeRelativePosition(direction.y, position.y, index.y, extent),
            computeRelativePosition(direction.z, position.z, index.z, extent));
    }

    /**
     * Gets the smallest component for the supplied {@link Vector3d}.
     *
     * @param v The {@link Vector3d} to get the smallest component from.
     *
     * @return The smallest component.
     */
    private static double getSmallestComponent(Vector3d v) {
        return v.x < v.y ? (v.x < v.z ? v.x : v.z) : (v.y < v.z ? v.y : v.z);
    }

    /**
     * Gets the biggest component for the supplied {@link Vector3d}.
     *
     * @param v The {@link Vector3d} to get the biggest component from.
     *
     * @return The biggest component.
     */
    private static double getBiggestComponent(Vector3d v) {
        return v.x >= v.y ? (v.x >= v.z ? v.x : v.z) : (v.y >= v.z ? v.y : v.z);
    }

    /**
     * Gets the biggest component's index for the supplied {@link Vector3d}.
     *
     * @param v The {@link Vector3d} to get the biggest component's index from.
     *
     * @return The biggest component's index.
     */
    private static int getBiggestComponentIndex(Vector3d v) {
        return v.x >= v.y ? (v.x >= v.z ? 0 : 2) : (v.y >= v.z ? 1 : 2);
    }

    /**
     * Computes the voxel distance, a.k.a. the number of voxel to traverse, for the ray cast.
     *
     * @param halfExtent The half extent of a voxel.
     * @param startIndex The starting position's index.
     */
    private void computeVoxelDistance(double halfExtent, Vector3i startIndex) {
        int voxelDistanceX = computeAxisVoxelDistance(m_start.x, startIndex.x, halfExtent, m_direction.x);
        int voxelDistanceY = computeAxisVoxelDistance(m_start.y, startIndex.y, halfExtent, m_direction.y);
        int voxelDistanceZ = computeAxisVoxelDistance(m_start.z, startIndex.z, halfExtent, m_direction.z);

        m_voxelDistance = 1 + voxelDistanceX * MathExt.getSignZeroPositive(voxelDistanceX) + voxelDistanceY * MathExt.getSignZeroPositive(voxelDistanceY) + voxelDistanceZ * MathExt
            .getSignZeroPositive(voxelDistanceZ);
    }

    /**
     * Computes the voxel distance, a.k.a. the number of voxel to traverse, for the ray cast.
     *
     * @param halfExtent The half extent of a voxel.
     */
    private void computeVoxelDistance(double halfExtent) {
        computeVoxelDistance(halfExtent, getPositionIndex(m_start, halfExtent));
    }

    /**
     * Computes the axis voxel distance for the supplied values according to the supposition that those values belong to the same axis.
     *
     * @param start      The starting double value.
     * @param startIndex The starting index.
     * @param halfExtent The half extent of a voxel.
     * @param direction  The direction of the ray.
     *
     * @return The axis voxel distance. It may be positive, negative or zero.
     */
    private int computeAxisVoxelDistance(double start, int startIndex, double halfExtent, double direction) {
        return getPositionIndex(start - startIndex * halfExtent * 2 + direction * m_length, halfExtent);
    }

    /**
     * Gets the position's integer index, according to the half extent of a voxel.
     *
     * @param position   The axis independent position.
     * @param halfExtent The half extent (radius) of a voxel.
     *
     * @return The position's integer index.
     */
    private static int getPositionIndex(double position, double halfExtent) {
        double floatIndex = (position + halfExtent) / (halfExtent * 2);
        return (int) (floatIndex + MathExt.getNegativeSign(floatIndex) * halfExtent * 2);
    }

    /**
     * Gets the 3D double position's 3D integer index. This method is local, as it modifies the supplied parameter index instead of creating a new {@link Vector3i}.
     *
     * @param position   The 3D double position to get its index.
     * @param halfExtent The half extent of a voxel.
     * @param index      The index that will be set to the position's index.
     *
     * @return The supplied index that was set to the position's index.
     */
    private static Vector3i getPositionIndexLocal(Vector3d position, double halfExtent, Vector3i index) {
        return index.set(getPositionIndex(position.x, halfExtent), getPositionIndex(position.y, halfExtent), getPositionIndex(position.z, halfExtent));
    }

    /**
     * Gets the 3D double position's 3D integer index.
     *
     * @param position   The 3D double position to get its index.
     * @param halfExtent The half extent of a voxel.
     *
     * @return A  newly created index that is set to the position's index.
     */
    private static Vector3i getPositionIndex(Vector3d position, double halfExtent) {
        return getPositionIndexLocal(position, halfExtent, new Vector3i());
    }

    public Vector3d getStart() {
        return m_start;
    }

    public Vector3d getDirection() {
        return m_direction;
    }

    public double getLength() {
        return m_length;
    }
}
