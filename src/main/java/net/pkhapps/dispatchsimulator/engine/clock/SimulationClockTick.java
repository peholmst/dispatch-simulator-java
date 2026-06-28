package net.pkhapps.dispatchsimulator.engine.clock;

import java.time.Duration;

public record SimulationClockTick(Duration timeSinceSimulationStart) implements SimulationClockEvent {
}
