package uk.ac.soton.adDashboard.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.adDashboard.components.FilterSet;
import uk.ac.soton.adDashboard.controller.Controller;
import uk.ac.soton.adDashboard.enums.Granularity;
import uk.ac.soton.adDashboard.enums.Stat;
import uk.ac.soton.adDashboard.filter.Filter;
import uk.ac.soton.adDashboard.records.DataSet;
import uk.ac.soton.adDashboard.ui.AppPane;
import uk.ac.soton.adDashboard.ui.AppWindow;

import java.util.ArrayList;

public class GraphView extends BaseView {
    private static final Logger logger = LogManager.getLogger(GraphView.class);
    private boolean switchedOn = false;
    /**
     * App class (logic)
     */

    protected DataSet dataSet;
    protected ArrayList<String> filenames;
    protected Graph graph;

    public GraphView(AppWindow appWindow, ArrayList<String> filenames) {
        super(appWindow);
        this.dataSet = appWindow.getController().getModel();
        this.filenames = filenames;
        logger.info("Creating the graph view View");
    }

    /**
     * Build the Landing Window
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new AppPane(appWindow.getWidth(), appWindow.getHeight());

        //builds the text for the dashboard writing and sets its style class
        Text title = new Text("Dashboard");
        title.getStyleClass().add("mediumText");

        //empty space so that dashboard is on top left and
        Region region = new Region();

        Button startAgain = new Button("Start Again");
        startAgain.getStyleClass().add("blueButton");

        MenuButton theme = new MenuButton("Theme");
        theme.getStyleClass().add("blueButton");
        MenuItem light = new MenuItem("Light");
        MenuItem dark = new MenuItem("Dark");
        theme.getItems().addAll(light, dark);
        light.setOnAction(e -> {
            appWindow.setDarkMode(false);
            appWindow.listViewWindow(filenames);
        });
        dark.setOnAction(e -> {
            appWindow.setDarkMode(true);
            appWindow.listViewWindow(filenames);
        });


        Button smaller = new Button("A-");
        smaller.getStyleClass().add("blueButton");
        Button bigger = new Button("A+");
        bigger.getStyleClass().add("blueButton");

        HBox sizeButtons = new HBox(smaller,bigger);

        HBox topButtons = new HBox(startAgain, theme, sizeButtons);

        //topButtons.getStyleClass().add("smallText");
        topButtons.setSpacing(10);

        HBox hbox = new HBox(title, region, topButtons);

        HBox.setHgrow(region, Priority.ALWAYS);

        hbox.setAlignment(Pos.CENTER);

        Rectangle backBar = new Rectangle(1280,150);
        backBar.getStyleClass().add("backBar");
        backBar.setEffect(new DropShadow(5,Color.GREY));

        Rectangle loadedRectangle = new Rectangle(200,130, Color.valueOf("#4B51FF"));
        loadedRectangle.setArcWidth(30);
        loadedRectangle.setArcHeight(30);

        Text loadedText = new Text(getFileNames(filenames));
        loadedText.getStyleClass().add("smallWhiteText");

        StackPane loadedFiles = new StackPane(loadedRectangle,loadedText);

        HBox longBarContent = new HBox(loadedFiles);

        longBarContent.setAlignment(Pos.CENTER);

        StackPane longBar = new StackPane(backBar,longBarContent);

        VBox vbox = new VBox(hbox, longBar);


        Color switchBack = Color.web("#4B51FF"); // create a Color object with the hex value for purple
        Color switchToggle = Color.web("4b0076");
        Color backgroundPane = Color.web("#F6F6F6");
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(4);
        dropShadow.setSpread(0.05);
        dropShadow.setColor(Color.GREY);

        // This is the border pane included in the whole view which includes the top bars, graphs, filters etc.
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("apppane");

        borderPane.setTop(vbox);
        BorderPane.setMargin(vbox, new Insets(0, 0, 25, 0));
        root.getChildren().add(borderPane);

        // This is the center of the borderPane - contains the toggle to list/graph and the graphs
        // There is a graphList which contains the toggle and multiple graphBoxes, each graphBox contains
        // the dropdown for Clicks, impressions, etc. and the graph itself
        VBox graphsList = new VBox(20);
        borderPane.setCenter(graphsList);
        graphsList.setMaxWidth(600);

        StackPane stack = new StackPane();
        Rectangle background = new Rectangle(100, 30);
        background.setFill(switchBack);
        background.setEffect(dropShadow);
        background.setArcWidth(30);
        background.setArcHeight(30);
        Text text = new Text("    List         Graph");
        text.getStyleClass().add("buttonTitle");
        Rectangle toggle = new Rectangle(50, 27, switchToggle);
        toggle.setOpacity(0.13);
        toggle.setStroke(Color.LIGHTGRAY);
        toggle.setStrokeWidth(0.5);
        toggle.setArcWidth(29);
        toggle.setArcHeight(30);
        toggle.setTranslateX(switchedOn ? -50 : 50);
        stack.getChildren().addAll(background, toggle, text);
        stack.setAlignment(Pos.CENTER_LEFT);

        stack.setOnMouseClicked(event -> {
              switchedOn = !switchedOn;
              toggle.setTranslateX(switchedOn ? -30 : 30);
            appWindow.loadView(new ListView(appWindow,filenames));
        });

        VBox graphBox = new VBox(20);
        graphBox.getStyleClass().add("graph-box");

        ComboBox<Color> cmb = new ComboBox<>();
        cmb.getItems().addAll(Color.RED, Color.GREEN, Color.BLUE);

        // Creates filter and graph object sets the filter and displays the graph
        Filter filter = new Filter();
        filter.setStat(Stat.totalImpressions);
        dataSet.setFilter(filter);
        graph = new Graph();
        graph.addNewSeries(dataSet.generateY(dataSet.earliestDate(),dataSet.latestDate(), Granularity.DAY));

        graphBox.getChildren().addAll(cmb, graph.getChart());
        graphsList.getChildren().addAll(stack,graphBox);

        // This is the right side of the borderPane
        Pane filterPane = new VBox(15);
        filterPane.getStyleClass().add("filter-pane");

        Text filterTitle = new Text("Filters");
        filterTitle.getStyleClass().add("mediumWhiteText");

        FilterSet set1 = new FilterSet("Filter set 1");

        filterPane.getChildren().addAll(filterTitle, set1);

        ScrollPane filtersScroll = new ScrollPane();
        filtersScroll.setContent(filterPane);
        filtersScroll.setPrefWidth(filterPane.USE_COMPUTED_SIZE);

        borderPane.setRight(filtersScroll);
        BorderPane.setMargin(filtersScroll, new Insets(0, 35, 0, 0));
    }

    /**
     * Takes the arraylist of filenames and outputs it as a string with line breaks
     * @param fileNames arraylist of filenames
     * @return string of the filenames with \n as linebreaks
     */
    private String getFileNames(ArrayList<String> fileNames) {
        StringBuilder output = new StringBuilder();
        for (String filename : filenames) {
            output.append(filename).append("\n");
        }
        return output.toString();
    }

    /**
     * Initialise the scene and start the app
     */
    @Override
    public void initialise() {
        logger.info("Initialising");
        //Initial stuff such as keyboard listeners
    }
}