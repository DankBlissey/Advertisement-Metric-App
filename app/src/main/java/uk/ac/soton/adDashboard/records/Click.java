package uk.ac.soton.adDashboard.records;

import java.time.LocalDateTime;

/**
 * Stores one record of the clicks csv.
 */
public class Click extends LogRow {

  private final LocalDateTime date;
  private final double cost;

  /**
   * Creates a click object.
   *
   * @param date The datetime of the click.
   * @param id   The id of the user who performed the click.
   * @param cost The cost of the click.
   * @throws Exception Thrown if the cost or id is negative or if the date is invalid.
   */
  public Click(String date, long id, double cost) throws Exception {
    super(id);

    this.date = parseDateTime(date);

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


}
