package com.cheesygames.colonysimulation.input;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;

/**
 * AppState for a specific set of player actions associated to inputs. It interfaces in a modular way the inputs needed to perform certain player actions.
 *
 * @param <E> An enum that implements the IActionInput. It's an enum that represents player actions associated to inputs.
 */
public abstract class ActionInputAppState<E extends Enum<E> & IActionInput> extends AbstractAppState implements AnalogListener, ActionListener {

    private InputManager m_inputManager;
    private Class<E> m_playerActionInputClass;
    private AnalogListener m_analogListener;
    private ActionListener m_actionListener;

    public ActionInputAppState(Class<E> playerActionInputClass, AnalogListener analogListener, ActionListener actionListener) {
        this.m_playerActionInputClass = playerActionInputClass;
        this.m_analogListener = analogListener;
        this.m_actionListener = actionListener;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.m_inputManager = app.getInputManager();
        addInputMappings();
        addInputListeners();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        deleteMappings();
        removeListeners();
        m_inputManager = null;
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (isEnabled()) {
            m_actionListener.onAction(name, isPressed, tpf);
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (isEnabled()) {
            m_analogListener.onAnalog(name, value, tpf);
        }
    }

    private void addInputMappings() {
        E[] playerActionInputs = m_playerActionInputClass.getEnumConstants();

        for (E playerActionInput : playerActionInputs) {
            m_inputManager.addMapping(playerActionInput.name(), playerActionInput.getTriggers());
        }
    }

    private void addInputListeners() {
        E[] playerActionInputs = m_playerActionInputClass.getEnumConstants();

        for (E playerActionInput : playerActionInputs) {
            if (playerActionInput.isAnalog()) {
                m_inputManager.addListener(this, playerActionInput.name());
            }

            if (playerActionInput.isAction()) {
                m_inputManager.addListener(this, playerActionInput.name());
            }
        }
    }

    private void deleteMappings() {
        E[] playerActionInputs = m_playerActionInputClass.getEnumConstants();

        for (E playerActionInput : playerActionInputs) {
            if (m_inputManager.hasMapping(playerActionInput.name())) {
                m_inputManager.deleteMapping(playerActionInput.name());
            }
        }
    }

    private void removeListeners() {
        m_inputManager.removeListener(this);
    }
}
