package com.cheesygames.colonysimulation.mainthread;

import com.cheesygames.colonysimulation.jme3.Node.INode;
import com.jme3.scene.Node;

/**
 * Main thread event for adding a node to the world.
 */
public class AddToWorldEvent implements IMainThreadEvent {

    private Node m_node;

    public AddToWorldEvent(Node node) {
        this.m_node = node;

        // To allow the node to be right now in the physics space.
        INode.addToPhysicsSpace(node);
    }

    @Override
    public void execute() {
        INode.addToSceneGraph(m_node);
    }

    public Node getNode() {
        return m_node;
    }
}
