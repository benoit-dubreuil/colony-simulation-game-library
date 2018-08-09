package com.cheesygames.colonysimulation.world.chunk.voxel;

/**
 * Static utility class to handle voxel lighting.
 */
public final class VoxelLightUtils {

    public static final int INDIVIDUAL_LIGHT_BIT_COUNT = 4;
    public static final int LIGHT_MAXIMUM_INTENSITY = 0xF;

    public static final int R_LIGHT_BIT_POSITION = 0;
    public static final int G_LIGHT_BIT_POSITION = R_LIGHT_BIT_POSITION + INDIVIDUAL_LIGHT_BIT_COUNT;
    public static final int B_LIGHT_BIT_POSITION = G_LIGHT_BIT_POSITION + INDIVIDUAL_LIGHT_BIT_COUNT;
    public static final int SUN_LIGHT_BIT_POSITION = B_LIGHT_BIT_POSITION + INDIVIDUAL_LIGHT_BIT_COUNT;

    public static final int R_LIGHT_BITS = 0xF << R_LIGHT_BIT_POSITION;
    public static final int G_LIGHT_BITS = 0xF << G_LIGHT_BIT_POSITION;
    public static final int B_LIGHT_BITS = 0xF << B_LIGHT_BIT_POSITION;
    public static final int SUN_LIGHT_BITS = 0xF << SUN_LIGHT_BIT_POSITION;

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

    /**
     * Gets the red component's intensity of the supplied light. The minimum intensity is 0. The maximum intensity is defined by 2 power of {@link #INDIVIDUAL_LIGHT_BIT_COUNT}.
     *
     * @param light The light that needs its red component's intensity returned.
     *
     * @return The red component's intensity of the supplied light.
     */
    public static int getRedIntensity(int light) {
        return getRed(light) >>> R_LIGHT_BIT_POSITION;
    }

    /**
     * Gets the green component's intensity of the supplied light. The minimum intensity is 0. The maximum intensity is defined by 2 power of {@link #INDIVIDUAL_LIGHT_BIT_COUNT}.
     *
     * @param light The light that needs its green component's intensity returned.
     *
     * @return The green component's intensity of the supplied light.
     */
    public static int getGreenIntensity(int light) {
        return getGreen(light) >>> R_LIGHT_BIT_POSITION;
    }

    /**
     * Gets the blue component's intensity of the supplied light. The minimum intensity is 0. The maximum intensity is defined by 2 power of {@link #INDIVIDUAL_LIGHT_BIT_COUNT}.
     *
     * @param light The light that needs its blue component's intensity returned.
     *
     * @return The blue component's intensity of the supplied light.
     */
    public static int getBlueIntensity(int light) {
        return getBlue(light) >>> R_LIGHT_BIT_POSITION;
    }

    /**
     * Gets the sun component's intensity of the supplied light. The minimum intensity is 0. The maximum intensity is defined by 2 power of {@link #INDIVIDUAL_LIGHT_BIT_COUNT}.
     *
     * @param light The light that needs its sun component's intensity returned.
     *
     * @return The sun component's intensity of the supplied light.
     */
    public static int getSunIntensity(int light) {
        return getSun(light) >>> R_LIGHT_BIT_POSITION;
    }

    /**
     * Sets the red component's intensity of the supplied. The intensity must be inclusively between 0 and {@link #LIGHT_MAXIMUM_INTENSITY}. In other words, the intensity of a
     * light component is the value of the latter without the bit shift.
     *
     * @param light        The light that needs its red component's intensity set.
     * @param redIntensity The red component's intensity to set to the supplied light.
     *
     * @return The light which its red component's intensity was set.
     */
    public static int setRedIntensity(int light, int redIntensity) {
        return light | ((redIntensity << R_LIGHT_BIT_POSITION) & R_LIGHT_BITS);
    }

    /**
     * Sets the green component's intensity of the supplied. The intensity must be inclusively between 0 and {@link #LIGHT_MAXIMUM_INTENSITY}. In other words, the intensity of a
     * light component is the value of the latter without the bit shift.
     *
     * @param light        The light that needs its green component's intensity set.
     * @param greenIntensity The green component's intensity to set to the supplied light.
     *
     * @return The light which its green component's intensity was set.
     */
    public static int setGreenIntensity(int light, int greenIntensity) {
        return light | ((greenIntensity << G_LIGHT_BIT_POSITION) & G_LIGHT_BITS);
    }

    /**
     * Sets the blue component's intensity of the supplied. The intensity must be inclusively between 0 and {@link #LIGHT_MAXIMUM_INTENSITY}. In other words, the intensity of a
     * light component is the value of the latter without the bit shift.
     *
     * @param light        The light that needs its blue component's intensity set.
     * @param blueIntensity The blue component's intensity to set to the supplied light.
     *
     * @return The light which its blue component's intensity was set.
     */
    public static int setBlueIntensity(int light, int blueIntensity) {
        return light | ((blueIntensity << B_LIGHT_BIT_POSITION) & B_LIGHT_BITS);
    }

    /**
     * Sets the sun component's intensity of the supplied. The intensity must be inclusively between 0 and {@link #LIGHT_MAXIMUM_INTENSITY}. In other words, the intensity of a
     * light component is the value of the latter without the bit shift.
     *
     * @param light        The light that needs its sun component's intensity set.
     * @param sunIntensity The sun component's intensity to set to the supplied light.
     *
     * @return The light which its sun component's intensity was set.
     */
    public static int setSunIntensity(int light, int sunIntensity) {
        return light | ((sunIntensity << SUN_LIGHT_BIT_POSITION) & SUN_LIGHT_BITS);
    }

    private VoxelLightUtils() {
    }
}
