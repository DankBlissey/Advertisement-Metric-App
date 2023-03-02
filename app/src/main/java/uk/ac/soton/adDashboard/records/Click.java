package uk.ac.soton.adDashboard.records;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Click {

  private final Date date;
  private final int cost;
  private final long id;

  public Click(String date,long id,int cost) throws Exception {
    SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy HH:mm");
    this.date= format.parse(date);
    this.id=id;
    if (cost<0) {
      throw new Exception("negative cost");
    }
    this.cost=cost;
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
