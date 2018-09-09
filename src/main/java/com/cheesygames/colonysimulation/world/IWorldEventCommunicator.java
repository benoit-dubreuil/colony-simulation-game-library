package com.cheesygames.colonysimulation.world;

import com.cheesygames.colonysimulation.world.chunk.Chunk;

/**
 * Interface for communicating events. They are either sent or received.
 */
public interface IWorldEventCommunicator {

    /**
     * Notice listeners or be noticed by the "chunk remeshed" event. The chunk is not empty and exists in the world.
     *
     * @param chunk             The chunk that was remeshed.
     * @param wasMeshNullBefore Was the chunk's mesh null before the remeshing?
     */
    void chunkRemeshed(Chunk chunk, boolean wasMeshNullBefore);

    /**
     * Notice listeners or be noticed by the "chunk is empty" event. That means the chunk was removed from the world because it is empty.
     *
     * @param chunk The chunk that was removed from the world and that is empty.
     */
    void chunkIsEmpty(Chunk chunk);
}
