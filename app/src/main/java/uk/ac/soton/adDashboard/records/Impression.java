package uk.ac.soton.adDashboard.records;


import java.time.LocalDateTime;

import uk.ac.soton.adDashboard.enums.Context;
import uk.ac.soton.adDashboard.enums.Gender;

/**
 * Stores an impression, without the user's data.
 */
public class Impression extends LogRow {


  private final double cost;
  private final LocalDateTime date;
  private final Context context;


  /**
   * Creates an impression row object.
   *
   * @param date    The date of the impression.
   * @param id      The id of the user.
   * @param cost    The cost of the impression.
   * @param context The context of the impression.
   * @throws Exception Thrown if the record parameters are out of bounds.
   */
  public Impression(String date, String id, String cost, String context)
      throws Exception {
    super(id);
    this.date = parseDateTime(date);

    this.cost = Double.parseDouble(cost);
    this.context = contextSetup(context);

  }

  public Context getContext() {
    return context;
  }


  public double getCost() {
    return cost;
  }


  public LocalDateTime getDate() {
    return date;
  }

  /**
   * Checks the validity of the context.
   *
   * @param context The string context to check.
   * @return Returns a context enum.
   * @throws Exception This is thrown if the context is invalid.
   */
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
