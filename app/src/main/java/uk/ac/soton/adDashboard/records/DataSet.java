package uk.ac.soton.adDashboard.records;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javafx.util.Pair;
import uk.ac.soton.adDashboard.enums.Context;
import uk.ac.soton.adDashboard.enums.Gender;
import uk.ac.soton.adDashboard.enums.Income;
import uk.ac.soton.adDashboard.filter.Filter;

/**
 * The model that stores and manipulates the dataset.
 */
public class DataSet {

  private ArrayList<Click> clicks;
  private ArrayList<Impression> impressions;
  private ArrayList<ServerAccess> serverAccess;
  private HashMap<Long, User> users;
  public double[] stats = {};

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

  private Filter filter = new Filter();

  private int lastImpression = 0;
  private int lastAccess = 0;
  private int lastClick = 0;
  private boolean efficiency = false;


  /**
   * Creates a model manager object.
   */
  public DataSet() {

  }

  public void setClicks(ArrayList<Click> clicks) {
    this.clicks = clicks;
  }

  public void setImpressions(ArrayList<Impression> impressions) {
    this.impressions = impressions;
  }

  public void setServerAccess(
      ArrayList<ServerAccess> serverAccess) {
    this.serverAccess = serverAccess;
  }

  public void setUsers(HashMap<Long, User> users) {
    this.users = users;
  }

  /**
   * Sets the bounce interval.
   *
   * @param interval The duration in seconds a user has to visit a website to not count as a
   *                 bounce.
   */
  public void setInterval(int interval) {
    this.interval = interval;
    resetStats();
  }

  /**
   * Sets the pages for a bounce.
   *
   * @param pagesForBounce The number of pages that will have to be viewed to not count as a
   *                       bounce.
   */
  public void setPagesForBounce(int pagesForBounce) {
    this.pagesForBounce = pagesForBounce;
    resetStats();
  }


  public void setPagesViewedBounceMetric(Boolean BounceMetric) {
    this.pagesViewedBounceMetric = BounceMetric;
    resetStats();
  }

  public void resetStats() {
    stats = new double[]{};
  }

  public Filter getFilter() {
    return filter;
  }

  public void setFilter(Filter filter) {
    this.filter = filter;
  }

  public boolean isEfficient() {
    return efficiency;
  }

  public void setEfficiency(boolean efficiency) {
    this.efficiency = efficiency;
  }

  public int getLastImpression() {
    if (isEfficient()) {
      return lastImpression;
    } else {
      return 0;
    }
  }

  public void setLastImpression(int lastImpression) {
    this.lastImpression = lastImpression;
  }

  public int getLastAccess() {
    if (isEfficient()) {
      return lastAccess;
    } else {
      return 0;
    }
  }

  public void setLastAccess(int lastAccess) {
    this.lastAccess = lastAccess;
  }

  public int getLastClick() {
    if (isEfficient()) {
      return lastClick;
    } else {
      return 0;
    }
  }

  public void setLastClick(int lastClick) {
    this.lastClick = lastClick;
  }

  public ArrayList<Click> getClicks() {
    return clicks;
  }

  public void resetAllAccess() {
    setLastClick(0);
    setLastAccess(0);
    setLastImpression(0);
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

  public double[] getStats() {
    return stats;
  }

  /**
   * Checks if the user matches the filterObject.
   *
   * @param user The User to be checked.
   * @return True if the user matches the filter.
   */
  public boolean matchesFilters(User user) {
    if (!filter.getAge().equals("") && !user.getAge().equals(filter.getAge())) {
      return false;
    }
    if (!filter.getGender().equals(Gender.ANY) && !user.getGender().equals(filter.getGender())) {
      return false;
    }
    if (filter.getIncome().equals(Income.ANY)) {
      return true;
    }
    return user.getIncome().equals(filter.getIncome());
  }

  public boolean matchesFilters(User user, Impression impression) {
    if (!matchesFilters(user)) {
      return false;
    }
    if (impression == null) {
      return true;
    }
    if (filter.getContext().equals(Context.ANY)) {
      return true;
    }
    return impression.getContext().equals(filter.getContext());
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
    for (int i = getLastImpression(); i < impressions.size(); i++) {

      if (matchesFilters(users.get(impressions.get(i).getId()),
          impressions.get(i))) { //if user legal
        if (!impressions.get(i).getDate().isBefore(start)
            && !impressions.get(i).getDate().isAfter(end)) {
          setLastImpression(i);
          totalCost += impressions.get(i).getCost();
        }  else if (!impressions.get(i).getDate().isAfter(end)) {
          setLastImpression(i);

        }else {
          break;
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
    for (int i = getLastClick(); i < clicks.size(); i++) {

      //if user matches filter
      if (matchesFilters(users.get(clicks.get(i).getId()),
          nearestImpression(clicks.get(i).getDate(), clicks.get(i).getId()))) {
        if (!clicks.get(i).getDate().isBefore(start)
            && !clicks.get(i).getDate().isAfter(end)) {
          totalCost += clicks.get(i).getCost();
          setLastClick(i);
        }  else if (!clicks.get(i).getDate().isAfter(end)) {
          setLastClick(i);

        }else {
          break;
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
    for (int i = getLastClick(); i < clicks.size(); i++) {

      if (matchesFilters(users.get(clicks.get(i).getId()),
          nearestImpression(clicks.get(i).getDate(), clicks.get(i).getId()))) { //if user legal
        if (clicks.get(i).getDate().compareTo(start) >= 0
            && clicks.get(i).getDate().compareTo(end) <= 0) {
          setLastClick(i);
          count += 1;
        }  else if (!clicks.get(i).getDate().isAfter(end)) {
          setLastClick(i);

        } else {
          break;
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
    System.out.println(getLastImpression());
    for (int i = getLastImpression(); i < impressions.size(); i++) {

      if (matchesFilters(users.get(impressions.get(i).getId()),
          impressions.get(i))) { //if user legal
        if (!impressions.get(i).getDate().isBefore(start)
            && !impressions.get(i).getDate().isAfter(end)) {
          setLastImpression(i);
          count += 1;
        } else if (!impressions.get(i).getDate().isAfter(end)) {
          setLastImpression(i);

        } else {
          break;
        }
      }
    }
    return count;
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
    for (int i = getLastAccess(); i < serverAccess.size(); i++) {

      if (serverAccess.get(i).isConversion() && matchesFilters(
          users.get(serverAccess.get(i).getId()),
          nearestImpression(serverAccess.get(i).getStartDate(), serverAccess.get(i).getId()))) {
        if (!serverAccess.get(i).getStartDate().isBefore(start)
            && !serverAccess.get(i).getStartDate().isAfter(end)) {
          setLastAccess(i);
          conversions += 1;
        } else if (!serverAccess.get(i).getStartDate().isAfter(end)) {
          setLastAccess(i);

        } else {
          break;
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
    if (access.getEndDate() == null) {
      return false;
    }
    if (isPagesViewedBounceMetric()) {
      return access.getPagesViewed() < getPagesForBounce();
    } else {

      return ChronoUnit.SECONDS.between(access.getStartDate(), access.getEndDate())
          < getInterval();
    }


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
    for (int i = getLastAccess(); i < serverAccess.size(); i++) {

      if (isBounce(serverAccess.get(i)) && matchesFilters(users.get(serverAccess.get(i).getId()),
          nearestImpression(serverAccess.get(i).getStartDate(), serverAccess.get(i).getId()))) {
        if (!serverAccess.get(i).getStartDate().isBefore(start)
            && !serverAccess.get(i).getStartDate().isAfter(end)) {
          bounces += 1;
          setLastAccess(i);
        }  else if (!serverAccess.get(i).getStartDate().isAfter(end)) {
          setLastAccess(i);

        }else {
          break;
        }
      }
    }
    return bounces;
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
    for (int i = getLastClick(); i < clicks.size(); i++) {

      if (matchesFilters(users.get(clicks.get(i).getId()),
          nearestImpression(clicks.get(i).getDate(), clicks.get(i).getId()))) { //if user legal
        if (clicks.get(i).getDate().compareTo(start) >= 0
            && clicks.get(i).getDate().compareTo(end) <= 0) {
          if (!visited.contains(clicks.get(i).getId())) {
            setLastClick(i);
            uniques += 1;
            visited.add(clicks.get(i).getId());
          } else {
            break;
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
    //todo: NaN divide by zero fix
    return cost / (impressions / 1000.0);
  }


  /**
   * Calculates the cost per click.
   *
   * @param start The start of the range as LocalDateTime.
   * @param end   The end of the range as LocalDateTime.
   * @return Returns the cost per click.
   */
  public double calcCostPerClick(LocalDateTime start, LocalDateTime end) {
    double cost = calcTotalCost(start, end);
    double clicks = totalClicks(start, end);
    return cost / clicks;
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
    setEfficiency(true);
    resetAllAccess();
    for (LocalDateTime day = startTime; !day.isAfter(endTime);
        day = day.plus(unit.getDuration())) {
//      System.out.println(x + " " + day + " " + next);
      points.add(new Pair<>(x, totalImpressions(day, next)));
      next = next.plus(unit.getDuration());
      x++;
    }
    System.out.println(points);

    resetAllAccess();
    setEfficiency(false);
    return points;
  }


  public LocalDateTime earliestDate() {
    return impressions.get(0).getDate();

  }

  public LocalDateTime latestDate() {
    return impressions.get(impressions.size() - 1).getDate();
  }
//TODO: reset stats on changes to filter.
  /**
   * Gets all the stats for the list view, only recalculating if there's been a change to any parameters.
   * @param start The start of the time range.
   * @param end The end of the time range.
   * @return Returns an array of impressionCost, impressions, clicks, uniques, bounces, conversions, cost,
   *         through, acquisitionCosts, clickCosts, thousand, bounceRate.
   */
  public double[] allStats(LocalDateTime start, LocalDateTime end) {
    setEfficiency(false);
    if (stats.length != 0) {
      return stats;
    }
    double impressionCost = calcImpressionCost(start, end);

    double impressions = totalImpressions(start, end);
    double clicks = totalClicks(start, end);
    double uniques = calcUniqueUsersClick(start, end);
    double bounces = calcBounces(start, end);
    double conversions = calcNumConversions(start, end);
    double cost = calcTotalCost(start, end);
    System.out.println("half way");
    double through = calcClickThrough(start, end);
    double acquisitionCosts = calcCostAcquisition(start, end);
    double clickCosts = calcCostPerClick(start, end);
    double thousand = costPerThousandImpre(start, end);
    double bounceRate = calcBounceRate(start, end);
    stats = new double[]{impressionCost, impressions, clicks, uniques, bounces, conversions, cost,
        through, acquisitionCosts, clickCosts, thousand, bounceRate};
    return stats;

  }

  public Impression nearestImpression(LocalDateTime time, long id) {
    int low=0;
    int high=impressions.size();
    int index=Integer.MAX_VALUE;
    int mid=0;
    while (low <= high) {
      mid = low  + ((high - low) / 2);
      if ( impressions.get(mid).getDate().isBefore(time)) {
        //if the truncate to day is the same then
        low = mid + 1;
      } else if ( impressions.get(mid).getDate().isAfter(time)) {
        high = mid - 1;
      } else if (impressions.get(mid).getDate().equals(time)) {
        index = mid;
        break;
      }
    }
    while (impressions.get(mid).getDate().isAfter(time)&&impressions.get(mid).getId()!=id) {
      mid-=1;
    }
    return impressions.get(mid);
  }



}
