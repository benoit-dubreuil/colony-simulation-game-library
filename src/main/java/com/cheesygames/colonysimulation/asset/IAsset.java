package com.cheesygames.colonysimulation.asset;

import com.jme3.asset.AssetKey;

/**
 * Interface for asset enums.
 */
public interface IAsset {

    AssetType getAssetType();

    String getPath();

    <T extends AssetKey<?>> T getAssetKey();
}
