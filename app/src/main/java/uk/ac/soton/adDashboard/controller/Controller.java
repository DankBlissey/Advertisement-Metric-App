package uk.ac.soton.adDashboard.controller;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import uk.ac.soton.adDashboard.Interfaces.GraphFeatures;
import uk.ac.soton.adDashboard.enums.Granularity;
import uk.ac.soton.adDashboard.enums.Stat;
import uk.ac.soton.adDashboard.filter.Filter;
import uk.ac.soton.adDashboard.records.DataSet;

public class Controller {
  private List<GraphFeatures> subscribers= new ArrayList<>();
  private List<DataSet> models = new ArrayList<>();

  private List<Filter> filters = new ArrayList<>();

  private Stat statType;

  private Granularity granularity = Granularity.DAY;

  public List<?> getSubscribers() {
    return subscribers;
  }


  public void setSubscribers(List<GraphFeatures> subscribers) {
    this.subscribers = subscribers;
  }

  public List<DataSet> getModels() {
    return models;
  }

  public void setModels(List<DataSet> models) {
    this.models = models;
  }

  public List<Filter> getFilters() {
    return filters;
  }

  public Stat getStatType() {
    return statType;
  }

  public void setStatType(Stat statType) {
    this.statType = statType;
  }

  public void setFilters(List<Filter> filters) {
    this.filters = filters;
  }

  public Granularity getGranularity() {
   return granularity;
  }

  public void setGranularity(Granularity granularity) {
    this.granularity = granularity;
  }

  public void filterUpdated(Filter filter) {
    for (DataSet model : models) {
      model.setFilter(filter);
      filter.setStat(statType);
      List<Pair<Integer,Double>> points = model.generateY(filter.getStartDate(),filter.getEndDate(), getGranularity()); //todo: should the filter contain the unit?
      for (GraphFeatures subscriber : subscribers) {
        subscriber.plot(points);
      }
    }
  }
}
