package uk.ac.soton.adDashboard.records;


import java.time.LocalDateTime;

import uk.ac.soton.adDashboard.enums.Context;
import uk.ac.soton.adDashboard.enums.Gender;
import uk.ac.soton.adDashboard.enums.Income;

public class Impression extends LogRow {

  private final long id; //also the key of the hashmap :)

  private final int cost;


  private final LocalDateTime date;

  private Gender gender;

  public Impression(String date, long id, int cost)
      throws Exception {
    super(id);
    this.date = parseDateTime(date);
    this.id = id;
    this.cost = cost;

  }


  public long getId() {
    return id;
  }


  public int getCost() {
    return cost;
  }


  public Gender getGender() {
    return gender;
  }

  public LocalDateTime getDate() {
    return date;
  }


}
