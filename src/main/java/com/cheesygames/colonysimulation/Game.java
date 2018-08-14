package com.cheesygames.colonysimulation;

import com.cheesygames.colonysimulation.event.MainThreadEventPool;
import com.jme3.app.LostFocusBehavior;
import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.system.AppSettings;

import java.awt.*;

/**
 * Encapsulate the life cycle of a game.
 */
public abstract class Game extends SimpleApplication {

    private volatile boolean m_isUpdating;

    @Override
    public void start() {
        setShowSettings(false);

        DisplayMode displayMode = GameGlobal.getDefaultDisplayMode();

        AppSettings appSettings = new AppSettings(true);
        appSettings.setRenderer(AppSettings.LWJGL_OPENGL33);
        appSettings.setVSync(true);
        appSettings.setGammaCorrection(true);
        appSettings.setDepthBits(displayMode.getBitDepth());
        appSettings.setBitsPerPixel(32);
        appSettings.setResolution(displayMode.getWidth(), displayMode.getHeight());

        setSettings(appSettings);
        super.start();
    }

    @Override
    public void simpleInitApp() {
        setLostFocusBehavior(LostFocusBehavior.PauseOnLostFocus);
        GameGlobal.initStatics(this, assetManager, stateManager, rootNode);

        ColorRGBA backgroundColor = new ColorRGBA(76, 196, 255, 255);
        backgroundColor.multLocal(1 / 255f);
        viewPort.setBackgroundColor(backgroundColor);

        initKeys();
    }

    @Override
    public void simpleUpdate(float tpf) {
        m_isUpdating = true;

        MainThreadEventPool.executeAllEvents();

        if (!GameGlobal.isPaused()) {
            // TODO : AI
            // BehaviorClock.update(tpf);
            updateGame();
        }

        GameGlobal.world.redrawChunksThatNeedIt();

        m_isUpdating = false;
    }

    protected abstract void updateGame();

    protected void initKeys() {
        inputManager.clearMappings();

        // TODO : Temporary, remove comments
        /*
        FlyCamAppState flyCamAppState = stateManager.getState(FlyCamAppState.class);
        flyCamAppState.setEnabled(false);
        stateManager.detach(flyCamAppState);

        inputManager.setCursorVisible(false);
        */
    }

    public boolean isUpdating() {
        return m_isUpdating;
    }
}