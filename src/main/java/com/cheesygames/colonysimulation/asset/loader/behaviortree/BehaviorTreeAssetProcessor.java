package com.cheesygames.colonysimulation.asset.loader.behaviortree;

import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetProcessor;

/**
 * Asset processor for LibGDX-AI's behavior tree. It is needed to automatically clone the loaded asset that is in the cache.
 */
public class BehaviorTreeAssetProcessor implements AssetProcessor {

    @Override
    public BehaviorTreeAsset postProcess(AssetKey key, Object behaviorTreeAsset) {
        return (BehaviorTreeAsset) behaviorTreeAsset;
    }

    @Override
    public BehaviorTreeAsset createClone(Object behaviorTreeAsset) {
        return ((BehaviorTreeAsset) behaviorTreeAsset).clone();
    }
}
