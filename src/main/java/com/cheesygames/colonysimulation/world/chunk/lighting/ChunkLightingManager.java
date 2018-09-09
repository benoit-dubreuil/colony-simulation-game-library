package com.cheesygames.colonysimulation.world.chunk.lighting;

import com.cheesygames.colonysimulation.world.chunk.Chunk;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manager for chunk lighting computation.
 */
public class ChunkLightingManager {

    private Set<Chunk> m_chunksAwaitingReset;
    private Set<Chunk> m_chunksAwaitingComputation;

    public ChunkLightingManager() {
        m_chunksAwaitingReset = ConcurrentHashMap.newKeySet();
        m_chunksAwaitingComputation = ConcurrentHashMap.newKeySet();
    }

    public boolean addToAwaitingReset(Chunk chunk) {
        boolean wasAdded = m_chunksAwaitingReset.add(chunk);

        if (wasAdded) {
            chunk.getChunkLighting().setLightingState(ChunkLightingState.AWAITING_RESET);
        }

         return wasAdded;
    }

    public boolean addToAwaitingComputation(Chunk chunk) {
        boolean wasAdded = m_chunksAwaitingComputation.add(chunk);

        if (wasAdded) {
            chunk.getChunkLighting().setLightingState(ChunkLightingState.AWAITING_COMPUTATION);
        }

        return wasAdded;
    }

    public void computeLighting() {
        Iterator<Chunk> chunkIterator;

        if (!m_chunksAwaitingReset.isEmpty()) {
            chunkIterator = m_chunksAwaitingReset.iterator();
            while (chunkIterator.hasNext()) {
                Chunk chunk = chunkIterator.next();
                chunk.getChunkLighting().resetLighting();
                chunk.getChunkLighting().setLightingState(ChunkLightingState.AWAITING_COMPUTATION);
                chunkIterator.remove();
            }
        }

        if (!m_chunksAwaitingComputation.isEmpty()) {
            chunkIterator = m_chunksAwaitingComputation.iterator();
            while (chunkIterator.hasNext()) {
                Chunk chunk = chunkIterator.next();
                // TODO : remove comment when ready
           //     chunk.getChunkLighting().computeLighting();
                chunk.getChunkLighting().setLightingState(ChunkLightingState.OK);
                chunkIterator.remove();
            }
        }
    }

    public Set<Chunk> getChunksAwaitingReset() {
        return m_chunksAwaitingReset;
    }

    public Set<Chunk> getChunksAwaitingComputation() {
        return m_chunksAwaitingComputation;
    }
}