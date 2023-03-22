package uk.ac.soton.adDashboard.enums;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;

/**
 * The granularity of the graph.
 */
public enum Granularity {
  DAY,
  WEEK,
  MONTH,
  YEAR;
  /**
   * Calculates the step based on the granularity enum.
   *
   * @return Returns a temporal amount to step by.
   */
  public TemporalAmount generateStep() {
    Duration step;
    switch (this) {
      case DAY -> step = ChronoUnit.DAYS.getDuration();
      case WEEK -> step = ChronoUnit.DAYS.getDuration().multipliedBy(7);
      case MONTH -> step = ChronoUnit.MONTHS.getDuration();
      case YEAR -> step = ChronoUnit.YEARS.getDuration();
      default -> step = ChronoUnit.DAYS.getDuration();
    }
    return step;
  }



}
