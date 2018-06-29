package com.cheesygames.colonysimulation.asset.loader.behaviortree;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.jme3.asset.AssetKey;
import com.jme3.asset.CloneableSmartAsset;

/**
 * Asset for LibGDX-AI'S behavior tree. Encapsulate a behavior tree and its asset key. Allows cloning for automatically cloning.
 */
public class BehaviorTreeAsset implements CloneableSmartAsset {

    private BehaviorTree m_behaviorTree;
    private BehaviorTreeAssetKey m_key;

    public BehaviorTreeAsset(BehaviorTree behaviorTree) {
        m_behaviorTree = behaviorTree;
    }

    @Override
    public BehaviorTreeAsset clone() {
        return new BehaviorTreeAsset((BehaviorTree) m_behaviorTree.cloneTask());
    }

    @Override
    public BehaviorTreeAssetKey getKey() {
        return m_key;
    }

    @Override
    public void setKey(AssetKey key) {
        this.m_key = (BehaviorTreeAssetKey) key;
    }

    public BehaviorTree getBehaviorTree() {
        return m_behaviorTree;
    }
}
