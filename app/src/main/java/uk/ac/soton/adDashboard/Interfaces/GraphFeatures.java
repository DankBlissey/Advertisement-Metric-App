package uk.ac.soton.adDashboard.Interfaces;

import java.util.List;
import javafx.util.Pair;

public interface GraphFeatures {
  void plot(int id,List<Pair<Integer,Double>> values, String x, String y);
  void delete(int id);
}
