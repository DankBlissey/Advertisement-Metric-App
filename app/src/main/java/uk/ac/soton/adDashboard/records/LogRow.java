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
  private static DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  /**
   * Generates a row object.
   *
   * @param strID The id of the row.
   * @throws Exception If the ID is negative.
   */
  public LogRow(String strID) throws Exception {
    var id = Long.parseLong(strID);
    if (id < 0) {
      throw new Exception("id shouldn't be negative");
    }
    this.id = id;


  }

  public static void setResolver() {
    format.withResolverStyle(ResolverStyle.STRICT);

  }
  public long getId() {
    return id;
  }

  /**
   * This parses a string date of the format "yyyy-MM-dd HH:mm:ss" into a LocalDateTime object.
   *
   * @param dateTime The string to parse.
   * @return Returns a LocalDateTime object.
   * @throws DateTimeException Exception is thrown if the date is an invalid format.
   */
  public static LocalDateTime parseDateTime(String dateTime) throws DateTimeException {
    if (dateTime.equals("n/a")) {
      return null;
    }
    int year = (dateTime.charAt(0)-48)*1000+(dateTime.charAt(1)-48)*100+(dateTime.charAt(2)-48)*10+(dateTime.charAt(3)-48);
    int month = (dateTime.charAt(5)-48)*10 + (dateTime.charAt(6)-48);

    int day = ((dateTime.charAt(8)-48)*10)+(dateTime.charAt(9)-48);
    int hour = (dateTime.charAt(11)-48)*10+(dateTime.charAt(12)-48);
    int minute = (dateTime.charAt(14)-48)*10+(dateTime.charAt(15)-48);
    int second = (dateTime.charAt(17)-48)*10+(dateTime.charAt(18)-48);
    return LocalDateTime.of(year,month,day,hour,minute,second);
//    return LocalDateTime.parse(dateTime, format);
  }

}
