package com.cheesygames.colonysimulation.asset.loader.behaviortree;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;
import com.cheesygames.colonysimulation.asset.AssetType;
import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The <code>AssetLoader</code> for LibGDX-AI's behavior tree text format.
 */
public class BehaviorTreeAssetLoader implements AssetLoader {

    public static final AssetType ASSET_TYPE = AssetType.BEHAVIOR;

    @Override
    public BehaviorTreeAsset load(AssetInfo assetInfo) throws IOException {
        BehaviorTreeAsset treeAsset = null;
        InputStreamReader in = null;

        try {
            in = new InputStreamReader(assetInfo.openStream());
            BehaviorTreeParser parser = new BehaviorTreeParser(BehaviorTreeParser.DEBUG_NONE);
            BehaviorTree tree = parser.parse(in, null);

            treeAsset = new BehaviorTreeAsset(tree);
            treeAsset.setKey(assetInfo.getKey());
        } finally {
            if (in != null) {
                in.close();
            }
        }

        return treeAsset;
    }
}
