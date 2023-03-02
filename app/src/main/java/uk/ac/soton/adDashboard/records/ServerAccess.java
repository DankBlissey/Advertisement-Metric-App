package uk.ac.soton.adDashboard.records;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import uk.ac.soton.adDashboard.enums.LogRow;

public class ServerAccess extends LogRow {

  private final LocalDateTime startDate;
  private final LocalDateTime endDate;
  private final long id;
  private final int pagesViewed;
  private final boolean conversion;


  public ServerAccess(String entryDate, long id, String exitDate, int pagesViewed,
      boolean conversion) throws Exception {
    super(id);
    this.startDate = parseDateTime(entryDate);
    this.endDate = parseDateTime(exitDate);

    this.id = id;
    if (pagesViewed < 0) {
      throw new Exception("invalid number of pages viewed");
    }
    this.pagesViewed = pagesViewed;
    this.conversion = conversion;
  }

  public LocalDateTime getStartDate() {
    return startDate;
  }

  public LocalDateTime getEndDate() {
    return endDate;
  }

  public long getId() {
    return id;
  }

  public int getPagesViewed() {
    return pagesViewed;
  }

  public boolean isConversion() {
    return conversion;
  }
}
