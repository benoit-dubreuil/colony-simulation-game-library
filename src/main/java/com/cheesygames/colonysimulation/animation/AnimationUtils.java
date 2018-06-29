package com.cheesygames.colonysimulation.animation;

import com.jme3.animation.AnimControl;
import com.jme3.animation.Animation;
import com.jme3.animation.BoneTrack;
import com.jme3.animation.Track;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 * Animation utilities. For example, one utility reverses an animation.
 */
public final class AnimationUtils {

    private AnimationUtils() {
    }

    /**
     * Reverses an animation saves it into the given AnimControl.
     *
     * @param animControl   The AnimControls that holds all the animations for a model.
     * @param animToReverse The enum value of the animation to reverse.
     */
    public static void addReversedAnimation(AnimControl animControl, AnimationConvention animToReverse) {
        Animation originalAnimation = animControl.getAnim(animToReverse.getAnimationName());
        Track[] tracks = originalAnimation.getTracks();
        Animation reversedAnim = new Animation(animToReverse.getReversedAnimationName(), originalAnimation.getLength());

        for (Track track : tracks) {
            Track cloneTrack = track.clone();
            Quaternion[] rotations = ((BoneTrack) cloneTrack).getRotations();
            Vector3f[] scales = ((BoneTrack) cloneTrack).getScales();
            Vector3f[] translations = ((BoneTrack) cloneTrack).getTranslations();
            Vector3f tempVector3f;
            Quaternion tempQuaternion;
            for (int i = 0; i < (rotations.length / 2); ++i) {
                tempQuaternion = rotations[i];
                rotations[i] = rotations[rotations.length - 1 - i];
                rotations[rotations.length - 1 - i] = tempQuaternion;
            }
            for (int i = 0; i < (scales.length / 2); ++i) {
                tempVector3f = scales[i];
                scales[i] = scales[scales.length - 1 - i];
                scales[scales.length - 1 - i] = tempVector3f;
            }
            for (int i = 0; i < (translations.length / 2); ++i) {
                tempVector3f = translations[i];
                translations[i] = translations[translations.length - 1 - i];
                translations[translations.length - 1 - i] = tempVector3f;
            }
            ((BoneTrack) cloneTrack).setKeyframes(((BoneTrack) cloneTrack).getTimes(), translations, rotations, scales);
            reversedAnim.addTrack(cloneTrack);
        }

        animControl.addAnim(reversedAnim);
    }
}
