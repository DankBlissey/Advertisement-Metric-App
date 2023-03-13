package uk.ac.soton.adDashboard.views;

import javafx.geometry.Side;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;

public class Graph{

    protected LineChart<Number, Number> chart;
    protected NumberAxis xAxis;
    protected NumberAxis yAxis;
    protected ArrayList<XYChart.Series<Number, Number>> dataSeriesList;

    public Graph(){
        this.dataSeriesList = new ArrayList<>();
        this.chart = createChart();
    }

    public LineChart<Number, Number> createChart(){
        xAxis = new NumberAxis();
        yAxis = new NumberAxis();

        chart = new LineChart<>(xAxis,yAxis);

        //Set the axis and graph titles.
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

        //Set the size and position of the graph within the layout container.
        chart.setLayoutX(50);
        chart.setLayoutY(50);
        chart.setPrefSize(500, 500);
        return chart;
    }

    public LineChart<Number, Number> getChart(){
        return this.chart;
    }

    //Used to add all points to the Series
    public void addDataPoints(ArrayList<Number> X,ArrayList<Number> Y, XYChart.Series<Number, Number> Series){
        //Iterate through X and Y using a loop.
        for (int i = 0; i < X.size() && i < Y.size(); i++) {
            //Get the x and y values for the current data point.
            Number xValue = X.get(i);
            Number yValue = Y.get(i);

            //Adds it to the dataSeries.
            Series.getData().add(new XYChart.Data<>(xValue, yValue));
        }
    }

    //Used to change the points of a Series
    public void resetSeries(ArrayList<Number> X,ArrayList<Number> Y, XYChart.Series<Number, Number> Series){
        //Creates new Series for chart
        XYChart.Series<Number, Number> newSeries = new XYChart.Series<>();
        addDataPoints(X,Y,newSeries);

        //Replaces old line on the graph
        Series = newSeries;
    }

    //gets the Series by Index
    public XYChart.Series<Number, Number> getSeriesByIndex(int index) {
        if (index < 0 || index >= dataSeriesList.size()) {
            throw new IndexOutOfBoundsException("Invalid series index: " + index);
        }
        return dataSeriesList.get(index);
    }

    //Used to change the points of a Series by Index in the arraylist
    public void resetIndexSeries(ArrayList<Number> X,ArrayList<Number> Y,int index){
        resetSeries(X,Y,getSeriesByIndex(index));
    }

    //Creates a new Series
    public XYChart.Series<Number, Number> createNewSeries(ArrayList<Number> X,ArrayList<Number> Y){
        //Creates a new Series
        XYChart.Series<Number, Number> newSeries = new XYChart.Series<>();

        //Gets the points for the series
        addDataPoints(X,Y,newSeries);

        //returns the new Series
        return newSeries;
    }

    //creates a new Series and adds it to the Series Lists and the Chart
    public void addNewSeries(ArrayList<Number> X,ArrayList<Number> Y){

        //Creates a new Series
        XYChart.Series<Number, Number> newSeries = createNewSeries(X,Y);

        //Adds Series to the List
        this.dataSeriesList.add(newSeries);

        //Adds Series to the Chart
        this.chart.getData().add(newSeries);
    }

}
