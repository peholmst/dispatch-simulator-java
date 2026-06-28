package net.pkhapps.dispatchsimulator.engine.clock;

import java.time.Duration;

/**
 * Interface representing the game clock speed, i.e. how fast time moves in the game compared to the real world.
 */
public interface GameClockSpeed {

    /**
     * Returns the duration that gets added to the {@link GameClock} for each tick.
     */
    Duration incrementPerTick();

    /**
     * Returns how many times the {@link GameClock} ticks during a single real world minute.
     */
    int ticksPerRealWorldMinute();

}
