package com.cheesygames.colonysimulation.input.listener.action;

import com.cheesygames.colonysimulation.jme3.Node.IAbstractControl;

/**
 * Interfaces {@link IOverrideActionListener} so that the {@link IOverrideActionListener#onAction(Enum, boolean, float)} method is only called when the extending control is
 * enabled.
 *
 * @param <E> The enum type for the mappings.
 */
public interface IControlActionListener<E extends Enum<E>> extends IAbstractControl, IOverrideActionListener<E> {

    /**
     * Dictates that {@link IOverrideActionListener#onAction(String, boolean, float)} should execute {@link IOverrideActionListener#onAction(Enum, boolean, float)} only if the game
     * isn't paused.
     *
     * @return If the {@link IOverrideActionListener#onAction(Enum, boolean, float)} method should be executed when {@link IOverrideActionListener#onAction(String, boolean, float)}
     * is called.
     */
    @Override
    default boolean shouldExecuteOnAction() {
        return isEnabled();
    }
}
