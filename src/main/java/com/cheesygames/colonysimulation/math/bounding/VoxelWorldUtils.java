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
    public static Vector3i getVoxelIndex(float voxelHalfExtent, Vector3d position) {
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
        // Using floor (round down) is actually very important. The explicit int-casting will round up for negative numbers.
        return voxelIndex.set((int) Math.floor((position.x + voxelHalfExtent) / (voxelHalfExtent * 2)),
            (int) Math.floor((position.y + voxelHalfExtent) / (voxelHalfExtent * 2)),
            (int) Math.floor((position.z + voxelHalfExtent) / (voxelHalfExtent * 2)));
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
    public static Vector3i getVoxelIndexLocal(float voxelHalfExtent, Vector3d position, Vector3i voxelIndex) {
        // Using floor (round down) is actually very important. The explicit int-casting will round up for negative numbers.
        return voxelIndex.set((int) Math.floor((position.x + voxelHalfExtent) / (voxelHalfExtent * 2)),
            (int) Math.floor((position.y + voxelHalfExtent) / (voxelHalfExtent * 2)),
            (int) Math.floor((position.z + voxelHalfExtent) / (voxelHalfExtent * 2)));
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
    public static Vector3i getVoxelIndexLocal(double voxelHalfExtent, Vector3f position, Vector3i voxelIndex) {
        // Using floor (round down) is actually very important. The explicit int-casting will round up for negative numbers.
        return voxelIndex.set((int) Math.floor((position.x + voxelHalfExtent) / (voxelHalfExtent * 2)),
            (int) Math.floor((position.y + voxelHalfExtent) / (voxelHalfExtent * 2)),
            (int) Math.floor((position.z + voxelHalfExtent) / (voxelHalfExtent * 2)));
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
        // Using floor (round down) is actually very important. The explicit int-casting will round up for negative numbers.
        return voxelIndex.set((int) Math.floor((position.x + voxelHalfExtent) / (voxelHalfExtent * 2)),
            (int) Math.floor((position.y + voxelHalfExtent) / (voxelHalfExtent * 2)),
            (int) Math.floor((position.z + voxelHalfExtent) / (voxelHalfExtent * 2)));
    }

    private VoxelWorldUtils() {
    }
}
