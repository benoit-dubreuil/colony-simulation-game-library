package com.cheesygames.colonysimulation.event;

import com.cheesygames.colonysimulation.jme3.Node.INode;
import com.jme3.scene.Node;

/**
 * An event to remove a node from the world.
 */
public class RemoveFromWorldEvent implements IMainThreadEvent {

    private Node m_node;

    public RemoveFromWorldEvent(Node node) {
        this.m_node = node;

        // To prevent the node's physics to be updated before the next frame.
        INode.removeFromPhysicsSpace(node);
    }

    @Override
    public void execute() {
        INode.removeFromSceneGraph(m_node);
    }

    public Node getNode() {
        return m_node;
    }
}
