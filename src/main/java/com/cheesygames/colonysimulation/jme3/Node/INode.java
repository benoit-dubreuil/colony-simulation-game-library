package com.cheesygames.colonysimulation.jme3.Node;

import com.cheesygames.colonysimulation.GameGlobal;
import com.cheesygames.colonysimulation.mainthread.*;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Provides utilities for handling a node (itself) and interacting with the game.
 */
public interface INode {

    /**
     * Adds the node to the rootNode (visual) and to the physicsSpace (physics). It does not need to be called from the main thread; it will automatically be added to the
     * MainThreadEventPool if it's called from another thread or if the physics is sequential.
     *
     * @param node The node to add to both physical and visual worlds.
     */
    static void addToWorld(Node node) {
        if (GameGlobal.canThreadUpdateSceneGraph()) {
            addToSceneGraph(node);
            addToPhysicsSpace(node);
        }
        else {
            MainThreadEventPool.addEvent(new AddToWorldEvent(node));
        }
    }

    /**
     * Adds the child node to the parent node (visual) and to the physicsSpace (physics). It does not need to be called from the main thread; it will automatically be added to the
     * MainThreadEventPool if it's called from another thread or if the physics is sequential.
     *
     * @param child  The children node to attach to both physical and the parent node.
     * @param parent The parent node to which we'll attach the child to.
     */
    static void attachToParent(Node child, Node parent) {
        if (GameGlobal.canThreadUpdateSceneGraph()) {
            parent.attachChild(child);
            addToPhysicsSpace(child);
        }
        else {
            MainThreadEventPool.addEvent(new AttachToParentEvent(child, parent));
        }
    }

    /**
     * Removes the node to the rootNode (visual) and to the physicsSpace (physics). It does not need to be called from the main thread; it will automatically be added to the
     * MainThreadEventPool if it's called from another thread or if the physics is sequential.
     *
     * @param node The node to remove to both physical and visual worlds.
     */
    static void removeFromWorld(Node node) {
        if (GameGlobal.canThreadUpdateSceneGraph()) {
            removeFromSceneGraph(node);
            removeFromPhysicsSpace(node);
        }
        else {
            MainThreadEventPool.addEvent(new RemoveFromWorldEvent(node));
        }
    }

    /**
     * Removes the child node fom the parent node (visual) and from the physicsSpace (physics). It does not need to be called from the main thread; it will automatically be added
     * to the MainThreadEventPool if it's called from another thread or if the physics is sequential.
     *
     * @param child  The children node to detach to both physical and the parent node.
     * @param parent The parent node to which we'll detach the child form.
     */
    static void detachToParent(Node child, Node parent) {
        if (GameGlobal.canThreadUpdateSceneGraph()) {
            parent.detachChild(child);
            removeFromPhysicsSpace(child);
        }
        else {
            MainThreadEventPool.addEvent(new DetachFromParentEvent(child, parent));
        }
    }

    static void addToSceneGraph(Node node) {
        if (node.getParent() == null) {
            if (node instanceof GameNode) {
                ((GameNode) node).getGameNodeType().getNode().attachChild(node);
            }
            else {
                GameGlobal.rootNode.attachChild(node);
            }
        }
    }

    static void addToPhysicsSpace(Node node) {
        // TODO : Physics
        // GameGlobal.physicsSpace.addAll(node);
    }

    /**
     * Removes the node from the scene graph. Must be executed from the main(render) thread.
     *
     * @param node The node to remove from the scene graph and the physical world.
     */
    static void removeFromSceneGraph(Node node) {
        if (node.getParent() != null) {
            node.getParent().detachChild(node);
        }
    }

    /**
     * Removes the node from the physics space.
     *
     * @param node The node to remove from the physics space.
     */
    static void removeFromPhysicsSpace(Node node) {
        // TODO : Physics
        // GameGlobal.physicsSpace.removeAll(node);
    }

    /**
     * Executes a process on the material of all children in the supplied node's hierarchy, including sub-nodes.
     *
     * @param traversingNode        The currently traversing node.
     * @param processOnEachMaterial The process to execute on a child material.
     */
    static void forEachMaterial(Node traversingNode, Consumer<Material> processOnEachMaterial) {
        List<Spatial> children = traversingNode.getChildren();
        for (Spatial child : children) {
            if (child instanceof Geometry) {
                processOnEachMaterial.accept(((Geometry) child).getMaterial());
            }
            else {
                forEachMaterial((Node) child, processOnEachMaterial);
            }
        }
    }

    /**
     * Gets the first parent in the spatial's super hierarchy that respects the conditionToReturn function.
     *
     * @param spatial           The spatial to get its parents.
     * @param conditionToReturn The condition to evaluate to check if the parent is the one that is actually wanted.
     * @param <P>               The parent type.
     *
     * @return The Parent node.
     */
    static <P extends Node> P getParentUntil(Spatial spatial, Function<Node, Boolean> conditionToReturn) {
        Node currentNode;
        if (spatial instanceof Geometry) {
            currentNode = spatial.getParent();
        }
        else {
            currentNode = (Node) spatial;
        }

        while (currentNode != null) {
            if (conditionToReturn.apply(currentNode)) {
                return (P) currentNode;
            }
            else {
                currentNode = currentNode.getParent();
            }
        }

        return null;
    }
}
