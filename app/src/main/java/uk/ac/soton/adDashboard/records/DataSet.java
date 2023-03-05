package uk.ac.soton.adDashboard.records;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javafx.util.Pair;

/**
 * The model that stores and manipulates the dataset.
 */
public class DataSet {

  private final ArrayList<Click> clicks;
  private final ArrayList<Impression> impressions;
  private final ArrayList<ServerAccess> serverAccess;
  private final HashMap<Long, User> users;


  /**
   * If true the pagesViewed is the bounce metric.
   */
  private boolean pagesViewedBounceMetric;

  /**
   * The minimum number of pages for a server access to not count as a bounce. Default=1
   */
  private int pagesForBounce = 1;

  /**
   * The time spent on the site to qualify as a bounce, provided the appropriate flag is set.
   * Default=50 seconds
   */
  private int interval = 50;


  /**
   * Creates a model manager object.
   *
   * @param clicks             This is an ArrayList of Click objects.
   * @param impressions        This is an ArrayList of Impression objects.
   * @param serverAccesses     This is an ArrayList of ServerAccess objects.
   * @param users              A hashmap of users, because the id is unique.
   * @param pageViewedAsMetric Whether to use pagesViewed as the bounce metric.
   */
  public DataSet(ArrayList<Click> clicks,
      ArrayList<Impression> impressions,
      ArrayList<ServerAccess> serverAccesses, HashMap<Long, User> users,
      boolean pageViewedAsMetric) {
    this.clicks = clicks;
    this.impressions = impressions;
    this.serverAccess = serverAccesses;
    this.users = users;
    this.pagesViewedBounceMetric = pageViewedAsMetric;

  }

  /**
   * Sets the bounce interval.
   *
   * @param interval The duration in seconds a user has to visit a website to not count as a
   *                 bounce.
   */
  public void setInterval(int interval) {
    this.interval = interval;
  }

  /**
   * Sets the pages for a bounce.
   *
   * @param pagesForBounce The number of pages that will have to be viewed to not count as a
   *                       bounce.
   */
  public void setPagesForBounce(int pagesForBounce) {
    this.pagesForBounce = pagesForBounce;
  }

  public ArrayList<Click> getClicks() {
    return clicks;
  }

  public ArrayList<Impression> getImpressions() {
    return impressions;
  }

  public ArrayList<ServerAccess> getServerAccess() {
    return serverAccess;
  }

  public HashMap<Long, User> getUsers() {
    return users;
  }

  public boolean isPagesViewedBounceMetric() {
    return pagesViewedBounceMetric;
  }

  public int getPagesForBounce() {
    return pagesForBounce;
  }

  public int getInterval() {
    return interval;
  }

  /**
   * Checks if the user matches the filterObject.
   *
   * @param user The User to be checked.
   * @return True if the user matches the filter.
   */
  public boolean matchesFilters(User user) {
    return true;
  }

  /**
   * Calculates the total cost of impressions between a given start and end datetime, that match the
   * filter.
   *
   * @param start The LocalDateTime which the impression must be more recent than.
   * @param end   The LocalDateTime which the impression must be before.
   * @return The double representing the cost of all the impressions in the range.
   */
  public double calcImpressionCost(LocalDateTime start, LocalDateTime end) {
    double totalCost = 0;
    for (Impression impression : impressions) {
      if (matchesFilters(users.get(impression.getId()))) { //if user legal
        if (impression.getDate().compareTo(start) >= 0
            && impression.getDate().compareTo(end) <= 0) {
          totalCost += impression.getCost();
        }
      }
    }
    return totalCost;
  }

  /**
   * Calculates the total cost of clicks between a given start and end datetime, that match the
   * filter.
   *
   * @param start The LocalDateTime of the beginning of the range.
   * @param end   The LocalDateTime of the end of the range.
   * @return Returns the cost of the clicks within the range, that match the filter.
   */
  public double calcClickCost(LocalDateTime start, LocalDateTime end) {
    double totalCost = 0;
    for (Click click : clicks) {
      //if user matches filter
      if (matchesFilters(users.get(click.getId()))) {
        if (click.getDate().compareTo(start) >= 0
            && click.getDate().compareTo(end) <= 0) {
          totalCost += click.getCost();
        }
      }
    }
    return totalCost;
  }

  /**
   * Calculates the total cost of the campaign (by summing impression costs and click costs) within
   * a time frame, that match the filter.
   *
   * @param start The start of the range.
   * @param end   The end of the range.
   * @return Returns the total cost between the specified LocalDateTimes.
   */
  public double calcTotalCost(LocalDateTime start, LocalDateTime end) {
    double totalCost = calcImpressionCost(start, end);
    totalCost += calcClickCost(start, end);

    return totalCost;
  }


  /**
   * The total number of clicks within a time range, where the user of the click matches the
   * filters.
   *
   * @param start The start of time range.
   * @param end   The end of the time range.
   * @return Returns the total number of clicks as a double, so it can be used in division.
   */
  public double totalClicks(LocalDateTime start, LocalDateTime end) {
    int count = 0;
    for (Click click : clicks) {
      if (matchesFilters(users.get(click.getId()))) { //if user legal
        if (click.getDate().compareTo(start) >= 0
            && click.getDate().compareTo(end) <= 0) {
          count += 1;
        }
      }
    }
    return count;
  }

  /**
   * The total number of impressions within a range, where the user and context match the filters.
   *
   * @param start The start of the range as LocalDateTime.
   * @param end   The end of the range as LocalDateTime.
   * @return Returns the total number of impressions.
   */
  public double totalImpressions(LocalDateTime start, LocalDateTime end) {
    int count = 0;
    for (Impression impression : impressions) {
      if (matchesFilters(users.get(impression.getId()))) { //if user legal
        if (impression.getDate().compareTo(start) >= 0
            && impression.getDate().compareTo(end) <= 0) {
          count += 1;
        }
      }
    }
    return count;
  }

  /**
   * Calculates the click-through rate (as the number of clicks per impression)
   *
   * @param start The start of the range as LocalDateTime.
   * @param end   The end of the range as LocalDateTime.
   * @return Returns the click-through rate for a range and set of filters.
   */
  public double calcClickThrough(LocalDateTime start, LocalDateTime end) {
    double imp = totalImpressions(start, end);
    double clicks = totalClicks(start, end);
    return clicks / imp;
  }

  /**
   * Calculates the number of conversions in a range, matching a set of filters.
   *
   * @param start The start of the range as LocalDateTime.
   * @param end   The end of the range as LocalDateTime.
   * @return Returns the number of conversions in a range, matching a set of filters.
   */
  public double calcNumConversions(LocalDateTime start, LocalDateTime end) {
    int conversions = 0;
    for (ServerAccess access : serverAccess) {
      if (access.isConversion() && matchesFilters(users.get(access.getId()))) {
        if (access.getStartDate().compareTo(start) >= 0
            && access.getStartDate().compareTo(end) <= 0) {
          conversions += 1;
        }
      }
    }
    return conversions;
  }

  /**
   * Checks if a server access is a bounce.
   *
   * @param access The access to check.
   * @return Returns true if the access is a bounce and false otherwise.
   */
  public boolean isBounce(ServerAccess access) {
    if (isPagesViewedBounceMetric()) {
      if (access.getPagesViewed() < getPagesForBounce()) {
        return false;

      } else {
        return ChronoUnit.SECONDS.between(access.getStartDate(), access.getEndDate())
            >= getInterval();
      }

    }
    return true;
  }

  /**
   * Calculates the number of bounces in a time frame, that match a filter.
   *
   * @param start The start of the range as LocalDateTime.
   * @param end   The end of the range as LocalDateTime.
   * @return Returns the number of bounces.
   */
  public double calcBounces(LocalDateTime start, LocalDateTime end) {
    int bounces = 0;
    for (ServerAccess access : serverAccess) {
      if (isBounce(access) && matchesFilters(users.get(access.getId()))) {
        if (access.getStartDate().compareTo(start) >= 0
            && access.getStartDate().compareTo(end) <= 0) {
          bounces += 1;
        }
      }
    }
    return bounces;
  }

  /**
   * Calculates the number of bounces per click (the bounce rate)
   *
   * @param start The start of the time range.
   * @param end   The end of the time range.
   * @return Returns the bounce rate.
   */
  public double calcBounceRate(LocalDateTime start, LocalDateTime end) {
    double bounces = calcBounces(start, end);
    double clicks = totalClicks(start, end);
    return bounces / clicks;
  }

  /**
   * Calculates the cost per acquisition (as the average money spent per acquisition)
   *
   * @param start       The start of the time range.
   * @param granularEnd The end of the time range.
   * @return Returns the cost per acquisition.
   */
  public double calcCostAcquisition(LocalDateTime start, LocalDateTime granularEnd) {
    double cost = calcTotalCost(start, granularEnd);
    double conversions = calcNumConversions(start, granularEnd);
    return cost / conversions;
  }

  /**
   * Calculates the cost per click.
   *
   * @param start The start of the range as LocalDateTime.
   * @param end   The end of the range as LocalDateTime.
   * @return Returns the cost per click.
   */
  public double calcCostPerClick(LocalDateTime start, LocalDateTime end) {
    double cost = calcClickCost(start, end);
    double clicks = totalClicks(start, end);
    return cost / clicks;
  }

  /**
   * Calculates the number of unique user clicks, utilising a hashSet for efficiency.
   *
   * @param start The start of the range as LocalDateTime.
   * @param end   The end of the range as LocalDateTime.
   * @return Returns the number of unique users, who match the filter, who clicked on an
   * advertisement, in a given range.
   */
  public double calcUniqueUsersClick(LocalDateTime start, LocalDateTime end) {
    HashSet<Long> visited = new HashSet<>();
    int uniques = 0;
    for (Click click : clicks) {
      if (matchesFilters(users.get(click.getId()))) { //if user legal
        if (click.getDate().compareTo(start) >= 0
            && click.getDate().compareTo(end) <= 0) {
          if (!visited.contains(click.getId())) {
            uniques += 1;
            visited.add(click.getId());
          }
        }
      }
    }
    return uniques;
  }

  /**
   * Calculates the average amount spent on a campaign per thousand impressions.
   *
   * @param start The start of the range as LocalDateTime.
   * @param end   The end of the range as LocalDateTime.
   * @return Returns the cost per thousand impressions in a range, where the cost and impressions
   * match a filter.
   */
  public double costPerThousandImpre(LocalDateTime start, LocalDateTime end) {
    double cost = calcTotalCost(start, end);
    double impressions = totalImpressions(start, end);
    return cost / (impressions / 1000.0);
  }

  /**
   * Generates a set of x and y coordinates to plot.
   *
   * @param startTime The start time.
   * @param endTime   The end time range.
   * @param unit      The granularity of the increment.
   * @return Returns an ArrayList of coordinates on a relative axis.
   */
  public ArrayList<Pair<Integer, Double>> generateY(LocalDateTime startTime,
      LocalDateTime endTime, ChronoUnit unit) {
    ArrayList<Pair<Integer, Double>> points = new ArrayList<>();
    LocalDateTime next = startTime.plus(unit.getDuration());
    int x = 0;
    for (LocalDateTime day = startTime; day.compareTo(endTime) <= 0;
        day = day.plus(unit.getDuration())) {
      System.out.println(x + " " + day + " " + next);
      points.add(new Pair<>(x, totalImpressions(day, next)));
      next = day.plus(unit.getDuration());
      x++;
      System.out.println(points);
    }
    return points;
  }
}
