package uk.ac.soton.adDashboard.records;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javafx.util.Pair;
import uk.ac.soton.adDashboard.Interfaces.Function;
import uk.ac.soton.adDashboard.enums.Context;
import uk.ac.soton.adDashboard.enums.Gender;
import uk.ac.soton.adDashboard.enums.Granularity;
import uk.ac.soton.adDashboard.enums.Income;
import uk.ac.soton.adDashboard.enums.Stat;
import uk.ac.soton.adDashboard.filter.Filter;

/**
 * The model that stores and manipulates the dataset.
 */
public class DataSet {

  private ArrayList<Click> clicks;
  private ArrayList<Impression> impressions;
  private ArrayList<ServerAccess> serverAccess;
  private HashMap<Long, User> users;
  /**
   * Stores the basic overall stats list.
   */
  public double[] stats = {};
  /**
   * A boolean flag for efficiency that can enable or disable filtering.
   */
  public boolean filteringEnabled = false;

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

  /**
   * Stores the index of the last impression accessed from impressions.
   */
  private int lastImpression = 0;

  /**
   * Stores the index of the last ServerAccess in serverAccess.
   */
  private int lastAccess = 0;
  /**
   * Stores the index of the last click in clicks.
   */
  private int lastClick = 0;

  /**
   * A flag to internally toggle efficiency gains. Should usually be false.
   */
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

  /**
   * Resets the stats array.
   */
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

  /**
   * Resets the last accessed global variables.
   */
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

  public boolean isFilteringEnabled() {
    return filteringEnabled;
  }

  public void filteringEnabled(boolean filteringEnabled) {
    this.filteringEnabled = filteringEnabled;
  }

  public double[] getStats() {
    return stats;
  }

  /**
   * Checks if the user matches the filterObject.
   *
   * @param user The User to be checked.
   * @return True if the user matches the filter and False otherwise.
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

  /**
   * Checks if an impression and user match the filter.
   *
   * @param user       The user to check.
   * @param impression The impression to check.
   * @return Returns true if both match the filter and false otherwise.
   */
  public boolean matchesFilters(User user, Impression impression) {
    if (!filteringEnabled) {
      return true;
    }
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
        } else if (!impressions.get(i).getDate().isAfter(end)) {
          setLastImpression(i);

        } else {
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
        } else if (!clicks.get(i).getDate().isAfter(end)) {
          setLastClick(i);

        } else {
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
        } else if (!clicks.get(i).getDate().isAfter(end)) {
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
        } else if (!serverAccess.get(i).getStartDate().isAfter(end)) {
          setLastAccess(i);

        } else {
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
    if (impressions <= 0) {
      return cost / (1.0 / 1000);
    }
    return cost / (impressions / 1000.0);
  }

  /**
   * Overloaded version that takes a precalculated impressions count.
   *
   * @param start            The start of the range as LocalDateTime.
   * @param end              The end of the range as LocalDateTime.
   * @param totalImpressions A precalculated number of totalImpressions.
   * @return Returns the cost per thousand impressions in a range, where the cost and impressions
   * match a filter.
   */
  public double costPerThousandImpre(LocalDateTime start, LocalDateTime end,
      double totalImpressions) {
    double cost = calcTotalCost(start, end);

    if (totalImpressions <= 0) {
      return cost / (1.0 / 1000);
    }
    return cost / (totalImpressions / 1000.0);
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
    if (clicks <= 0) {
      return cost;
    }
    return cost / clicks;
  }

  /**
   * Overloaded version that calculates the cost per click but taking in a precalculated cost.
   *
   * @param start     The start of the range as LocalDateTime.
   * @param end       The end of the range as LocalDateTime.
   * @param totalCost A precalculated totalCost.
   * @return Returns the cost per click.
   */
  public double calcCostPerClick(LocalDateTime start, LocalDateTime end, double totalCost) {

    double clicks = totalClicks(start, end);
    if (clicks <= 0) {
      return totalCost;
    }
    return totalCost / clicks;
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
    if (conversions <= 0) {
      return cost;
    }
    return cost / conversions;
  }

  /**
   * Overloaded version that calculates the cost of acquisitions using a precalculated total cost.
   *
   * @param start       The start of the time range.
   * @param granularEnd The end of the time range.
   * @param totalCost   A precalculated totalCost.
   * @return Returns the cost per acquisition.
   */
  public double calcCostAcquisition(LocalDateTime start, LocalDateTime granularEnd,
      double totalCost) {
    double conversions = calcNumConversions(start, granularEnd);
    if (conversions <= 0) {
      return totalCost;
    }
    return totalCost / conversions;
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
    if (clicks <= 0) {
      return bounces;
    }
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
    double clicks = totalClicks(start, end);
    double imp = totalImpressions(start, end);
    if (imp <= 0) {
      return clicks;
    }
    return clicks / imp;
  }

  /**
   * Overloaded version that calculates the clickthroughrate using a precalculated total number of
   * impressions.
   *
   * @param start    The start of the range as LocalDateTime.
   * @param end      The end of the range as LocalDateTime.
   * @param totalImp A precalculated totalImpressions.
   * @return Returns the click-through rate for a range and set of filters.
   */
  public double calcClickThrough(LocalDateTime start, LocalDateTime end, double totalImp) {
    double clicks = totalClicks(start, end);
    if (totalImp <= 0) {
      return clicks;
    }
    return clicks / totalImp;
  }


  /**
   * Generates a set of x and y coordinates to plot based on the selected stat.
   *
   * @param startTime The start time.
   * @param endTime   The end time range.
   * @param unit      The granularity of the increment.
   * @return Returns an ArrayList of coordinates on a relative axis.
   */
  public List<Pair<Integer, Double>> generateY(LocalDateTime startTime,
      LocalDateTime endTime, Granularity unit, Stat stat) {
    List<Pair<Integer, Double>> points;

    Function f = pointGenerationSetup(stat);
    points = genPoints(startTime, endTime, unit, f);

    System.out.println(points);

    resetAllAccess();
    setEfficiency(false);
    return points;
  }

  /**
   * Generates a list of string ranges and the corresponding points.
   * @param startTime The start of the time range.
   * @param endTime The end of the time range.
   * @param unit The granularity of the range.
   * @param stat The stat type to display (currently, will just be clicks).
   * @return Returns a list of histogram points.
   */
  public List<Pair<String,Double>> generateHistogramY(LocalDateTime startTime,
      LocalDateTime endTime, Granularity unit, Stat stat) {
    List<Pair<String, Double>> points;


    Function f = pointGenerationSetup(stat);
    points = genHistogramPoints(startTime, endTime, unit, f);

    System.out.println(points);

    resetAllAccess();
    setEfficiency(false);
    return points;
  }

  /**
   * Sets up flags for point generation efficiency and returns a function to calculate.
   * @param stat The stat to calculate the points for.
   * @return Returns the function to call that matches the stat.
   */
  private Function pointGenerationSetup(Stat stat) {
    setEfficiency(true);
    resetAllAccess();
    filteringEnabled(true);
    Function f;
    switch (stat) {  //sets the function to perform in genPoints depending on the stat needed.
      case totalImpressions -> f = this::totalImpressions;
      case totalClicks -> f = this::totalClicks;
      case totalUniques -> f = this::calcUniqueUsersClick;
      case totalBounces -> f = this::calcBounces;
      case totalConversions -> f = this::calcNumConversions;
      case totalCost -> f = this::calcTotalCost;
      case CTR -> f = this::calcClickThrough;
      case CPA -> f = this::calcCostAcquisition;
      case CPC -> f = this::calcCostPerClick;
      case CPM -> f = this::costPerThousandImpre;
      case bounceRate -> f = this::calcBounceRate;
      default -> f = this::totalImpressions;
    }
    return f;
  }

  /**
   * Generates a set of points calculated by the passed function, between a given range, stepping by
   * a given unit.
   *
   * @param startTime The start of the range of points.
   * @param endTime   The end of the range of points.
   * @param unit      The unit to step by.
   * @param f         The function to calculate the y point, which is passed a range of dates.
   * @return Returns an arraylist of pairs of points.
   */
  private List<Pair<Integer, Double>> genPoints(LocalDateTime startTime,
      LocalDateTime endTime, Granularity unit, Function f) {
    ArrayList<Pair<Integer, Double>> points = new ArrayList<>();
    TemporalAmount amount = unit.generateStep();
    LocalDateTime next = startTime.plus(amount);
    int x = 0;
    for (LocalDateTime day = startTime; !day.isAfter(endTime);
        day = day.plus(amount)) {

      points.add(new Pair<>(x, f.run(day, next)));
      next = next.plus(amount);
      x++;
    }
    return points;
  }

  /**
   * Generates a histogram plot points.
   *
   * @param startTime The start time for the histogram.
   * @param endTime   The end time for the histogram.
   * @param unit      The unit of the range.
   * @param f         The function to apply.
   * @return Returns a list of pairs, where the first item is a string of the range and the second
   * is the point height to plot.
   */
  private List<Pair<String, Double>> genHistogramPoints(LocalDateTime startTime, LocalDateTime endTime,
      Granularity unit, Function f) {
    ArrayList<Pair<String, Double>> points = new ArrayList<>();
    TemporalAmount amount = unit.generateStep();
    LocalDateTime next = startTime.plus(amount);
    for (LocalDateTime day = startTime; !day.isAfter(endTime);
        day = day.plus(amount)) {

      points.add(new Pair<>(stringify(day, next, unit), f.run(day, next)));
      next = next.plus(amount);
    }
    return points;
  }


  /**
   *  Generates a string that matches the input dates.
   * @param start The start of the date range.
   * @param end The end of the date range.
   * @param unit The granularity to tune the string to.
   * @return Returns a string representing the range, tuned to the granularity.
   */
  public String stringify(LocalDateTime start, LocalDateTime end, Granularity unit) {
    DateTimeFormatter dateTimeFormatter;
    String range;
    switch (unit) {
      case DAY, WEEK -> {
        if (start.getMonth() == end.getMonth() && start.getYear()==end.getYear()) {
          dateTimeFormatter = DateTimeFormatter.ofPattern("dd");
        } else if (start.getMonth()==end.getMonth()) {
          dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        } else {
          dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM");
        }
        range = start.format(dateTimeFormatter) + " - " + end.format(dateTimeFormatter);

      }
      case MONTH -> {
        if (start.getYear() == end.getYear()) {
          dateTimeFormatter = DateTimeFormatter.ofPattern("MM");
        } else {
          dateTimeFormatter = DateTimeFormatter.ofPattern("MM/yyyy");
        }
        range = start.format(dateTimeFormatter) + " - " + end.format(dateTimeFormatter);
      }
      case YEAR -> {
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy");
        range = start.format(dateTimeFormatter) + " - " + end.format(dateTimeFormatter);
      }
      default -> {
        dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        range = start.format(dateTimeFormatter) + " - " + end.format(dateTimeFormatter);
      }
    }

    System.out.println("Range: " + range);
    return range;
  }

  public LocalDateTime earliestDate() {
    return impressions.get(0).getDate();

  }

  public LocalDateTime latestDate() {
    return impressions.get(impressions.size() - 1).getDate();
  }

//TODO: reset stats on changes to filter.

  /**
   * Gets all the stats for the list view, only recalculating if there's been a change to any
   * parameters.
   *
   * @param start The start of the time range.
   * @param end   The end of the time range.
   * @return Returns an array of impressionCost, impressions, clicks, uniques, bounces, conversions,
   * cost, through, acquisitionCosts, clickCosts, thousand, bounceRate.
   */
  public double[] allStats(LocalDateTime start, LocalDateTime end) {
    setEfficiency(false);
    if (stats.length != 0) {
      return stats;
    }
    filteringEnabled(false);
    double impressionCost = calcImpressionCost(start, end);

    double impressions = totalImpressions(start, end);
    double clicks = totalClicks(start, end);
    double uniques = calcUniqueUsersClick(start, end);
    double bounces = calcBounces(start, end);
    double conversions = calcNumConversions(start, end);
    double cost = calcTotalCost(start, end);
    System.out.println("half way");
    double through = calcClickThrough(start, end, impressions);
    double acquisitionCosts = calcCostAcquisition(start, end, cost);
    double clickCosts = calcCostPerClick(start, end, cost);
    double thousand = costPerThousandImpre(start, end, impressions);
    double bounceRate = calcBounceRate(start, end);
    stats = new double[]{impressionCost, impressions, clicks, uniques, bounces, conversions, cost,
        through, acquisitionCosts, clickCosts, thousand, bounceRate};
    filteringEnabled(true);
    return stats;

  }

  /**
   * Gets the nearest impression to a datetime that matches an id using a binary search.
   *
   * @param time The time to aim for.
   * @param id   The id of the impression to match.
   * @return Returns the matching impresion.
   */
  public Impression nearestImpression(LocalDateTime time, long id) {
    if (!filteringEnabled) {
      return null;
    }
    int low = 0;
    int high = impressions.size() - 1;
    int index = Integer.MAX_VALUE;
    int mid = 0;
    while (low <= high) {
      mid = low + ((high - low) / 2);
      if (mid < 0 || mid >= impressions.size()) {
        break;
      }

      if (impressions.get(mid).getDate().isBefore(time)) {
        //if the truncate to day is the same then
        low = mid + 1;
      } else if (impressions.get(mid).getDate().isAfter(time)) {
        high = mid - 1;
      } else if (impressions.get(mid).getDate().equals(time)) {
        index = mid;
        break;
      }
    }
    while (impressions.get(mid).getDate().isAfter(time) && impressions.get(mid).getId() != id
        && mid > 0) {
      mid -= 1;
    }
    return impressions.get(mid);
  }


}
