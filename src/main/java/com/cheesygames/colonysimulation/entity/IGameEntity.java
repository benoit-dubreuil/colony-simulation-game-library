package com.cheesygames.colonysimulation.entity;

import com.cheesygames.colonysimulation.animation.BoneConvention;
import com.cheesygames.colonysimulation.jme3.Node.INode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

/**
 * Interface for a game entity. Provides methods to deal with the game entity's model.
 */
public interface IGameEntity {

    /**
     * Attaches itself to the supplied game entity at the specified attachment bone.
     *
     * @param gameEntity The game entity to attach itself to.
     * @param attachment The bone where to attach itself.
     */
    default void attachToGameEntity(GameEntity gameEntity, BoneConvention attachment) {
        Node attachmentsNode = attachment.getAttachmentsNode(gameEntity);

        if (!attachmentsNode.hasChild((GameEntity) this)) {
            beforeAttachedToGameEntity(gameEntity, attachment);
            gameEntity.beforeAttachedGameEntity((GameEntity) this, attachment);

            attachmentsNode.attachChild((GameEntity) this);

            attachedToGameEntity(gameEntity, attachment);
            gameEntity.attachedGameEntity((GameEntity) this, attachment);
        }
    }

    /**
     * The logic to execute before when this game entity is attached to another game entity.
     *
     * @param gameEntity The game entity it will be soon attached to.
     * @param attachment Where it will be attached to on the game entity.
     */
    default void beforeAttachedToGameEntity(GameEntity gameEntity, BoneConvention attachment) {
    }

    /**
     * The logic to execute before when a game entity is attached to this game entity.
     *
     * @param gameEntity The game entity that will be soon attached to this one.
     * @param attachment Where the game entity will be attached onto this one.
     */
    default void beforeAttachedGameEntity(GameEntity gameEntity, BoneConvention attachment) {
    }

    /**
     * The logic to execute when this game entity was attached to another game entity.
     *
     * @param gameEntity The game entity to attach itself to.
     * @param attachment Where to attach itself on the game entity.
     */
    default void attachedToGameEntity(GameEntity gameEntity, BoneConvention attachment) {
    }

    /**
     * The logic to execute when a game entity is attached to this game entity.
     *
     * @param gameEntity The newly attached game entity.
     * @param attachment Where the game entity was attached.~
     */
    default void attachedGameEntity(GameEntity gameEntity, BoneConvention attachment) {
    }

    /**
     * Attaches a game entity to this game entity's attachment bone.
     *
     * @param gameEntity The game entity to attach.
     * @param attachment The attachment bone from where to attach the game entity.
     */
    default void attachGameEntity(GameEntity gameEntity, BoneConvention attachment) {
        Node attachmentsNode = attachment.getAttachmentsNode(this);

        if (!attachmentsNode.hasChild(gameEntity)) {

            gameEntity.beforeAttachedToGameEntity((GameEntity) this, attachment);
            beforeAttachedGameEntity(gameEntity, attachment);

            INode.attachToParent(gameEntity, attachmentsNode);

            gameEntity.attachedToGameEntity((GameEntity) this, attachment);
            attachedGameEntity(gameEntity, attachment);
        }
    }

    /**
     * Detaches a specific game entity from this game entity's attachment bone.
     *
     * @param gameEntity The game entity to detach.
     * @param attachment The attachment bone from where to detach the game entity.
     *
     * @return The index where the game entity was in the model's skeleton attachment hierarchy. -1 if the game entity was not in the list.
     */
    default int detachGameEntity(GameEntity gameEntity, BoneConvention attachment) {
        int detachResult = -1;
        Node attachmentsNode = attachment.getAttachmentsNode(this);

        if (attachmentsNode.hasChild(gameEntity)) {
            gameEntity.beforeDetachedFromGameEntity((GameEntity) this, attachment);
            beforeDetachedGameEntity(gameEntity, attachment);

            detachResult = attachmentsNode.detachChild(gameEntity);

            gameEntity.detachedFromGameEntity((GameEntity) this, attachment);
            detachedGameEntity(gameEntity, attachment);
        }

        return detachResult;
    }

    /**
     * The logic to execute before this game entity is detached from another game entity.
     *
     * @param gameEntity The game entity it will be detached from.
     * @param attachment Where it is attached.
     */
    default void beforeDetachedFromGameEntity(GameEntity gameEntity, BoneConvention attachment) {
    }

    /**
     * The logic to execute before a game entity is detached from this one.
     *
     * @param gameEntity The game entity that will be detached.
     * @param attachment Where it is attached.
     */
    default void beforeDetachedGameEntity(GameEntity gameEntity, BoneConvention attachment) {
    }

    /**
     * The logic to execute when this game entity is detached from another game entity.
     *
     * @param gameEntity The game entity it was detached from.
     * @param attachment Where it was attached.
     */
    default void detachedFromGameEntity(GameEntity gameEntity, BoneConvention attachment) {
    }

    /**
     * The logic to execute when a game entity was detached from this one.
     *
     * @param gameEntity The detached game entity.
     * @param attachment Where it was attached.
     */
    default void detachedGameEntity(GameEntity gameEntity, BoneConvention attachment) {
    }

    /**
     * Detaches a game entity at the supplied index in this game entity's model's skeleton attachment hierarchy.
     *
     * @param index      The index of the game entity to be removed from this game entity's model's skeleton attachment hierarchy.
     * @param attachment The attachment bone from where to detach the game entity.
     *
     * @return The detached game entity. Null if it was non-existent.
     */
    default GameEntity detachGameEntity(int index, BoneConvention attachment) {
        Node attachmentsNode = attachment.getAttachmentsNode(this);
        GameEntity gameEntity = (GameEntity) attachmentsNode.getChild(index);

        gameEntity.beforeDetachedFromGameEntity((GameEntity) this, attachment);
        beforeDetachedGameEntity(gameEntity, attachment);

        attachmentsNode.detachChildAt(index);

        gameEntity.detachedFromGameEntity((GameEntity) this, attachment);
        detachedGameEntity(gameEntity, attachment);

        return gameEntity;
    }

    /**
     * Detaches the only game entity in this game entity's model's skeleton attachment hierarchy.
     *
     * @param attachment The attachment bone from where to detach the game entity.
     *
     * @return The detached game entity. Null if it was non-existent.
     */
    default GameEntity detachGameEntity(BoneConvention attachment) {
        Node attachmentsNode = attachment.getAttachmentsNode(this);
        GameEntity gameEntity = (GameEntity) attachmentsNode.getChild(0);

        gameEntity.beforeDetachedFromGameEntity((GameEntity) this, attachment);
        beforeDetachedGameEntity(gameEntity, attachment);

        attachmentsNode.detachChildAt(0);

        gameEntity.detachedFromGameEntity((GameEntity) this, attachment);
        detachedGameEntity(gameEntity, attachment);

        return gameEntity;
    }

    /**
     * Adds a control to this game entity's model.
     *
     * @param control The control to add.
     */
    default void addModelControl(Control control) {
        getModel().addControl(control);
    }

    /**
     * Gets a specific control from this game entity's model.
     *
     * @param modelControl The class of the control to get.
     * @param <C>          The type of the control to get.
     *
     * @return The specified control.
     */
    default <C extends Control> C getModelControl(Class<C> modelControl) {
        return getModel().getControl(modelControl);
    }

    /**
     * Gets this game entity's model.
     *
     * @return This game entity's model.
     */
    Spatial getModel();
}
