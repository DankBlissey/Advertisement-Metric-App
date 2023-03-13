package uk.ac.soton.adDashboard.views;

import javafx.geometry.Side;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class Graph{

    protected LineChart<Number, Number> chart;
    protected NumberAxis xAxis;
    protected NumberAxis yAxis;
    protected XYChart.Series<Number, Number> dataSeries;

    public Graph(){
        this.dataSeries = new XYChart.Series<>();
        this.chart = createChart();
    }

    public LineChart<Number, Number> createChart(){

        xAxis = new NumberAxis();
        yAxis = new NumberAxis();

        chart = new LineChart<>(xAxis,yAxis);

        // Create a new series for the graph data
        dataSeries = new XYChart.Series<>();

        // Add some data points to the series
        dataSeries.getData().add(new XYChart.Data<>(1, 6));
        dataSeries.getData().add(new XYChart.Data<>(2, 2));
        dataSeries.getData().add(new XYChart.Data<>(3, 4));
        dataSeries.getData().add(new XYChart.Data<>(4, 8));
        dataSeries.getData().add(new XYChart.Data<>(5, 10));

        // Add the series to the graph
        chart.getData().add(dataSeries);

        // Set the axis and graph titles
        chart.setTitle("Graph");
        chart.setCreateSymbols(false);
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
        chart.setHorizontalGridLinesVisible(true);
        chart.setVerticalGridLinesVisible(true);
        chart.setHorizontalZeroLineVisible(true);
        chart.setVerticalZeroLineVisible(true);
        chart.setLegendVisible(true);
        chart.setLegendSide(Side.RIGHT);

        // Set the size and position of the graph within the layout container
        chart.setLayoutX(50);
        chart.setLayoutY(50);
        chart.setPrefSize(500, 500);
        return chart;
    }

    public LineChart<Number, Number> getChart(){
        return this.chart;
    }

    public XYChart.Series<Number, Number> getSeries(){
        return this.dataSeries;
    }


}
