package com.cheesygames.colonysimulation.world.chunk;

/**
 * Chunk lighting state. Does the lighting in a chunk needs to be reset, computed or is it ok?
 */
public enum ChunkLightingState {

    AWAITING_RESET,
    AWAITING_COMPUTATION,
    OK
}
