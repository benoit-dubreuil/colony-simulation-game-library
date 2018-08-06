package com.cheesygames.colonysimulation.input.gameplayaction;

import com.cheesygames.colonysimulation.input.ActionInputAppState;

/**
 * App state that interfaces in a modular way gameplay input and control classes.
 */
public class GameplayActionInputAppState extends ActionInputAppState<GameplayActionInput> {

    public GameplayActionInputAppState() {
        this(new GameplayActionListener());
    }

    public GameplayActionInputAppState(GameplayActionListener gameplayActionListener) {
        super(GameplayActionInput.class, gameplayActionListener, gameplayActionListener);
    }
}
