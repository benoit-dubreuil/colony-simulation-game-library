package com.cheesygames.colonysimulation.math.bounding;

import com.cheesygames.colonysimulation.math.vector.Vector2i;

/**
 * Represents a 2D rectangle with starting and ending coordinates. Per axis coordinates are integers.
 */
public class Rectangle2i {

    private Vector2i m_bottomLeft;
    private Vector2i m_topRight;

    public Rectangle2i(int bottomLeftX, int bottomLeftY, int topRightX, int topRightY) {
        this.m_bottomLeft = new Vector2i(bottomLeftX, bottomLeftY);
        this.m_topRight = new Vector2i(topRightX, topRightY);
    }

    public static Rectangle2i createFromDimensions(int bottomLeftX, int bottomLeftY, int width, int height) {
        return new Rectangle2i(bottomLeftX, bottomLeftY, bottomLeftX + width, bottomLeftY + height);
    }

    public static Rectangle2i createFromCenter(int centerX, int centerY, int width, int height) {
        return new Rectangle2i(centerX - width / 2, centerY - height / 2, centerX + width / 2, centerY + height / 2);
    }

    /**
     * Checks if this rectangle and the other rectangle intersect. Two rectangles intersect with each other if any of them is partially or completely inside the other one.
     *
     * @param other The other rectangle to test the intersection with.
     *
     * @return If the two rectangles intersect.
     */
    public boolean isIntersecting(Rectangle2i other) {
        return m_topRight.x > other.m_bottomLeft.x && m_bottomLeft.x < other.m_topRight.x && m_topRight.y > other.m_bottomLeft.y && m_bottomLeft.y < other.m_topRight.y;
    }

    /**
     * Checks if this rectangle and the other rectangle intersect or touch. Two rectangles intersect with each other if any of them is partially or completely inside the other one.
     * Two rectangles touch only if one of their edges touch and that they are not intersecting.
     *
     * @param other The other rectangle to test the intersection or touching with.
     *
     * @return If the two rectangles intersect or touch.
     */
    public boolean isIntersectingOrTouching(Rectangle2i other) {
        return m_topRight.x >= other.m_bottomLeft.x && m_bottomLeft.x <= other.m_topRight.x && m_topRight.y >= other.m_bottomLeft.y && m_bottomLeft.y <= other.m_topRight.y;
    }

    /**
     * Checks if this rectangle and the other rectangle are touching. Two rectangles touch only if one of their edges touch and that they are not intersecting.
     *
     * @param other The other rectangle to test the touching with.
     *
     * @return If the two rectangles touch.
     */
    public boolean isTouching(Rectangle2i other) {
        return !isIntersecting(other) && isIntersectingOrTouching(other);
    }

    public boolean equals(Rectangle2i other) {
        return m_bottomLeft.x == other.m_bottomLeft.x && m_bottomLeft.y == other.m_bottomLeft.y && m_topRight.x == other.m_topRight.x && m_topRight.y == other.m_topRight.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rectangle2i other = (Rectangle2i) o;
        return equals(other);
    }

    public int getBottomLeftX() {
        return m_bottomLeft.x;
    }

    public void setBottomLeftX(int bottomLeftX) {
        this.m_bottomLeft.x = bottomLeftX;
    }

    public int getBottomLeftY() {
        return m_bottomLeft.y;
    }

    public void setBottomLeftY(int bottomLeftY) {
        this.m_bottomLeft.y = bottomLeftY;
    }

    public int getTopRightX() {
        return m_topRight.x;
    }

    public void setTopRightX(int topRightX) {
        this.m_topRight.x = topRightX;
    }

    public int getTopRightY() {
        return m_topRight.y;
    }

    public void setTopRightY(int topRightY) {
        this.m_topRight.y = topRightY;
    }

    public Vector2i getBottomLeft() {
        return m_bottomLeft;
    }

    public void setBottomLeft(Vector2i bottomLeft) {
        this.m_bottomLeft = bottomLeft;
    }

    public Vector2i getTopRight() {
        return m_topRight;
    }

    public void setTopRight(Vector2i topRight) {
        this.m_topRight = topRight;
    }

    public int getWidth() {
        return m_topRight.x - m_bottomLeft.x;
    }

    public void setWidth(int width) {
        this.m_topRight.x = m_bottomLeft.x + width;
    }

    public int getHeight() {
        return m_topRight.y - m_bottomLeft.y;
    }

    public void setHeight(int height) {
        this.m_topRight.y = m_bottomLeft.y + height;
    }
}
