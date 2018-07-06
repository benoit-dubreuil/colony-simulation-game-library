package com.cheesygames.colonysimulation.math.direction;

import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.reflection.IEnumCachedValues;
import com.jme3.math.Vector3f;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 3D Directions represented as Vector3i.
 */
public enum Direction3D {
    ZERO(0, 0, 0),
    LEFT(-1, 0, 0) {
        @Override
        public Vector3f projectPlanar(float x, float y, float offsetX, float offsetY) {
            return super.projectPlanar(x, y, offsetX, offsetY).set(getDirectionX(), y + offsetY, x + offsetX);
        }

        @Override
        public Vector3f swizzleAccordingToDirectionLocal(Vector3f vertex) {
            return vertex.set(-vertex.z, vertex.y, vertex.x);
        }
    },
    RIGHT(1, 0, 0) {
        @Override
        public Vector3f projectPlanar(float x, float y, float offsetX, float offsetY) {
            return super.projectPlanar(x, y, offsetX, offsetY).set(getDirectionX(), y + offsetY, -(x + offsetX));
        }

        @Override
        public Vector3f swizzleAccordingToDirectionLocal(Vector3f vertex) {
            return vertex.set(vertex.z, vertex.y, -vertex.x);
        }
    },
    BOTTOM(0, -1, 0) {
        @Override
        public Vector3f projectPlanar(float x, float y, float offsetX, float offsetY) {
            return super.projectPlanar(x, y, offsetX, offsetY).set(x + offsetX, getDirectionY(), y + offsetY);
        }

        @Override
        public Vector3f swizzleAccordingToDirectionLocal(Vector3f vertex) {
            return vertex.set(vertex.x, -vertex.z, vertex.y);
        }
    },
    TOP(0, 1, 0) {
        @Override
        public Vector3f projectPlanar(float x, float y, float offsetX, float offsetY) {
            return super.projectPlanar(x, y, offsetX, offsetY).set(x + offsetX, getDirectionY(), -(y + offsetY));
        }

        @Override
        public Vector3f swizzleAccordingToDirectionLocal(Vector3f vertex) {
            return vertex.set(vertex.x, vertex.z, -vertex.y);
        }
    },
    BACK(0, 0, -1) {
        @Override
        public Vector3f projectPlanar(float x, float y, float offsetX, float offsetY) {
            return super.projectPlanar(x, y, offsetX, offsetY).set(x + offsetX, -(y + offsetY), getDirectionZ());
        }

        @Override
        public Vector3f swizzleAccordingToDirectionLocal(Vector3f vertex) {
            return vertex.set(-vertex.x, vertex.y, -vertex.z);
        }
    },
    FRONT(0, 0, 1) {
        @Override
        public Vector3f projectPlanar(float x, float y, float offsetX, float offsetY) {
            return super.projectPlanar(x, y, offsetX, offsetY).set(x + offsetX, y + offsetY, getDirectionZ());
        }
    },

    FRONT_RIGHT(1, 0, 1),
    BACK_RIGHT(1, 0, -1),
    BACK_LEFT(-1, 0, -1),
    FRONT_LEFT(-1, 0, 1),

    TOP_RIGHT(1, 1, 0),
    BOTTOM_RIGHT(1, -1, 0),
    BOTTOM_LEFT(-1, -1, 0),
    TOP_LEFT(-1, 1, 0),

    TOP_FRONT(0, 1, 1),
    BOTTOM_FRONT(0, -1, 1),
    BOTTOM_BACK(0, -1, -1),
    TOP_BACK(0, 1, -1),

    TOP_FRONT_RIGHT(1, 1, 1),
    TOP_BACK_RIGHT(1, 1, -1),
    BOTTOM_FRONT_RIGHT(1, -1, 1),
    BOTTOM_BACK_RIGHT(1, -1, -1),
    TOP_FRONT_LEFT(-1, 1, 1),
    TOP_BACK_LEFT(-1, 1, -1),
    BOTTOM_FRONT_LEFT(-1, -1, 1),
    BOTTOM_BACK_LEFT(-1, -1, -1),;

    public static final Direction3D FIRST_DIAGONAL_3D = TOP_FRONT_RIGHT;
    /**
     * All the directions except the ZERO (0, 0, 0).
     */
    public static final Direction3D[] ALL_EXCEPT_ZERO = { LEFT, RIGHT, BOTTOM, TOP, BACK, FRONT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT, TOP_LEFT, TOP_FRONT, BOTTOM_FRONT,
        BOTTOM_BACK, TOP_BACK, TOP_FRONT_RIGHT, TOP_BACK_RIGHT, BOTTOM_FRONT_RIGHT, BOTTOM_BACK_RIGHT, TOP_FRONT_LEFT, TOP_BACK_LEFT, BOTTOM_FRONT_LEFT, BOTTOM_BACK_LEFT };
    /**
     * The orthogonal directions in a croissant and dimension order. In a cube, these directions are the faces.
     */
    public static final Direction3D[] ORTHOGONALS = { RIGHT, LEFT, TOP, BOTTOM, FRONT, BACK };
    /**
     * The orthogonal directions along the Y axis.
     */
    public static final Direction3D[] ORTHOGONALS_AXIS_Y = { BOTTOM, TOP };
    /**
     * All the edges in a cube. Includes the DIAGONALS_2D.
     */
    public static final Direction3D[] CUBE_EDGES = { FRONT_RIGHT, BACK_RIGHT, BACK_LEFT, FRONT_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT, TOP_LEFT, TOP_FRONT, BOTTOM_FRONT,
        BOTTOM_BACK, TOP_BACK };
    /**
     * The two dimensional diagonal directions. 2D in the sens only two dimensions are used per enum value. In a cube, these directions are the edges between the top and bottom
     * faces. Ordered in a clockwise manner.
     */
    public static final Direction3D[] DIAGONALS_2D = { FRONT_RIGHT, BACK_RIGHT, BACK_LEFT, FRONT_LEFT };
    /**
     * The three dimensional diagonals directions. 3D in the sens all three dimensions are used per enum value. In a cube, these directions are the corners.
     */
    public static final Direction3D[] DIAGONALS_3D = { TOP_FRONT_RIGHT, TOP_BACK_RIGHT, BOTTOM_FRONT_RIGHT, BOTTOM_BACK_RIGHT, TOP_FRONT_LEFT, TOP_BACK_LEFT, BOTTOM_FRONT_LEFT,
        BOTTOM_BACK_LEFT };
    /**
     * Duplicate with a Direction2D static variable because the circular dependency causes bugs when accessing enum names, which are generated before everything else, even before
     * the static blocks.
     */
    private static final String[] NAMES_TO_REPLACE_FOR_3D_CONVERSION = { BACK.name(), FRONT.name() };
    /**
     * Duplicate with a Direction2D static variable because the circular dependency causes bugs when accessing enum names, which are generated before everything else, even before
     * the static blocks.
     */
    private static final String[] NAME_REPLACEMENTS_FOR_3D_CONVERSION = { Direction2D.BOTTOM.name(), Direction2D.TOP.name() };

    static {
        try {
            IEnumCachedValues.cacheValues(Direction3D.class);
            initDirection2DEquivalent();
            initOpposite();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final Vector3i m_direction;
    private Direction2D m_direction2DEquivalent;
    private Direction3D m_opposite;

    Direction3D(int x, int y, int z) {
        this.m_direction = new Vector3i(x, y, z);
    }

    private static void initDirection2DEquivalent() {
        for (Direction3D direction : values()) {
            String direction2DName = direction.name();

            for (int i = 0; i < NAMES_TO_REPLACE_FOR_3D_CONVERSION.length; ++i) {
                direction2DName = direction2DName.replaceAll(Pattern.quote(NAMES_TO_REPLACE_FOR_3D_CONVERSION[i]), NAME_REPLACEMENTS_FOR_3D_CONVERSION[i]);
            }

            try {
                direction.m_direction2DEquivalent = Direction2D.valueOf(direction2DName);
            } catch (IllegalArgumentException e) {
                direction.m_direction2DEquivalent = null;
            }
        }
    }

    private static void initOpposite() {
        List<Direction3D> directions = Arrays.asList(values());
        for (Direction3D direction : directions) {
            direction.m_opposite = directions.stream()
                                             .filter(opposite -> direction.getDirectionX() == -opposite.getDirectionX() && direction.getDirectionY() == -opposite.getDirectionY()
                                                 && direction.getDirectionZ() == -opposite.getDirectionZ())
                                             .findFirst()
                                             .get();
        }
    }

    /**
     * Finds the direction that can be represented by the supplied value, which is used manhattan normalized value. I other words, the signs of the given value are used.
     *
     * @param value The value to search for. It will be modified by the method to compute the result.
     *
     * @return The Direction3D equivalent to the supplied value.
     */
    public static Direction3D findDirectionFromVectorLocal(Vector3i value) {
        Direction3D[] directions = IEnumCachedValues.getCachedValues(Direction3D.class);

        // Manhattan normalized value
        value.set((int) Math.signum(value.x), (int) Math.signum(value.y), (int) Math.signum(value.z));

        for (Direction3D direction : directions) {
            if (direction.getDirection().equals(value)) {
                return direction;
            }
        }

        return null;
    }

    /**
     * Finds the direction that can be represented by the supplied value, which is used manhattan normalized value. I other words, the signs of the given value are used.
     *
     * @param value The value to search for.
     *
     * @return The Direction3D equivalent to the supplied value.
     */
    public static Direction3D findDirectionFromVector(Vector3i value) {
        return findDirectionFromVectorLocal(new Vector3i(value));
    }

    /**
     * Projects the given X and Y components onto the planar representation of the orthogonal direction and thus it is only doable on orthogonal directions.
     *
     * @param x The X value to project.
     * @param y The Y value to project.
     *
     * @return A planar projection of the X and Y values in the current 3D direction.
     */
    public Vector3f projectPlanar(float x, float y) {
        return projectPlanar(x, y, 0, 0);
    }

    /**
     * Projects the given X and Y components onto the planar representation of the orthogonal direction and thus it is only doable on orthogonal directions.
     *
     * @param x       The X value to project.
     * @param y       The Y value to project.
     * @param offsetX The X value's offset.
     * @param offsetY The Y value'S offset.
     *
     * @return A planar projection of the X and Y values in the current 3D direction.
     */
    public Vector3f projectPlanar(float x, float y, float offsetX, float offsetY) {
        return m_direction.toVector3f();
    }

    /**
     * Swizzle the vertex components according to the direction. Only orthogonal directions can swizzle vertices. Swizzling interchanges components and might change the sign of
     * those.
     *
     * @param vertex The vertex to locally swizzle it's components.
     *
     * @return The supplied vertex that is now swizzled.
     */
    public Vector3f swizzleAccordingToDirectionLocal(Vector3f vertex) {
        return vertex;
    }

    /**
     * Swizzle the vertex components according to the direction. Only orthogonal directions can swizzle vertices. Swizzling interchanges components and might change the sign of
     * those.
     *
     * @param vertex The vertex to swizzle it's components.
     *
     * @return A new vector based upon the one given that is now swizzled.
     */
    public Vector3f swizzleAccordingToDirection(Vector3f vertex) {
        return swizzleAccordingToDirectionLocal(new Vector3f(vertex));
    }

    /**
     * Gets this enum value's index in the DIAGONALS_3D array.
     *
     * @return This enum value's index in the DIAGONALS_3D array.
     */
    public int getDiagonal3DIndex() {
        return ordinal() - FIRST_DIAGONAL_3D.ordinal();
    }

    public Vector3i getDirection() {
        return m_direction;
    }

    public Direction2D getDirection2DEquivalent() {
        return m_direction2DEquivalent;
    }

    public Direction3D getOpposite() {
        return m_opposite;
    }

    public int getDirectionX() {
        return m_direction.x;
    }

    public int getDirectionY() {
        return m_direction.y;
    }

    public int getDirectionZ() {
        return m_direction.z;
    }
}
