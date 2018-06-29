package com.cheesygames.colonysimulation.input.listener.analog;

import com.jme3.input.controls.AnalogListener;

/**
 * Interface to be extended by other interfaces to override the call logic of the {@link AnalogListener}.
 *
 * @param <E> The enum type for the mappings.
 */
public interface IOverrideAnalogListener<E extends Enum<E>> extends IEnumAnalogListener<E> {

    /**
     * Checks if the {@link IEnumAnalogListener#onAnalog(Enum, float, float)} method should executed when {@link IEnumAnalogListener#onAnalog(String, float, float)} is called.
     *
     * @return If the {@link IEnumAnalogListener#onAnalog(Enum, float, float)} method should be executed when {@link IEnumAnalogListener#onAnalog(String, float, float)} is called.
     */
    boolean shouldExecuteOnAnalog();

    /**
     * Checks if {@link IOverrideAnalogListener#shouldExecuteOnAnalog()} is true before executing {@link IEnumAnalogListener#onAnalog(Enum, float, float)} .
     * <p>
     * Called to notify the implementation that an analog event has occurred.
     * <p>
     * The results of KeyTrigger and MouseButtonTrigger events will have tpf == value.
     *
     * @param name  The name of the mapping that was invoked
     * @param value Value of the axis, from 0 to 1.
     * @param tpf   The time per frame value.
     *
     * @see AnalogListener
     */
    @Override
    default void onAnalog(String name, float value, float tpf) {
        if (shouldExecuteOnAnalog()) {
            IEnumAnalogListener.super.onAnalog(name, value, tpf);
        }
    }
}
