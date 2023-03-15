package uk.ac.soton.adDashboard.Interfaces;

import java.util.List;
import javafx.util.Pair;

public interface GraphFeatures {
  void plot(List<Pair<Integer,Double>> values);
}
