package uk.ac.soton.adDashboard.controller;


import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import javafx.util.Pair;
import uk.ac.soton.adDashboard.Interfaces.FilterWindow;
import uk.ac.soton.adDashboard.Interfaces.GraphFeatures;

import uk.ac.soton.adDashboard.enums.Granularity;
import uk.ac.soton.adDashboard.enums.Stat;
import uk.ac.soton.adDashboard.filter.Filter;
import uk.ac.soton.adDashboard.records.DataSet;


/**
 * The overarching Controller of the system.
 */
public class Controller {

  private GraphFeatures graph;
  /**
   * Tracks all of the models. The key is the id of the model.
   */
  private ObservableMap<Integer, DataSet> models = FXCollections.observableMap(new HashMap<>());

  /**
   * A counter to assign an id to a model.
   */
  private int modelId = 0;


  private FilterWindow filterWindow;

  private Stat statType = Stat.totalImpressions;

  private Granularity granularity = Granularity.DAY;


  public GraphFeatures getGraph() {
    return graph;
  }


  public void setGraph(GraphFeatures graph) {
    this.graph = graph;
  }


  /**
   * @return Returns an observable map of all the models.
   */
  public ObservableMap<Integer, DataSet> getModels() {
    return models;
  }

  public ArrayList<Integer> getModelIds() {
    ArrayList<Integer> ids = new ArrayList<Integer>();
    models.forEach((id, dataSet) -> ids.add(id));
    return ids;
  }

  /**
   * Gets the first model in the list.
   *
   * @return Returns the first model in the list.
   */
  public DataSet getModel() {
    if (models.keySet().size() > 0) {
      return models.get(getModelIds().get(0));
    } else {
      return null;
    }
  }

  /**
   * Gets a model by its id.
   *
   * @param id The id of the model to get.
   * @return Returns the model with the provided id.
   */
  public DataSet getModel(int id) {
    return models.get(id);
  }

  /**
   * Adds a model and increments the id afterwards.
   *
   * @param model The model to add.
   */
  public void addModel(DataSet model) {
    this.models.put(modelId++, model);
  }

  /**
   * removes a model.
   *
   * @param id The dataSetId of the model to remove.
   * @return
   */
  public DataSet removeModel(int id) {
//    for (Filter filter : getFilterWindow().getFilters()) {
//      if (filter.getDataSetId() == id) {
//        deleteLine(filter);
//      }
//    }
    return models.remove(id);
  }

  public int getModelId() {
    return modelId;
  }


  public Stat getStatType() {
    return statType;
  }

  /**
   * Sets the stat type and re-plots all the filters with the new stat.
   *
   * @param statType The new stat to display on the graph.
   */
  public void setStatType(Stat statType) {
    this.statType = statType;
    rePlot();
  }

  public void setFilterWindow(FilterWindow filter) {
    this.filterWindow = filter;
  }

  public FilterWindow getFilterWindow() {
    return filterWindow;
  }


  public Granularity getGranularity() {
    return granularity;
  }

  /**
   * Sets the granularity of the graph and points. It triggers a recalculation of all points.
   *
   * @param granularity The new Granularity.
   */
  public void setGranularity(Granularity granularity) {
    this.granularity = granularity;
    rePlot();
  }

  private void rePlot() {
    List<Filter> filters = filterWindow.getFilters();
    for (Filter filter : filters) {
      System.out.println("REPLOTTING");
      filterUpdated(filter);
    }
  }

  /**
   * Called to update the line for a Filter which has changed.
   *
   * @param filter The Filter object which changed.
   */
  public void filterUpdated(Filter filter) {
    System.out.println(
        "filter id " + filter.getId() + " model id" + filter.getDataSetId() + " filter itself"
            + filter);
    System.out.println(getFilterWindow().getFilters());
    DataSet model = getModels().get(filter.getDataSetId());
    model.setFilter(filter);

    List<Pair<Integer, Double>> points = model.generateY(filter.getStartDate(),
        filter.getEndDate(), getGranularity(),
        statType); //todo: should the filter contain the unit?
    graph.delete(filter.getId());
    graph.plot(filter.getId(), points);
  }

  /**
   * Deletes the line for a given Filter.
   *
   * @param filter The Filter to delete from the graph.
   */
  public void deleteLine(Filter filter) {
    graph.delete(filter.getId());
  }


}
