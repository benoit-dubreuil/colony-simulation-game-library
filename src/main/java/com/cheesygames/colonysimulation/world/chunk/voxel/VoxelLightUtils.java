package com.cheesygames.colonysimulation.world.chunk.voxel;

/**
 * Static utility class to handle voxel lighting.
 */
public final class VoxelLightUtils {

    private static final int INDIVIDUAL_LIGHT_BIT_COUNT = 4;

    private static final int R_LIGHT_BITS = 0xF;
    private static final int G_LIGHT_BITS = R_LIGHT_BITS << INDIVIDUAL_LIGHT_BIT_COUNT;
    private static final int B_LIGHT_BITS = G_LIGHT_BITS << INDIVIDUAL_LIGHT_BIT_COUNT;
    private static final int SUN_LIGHT_BITS = B_LIGHT_BITS << INDIVIDUAL_LIGHT_BIT_COUNT;

    /**
     * Gets the red component of the supplied light.
     *
     * @param light The light that needs its red component returned.
     *
     * @return The red component of the supplied light.
     */
    public static int getRed(int light) {
        return light & R_LIGHT_BITS;
    }

    /**
     * Gets the green component of the supplied light.
     *
     * @param light The light that needs its green component returned.
     *
     * @return The green component of the supplied light.
     */
    public static int getGreen(int light) {
        return light & G_LIGHT_BITS;
    }

    /**
     * Gets the blue component of the supplied light.
     *
     * @param light The light that needs its blue component returned.
     *
     * @return The blue component of the supplied light.
     */
    public static int getBlue(int light) {
        return light & B_LIGHT_BITS;
    }

    /**
     * Gets the stellar component of the supplied light.
     *
     * @param light The light that needs its stellar component returned.
     *
     * @return The stellar component of the supplied light.
     */
    public static int getSun(int light) {
        return light & SUN_LIGHT_BITS;
    }

    /**
     * Sets the red component of the supplied light.
     *
     * @param light The light that needs its red component set.
     *
     * @return The light with the red component set to the supplied parameter.
     */
    public static int setRed(int light, int red) {
        return light | getRed(red);
    }

    /**
     * Sets the green component of the supplied light.
     *
     * @param light The light that needs its green component set.
     *
     * @return The light with the green component set to the supplied parameter.
     */
    public static int setGreen(int light, int green) {
        return light | getGreen(green);
    }

    /**
     * Sets the blue component of the supplied light.
     *
     * @param light The light that needs its blue component set.
     *
     * @return The light with the blue component set to the supplied parameter.
     */
    public static int setBlue(int light, int blue) {
        return light | getBlue(blue);
    }

    /**
     * Sets the sun component of the supplied light.
     *
     * @param light The light that needs its sun component set.
     *
     * @return The light with the sun component set to the supplied parameter.
     */
    public static int setSun(int light, int sun) {
        return light | getSun(sun);
    }

    private VoxelLightUtils() {
    }
}
