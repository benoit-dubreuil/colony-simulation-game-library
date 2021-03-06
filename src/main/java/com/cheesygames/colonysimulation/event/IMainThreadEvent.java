package com.cheesygames.colonysimulation.event;

/**
 * Interface for a main thread event data structure.
 */
public interface IMainThreadEvent {

    /**
     * Executes the event's purpose. This is actually executed on the main thread.
     */
    void execute();
}
