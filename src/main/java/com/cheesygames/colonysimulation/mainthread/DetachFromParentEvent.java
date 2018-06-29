package com.cheesygames.colonysimulation.mainthread;

import com.cheesygames.colonysimulation.jme3.Node.INode;
import com.jme3.scene.Node;

/**
 * Main thread event for detaching a child node from a parent node.
 */
public class DetachFromParentEvent implements IMainThreadEvent {

    private Node m_childNode;
    private Node m_parentNode;

    public DetachFromParentEvent(Node childNode, Node parentNode) {

        m_childNode = childNode;
        m_parentNode = parentNode;
    }

    @Override
    public void execute() {
        m_parentNode.detachChild(m_childNode);
        INode.removeFromPhysicsSpace(m_childNode);
    }

    public Node getChildNode() {
        return m_childNode;
    }

    public Node getParentNode() {
        return m_parentNode;
    }
}
