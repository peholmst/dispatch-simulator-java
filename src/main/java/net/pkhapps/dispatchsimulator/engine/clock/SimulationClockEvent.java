package net.pkhapps.dispatchsimulator.engine.clock;

public sealed interface SimulationClockEvent permits SimulationClockPaused, SimulationClockStarted, SimulationClockTick{
}
