package com.cheesygames.colonysimulation.input.listener.action;

import com.jme3.input.controls.ActionListener;

/**
 * Interfaces the {@link ActionListener} so that it takes into account the enum that represents the name of the mapping.
 *
 * @param <E> The enum type for the mappings.
 */
public interface IEnumActionListener<E extends Enum<E>> extends ActionListener {

    /**
     * Gets the value of the supplied mapping name as an enum value and then call {@link #onAction(Enum, boolean, float)} with that value.
     *
     * @param name      The name of the mapping that was invoked
     * @param isPressed True if the action is "pressed", false otherwise
     * @param tpf       The time per frame value.
     */
    @Override
    default void onAction(String name, boolean isPressed, float tpf) {
        E enumValue;

        try {
            enumValue = Enum.valueOf(getActionListenerEnumClass(), name);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return;
        }

        onAction(enumValue, isPressed, tpf);
    }

    /**
     * Called to notify the implementation that an action event has occurred.
     *
     * @param enumValue The enum value representing the mapping that was invoked.
     * @param isPressed True if the action is "pressed", false otherwise
     * @param tpf       The time per frame value.
     */
    void onAction(E enumValue, boolean isPressed, float tpf);

    Class<E> getActionListenerEnumClass();
}
