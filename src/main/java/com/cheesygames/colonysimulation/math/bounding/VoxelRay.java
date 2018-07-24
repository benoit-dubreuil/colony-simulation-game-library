package com.cheesygames.colonysimulation.math.bounding;

import com.cheesygames.colonysimulation.math.MathExt;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.World;
import com.jme3.math.Vector3f;
import com.jme3.scene.plugins.blender.math.Vector3d;

import java.util.ArrayList;
import java.util.List;

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

    public List<Vector3i> rayCastLocal(World world, Vector3i voxelIndex) {
        double halfExtent = world.getMeshGenerator().getHalfExtent();
        double extent = halfExtent * 2;

        // TODO : assert not NaN

        List<Vector3i> visitedVoxels = new ArrayList<>();

        // This id of the first/current voxel hit by the ray.
        // Using floor (round down) is actually very important,
        // the implicit int-casting will round up for negative numbers.
        voxelIndex.set((int) Math.floor((m_start.x + halfExtent) / extent),
            (int) Math.floor((m_start.y + halfExtent) / extent),
            (int) Math.floor((m_start.z + halfExtent) / extent));


        // The id of the last voxel hit by the ray.
        // TODO: what happens if the end point is on a border?
        Vector3i lastVoxelIndex = new Vector3i((int) Math.floor((m_start.x + halfExtent + m_direction.x * m_length) / extent),
            (int) Math.floor((m_start.y + halfExtent + m_direction.y * m_length) / extent),
            (int) Math.floor((m_start.z + halfExtent + m_direction.z * m_length) / extent));

        computeVoxelDistance(halfExtent, voxelIndex);
        System.out.println(m_voxelDistance);

        // In which direction the voxel ids are incremented.
        double stepX = MathExt.getSignZeroPositive(m_direction.x);
        double stepY = MathExt.getSignZeroPositive(m_direction.y);
        double stepZ = MathExt.getSignZeroPositive(m_direction.z);

        // Distance along the ray to the next voxel border from the current position (tMaxX, tMaxY, tMaxZ).
        double nextVoxelBoundaryX = (voxelIndex.x + stepX) * extent;
        double nextVoxelBoundaryY = (voxelIndex.y + stepY) * extent;
        double nextVoxelBoundaryZ = (voxelIndex.z + stepZ) * extent;

        // tMaxX, tMaxY, tMaxZ -- distance until next intersection with voxel-border
        // the value of t at which the ray crosses the first vertical voxel boundary
        double tMaxX = (m_direction.x != 0) ? (nextVoxelBoundaryX - m_start.x) / m_direction.x : Double.MAX_VALUE; //
        double tMaxY = (m_direction.y != 0) ? (nextVoxelBoundaryY - m_start.y) / m_direction.y : Double.MAX_VALUE; //
        double tMaxZ = (m_direction.z != 0) ? (nextVoxelBoundaryZ - m_start.z) / m_direction.z : Double.MAX_VALUE; //

        // tDeltaX, tDeltaY, tDeltaZ --
        // how far along the ray we must move for the horizontal component to equal the width of a voxel
        // the direction in which we traverse the grid
        // can only be FLT_MAX if we never go in that direction
        double tDeltaX = (m_direction.x != 0) ? extent / m_direction.x * stepX : Double.MAX_VALUE;
        double tDeltaY = (m_direction.y != 0) ? extent / m_direction.y * stepY : Double.MAX_VALUE;
        double tDeltaZ = (m_direction.z != 0) ? extent / m_direction.z * stepZ : Double.MAX_VALUE;

        visitedVoxels.add(new Vector3i(voxelIndex));

        while(visitedVoxels.size() < m_voxelDistance) {
            if (tMaxX < tMaxY) {
                if (tMaxX < tMaxZ) {
                    voxelIndex.x += stepX;
                    tMaxX += tDeltaX;
                } else {
                    voxelIndex.z += stepZ;
                    tMaxZ += tDeltaZ;
                }
            } else {
                if (tMaxY < tMaxZ) {
                    voxelIndex.y += stepY;
                    tMaxY += tDeltaY;
                } else {
                    voxelIndex.z += stepZ;
                    tMaxZ += tDeltaZ;
                }
            }

            visitedVoxels.add(new Vector3i(voxelIndex));
        }

        return visitedVoxels;
    }

    public static void main(String[] args) {
        Vector3d start = new Vector3d(0, 0, 0);
        Vector3d end = new Vector3d(2, 4, 0);

        World world = new World();
        VoxelRay ray = new VoxelRay(start, end);
        List<Vector3i> visitedVoxels = ray.rayCastLocal(world, new Vector3i());

        System.out.println(visitedVoxels);
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
        double decimalIndex = (position + halfExtent) / (halfExtent * 2);
        return (int) (decimalIndex + MathExt.getNegativeSign(decimalIndex) * halfExtent * 2);
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
