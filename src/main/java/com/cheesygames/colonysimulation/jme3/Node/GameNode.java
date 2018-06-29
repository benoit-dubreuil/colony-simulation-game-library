package com.cheesygames.colonysimulation.jme3.Node;

import com.cheesygames.colonysimulation.GameGlobal;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SafeArrayList;

import java.util.function.Consumer;

/**
 * A node that can easily be added and removed from the game.
 */
public class GameNode extends Node implements INode {

    protected GameNodeType m_gameNodeType;

    public GameNode() {
        this.m_gameNodeType = GameNodeType.OTHER;
    }

    public GameNode(String name) {
        super(name);
        this.m_gameNodeType = GameNodeType.OTHER;
    }

    public GameNode(GameNodeType gameNodeType) {
        this.m_gameNodeType = gameNodeType;
    }

    public GameNode(String name, GameNodeType gameNodeType) {
        super(name);
        this.m_gameNodeType = gameNodeType;
    }

    /**
     * Adds the unit to the rootNode (visual) and to the physicsSpace (physics).
     */
    public void addToWorld() {
        INode.addToWorld(this);
    }

    /**
     * Removes the unit from the rootNode (visual) and from the physicsSpace (physics).
     */
    public void removeFromWorld() {
        INode.removeFromWorld(this);
    }

    /**
     * Executes a process on the material of all children in this node's hierarchy, including sub-nodes.
     *
     * @param processOnEachMaterial The process to execute on a child material.
     */
    public void forEachMaterial(Consumer<Material> processOnEachMaterial) {
        INode.forEachMaterial(this, processOnEachMaterial);
    }

    @Override
    protected void addUpdateChildren(SafeArrayList<Spatial> results) {
        if (!GameGlobal.isPaused()) {
            super.addUpdateChildren(results);
        }
    }

    @Override
    public void updateLogicalState(float tpf) {
        if (!GameGlobal.isPaused()) {
            super.updateLogicalState(tpf);
        }
    }

    @Override
    protected boolean requiresUpdates() {
        return !GameGlobal.isPaused() && super.requiresUpdates();
    }

    public GameNodeType getGameNodeType() {
        return m_gameNodeType;
    }
}
