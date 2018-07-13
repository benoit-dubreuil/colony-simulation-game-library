package com.cheesygames.colonysimulation.math;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * Extension for the Math class.
 */
public final class MathExt {

    public static final float FLOAT_BIG_EPSILON = 0.0001f;

    private MathExt() {
    }

    /**
     * Indexifies a normal that is either -1, 0 or 1. In other words, negative numbers become 0 and positive numbers, including 0, become 1.
     *
     * @param normal A normal that should be either -1, 0 or 1. If it's smaller than -1, it will treated as -1. If it's bigger than 1, it will be treated as 1.
     *
     * @return An index that is either 0 (negative) or 1 (null or positive normal).
     */
    public static int indexifyNormalZeroPositive(int normal) {
        return ~((normal | 1) - 1 >> 31) & 1;
    }

    /**
     * Indexifies a normal that is either -1, 0 or 1. In other words, negative numbers become 0, positive numbers become 1 and zero is left unchanged.
     *
     * @param normal A normal that should be either -1, 0 or 1. If it's smaller than -1, it will treated as -1. If it's bigger than 1, it will be treated as 1.
     *
     * @return An index that is either 0 (negative and null normal) or 1 (positive normal).
     */
    public static int indexifyNormal(int normal) {
        return ~(normal - 1 >> 31) & 1;
    }

    /**
     * Gets the sign of the supplied number. The method being "zero position" means that the sign of zero is 1.
     *
     * @param number The number to get the sign from.
     *
     * @return The number's sign.
     */
    public static int getSignZeroPositive(int number) {
        return (number & 0x80000000) >> 31 | 1;
    }

    /**
     * Gets the negative sign of the supplied number. So, in other words, if the number is negative, -1 is returned but if the number is positive or zero, then zero is returned.
     *
     * @param number The number to get its negative sign.
     *
     * @return -1 if the number is negative, 0 otherwise.
     */
    public static int getNegativeSign(int number) {
        return number >> 31;
    }

    /**
     * Gets the negative sign of the supplied number. So, in other words, if the number is negative, -1 is returned but if the number is positive or zero, then zero is returned.
     *
     * @param number The number to get its negative sign.
     *
     * @return -1 if the number is negative, 0 otherwise.
     */
    public static int getNegativeSign(float number) {
        return Float.floatToRawIntBits(number) >> 31;
    }

    /**
     * Checks if the supplied number is a power of 2.
     *
     * @param number The number to check against.
     *
     * @return True if the given number is a power of 2, false otherwise.
     */
    public static boolean isPowerOfTwo(int number) {
        return number > 0 && ((number & (number - 1)) == 0);
    }

    /**
     * Cycles between the inclusive minimum and the exclusive maximum.
     *
     * @param min          The inclusive minimum.
     * @param exclusiveMax The exclusive maximum.
     * @param index        The index to use to cycle.
     *
     * @return The cycle index.
     */
    public static int cycle(int min, int exclusiveMax, int index) {
        return index >= exclusiveMax ? min : (index < min ? exclusiveMax - 1 : index);
    }

    /**
     * Does a 2D cross product of 3 vertices.
     * <p>
     * Useful to find which on which side a vertex is according to the supplied tip and the other vertex.
     *
     * @param tip     the tip, or point A
     * @param vertexA the first vector, or point B
     * @param vertexB the second vector, or point C
     *
     * @return a scalar representing the result of the cross product
     */
    public static float funnelCross2D(Vector3f tip, Vector3f vertexA, Vector3f vertexB) {
        return (vertexB.x - tip.x) * (vertexA.z - tip.z) - (vertexA.x - tip.x) * (vertexB.z - tip.z);
    }

    /**
     * Checks whether a vector is almost equal to another one or that the difference between the two is smaller than a given distance.
     * <p>
     * This is the squared version, which is faster, but has a smaller domain.
     *
     * @param a        The first Vector3f to check
     * @param b        The second Vector3f to check
     * @param distance The exclusive maximal distance at which two vectors are considered equal.
     *
     * @return Whether a is almost equal to b
     * @see MathExt#almostEqual(Vector3f, Vector3f)
     */
    public static boolean almostEqualSquared(Vector3f a, Vector3f b, float distance) {
        return a.distanceSquared(b) < (distance * distance);
    }

    /**
     * Checks whether a vector is almost equal to another or that the difference between the two is minimal.
     * <p>
     * This is the squared version, which is faster, but imprecise.
     *
     * @param a The first Vector3f to check
     * @param b The second Vector3f to check
     *
     * @return Whether a is almost equal to b
     * @see MathExt#almostEqual(Vector3f, Vector3f)
     */
    public static boolean almostEqualSquared(Vector3f a, Vector3f b) {
        return a.distanceSquared(b) < (FastMath.FLT_EPSILON * FastMath.FLT_EPSILON);
    }

    /**
     * Checks whether a vector is almost equal to another one or that the difference between the two is minimal.
     * <p>
     * This is the non-squared version, which has a larger domain, but is slower.
     *
     * @param a        The first Vector3f to check
     * @param b        The second Vector3f to check
     * @param distance The exclusive maximal distance at which two vectors are considered equal.
     *
     * @return Whether a is almost equal to b
     * @see MathExt#almostEqualSquared(Vector3f, Vector3f)
     */
    public static boolean almostEqual(Vector3f a, Vector3f b, float distance) {
        return a.distance(b) < distance;
    }

    /**
     * Checks whether a vector is almost equal to another or that the difference between the two is minimal.
     * <p>
     * This is the non-squared version, which is more precise, but slower.
     *
     * @param a The first Vector3f to check
     * @param b The second Vector3f to check
     *
     * @return Whether a is almost equal to b
     * @see MathExt#almostEqualSquared(Vector3f, Vector3f)
     */
    public static boolean almostEqual(Vector3f a, Vector3f b) {
        return a.distance(b) < FastMath.FLT_EPSILON;
    }

    /**
     * Checks whether a vector is almost equal to another one or that the difference between the two is smaller than a given distance.
     * <p>
     * This is the squared version, which is faster, but has a smaller domain.
     *
     * @param a        The first Vector2f to check
     * @param b        The second Vector2f to check
     * @param distance The exclusive maximal distance at which two vectors are considered equal.
     *
     * @return Whether a is almost equal to b
     * @see MathExt#almostEqual(Vector2f, Vector2f)
     */
    public static boolean almostEqualSquared(Vector2f a, Vector2f b, float distance) {
        return a.distanceSquared(b) < (distance * distance);
    }

    /**
     * Checks whether a vector is almost equal to another or that the difference between the two is minimal.
     * <p>
     * This is the squared version, which is faster, but imprecise.
     *
     * @param a The first Vector2f to check
     * @param b The second Vector2f to check
     *
     * @return Whether a is almost equal to b
     * @see MathExt#almostEqual(Vector2f, Vector2f)
     */
    public static boolean almostEqualSquared(Vector2f a, Vector2f b) {
        return a.distanceSquared(b) < (FastMath.FLT_EPSILON * FastMath.FLT_EPSILON);
    }

    /**
     * Checks whether a vector is almost equal to another one or that the difference between the two is minimal.
     * <p>
     * This is the non-squared version, which has a larger domain, but is slower.
     *
     * @param a        The first Vector2f to check
     * @param b        The second Vector2f to check
     * @param distance The exclusive maximal distance at which two vectors are considered equal.
     *
     * @return Whether a is almost equal to b
     * @see MathExt#almostEqualSquared(Vector2f, Vector2f)
     */
    public static boolean almostEqual(Vector2f a, Vector2f b, float distance) {
        return a.distance(b) < distance;
    }

    /**
     * Checks whether a vector is almost equal to another or that the difference between the two is minimal.
     * <p>
     * This is the non-squared version, which is more precise, but slower.
     *
     * @param a The first Vector2f to check
     * @param b The second Vector2f to check
     *
     * @return Whether a is almost equal to b
     * @see MathExt#almostEqualSquared(Vector2f, Vector2f)
     */
    public static boolean almostEqual(Vector2f a, Vector2f b) {
        return a.distance(b) < FastMath.FLT_EPSILON;
    }

    /**
     * Rounds a float to a certain decimal.
     *
     * @param v           A float value to round
     * @param nbAfterZero A number of decimals
     *
     * @return The rounded float
     */
    public static float roundToDecimal(float v, int nbAfterZero) {
        BigDecimal bd = new BigDecimal(Float.toString(v));
        bd = bd.setScale(nbAfterZero, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    /**
     * Checks whether two floats are equal. It is done considering float imprecision and thus it uses an epsilon.
     *
     * @param lhs The left hand side of the operation.
     * @param rhs The right hand side of the operation.
     *
     * @return True if both floats are equal, false otherwise.
     */
    public static boolean floatEquals(float lhs, float rhs) {
        return (rhs - lhs) <= FastMath.FLT_EPSILON && (rhs - lhs) >= -FastMath.FLT_EPSILON;
    }

    /**
     * Projects the supplied point on the line defined by its two given points.
     *
     * @param pointToProject The point that we need to project
     * @param start          The starting point of the linear function, or (x1, y1, z1)
     * @param end            The ending point of the linear function, or (x2, y2, z2)
     *
     * @return The projected point on the defined line.
     */
    public static Vector3f projectPointOnLine(Vector3f pointToProject, Vector3f start, Vector3f end) {
        // Line : AB
        // Point : P
        // A + dot(AP,AB) / dot(AB,AB) * AB
        Vector3f lineToPoint = pointToProject.subtract(start);
        Vector3f line = end.subtract(start);
        return new Vector3f().interpolateLocal(start, end, lineToPoint.dot(line) / line.dot(line));
    }

    /**
     * Computes the angle in radians between the two normals. Naturally, the normals must be normalized.
     *
     * @param firstNormal  The first normal from where to compute the angle.
     * @param secondNormal Against which normal the angle is calculated.
     * @param reference    The reference normal to determine directions.
     *
     * @return The angle in radians between the normals
     * @see <a href="https://www.gamedev.net/forums/topic/696415-clockwise-angle-between-two-vector3/">Clockwise angle between two Vector3</a>
     */
    public static float calculateAngle(Vector3f firstNormal, Vector3f secondNormal, Vector3f reference) {
        return (float) Math.atan2(reference.dot(firstNormal.cross(secondNormal)), firstNormal.dot(secondNormal));
    }

    /**
     * Truncates a vector. Assures that its length does not exceed a maximum scalar.
     *
     * @param vector    The vector to truncate.
     * @param maxScalar The scalar for the maximum length.
     *
     * @return A new truncated vector
     */
    private static Vector3f truncateVector(Vector3f vector, float maxScalar) {
        if (vector.length() > maxScalar) {
            return vector.normalize().multLocal(maxScalar);
        }
        else {
            return vector.clone();
        }
    }

    /**
     * Truncates a vector. Assures that its length does not exceed a maximum scalar.
     *
     * @param vector    The vector to truncate.
     * @param maxScalar The scalar for the maximum length.
     *
     * @return The truncated vector
     */
    public static Vector3f truncateVectorLocal(Vector3f vector, float maxScalar) {
        if (vector.length() > maxScalar) {
            return vector.normalizeLocal().multLocal(maxScalar);
        }
        else {
            return vector;
        }
    }

    /**
     * Checks if two collinear lines are intersecting.
     *
     * @param lineAPointA First line first vertex.
     * @param lineAPointB First line second vertex.
     * @param lineBPointA Second line first vertex.
     * @param lineBPointB Second line second vertex.
     *
     * @return True if the two collinear lines are intersecting, false otherwise.
     */
    public static boolean areCollinearLinesIntersecting(Vector3f lineAPointA, Vector3f lineAPointB, Vector3f lineBPointA, Vector3f lineBPointB) {
        Map<Float, Vector3f> distances = new HashMap();

        distances.put(lineAPointA.distanceSquared(lineAPointB), lineAPointB);
        distances.put(lineAPointA.distanceSquared(lineBPointA), lineBPointA);
        distances.put(lineAPointA.distanceSquared(lineBPointB), lineBPointB);

        Vector3f origin = distances.entrySet().stream().max(Map.Entry.comparingByKey()).get().getValue();

        // Never lineAPointA, so only one check
        if (origin == lineAPointB) {
            return origin.distanceSquared(lineBPointB) < origin.distanceSquared(lineAPointA) || origin.distanceSquared(lineBPointA) < origin.distanceSquared(lineAPointA);
        }
        else {
            Vector3f originLineOtherVertex;
            if (origin != lineBPointA) {
                originLineOtherVertex = lineBPointA;
            }
            else {
                originLineOtherVertex = lineBPointB;
            }

            return origin.distanceSquared(lineAPointA) < origin.distanceSquared(originLineOtherVertex) || origin.distanceSquared(lineAPointB) < origin.distanceSquared(
                originLineOtherVertex);
        }
    }

    /**
     * This function does a luck test by testing whenever or a random float is lesser than a calculated threshold.
     * <p>
     * We use an inverse tangent function to get a threshold value. Because of this, a returned value of 1 is actually a horizontal asymptote, therefore only an infinite chance
     * value can yield 1.
     *
     * @param chance the actual chance (no limit, but after 100 the return value wont change much)
     *
     * @return A boolean value indicating  whenever or a random float is lesser than a calculated threshold.
     */
    public static boolean isLucky(float chance) {
        return FastMath.nextRandomFloat() < ((FastMath.atan(chance / 100f) / 1.125f) / FastMath.HALF_PI);
    }

}
