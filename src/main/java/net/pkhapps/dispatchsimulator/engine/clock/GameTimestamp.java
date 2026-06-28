package net.pkhapps.dispatchsimulator.engine.clock;

import java.time.Duration;

/**
 * Represents a timestamp inside the game.
 *
 * @param timeSinceStart the time that has passed since the game started (never negative).
 */
public record GameTimestamp(Duration timeSinceStart) {

    public GameTimestamp {
        if (timeSinceStart.isNegative()) {
            throw new IllegalArgumentException("timeSinceStart cannot be negative");
        }
    }

    /**
     * Returns a copy of this timestamp with the given {@code duration} added.
     *
     * @throws IllegalArgumentException if {@code duration} is negative.
     */
    public GameTimestamp plus(Duration duration) {
        if (duration.isNegative()) {
            throw new IllegalArgumentException("duration cannot be negative");
        }
        return new GameTimestamp(timeSinceStart.plus(duration));
    }

    /**
     * Returns the absolute (always positive) duration between both timestamps.
     */
    public static Duration between(GameTimestamp t1, GameTimestamp t2) {
        return t1.timeSinceStart.minus(t2.timeSinceStart).abs();
    }

    /**
     * Returns whether this timestamp is at or after the {@code other} timestamp.
     */
    public boolean isAtOrAfter(GameTimestamp other) {
        return timeSinceStart.compareTo(other.timeSinceStart) >= 0;
    }

    /**
     * Returns whether this timestamp is before the {@code other} timestamp.
     */
    public boolean isBefore(GameTimestamp other) {
        return timeSinceStart.compareTo(other.timeSinceStart) < 0;
    }
}
