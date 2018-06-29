package com.cheesygames.colonysimulation.input.listener.analog;

/**
 * Merge the inherited default {@link IOverrideAnalogListener#shouldExecuteOnAnalog()} method from {@link IPausableAnalogListener} and {@link IControlAnalogListener} into one. So,
 * {@link IEnumAnalogListener#onAnalog(Enum, float, float)} is only called when the game isn't paused and that the control is enabled.
 *
 * @param <E> The enum type for the mappings.
 */
public interface IPausableControlAnalogListener<E extends Enum<E>> extends IPausableAnalogListener<E>, IControlAnalogListener<E> {

    /**
     * Dictates that {@link IOverrideAnalogListener#onAnalog(String, float, float)} should execute {@link IOverrideAnalogListener#onAnalog(Enum, float, float)} only if the game
     * isn't paused and if the control is enabled.
     *
     * @return If the {@link IOverrideAnalogListener#onAnalog(Enum, float, float)} method should be executed when {@link IOverrideAnalogListener#onAnalog(String, float, float)} is
     * called.
     */
    @Override
    default boolean shouldExecuteOnAnalog() {
        return IPausableAnalogListener.super.shouldExecuteOnAnalog() && IControlAnalogListener.super.shouldExecuteOnAnalog();
    }
}
