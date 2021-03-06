package com.cheesygames.colonysimulation.world.chunk.voxel;

/**
 * Static utility class to handle voxel lighting.
 */
public final class VoxelLightUtils {

    // Must be power of 2
    public static final int INDIVIDUAL_LIGHT_BIT_COUNT = 4;
    public static final int LIGHT_MAXIMUM_INTENSITY = 0xF;

    public static final int R_INDEX = 0;
    public static final int G_INDEX = R_INDEX + 1;
    public static final int B_INDEX = G_INDEX + 1;
    public static final int SUN_INDEX = B_INDEX + 1;

    public static final int COLOR_COMPONENT_COUNT = B_INDEX + 1;
    public static final int COLOR_WITH_SUN_COMPONENT_COUNT = SUN_INDEX + 1;

    public static final int R_LIGHT_BIT_POSITION = R_INDEX * INDIVIDUAL_LIGHT_BIT_COUNT;
    public static final int G_LIGHT_BIT_POSITION = G_INDEX * INDIVIDUAL_LIGHT_BIT_COUNT;
    public static final int B_LIGHT_BIT_POSITION = B_INDEX * INDIVIDUAL_LIGHT_BIT_COUNT;
    public static final int SUN_LIGHT_BIT_POSITION = SUN_INDEX * INDIVIDUAL_LIGHT_BIT_COUNT;

    public static final int R_LIGHT_BITS = 0xF << R_LIGHT_BIT_POSITION;
    public static final int G_LIGHT_BITS = 0xF << G_LIGHT_BIT_POSITION;
    public static final int B_LIGHT_BITS = 0xF << B_LIGHT_BIT_POSITION;
    public static final int SUN_LIGHT_BITS = 0xF << SUN_LIGHT_BIT_POSITION;

    /**
     * Propagates the light from the source into the destination and then returns the result. Does not propagate the sunlight.
     *
     * @param source      The source of the light to propagate. Must be the light from an adjacent voxel to the destination.
     * @param destination The destination where the light will propagate. Must be the light from an adjacent voxel to the source.
     *
     * @return The destination's light affected by the propagation of the source's light.
     */
    public static int propagateLight(int source, int destination) {
        for (int componentIndex = 0; componentIndex < COLOR_COMPONENT_COUNT; ++componentIndex) {
            if (getComponent(source, componentIndex) > getComponent(destination, componentIndex)) {
                destination = setComponentIntensity(destination, getComponentIntensity(source, componentIndex) - 1, componentIndex);
            }
        }

        return destination;
    }

    /**
     * Checks if the source light can propagate its light into the destination light. It only checks for the color lighting and the sunlight.
     *
     * @param source      The source light that needs to know if its light can propagate.
     * @param destination The destination into which the source light needs to know if its light can propagate.
     *
     * @return True if the source light can propagate into the destination, false otherwise.
     */
    public static boolean canPropagateLight(int source, int destination) {
        boolean canPropagateLight = false;

        for (int componentIndex = 0; componentIndex < COLOR_COMPONENT_COUNT && !canPropagateLight; ++componentIndex) {
            canPropagateLight |= getComponent(source, componentIndex) > getComponent(destination, componentIndex);
        }

        return canPropagateLight;
    }

    /**
     * Gets the color component of the supplied light at the specified index.
     *
     * @param light The light that needs its red component returned.
     * @param index The color component's index.
     *
     * @return The color component of the supplied light at the specified index.
     */
    public static int getComponent(int light, int index) {
        return light & (LIGHT_MAXIMUM_INTENSITY << (index * INDIVIDUAL_LIGHT_BIT_COUNT));
    }

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
     * Sets the color component of the supplied light at the specified index.
     *
     * @param light The light that needs its color component set at the specified index.
     * @param index The color component's index.
     *
     * @return The light with the color component set to the supplied parameter at the specified index.
     */
    public static int setComponent(int light, int color, int index) {
        return light | getComponent(color, index);
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
     * Gets the color component's intensity of the supplied light at the specified index. The minimum intensity is 0. The maximum intensity is defined by 2 power of {@link
     * #INDIVIDUAL_LIGHT_BIT_COUNT}.
     *
     * @param light The light that needs its color component's intensity at the specified index returned.
     * @param index The color component's index.
     *
     * @return The color component's intensity of the supplied light at the specified index.
     */
    public static int getComponentIntensity(int light, int index) {
        return getComponent(light, index) >>> (index * INDIVIDUAL_LIGHT_BIT_COUNT);
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
        return getGreen(light) >>> G_LIGHT_BIT_POSITION;
    }

    /**
     * Gets the blue component's intensity of the supplied light. The minimum intensity is 0. The maximum intensity is defined by 2 power of {@link #INDIVIDUAL_LIGHT_BIT_COUNT}.
     *
     * @param light The light that needs its blue component's intensity returned.
     *
     * @return The blue component's intensity of the supplied light.
     */
    public static int getBlueIntensity(int light) {
        return getBlue(light) >>> B_LIGHT_BIT_POSITION;
    }

    /**
     * Gets the sun component's intensity of the supplied light. The minimum intensity is 0. The maximum intensity is defined by 2 power of {@link #INDIVIDUAL_LIGHT_BIT_COUNT}.
     *
     * @param light The light that needs its sun component's intensity returned.
     *
     * @return The sun component's intensity of the supplied light.
     */
    public static int getSunIntensity(int light) {
        return getSun(light) >>> SUN_LIGHT_BIT_POSITION;
    }

    /**
     * Sets the color component's intensity of the supplied light. The intensity must be inclusively between 0 and {@link #LIGHT_MAXIMUM_INTENSITY}. In other words, the intensity
     * of a light component is the value of the latter without the bit shift.
     *
     * @param light     The light that needs its color component's intensity set at the specified index.
     * @param intensity The color component's intensity to set to the supplied light at the specified index.
     * @param index     The color component's index.
     *
     * @return The light which its color component's intensity was set at the specified index.
     */
    public static int setComponentIntensity(int light, int intensity, int index) {
        return light | ((intensity << (index * INDIVIDUAL_LIGHT_BIT_COUNT)));
    }

    /**
     * Sets the red component's intensity of the supplied light. The intensity must be inclusively between 0 and {@link #LIGHT_MAXIMUM_INTENSITY}. In other words, the intensity of
     * a light component is the value of the latter without the bit shift.
     *
     * @param light        The light that needs its red component's intensity set.
     * @param redIntensity The red component's intensity to set to the supplied light.
     *
     * @return The light which its red component's intensity was set.
     */
    public static int setRedIntensity(int light, int redIntensity) {
        return light | ((redIntensity << R_LIGHT_BIT_POSITION));
    }

    /**
     * Sets the green component's intensity of the supplied light. The intensity must be inclusively between 0 and {@link #LIGHT_MAXIMUM_INTENSITY}. In other words, the intensity
     * of a light component is the value of the latter without the bit shift.
     *
     * @param light          The light that needs its green component's intensity set.
     * @param greenIntensity The green component's intensity to set to the supplied light.
     *
     * @return The light which its green component's intensity was set.
     */
    public static int setGreenIntensity(int light, int greenIntensity) {
        return light | ((greenIntensity << G_LIGHT_BIT_POSITION));
    }

    /**
     * Sets the blue component's intensity of the supplied light. The intensity must be inclusively between 0 and {@link #LIGHT_MAXIMUM_INTENSITY}. In other words, the intensity of
     * a light component is the value of the latter without the bit shift.
     *
     * @param light         The light that needs its blue component's intensity set.
     * @param blueIntensity The blue component's intensity to set to the supplied light.
     *
     * @return The light which its blue component's intensity was set.
     */
    public static int setBlueIntensity(int light, int blueIntensity) {
        return light | ((blueIntensity << B_LIGHT_BIT_POSITION));
    }

    /**
     * Sets the sun component's intensity of the supplied light. The intensity must be inclusively between 0 and {@link #LIGHT_MAXIMUM_INTENSITY}. In other words, the intensity of
     * a light component is the value of the latter without the bit shift.
     *
     * @param light        The light that needs its sun component's intensity set.
     * @param sunIntensity The sun component's intensity to set to the supplied light.
     *
     * @return The light which its sun component's intensity was set.
     */
    public static int setSunIntensity(int light, int sunIntensity) {
        return light | ((sunIntensity << SUN_LIGHT_BIT_POSITION));
    }

    private VoxelLightUtils() {
    }
}
