package uk.ac.soton.adDashboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

  @BeforeAll
  static void setup() throws Exception {

    dataSet = new DataSet();
    dataSet.setImpressions(generateImpressions());
    dataSet.setUsers(generateUser());
    dataSet.setServerAccess(generateServerAccess());
    dataSet.setClicks(generateClicks());
    cost = 42.5;
  }

  @AfterEach
  void afterEach() {
    dataSet.setInterval(30);
    dataSet.setPagesForBounce(2);
    dataSet.setPagesViewedBounceMetric(true);
  }


  static ArrayList<Impression> generateImpressions() throws Exception {
    var impressions = new ArrayList<Impression>();
    impressions.add(new Impression("2015-01-01 23:00:02", "25", "12.5", "Shopping"));
    impressions.add(new Impression("2015-02-01 23:05:00", "26", "10", "Shopping"));
    impressions.add(new Impression("2015-02-03 23:10:00", "27", "15", "Blog"));
    impressions.add(new Impression("2015-05-01 23:05:00", "26", "0", "Shopping"));
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
    access.add(new ServerAccess("2015-01-01 23:05:03", "26", "2015-01-01 23:05:08", "1", "Yes"));
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

  //11 main things to test


  @Test
  void calcTotalCost() {
    assertEquals(cost, dataSet.calcTotalCost(dataSet.earliestDate(), dataSet.latestDate()));

  }

  @Test
  void totalClicks() {
    assertEquals(3, dataSet.totalClicks(dataSet.earliestDate(), dataSet.latestDate()));

  }

  @Test
  void totalImpressions() {
    assertEquals(4, dataSet.totalImpressions(dataSet.earliestDate(), dataSet.latestDate()));
  }

  @Test
  void calcClickThrough() {
    //clicks per impression
    assertEquals(3 / 4.0, dataSet.calcClickThrough(dataSet.earliestDate(), dataSet.latestDate()));
  }

  @Test
  void calcNumConversions() {
    assertEquals(2, dataSet.calcNumConversions(dataSet.earliestDate(), dataSet.latestDate()));
  }

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

  @Test
  void calcBounces() {
    dataSet.setPagesForBounce(2);
    assertEquals(2, dataSet.calcBounces(dataSet.earliestDate(), dataSet.latestDate()));
  }

  @Test
  void calcBounceRate() {
    //number of bounces per click
    dataSet.setPagesForBounce(2);
    assertEquals(2.0 / 3, dataSet.calcBounceRate(dataSet.earliestDate(), dataSet.latestDate()));
    //test the affects of changing the bounce rate measurement
  }

  @Test
  void calcCostAcquisition() {
    assertEquals(cost / 2,
        dataSet.calcCostAcquisition(dataSet.earliestDate(), dataSet.latestDate()));
  }

  @Test
  void calcCostPerClick() {
    assertEquals(cost / 3, dataSet.calcCostPerClick(dataSet.earliestDate(), dataSet.latestDate()));
  }

  @Test
  void calcUniqueUsersClick() {
    assertEquals(2, dataSet.calcUniqueUsersClick(dataSet.earliestDate(), dataSet.latestDate()));
  }

  @Test
  void costPerThousandImpre() {
    double value = Math.round(
        dataSet.costPerThousandImpre(dataSet.earliestDate(), dataSet.latestDate()));
    assertEquals(cost / (4.0 / 1000), value);
  }


}
