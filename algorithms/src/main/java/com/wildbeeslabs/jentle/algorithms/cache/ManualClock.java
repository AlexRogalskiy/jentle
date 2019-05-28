package com.wildbeeslabs.jentle.algorithms.cache;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Mostly for testing, this clock must be explicitly set to a given value. Defaults to init.
 */
public class ManualClock implements Clock {

  private final AtomicLong time;

  /**
   * Construct a new clock setting the current time to {@code init}.
   *
   * @param init Number of milliseconds to use as the initial time.
   */
  public ManualClock(long init) {
    time = new AtomicLong(init);
  }

  /**
   * Update the current time to {@code t}.
   *
   * @param t Number of milliseconds to use for the current time.
   */
  public void set(long t) {
    time.set(t);
  }

  /**
   * {@inheritDoc}
   */
  public long now() {
    return time.get();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ManualClock clock = (ManualClock) o;
    return now() == clock.now();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return Long.valueOf(now()).hashCode();
  }
}
