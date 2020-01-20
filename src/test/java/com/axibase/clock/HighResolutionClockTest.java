package com.axibase.clock;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class HighResolutionClockTest {

	@Test
	void instant() {
		final HighResolutionClock clock = new HighResolutionClock(ZoneOffset.UTC);
		if (isInstantWithMillisecondPrecision(clock.instant()) && isInstantWithMillisecondPrecision(clock.instant())) {
			fail("High resolution clock must have precision higher than millisecond");
		}
	}

	private static boolean isInstantWithMillisecondPrecision(Instant instant) {
		return instant.getNano() % TimeUnit.MILLISECONDS.toNanos(1) == 0;
	}
}