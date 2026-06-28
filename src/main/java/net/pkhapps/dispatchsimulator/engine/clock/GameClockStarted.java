package net.pkhapps.dispatchsimulator.engine.clock;

/**
 * Event fired by a {@link GameClock} when the clock is started or resumed after being paused.
 *
 * @param gameClock the game clock that fired the event.
 */
public record GameClockStarted(GameClock gameClock) implements GameClockEvent {
}
