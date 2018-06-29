package com.cheesygames.colonysimulation.input.listener.action;

/**
 * Merge the inherited default {@link IOverrideActionListener#shouldExecuteOnAction()} method from {@link IPausableActionListener} and {@link IControlActionListener} into one. So,
 * {@link IEnumActionListener#onAction(Enum, boolean, float)} is only called when the game isn't paused and that the control is enabled.
 *
 * @param <E> The enum type for the mappings.
 */
public interface IPausableControlActionListener<E extends Enum<E>> extends IPausableActionListener<E>, IControlActionListener<E> {

    /**
     * Dictates that {@link IOverrideActionListener#onAction(String, boolean, float)} should execute {@link IOverrideActionListener#onAction(Enum, boolean, float)} only if the game
     * isn't paused and if the control is enabled.
     *
     * @return If the {@link IOverrideActionListener#onAction(Enum, boolean, float)} method should be executed when {@link IOverrideActionListener#onAction(String, boolean, float)}
     * is called.
     */
    @Override
    default boolean shouldExecuteOnAction() {
        return IPausableActionListener.super.shouldExecuteOnAction() && IControlActionListener.super.shouldExecuteOnAction();
    }
}
