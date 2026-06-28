package net.pkhapps.dispatchsimulator.engine.clock;

import com.vaadin.flow.shared.Registration;

import java.time.Duration;
import java.util.function.Consumer;

public interface SimulationClock {

    Duration timeSinceSimulationStart();

    Registration subscribe(Consumer<SimulationClockEvent> subscriber);

    void start();

    void pause();

    void setSpeed(SimulationClockSpeed speed);
}
