package com.cheesygames.colonysimulation.math.bounding;

import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.World;
import com.jme3.math.Vector3f;
import com.jme3.scene.plugins.blender.math.Vector3d;

/**
 * Static class that contains utilities method for dealing with voxel worlds.
 */
public final class VoxelWorldUtils {

    /**
     * Gets the voxel index of the specified position.
     *
     * @param position The position to get the voxel index from.
     *
     * @return The position's voxel index.
     */
    public static Vector3i getVoxelIndex(Vector3f position) {
        return getVoxelIndexLocal(World.VOXEL_HALF_EXTENT, position, new Vector3i());
    }

    /**
     * Gets the voxel index of the specified position.
     *
     * @param position The position to get the voxel index from.
     *
     * @return The supplied position's voxel index.
     */
    public static Vector3i getVoxelIndex(Vector3d position) {
        return getVoxelIndexLocal(World.VOXEL_HALF_EXTENT, position, new Vector3i());
    }

    /**
     * Gets the voxel index of the specified position.
     *
     * @param voxelHalfExtent The half extent of a voxel, which is the equivalent to a cube of a sphere's radius.
     * @param position        The position to get the voxel index from.
     *
     * @return The position's voxel index.
     */
    public static Vector3i getVoxelIndex(float voxelHalfExtent, Vector3f position) {
        return getVoxelIndexLocal(voxelHalfExtent, position, new Vector3i());
    }

    /**
     * Gets the voxel index of the specified position.
     *
     * @param voxelHalfExtent The half extent of a voxel, which is the equivalent to a cube of a sphere's radius.
     * @param position        The position to get the voxel index from.
     *
     * @return The supplied position's voxel index.
     */
    public static Vector3i getVoxelIndex(double voxelHalfExtent, Vector3d position) {
        return getVoxelIndexLocal(voxelHalfExtent, position, new Vector3i());
    }

    /**
     * Gets the voxel index of the specified position. This method local because the supplied voxel index will be locally modified and returned.
     *
     * @param position   The position to get the voxel index from.
     * @param voxelIndex Where to store the voxel index.
     *
     * @return The voxel index parameter that is set to the supplied position's voxel index.
     */
    public static Vector3i getVoxelIndexLocal(Vector3f position, Vector3i voxelIndex) {
        return getVoxelIndexLocal(World.VOXEL_HALF_EXTENT, position, voxelIndex);
    }

    /**
     * Gets the voxel index of the specified position. This method local because the supplied voxel index will be locally modified and returned.
     *
     * @param position   The position to get the voxel index from.
     * @param voxelIndex Where to store the voxel index.
     *
     * @return The voxel index parameter that is set to the supplied position's voxel index.
     */
    public static Vector3i getVoxelIndexLocal(Vector3d position, Vector3i voxelIndex) {
        return getVoxelIndexLocal(World.VOXEL_HALF_EXTENT, position, voxelIndex);
    }

    /**
     * Gets the voxel index of the specified position. This method local because the supplied voxel index will be locally modified and returned.
     *
     * @param voxelHalfExtent The half extent of a voxel, which is the equivalent to a cube of a sphere's radius.
     * @param position        The position to get the voxel index from.
     * @param voxelIndex      Where to store the voxel index.
     *
     * @return The voxel index parameter that is set to the supplied position's voxel index.
     */
    public static Vector3i getVoxelIndexLocal(float voxelHalfExtent, Vector3f position, Vector3i voxelIndex) {
        return voxelIndex.set(getVoxelIndex(voxelHalfExtent, position.x), getVoxelIndex(voxelHalfExtent, position.y), getVoxelIndex(voxelHalfExtent, position.z));
    }

    /**
     * Gets the voxel index of the specified position. This method local because the supplied voxel index will be locally modified and returned.
     *
     * @param voxelHalfExtent The half extent of a voxel, which is the equivalent to a cube of a sphere's radius.
     * @param position        The position to get the voxel index from.
     * @param voxelIndex      Where to store the voxel index.
     *
     * @return The voxel index parameter that is set to the supplied position's voxel index.
     */
    public static Vector3i getVoxelIndexLocal(double voxelHalfExtent, Vector3d position, Vector3i voxelIndex) {
        return voxelIndex.set(getVoxelIndex(voxelHalfExtent, position.x), getVoxelIndex(voxelHalfExtent, position.y), getVoxelIndex(voxelHalfExtent, position.z));
    }

    /**
     * Gets the voxel index of the specified position. This method suppose that the parameter position is already offsetted with + voxel half extent. This method local because the
     * supplied voxel index will be locally modified and returned.
     *
     * @param voxelExtent The  extent of a voxel, which is the equivalent to a cube of a sphere's diameter.
     * @param position    The position to get the voxel index from. Must already be offsetted with + voxel half extent
     * @param voxelIndex  Where to store the voxel index.
     *
     * @return The voxel index parameter that is set to the supplied position's voxel index.
     */
    public static Vector3i getVoxelIndexNoOffsetLocal(double voxelExtent, Vector3d position, Vector3i voxelIndex) {
        return voxelIndex.set(getVoxelIndexNoOffset(voxelExtent, position.x), getVoxelIndexNoOffset(voxelExtent, position.y), getVoxelIndexNoOffset(voxelExtent, position.z));
    }

    /**
     * Gets the voxel axis independent index of the specified position. This method suppose that the parameter position is already offsetted with + voxel half extent.
     *
     * @param voxelExtent The  extent of a voxel, which is the equivalent to a cube of a sphere's diameter.
     * @param position    The position to get the voxel index from. Must already be offsetted with + voxel half extent
     *
     * @return The voxel absolute (world) index at the specified offsetted position.
     */
    public static int getVoxelIndexNoOffset(double voxelExtent, double position) {
        // Using floor (round down) is actually very important. The explicit int-casting will round up for negative numbers.
        return (int) Math.floor((position) / voxelExtent);
    }

    /**
     * Gets the voxel axis independent index of the specified position.
     *
     * @param voxelHalfExtent The half extent of a voxel, which is the equivalent to a cube of a sphere's radius.
     * @param position        The position to get the voxel index from.
     *
     * @return The voxel absolute (world) index at the specified position.
     */
    public static int getVoxelIndex(double voxelHalfExtent, double position) {
        // Using floor (round down) is actually very important. The explicit int-casting will round up for negative numbers.
        return (int) Math.floor((position + voxelHalfExtent) / (voxelHalfExtent * voxelHalfExtent));
    }

    /**
     * Gets the voxel axis independent index of the specified position.
     *
     * @param voxelHalfExtent The half extent of a voxel, which is the equivalent to a cube of a sphere's radius.
     * @param position        The position to get the voxel index from.
     *
     * @return The voxel absolute (world) index at the specified position.
     */
    public static int getVoxelIndex(float voxelHalfExtent, float position) {
        // Using floor (round down) is actually very important. The explicit int-casting will round up for negative numbers.
        return (int) Math.floor((position + voxelHalfExtent) / (voxelHalfExtent * voxelHalfExtent));
    }

    private VoxelWorldUtils() {
    }
}
