package uk.ac.soton.adDashboard.views;

import javafx.geometry.Side;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javafx.util.Pair;
import uk.ac.soton.adDashboard.Interfaces.GraphFeatures;

public class Graph implements GraphFeatures {

    protected LineChart<Number, Number> chart;
    protected NumberAxis xAxis;
    protected NumberAxis yAxis;
    protected HashMap<Integer, XYChart.Series<Number, Number>> series;

    public Graph() {
        this.chart = createChart();
        this.series = new HashMap<>();
    }

    public LineChart<Number, Number> createChart() {
        xAxis = new NumberAxis();
        yAxis = new NumberAxis();

        yAxis.setUpperBound(43000);
        chart = new LineChart<>(xAxis, yAxis);

        // Set the axis and graph titles.
        chart.setTitle("Graph");
        chart.setCreateSymbols(false);
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setAxisSortingPolicy(LineChart.SortingPolicy.X_AXIS);
        chart.setHorizontalGridLinesVisible(true);
        chart.setVerticalGridLinesVisible(true);
        chart.setHorizontalZeroLineVisible(true);
        chart.setVerticalZeroLineVisible(true);
        chart.setLegendVisible(true);
        chart.setLegendSide(Side.RIGHT);

        // Set the size and position of the graph within the layout container.
        chart.setLayoutX(50);
        chart.setLayoutY(50);
        chart.setPrefSize(500, 500);
        return chart;
    }

    public LineChart<Number, Number> getChart(){
        return this.chart;
    }

    // Used to add all points to the Series
    private void addDataPoints(List<Pair<Number, Number>> data, XYChart.Series<Number, Number> series) {
        // Iterate through the data and add each point to the series.
        for (Pair<Number, Number> point : data) {
            Number xValue = point.getKey();
            Number yValue = point.getValue();
            series.getData().add(new XYChart.Data<>(xValue, yValue));
        }
    }

    // Creates a new Series
    private XYChart.Series<Number, Number> createNewSeries(List<Pair<Integer, Double>> data) {
        // Creates a new Series
        XYChart.Series<Number, Number> newSeries = new XYChart.Series<>();

        // Gets the points for the series
        List<Pair<Number, Number>> points = data.stream()
                .map(pair -> new Pair<Number, Number>(pair.getKey(), pair.getValue()))
                .collect(Collectors.toList());
        addDataPoints(points, newSeries);

        // Returns the new Series
        return newSeries;
    }


    // Adds data to an existing series in the chart
    @Override
    public void plot(int id, List<Pair<Integer, Double>> values) {
        // Gets the existing series for the specified ID
        XYChart.Series<Number, Number> existingSeries = series.get(id);

        if (existingSeries == null) {
            // If the series doesn't exist, create a new series and add it to the chart
            existingSeries = createNewSeries(values);
            series.put(id, existingSeries);
            chart.getData().add(existingSeries);
        } else {
            // If the series already exists, add new data points to the existing series
            for (Pair<Integer, Double> value : values) {
                existingSeries.getData().add(new XYChart.Data<>(value.getKey(), value.getValue()));
            }
        }
    }

    //removes line from the chart then removes it from the HashMap
    @Override
    public void delete(int id) {
        this.chart.getData().remove(series.get(id));
        series.remove(id);
    }
}