package com.cheesygames.colonysimulation.animation;

import com.cheesygames.colonysimulation.reflection.IEnumCachedValues;

/**
 * An enum for all the animations enumerated in the conventions.
 */
public enum AnimationConvention {

    STAND,
    WALK,
    RUN,
    ATTACK(BoneConvention.RIGHT_SHOULDER),
    HURT(BoneConvention.CHEST),
    EVADE,
    JUMP,
    FALL,
    THROW_CRYSTAL(BoneConvention.LEFT_SHOULDER),
    HOLD_CRYSTAL(BoneConvention.LEFT_SHOULDER),
    PUT_CRYSTAL_AWAY(BoneConvention.LEFT_SHOULDER),
    LOOK_AROUND(BoneConvention.CHEST);

    public static final String REVERSED_PREFIX = "reversed_";

    static {
        try {
            IEnumCachedValues.cacheValues(AnimationConvention.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final String m_animationName;
    private final BoneConvention m_bonePriority;

    AnimationConvention() {
        this.m_animationName = getDefaultName();
        this.m_bonePriority = BoneConvention.ROOT;
    }

    AnimationConvention(BoneConvention bonePriority) {
        this.m_animationName = getDefaultName();
        this.m_bonePriority = bonePriority;
    }

    /**
     * A loose form of the method valueOf. It uppers the case of the name.
     *
     * @param name The name of the enum value to search for.
     *
     * @return The enum value associated to the given name.
     */
    public static AnimationConvention looseValueOf(String name) {
        return valueOf(name.toUpperCase());
    }

    private String getDefaultName() {
        return name().toLowerCase();
    }

    public String getAnimationName() {
        return m_animationName;
    }

    public BoneConvention getBonePriority() {
        return m_bonePriority;
    }

    public String getReversedAnimationName() {
        return REVERSED_PREFIX + getAnimationName();
    }
}
