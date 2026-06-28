package net.pkhapps.dispatchsimulator.engine.clock;

/**
 * Interface for events fired by a {@link GameClock}.
 */
public sealed interface GameClockEvent permits GameClockPaused, GameClockStarted, GameClockTick {

    /**
     * Returns the {@link GameClock} that fired the event.
     */
    GameClock gameClock();
}
