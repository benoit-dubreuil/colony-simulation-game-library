package com.cheesygames.colonysimulation.world;

import com.cheesygames.colonysimulation.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for emitting world events.
 */
public class AbstractWorldEventEmitter implements IWorldEventCommunicator {

    private List<IWorldEventCommunicator> m_listeners;

    public AbstractWorldEventEmitter() {
        this.m_listeners = new ArrayList<>();
    }

    @Override
    public void chunkRemeshed(Chunk chunk, boolean wasMeshNullBefore) {
        for (IWorldEventCommunicator listener : m_listeners) {
            listener.chunkRemeshed(chunk, wasMeshNullBefore);
        }
    }

    @Override
    public void chunkIsEmpty(Chunk chunk) {
        for (IWorldEventCommunicator listener : m_listeners) {
            listener.chunkIsEmpty(chunk);
        }
    }

    public void addListener(IWorldEventCommunicator listener) {
        m_listeners.add(listener);
    }

    public void removeListener(IWorldEventCommunicator listener) {
        m_listeners.remove(listener);
    }

    public void clearListeners() {
        m_listeners.clear();
    }
}
