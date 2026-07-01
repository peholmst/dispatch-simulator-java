package net.pkhapps.dispatchsimulator.engine.world;

import java.time.Duration;

/**
 * Represents the speed at which something is moving.
 *
 * @param metersPerSecond the speed in meters per second.
 * @see Distance
 */
public record Speed(int metersPerSecond) {

    /**
     * Returns the duration it takes to travel the given {@code distance} at this speed.
     */
    public Duration travelingTime(Distance distance) {
        return Duration.ofSeconds((distance.distanceInMeters() / metersPerSecond()));
    }

    /**
     * Returns the distance travelled in the given {@code duration} at this speed.
     */
    public Distance travelingDistance(Duration duration) {
        return new Distance(metersPerSecond() * duration.toSeconds());
    }
}
