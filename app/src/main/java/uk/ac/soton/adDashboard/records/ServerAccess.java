package uk.ac.soton.adDashboard.records;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerAccess {

  private final Date startDate;
  private final Date endDate;
  private final long id;
  private final int pagesViewed;
  private final boolean conversion;



  public ServerAccess(String entryDate, long id,String exitDate, int pagesViewed, boolean conversion)
      throws Exception {
    SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy HH:mm");
    this.startDate = format.parse(entryDate);
    this.endDate = format.parse(exitDate);
    this.id = id;
    if (pagesViewed<0) {
      throw new Exception("invalid number of pages viewed");
    }
    this.pagesViewed = pagesViewed;
    this.conversion = conversion;
  }

  public Date getStartDate() {
    return startDate;
  }

  public Date getEndDate() {
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
