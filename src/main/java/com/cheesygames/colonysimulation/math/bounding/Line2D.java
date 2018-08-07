package com.cheesygames.colonysimulation.math.bounding;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 * A class for 2D lines. It can be used to test out the intersection between two lines.
 */
public class Line2D {

    private Vector2f m_start;
    private Vector2f m_end;

    public Line2D() {
        this.m_start = new Vector2f();
        this.m_end = new Vector2f();
    }

    public Line2D(Vector2f start, Vector2f end) {
        this.m_start = start;
        this.m_end = end;
    }

    public Line2D(float startX, float startY, float endX, float endY) {
        this.m_start = new Vector2f(startX, startY);
        this.m_end = new Vector2f(endX, endY);
    }

    /**
     * Intersect two lines with each other. Returns null if it does not intersect or if they are collinear.
     *
     * @param line The line to intersect with.
     *
     * @return The intersection point if the lines intersect on one point, null otherwise.
     * @see <a href="https://ideone.com/PnPJgb">Taken this piece of code</a>
     * @see <a href="https://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect">Theory behind the algorithm</a>
     */
    public Vector2f intersect(Line2D line) {
        Vector2f CmP = new Vector2f(line.getStart().x - getStart().x, line.getStart().y - getStart().y);
        Vector2f r = new Vector2f(getEnd().x - getStart().x, getEnd().y - getStart().y);
        Vector2f s = new Vector2f(line.getEnd().x - line.getStart().x, line.getEnd().y - line.getStart().y);

        float CmPxr = CmP.x * r.y - CmP.y * r.x;
        float CmPxs = CmP.x * s.y - CmP.y * s.x;
        float rxs = r.x * s.y - r.y * s.x;

        if (CmPxr == 0f) {
            return null;

            // Lines are collinear, and so intersect if they have any overlap
            // if (((line.getStart().x - getStart().x < 0f) != (line.getStart().x - getEnd().x < 0f))
            //     || ((line.getStart().y - getStart().y < 0f) != (line.getStart().y - getEnd().y < 0f))) {
            // }
        }

        if (rxs == 0f) {
            return null; // Lines are parallel.
        }

        float rxsr = 1f / rxs;
        float t = CmPxs * rxsr;
        float u = CmPxr * rxsr;

        if ((t >= 0f) && (t <= 1f) && (u >= 0f) && (u <= 1f)) {
            // The two line segments meet at the point getStart() + t * r = line.getStart() + u * s
            return getStart().add(r.mult(t));
        }

        return null;
    }

    public Vector2f computeDirection() {
        return m_end.subtract(m_start).normalizeLocal();
    }

    public Vector2f computeDirectionLocal(Vector2f vector) {
        return vector.set(m_end).subtractLocal(m_start).normalizeLocal();
    }

    public Vector2f setStart(Vector2f start) {
        return m_start.set(start);
    }

    /**
     * Sets the end vector with the supplied Vector3f. Only the X and Z components are used.
     *
     * @param start The starting point for the line.
     *
     * @return The locally modified start vector3. It is not the supplied reference.
     */
    public Vector2f setStartVector3(Vector3f start) {
        return m_start.setFromVec3XZ(start);
    }

    public Vector2f setEnd(Vector2f end) {
        return m_end.set(end);
    }

    /**
     * Sets the end vector with the supplied Vector3f. Only the X and Z components are used.
     *
     * @param end The ending point for the line.
     *
     * @return The locally modified end vector3. It is not the supplied reference.
     */
    public Vector2f setEndVector3(Vector3f end) {
        return m_end.setFromVec3XZ(end);
    }

    public Vector2f getStart() {
        return m_start;
    }

    public Vector2f getEnd() {
        return m_end;
    }
}
