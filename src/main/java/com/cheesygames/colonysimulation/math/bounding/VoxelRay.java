package com.cheesygames.colonysimulation.math.bounding;

import com.cheesygames.colonysimulation.math.MathExt;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.World;
import com.jme3.math.Vector3f;

/**
 * Ray for ray casting inside a voxel world. Each voxel is considered as a cube within this ray.
 */
public class VoxelRay {

    private Vector3f m_start;
    private Vector3f m_direction;
    private float m_length;
    private int m_voxelDistance;

    /**
     * Constructs a {@link VoxelRay} from two points : start and end. The start point is kept as reference.
     *
     * @param start The absolute starting position of the ray. Is kept as reference.
     * @param end   The absolute ending position of the ray.
     */
    public VoxelRay(Vector3f start, Vector3f end) {
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
    public VoxelRay(Vector3f start, Vector3f direction, float length) {
        this.m_start = start;
        this.m_direction = direction;
        this.m_length = length;
    }

    public Vector3i rayCastLocal(World world, Vector3i index) {
        getPositionIndexLocal(m_start, world.getMeshGenerator().getHalfExtend(), index);
        computeVoxelDistance(world.getMeshGenerator().getHalfExtend(), index);

        for (int i = 0; i < m_voxelDistance; ++i) {

        }

        // TODO
        return null;
    }

    /**
     * Computes the voxel distance, a.k.a. the number of voxel to traverse, for the ray cast.
     *
     * @param halfExtent The half extent of a voxel.
     * @param startIndex The starting position's index.
     */
    private void computeVoxelDistance(float halfExtent, Vector3i startIndex) {
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
    private void computeVoxelDistance(float halfExtent) {
        computeVoxelDistance(halfExtent, getPositionIndex(m_start, halfExtent));
    }

    /**
     * Computes the axis voxel distance for the supplied values according to the supposition that those values belong to the same axis.
     *
     * @param start      The starting float value.
     * @param startIndex The starting index.
     * @param halfExtent The half extent of a voxel.
     * @param direction  The direction of the ray.
     *
     * @return The axis voxel distance. It may be positive, negative or zero.
     */
    private int computeAxisVoxelDistance(float start, int startIndex, float halfExtent, float direction) {
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
    private static int getPositionIndex(float position, float halfExtent) {
        float floatIndex = (position + halfExtent) / (halfExtent * 2);
        return (int) (floatIndex + MathExt.getNegativeSign(floatIndex) * halfExtent * 2);
    }

    /**
     * Gets the 3D float position's 3D integer index. This method is local, as it modifies the supplied parameter index instead of creating a new {@link Vector3i}.
     *
     * @param position   The 3D float position to get its index.
     * @param halfExtent The half extent of a voxel.
     * @param index      The index that will be set to the position's index.
     *
     * @return The supplied index that was set to the position's index.
     */
    private static Vector3i getPositionIndexLocal(Vector3f position, float halfExtent, Vector3i index) {
        return index.set(getPositionIndex(position.x, halfExtent), getPositionIndex(position.y, halfExtent), getPositionIndex(position.z, halfExtent));
    }

    /**
     * Gets the 3D float position's 3D integer index.
     *
     * @param position   The 3D float position to get its index.
     * @param halfExtent The half extent of a voxel.
     *
     * @return A  newly created index that is set to the position's index.
     */
    private static Vector3i getPositionIndex(Vector3f position, float halfExtent) {
        return getPositionIndexLocal(position, halfExtent, new Vector3i());
    }

    public Vector3f getStart() {
        return m_start;
    }

    public Vector3f getDirection() {
        return m_direction;
    }

    public float getLength() {
        return m_length;
    }
}
