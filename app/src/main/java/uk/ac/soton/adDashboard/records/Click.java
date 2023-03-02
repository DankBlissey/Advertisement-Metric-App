package uk.ac.soton.adDashboard.records;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import uk.ac.soton.adDashboard.enums.LogRow;


public class Click extends LogRow {

  private final LocalDateTime date;
  private final int cost;
  private final long id;

  public Click(String date, long id, int cost) throws Exception {
    super(id);

    this.date = parseDateTime(date);

    this.id = id;
    if (cost < 0) {
      throw new Exception("negative cost");
    }
    this.cost = cost;
  }

  public String getDate() {
    return date.toString();
  }

  public int getCost() {
    return cost;
  }

  public long getId() {
    return id;
  }
}
