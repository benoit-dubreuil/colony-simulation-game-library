package com.cheesygames.colonysimulation.animation;

import com.cheesygames.colonysimulation.entity.IGameEntity;
import com.cheesygames.colonysimulation.reflection.IEnumCachedValues;
import com.jme3.animation.SkeletonControl;
import com.jme3.scene.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * An enum for all the bones enumerated in the conventions.
 */
public enum BoneConvention {
    NONE() {
        @Override
        public Node getAttachmentsNode(IGameEntity gameEntity) {
            return (Node) gameEntity;
        }
    },
    ROOT("b_root"),
    CHEST("b_chest", ROOT),
    NECK("b_neck", CHEST),
    HEAD("b_head", NECK),
    LEFT_SHOULDER("b_l_shoulder", CHEST),
    LEFT_UPPER_ARM("b_l_upperArm", LEFT_SHOULDER),
    LEFT_ELBOW_POLE("b_l_elbow.pole", LEFT_UPPER_ARM),
    LEFT_LOWER_ARM("b_l_lowerArm", LEFT_ELBOW_POLE),
    LEFT_LOWER_ARM_IK("b_l_lowerArm.IK", LEFT_LOWER_ARM),
    LEFT_HAND("b_l_hand", LEFT_LOWER_ARM_IK),
    LEFT_ATTACHMENT("b_l_attachment", LEFT_HAND),
    RIGHT_SHOULDER("b_r_shoulder", CHEST),
    RIGHT_UPPER_ARM("b_r_upperArm", RIGHT_SHOULDER),
    RIGHT_ELBOW_POLE("b_r_elbow.pole", RIGHT_UPPER_ARM),
    RIGHT_LOWER_ARM("b_r_lowerArm", RIGHT_ELBOW_POLE),
    RIGHT_LOWER_ARM_IK("b_r_lowerArm.IK", RIGHT_LOWER_ARM),
    RIGHT_HAND("b_r_hand", RIGHT_LOWER_ARM_IK),
    RIGHT_ATTACHMENT("b_r_attachment", RIGHT_HAND),
    PELVIS("b_pelvis", ROOT),
    LEFT_UPPER_LEG("b_l_upperLeg", PELVIS),
    LEFT_KNEE_POLE("b_l_knee.pole", LEFT_UPPER_LEG),
    LEFT_LOWER_LEG("b_l_lowerLeg", LEFT_KNEE_POLE),
    LEFT_LOWER_LEG_IK("b_l_lowerLeg.IK", LEFT_LOWER_LEG),
    LEFT_FOOT("b_l_foot", LEFT_LOWER_LEG_IK),
    RIGHT_UPPER_LEG("b_r_upperLeg", PELVIS),
    RIGHT_KNEE_POLE("b_r_knee.pole", RIGHT_UPPER_LEG),
    RIGHT_LOWER_LEG("b_r_lowerLeg", RIGHT_KNEE_POLE),
    RIGHT_LOWER_LEG_IK("b_r_lowerLeg.IK", RIGHT_KNEE_POLE),
    RIGHT_FOOT("b_r_foot", RIGHT_LOWER_LEG_IK),;

    static {
        try {
            IEnumCachedValues.cacheValues(BoneConvention.class);

            List<BoneConvention> children = new ArrayList<>();
            for (BoneConvention parentBone : IEnumCachedValues.getCachedValues(BoneConvention.class)) {
                for (BoneConvention childBone : IEnumCachedValues.getCachedValues(BoneConvention.class)) {
                    if (childBone == parentBone) {
                        continue;
                    }

                    if (childBone.getParent() == parentBone) {
                        children.add(childBone);
                    }
                }

                BoneConvention[] childrenArray = new BoneConvention[children.size()];
                parentBone.setChildren(children.toArray(childrenArray));
                children.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final String m_boneName;
    private final BoneConvention m_parent;
    private final int m_priority;
    private BoneConvention[] m_children;

    /**
     * Only for the bone NONE.
     */
    BoneConvention() {
        this("");
    }

    BoneConvention(String boneName) {
        this.m_boneName = boneName;
        this.m_parent = null;
        this.m_priority = 0;
    }

    BoneConvention(String boneName, BoneConvention parent) {
        this.m_boneName = boneName;
        this.m_parent = parent;
        this.m_priority = parent.getPriority() + 1;
    }

    /**
     * Gets the game entity's model SkeletonControl's attachments node according to the current bone.
     *
     * @param gameEntity The game entity from where to get the attachments node.
     *
     * @return The bone specific attachments node.
     */
    public Node getAttachmentsNode(IGameEntity gameEntity) {
        return gameEntity.getModelControl(SkeletonControl.class).getAttachmentsNode(getBoneName());
    }

    public String getBoneName() {
        return m_boneName;
    }

    public BoneConvention getParent() {
        return m_parent;
    }

    public BoneConvention[] getChildren() {
        return m_children;
    }

    private void setChildren(BoneConvention... children) {
        this.m_children = children;
    }

    public int getPriority() {
        return m_priority;
    }
}
