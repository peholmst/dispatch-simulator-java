package net.pkhapps.dispatchsimulator.engine.clock;

/**
 * Event fired by a {@link GameClock} when the game time ticks forward.
 *
 * @param gameClock the game clock that fired the event.
 * @param now       the current time inside the game.
 */
public record GameClockTick(GameClock gameClock, GameTimestamp now) implements GameClockEvent {
}
