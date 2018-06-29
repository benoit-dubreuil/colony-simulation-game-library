package com.cheesygames.colonysimulation.animation;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;

/**
 * Interface between the AnimEventListener from jme3 and the animation system for this game.
 */
public interface IBetterAnimEventListener extends AnimEventListener {

    void onAnimCycleDone(AnimControl control, AnimChannel channel, AnimationConvention animation);

    void onAnimChange(AnimControl control, AnimChannel channel, AnimationConvention animation);

    @Override
    default void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        onAnimCycleDone(control, channel, AnimationConvention.looseValueOf(animName));
    }

    @Override
    default void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        onAnimChange(control, channel, AnimationConvention.looseValueOf(animName));
    }
}
