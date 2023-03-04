package uk.ac.soton.adDashboard.records;


import java.time.LocalDateTime;

import uk.ac.soton.adDashboard.enums.Context;
import uk.ac.soton.adDashboard.enums.Gender;

public class Impression extends LogRow {

  private final long id; //also the key of the hashmap :)

  private final double cost;


  private final LocalDateTime date;
  private final Context context;

  private Gender gender;

  public Impression(String date, long id, double cost, String context)
      throws Exception {
    super(id);
    this.date = parseDateTime(date);
    this.id = id;
    this.cost = cost;
    this.context = contextSetup(context);

  }

  public Context getContext() {
    return context;
  }

  public long getId() {
    return id;
  }


  public double getCost() {
    return cost;
  }


  public Gender getGender() {
    return gender;
  }

  public LocalDateTime getDate() {
    return date;
  }

  private Context contextSetup(String context) throws Exception {
    if (context.equals("News")) {
      return Context.News;
    } else if (context.equals("Shopping")) {
      return Context.Shopping;
    } else if (context.equals("Social Media")) {
      return Context.Social;
    } else if (context.equals("Blog")) {
      return Context.Blog;
    } else if (context.equals("Hobbies")) {
      return Context.Hobbies;
    } else if (context.equals("Travel")) {
      return Context.Travel;
    } else if (context.equals("Media")) {
      return Context.Media;
    } else {
      throw new Exception("context invalid");
    }


  }

}
