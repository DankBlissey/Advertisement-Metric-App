package uk.ac.soton.adDashboard.views;

import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.util.Pair;
import uk.ac.soton.adDashboard.Interfaces.HistogramFeatures;

import java.util.List;
import java.util.stream.Collectors;

public class Histogram implements HistogramFeatures {

    protected BarChart<String, Number> chart;
    protected String xAxis;
    protected NumberAxis yAxis;
    protected XYChart.Series<String, Number> series;

    public Histogram(){
        this.chart = createChart();
    }

    private BarChart<String, Number> createChart(){
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        chart = new BarChart<>(xAxis,yAxis);

        //Set the axis and graph titles.
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setHorizontalGridLinesVisible(true);
        chart.setVerticalGridLinesVisible(true);
        chart.setHorizontalZeroLineVisible(true);
        chart.setVerticalZeroLineVisible(true);
        chart.setLegendVisible(true);
        chart.setLegendSide(Side.RIGHT);

        //Set the size and position of the graph within the layout container.
        chart.setLayoutX(50);
        chart.setLayoutY(50);
        chart.setPrefSize(500, 500);

        return chart;
    }

    public BarChart<String, Number> getChart(){
        return this.chart;
    }

    //Used to add all points to the Series
    private void addDataPoints(List<Pair<String, Number>> data) {
        //Iterate through the data and add each point to the series.
        for (Pair<String, Number> point : data) {
            String xValue = point.getKey();
            Number yValue = point.getValue();
            this.series.getData().add(new XYChart.Data<>(xValue, yValue));
        }
    }

    //Creates a new Series
    private XYChart.Series<String, Number> ChangeSeries(List<Pair<String, Double>> data) {
        //removes old series from graph
        this.chart.getData().remove(this.series);

        //Creates a new Series
        XYChart.Series<String, Number> newSeries = new XYChart.Series<>();

        //Gets the points for the series
        List<Pair<String, Number>> points = data.stream()
                .map(pair -> new Pair<String, Number>(pair.getKey(), pair.getValue()))
                .collect(Collectors.toList());
        addDataPoints(points);

        //Returns the new Series
        return newSeries;
    }

    //Adds/changes the histogram bars
    @Override
    public void plot(List<Pair<String, Double>> values) {
        //creates the new series and adds
        XYChart.Series<String, Number> newSeries = ChangeSeries(values);
        this.series = newSeries;
        this.chart.getData().add(this.series);
    }


}



