package com.cheesygames.colonysimulation.world.raycast;

import com.cheesygames.colonysimulation.math.direction.Direction3D;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.World;
import com.cheesygames.colonysimulation.world.chunk.IChunkVoxelData;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Acts like a real-time {@link java.util.Iterator}. It is used by a {@link com.cheesygames.colonysimulation.math.bounding.VoxelRay} to continuously traverse the {@link World}
 * voxels in an efficient manner and to supply the incoming direction.
 * <p>
 * The user supplies the initial position of the voxel ray cast, which is kept as reference and changed locally.
 * <p>
 * The user supplies a {@link BiFunction} that acts as the break condition of the {@link com.cheesygames.colonysimulation.math.bounding.VoxelRay} and as what to do whilst
 * traversing the world. The break condition takes the absolute voxel index, the voxel type and returns a boolean that signifies if the voxel traversal should stop (true) or not
 * (false). Bear in mind to not modify the absolute index, as it will interfere will the ray cast.
 */
public class VoxelFaceRayCastContinuousTraverser extends VoxelRayCastContinuousTraverser {

    protected Vector3i m_lastTraversedVoxelIndex;
    protected IChunkVoxelData m_lastTraversedChunk;
    protected Vector3i m_lastTraversedRelativeVoxelIndex;
    protected Vector3i m_incomingDirectionVector;
    protected Direction3D m_incomingDirection;

    /**
     * Constructs a {@link VoxelFaceRayCastContinuousTraverser}. With this constructor, initial voxel must be supplied with and the {@link World} must be supplied with the setter
     * {@link VoxelRayCastContinuousTraverser#setWorld(World)}.
     */
    public VoxelFaceRayCastContinuousTraverser() {
        super();
        m_lastTraversedVoxelIndex = null;
        m_lastTraversedRelativeVoxelIndex = new Vector3i();
        m_incomingDirectionVector = new Vector3i();
    }

    /**
     * Constructs a {@link VoxelFaceRayCastContinuousTraverser} with the supplied initial voxel, which is kept as a reference. With this constructor, the {@link World} must be
     * supplied with the setter {@link VoxelRayCastContinuousTraverser#setWorld(World)}.
     *
     * @param initialVoxel The initial voxel from where the ray starts. This variable is kept as reference and is changed locally.
     */
    public VoxelFaceRayCastContinuousTraverser(Vector3i initialVoxel) {
        super();
        m_lastTraversedVoxelIndex = initialVoxel;
        m_lastTraversedRelativeVoxelIndex = new Vector3i();
        m_incomingDirectionVector = new Vector3i();
    }

    /**
     * Constructs a {@link VoxelFaceRayCastContinuousTraverser} with the supplied initial voxel, which is kept as a reference, and a world.
     *
     * @param initialVoxel The initial voxel from where the ray starts. This variable is kept as reference and is changed locally.
     * @param world        The world in which to traverse voxels.
     */
    public VoxelFaceRayCastContinuousTraverser(Vector3i initialVoxel, World world) {
        super(world);
        m_lastTraversedVoxelIndex = initialVoxel;
        m_lastTraversedRelativeVoxelIndex = new Vector3i();
        m_incomingDirectionVector = new Vector3i();
    }

    /**
     * Constructs a {@link VoxelFaceRayCastContinuousTraverser} with the supplied initial voxel, which is kept as a reference. With this constructor, the {@link World} must be
     * supplied with the setter {@link VoxelRayCastContinuousTraverser#setWorld(World)}.
     *
     * @param initialVoxel    The initial voxel from where the ray starts. This variable is kept as reference and is changed locally.
     * @param returnCondition {@link com.cheesygames.colonysimulation.math.bounding.VoxelRay} and as what to do whilst traversing the world. The break condition takes the absolute
     *                        voxel index, the voxel type and returns a boolean that signifies if the voxel traversal should stop (true) or not (false). Bear in mind to not modify
     *                        the absolute index, as it will interfere will the ray cast.
     */
    public VoxelFaceRayCastContinuousTraverser(Vector3i initialVoxel, BiFunction<Vector3i, VoxelType, Boolean> returnCondition) {
        super(returnCondition);
        m_lastTraversedVoxelIndex = initialVoxel;
        m_lastTraversedRelativeVoxelIndex = new Vector3i();
        m_incomingDirectionVector = new Vector3i();
    }

    /**
     * Constructs a {@link VoxelFaceRayCastContinuousTraverser} with the supplied initial voxel, which is kept as a reference.
     *
     * @param initialVoxel    The initial voxel from where the ray starts. This variable is kept as reference and is changed locally.
     * @param world           The world in which to traverse voxels.
     * @param returnCondition {@link com.cheesygames.colonysimulation.math.bounding.VoxelRay} and as what to do whilst traversing the world. The break condition takes the absolute
     *                        voxel index, the voxel type and returns a boolean that signifies if the voxel traversal should stop (true) or not (false). Bear in mind to not modify
     *                        the absolute index, as it will interfere will the ray cast.
     */
    public VoxelFaceRayCastContinuousTraverser(Vector3i initialVoxel, World world, BiFunction<Vector3i, VoxelType, Boolean> returnCondition) {
        super(world, returnCondition);
        m_lastTraversedVoxelIndex = initialVoxel;
        m_lastTraversedRelativeVoxelIndex = new Vector3i();
        m_incomingDirectionVector = new Vector3i();
    }

    @Override
    protected boolean applyReturnCondition(Vector3i absoluteVoxelIndex) {
        m_incomingDirectionVector.set(absoluteVoxelIndex).subtractLocal(m_lastTraversedVoxelIndex);
        m_incomingDirection = Direction3D.findDirectionFromVectorLocal(m_incomingDirectionVector);

        boolean returnCondition = super.applyReturnCondition(absoluteVoxelIndex);

        m_lastTraversedVoxelIndex.set(absoluteVoxelIndex);
        m_lastTraversedRelativeVoxelIndex.set(m_relativeVoxelIndex);
        m_lastTraversedChunk = m_chunk;

        return returnCondition;
    }

    /**
     * Gets the incoming direction from the previous traversed voxel. If there is no previous traversed voxel, then the method returns {@link Direction3D#ZERO}. If the method
     * <pre>
     * {@link com.cheesygames.colonysimulation.math.bounding.VoxelRay#rayCastLocal(double, Function, Vector3i)}
     * </pre>
     * or
     * <pre>
     * {@link com.cheesygames.colonysimulation.math.bounding.VoxelRay#rayCast(double, Function)}
     * </pre>
     * have never been called with this object, then the method returns null.
     *
     * @return The incoming direction from the previous traversed voxel, {@link Direction3D#ZERO} if there is no previous traversed voxel and null if the method
     * <pre>
     * {@link com.cheesygames.colonysimulation.math.bounding.VoxelRay#rayCastLocal(double, Function, Vector3i)}
     * </pre>
     * or
     * <pre>
     * {@link com.cheesygames.colonysimulation.math.bounding.VoxelRay#rayCast(double, Function)}
     * </pre>
     * have never been called with this object.
     */
    public Direction3D getIncomingDirection() {
        return m_incomingDirection;
    }

    /**
     * Gets the last traversed voxel index. If the ray is currently traversing a voxel, then it is the voxel before the one currently traversing.
     *
     * @return The last traversed voxel index.
     */
    public Vector3i getLastTraversedVoxelIndex() {
        return m_lastTraversedVoxelIndex;
    }

    public Vector3i getIncomingDirectionVector() {
        return m_incomingDirectionVector;
    }

    public IChunkVoxelData getLastTraversedChunk() {
        return m_lastTraversedChunk;
    }

    public Vector3i getLastTraversedRelativeVoxelIndex() {
        return m_lastTraversedRelativeVoxelIndex;
    }

    /**
     * Sets the initial voxel of the ray cast, which is kept as a reference and changed locally. Must be set before the casting the ray.
     *
     * @param initialVoxel The initial voxel of the ray cast, which is kept as a reference and changed locally.
     *
     * @return The supplied initial voxel.
     */
    public Vector3i setInitialVoxel(Vector3i initialVoxel) {
        return m_lastTraversedVoxelIndex = initialVoxel;
    }
}
