package com.cheesygames.colonysimulation.input.listener.analog;

import com.cheesygames.colonysimulation.GameGlobal;

/**
 * Interfaces {@link IOverrideAnalogListener} so that the {@link IOverrideAnalogListener#onAnalog(Enum, float, float)} method is only called when the game is paused.
 *
 * @param <E> The enum type for the mappings.
 */
public interface IPausableAnalogListener<E extends Enum<E>> extends IOverrideAnalogListener<E> {

    /**
     * Dictates that {@link IOverrideAnalogListener#onAnalog(String, float, float)} should execute {@link IOverrideAnalogListener#onAnalog(Enum, float, float)} only if the game
     * isn't paused.
     *
     * @return If the {@link IOverrideAnalogListener#onAnalog(Enum, float, float)} method should be executed when {@link IOverrideAnalogListener#onAnalog(String, float, float)} is
     * called.
     */
    @Override
    default boolean shouldExecuteOnAnalog() {
        return !GameGlobal.isPaused();
    }
}
