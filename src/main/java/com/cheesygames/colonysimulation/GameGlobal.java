package com.cheesygames.colonysimulation;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.Logger;
import com.badlogic.gdx.ai.StdoutLogger;
import com.cheesygames.colonysimulation.asset.loader.behaviortree.BehaviorTreeAssetLoader;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

import java.awt.*;

/**
 * Game global variables.
 */
public final class GameGlobal {

    public static final float DEFAULT_INVERTED_PHYSICS_ACCURACY = 120;

    public static volatile AssetManager assetManager;
    public static volatile Game game;

    public static volatile Node rootNode;
    public static volatile Node terrainNode;
    public static volatile Node unitNode;
    public static volatile Node entityNode;

    public static volatile long mainThreadId;
    private static volatile boolean isPaused;

    public static void initStatics(Game game, AssetManager assetManager, AppStateManager stateManager, Node rootNode) {
        initThreadIds();

        GameGlobal.assetManager = assetManager;
        setupLibGDXAI();

        GameGlobal.rootNode = rootNode;
        GameGlobal.game = game;

        GameGlobal.terrainNode = new Node();
        GameGlobal.unitNode = new Node();
        GameGlobal.entityNode = new Node();

        GameGlobal.rootNode.attachChild(GameGlobal.terrainNode);
        GameGlobal.rootNode.attachChild(GameGlobal.unitNode);
        GameGlobal.rootNode.attachChild(GameGlobal.entityNode);

        isPaused = false;
    }

    private static void initThreadIds() {
        mainThreadId = Thread.currentThread().getId();
    }

    /**
     * Sets up the LibGDX-AI library.
     */
    private static void setupLibGDXAI() {
        Logger logger = new StdoutLogger();
        GdxAI.setLogger(logger);

        assetManager.registerLoader(BehaviorTreeAssetLoader.class,
            BehaviorTreeAssetLoader.ASSET_TYPE.getSupportedFormats().toArray(new String[BehaviorTreeAssetLoader.ASSET_TYPE.getSupportedFormats().size()]));
    }

    /**
     * Checks if the scene graph can be updated from this thread, in the sense that a node can be attached or detached from teh root node.
     *
     * @return True if the scene graph can be updated from this thread, false otherwise.
     */
    public static boolean canThreadUpdateSceneGraph() {
        return game.isUpdating();
    }

    public static void pause() {
        if (!isPaused()) {
            // TODO : Physics
            // game.getStateManager().getState(BulletAppState.class).setEnabled(false);
            // TODO : Player camera
            // game.getStateManager().getState(FPSCameraActionInputAppState.class).setEnabled(false);
            rootNode.setUpdateListValid(false);

            isPaused = true;
        }
    }

    public static void unpause() {
        if (isPaused()) {
            // TODO : Physics
            // game.getStateManager().getState(BulletAppState.class).setEnabled(true);
            // TODO : Player camera
            // game.getStateManager().getState(FPSCameraActionInputAppState.class).setEnabled(true);
            rootNode.setUpdateListValid(false);

            isPaused = false;
        }
    }

    public static DisplayMode getDefaultDisplayMode() {
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        DisplayMode[] displayModes = device.getDisplayModes();
        for (int i = (displayModes.length >= 3 ? 3 : 0); i < displayModes.length; ++i) {
            if (displayModes[i].getWidth() >= 600) {
                return displayModes[i]; // TODO : Menu -> Options -> Select this.
            }
        }

        return displayModes[0];
    }

    /**
     * Checks if the current thread is the main thread.
     *
     * @return True if the current thread is the main thread, false otherwise.
     */
    public static boolean isCurrentThreadMainThread() {
        return Thread.currentThread().getId() == mainThreadId;
    }

    public static boolean isPaused() {
        return isPaused;
    }

    public static void setPaused(boolean paused) {
        if (paused == !isPaused()) {
            if (paused) {
                pause();
            }
            else {
                unpause();
            }
        }
    }
}
