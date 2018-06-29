package com.cheesygames.colonysimulation.jme3.Node;

import com.cheesygames.colonysimulation.GameGlobal;
import com.cheesygames.colonysimulation.reflection.IEnumCachedValues;
import com.jme3.scene.Node;

/**
 * The super types of game nodes. Every value in this enum is a static node in the GameGlobal static class, except for the value OTHER, which redirects to the root node.
 */
public enum GameNodeType {
    TERRAIN() {
        @Override
        public Node getNode() {
            return GameGlobal.terrainNode;
        }
    },
    UNIT() {
        @Override
        public Node getNode() {
            return GameGlobal.unitNode;
        }
    },
    ENTITY() {
        @Override
        public Node getNode() {
            return GameGlobal.entityNode;
        }
    },
    OTHER() {
        @Override
        public Node getNode() {
            return GameGlobal.rootNode;
        }
    };

    static {
        try {
            IEnumCachedValues.cacheValues(GameNodeType.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    GameNodeType() {
    }

    public abstract Node getNode();
}
