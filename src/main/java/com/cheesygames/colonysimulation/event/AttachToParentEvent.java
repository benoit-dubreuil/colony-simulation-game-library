package com.cheesygames.colonysimulation.event;

import com.cheesygames.colonysimulation.jme3.Node.INode;
import com.jme3.scene.Node;

/**
 * Main thread event for attaching a child node to a parent node.
 */
public class AttachToParentEvent implements IMainThreadEvent {

    private Node m_childNode;
    private Node m_parentNode;

    public AttachToParentEvent(Node childNode, Node parentNode) {

        m_childNode = childNode;
        m_parentNode = parentNode;
    }

    @Override
    public void execute() {
        m_parentNode.attachChild(m_childNode);
        INode.addToPhysicsSpace(m_childNode);
    }

    public Node getChildNode() {
        return m_childNode;
    }

    public Node getParentNode() {
        return m_parentNode;
    }
}
