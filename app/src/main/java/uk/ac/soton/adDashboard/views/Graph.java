package uk.ac.soton.adDashboard.views;

import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

public class Graph {

    protected LineChart<Number, Number> chart;
    protected NumberAxis xAxis;
    protected NumberAxis yAxis;
    protected List<XYChart.Series<Number, Number>> dataSeriesList;

    public Graph() {
        this.dataSeriesList = new ArrayList<>();
        this.chart = createChart();
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

    public LineChart<Number, Number> getChart() {
        return this.chart;
    }

    // Used to add all points to the Series
    public void addDataPoints(List<Pair<Integer, Double>> data, XYChart.Series<Number, Number> series) {
        // Iterate through the data and add each point to the series.
        for (Pair<Integer, Double> point : data) {
            Number xValue = point.getKey();
            Number yValue = point.getValue();
            series.getData().add(new XYChart.Data<>(xValue, yValue));
        }
    }

    //Gets the Series by Index
    public XYChart.Series<Number, Number> getSeriesByIndex(int index) {
        if (index < 0 || index >= dataSeriesList.size()) {
            throw new IndexOutOfBoundsException("Invalid series index: " + index);
        }
        return dataSeriesList.get(index);
    }

    //Used to change the points of a Series by Index in the ArrayList
    public void resetIndexSeries(List<Pair<Integer, Double>> data, int index){
        this.dataSeriesList.add(index,createNewSeries(data));
    }

    //Creates a new Series
    public XYChart.Series<Number, Number> createNewSeries(List<Pair<Integer, Double>> data) {
        // Creates a new Series
        XYChart.Series<Number, Number> newSeries = new XYChart.Series<>();

        // Gets the points for the series
        addDataPoints(data, newSeries);

        //returns the new Series
        return newSeries;
    }

    //creates a new Series and adds it to the Series Lists and the Chart
    public void addNewSeries(ArrayList<Pair<Integer, Double>> data) {
        // Creates a new Series
        XYChart.Series<Number, Number> newSeries = createNewSeries(data);

        // Adds the new series to the list
        this.dataSeriesList.add(newSeries);

        // Adds the new series to the chart
        this.chart.getData().add(newSeries);
    }

    public void changeLineColor(Color newColor, int index) {
        // Find the Node representing the line for the series
        Node lineNode = getSeriesByIndex(index).getNode().lookup(".chart-series-line");

        // Set the stroke color of the line
        lineNode.setStyle("-fx-stroke: " + toRGBCode(newColor) + ";");
    }

    private String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    public void deleteSeriesByIndex(int index) {
        // Check if the index is valid
        if (index < 0 || index >= dataSeriesList.size()) {
            throw new IndexOutOfBoundsException("Invalid series index: " + index);
        }

        // Remove the series from the data series list and the chart
        XYChart.Series<Number, Number> seriesToRemove = dataSeriesList.remove(index);
        chart.getData().remove(seriesToRemove);

        // Update the series index for all the remaining series
        for (int i = index; i < dataSeriesList.size(); i++) {
            XYChart.Series<Number, Number> series = dataSeriesList.get(i);
            series.setName("Series " + i);  // Update the series name to reflect the new index
        }
    }

    public void deleteAllSeries() {
        // Remove all series from the chart
        chart.getData().clear();

        // Clear the dataSeriesList
        dataSeriesList.clear();
    }

}