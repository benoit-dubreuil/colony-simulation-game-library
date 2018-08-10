package com.cheesygames.colonysimulation.world.chunk.voxel;

/**
 * All voxel types.
 */
public enum VoxelType {
    AIR(false),
    SOLID,
    LIGHT(true, 0x0FF);

    private final boolean m_isSolid;
    private final boolean m_emitsLight;
    private final int m_light;

    VoxelType() {
        m_isSolid = true;
        m_emitsLight = false;
        m_light = 0;
    }

    VoxelType(boolean isSolid) {
        m_isSolid = isSolid;
        m_emitsLight = false;
        m_light = 0;
    }

    VoxelType(boolean emitsLight, int light) {
        m_isSolid = true;
        m_emitsLight = emitsLight;
        m_light = light;
    }

    VoxelType(boolean isSolid, boolean emitsLight, int light) {
        m_isSolid = isSolid;
        m_emitsLight = emitsLight;
        m_light = light;
    }

    public boolean isSolid() {
        return m_isSolid;
    }

    public boolean emitsLight() {
        return m_emitsLight;
    }

    public int getLight() {
        return m_light;
    }
}
