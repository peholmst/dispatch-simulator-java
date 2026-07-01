package net.pkhapps.dispatchsimulator.engine.route;

import net.pkhapps.dispatchsimulator.engine.geo.Distance;
import net.pkhapps.dispatchsimulator.engine.geo.GameWorldPoint;
import net.pkhapps.dispatchsimulator.engine.geo.Speed;

import java.time.Duration;
import java.util.List;

/**
 * Represents a precalculated route that something can travel on.
 */
public interface GameRoute {

    /**
     * Returns the points that make up this route, from start to end.
     */
    List<GameWorldPoint> geometry();

    /**
     * Checks if the given point corresponds to the start of this route.
     */
    default boolean isStart(GameWorldPoint point) {
        return geometry().getFirst().equals(point);
    }

    /**
     * Checks if the given point corresponds to the end of this route.
     */
    default boolean isEnd(GameWorldPoint point) {
        return geometry().getLast().equals(point);
    }

    /**
     * Calculates where you would be after traveling this route for the given duration with the given average speed.
     * If the traveled distance is longer than the route, the final point of the route is returned.
     *
     * @throws IllegalArgumentException if the duration is negative.
     */
    GameWorldPoint calculateLocation(Duration timeSinceStart, Speed speed);

    /**
     * Returns the time it takes to drive this route from start to finish with the given average speed.
     */
    Duration travelingTime(Speed speed);

    /**
     * Returns the distance from the start of this route to the end of this route.
     */
    Distance totalDistance();

    /**
     * Returns the distance from the given {@code point} to the end of this route.
     *
     * @throws IllegalArgumentException if the given {@code point} is not in the {@link #geometry() geometry}.
     */
    Distance distanceFrom(GameWorldPoint point);
}
