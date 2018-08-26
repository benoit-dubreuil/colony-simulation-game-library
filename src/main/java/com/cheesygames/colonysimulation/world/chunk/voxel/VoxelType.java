package com.cheesygames.colonysimulation.world.chunk.voxel;

/**
 * All voxel types.
 */
public enum VoxelType {
    AIR(false),
    SOLID,
    LIGHT(true, 0x0FF);

    private final boolean m_isSolid;
    private final int m_light;

    VoxelType() {
        m_isSolid = true;
        m_light = 0;
    }

    VoxelType(boolean isSolid) {
        m_isSolid = isSolid;
        m_light = 0;
    }

    VoxelType(boolean isSolid, int light) {
        m_isSolid = isSolid;
        m_light = light;
    }

    public boolean isSolid() {
        return m_isSolid;
    }

    public boolean emitsLight() {
        return m_light != 0;
    }

    public int getLight() {
        return m_light;
    }
}
