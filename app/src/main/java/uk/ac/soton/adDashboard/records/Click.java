package uk.ac.soton.adDashboard.records;

import java.time.LocalDateTime;

/**
 * Stores one record of the clicks csv.
 */
public class Click extends LogRow {

  private final LocalDateTime date;
  private final double cost;
  private final long id;

  public Click(String date, long id, double cost) throws Exception {
    super(id);

    this.date = parseDateTime(date);

    this.id = id;
    if (cost < 0) {
      throw new Exception("negative cost");
    }
    this.cost = cost;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public double getCost() {
    return cost;
  }

  public long getId() {
    return id;
  }
}
