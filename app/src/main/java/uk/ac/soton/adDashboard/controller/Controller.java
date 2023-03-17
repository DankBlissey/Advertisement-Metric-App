package uk.ac.soton.adDashboard.controller;

import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import uk.ac.soton.adDashboard.Interfaces.GraphFeatures;
import uk.ac.soton.adDashboard.enums.Granularity;
import uk.ac.soton.adDashboard.enums.Stat;
import uk.ac.soton.adDashboard.filter.Filter;
import uk.ac.soton.adDashboard.records.DataSet;

public class Controller {

  private GraphFeatures graph;
  private DataSet model;

  private Filter filter;

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

  public void setModels(DataSet model) {
    this.model = model;
  }

  public Filter getFilters() {
    return filter;
  }

  public Stat getStatType() {
    return statType;
  }

  public void setStatType(Stat statType) {
    this.statType = statType;
  }

  public void setFilters(Filter filter) {
    this.filter= filter;
  }

  public Granularity getGranularity() {
    return granularity;
  }

  public void setGranularity(Granularity granularity) {
    this.granularity = granularity;
  }

  public void filterUpdated(Filter filter) {

    model.setFilter(filter);
    filter.setStat(statType);
    List<Pair<Integer, Double>> points = model.generateY(filter.getStartDate(),
        filter.getEndDate(), getGranularity()); //todo: should the filter contain the unit?
    graph.plot(filter.getId(),points);
  }

  public void deleteLine(Filter filter) {
    graph.delete(filter.getId());
  }


}
