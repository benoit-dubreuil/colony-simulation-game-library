package com.cheesygames.colonysimulation.input.listener.action;

import com.jme3.input.controls.AnalogListener;

/**
 * Interface to be extended by other interfaces to override the call logic of the {@link AnalogListener}.
 *
 * @param <E> The enum type for the mappings.
 */
public interface IOverrideActionListener<E extends Enum<E>> extends IEnumActionListener<E> {

    /**
     * Checks if the {@link IEnumActionListener#onAction(Enum, boolean, float)} method should executed when {@link IEnumActionListener#onAction(String, boolean, float)} is called.
     *
     * @return If the {@link IEnumActionListener#onAction(Enum, boolean, float)} method should be executed when {@link IEnumActionListener#onAction(String, boolean, float)} is
     * called.
     */
    boolean shouldExecuteOnAction();

    /**
     * Checks if {@link IOverrideActionListener#shouldExecuteOnAction()} is true before executing {@link IEnumActionListener#onAction(Enum, boolean, float)}.
     * <p>
     * Called to notify the implementation that an action event has occurred.
     * <p>
     * The results of KeyTrigger and MouseButtonTrigger events will have tpf == value.
     *
     * @param name      The name of the mapping that was invoked
     * @param isPressed True if the action is "pressed", false otherwise
     * @param tpf       The time per frame value.
     *
     * @see com.jme3.input.controls.ActionListener
     */
    default void onAction(String name, boolean isPressed, float tpf) {
        if (shouldExecuteOnAction()) {
            IEnumActionListener.super.onAction(name, isPressed, tpf);
        }
    }
}
