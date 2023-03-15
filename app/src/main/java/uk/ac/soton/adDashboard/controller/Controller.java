package uk.ac.soton.adDashboard.controller;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import uk.ac.soton.adDashboard.Interfaces.GraphFeatures;
import uk.ac.soton.adDashboard.filter.Filter;
import uk.ac.soton.adDashboard.records.DataSet;

public class Controller {
  private List<GraphFeatures> subscribers= new ArrayList<>();
  private List<DataSet> models = new ArrayList<>();

  private List<Filter> filters = new ArrayList<>();

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

  public void setFilters(List<Filter> filters) {
    this.filters = filters;
  }

  public void filterUpdated(Filter filter) {
    for (DataSet model : models) {
      model.setFilter(filter);
      List<Pair<Integer,Double>> points = model.generateY(filter.getStartDate(),filter.getEndDate(), ChronoUnit.DAYS);
      for (GraphFeatures subscriber : subscribers) {
        subscriber.plot(points);
      }
    }
  }
}
