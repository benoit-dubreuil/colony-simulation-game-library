package com.cheesygames.colonysimulation.math.vector;

import com.jme3.export.*;
import com.jme3.math.FastMath;

import java.io.IOException;

/**
 * Two dimensional vector for integers.
 *
 * @author Benoît Dubreuil
 * @author t0neg0d
 */
public class Vector2i implements Savable, Cloneable, java.io.Serializable, IVector2i {

    public int x;
    public int y;

    /**
     * Creates a new empty Vector2i.
     */
    public Vector2i() {
    }

    /**
     * Creates a new Vector2i from the supplied x, y integers.
     *
     * @param x The x value of the Vector2i.
     * @param y The y value of the Vector2i.
     */
    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates the distance between this Vector2i and another one.
     *
     * @param vector The second Vector2i used to calculate distance.
     *
     * @return The distance between this and the other vector.
     */
    public int distance(Vector2i vector) {
        int x1 = this.x - vector.x;
        int y1 = this.y - vector.y;

        return (int) FastMath.sqrt(x1 * x1 + y1 * y1);
    }

    public boolean equals(Vector2i rhs) {
        return x == rhs.x && y == rhs.y;
    }

    /**
     * <code>hashCode</code> Returns a unique code for this vector object based on it's values. If two vectors are logically equivalent, they will return the same hash code value.
     *
     * @return The hash code value of this vector.
     */
    public int hashCode() {
        final int HASH_INITIAL_VALUE = 37;

        int hash = HASH_INITIAL_VALUE;
        hash += HASH_INITIAL_VALUE * hash + x;
        hash += HASH_INITIAL_VALUE * hash + y;

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Vector2i vector2i = (Vector2i) obj;
        return x == vector2i.x && y == vector2i.y;
    }

    @Override
    public Vector2i clone() {
        try {
            return (Vector2i) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // can not happen
        }

    }

    public void write(JmeExporter e) throws IOException {
        OutputCapsule capsule = e.getCapsule(this);
        capsule.write(x, "x", 0);
        capsule.write(y, "y", 0);
    }

    public void read(JmeImporter e) throws IOException {
        InputCapsule capsule = e.getCapsule(this);
        x = capsule.readInt("x", 0);
        y = capsule.readInt("y", 0);
    }

    @Override
    public int getX() {
        return x;
    }

    /**
     * Sets the x coordinate of the Vector2i.
     *
     * @param x The x coordinate.
     */
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    /**
     * Sets the y coordinate of the Vector2i.
     *
     * @param y The y coordinate.
     */
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + String.valueOf(x) + "," + String.valueOf(y) + ")";
    }

}