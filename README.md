# high-resolution-clock
Java Clock implementation with nanosecond resolution in Linux

## Use Cases

### High-resolution clock for Java 8

In Java 8 `Instant.now()` returns instant with millisecond precision. This library can be used as a workaround.

```java
Clock clock = new HighResolutionClock();
Instant instant = clock.instant();
```

### Garbage-free timestamps

Java 9 introduced `VM.getNanoTimeAdjustment(offset)` method that can be used to retrieve high-precision 
timestamps as primitive values. `High-resolution clock` class can solve similar task, but return the result 
a different way. `getTimeAsLong()` method returns a 64-bit number where first 32 bits are used to store unix 
seconds, and last 32 bit are used for nanos of second. Hence, maximum date that can be correctly returned
by this method is 2038-01-19. 

```java
HighResolutionClock clock = new HighResolutionClock();
long timestamp = clock.getTimeAsLong();
int seconds = HighResolutionClock.getEpochSecondFromTimestamp(timestamp);
int nanos = HighResolutionClock.getNanosecondsFromTimestamp(timestamp);
```

