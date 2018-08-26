package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.GameGlobal;
import com.cheesygames.colonysimulation.math.direction.Direction3D;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.chunk.voxel.Voxel;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelLightUtils;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;
import com.cheesygames.colonysimulation.world.generation.IWorldGenerator;
import com.jme3.math.FastMath;
import com.jme3.scene.Mesh;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A world chunk consisting of voxels. Its size on all X, Y and Z axes must be a power of 2.
 */
public class Chunk extends AbstractChunk {

    private Voxel[][][] m_voxels;
    private Mesh m_mesh;
    private boolean m_isEmpty;
    private ChunkLightingState m_lightingState;

    public Chunk(Vector3i index) {
        super(index);
        this.m_isEmpty = true;
        this.m_lightingState = ChunkLightingState.AWAITING_RESET;
    }

    /**
     * Generates this world chunk's voxel data according to the supplied generator.
     *
     * @param generator The generator used to generate the voxel data.
     */
    public void generateData(IWorldGenerator generator) {
        final Vector3i chunkSize = getSize();
        m_voxels = new Voxel[chunkSize.x][chunkSize.y][chunkSize.z];

        for (int x = 0; x < chunkSize.x; ++x) {
            for (int y = 0; y < chunkSize.y; ++y) {
                for (int z = 0; z < chunkSize.z; ++z) {
                    m_voxels[x][y][z] = generator.generateVoxel(GameGlobal.world.getAbsoluteIndexX(m_index.x, x),
                        GameGlobal.world.getAbsoluteIndexY(m_index.y, y),
                        GameGlobal.world.getAbsoluteIndexZ(m_index.z, z));

                    m_voxels[x][y][z].light = m_voxels[x][y][z].voxelType.getLight();

                    m_isEmpty &= (m_voxels[x][y][z].voxelType == VoxelType.AIR);
                }
            }
        }

        m_lightingState = ChunkLightingState.AWAITING_COMPUTATION;
    }

    /**
     * Checks if the chunk is empty and overrides the previous {@link #isEmpty()} state.
     *
     * @return True if the chunk is empty, false otherwise.
     */
    public boolean computeIsEmpty() {
        boolean isEmpty = true;
        Vector3i chunkSize = getSize();

        for (int x = 0; x < chunkSize.x; ++x) {
            for (int y = 0; y < chunkSize.y; ++y) {
                for (int z = 0; z < chunkSize.z; ++z) {
                    isEmpty &= m_voxels[x][y][z].voxelType != VoxelType.SOLID;
                }
            }
        }

        return m_isEmpty = isEmpty;
    }

    /**
     * Resets the lighting and then propagates all the lights.
     */
    public void computeLighting() {
        m_lightingState = ChunkLightingState.AWAITING_RESET;

        List<Vector3i> voxelsToPropagate = new ArrayList<>();
        Vector3i chunkSize = getSize();

        // Reset lighting and find lights
        for (int x = 0; x < chunkSize.x; ++x) {
            for (int y = 0; y < chunkSize.y; ++y) {
                for (int z = 0; z < chunkSize.z; ++z) {
                    if (m_voxels[x][y][z].voxelType.emitsLight() && doesVoxelHaveRoomToPropagateLight(x, y, z)) {
                        voxelsToPropagate.add(new Vector3i(x, y, z));
                    }

                    // TODO : Remove stellar light?
                    m_voxels[x][y][z].light = m_voxels[x][y][z].voxelType.getLight();
                }
            }

            m_lightingState = ChunkLightingState.AWAITING_COMPUTATION;
        }

        // Propagate light
        Iterator<Vector3i> lightPropagationIterator = voxelsToPropagate.iterator();
        while (lightPropagationIterator.hasNext()) {
            Vector3i voxelIndexToPropagate = lightPropagationIterator.next();
            Voxel voxelToPropagate = getVoxelAt(voxelIndexToPropagate);

            for (int directionIndex = 0; directionIndex < Direction3D.ORTHOGONALS.length; ++directionIndex) {
                Vector3i adjacentVoxelDirection = Direction3D.ORTHOGONALS[directionIndex].getDirection();

                int adjacentVoxelX = voxelIndexToPropagate.x + adjacentVoxelDirection.x;
                int adjacentVoxelY = voxelIndexToPropagate.y + adjacentVoxelDirection.y;
                int adjacentVoxelZ = voxelIndexToPropagate.z + adjacentVoxelDirection.z;

                if (adjacentVoxelX >= 0 && adjacentVoxelX < chunkSize.x && adjacentVoxelY >= 0 && adjacentVoxelY < chunkSize.y && adjacentVoxelZ >= 0
                    && adjacentVoxelZ < chunkSize.z) {
                    Voxel adjacentVoxel = m_voxels[adjacentVoxelX][adjacentVoxelY][adjacentVoxelZ];

                    if (!adjacentVoxel.voxelType.isSolid()) {
                        adjacentVoxel.light = VoxelLightUtils.propagateLight(voxelToPropagate.light, adjacentVoxel.light);

                        // TODO : Add adjacent voxels that can receive light propagation to the list
                    }
                }
            }
        }

        m_lightingState = ChunkLightingState.OK;
        // TODO : Finish
    }

    /**
     * Checks if the voxel at the supplied indices has room to propagate light. For a voxel, having room to propagate light means that at least one adjacent voxel of the same chunk
     * is empty ({@link VoxelType#AIR}) or transparent. If true, then it also checks if those voxels have a lower light intensity than the source voxel.
     *
     * @param x The index on the X axis.
     * @param y The index on the Y axis.
     * @param z The index on the Z axis.
     *
     * @return True if the voxel at the supplied indices has room to propagate light, false otherwise.
     */
    private boolean doesVoxelHaveRoomToPropagateLight(int x, int y, int z) {
        boolean isThereRoomToPropagateLight = false;
        Vector3i chunkSize = getSize();
        Voxel voxel = m_voxels[x][y][z];

        for (int directionIndex = 0; directionIndex < Direction3D.ORTHOGONALS.length && !isThereRoomToPropagateLight; ++directionIndex) {
            Vector3i adjacentVoxelDirection = Direction3D.ORTHOGONALS[directionIndex].getDirection();

            int adjacentVoxelX = x + adjacentVoxelDirection.x;
            int adjacentVoxelY = y + adjacentVoxelDirection.y;
            int adjacentVoxelZ = z + adjacentVoxelDirection.z;

            if (adjacentVoxelX >= 0 && adjacentVoxelX < chunkSize.x && adjacentVoxelY >= 0 && adjacentVoxelY < chunkSize.y && adjacentVoxelZ >= 0 && adjacentVoxelZ < chunkSize.z) {

                Voxel adjacentVoxel = m_voxels[adjacentVoxelX][adjacentVoxelY][adjacentVoxelZ];
                isThereRoomToPropagateLight |= !adjacentVoxel.voxelType.isSolid() && VoxelLightUtils.canPropagateLight(voxel.light, adjacentVoxel.light);
            }
        }

        return isThereRoomToPropagateLight;
    }

    @Override
    public Voxel getVoxelAt(Vector3i voxelIndex) {
        return m_voxels[voxelIndex.x][voxelIndex.y][voxelIndex.z];
    }

    @Override
    public Voxel getVoxelAt(int x, int y, int z) {
        return m_voxels[x][y][z];
    }

    /**
     * Gets the voxel at the supplied indices. The indices are clamped within the method so that it is safe to supply any indices possible.
     *
     * @param x The index on the X axis.
     * @param y The index on the Y axis.
     * @param z The index on the Z axis.
     *
     * @return The voxel at the supplied indices, if they are correct. If not, then returns the voxel at the clamped indices.
     */
    public Voxel getVoxelSafelyAt(int x, int y, int z) {
        Vector3i chunkSize = getSize();
        return m_voxels[FastMath.clamp(x, 0, chunkSize.x)][FastMath.clamp(y, 0, chunkSize.y)][FastMath.clamp(z, 0, chunkSize.z)];
    }

    @Override
    public boolean isEmpty() {
        return m_isEmpty;
    }

    public void setEmpty(boolean empty) {
        m_isEmpty = empty;
    }

    public Mesh getMesh() {
        return m_mesh;
    }

    public void setMesh(Mesh mesh) {
        m_mesh = mesh;
    }

    public void setVoxelTypeAt(VoxelType voxelType, Vector3i voxelIndex) {
        m_voxels[voxelIndex.x][voxelIndex.y][voxelIndex.z].voxelType = voxelType;
    }

    public void setVoxelTypeAt(VoxelType voxelType, int x, int y, int z) {
        m_voxels[x][y][z].voxelType = voxelType;
    }
}
