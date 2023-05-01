package uk.ac.soton.adDashboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.ac.soton.adDashboard.records.Click;
import uk.ac.soton.adDashboard.records.DataSet;
import uk.ac.soton.adDashboard.records.Impression;
import uk.ac.soton.adDashboard.records.ServerAccess;
import uk.ac.soton.adDashboard.records.User;

public class DataSetTest {

  private static DataSet dataSet;
  private static double cost;

  // Creates sample DataSet for testing
  @BeforeAll
  static void setup() throws Exception {

    dataSet = new DataSet();
    dataSet.setImpressions(generateImpressions());
    dataSet.setUsers(generateUser());
    dataSet.setServerAccess(generateServerAccess());
    dataSet.setClicks(generateClicks());
    cost = 42.5;
    dataSet.setPagesViewedBounceMetric(true);
    dataSet.filteringEnabled(false);
  }

  // Resets the DataSet object after each test case is executed
  @AfterEach
  void afterEach() {
    dataSet.setInterval(30);
    dataSet.setPagesForBounce(2);
    dataSet.setPagesViewedBounceMetric(true);
    dataSet.filteringEnabled(false);
  }

  //  Creates sample data for the DataSet

  static ArrayList<Impression> generateImpressions() throws Exception {
    var impressions = new ArrayList<Impression>();
    impressions.add(new Impression("2015-01-01 23:00:02", "25", "12.5", "Shopping"));
    impressions.add(new Impression("2015-02-01 23:05:00", "26", "10", "Shopping"));
    impressions.add(new Impression("2015-02-03 23:10:00", "27", "15", "Blog"));
    impressions.add(new Impression("2015-05-01 23:05:00", "26", "0", "Hobbies"));
    return impressions;
  }

  static HashMap<Long, User> generateUser() throws Exception {
    var users = new HashMap<Long, User>();
    users.put(25L, new User("25", "<25", "Male", "Low"));
    users.put(26L, new User("26", "25-34", "Female", "High"));
    users.put(27L, new User("27", "25-34", "Female", "Medium"));
    return users;
  }

  static ArrayList<ServerAccess> generateServerAccess() throws Exception {
    var access = new ArrayList<ServerAccess>();
    access.add(new ServerAccess("2015-01-01 23:01:02", "25", "2015-01-01 23:02:02", "2", "Yes"));
    access.add(new ServerAccess("2015-02-01 23:05:03", "26", "2015-01-01 23:05:08", "1", "Yes"));
    access.add(new ServerAccess("2015-05-01 23:05:00", "26", "2015-05-01 23:05:50", "1", "No"));
    return access;
  }

  static ArrayList<Click> generateClicks() throws Exception {
    var clicks = new ArrayList<Click>();
    clicks.add(new Click("2015-01-01 23:01:02", "25", "2.5"));
    clicks.add(new Click("2015-02-01 23:05:03", "26", "2.5"));
    clicks.add(new Click("2015-05-01 23:05:00", "26", "0"));
    return clicks;
  }

  // Test of all metrics

  // Number of Impressions
  @Test
  void totalImpressions() {
    assertEquals(4, dataSet.totalImpressions(dataSet.earliestDate(), dataSet.latestDate()));


  }

  @Test
  void totalImpressionsConsecutive() { //found bugs in values on the date boundaries being counted in two days.
    dataSet.setEfficiency(true);
    dataSet.resetAllAccess();
    assertEquals(2, dataSet.totalImpressions(LocalDateTime.of(2014, 2, 1, 1, 1),
        LocalDateTime.of(2015, 2, 1, 23, 5)));
    assertEquals(1, dataSet.totalImpressions(LocalDateTime.of(2015, 2, 1, 23, 5),
        LocalDateTime.of(2015, 2, 3, 23, 11)));
    dataSet.setEfficiency(false);
    dataSet.resetAllAccess();
  }

  // Number of Clicks
  @Test
  void totalClicks() {
    assertEquals(3, dataSet.totalClicks(dataSet.earliestDate(), dataSet.latestDate()));

  }

  @Test
  void totalClicksConsecutive() {
    dataSet.setEfficiency(true);
    dataSet.resetAllAccess();
    assertEquals(1, dataSet.totalClicks(LocalDateTime.of(2014, 2, 1, 1, 1),
        LocalDateTime.of(2015, 1, 1, 23, 1,2)));
    assertEquals(2, dataSet.totalClicks(LocalDateTime.of(2015, 1, 1, 23, 1,2),
        LocalDateTime.now()));
    dataSet.setEfficiency(false);
    dataSet.resetAllAccess();
  }

  // Number of Uniques
  @Test
  void calcUniqueUsersClick() {
    assertEquals(2, dataSet.calcUniqueUsersClick(dataSet.earliestDate(), dataSet.latestDate()));
  }
  @Test
  void totalUniqueUsersConsecutive() {
    dataSet.setEfficiency(true);
    dataSet.resetAllAccess();
    assertEquals(1, dataSet.calcUniqueUsersClick(LocalDateTime.of(2014, 2, 1, 1, 1),
        LocalDateTime.of(2015, 1, 1, 23, 1,2)));
    assertEquals(1, dataSet.calcUniqueUsersClick(LocalDateTime.of(2015, 1, 1, 23, 1,2),
        LocalDateTime.now()));
    dataSet.setEfficiency(false);
    dataSet.resetAllAccess();
  }


  // Bounce
  @Test
  void isBounce() {
    dataSet.setPagesViewedBounceMetric(true);
    dataSet.setPagesForBounce(5);
    assertTrue(dataSet.isBounce(dataSet.getServerAccess().get(0)));
    dataSet.setPagesForBounce(2);
    assertFalse(dataSet.isBounce(dataSet.getServerAccess().get(0)));
    dataSet.setPagesViewedBounceMetric(false);
    dataSet.setInterval(1);
    assertFalse(dataSet.isBounce(dataSet.getServerAccess().get(0)));
    dataSet.setInterval(60);
    assertFalse(dataSet.isBounce(dataSet.getServerAccess().get(0)));
    dataSet.setInterval(61);
    assertTrue(dataSet.isBounce(dataSet.getServerAccess().get(0)));
    dataSet.setPagesViewedBounceMetric(true);

  }

  // Number of Bounces (Number of Pages)
  @Test
  void calcBounces() {
    dataSet.setPagesForBounce(2);
    dataSet.setPagesViewedBounceMetric(true);
    assertEquals(2, dataSet.calcBounces(dataSet.earliestDate(), dataSet.latestDate()));
  }

  @Test
  void bounceConsecutive() {
    //default pages and 2
    dataSet.setEfficiency(true);
    dataSet.resetAllAccess();
    assertEquals(0, dataSet.calcBounces(LocalDateTime.of(2014, 2, 1, 1, 1),
        LocalDateTime.of(2015, 1, 1, 23, 1,2)));
    assertEquals(2, dataSet.calcBounces(LocalDateTime.of(2015, 1, 1, 23, 1,2),
        LocalDateTime.now()));
    dataSet.setEfficiency(false);
    dataSet.resetAllAccess();
  }

  // Number of Bounces (Time Interval)
  @Test
  void calcBouncesTime() {
    dataSet.setPagesViewedBounceMetric(false);
    dataSet.setInterval(120);
    assertEquals(3, dataSet.calcBounces(dataSet.earliestDate(), dataSet.latestDate()));
  }

  // Number of Conversions
  @Test
  void calcNumConversions() {
    assertEquals(2, dataSet.calcNumConversions(dataSet.earliestDate(), dataSet.latestDate()));
  }

  @Test
  void conversionsConsecutive() {
    dataSet.setEfficiency(true);
    dataSet.resetAllAccess();
    assertEquals(1, dataSet.calcNumConversions(LocalDateTime.of(2014, 2, 1, 1, 1),
        LocalDateTime.of(2015, 1, 1, 23, 1,2)));
    assertEquals(1, dataSet.calcNumConversions(LocalDateTime.of(2015, 1, 1, 23, 1,2),
        LocalDateTime.now()));
    dataSet.setEfficiency(false);
    dataSet.resetAllAccess();
  }

  // Total Cost
  @Test
  void calcTotalCost() {
    assertEquals(cost, dataSet.calcTotalCost(dataSet.earliestDate(), dataSet.latestDate()));

  }


  @Test
  void totalCostConsecutive() {
    dataSet.setEfficiency(true);
    dataSet.resetAllAccess();
    assertEquals(15, dataSet.calcTotalCost(LocalDateTime.of(2014, 2, 1, 1, 1),
        LocalDateTime.of(2015, 1, 1, 23, 1,2)));
    assertEquals(cost-15, dataSet.calcTotalCost(LocalDateTime.of(2015, 1, 1, 23, 1,2),
        LocalDateTime.now()));
    dataSet.setEfficiency(false);
    dataSet.resetAllAccess();
  }


  // CTR
  @Test
  void calcClickThrough() {
    assertEquals(3 / 4.0, dataSet.calcClickThrough(dataSet.earliestDate(), dataSet.latestDate()));
  }



  @Test
  void clickThroughConsecutive() { //clicks per impression
    dataSet.setEfficiency(true);
    dataSet.resetAllAccess();
    assertEquals(1, dataSet.calcClickThrough(LocalDateTime.of(2014, 2, 1, 1, 1),
        LocalDateTime.of(2015, 1, 1, 23, 1,2)));
    assertEquals(2/3.0, dataSet.calcClickThrough(LocalDateTime.of(2015, 1, 1, 23, 1,2),
        LocalDateTime.now()));
    dataSet.setEfficiency(false);
    dataSet.resetAllAccess();
  }

  // CPA
  @Test
  void calcCostAcquisition() {
    assertEquals(cost / 2,
        dataSet.calcCostAcquisition(dataSet.earliestDate(), dataSet.latestDate()));
  }

  @Test
  void cpaConsecutive() { //cost per conversion    //todo: recheck
    dataSet.setEfficiency(true);
    dataSet.resetAllAccess();
    assertEquals(15/1, dataSet.calcCostAcquisition(LocalDateTime.of(2014, 2, 1, 1, 1),
        LocalDateTime.of(2015, 1, 1, 23, 1,2)));
    assertEquals((42.5-15)/1, dataSet.calcCostAcquisition(LocalDateTime.of(2015, 1, 1, 23, 1,2),
        LocalDateTime.now()));
    dataSet.setEfficiency(false);
    dataSet.resetAllAccess();
  }

  // CPC
  @Test
  void calcCostPerClick() {
    assertEquals(cost / 3, dataSet.calcCostPerClick(dataSet.earliestDate(), dataSet.latestDate()));
  }

  @Test
  void cpcConsecutive() { //found bugs
    dataSet.setEfficiency(true);
    dataSet.resetAllAccess();
    assertEquals(15, dataSet.calcCostPerClick(LocalDateTime.of(2014, 2, 1, 1, 1),
        LocalDateTime.of(2015, 1, 1, 23, 1,2)));
    assertEquals((42.5-15)/2, dataSet.calcCostPerClick(LocalDateTime.of(2015, 1, 1, 23, 1,2),
        LocalDateTime.now()));
    dataSet.setEfficiency(false);
    dataSet.resetAllAccess();
  }

  // CPM
  @Test
  void costPerThousandImpre() {
    double value = Math.round(
        dataSet.costPerThousandImpre(dataSet.earliestDate(), dataSet.latestDate()));
    assertEquals(cost / (4.0 / 1000), value);
  }

  @Test
  void cpmConsecutive() {
    dataSet.setEfficiency(true);
    dataSet.resetAllAccess();
    assertEquals(15/(1/1000.0), dataSet.costPerThousandImpre(LocalDateTime.of(2014, 2, 1, 1, 1),
        LocalDateTime.of(2015, 1, 1, 23, 1,2)));
    assertEquals(27.5/(3/1000.0), dataSet.costPerThousandImpre(LocalDateTime.of(2015, 1, 1, 23, 1,2),
        LocalDateTime.now()));
    dataSet.setEfficiency(false);
    dataSet.resetAllAccess();
  }

  // Bounce Rate
  @Test
  void calcBounceRate() {
    //number of bounces per click
    dataSet.setPagesForBounce(2);
    assertEquals(2.0 / 3, dataSet.calcBounceRate(dataSet.earliestDate(), dataSet.latestDate()));
    //test the effects of changing the bounce rate measurement
  }

  @Test
  void bounceRateConsecutive() { //bounces per click
    dataSet.setEfficiency(true);
    dataSet.resetAllAccess();
    dataSet.setPagesForBounce(2);
    dataSet.setPagesViewedBounceMetric(true);
    assertEquals(0, dataSet.calcBounceRate(LocalDateTime.of(2014, 2, 1, 1, 1),
        LocalDateTime.of(2015, 1, 1, 23, 1,2)));
    assertEquals(2/2, dataSet.calcBounceRate(LocalDateTime.of(2015, 1, 1, 23, 1,2),
        LocalDateTime.now()));
    dataSet.setEfficiency(false);
    dataSet.resetAllAccess();
  }


  @Test
  void nearestImpression() {
    dataSet.filteringEnabled(true);
    Impression i = dataSet.nearestImpression(dataSet.getServerAccess().get(0).getStartDate(),dataSet.getServerAccess().get(0).getId());
    assertEquals(25,i.getId());
    assertEquals(dataSet.getImpressions().get(0),i);
    dataSet.filteringEnabled(false);
  }

  /**
   * Checks how nearestImpression handles impressions which are before the server access between the impression with the correct id.
   * @throws Exception Exception if data is incorrect.
   */
  @Test
  void nearestImpressionInbetween() throws Exception { //found errors
    DataSet dataSet = new DataSet();
    dataSet.setImpressions(generateImpressions2());
    dataSet.setUsers(generateUser2());
    dataSet.setServerAccess(generateServerAccess2());
    dataSet.setClicks(generateClicks2());

    dataSet.setPagesViewedBounceMetric(true);
    dataSet.filteringEnabled(false);

    dataSet.filteringEnabled(true);
    Impression i = dataSet.nearestImpression(dataSet.getServerAccess().get(0).getStartDate(),dataSet.getServerAccess().get(0).getId());
    assertEquals(25,i.getId());
    assertEquals(dataSet.getImpressions().get(0),i);
    dataSet.filteringEnabled(false);

  }

  @Test
  void nearestImpressionJustBefore() throws Exception {
    DataSet dataSet = new DataSet();
    dataSet.setImpressions(generateImpressions3());
    dataSet.setUsers(generateUser2());
    dataSet.setServerAccess(generateServerAccess2());
    dataSet.setClicks(generateClicks2());

    dataSet.setPagesViewedBounceMetric(true);
    dataSet.filteringEnabled(false);

    dataSet.filteringEnabled(true);
    Impression i = dataSet.nearestImpression(dataSet.getServerAccess().get(0).getStartDate(),dataSet.getServerAccess().get(0).getId());
    assertEquals(25,i.getId());
    assertEquals(dataSet.getImpressions().get(2),i);
    dataSet.filteringEnabled(false);
  }



  static ArrayList<Impression> generateImpressions2() throws Exception {
    var impressions = new ArrayList<Impression>();
    impressions.add(new Impression("2015-01-01 23:00:02", "25", "12.5", "Shopping"));

    impressions.add(new Impression("2015-01-01 23:00:02", "30", "12.5", "Shopping"));

    impressions.add(new Impression("2015-02-01 23:05:00", "26", "10", "Shopping"));
    impressions.add(new Impression("2015-02-03 23:10:00", "27", "15", "Blog"));
    impressions.add(new Impression("2015-05-01 23:05:00", "26", "0", "Hobbies"));
    return impressions;
  }

  static ArrayList<Impression> generateImpressions3() throws Exception {
    var impressions = new ArrayList<Impression>();
    impressions.add(new Impression("2015-01-01 22:00:02", "90", "12.5", "Shopping"));

    impressions.add(new Impression("2015-01-01 22:00:03", "95", "12.5", "Shopping"));

    impressions.add(new Impression("2015-01-01 23:00:02", "25", "12.5", "Shopping"));

    impressions.add(new Impression("2015-01-01 23:01:02", "29", "12.5", "Shopping"));
      //the above item is exactly the right time so will be selected. however, afterwards the previous left shifter won't move because it is not after the time.

    impressions.add(new Impression("2015-02-01 23:05:00", "26", "10", "Shopping"));
    impressions.add(new Impression("2015-01-01 23:06:02", "30", "12.5", "Shopping"));

    return impressions;
  }

  static HashMap<Long, User> generateUser2() throws Exception {
    var users = new HashMap<Long, User>();
    users.put(25L, new User("25", "<25", "Male", "Low"));
    users.put(26L, new User("26", "25-34", "Female", "High"));
    users.put(27L, new User("27", "25-34", "Female", "Medium"));
    return users;
  }

  static ArrayList<ServerAccess> generateServerAccess2() throws Exception {
    var access = new ArrayList<ServerAccess>();
    access.add(new ServerAccess("2015-01-01 23:01:02", "25", "2015-01-01 23:02:02", "2", "Yes"));
    access.add(new ServerAccess("2015-02-01 23:05:03", "26", "2015-01-01 23:05:08", "1", "Yes"));
    access.add(new ServerAccess("2015-05-01 23:05:00", "26", "2015-05-01 23:05:50", "1", "No"));
    return access;
  }
  static ArrayList<Click> generateClicks2() throws Exception {
    var clicks = new ArrayList<Click>();
    clicks.add(new Click("2015-01-01 23:01:02", "25", "2.5"));
    clicks.add(new Click("2015-02-01 23:05:03", "26", "2.5"));
    clicks.add(new Click("2015-05-01 23:05:00", "26", "0"));
    return clicks;
  }


}
