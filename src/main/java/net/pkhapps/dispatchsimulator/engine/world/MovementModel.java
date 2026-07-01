package net.pkhapps.dispatchsimulator.engine.world;

import net.pkhapps.dispatchsimulator.annotation.NotThreadSafe;
import net.pkhapps.dispatchsimulator.engine.clock.GameTimestamp;
import org.jspecify.annotations.Nullable;

/**
 * A model for calculating the location of a moving object in the game world. All moving objects follow a predefined
 * {@link GameRoute} and move at a constant {@link Speed}. The game clock controls where a moving object is based on
 * the time at which the movement started. A moving object can never stray or overshoot its route. An object always
 * stops at the last point of the route.
 */
@NotThreadSafe
public class MovementModel {

    private GameWorldPoint location;
    private @Nullable MovementState state;

    /**
     * Creates a new {@code MovementModel}.
     *
     * @param initialLocation the initial location of the moving object.
     */
    public MovementModel(GameWorldPoint initialLocation) {
        this.location = initialLocation;
    }

    /**
     * Returns the current location of the moving object.
     */
    public GameWorldPoint location() {
        return location;
    }

    /**
     * Starts a new movement with the given route and speed. The object will not actually move until {@link #update(GameTimestamp)} has been called.
     *
     * @param now   the timestamp at which the movement started. Further calls to {@link #update(GameTimestamp)} must be after this timestamp.
     * @param route the route to move along.
     * @param speed the speed (or average speed) to move at.
     * @throws IllegalArgumentException if the {@link #location() current location} is not the start of the given route.
     */
    public void startMovement(GameTimestamp now, GameRoute route, Speed speed) {
        if (!route.isStart(location)) {
            throw new IllegalArgumentException("Invalid route");
        }
        this.state = new MovementState(route, now, speed);
    }

    /**
     * Stops the moving object at its current location. Further updates will not move it until movement is started again
     * with a new route.
     */
    public void stopMovement() {
        this.state = null;
    }

    /**
     * Returns whether the object is currently moving or not.
     */
    public boolean isMoving() {
        return state != null;
    }

    /**
     * Updates the model with the given timestamp. The moving object will change the location according to its route and
     * speed. If the object is not moving, this method does nothing.
     *
     * @param now the current timestamp.
     * @return the result of the move (the new location and whether the object reached its destination or not).
     * @throws IllegalArgumentException if the given timestamp is in the past compared to when the object started moving.
     */
    public UpdateResult update(GameTimestamp now) {
        if (state != null) {
            if (now.isBefore(state.movingSince)) {
                throw new IllegalArgumentException("Given timestamp is in the past");
            }
            // Calculate the new location
            var movingDuration = GameTimestamp.between(now, state.movingSince);
            location = state.currentRoute.calculateLocation(movingDuration, state.speed);
            var isAtDestination = state.currentRoute.isEnd(location);
            if (isAtDestination) {
                stopMovement();
            }
            return new UpdateResult(location, isAtDestination);
        } else {
            // No route, stay where you are.
            return new UpdateResult(location, true);
        }
    }

    /**
     * The result of a {@link MovementModel} update.
     *
     * @param location      the current location of the moving object.
     * @param atDestination whether the moving object has reached its destination (the current location is the last point of the geometry).
     */
    public record UpdateResult(GameWorldPoint location, boolean atDestination) {
    }

    private record MovementState(
            GameRoute currentRoute,
            GameTimestamp movingSince,
            Speed speed
    ) {
    }
}
