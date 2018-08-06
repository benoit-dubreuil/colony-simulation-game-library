package com.cheesygames.colonysimulation.input.gameplayaction;

import com.cheesygames.colonysimulation.GameGlobal;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;

/**
 * Listens to the input events from {@link GameplayActionInput}. Should be used inside the {@link com.cheesygames.colonysimulation.game.input.ActionInputAppState} app state.
 */
public class GameplayActionListener implements AnalogListener, ActionListener {

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        try {
            GameplayActionInput gameplayAction = GameplayActionInput.valueOf(name);

            switch (gameplayAction) {
                case PAUSE:
                    if (!isPressed) {
                        GameGlobal.setPaused(!GameGlobal.isPaused());
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {

    }
}
