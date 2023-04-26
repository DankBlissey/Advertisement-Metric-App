package uk.ac.soton.adDashboard.Controller;


import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javafx.util.Pair;
import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import uk.ac.soton.adDashboard.Interfaces.FilterWindow;
import uk.ac.soton.adDashboard.Interfaces.GraphFeatures;
import uk.ac.soton.adDashboard.controller.Controller;
import uk.ac.soton.adDashboard.enums.Granularity;
import uk.ac.soton.adDashboard.enums.Stat;
import uk.ac.soton.adDashboard.filter.Filter;
import uk.ac.soton.adDashboard.records.Click;
import uk.ac.soton.adDashboard.records.DataSet;
import uk.ac.soton.adDashboard.records.Impression;
import uk.ac.soton.adDashboard.records.ServerAccess;
import uk.ac.soton.adDashboard.records.User;


public class ControllerTest {

  private static DataSet dataSet;
  private static double cost;
  private static Controller controller;

  List<Pair<Integer,Double>> data = new ArrayList<>();
  private static Filter filter;

  @BeforeAll
  static void setup() throws Exception {

    dataSet = new DataSet();
    dataSet.setImpressions(generateImpressions());
    dataSet.setUsers(generateUser());
    dataSet.setServerAccess(generateServerAccess());
    dataSet.setClicks(generateClicks());
    cost = 42.5;
    controller = new Controller();
    controller.addModel(dataSet);
    filter = defaultFilter();




  }



  // Resets the DataSet object after each test case is executed
  @BeforeEach
  void beforeEach() {
    dataSet.setInterval(30);
    dataSet.setPagesForBounce(2);
    dataSet.setPagesViewedBounceMetric(true);
    controller= new Controller();
    controller.addModel(dataSet);
    data.clear();
    Collections.addAll(data,new Pair<>(0,0.0),new Pair<>(1,0.0));

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

  public static Filter defaultFilter() {

      var defaultFilter = new Filter();
      defaultFilter.setStartDate(controller.getModel().earliestDate());
      defaultFilter.setEndDate(controller.getModel().latestDate());
      defaultFilter.setId(0);
      return defaultFilter;

  }

  /**
   * Tests that the graph is cleared and plotted when the filter is updated (and only 1 time).
   */
  @Test
  void graphClearedAndPlotted() {


    GraphFeatures graph = niceMock(GraphFeatures.class);

    graph.delete(0);
    graph.plot(0,data);
    expectLastCall().times(1);

    replay(graph);

    controller.setGraph(graph);
    Filter filter= defaultFilter();
    filter.setStartDate(LocalDateTime.now());
    filter.setEndDate( LocalDateTime.now().plusDays(1));
    controller.filterUpdated(filter);
    verify(graph);
  }

  /**
   * Tests that the plot filter matched that of the filter passed to the controller.
   * @param id The id of the filter.
   */
  @ParameterizedTest(name= "value {0}")
  @ValueSource(ints = {0,1,2,3,4,5,6,7,8,9,10})
  void filterMatches(int id) {

    filter = defaultFilter();
    filter.setStartDate(LocalDateTime.now());
    filter.setEndDate( LocalDateTime.now().plusDays(1));
    filter.setId(id);
    GraphFeatures graph = niceMock(GraphFeatures.class);
    graph.delete(id);

    graph.plot(id,data);
    replay(graph);
    controller.setGraph(graph);


    controller.filterUpdated(filter);
    verify(graph);
  }

  /**
   * Tests that deletion actually calls deletion on the correct series of the graph.
   * @param id The id of the filter to delete
   */
  @ParameterizedTest(name= "value {0}")
  @ValueSource(ints = {0,1,2,3,4,5,6,7,8,9,10})
  void filterDeleted(int id) {
    filter.setStartDate(LocalDateTime.now());
    filter.setEndDate( LocalDateTime.now().plusDays(1));
    filter.setId(id);
    GraphFeatures graph = niceMock(GraphFeatures.class);
    graph.delete(id);
    replay(graph);
    controller.setGraph(graph);
    controller.deleteLine(filter);
    verify(graph);
  }

  /**
   * Tests granularity change causes correct call in data set and graph.
   * @param granularity The granularity to change to.
   */
  @ParameterizedTest(name= "value {0}")
  @EnumSource(value = Granularity.class, names = {"DAY","WEEK","MONTH","YEAR"})
  void granularityChange(Granularity granularity) {

    var f1 = new Filter();
    f1.setId(0);
    f1.setDataSetId(controller.getLatestId());
    f1.setStartDate(LocalDateTime.now());
    f1.setEndDate(LocalDateTime.now().plusDays(1));
    var f2 = new Filter();
    f2.setId(1);
    f2.setDataSetId(controller.getLatestId());
    f2.setStartDate(LocalDateTime.now());
    f2.setEndDate(LocalDateTime.now().plusDays(1));
    controller.setFilterWindow(new FilterStub(List.of(f1,f2)));
    GraphFeatures graph = niceMock(GraphFeatures.class);
    var d1 = dataSet.generateY(f1.getStartDate(),f1.getEndDate(),granularity,controller.getStatType());
    graph.plot(0,d1);
    var d2 = dataSet.generateY(f2.getStartDate(),f2.getEndDate(),granularity,controller.getStatType());
    graph.plot(1,d2);
    replay(graph);
    controller.setGraph(graph);

    DataSet dataSet1 = niceMock(DataSet.class);
    expect(dataSet1.generateY(f1.getStartDate(),f1.getEndDate(),granularity,controller.getStatType())).andReturn(d1);
    expect(dataSet1.generateY(f2.getStartDate(),f2.getEndDate(),granularity,controller.getStatType())).andReturn(d2);
    replay(dataSet1);
    controller.addModel(dataSet1);
    f1.setDataSetId(controller.getLatestId());
    f2.setDataSetId(controller.getLatestId());
    controller.setGranularity(granularity);
    verify(graph);
    verify(dataSet1);


  }





  /**
   * Tests stat change causes correct call in data set and graph.
   * @param stat The stat to change to.
   */
  @ParameterizedTest(name= "value {0}")
  @EnumSource(value = Stat.class, names = { "totalImpressions",  "totalClicks","totalUniques","totalBounces","totalConversions","totalCost","CTR","CPA","CPC","CPM","bounceRate"})
  void statChange(Stat stat) {

    var f1 = new Filter();
    f1.setId(0);
    f1.setStartDate(LocalDateTime.now());
    f1.setEndDate(LocalDateTime.now().plusDays(1));
    f1.setDataSetId(controller.getLatestId());
    var f2 = new Filter();
    f2.setId(1);
    f2.setStartDate(LocalDateTime.now());
    f2.setEndDate(LocalDateTime.now().plusDays(1));
    f2.setDataSetId(controller.getLatestId());
    controller.setFilterWindow(new FilterStub(List.of(f1,f2)));
    GraphFeatures graph = niceMock(GraphFeatures.class);
    var d1 = dataSet.generateY(f1.getStartDate(),f1.getEndDate(),Granularity.DAY,stat);
    graph.plot(0,d1);
    var d2 = dataSet.generateY(f2.getStartDate(),f2.getEndDate(),Granularity.DAY,stat);
    graph.plot(1,d2);
    replay(graph);
    controller.setGraph(graph);

    DataSet dataSet1 = niceMock(DataSet.class);
    expect(dataSet1.generateY(f1.getStartDate(),f1.getEndDate(),Granularity.DAY,stat)).andReturn(d1);
    expect(dataSet1.generateY(f2.getStartDate(),f2.getEndDate(),Granularity.DAY,stat)).andReturn(d2);
    replay(dataSet1);
    controller.addModel(dataSet1);
    f1.setDataSetId(controller.getLatestId());
    f2.setDataSetId(controller.getLatestId());
    controller.setStatType(stat);
    verify(graph);
    verify(dataSet1);


  }




  private class FilterStub implements FilterWindow {
    List<Filter> filters;
    public FilterStub(List<Filter> filters) {
      this.filters=filters;
    }
    @Override
    public List<Filter> getFilters() {
      return filters;
    }
  }
}
