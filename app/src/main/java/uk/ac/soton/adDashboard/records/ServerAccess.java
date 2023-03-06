package uk.ac.soton.adDashboard.records;

import java.time.LocalDateTime;

/**
 * Stores a server access.
 */
public class ServerAccess extends LogRow {

  private final LocalDateTime startDate;
  private final LocalDateTime endDate;

  private final int pagesViewed;
  private final boolean conversion;

  /**
   * Creates a ServerAccess object.
   *
   * @param entryDate   The datetime the user entered the site.
   * @param id          The id of the user who visited.
   * @param exitDate    The datetime the user left the site.
   * @param pagesViewed The number of pages viewed.
   * @param conversion  Whether the user acted on the page and was a conversion.
   * @throws Exception If the input data is out of bounds an Exception is thrown.
   */
  public ServerAccess(String entryDate, String id, String exitDate, String pagesViewed,
      String conversion) throws Exception {
    super(id);
    this.startDate = parseDateTime(entryDate);
    this.endDate = parseDateTime(exitDate);
    Integer parsedPagesViewed = Integer.parseInt(pagesViewed);
    if (parsedPagesViewed < 0) {
      throw new Exception("invalid number of pages viewed");
    }
    this.pagesViewed = parsedPagesViewed;

    if (conversion.equals("Yes")) {
      this.conversion = true;
    } else {
      this.conversion = false;
    }
  }

  public LocalDateTime getStartDate() {
    return startDate;
  }

  public LocalDateTime getEndDate() {
    return endDate;
  }


  public int getPagesViewed() {
    return pagesViewed;
  }

  public boolean isConversion() {
    return conversion;
  }
}
