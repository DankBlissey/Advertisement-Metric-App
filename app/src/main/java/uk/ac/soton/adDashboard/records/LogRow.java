package uk.ac.soton.adDashboard.records;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class LogRow {

  private final long id;
  private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm:ss");

  public LogRow(long id) throws Exception {
    if (id < 0) {
      throw new Exception("id shouldn't be negative");
    }
    this.id = id;
    format.withResolverStyle(ResolverStyle.SMART);

  }

  public LocalDateTime parseDateTime(String dateTime) {
    if (dateTime.equals("n/a")) {
      return null;
    }
    return LocalDateTime.parse(dateTime, format);
  }

}
