package com.cheesygames.colonysimulation.world.raycast;

import com.cheesygames.colonysimulation.lambda.TriFunction;
import com.cheesygames.colonysimulation.math.direction.Direction3D;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.World;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;

/**
 * Acts like a real-time {@link java.util.Iterator}. It is used by a {@link com.cheesygames.colonysimulation.math.bounding.VoxelRay} to continuously traverse the {@link World}
 * voxels in an efficient manner and to supply the incoming direction.
 * <p>
 * The user supplies the initial position of the voxel ray cast, which is kept as reference and changed locally.
 * <p>
 * The user also supplies a {@link TriFunction} that acts as the break condition of the {@link com.cheesygames.colonysimulation.math.bounding.VoxelRay} and as what to do whilst
 * traversing the world. The break condition takes the absolute voxel index, the voxel type, the incoming direction and returns a boolean that signifies if the voxel traversal
 * should stop (true) or not (false). Bear in mind to not modify the absolute index, as it will interfere will the ray cast.
 */
public class VoxelFaceRayCastContinuousTraverser extends AbstractVoxelRayCastContinuousTraverser {

    protected Vector3i m_lastVoxelIndex;
    protected Vector3i m_incomingDirection;
    protected TriFunction<Vector3i, VoxelType, Direction3D, Boolean> m_returnCondition;

    /**
     * Constructs a {@link VoxelFaceRayCastContinuousTraverser} with the supplied initial voxel, which is kept as a reference. With this constructor, the {@link World} must be
     * supplied with the setter {@link #setWorld(World)}.
     *
     * @param initialVoxel The initial voxel from where the ray starts. This variable is kept as reference and is changed locally.
     */
    public VoxelFaceRayCastContinuousTraverser(Vector3i initialVoxel) {
        super();
        m_lastVoxelIndex = initialVoxel;
        m_incomingDirection = new Vector3i();
    }

    /**
     * Constructs a {@link VoxelFaceRayCastContinuousTraverser} with the supplied initial voxel, which is kept as a reference, and a world.
     *
     * @param initialVoxel The initial voxel from where the ray starts. This variable is kept as reference and is changed locally.
     * @param world        The world in which to traverse voxels.
     */
    public VoxelFaceRayCastContinuousTraverser(Vector3i initialVoxel, World world) {
        super(world);
        m_lastVoxelIndex = initialVoxel;
        m_incomingDirection = new Vector3i();
    }

    /**
     * Constructs a {@link VoxelFaceRayCastContinuousTraverser} with the supplied initial voxel, which is kept as a reference. With this constructor, the {@link World} must be
     * supplied with the setter {@link #setWorld(World)}.
     *
     * @param initialVoxel    The initial voxel from where the ray starts. This variable is kept as reference and is changed locally.
     * @param returnCondition {@link TriFunction} that acts as the break condition of the {@link com.cheesygames.colonysimulation.math.bounding.VoxelRay} and as what to do whilst
     *                        traversing the world. The break condition takes the absolute voxel index, the voxel type and returns a boolean that signifies if the voxel traversal
     *                        should stop (true) or not (false). Bear in mind to not modify the absolute index, as it will interfere will the ray cast.
     */
    public VoxelFaceRayCastContinuousTraverser(Vector3i initialVoxel, TriFunction<Vector3i, VoxelType, Direction3D, Boolean> returnCondition) {
        super();
        m_lastVoxelIndex = initialVoxel;
        m_incomingDirection = new Vector3i();
        m_returnCondition = returnCondition;
    }

    /**
     * Constructs a {@link VoxelFaceRayCastContinuousTraverser} with the supplied initial voxel, which is kept as a reference.
     *
     * @param initialVoxel    The initial voxel from where the ray starts. This variable is kept as reference and is changed locally.
     * @param world           The world in which to traverse voxels.
     * @param returnCondition {@link TriFunction} that acts as the break condition of the {@link com.cheesygames.colonysimulation.math.bounding.VoxelRay} and as what to do whilst
     *                        traversing the world. The break condition takes the absolute voxel index, the voxel type and returns a boolean that signifies if the voxel traversal
     *                        should stop (true) or not (false). Bear in mind to not modify the absolute index, as it will interfere will the ray cast.
     */
    public VoxelFaceRayCastContinuousTraverser(Vector3i initialVoxel, World world, TriFunction<Vector3i, VoxelType, Direction3D, Boolean> returnCondition) {
        super(world);
        m_lastVoxelIndex = initialVoxel;
        m_incomingDirection = new Vector3i();
        m_returnCondition = returnCondition;
    }

    @Override
    protected boolean applyReturnCondition(Vector3i absoluteVoxelIndex) {
        m_incomingDirection.set(absoluteVoxelIndex).subtractLocal(m_lastVoxelIndex);

        boolean returnCondition = m_returnCondition.apply(absoluteVoxelIndex,
            getVoxalAtAbsoluteVoxelIndex(absoluteVoxelIndex),
            Direction3D.findDirectionFromVectorLocal(m_incomingDirection));

        m_lastVoxelIndex.set(absoluteVoxelIndex);

        return returnCondition;
    }

    public TriFunction<Vector3i, VoxelType, Direction3D, Boolean> getReturnCondition() {
        return m_returnCondition;
    }

    public void setReturnCondition(TriFunction<Vector3i, VoxelType, Direction3D, Boolean> returnCondition) {
        m_returnCondition = returnCondition;
    }
}
