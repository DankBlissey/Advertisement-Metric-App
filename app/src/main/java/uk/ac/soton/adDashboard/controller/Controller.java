package uk.ac.soton.adDashboard.controller;


import java.util.*;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import javafx.util.Pair;
import uk.ac.soton.adDashboard.Interfaces.FilterWindow;
import uk.ac.soton.adDashboard.Interfaces.GraphFeatures;

import uk.ac.soton.adDashboard.Interfaces.HistogramFeatures;
import uk.ac.soton.adDashboard.enums.Granularity;
import uk.ac.soton.adDashboard.enums.Stat;
import uk.ac.soton.adDashboard.filter.Filter;
import uk.ac.soton.adDashboard.records.DataSet;
import uk.ac.soton.adDashboard.views.Histogram;


/**
 * The overarching Controller of the system.
 */
public class Controller {

  private GraphFeatures graph;

  private ArrayList<HistogramFeatures> histogramFeatures = new ArrayList<>();
  private HistogramFeatures histogram;
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

  private DataSet latestDataset;
  private int latestId;

  /**
   * Selected font size. When this value changes some listeners update the font-size
   * -1: Smaller text
   * 0: default text
   * 1: bigger text
   */
  IntegerProperty fontSize = new SimpleIntegerProperty(0);

  public IntegerProperty getFontSize() {
    return fontSize;
  }

  public void setFontSize(int value) {
    fontSize.set(value);
  }

  public GraphFeatures getGraph() {
    return graph;
  }

  public HistogramFeatures getHistogram() {
    return histogram;
  }

  public void setGraph(GraphFeatures graph) {
    this.graph = graph;
  }

  public void addHistogram(HistogramFeatures histogram){this.histogramFeatures.add(histogram);}

  public void setHistogram(HistogramFeatures histogram) {this.histogram = histogram;}

  public int getLatestId() {
    return latestId;
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
    latestDataset=model;
    latestId = modelId -1;
  }

  /**
   * Gets the most recently added dataset.
   * @return Returns the most recently added dataSet.
   */
  public DataSet getLatestDataSet() {
    return latestDataset;
  }
  /**
   * removes a model.
   *
   * @param id The dataSetId of the model to remove.
   * @return Returns the DataSet that was removed.
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
      filterUpdated(filter);
    }
  }

  /**
   * Called to update the line for a Filter which has changed.
   *
   * @param filter The Filter object which changed.
   */
  public void filterUpdated(Filter filter) {
//    System.out.println(
//        "filter id " + filter.getId() + " model id" + filter.getDataSetId() + " filter itself"
//            + filter);
//    System.out.println(getFilterWindow().getFilters());
    DataSet model = getModels().get(filter.getDataSetId());
    model.setFilter(filter);

    String y = null;
    if(statType == Stat.totalImpressions){
      y = "Total Impressions /Impressions";
    }
    if(statType == Stat.totalClicks){
      y = "Total Clicks /Clicks";
    }
    if(statType == Stat.totalUniques){
      y = "Total Unique clicks /Clicks";
    }
    if(statType == Stat.totalBounces){
      y = "Total Bounces /Bounces";
    }
    if(statType == Stat.totalConversions){
      y = "Total Conversions /Conversions";
    }
    if(statType == Stat.totalCost){
      y = "total Cost /Pounds";
    }
    if(statType == Stat.CTR){
      y = "Click-through-rate /CTR";
    }

    if(statType == Stat.CPA){
      y = "Cost-per-acquisition /Pounds";
    }

    if(statType == Stat.CPC){
      y = "Cost-per-click /Pounds";
    }

    if(statType == Stat.CPM){
      y = "Cost-per-thousand impressions /Pounds";
    }

    if(statType == Stat.bounceRate){
      y = "Bounces per click /BPC";
    }

    Granularity g = getGranularity();
    String x = null;
    if(g == Granularity.DAY){
      x = "Day";
    }
    if(g == Granularity.MONTH){
      x = "Month";
    }
    if(g == Granularity.WEEK){
      x = "Week";
    }
    if(g == Granularity.YEAR){
      x = "Year";
    }

    if(statType == Stat.totalClickCost){
      List<Pair<String, Double>> points = model.newHistogram(filter.getStartDate(), filter.getEndDate());

      for(HistogramFeatures histogram : histogramFeatures){
          histogram.plot(filter.getId(), points, x);
      }

    }
    else {
      List<Pair<Integer, Double>> points = model.generateY(filter.getStartDate(), filter.getEndDate(), getGranularity(), statType); //todo: should the filter contain the unit?
      graph.delete(filter.getId());
      System.out.println("id" +filter.getId()+points.toString());
      graph.plot(filter.getId(), points, x, y);
    }

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
