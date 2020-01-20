package com.axibase.clock.jmh;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

import com.axibase.clock.HighResolutionClock;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

@Warmup(iterations = 1, time = 3)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class InstantCreationBenchmark {
	private static final HighResolutionClock clock = new HighResolutionClock(ZoneOffset.UTC);

	@Benchmark
	public Instant usingInstant() {
		return Instant.now();
	}

	@Benchmark
	public Instant usingCustomClock() {
		return clock.instant();
	}

	@Benchmark
	public long usingCustomLong() {
		return clock.getTimeAsLong();
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
			.include("InstantCreationBenchmark.*")
			.jvmArgs("-server", "-Xmx1024M")
			.warmupIterations(1)
			.warmupTime(TimeValue.seconds(3))
			.measurementIterations(5)
			.addProfiler( GCProfiler.class )
			.forks(1)
			.build();
		new Runner(opt).run();
	}
}
