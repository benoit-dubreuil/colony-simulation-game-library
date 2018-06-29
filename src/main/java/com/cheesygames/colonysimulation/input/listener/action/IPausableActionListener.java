package com.cheesygames.colonysimulation.input.listener.action;

import com.cheesygames.colonysimulation.GameGlobal;

/**
 * Interfaces {@link IOverrideActionListener} so that the {@link IOverrideActionListener#onAction(Enum, boolean, float)} method is only called when the game is paused.
 *
 * @param <E> The enum type for the mappings.
 */
public interface IPausableActionListener<E extends Enum<E>> extends IOverrideActionListener<E> {

    /**
     * Dictates that {@link IOverrideActionListener#onAction(String, boolean, float)} )} should execute {@link IOverrideActionListener#onAction(Enum, boolean, float)} only if the
     * game isn't paused.
     *
     * @return If the {@link IOverrideActionListener#onAction(Enum, boolean, float)} method should be executed when {@link IOverrideActionListener#onAction(String, boolean, float)}
     * is called.
     */
    @Override
    default boolean shouldExecuteOnAction() {
        return !GameGlobal.isPaused();
    }
}
