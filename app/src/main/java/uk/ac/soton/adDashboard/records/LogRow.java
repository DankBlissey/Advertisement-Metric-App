package uk.ac.soton.adDashboard.records;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

/**
 * Stores a row from some log file. It also contains the default date time formatter.
 */
public class LogRow {

  private final long id;
  private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm:ss");

  /**
   * Generates a row object.
   *
   * @param id The id of the row.
   * @throws Exception If the ID is negative.
   */
  public LogRow(long id) throws Exception {
    if (id < 0) {
      throw new Exception("id shouldn't be negative");
    }
    this.id = id;
    format.withResolverStyle(ResolverStyle.SMART);

  }

  /**
   * This parses a string date of the format "dd/MM/yyyy  HH:mm:ss" into a LocalDateTime object.
   *
   * @param dateTime The string to parse.
   * @return Returns a LocalDateTime object.
   * @throws DateTimeException Exception is thrown if the date is an invalid format.
   */
  public LocalDateTime parseDateTime(String dateTime) throws DateTimeException {
    if (dateTime.equals("n/a")) {
      return null;
    }
    return LocalDateTime.parse(dateTime, format);
  }

}
