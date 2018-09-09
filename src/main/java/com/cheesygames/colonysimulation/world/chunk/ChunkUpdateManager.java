package com.cheesygames.colonysimulation.world.chunk;

import com.cheesygames.colonysimulation.GameGlobal;
import com.cheesygames.colonysimulation.world.AbstractWorldEventEmitter;
import com.cheesygames.colonysimulation.world.chunk.lighting.ChunkLightingManager;
import com.cheesygames.colonysimulation.world.chunk.mesh.IChunkMeshGenerator;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChunkUpdateManager extends AbstractWorldEventEmitter {

    private IChunkMeshGenerator m_meshGenerator;
    private ChunkLightingManager m_chunkLightingManager;
    private Set<Chunk> m_chunksToRemesh;

    public ChunkUpdateManager(IChunkMeshGenerator meshGenerator) {
        m_meshGenerator = meshGenerator;
        m_chunkLightingManager = new ChunkLightingManager();
        m_chunksToRemesh = ConcurrentHashMap.newKeySet();
    }

    public void computeChunkMeshes() {
        m_chunksToRemesh.addAll(m_chunkLightingManager.getChunksAwaitingReset());
        m_chunksToRemesh.addAll(m_chunkLightingManager.getChunksAwaitingComputation());

        m_chunkLightingManager.computeLighting();

        if (!m_chunksToRemesh.isEmpty()) {
            Iterator<Chunk> chunksToRedrawIterator = m_chunksToRemesh.iterator();

            while (chunksToRedrawIterator.hasNext()) {
                Chunk chunkToRedraw = chunksToRedrawIterator.next();

                if (chunkToRedraw.computeIsEmpty()) {
                    GameGlobal.world.removeChunk(chunkToRedraw.getIndex());
                    chunkIsEmpty(chunkToRedraw);
                }
                else {
                    boolean wasMeshNullBefore = chunkToRedraw.getMesh() == null;
                    m_meshGenerator.generateMesh(chunkToRedraw);
                    chunkRemeshed(chunkToRedraw, wasMeshNullBefore);
                }

                chunksToRedrawIterator.remove();
            }
        }
    }

    /**
     * Adds the chunk to the queue of chunks that need remeshing.
     *
     * @param chunk The chunks that needs to be remeshed.
     *
     * @return True if the chunk was successfully added to the queue, false otherwise. False means that it was already awaiting to be remeshed.
     */
    public boolean addToRemeshing(Chunk chunk) {
        return m_chunksToRemesh.add(chunk);
    }

    public ChunkLightingManager getChunkLightingManager() {
        return m_chunkLightingManager;
    }
}
