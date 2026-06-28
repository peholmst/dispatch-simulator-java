package net.pkhapps.dispatchsimulator.engine.clock;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InGameTimestampTest {

    @Test
    void plusDuration_durationNotNegative_newTimestampReturned() {
        var start = new InGameTimestamp(Duration.ofSeconds(5));
        var duration = Duration.ofSeconds(7);
        var result = start.plus(duration);
        assertThat(result.timeSinceStart()).isEqualTo(Duration.ofSeconds(12));
    }

    @Test
    void plusDuration_durationNegative_throws() {
        var start = new InGameTimestamp(Duration.ofSeconds(5));
        var duration = Duration.ofSeconds(-7);
        assertThatThrownBy(() -> start.plus(duration)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void betweenTimestamp_resultNotNegative_durationReturned() {
        var earlier = new InGameTimestamp(Duration.ofSeconds(5));
        var later = new InGameTimestamp(Duration.ofSeconds(7));
        var result = InGameTimestamp.between(later, earlier);
        assertThat(result).isEqualTo(Duration.ofSeconds(2));
    }

    @Test
    void betweenTimestamp_resultNegative_absoluteDurationReturned() {
        var earlier = new InGameTimestamp(Duration.ofSeconds(5));
        var later = new InGameTimestamp(Duration.ofSeconds(7));
        var result = InGameTimestamp.between(earlier, later);
        assertThat(result).isEqualTo(Duration.ofSeconds(2));
    }

    @Test
    void isAtOrAfter() {
        var t1 = new InGameTimestamp(Duration.ofSeconds(5));
        var t2 = new InGameTimestamp(Duration.ofSeconds(7));
        assertThat(t2.isAtOrAfter(t1)).isTrue();
        assertThat(t2.isAtOrAfter(t2)).isTrue();
        assertThat(t1.isAtOrAfter(t2)).isFalse();
    }

    @Test
    void isBefore() {
        var t1 = new InGameTimestamp(Duration.ofSeconds(5));
        var t2 = new InGameTimestamp(Duration.ofSeconds(7));
        assertThat(t1.isBefore(t2)).isTrue();
        assertThat(t2.isBefore(t1)).isFalse();
    }
}
