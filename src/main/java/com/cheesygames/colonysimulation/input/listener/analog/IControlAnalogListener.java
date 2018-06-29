package com.cheesygames.colonysimulation.input.listener.analog;

import com.cheesygames.colonysimulation.jme3.Node.IAbstractControl;

/**
 * Interfaces {@link IOverrideAnalogListener} so that the {@link IOverrideAnalogListener#onAnalog(Enum, float, float)} method is only called when the extending control is enabled.
 *
 * @param <E> The enum type for the mappings.
 */
public interface IControlAnalogListener<E extends Enum<E>> extends IAbstractControl, IOverrideAnalogListener<E> {

    /**
     * Dictates that {@link IOverrideAnalogListener#onAnalog(String, float, float)} should execute {@link IOverrideAnalogListener#onAnalog(Enum, float, float)} only if the game
     * isn't paused.
     *
     * @return If the {@link IOverrideAnalogListener#onAnalog(Enum, float, float)} method should be executed when {@link IOverrideAnalogListener#onAnalog(String, float, float)} is
     * called.
     */
    @Override
    default boolean shouldExecuteOnAnalog() {
        return isEnabled();
    }
}
