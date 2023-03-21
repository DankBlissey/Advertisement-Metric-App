package uk.ac.soton.adDashboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.util.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.ac.soton.adDashboard.enums.*;
import uk.ac.soton.adDashboard.filter.Filter;
import uk.ac.soton.adDashboard.records.Click;
import uk.ac.soton.adDashboard.records.DataSet;
import uk.ac.soton.adDashboard.records.Impression;
import uk.ac.soton.adDashboard.records.ServerAccess;
import uk.ac.soton.adDashboard.records.User;


public class FilterSetTest {

    private static DataSet dataSet;
    private static double cost;
    private static Filter filter;


    // Creates sample DataSet for testing
    @BeforeAll
    static void setup() throws Exception {

        dataSet = new DataSet();
        dataSet.setImpressions(DataSetTest.generateImpressions());
        dataSet.setUsers(DataSetTest.generateUser());
        dataSet.setServerAccess(DataSetTest.generateServerAccess());
        dataSet.setClicks(DataSetTest.generateClicks());
        cost = 42.5;

    }

    // Clear data in sample dataset
    @AfterEach
    void afterEach() {
        dataSet.setInterval(30);
        dataSet.setPagesForBounce(2);
        dataSet.setPagesViewedBounceMetric(true);

        filter.setAge("Any");
        filter.setContext(Context.ANY);
        filter.setIncome(Income.ANY);
        filter.setGender(Gender.ANY);
    }

    // Creates sample FilterSet
    void newFilterSet() {
        filter = new Filter();
        filter.setAge("Any");
        filter.setContext(Context.Shopping);
        filter.setIncome(Income.ANY);
        filter.setGender(Gender.FEMALE);
        filter.setStartDate(LocalDateTime.of(2015,2,1,0,0,0));
        filter.setEndDate(LocalDateTime.of(2015,2,3,0,0,0));
    }

    // Test if filters are working
    @Test
    void filterSetTest() {
        newFilterSet();
        dataSet.setFilter(filter);

        // Total Impressions
        var points = new ArrayList<Pair<Integer, Double>>();
        points.add(new Pair<>(0, 1.0));
        points.add(new Pair<>(1, 0.0));
        points.add(new Pair<>(2, 0.0));
        assertEquals(points, dataSet.generateY(filter.getStartDate(), filter.getEndDate(), Granularity.DAY, Stat.totalImpressions));

        // Total Cost
        var points2 = new ArrayList<Pair<Integer, Double>>();
        points2.add(new Pair<>(0, 12.5));
        points2.add(new Pair<>(1, 0.0));
        points2.add(new Pair<>(2, 0.0));
        assertEquals(points2, dataSet.generateY(filter.getStartDate(), filter.getEndDate(), Granularity.DAY, Stat.totalCost));
    }

    // Date range test
    @Test
    void dateRangeFilter() {
        newFilterSet();
        filter.setContext(Context.ANY);
        dataSet.setFilter(filter);

        var points = new ArrayList<Pair<Integer, Double>>();
        points.add(new Pair<>(0, 1.0));
        points.add(new Pair<>(1, 0.0));
        points.add(new Pair<>(2, 0.0));
        assertEquals(points, dataSet.generateY(filter.getStartDate(), filter.getEndDate(), Granularity.DAY, Stat.totalClicks));

        // Date range change
        var points5 = new ArrayList<Pair<Integer, Double>>();
        filter.setStartDate(LocalDateTime.of(2015,4,28,12,0,0));
        filter.setEndDate(LocalDateTime.of(2015,5,2,12,0,0));
        points5.add(new Pair<>(0, 0.0));
        points5.add(new Pair<>(1, 0.0));
        points5.add(new Pair<>(2, 0.0));
        points5.add(new Pair<>(3, 1.0));
        points5.add(new Pair<>(4, 0.0));
        assertEquals(points5, dataSet.generateY(filter.getStartDate(), filter.getEndDate(), Granularity.DAY, Stat.totalClicks));
    }

    @Test
    void GenderFilter() {
        newFilterSet();
        dataSet.setFilter(filter);

        // Gender filter change
        var points = new ArrayList<Pair<Integer, Double>>();
        filter.setGender(Gender.MALE);
        points.add(new Pair<>(0, 0.0));
        points.add(new Pair<>(1, 0.0));
        points.add(new Pair<>(2, 0.0));
        assertEquals(points, dataSet.generateY(filter.getStartDate(), filter.getEndDate(), Granularity.DAY, Stat.totalImpressions));

        // Gender filter change for Cost
        var points2 = new ArrayList<Pair<Integer, Double>>();
        filter.setGender(Gender.ANY);
        filter.setContext(Context.ANY);
        points2.add(new Pair<>(0, 12.5));
        points2.add(new Pair<>(1, 0.0));
        points2.add(new Pair<>(2, 15.0));
        assertEquals(points2, dataSet.generateY(filter.getStartDate(), filter.getEndDate(), Granularity.DAY, Stat.totalCost));

    }

    @Test
    void AgeFilter() {
        newFilterSet();
        dataSet.setFilter(filter);

        // Age filter change
        var points = new ArrayList<Pair<Integer, Double>>();
        filter.setGender(Gender.ANY);
        filter.setIncome(Income.ANY);
        filter.setContext(Context.ANY);
        filter.setAge("25-34");
        points.add(new Pair<>(0, 1.0));
        points.add(new Pair<>(1, 0.0));
        points.add(new Pair<>(2, 1.0));
        assertEquals(points, dataSet.generateY(filter.getStartDate(), filter.getEndDate(), Granularity.DAY, Stat.totalImpressions));

        // Age filter change for Cost
        var points2 = new ArrayList<Pair<Integer, Double>>();
        filter.setAge("Any");
        points2.add(new Pair<>(0, 12.5));
        points2.add(new Pair<>(1, 0.0));
        points2.add(new Pair<>(2, 15.0));
        assertEquals(points2, dataSet.generateY(filter.getStartDate(), filter.getEndDate(), Granularity.DAY, Stat.totalCost));
    }

    @Test
    void IncomeFilter() {
        newFilterSet();
        dataSet.setFilter(filter);

        // Income filter change
        var points = new ArrayList<Pair<Integer, Double>>();
        filter.setIncome(Income.HIGH);
        points.add(new Pair<>(0, 1.0));
        points.add(new Pair<>(1, 0.0));
        points.add(new Pair<>(2, 0.0));
        assertEquals(points, dataSet.generateY(filter.getStartDate(), filter.getEndDate(), Granularity.DAY, Stat.totalImpressions));

        // Income filter change for Cost
        var points2 = new ArrayList<Pair<Integer, Double>>();
        filter.setContext(Context.ANY);
        filter.setIncome(Income.MEDIUM);
        points2.add(new Pair<>(0, 0.0));
        points2.add(new Pair<>(1, 0.0));
        points2.add(new Pair<>(2, 15.0));
        assertEquals(points2, dataSet.generateY(filter.getStartDate(), filter.getEndDate(), Granularity.DAY, Stat.totalCost));

    }

    @Test
    void ContextFilter() {
        newFilterSet();
        dataSet.setFilter(filter);

        // Context filter change for Impressions
        var points = new ArrayList<Pair<Integer, Double>>();
        filter.setContext(Context.ANY);
        points.add(new Pair<>(0, 1.0));
        points.add(new Pair<>(1, 0.0));
        points.add(new Pair<>(2, 1.0));
        assertEquals(points, dataSet.generateY(filter.getStartDate(), filter.getEndDate(), Granularity.DAY, Stat.totalImpressions));

        // Context filter change for Cost
        var points2 = new ArrayList<Pair<Integer, Double>>();
        filter.setContext(Context.ANY);
        points2.add(new Pair<>(0, 12.5));
        points2.add(new Pair<>(1, 0.0));
        points2.add(new Pair<>(2, 15.0));
        assertEquals(points2, dataSet.generateY(filter.getStartDate(), filter.getEndDate(), Granularity.DAY, Stat.totalCost));


    }

}
