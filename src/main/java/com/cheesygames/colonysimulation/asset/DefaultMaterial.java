package com.cheesygames.colonysimulation.asset;

import com.jme3.asset.MaterialKey;

/**
 * All the provided materials by JME3.
 */
public enum DefaultMaterial implements IAsset {
    UNSHADED("Common/MatDefs/Misc/Unshaded.j3md"),
    SKY("Common/MatDefs/Misc/Sky.j3md"),
    TERRAIN("Common/MatDefs/Terrain/Terrain.j3md"),
    HEIGHT_BASED_TERRAIN("Common/MatDefs/Terrain/HeightBasedTerrain.j3md"),
    PARTICLE("Common/MatDefs/Misc/Particle.j3md"),
    LIGHTING("Common/MatDefs/Light/Lighting.j3md"),
    TERRAIN_LIGHTING("Common/MatDefs/Terrain/TerrainLighting.j3md"),
    REFLECTION("Common/MatDefs/Light/Reflection.j3md"),
    SHOW_NORMALS("Common/MatDefs/Misc/ShowNormals.j3md");

    private final String m_path;

    private final MaterialKey m_assetKey;

    DefaultMaterial(String path) {
        this.m_path = path;
        this.m_assetKey = AssetType.MATERIAL.createAssetKey(path);
    }

    @Override
    public AssetType getAssetType() {
        return AssetType.MATERIAL_DEFINITION;
    }

    @Override
    public String getPath() {
        return m_path;
    }

    @SuppressWarnings("unchecked")
    @Override
    public MaterialKey getAssetKey() {
        return m_assetKey;
    }
}
