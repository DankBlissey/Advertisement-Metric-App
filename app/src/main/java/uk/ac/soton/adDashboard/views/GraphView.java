package uk.ac.soton.adDashboard.views;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.adDashboard.App;
import uk.ac.soton.adDashboard.Interfaces.FilterWindow;
import uk.ac.soton.adDashboard.components.FilterSet;
import uk.ac.soton.adDashboard.controller.Controller;
import uk.ac.soton.adDashboard.enums.Granularity;
import uk.ac.soton.adDashboard.enums.Stat;
import uk.ac.soton.adDashboard.filter.Filter;
import uk.ac.soton.adDashboard.records.DataSet;
import uk.ac.soton.adDashboard.ui.AppPane;
import uk.ac.soton.adDashboard.ui.AppWindow;

import java.util.ArrayList;

public class GraphView extends BaseView implements FilterWindow {
    private static final Logger logger = LogManager.getLogger(GraphView.class);
    private boolean switchedOn = false;
    /**
     * App class (logic)
     */

    protected DataSet dataSet;
    protected ArrayList<String> filenames;
    protected Graph graph;

    /**
     * VBox which contains multiple FilterSets that are displayed in a scrollable view
     */
    private VBox filterSetPane;

    /**
     * List which contains a copy of each Filter object which is also included in it's representing FilterSet
     */
    private ArrayList<Filter> filters;

    public GraphView(AppWindow appWindow, ArrayList<String> filenames) {
        super(appWindow);
        this.dataSet = controller.getModel();
        this.filenames = filenames;
        controller.setFilterWindow(this);
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

        Button startAgain = new Button("Go Back");
        startAgain.setOnAction(e -> {
            appWindow.bounceRateWindow(filenames);
        });
        startAgain.getStyleClass().add("blueButton");

        MenuButton theme = new MenuButton("Theme");
        theme.getStyleClass().add("menu-item");
        MenuItem light = new MenuItem("Light");
        MenuItem dark = new MenuItem("Dark");
        theme.getItems().addAll(light, dark);
        light.setOnAction(e -> {
            appWindow.setDarkMode(false);
            appWindow.graphViewWindow(filenames);
        });
        dark.setOnAction(e -> {
            appWindow.setDarkMode(true);
            appWindow.graphViewWindow(filenames);
        });


        Button smaller = new Button("A-");
        smaller.getStyleClass().add("blueButton");
        Button bigger = new Button("A+");
        bigger.getStyleClass().add("blueButton");

        HBox sizeButtons = new HBox(smaller,bigger);
        sizeButtons.setAlignment(Pos.CENTER);

        HBox topButtons = new HBox(startAgain, theme, sizeButtons);

        //topButtons.getStyleClass().add("smallText");
        topButtons.setSpacing(10);
        topButtons.setAlignment(Pos.CENTER);

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
        VBox toggleButton = new VBox();
        BorderPane.setMargin(toggleButton, new Insets(10, 0, 0, 10));
        toggleButton.getChildren().add(stack);
        stack.setAlignment(Pos.CENTER_LEFT);

        stack.setOnMouseClicked(event -> {
              switchedOn = !switchedOn;
              toggle.setTranslateX(switchedOn ? -30 : 30);
            appWindow.loadView(new ListView(appWindow,filenames));
        });

        borderPane.setLeft(toggleButton);

        VBox graphBox = new VBox(20);
        graphBox.getStyleClass().add("graph-box");

        ComboBox<String> cmb = new ComboBox<>();
        cmb.getStyleClass().add("bounce-dropdown");
        cmb.getItems().addAll("total Impressions", "total Clicks", "total Uniques", "total Bounces", "total Conversions", "total Cost", "CTR", "CPA", "CPC", "CPM", "bounce Rate");

        cmb.setOnAction((e) -> {
            String selectedOption = cmb.getValue();
            Stat selectedStat = null;
            switch (selectedOption) {
                case "total Impressions" -> selectedStat = Stat.totalImpressions;
                case "total Clicks" -> selectedStat = Stat.totalClicks;
                case "total Uniques" -> selectedStat = Stat.totalUniques;
                case "total Bounces" -> selectedStat = Stat.totalBounces;
                case "total Conversions" -> selectedStat = Stat.totalConversions;
                case "total Cost" -> selectedStat = Stat.totalCost;
                case "CTR" -> selectedStat = Stat.CTR;
                case "CPA" -> selectedStat = Stat.CPA;
                case "CPC" -> selectedStat = Stat.CPC;
                case "CPM" -> selectedStat = Stat.CPM;
                case "bounce Rate" -> selectedStat = Stat.bounceRate;
                default -> {
                }
            }
            logger.info("Selected stat: " + selectedStat);
            if(selectedStat != null) {
                AppWindow.getController().setStatType(selectedStat);
            }
        });
        cmb.setValue("total Impressions");

        graph = new Graph();
        controller.setGraph(graph);

        ComboBox<String> granularity = new ComboBox<>();
        granularity.getStyleClass().add("bounce-dropdown");
        granularity.getItems().addAll("day","week","month","year");
        granularity.setValue("day");

        granularity.setOnAction((e) -> {
            String selectedGranularityOption = granularity.getValue();
            Granularity selectedGranularity = null;
            switch (selectedGranularityOption) {
                case "day" -> selectedGranularity = Granularity.DAY;
                case "week" -> selectedGranularity = Granularity.WEEK;
                case "month" -> selectedGranularity = Granularity.MONTH;
                case "year" -> selectedGranularity = Granularity.YEAR;
                default -> {
                }
            }
            logger.info("selected Granularity: " + selectedGranularity);
            if(selectedGranularity != null){
                AppWindow.getController().setGranularity(selectedGranularity);
            }
        });

        HBox itemMenus = new HBox(cmb, granularity);
        granularity.setTranslateX(350);

        graphBox.getChildren().addAll(itemMenus, graph.getChart());
        graphsList.getChildren().addAll(graphBox);

        // This is the right side of the borderPane which includes a "filterPane"
        HBox filterSide = new HBox(30);
        filterSide.setAlignment(Pos.CENTER);
        Button showPaneButton = new Button("<");
        showPaneButton.setVisible(false);
        showPaneButton.getStyleClass().add("arrow");

        VBox filterPane = new VBox(15);
        filterPane.getStyleClass().add("filter-pane");

        Text filterTitle = new Text("Filters");
        Button hidePaneButton = new Button("Hide");
        filterTitle.getStyleClass().add("mediumWhiteText");
        hidePaneButton.getStyleClass().add("mediumGreyText-button");

        hidePaneButton.setOnAction(e -> {
            toggleFilterPane(false, filterPane, showPaneButton);
        });
        showPaneButton.setOnAction(e -> {
            toggleFilterPane(true, filterPane, showPaneButton);
        });

        HBox filterPaneTop = new HBox();
        filterPaneTop.setAlignment(Pos.CENTER);
        Region region2 = new Region();
        HBox.setHgrow(region2, Priority.ALWAYS);
        filterPaneTop.getChildren().addAll(filterTitle, region2, hidePaneButton);

        Button addFilterButton = new Button("+ Add filter set");
        addFilterButton.getStyleClass().add("simple-button");
        addFilterButton.setOnAction(e -> {
            addNewFilter();
        });

        // The filterPane includes a scrollable FilterSetPane which includes multiple filterSets
        filterSetPane = new VBox(15);
        filterSetPane.getStyleClass().add("filter-set-pane");

        Filter defaultFilter = defaultFilter();
        logger.info("creating the filter");
        filters = new ArrayList<Filter>();
        filters.add(defaultFilter);
        FilterSet defaultFilterSet = new FilterSet("Filter set 1", defaultFilter, null, appWindow);
        filterSetPane.getChildren().add(defaultFilterSet);

        ScrollPane filtersScroll = new ScrollPane();
        filtersScroll.setContent(filterSetPane);
        filtersScroll.setPrefWidth(filterSetPane.USE_COMPUTED_SIZE);
        filtersScroll.getStyleClass().add("filter-scroll");
        filtersScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        filtersScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        filterPane.getChildren().addAll(filterPaneTop, filtersScroll, addFilterButton);
        filterSide.getChildren().addAll(showPaneButton, filterPane);

        //borderPane.setRight(filterPane);
        //BorderPane.setMargin(filterPane, new Insets(0, 35, 0, 0));
        borderPane.setRight(filterSide);
        BorderPane.setMargin(filterSide, new Insets(0, 35, 0, 0));
    }

    private void toggleFilterPane(Boolean option, VBox filterPane, Button showPaneButton) {

        if(!option) {
            TranslateTransition slideOut = new TranslateTransition(Duration.seconds(1), filterPane);
            slideOut.setByX(400); // slide to the right by 100 pixels
            slideOut.play();

            TranslateTransition slideOut2 = new TranslateTransition(Duration.seconds(1), showPaneButton);
            slideOut2.setByX(400); // slide to the right by 100 pixels
            slideOut2.play();

            showPaneButton.setVisible(true);
        } else {
            TranslateTransition slideIn = new TranslateTransition(Duration.seconds(1), filterPane);
            slideIn.setByX(-400); // slide to the right by 100 pixels
            slideIn.play();

            TranslateTransition slideIn2 = new TranslateTransition(Duration.seconds(1), showPaneButton);
            slideIn2.setByX(-400); // slide to the right by 100 pixels
            slideIn2.play();

            showPaneButton.setVisible(false);
        }
        //filterPane.setVisible(option);
    }

    private Filter defaultFilter() {
        var defaultFilter = new Filter();
        defaultFilter.setStartDate(controller.getModel().earliestDate());
        defaultFilter.setEndDate(controller.getModel().latestDate());
        defaultFilter.setId(0);
        defaultFilter.setDataSetId(0);
        return defaultFilter;
    }

    /**
     * Function calculates the next index that can be used for a new filter
     * @return index
     */
    private int getValidIndex() {
        int max = filters.get(0).getId();

        for(Filter currentFilter : filters) {
            if (currentFilter.getId() > max) {
                max = currentFilter.getId();
            }
        }

        return max + 1;
    }

    /**
     * Function which creates a new Filter with the next available index,
     * then creates a new FilterSet to display in the FilterSetPane.
     */
    private void addNewFilter() {
        int index = getValidIndex();

        Filter newFilter = defaultFilter();
        newFilter.setId(index);
        filters.add(newFilter);

        Button deleteButton = new Button("x");

        FilterSet newFilterSet = new FilterSet("Filter set " + (index + 1), newFilter, deleteButton, appWindow);

        deleteButton.setOnAction(e -> {
            filterSetPane.getChildren().remove(newFilterSet);
            filters.remove(newFilter);
            AppWindow.getController().deleteLine(newFilter);
            logger.info("Deleted filter at index " + index);
        });

        filterSetPane.getChildren().add(newFilterSet);
        logger.info("Created a new filter with index " + index);
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
     * Getter for filter ArrayList
     * @return filters
     */
    public ArrayList<Filter> getFilters() {
        return this.filters;
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