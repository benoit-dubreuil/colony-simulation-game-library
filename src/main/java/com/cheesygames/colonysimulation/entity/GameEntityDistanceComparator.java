package com.cheesygames.colonysimulation.entity;

import com.cheesygames.colonysimulation.math.MathExt;
import com.jme3.math.Vector3f;

import java.util.Comparator;

/**
 * A distance comparator for game entities based on a reference game entity. The smaller the distance between a game entity and the reference game entity, the smaller the resulting
 * index.
 *
 * @param <E> The type of the reference game entity.
 */
public class GameEntityDistanceComparator<E extends GameEntity> implements Comparator<GameEntity> {

    private E m_gameEntity;
    private Vector3f m_tmpDistanceLhs;
    private Vector3f m_tmpDistanceRhs;

    public GameEntityDistanceComparator() {
        this.m_tmpDistanceLhs = new Vector3f();
        this.m_tmpDistanceRhs = new Vector3f();
    }

    public GameEntityDistanceComparator(E gameEntity) {
        this.m_gameEntity = gameEntity;
        this.m_tmpDistanceLhs = new Vector3f();
        this.m_tmpDistanceRhs = new Vector3f();
    }

    @Override
    public int compare(GameEntity lhs, GameEntity rhs) {
        Vector3f entityTranslation = m_gameEntity.getModel().getWorldTranslation();

        float lhsDistance = m_tmpDistanceLhs.set(lhs.getModel().getWorldTranslation()).subtractLocal(entityTranslation).lengthSquared();
        float rhsDistance = m_tmpDistanceRhs.set(rhs.getModel().getWorldTranslation()).subtractLocal(entityTranslation).lengthSquared();

        if (MathExt.floatEquals(lhsDistance, rhsDistance)) {
            return 0;
        }
        else if (lhsDistance < rhsDistance) {
            return -1;
        }
        else {
            return 1;
        }
    }

    public E getGameEntity() {
        return m_gameEntity;
    }

    public void setGameEntity(E gameEntity) {
        m_gameEntity = gameEntity;
    }
}
