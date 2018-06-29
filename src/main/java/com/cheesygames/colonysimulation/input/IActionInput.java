package com.cheesygames.colonysimulation.input;

import com.jme3.input.controls.Trigger;

/**
 * Interface for all enums that represents player actions associated to inputs.
 */
public interface IActionInput {

    /**
     * Gets all the triggers that will prompt the action identified by the enum.
     *
     * @return All triggers.
     */
    Trigger[] getTriggers();

    /**
     * Gets if the action is analog, i.e. continuous.
     *
     * @return If the action is continuous.
     */
    boolean isAnalog();

    /**
     * Gets if the action is not continuous.
     *
     * @return If the action is not continuous.
     */
    boolean isAction();
}
