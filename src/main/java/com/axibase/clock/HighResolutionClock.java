package com.axibase.clock;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

/**
 * Clock implementation that supports nanosecond precision (in Linux)
 */
public class HighResolutionClock extends Clock {
	private static final boolean nativeLibLoaded = HighResolutionClockLoader.loadLibrary();

	private final ZoneId zoneId;

	public HighResolutionClock(ZoneId zoneId) {
		if (!nativeLibLoaded) {
			throw new IllegalStateException("clock_gettime library is not loaded");
		}
		this.zoneId = zoneId;
	}

	@Override
	public ZoneId getZone() {
		return zoneId;
	}

	@Override
	public Clock withZone(ZoneId zone) {
		return new HighResolutionClock(zone);
	}

	@Override
	public Instant instant() {
		final long time = clock_gettime();
		return Instant.ofEpochSecond( time >> 32, time & 0xFFFFFFFFL);
	}

	public long getTimeAsLong() {
		return clock_gettime();
	}

	public static int getEpochSecondFromTimestamp(long timestamp) {
		return (int)(timestamp >> 32);
	}

	public static int getNanosecondsFromTimestamp(long timestamp) {
		return (int)(timestamp & 0xFFFFFFFFL);
	}

	private native long clock_gettime();
}
