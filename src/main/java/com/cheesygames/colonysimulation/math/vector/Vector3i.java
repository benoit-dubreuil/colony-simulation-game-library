package com.cheesygames.colonysimulation.math.vector;

import com.jme3.export.*;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

import java.io.IOException;

/**
 * <code>Vector3i</code> defines a 3 dimensional vector as integers. Utility methods are also included to aid in mathematical calculations.
 *
 * @author Mark Powell
 * @author Joshua Slack
 * @author Benoît Dubreuil
 */
public class Vector3i implements Savable, Cloneable, java.io.Serializable, IVector2i {

    public static final int COMPONENT_COUNT = 3;
    public static final Vector3i ZERO = new Vector3i(0, 0, 0);
    public static final Vector3i UNIT_X = new Vector3i(1, 0, 0);
    public static final Vector3i UNIT_Y = new Vector3i(0, 1, 0);
    public static final Vector3i UNIT_Z = new Vector3i(0, 0, 1);
    public static final Vector3i UNIT_XYZ = new Vector3i(1, 1, 1);
    static final long serialVersionUID = 1;
    /**
     * The X coordinate of the vector.
     */
    public int x;
    /**
     * The Y coordinate of the vector.
     */
    public int y;
    /**
     * The Z coordinate of the vector.
     */
    public int z;

    /**
     * Constructor instantiates a new <code>Vector3i</code> with default values of (0,0,0).
     */
    public Vector3i() {
    }

    /**
     * Constructor instantiates a new <code>Vector3i</code> with provides values.
     *
     * @param x the x value of the vector.
     * @param y the y value of the vector.
     * @param z the z value of the vector.
     */
    public Vector3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3i(int xyz) {
        this.x = xyz;
        this.y = xyz;
        this.z = xyz;
    }

    /**
     * Constructor instantiates a new <code>Vector3i</code> that is a copy of the provided vector
     *
     * @param copy The Vector3i to copy
     */
    public Vector3i(Vector3i copy) {
        this.set(copy);
    }

    /**
     * Verifies if vector is valid. If it is null or its ints are NaN or infinite, return false. Else return true.
     *
     * @param vector The vector to validate.
     *
     * @return True or false as stated above.
     */
    public static boolean isValidVector(Vector3i vector) {
        if (vector == null) {
            return false;
        }
        if (Float.isNaN(vector.x) || Float.isNaN(vector.y) || Float.isNaN(vector.z)) {
            return false;
        }
        if (Float.isInfinite(vector.x) || Float.isInfinite(vector.y) || Float.isInfinite(vector.z)) {
            return false;
        }
        return true;
    }

    /**
     * <code>set</code> Sets the X, Y, Z values of the vector based on passed parameters.
     *
     * @param x the x value of the vector.
     * @param y the y value of the vector.
     * @param z the z value of the vector.
     *
     * @return this vector
     */
    public Vector3i set(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;

        return this;
    }

    /**
     * <code>set</code> Sets the X, Y, Z values of the vector by copying the supplied vector.
     *
     * @param vec The vector to copy.
     *
     * @return This vector.
     */
    public Vector3i set(Vector3i vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;

        return this;
    }

    /**
     * <code>add</code> Adds a provided vector to this vector creating a resultant vector which is returned. If the provided vector is null, null is returned.
     *
     * @param vec the vector to add to this.
     *
     * @return the resultant vector.
     */
    public Vector3i add(Vector3i vec) {
        return new Vector3i(x + vec.x, y + vec.y, z + vec.z);
    }

    /**
     * <code>add</code> adds the values of a provided vector storing the values in the supplied vector.
     *
     * @param vec    the vector to add to this
     * @param result the vector to store the result in
     *
     * @return result returns the supplied result vector.
     */
    public Vector3i add(Vector3i vec, Vector3i result) {
        result.x = x + vec.x;
        result.y = y + vec.y;
        result.z = z + vec.z;

        return result;
    }

    /**
     * <code>addLocal</code> adds a provided vector to this vector internally, and returns a handle to this vector for easy chaining of calls. If the provided vector is null, null
     * is returned.
     *
     * @param vec the vector to add to this vector.
     *
     * @return this
     */
    public Vector3i addLocal(Vector3i vec) {
        x += vec.x;
        y += vec.y;
        z += vec.z;

        return this;
    }

    /**
     * <code>add</code> Adds the provided values to this vector, creating a new vector that is then returned.
     *
     * @param addX The X coordinate value to add.
     * @param addY The Y coordinate value to add.
     * @param addZ The Z coordinate value to add.
     *
     * @return The result vector.
     */
    public Vector3i add(int addX, int addY, int addZ) {
        return new Vector3i(x + addX, y + addY, z + addZ);
    }

    /**
     * <code>addLocal</code> Adds the provided values to this vector internally, and returns a handle to this vector for easy chaining of calls.
     *
     * @param addX Value to add to the X coordinate
     * @param addY Value to add to the Y coordinate
     * @param addZ Value to add to the Z coordinate
     *
     * @return this
     */
    public Vector3i addLocal(int addX, int addY, int addZ) {
        x += addX;
        y += addY;
        z += addZ;

        return this;
    }

    /**
     * <code>scaleAdd</code> Multiplies this vector by a scalar then adds the given Vector3i.
     *
     * @param scalar The value to multiply this vector by.
     * @param add    The value to add
     *
     * @return The current instance scale-added.
     */
    public Vector3i scaleAdd(int scalar, Vector3i add) {
        x = x * scalar + add.x;
        y = y * scalar + add.y;
        z = z * scalar + add.z;

        return this;
    }

    /**
     * <code>scaleAdd</code> multiplies the given vector by a scalar then adds the given vector.
     *
     * @param scalar the value to multiply this vector by.
     * @param mult   the value to multiply the scalar by
     * @param add    the value to add
     *
     * @return The current instance scale-added.
     */
    public Vector3i scaleAdd(int scalar, Vector3i mult, Vector3i add) {
        this.x = mult.x * scalar + add.x;
        this.y = mult.y * scalar + add.y;
        this.z = mult.z * scalar + add.z;

        return this;
    }

    /**
     * <code>dot</code> Calculates the dot product of this vector with a provided vector. If the provided vector is null, 0 is returned.
     *
     * @param vec The vector to dot with this vector.
     *
     * @return The resultant dot product of this vector and a given vector.
     */
    public int dot(Vector3i vec) {
        return x * vec.x + y * vec.y + z * vec.z;
    }

    /**
     * <code>cross</code> Calculates the cross product of this vector with a parameter vector v.
     *
     * @param v The vector to take the cross product of with this.
     *
     * @return The cross product vector.
     */
    public Vector3i cross(Vector3i v) {
        return cross(v, null);
    }

    /**
     * <code>cross</code> Calculates the cross product of this vector with a parameter vector v. The result is stored in <code>result</code>
     *
     * @param v      The vector to take the cross product of with this.
     * @param result The vector to store the cross product result.
     *
     * @return The result after receiving the cross product vector.
     */
    public Vector3i cross(Vector3i v, Vector3i result) {
        return cross(v.x, v.y, v.z, result);
    }

    /**
     * <code>cross</code> Calculates the cross product of this vector with a parameter vector v. The result is stored in <code>result</code>
     *
     * @param otherX X component of the vector to take the cross product of with this.
     * @param otherY Y component of the vector to take the cross product of with this.
     * @param otherZ Z component of the vector to take the cross product of with this.
     * @param result The vector to store the cross product result.
     *
     * @return The result after receiving the cross product vector.
     */
    public Vector3i cross(int otherX, int otherY, int otherZ, Vector3i result) {
        if (result == null) {
            result = new Vector3i();
        }

        int resX = ((y * otherZ) - (z * otherY));
        int resY = ((z * otherX) - (x * otherZ));
        int resZ = ((x * otherY) - (y * otherX));

        result.set(resX, resY, resZ);

        return result;
    }

    /**
     * <code>crossLocal</code> Calculates the cross product of this vector with a parameter vector v.
     *
     * @param v The vector to take the cross product of with this.
     *
     * @return This.
     */
    public Vector3i crossLocal(Vector3i v) {
        return crossLocal(v.x, v.y, v.z);
    }

    /**
     * <code>crossLocal</code> Calculates the cross product of this vector with a parameter vector v.
     *
     * @param otherX X component of the vector to take the cross product of with this.
     * @param otherY Y component of the vector to take the cross product of with this.
     * @param otherZ Z component of the vector to take the cross product of with this.
     *
     * @return this.
     */
    public Vector3i crossLocal(int otherX, int otherY, int otherZ) {
        int tempx = (y * otherZ) - (z * otherY);
        int tempy = (z * otherX) - (x * otherZ);

        z = (x * otherY) - (y * otherX);
        x = tempx;
        y = tempy;

        return this;
    }

    /**
     * <code>length</code> Calculates the magnitude of this vector.
     *
     * @return The length / magnitude of the vector.
     */
    public float length() {
        return FastMath.sqrt(lengthSquared());
    }

    /**
     * <code>lengthSquared</code> Calculates the squared value of the magnitude of the vector.
     *
     * @return The magnitude squared of the vector.
     */
    public int lengthSquared() {
        return x * x + y * y + z * z;
    }

    /**
     * <code>distanceSquared</code> Calculates the distance squared between this vector and vector v.
     *
     * @param v The second vector to determine the distance squared.
     *
     * @return The distance squared between the two vectors.
     */
    public int distanceSquared(Vector3i v) {
        double dx = x - v.x;
        double dy = y - v.y;
        double dz = z - v.z;
        return (int) (dx * dx + dy * dy + dz * dz);
    }

    /**
     * <code>mult</code> Multiplies this vector by a scalar. The resultant vector is returned.
     *
     * @param scalar The value to multiply this vector by.
     *
     * @return The new vector.
     */
    public Vector3i mult(int scalar) {
        return new Vector3i(x * scalar, y * scalar, z * scalar);
    }

    /**
     * <code>mult</code> Multiplies this vector by a scalar. The resultant vector is supplied as the second parameter and returned.
     *
     * @param scalar  The scalar to multiply this vector by.
     * @param product The product to store the result in.
     *
     * @return The new resultant vector.
     */
    public Vector3i mult(int scalar, Vector3i product) {
        if (null == product) {
            product = new Vector3i();
        }

        product.x = x * scalar;
        product.y = y * scalar;
        product.z = z * scalar;

        return product;
    }

    /**
     * <code>multLocal</code> Multiplies this vector by a scalar internally, and returns a handle to this vector for easy chaining of calls.
     *
     * @param scalar the value to multiply this vector by.
     *
     * @return This
     */
    public Vector3i multLocal(int scalar) {
        x *= scalar;
        y *= scalar;
        z *= scalar;

        return this;
    }

    /**
     * <code>multLocal</code> Multiplies a provided vector to this vector internally, and returns a handle to this vector for easy chaining of calls. If the provided vector is
     * null, null is returned.
     *
     * @param vec The vector to mult to this vector.
     *
     * @return This
     */
    public Vector3i multLocal(Vector3i vec) {
        x *= vec.x;
        y *= vec.y;
        z *= vec.z;

        return this;
    }

    /**
     * <code>multLocal</code> Multiplies this vector by 3 scalars internally, and returns a handle to this vector for easy chaining of calls.
     *
     * @param x The x multiplier value.
     * @param y The y multiplier value.
     * @param z The z multiplier value.
     *
     * @return This
     */
    public Vector3i multLocal(int x, int y, int z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;

        return this;
    }

    /**
     * <code>multLocal</code> Multiplies a provided vector to this vector internally, and returns a handle to this vector for easy chaining of calls. If the provided vector is
     * null, null is returned.
     *
     * @param vec The vector to mult to this vector.
     *
     * @return This
     */
    public Vector3i mult(Vector3i vec) {
        return mult(vec, null);
    }

    /**
     * <code>multLocal</code> Multiplies a provided vector to this vector internally, and returns a handle to this vector for easy chaining of calls. If the provided vector is
     * null, null is returned.
     *
     * @param vec   The vector to mult to this vector.
     * @param store The result vector (null to create a new vector)
     *
     * @return This
     */
    public Vector3i mult(Vector3i vec, Vector3i store) {
        if (store == null) {
            store = new Vector3i();
        }

        return store.set(x * vec.x, y * vec.y, z * vec.z);
    }

    /**
     * <code>divide</code> Divides the values of this vector by a scalar and returns the result. The values of this vector remain untouched.
     *
     * @param scalar The value to divide this vectors attributes by.
     *
     * @return The result <code>Vector</code>.
     */
    public Vector3i divide(Vector3i scalar) {
        return new Vector3i(x / scalar.x, y / scalar.y, z / scalar.z);
    }

    /**
     * <code>divideLocal</code> Divides this vector by a scalar internally, and returns a handle to this vector for easy chaining of calls. Dividing by zero will result in an
     * exception.
     *
     * @param scalar The value to divides this vector by.
     *
     * @return This
     */
    public Vector3i divideLocal(Vector3i scalar) {
        x /= scalar.x;
        y /= scalar.y;
        z /= scalar.z;

        return this;
    }

    /**
     * <code>negate</code> Returns the negative of this vector. All values are negated and set to a new vector.
     *
     * @return The negated vector.
     */
    public Vector3i negate() {
        return new Vector3i(-x, -y, -z);
    }

    /**
     * <code>negateLocal</code> Negates the internal values of this vector.
     *
     * @return This.
     */
    public Vector3i negateLocal() {
        x = -x;
        y = -y;
        z = -z;

        return this;
    }

    /**
     * <code>subtract</code> Subtracts the values of a given vector from those of this vector creating a new vector object. If the provided vector is null, null is returned.
     *
     * @param vec The vector to subtract from this vector.
     *
     * @return The result vector.
     */
    public Vector3i subtract(Vector3i vec) {
        return new Vector3i(x - vec.x, y - vec.y, z - vec.z);
    }

    /**
     * <code>subtractLocal</code> Subtracts a provided vector to this vector internally, and returns a handle to this vector for easy chaining of calls. If the provided vector is
     * null, null is returned.
     *
     * @param vec The vector to subtract
     *
     * @return This
     */
    public Vector3i subtractLocal(Vector3i vec) {
        x -= vec.x;
        y -= vec.y;
        z -= vec.z;

        return this;
    }

    /**
     * <code>subtract</code>
     *
     * @param vec    The vector to subtract from this
     * @param result The vector to store the result in
     *
     * @return The result
     */
    public Vector3i subtract(Vector3i vec, Vector3i result) {
        if (result == null) {
            result = new Vector3i();
        }

        result.x = x - vec.x;
        result.y = y - vec.y;
        result.z = z - vec.z;

        return result;
    }

    /**
     * <code>subtract</code> Subtracts the provided values from this vector, creating a new vector that is then returned.
     *
     * @param subtractX The x value to subtract.
     * @param subtractY The y value to subtract.
     * @param subtractZ The z value to subtract.
     *
     * @return The result vector.
     */
    public Vector3i subtract(int subtractX, int subtractY, int subtractZ) {
        return new Vector3i(x - subtractX, y - subtractY, z - subtractZ);
    }

    /**
     * <code>subtractLocal</code> Subtracts the provided values from this vector internally, and returns a handle to this vector for easy chaining of calls.
     *
     * @param subtractX The x value to subtract.
     * @param subtractY The y value to subtract.
     * @param subtractZ The z value to subtract.
     *
     * @return This
     */
    public Vector3i subtractLocal(int subtractX, int subtractY, int subtractZ) {
        x -= subtractX;
        y -= subtractY;
        z -= subtractZ;

        return this;
    }

    /**
     * <code>maxLocal</code> Computes the maximum value for each component in this and <code>other</code> vector. The result is stored in this vector.
     *
     * @param other The other vector from which to compute and set to this the maximum.
     *
     * @return The current instance with the maximum values.
     */
    public Vector3i maxLocal(Vector3i other) {
        x = other.x > x ? other.x : x;
        y = other.y > y ? other.y : y;
        z = other.z > z ? other.z : z;

        return this;
    }

    /**
     * <code>minLocal</code> computes the minimum value for each component in this and <code>other</code> vector. The result is stored in this vector.
     *
     * @param other The other vector from which to compute and set to this the minimum.
     *
     * @return The current instance with the minimum values.
     */
    public Vector3i minLocal(Vector3i other) {
        x = other.x < x ? other.x : x;
        y = other.y < y ? other.y : y;
        z = other.z < z ? other.z : z;

        return this;
    }

    /**
     * <code>zero</code> Resets this vector's data to zero internally.
     *
     * @return The current instance with all components set to zero.
     */
    public Vector3i zero() {
        x = y = z = 0;
        return this;
    }

    /**
     * Sets this vector to the interpolation by changeAmnt from this to the finalVec this=(1-changeAmnt)*this + changeAmnt * finalVec
     *
     * @param finalVec   The vector to interpolate towards
     * @param changeAmnt An amount between 0.0 - 1.0 representing a precentage change from this towards finalVec
     *
     * @return The current instance with its components locally interpolated.
     */
    public Vector3i interpolateLocal(Vector3i finalVec, int changeAmnt) {
        this.x = (1 - changeAmnt) * this.x + changeAmnt * finalVec.x;
        this.y = (1 - changeAmnt) * this.y + changeAmnt * finalVec.y;
        this.z = (1 - changeAmnt) * this.z + changeAmnt * finalVec.z;

        return this;
    }

    /**
     * Sets this vector to the interpolation by changeAmnt from beginVec to finalVec this=(1-changeAmnt)*beginVec + changeAmnt * finalVec
     *
     * @param beginVec   the beging vector (changeAmnt=0)
     * @param finalVec   The vector to interpolate towards
     * @param changeAmnt An amount between 0.0 - 1.0 representing a precentage change from beginVec towards finalVec
     *
     * @return The current instance with its components locally interpolated.
     */
    public Vector3i interpolateLocal(Vector3i beginVec, Vector3i finalVec, int changeAmnt) {
        this.x = (1 - changeAmnt) * beginVec.x + changeAmnt * finalVec.x;
        this.y = (1 - changeAmnt) * beginVec.y + changeAmnt * finalVec.y;
        this.z = (1 - changeAmnt) * beginVec.z + changeAmnt * finalVec.z;

        return this;
    }

    public Vector3f toVector3f() {
        return new Vector3f(x, y, z);
    }

    /**
     * Saves this Vector3i into the given int[] object.
     *
     * @param ints The int[] to take this Vector3i. If null, a new int[3] is created.
     *
     * @return The array, with X, Y, Z int values in that order
     */
    public int[] toArray(int[] ints) {
        if (ints == null) {
            ints = new int[3];
        }

        ints[0] = x;
        ints[1] = y;
        ints[2] = z;

        return ints;
    }

    public boolean equals(int x, int y, int z) {
        return this.x == x && this.y == y && this.z == z;
    }

    public boolean equals(Vector3f rhs) {
        return rhs != null && x == (int) rhs.x && y == (int) rhs.y && z == (int) rhs.z;
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
        hash += HASH_INITIAL_VALUE * hash + z;

        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }

        Vector3i other = (Vector3i) o;

        return x == other.x && y == other.y && z == other.z;
    }

    @Override
    public Vector3i clone() {
        try {
            return (Vector3i) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // can not happen
        }
    }

    public void write(JmeExporter e) throws IOException {
        OutputCapsule capsule = e.getCapsule(this);
        capsule.write(x, "x", 0);
        capsule.write(y, "y", 0);
        capsule.write(z, "z", 0);
    }

    public void read(JmeImporter e) throws IOException {
        InputCapsule capsule = e.getCapsule(this);
        x = capsule.readInt("x", 0);
        y = capsule.readInt("y", 0);
        z = capsule.readInt("z", 0);
    }

    /**
     * Gets the coordinate at the given dimensional index.
     *
     * @param index The dimensional index.
     *
     * @return x value if index == 0, y value if index == 1 or z value if index == 2
     * @throws IllegalArgumentException if index is not one of 0, 1, 2.
     */
    public int get(int index) {
        switch (index) {
            case 0:
                return x;
            case 1:
                return y;
            case 2:
                return z;
        }

        throw new IllegalArgumentException("index must be either 0, 1 or 2");
    }

    /**
     * Sets the coordinate at the given dimensional index.
     *
     * @param index Which dimensional index in this vector to set.
     * @param value Value to set to one of X, Y or Z.
     *
     * @throws IllegalArgumentException If index is not one of 0, 1, 2.
     */
    public void set(int index, int value) {
        switch (index) {
            case 0:
                x = value;
                return;
            case 1:
                y = value;
                return;
            case 2:
                z = value;
                return;
        }

        throw new IllegalArgumentException("index must be either 0, 1 or 2");
    }

    /**
     * Returns true if this vector is a unit vector (length() ~= 1), returns false otherwise.
     *
     * @return True if this vector is a unit vector (length() ~= 1), or false otherwise.
     */
    public boolean isUnitVector() {
        float len = length();
        return 0.99f < len && len < 1.01f;
    }

    @Override
    public int getX() {
        return x;
    }

    public Vector3i setX(int x) {
        this.x = x;
        return this;
    }

    @Override
    public int getY() {
        return y;
    }

    public Vector3i setY(int y) {
        this.y = y;
        return this;
    }

    public int getZ() {
        return z;
    }

    public Vector3i setZ(int z) {
        this.z = z;
        return this;
    }

    /**
     * <code>toString</code> Returns the string representation of this vector. The format is:
     * <p>
     * org.jme.math.Vector3i [X=XX.XXXX, Y=YY.YYYY, Z=ZZ.ZZZZ]
     *
     * @return The string representation of this vector.
     */
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}