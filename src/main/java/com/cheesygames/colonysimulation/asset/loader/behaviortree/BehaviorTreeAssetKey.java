package com.cheesygames.colonysimulation.asset.loader.behaviortree;

import com.jme3.asset.AssetKey;
import com.jme3.asset.cache.AssetCache;
import com.jme3.asset.cache.WeakRefCloneAssetCache;

/**
 * Asset key for LibGDX-AI's behavior tree. Tells jme3 it should clones new assets from the cache and what is the asset's processor.
 */
public class BehaviorTreeAssetKey extends AssetKey<BehaviorTreeAsset> {

    public BehaviorTreeAssetKey(String name) {
        super(name);
    }

    public BehaviorTreeAssetKey() {
        super();
    }

    @Override
    public Class<? extends AssetCache> getCacheType() {
        return WeakRefCloneAssetCache.class;
    }

    @Override
    public Class<BehaviorTreeAssetProcessor> getProcessorType() {
        return BehaviorTreeAssetProcessor.class;
    }
}
