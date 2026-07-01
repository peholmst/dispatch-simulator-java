package net.pkhapps.dispatchsimulator.engine.world;

import net.pkhapps.dispatchsimulator.engine.clock.GameTimestamp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MovementModelTest {

    private final static TestGameRoute STRAIGHT_LINE = new TestGameRoute(List.of(
            new GameWorldPoint(0, 0),
            new GameWorldPoint(1, 0),
            new GameWorldPoint(2, 0),
            new GameWorldPoint(3, 0)
    ));
    ;

    static class TestGameRoute implements GameRoute {

        // TODO Simplify since we're only using straight lines

        private final List<GameWorldPoint> geometry;
        private final Distance totalDistance;

        public TestGameRoute(List<GameWorldPoint> geometry) {
            if (geometry.isEmpty()) {
                throw new IllegalArgumentException("geometry is empty");
            }
            this.geometry = List.copyOf(geometry);
            this.totalDistance = distanceFrom(geometry.getFirst());
        }

        private long distanceBetween(GameWorldPoint point1, GameWorldPoint point2) {
            var deltaX = Math.abs(point1.x() - point2.x());
            var deltaY = Math.abs(point1.y() - point2.y());
            if (deltaX == 0) {
                return (long) deltaY;
            } else if (deltaY == 0) {
                return (long) deltaX;
            }
            return Math.round(Math.sqrt(deltaX * deltaX + deltaY * deltaY));
        }

        @Override
        public List<GameWorldPoint> geometry() {
            return geometry;
        }

        @Override
        public GameWorldPoint calculateLocation(Duration timeSinceStart, Speed speed) {
            var traveledDistance = speed.travelingDistance(timeSinceStart);
            if (traveledDistance.distanceInMeters() >= totalDistance.distanceInMeters()) {
                return geometry.getLast();
            }
            long distance = 0;
            for (int i = 0; i < geometry.size() - 1; ++i) {
                var p1 = geometry.get(i);
                var p2 = geometry.get(i + 1);
                distance += distanceBetween(p1, p2);
                if (distance >= traveledDistance.distanceInMeters()) {
                    return p2;
                }
            }
            return geometry.getLast(); // Should never happen
        }

        @Override
        public Duration travelingTime(Speed speed) {
            return speed.travelingTime(totalDistance);
        }

        @Override
        public Distance totalDistance() {
            return totalDistance;
        }

        @Override
        public Distance distanceFrom(GameWorldPoint point) {
            long distance = 0;
            for (int i = 0; i < geometry.size() - 1; ++i) {
                var p1 = geometry.get(i);
                var p2 = geometry.get(i + 1);
                distance += distanceBetween(p1, p2);
            }
            return new Distance(distance);
        }
    }

    @Test
    void initialState() {
        var model = new MovementModel(new GameWorldPoint(0, 0));
        assertThat(model.location()).isEqualTo(new GameWorldPoint(0, 0));
        assertThat(model.isMoving()).isFalse();
    }

    @Test
    void startMovement_staysAtOriginalPositionUntilFirstUpdate() {
        var model = new MovementModel(new GameWorldPoint(0, 0));
        model.startMovement(GameTimestamp.ofSeconds(0), STRAIGHT_LINE, new Speed(1));
        assertThat(model.location()).isEqualTo(new GameWorldPoint(0, 0));
        assertThat(model.isMoving()).isTrue();
    }

    @Test
    void startMovement_notAtStartOfRoute_throws() {
        var model = new MovementModel(new GameWorldPoint(1, 1));
        assertThatThrownBy(() -> model.startMovement(GameTimestamp.ofSeconds(0), STRAIGHT_LINE, new Speed(1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void update_notMoving_staysAtOriginalPosition() {
        var model = new MovementModel(new GameWorldPoint(0, 0));
        var result = model.update(GameTimestamp.ofSeconds(1));
        assertThat(result.location()).isEqualTo(new GameWorldPoint(0, 0));
        assertThat(result.atDestination()).isTrue();
        assertThat(model.location()).isEqualTo(result.location());
    }

    @Test
    void update_lessThanTravelDurationAndMoving_movesForward() {
        var model = new MovementModel(new GameWorldPoint(0, 0));
        model.startMovement(GameTimestamp.ofSeconds(0), STRAIGHT_LINE, new Speed(1));
        var result = model.update(GameTimestamp.ofSeconds(1));
        assertThat(result.location()).isEqualTo(STRAIGHT_LINE.geometry().get(1));
        assertThat(result.atDestination()).isFalse();
        assertThat(model.location()).isEqualTo(result.location());
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 4})
    void update_travelDurationOrLongerAndMoving_stopsAtEnd(int secondsSinceStart) {
        var model = new MovementModel(new GameWorldPoint(0, 0));
        model.startMovement(GameTimestamp.ofSeconds(0), STRAIGHT_LINE, new Speed(1));
        var result = model.update(GameTimestamp.ofSeconds(secondsSinceStart));
        assertThat(result.location()).isEqualTo(STRAIGHT_LINE.geometry().getLast());
        assertThat(result.atDestination()).isTrue();
        assertThat(model.location()).isEqualTo(result.location());
        assertThat(model.isMoving()).isFalse();
    }

    @Test
    void update_timestampInThePastAfterStartingToMove_throws() {
        var model = new MovementModel(new GameWorldPoint(0, 0));
        model.startMovement(GameTimestamp.ofSeconds(5), STRAIGHT_LINE, new Speed(1));
        assertThatThrownBy(() -> model.update(GameTimestamp.ofSeconds(1))).isInstanceOf(IllegalArgumentException.class);
    }
}
