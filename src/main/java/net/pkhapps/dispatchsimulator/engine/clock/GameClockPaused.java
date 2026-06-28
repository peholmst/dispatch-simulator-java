package net.pkhapps.dispatchsimulator.engine.clock;

/**
 * Event fired by a {@link GameClock} when the clock is paused.
 *
 * @param gameClock the game clock that fired the event.
 */
public record GameClockPaused(GameClock gameClock) implements GameClockEvent {
}
