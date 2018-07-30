package com.cheesygames.colonysimulation.math.bounding;

import com.cheesygames.colonysimulation.math.MathExt;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.jme3.math.Vector3f;
import com.jme3.scene.plugins.blender.math.Vector3d;

import java.util.function.Function;

/**
 * Ray for ray casting inside a voxel world. Each voxel is considered as a cube within this ray. A ray consists of a starting position, a direction and a length. The voxel distance
 * is computed once the method {@link #rayCastLocal(double, Function, Vector3i)} or {@link #rayCast(double, Function)} is called.
 */
public class VoxelRay {

    private Vector3d m_start;
    private Vector3d m_direction;
    private double m_length;
    private int m_voxelDistance;

    /**
     * Constructs an invalid {@link VoxelRay} as its direction and length are null. The setters must be called after constructing a {@link VoxelRay} with this constructors.
     */
    public VoxelRay() {
        this.m_start = new Vector3d();
        this.m_direction = new Vector3d();
        this.m_length = 0;
    }

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
     * Constructs a {@link VoxelRay} from two points : start and end.
     *
     * @param start The absolute starting position of the ray.
     * @param end   The absolute ending position of the ray.
     */
    public VoxelRay(Vector3f start, Vector3f end) {
        this.m_start = new Vector3d(start);
        this.m_direction = new Vector3d(end).subtractLocal(m_start);
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

    /**
     * Constructs a {@link VoxelRay} from a start, a direction and a length.
     *
     * @param start     The absolute starting position of the ray.
     * @param direction The direction of the ray. Must be normalized.
     * @param length    The length of the ray.
     */
    public VoxelRay(Vector3f start, Vector3f direction, float length) {
        this.m_start = new Vector3d(start);
        this.m_direction = new Vector3d(direction);
        this.m_length = length;
    }

    /**
     * Casts the ray from its starting position towards its direction whilst keeping in mind its length. A lambda parameter is supplied and called each time a voxel is traversed.
     * This allows the lambda to stop anytime the algorithm to continue its loop.
     *
     * @param voxelHalfExtent   The half extent (radius) of a voxel.
     * @param onTraversingVoxel The operation to execute when traversing a voxel. This method called the same number of times as the value of {@link #getVoxelDistance()}. The
     *                          supplied {@link Vector3i} parameter is not a new instance but a local instance, so it is a reference. The return value {@link Boolean} defines if
     *                          the algorithm should stop.
     *
     * @see <a href="http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.42.3443&rep=rep1&type=pdf">A Fast Voxel Traversal Algorithm</a>
     */
    public void rayCast(double voxelHalfExtent, Function<Vector3i, Boolean> onTraversingVoxel) {
        rayCastLocal(voxelHalfExtent, onTraversingVoxel, new Vector3i());
    }

    /**
     * Casts the ray from its starting position towards its direction whilst keeping in mind its length. A lambda parameter is supplied and called each time a voxel is traversed.
     * This allows the lambda to stop anytime the algorithm to continue its loop.
     * <p>
     * This method is local because the parameter voxelIndex is locally changed to avoid creating a new instance of {@link Vector3i}.
     *
     * @param voxelHalfExtent   The half extent (radius) of a voxel.
     * @param onTraversingVoxel The operation to execute when traversing a voxel. This method called the same number of times as the value of {@link #getVoxelDistance()}. The
     *                          supplied {@link Vector3i} parameter is not a new instance but a local instance, so it is a reference. The return value {@link Boolean} defines if
     *                          the algorithm should stop.
     * @param voxelIndex        The voxel index to locally modify in order to traverse voxels. This parameter exists simply to avoid creating a new {@link Vector3i} instance.
     *
     * @see <a href="http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.42.3443&rep=rep1&type=pdf">A Fast Voxel Traversal Algorithm</a>
     */
    public void rayCastLocal(double voxelHalfExtent, Function<Vector3i, Boolean> onTraversingVoxel, Vector3i voxelIndex) {
        assert !Double.isNaN(voxelHalfExtent);

        assert !Double.isNaN(m_start.x);
        assert !Double.isNaN(m_start.y);
        assert !Double.isNaN(m_start.z);

        assert !Double.isNaN(m_direction.x);
        assert !Double.isNaN(m_direction.y);
        assert !Double.isNaN(m_direction.z);

        assert !Double.isNaN(m_length);

        double voxelExtent = voxelHalfExtent * 2;

        // This id of the first/current voxel hit by the ray.
        VoxelWorldUtils.getVoxelIndexLocal(voxelHalfExtent, m_start, voxelIndex);

        computeVoxelDistance(voxelHalfExtent, voxelIndex);
        assert !Double.isNaN(m_voxelDistance);

        // In which direction the voxel ids are incremented.
        double stepX = MathExt.getSignZeroPositive(m_direction.x);
        double stepY = MathExt.getSignZeroPositive(m_direction.y);
        double stepZ = MathExt.getSignZeroPositive(m_direction.z);

        // Distance along the ray to the next voxel border from the current position (tMaxX, tMaxY, tMaxZ).
        double nextVoxelBoundaryX = (voxelIndex.x + stepX) * voxelExtent;
        double nextVoxelBoundaryY = (voxelIndex.y + stepY) * voxelExtent;
        double nextVoxelBoundaryZ = (voxelIndex.z + stepZ) * voxelExtent;

        // tMaxX, tMaxY, tMaxZ -- distance until next intersection with voxel-border
        // the value of t at which the ray crosses the first vertical voxel boundary
        double tMaxX = (m_direction.x != 0) ? (nextVoxelBoundaryX - m_start.x) / m_direction.x : Double.MAX_VALUE; //
        double tMaxY = (m_direction.y != 0) ? (nextVoxelBoundaryY - m_start.y) / m_direction.y : Double.MAX_VALUE; //
        double tMaxZ = (m_direction.z != 0) ? (nextVoxelBoundaryZ - m_start.z) / m_direction.z : Double.MAX_VALUE; //

        // tDeltaX, tDeltaY, tDeltaZ --
        // how far along the ray we must move for the horizontal component to equal the width of a voxel
        // the direction in which we traverse the grid
        // can only be FLT_MAX if we never go in that direction
        double tDeltaX = (m_direction.x != 0) ? voxelExtent / m_direction.x * stepX : Double.MAX_VALUE;
        double tDeltaY = (m_direction.y != 0) ? voxelExtent / m_direction.y * stepY : Double.MAX_VALUE;
        double tDeltaZ = (m_direction.z != 0) ? voxelExtent / m_direction.z * stepZ : Double.MAX_VALUE;

        if (onTraversingVoxel.apply(voxelIndex)) {
            return;
        }

        int traversedVoxelCount = 0;
        while (++traversedVoxelCount < m_voxelDistance) {
            if (tMaxX < tMaxY) {
                if (tMaxX < tMaxZ) {
                    voxelIndex.x += stepX;
                    tMaxX += tDeltaX;
                }
                else {
                    voxelIndex.z += stepZ;
                    tMaxZ += tDeltaZ;
                }
            }
            else {
                if (tMaxY < tMaxZ) {
                    voxelIndex.y += stepY;
                    tMaxY += tDeltaY;
                }
                else {
                    voxelIndex.z += stepZ;
                    tMaxZ += tDeltaZ;
                }
            }

            if (onTraversingVoxel.apply(voxelIndex)) {
                return;
            }
        }
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
        double gridDecimal = (position + halfExtent) / (halfExtent * 2);

        // TODO : Is it actaully *always* working?
        return (int) (gridDecimal >= 0 && gridDecimal < 1 ? gridDecimal : Math.floor(gridDecimal));
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

    public int getVoxelDistance() {
        return m_voxelDistance;
    }

    public void setStart(Vector3d start) {
        m_start.set(start);
    }

    public void setStart(Vector3f start) {
        m_start.set(start);
    }

    /**
     * Sets the direction.
     *
     * @param direction The direction to set to the ray. Must be normalized.
     */
    public void setDirection(Vector3d direction) {
        m_direction.set(direction);
    }

    /**
     * Sets the direction.
     *
     * @param direction The direction to set to the ray. Must be normalized.
     */
    public void setDirection(Vector3f direction) {
        m_direction.set(direction);
    }

    /**
     * Sets the length of the ray.
     *
     * @param length The new length of the ray. Must be positive.
     */
    public void setLength(double length) {
        m_length = length;
    }
}
