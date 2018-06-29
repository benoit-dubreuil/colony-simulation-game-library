package com.cheesygames.colonysimulation.jme3.Node;

import com.jme3.scene.Spatial;

/**
 * Interface that guarantees to other interfaces that the implementing class has these methods, which are implemented in {@link com.jme3.scene.control.AbstractControl}.
 */
public interface IAbstractControl {

    boolean isEnabled();

    void setEnabled(boolean enabled);

    void setSpatial(Spatial spatial);
}
