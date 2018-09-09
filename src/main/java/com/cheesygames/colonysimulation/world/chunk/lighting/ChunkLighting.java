package com.cheesygames.colonysimulation.world.chunk.lighting;

import com.cheesygames.colonysimulation.GameGlobal;
import com.cheesygames.colonysimulation.math.direction.Direction3D;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.World;
import com.cheesygames.colonysimulation.world.chunk.Chunk;
import com.cheesygames.colonysimulation.world.chunk.voxel.Voxel;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelLightUtils;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChunkLighting {

    private ChunkLightingState m_lightingState;
    private Chunk m_chunk;
    private Vector3i m_chunkSize;
    private List<Vector3i> m_voxelsToPropagate;

    public ChunkLighting(Chunk chunk) {
        this.m_lightingState = ChunkLightingState.AWAITING_RESET;
        this.m_chunk = chunk;
        this.m_chunkSize = chunk.getSize();
    }

    public void resetLighting() {
        World world = GameGlobal.world;
        m_voxelsToPropagate = new ArrayList<>();

        for (int x = 0; x < m_chunkSize.x; ++x) {
            for (int y = 0; y < m_chunkSize.y; ++y) {
                for (int z = 0; z < m_chunkSize.z; ++z) {
                    if (m_chunk.getVoxelAt(x, y, z).voxelType.emitsLight() && doesVoxelHaveRoomToPropagateLight(x, y, z)) {
                        m_voxelsToPropagate.add(new Vector3i(world.getAbsoluteIndexX(m_chunk.getIndex().x, x),
                            world.getAbsoluteIndexY(m_chunk.getIndex().y, y),
                            world.getAbsoluteIndexZ(m_chunk.getIndex().z, z)));
                    }

                    // TODO : Remove stellar light?
                    m_chunk.getVoxelAt(x, y, z).light = m_chunk.getVoxelAt(x, y, z).voxelType.getLight();
                }
            }
        }
    }

    /**
     * Resets the lighting and then propagates all the lights.
     */
    public void computeLighting() {
        if (m_lightingState == ChunkLightingState.AWAITING_RESET) {
            resetLighting();
        }

        World world = GameGlobal.world;

        Iterator<Vector3i> lightPropagationIterator = m_voxelsToPropagate.iterator();
        Chunk chunk = m_chunk;
        Vector3i chunkSize = m_chunkSize;
        Vector3i nextChunkIndex = new Vector3i();
        Vector3i voxelRelativeIndexToPropagate = new Vector3i();

        while (lightPropagationIterator.hasNext()) {
            Vector3i voxelAbsoluteIndexToPropagate = lightPropagationIterator.next();

            world.getChunkIndexLocal(voxelAbsoluteIndexToPropagate, nextChunkIndex);
            world.getVoxelRelativeIndexLocal(voxelAbsoluteIndexToPropagate, voxelRelativeIndexToPropagate);
            if (!nextChunkIndex.equals(chunk.getIndex())) {
                chunk = world.getChunkAt(nextChunkIndex);
                chunkSize = chunk.getSize();

                if (chunk.getChunkLighting().getLightingState().ordinal() <= ChunkLightingState.AWAITING_RESET.ordinal()) {

                }
            }

            Voxel voxelToPropagate = chunk.getVoxelAt(voxelRelativeIndexToPropagate);

            for (int directionIndex = 0; directionIndex < Direction3D.ORTHOGONALS.length; ++directionIndex) {
                Vector3i adjacentVoxelDirection = Direction3D.ORTHOGONALS[directionIndex].getDirection();

                int adjacentVoxelX = voxelRelativeIndexToPropagate.x + adjacentVoxelDirection.x;
                int adjacentVoxelY = voxelRelativeIndexToPropagate.y + adjacentVoxelDirection.y;
                int adjacentVoxelZ = voxelRelativeIndexToPropagate.z + adjacentVoxelDirection.z;

                if (adjacentVoxelX >= 0 && adjacentVoxelX < chunkSize.x && adjacentVoxelY >= 0 && adjacentVoxelY < chunkSize.y && adjacentVoxelZ >= 0
                    && adjacentVoxelZ < chunkSize.z) {
                    Voxel adjacentVoxel = chunk.getVoxelAt(adjacentVoxelX, adjacentVoxelY, adjacentVoxelZ);

                    if (!adjacentVoxel.voxelType.isSolid()) {
                        adjacentVoxel.light = VoxelLightUtils.propagateLight(voxelToPropagate.light, adjacentVoxel.light);

                        // TODO : Add adjacent voxels that can receive light propagation to the list
                    }
                }
            }
        }

        m_voxelsToPropagate = null;
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
        Voxel voxel = m_chunk.getVoxelAt(x, y, z);

        for (int directionIndex = 0; directionIndex < Direction3D.ORTHOGONALS.length && !isThereRoomToPropagateLight; ++directionIndex) {
            Vector3i adjacentVoxelDirection = Direction3D.ORTHOGONALS[directionIndex].getDirection();

            int adjacentVoxelX = x + adjacentVoxelDirection.x;
            int adjacentVoxelY = y + adjacentVoxelDirection.y;
            int adjacentVoxelZ = z + adjacentVoxelDirection.z;

            if (adjacentVoxelX >= 0 && adjacentVoxelX < m_chunkSize.x && adjacentVoxelY >= 0 && adjacentVoxelY < m_chunkSize.y && adjacentVoxelZ >= 0
                && adjacentVoxelZ < m_chunkSize.z) {

                Voxel adjacentVoxel = m_chunk.getVoxelAt(adjacentVoxelX, adjacentVoxelY, adjacentVoxelZ);
                isThereRoomToPropagateLight |= !adjacentVoxel.voxelType.isSolid() && VoxelLightUtils.canPropagateLight(voxel.light, adjacentVoxel.light);
            }
        }

        return isThereRoomToPropagateLight;
    }

    public ChunkLightingState getLightingState() {
        return m_lightingState;
    }

    public void setLightingState(ChunkLightingState lightingState) {
        m_lightingState = lightingState;
    }
}
