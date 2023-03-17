package uk.ac.soton.adDashboard.controller;

import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import uk.ac.soton.adDashboard.Interfaces.FilterWindow;
import uk.ac.soton.adDashboard.Interfaces.GraphFeatures;
import uk.ac.soton.adDashboard.components.FilterSet;
import uk.ac.soton.adDashboard.enums.Granularity;
import uk.ac.soton.adDashboard.enums.Stat;
import uk.ac.soton.adDashboard.filter.Filter;
import uk.ac.soton.adDashboard.records.DataSet;


/**
 * The overarching Controller of the system.
 */
public class Controller {

  private GraphFeatures graph;
  private DataSet model;

  private FilterWindow filterWindow;

  private Stat statType;

  private Granularity granularity = Granularity.DAY;


  public GraphFeatures getGraph() {
    return graph;
  }


  public void setGraph(GraphFeatures graph) {
    this.graph = graph;
  }

  public DataSet getModel() {
    return model;
  }

  public void setModel(DataSet model) {
    this.model = model;
  }



  public Stat getStatType() {
    return statType;
  }

  /**
   * Sets the stat type and re-plots all the filters with the new stat.
   * @param statType The new stat to display on the graph.
   */
  public void setStatType(Stat statType) {
    this.statType = statType;
    rePlot();
  }

  public void setFilterWindow(FilterWindow filter) {
    this.filterWindow= filter;
  }

  public FilterWindow getFilterWindow() {
    return filterWindow;
  }


  public Granularity getGranularity() {
    return granularity;
  }

  /**
   * Sets the granularity of the graph and points. It triggers a recalculation of all points.
   * @param granularity The new Granularity.
   */
  public void setGranularity(Granularity granularity) {
    this.granularity = granularity;
    rePlot();
  }

  private void rePlot() {
    List<Filter> filters = filterWindow.getFilters();
    for (Filter filter : filters) {
      filterUpdated(filter);
    }
  }

  /**
   * Called to update the line for a Filter which has changed.
   * @param filter The Filter object which changed.
   */
  public void filterUpdated(Filter filter) {

    model.setFilter(filter);

    List<Pair<Integer, Double>> points = model.generateY(filter.getStartDate(),
        filter.getEndDate(), getGranularity(),statType); //todo: should the filter contain the unit?
    graph.plot(filter.getId(),points);
  }

  /**
   * Deletes the line for a given Filter.
   * @param filter The Filter to delete from the graph.
   */
  public void deleteLine(Filter filter) {
    graph.delete(filter.getId());
  }


}
