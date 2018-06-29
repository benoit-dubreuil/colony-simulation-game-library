package com.cheesygames.colonysimulation.entity;

import com.cheesygames.colonysimulation.jme3.Node.GameNode;
import com.cheesygames.colonysimulation.jme3.Node.GameNodeType;
import com.jme3.scene.Spatial;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A game entity is a game node that has a model as a child.
 */
public class GameEntity extends GameNode implements IGameEntity {

    private static volatile AtomicInteger idCounter = new AtomicInteger();
    private static char NAME_ID_SEPARATOR = '-';
    private static String DEFAULT_NAME_SUFFIX = "";
    protected final int m_id;
    protected Spatial m_model;

    public GameEntity(Spatial model) {
        this(model, GameNodeType.ENTITY);
    }

    protected GameEntity(Spatial model, GameNodeType gameNodeType) {
        super(gameNodeType);

        this.m_id = createID();

        setName(assembleName());
        setModel(model);
    }

    /**
     * Creates an ID for this game entity in a thread-safe way.
     *
     * @return A new ID.
     */
    private static int createID() {
        return idCounter.getAndIncrement();
    }

    /**
     * Resets the ID counter in a thread-safe way.
     */
    public static void resetIDCounter() {
        idCounter.set(0);
    }

    /**
     * Assembles this entity's name to be affected use the Node's method setName().
     *
     * @return This entity's name.
     */
    private String assembleName() {
        String nameSuffix = getNameSuffix();
        String name = String.valueOf(m_id);

        if (!nameSuffix.isEmpty()) {
            name = new StringBuilder(name).append(NAME_ID_SEPARATOR).append(nameSuffix).toString();
        }

        return name;
    }

    @Override
    public int hashCode() {
        return m_id;
    }

    /**
     * Gets the name suffix. For a unit, this can be the unit type.
     *
     * @return The name suffix.
     */
    protected String getNameSuffix() {
        return DEFAULT_NAME_SUFFIX;
    }

    @Override
    public Spatial getModel() {
        return m_model;
    }

    /**
     * To be only used once for instantiation.
     *
     * @param model The game entity's model.
     */
    protected void setModel(Spatial model) {
        this.m_model = model;
        attachChild(model);
    }
}
