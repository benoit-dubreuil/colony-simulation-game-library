package com.cheesygames.colonysimulation.input.gameplayaction;

import com.cheesygames.colonysimulation.input.IActionInput;
import com.cheesygames.colonysimulation.reflection.IEnumCachedValues;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;

/**
 * The actions and their inputs for the player with the game globally while playing the game.
 */
public enum GameplayActionInput implements IActionInput {

    PAUSE(false, true, new KeyTrigger(KeyInput.KEY_ESCAPE));

    static {
        try {
            IEnumCachedValues.cacheValues(GameplayActionInput.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final Trigger[] triggers;
    private final boolean isAnalog;
    private final boolean isAction;

    GameplayActionInput(boolean isAnalog, boolean isAction, Trigger... triggers) {
        this.triggers = triggers;
        this.isAnalog = isAnalog;
        this.isAction = isAction;
    }

    @Override
    public Trigger[] getTriggers() {
        return triggers;
    }

    @Override
    public boolean isAnalog() {
        return isAnalog;
    }

    @Override
    public boolean isAction() {
        return isAction;
    }
}
