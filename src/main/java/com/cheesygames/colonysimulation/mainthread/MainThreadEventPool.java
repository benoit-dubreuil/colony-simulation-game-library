package com.cheesygames.colonysimulation.mainthread;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Static class for handling events in the main thread.
 */
public final class MainThreadEventPool {

    private static final ConcurrentLinkedQueue<IMainThreadEvent> eventPool = new ConcurrentLinkedQueue();

    private MainThreadEventPool() {
    }

    /**
     * Adds an event to the main thread event pool.
     *
     * @param event The event to add to the main thread event pool.
     */
    public static void addEvent(IMainThreadEvent event) {
        eventPool.add(event);
    }

    /**
     * Executes all stored events and effectively empties the eventPool by polling all events.
     */
    public static void executeAllEvents() {
        IMainThreadEvent event = null;
        while ((event = eventPool.poll()) != null) {
            event.execute();
        }
    }
}
