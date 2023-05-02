package uk.ac.soton.adDashboard.Interfaces;

import javafx.util.Pair;

import java.util.List;

public interface HistogramFeatures {
   void plot(int index, List<Pair<String, Double>> values);

}
