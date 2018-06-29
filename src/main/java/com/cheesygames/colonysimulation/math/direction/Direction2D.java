package com.cheesygames.colonysimulation.math.direction;

import com.cheesygames.colonysimulation.math.vector.Vector2i;
import com.cheesygames.colonysimulation.reflection.IEnumCachedValues;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 2D Directions represented as Vector2i.
 */
public enum Direction2D {
    ZERO(0, 0),
    LEFT(-1, 0),
    RIGHT(1, 0),
    BOTTOM(0, -1),
    TOP(0, 1),
    TOP_LEFT(LEFT.getDirectionX(), TOP.getDirectionY()),
    TOP_RIGHT(RIGHT.getDirectionX(), TOP.getDirectionY()),
    BOTTOM_RIGHT(RIGHT.getDirectionX(), BOTTOM.getDirectionY()),
    BOTTOM_LEFT(LEFT.getDirectionX(), BOTTOM.getDirectionY());

    /**
     * All the directions in a clockwise order.
     */
    public static final Direction2D[] ALL_EXCEPT_ZERO = { TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, TOP_LEFT };
    /**
     * The orthogonal directions in a clockwise order.
     */
    public static final Direction2D[] ORTHOGONALS = { TOP, RIGHT, BOTTOM, LEFT };
    /**
     * The diagonal directions in a clockwise order.
     */
    public static final Direction2D[] DIAGONALS = { TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT };
    /**
     * Duplicate with a Direction3D static variable because the circular dependency causes bugs when accessing enum names, which are generated before everything else, even before
     * the static blocks.
     */
    private static final String[] NAMES_TO_REPLACE_FOR_3D_CONVERSION = { BOTTOM.name(), TOP.name() };
    /**
     * Duplicate with a Direction3D static variable because the circular dependency causes bugs when accessing enum names, which are generated before everything else, even before
     * the static blocks.
     */
    private static final String[] NAME_REPLACEMENTS_FOR_3D_CONVERSION = { Direction3D.BACK.name(), Direction3D.FRONT.name() };

    static {
        try {
            IEnumCachedValues.cacheValues(Direction2D.class);
            initDirection3DEquivalent();
            initOpposite();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final Vector2i m_direction;
    private Direction3D m_direction3DEquivalent;
    private Direction2D m_opposite;

    Direction2D(int x, int y) {
        this.m_direction = new Vector2i(x, y);
    }

    private static void initDirection3DEquivalent() {
        for (Direction2D direction : values()) {
            String direction3DName = direction.name();

            for (int i = 0; i < NAMES_TO_REPLACE_FOR_3D_CONVERSION.length; ++i) {
                direction3DName = direction3DName.replaceAll(Pattern.quote(NAMES_TO_REPLACE_FOR_3D_CONVERSION[i]), NAME_REPLACEMENTS_FOR_3D_CONVERSION[i]);
            }

            direction.m_direction3DEquivalent = Direction3D.valueOf(direction3DName);
        }
    }

    private static void initOpposite() {
        List<Direction2D> directions = Arrays.asList(values());
        for (Direction2D direction : directions) {
            direction.m_opposite = directions.stream()
                                             .filter(opposite -> direction.getDirectionX() == -opposite.getDirectionX() && direction.getDirectionY() == -opposite.getDirectionY())
                                             .findFirst()
                                             .get();
        }
    }

    public Vector2i getDirection() {
        return m_direction;
    }

    public Direction3D getDirection3DEquivalent() {
        return m_direction3DEquivalent;
    }

    public Direction2D getOpposite() {
        return m_opposite;
    }

    public int getDirectionX() {
        return m_direction.x;
    }

    public int getDirectionY() {
        return m_direction.y;
    }
}
