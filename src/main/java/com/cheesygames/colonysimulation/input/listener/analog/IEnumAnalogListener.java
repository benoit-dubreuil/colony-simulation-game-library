package com.cheesygames.colonysimulation.input.listener.analog;

import com.jme3.input.controls.AnalogListener;

/**
 * Interfaces the {@link AnalogListener} so that it takes into account the enum that represents the name of the mapping.
 *
 * @param <E> The enum type for the mappings.
 */
public interface IEnumAnalogListener<E extends Enum<E>> extends AnalogListener {

    /**
     * Gets the value of the supplied mapping name as an enum value and then call {@link #onAnalog(Enum, float, float)} with that value.
     *
     * @param name  The name of the mapping that was invoked
     * @param value Value of the axis, from 0 to 1.
     * @param tpf   The time per frame value.
     */
    @Override
    default void onAnalog(String name, float value, float tpf) {
        E enumValue;

        try {
            enumValue = Enum.valueOf(getAnalogListenerEnumClass(), name);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return;
        }

        onAnalog(enumValue, value, tpf);
    }

    /**
     * Called to notify the implementation that an analog event has occurred.
     *
     * @param enumValue The enum value representing the mapping that was invoked.
     * @param value     Value of the axis, from 0 to 1.
     * @param tpf       The time per frame value.
     */
    void onAnalog(E enumValue, float value, float tpf);

    Class<E> getAnalogListenerEnumClass();
}
