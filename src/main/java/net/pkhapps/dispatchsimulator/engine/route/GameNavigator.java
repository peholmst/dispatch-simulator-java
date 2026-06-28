package net.pkhapps.dispatchsimulator.engine.route;


import net.pkhapps.dispatchsimulator.engine.geo.GameWorldPoint;

import java.util.Optional;

/**
 * API for a game navigator that calculates routes between points in the game world.
 */
public interface GameNavigator {

    /**
     * Finds the shortest route between {@code origin} and {@code destination}.
     *
     * @param origin      the start point.
     * @param destination the end point.
     * @return a {@link GameRoute}, or an empty {@code Optional} if it is not possible to travel between the two points.
     */
    Optional<GameRoute> findRoute(GameWorldPoint origin, GameWorldPoint destination);
}
