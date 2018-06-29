package com.cheesygames.colonysimulation.math.bounding.octree;

import com.cheesygames.colonysimulation.math.bounding.AABB;

/**
 * Interface for octree entities. It allows a generic way to detect the boundaries of an entity.
 */
public interface IOctreeEntity {

    AABB getAABB();
}
