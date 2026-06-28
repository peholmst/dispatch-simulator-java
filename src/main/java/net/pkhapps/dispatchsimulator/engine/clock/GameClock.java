package net.pkhapps.dispatchsimulator.engine.clock;

import com.vaadin.flow.shared.Registration;

import java.util.function.Consumer;

/**
 * Interface representing the clock inside a game. Every game clock always starts out stopped and has to be explicitly
 * started by calling {@link #start()}. A game clock can be paused, but not stopped (it stops when the game is ended).
 */
public interface GameClock {

    /**
     * Subscribes to {@link GameClockEvent}s. Use the returned registration handle to unsubscribe.
     */
    Registration subscribe(Consumer<GameClockEvent> subscriber);

    /**
     * Start the clock. If the clock is already running, nothing happens.
     */
    void start();

    /**
     * Pauses the clock. If the clock is already paused, nothing happens.
     */
    void pause();

    /**
     * Returns whether the clock is currently running.
     */
    boolean isRunning();

    /**
     * Returns the speed of the game clock. The default speed is implementation dependent.
     */
    GameClockSpeed speed();

    /**
     * Sets the speed of the game clock. The default speed is implementation dependent.
     */
    void setSpeed(GameClockSpeed speed);

    /**
     * Returns the current timestamp inside the game. If the game is paused, the same timestamp keeps being returned
     * until the clock is started again.
     */
    GameTimestamp now();
}
