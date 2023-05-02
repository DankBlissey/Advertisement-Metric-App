package uk.ac.soton.adDashboard.views;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    protected ArrayList<Histogram> histograms;
    private int noCampains;
    private HBox longBarContent;
    private ScrollPane histogramScroll;
    private VBox histogramBox;

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
        noCampains = controller.getModels().size();
        logger.info("Creating the graph view View");
        logger.info("number of campaigns:" + noCampains);
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
            appWindow.bounceRateWindow(filenames, false);
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

        smaller.setOnAction(e -> {
            logger.info("Smaller");
            int currentSize = AppWindow.getController().getFontSize().get();
            if(currentSize != -1) {
                AppWindow.getController().setFontSize(currentSize - 1);
            }
        });
        bigger.setOnAction(e -> {
            logger.info("Bigger");
            int currentSize = AppWindow.getController().getFontSize().get();
            if(currentSize != 1) {
                AppWindow.getController().setFontSize(currentSize + 1);
            }
        });

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

        //Button to add more campaigns
        Button anotherCampaign = new Button("+ Compare campaigns");
        anotherCampaign.getStyleClass().add("smallBlackText-button");
        anotherCampaign.setOnAction(e -> switchView(new AnotherCampaignView(appWindow,this)));

        Rectangle loadedRectangle = new Rectangle(200,130, Color.valueOf("#4B51FF"));
        loadedRectangle.setArcWidth(30);
        loadedRectangle.setArcHeight(30);

        Text loadedText = new Text(getFileNames(filenames));
        loadedText.getStyleClass().add("smallWhiteText");

        // StackPane loadedFiles = new StackPane(loadedRectangle,loadedText);

        longBarContent = new HBox();
        generateCampaigns();
        longBarContent.getChildren().add(anotherCampaign);
        longBarContent.setAlignment(Pos.CENTER);
        longBarContent.setSpacing(10);

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
            switchView(new ListView(appWindow,filenames));
        });

        borderPane.setLeft(toggleButton);

        VBox graphBox = new VBox(20);
        graphBox.getStyleClass().add("graph-box");

        ComboBox<String> cmb = new ComboBox<>();
        cmb.getStyleClass().add("bounce-dropdown");
        cmb.getItems().addAll("total Impressions", "total Clicks", "total Uniques", "total Bounces", "total Conversions", "total Cost", "CTR", "CPA", "CPC", "CPM", "bounce Rate", "Click Cost");


        cmb.setValue("total Impressions");

        graph = new Graph();
        Histogram histogram = new Histogram(0);

        controller.setGraph(graph);
        controller.addHistogram(histogram);

        histograms = new ArrayList<>();
        histograms.add(histogram);

        ComboBox<String> granularity = new ComboBox<>();
        granularity.getStyleClass().add("bounce-dropdown-grey");
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

        Region region3 = new Region();
        HBox itemMenus = new HBox(cmb, region3, granularity);
        HBox.setHgrow(region3, Priority.ALWAYS);

        histogramBox = new VBox();
        histogramBox.getChildren().add(histogram.getChart());

        histogramScroll = new ScrollPane();
        histogramScroll.setContent(histogramBox);

        graphBox.getChildren().addAll(itemMenus, graph.getChart(), histogramScroll);
        graphBox.getChildren().get(2).setVisible(false);
        graphBox.getChildren().get(2).setManaged(false);
        graphsList.getChildren().addAll(graphBox);

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
                case "Click Cost" -> selectedStat = Stat.totalClickCost;
                default -> {
                }

            }
            logger.info("Selected stat: " + selectedStat);
            if(selectedStat != null) {
                AppWindow.getController().setStatType(selectedStat);
            }

            if(selectedOption.equals("Click Cost")) {
                graphBox.getChildren().get(1).setVisible(false);
                graphBox.getChildren().get(1).setManaged(false);
                graphBox.getChildren().get(2).setVisible(true);
                graphBox.getChildren().get(2).setManaged(true);
            }
            else {
                graphBox.getChildren().get(1).setVisible(true);
                graphBox.getChildren().get(1).setManaged(true);
                graphBox.getChildren().get(2).setVisible(false);
                graphBox.getChildren().get(2).setManaged(false);
            }
        });

        // This is the right side of the borderPane which includes a "filterPane"
        HBox filterSide = new HBox(30);
        filterSide.setAlignment(Pos.CENTER);
        Button showPaneButton = new Button("<");
        showPaneButton.setVisible(false);
        showPaneButton.getStyleClass().add("show-button");

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

        Button saveButton = new Button("Save results");
        saveButton.getStyleClass().add("blueButton");
        saveButton.setOnAction(e -> {
            ScreenShot(graphsList);
        });

        topButtons.getChildren().add(saveButton);

        backBar.widthProperty().bind(root.widthProperty());

        AppWindow.getController().getFontSize().addListener((obs, oldVal, newVal) -> {
            logger.info("Font-size changed from " + oldVal + " to " + newVal);

            if(newVal.intValue() == -1) {
                title.setStyle(title.getStyle() + "-fx-font-size: 30px;");
                startAgain.setStyle(startAgain.getStyle() + "-fx-font-size: 13px;");
                theme.setStyle(theme.getStyle() + "-fx-font-size: 13px;");
                smaller.setStyle(smaller.getStyle() + "-fx-font-size: 13px;");
                bigger.setStyle(bigger.getStyle() + "-fx-font-size: 13px;");
                saveButton.setStyle(saveButton.getStyle() + "-fx-font-size: 13px;");
                anotherCampaign.setStyle(anotherCampaign.getStyle() + "-fx-font-size: 13px;");
                loadedText.setStyle(loadedText.getStyle() + "-fx-font-size: 13px !important;");
                //text.setStyle(text.getStyle() + "-fx-font-size: 10px;");
                cmb.setStyle(cmb.getStyle() + "-fx-font-size: 12px;");
                granularity.setStyle(granularity.getStyle() + "-fx-font-size: 12px;");
                filterTitle.setStyle(filterTitle.getStyle() + "-fx-font-size: 16px;");
                hidePaneButton.setStyle(hidePaneButton.getStyle() + "-fx-font-size: 12px;");
                addFilterButton.setStyle(addFilterButton.getStyle() + "-fx-font-size: 13px;");
                saveButton.setStyle(saveButton.getStyle() + "-fx-font-size: 13px;");
            }
            else if(newVal.intValue() == 0) {
                title.setStyle(title.getStyle() + "-fx-font-size: 35px;");
                startAgain.setStyle(startAgain.getStyle() + "-fx-font-size: 15px;");
                theme.setStyle(theme.getStyle() + "-fx-font-size: 15px;");
                smaller.setStyle(smaller.getStyle() + "-fx-font-size: 15px;");
                bigger.setStyle(bigger.getStyle() + "-fx-font-size: 15px;");
                saveButton.setStyle(saveButton.getStyle() + "-fx-font-size: 15px;");
                anotherCampaign.setStyle(anotherCampaign.getStyle() + "-fx-font-size: 15px;");
                //
                loadedText.setStyle(loadedText.getStyle() + "-fx-font-size: 15px !important;");
                //
                //text.setStyle(text.getStyle() + "-fx-font-size: 12px;");
                cmb.setStyle(cmb.getStyle() + "-fx-font-size: 14px;");
                granularity.setStyle(granularity.getStyle() + "-fx-font-size: 14px;");
                filterTitle.setStyle(filterTitle.getStyle() + "-fx-font-size: 18px;");
                hidePaneButton.setStyle(hidePaneButton.getStyle() + "-fx-font-size: 14px;");
                addFilterButton.setStyle(addFilterButton.getStyle() + "-fx-font-size: 15px;");
                saveButton.setStyle(saveButton.getStyle() + "-fx-font-size: 15px;");
            }
            else if(newVal.intValue() == 1) {
                title.setStyle(title.getStyle() + "-fx-font-size: 40px;");
                startAgain.setStyle(startAgain.getStyle() + "-fx-font-size: 17px;");
                theme.setStyle(theme.getStyle() + "-fx-font-size: 17px;");
                smaller.setStyle(smaller.getStyle() + "-fx-font-size: 17px;");
                bigger.setStyle(bigger.getStyle() + "-fx-font-size: 17px;");
                saveButton.setStyle(saveButton.getStyle() + "-fx-font-size: 17px;");
                anotherCampaign.setStyle(anotherCampaign.getStyle() + "-fx-font-size: 17px;");
                loadedText.setStyle(loadedText.getStyle() + "-fx-font-size: 20px;");
                //text.setStyle(text.getStyle() + "-fx-font-size: 14px;");
                cmb.setStyle(cmb.getStyle() + "-fx-font-size: 16px;");
                granularity.setStyle(granularity.getStyle() + "-fx-font-size: 16px;");
                filterTitle.setStyle(filterTitle.getStyle() + "-fx-font-size: 20px;");
                hidePaneButton.setStyle(hidePaneButton.getStyle() + "-fx-font-size: 16px;");
                addFilterButton.setStyle(addFilterButton.getStyle() + "-fx-font-size: 17px;");
                saveButton.setStyle(saveButton.getStyle() + "-fx-font-size: 17px;");
            }
        });
        AppWindow.getController().setFontSize(AppWindow.getController().getFontSize().get() - 1);
        AppWindow.getController().setFontSize(AppWindow.getController().getFontSize().get() + 1);
    }

    private void addHistogram(Filter filter){
        System.out.println("I got here");
        Histogram histogram = new Histogram(filter.getId());
        controller.addHistogram(histogram);
        this.histograms.add(histogram);
        histogramBox.getChildren().add(histogram.getChart());
        histogramScroll.setContent(histogramBox);
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
        defaultFilter.setDataSetId(controller.getModelIds().get(0));
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
        addHistogram(newFilter);

        Button deleteButton = new Button("x");

        FilterSet newFilterSet = new FilterSet("Filter set " + (index + 1), newFilter, deleteButton, appWindow);

        deleteButton.setOnAction(e -> {
            filterSetPane.getChildren().remove(newFilterSet);
            removeFilter(newFilter);
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

    /**
     * Method for generating the objects listing the loaded files and for which campaign
     */
    private void generateCampaigns(){
        ArrayList<Integer> ids = controller.getModelIds();
        for (int idIndex = 0; idIndex < ids.size(); idIndex++) {
            int modelId = ids.get(idIndex);
            //Box containing first set of loaded files
            Rectangle loadedRectangle = new Rectangle(200, 130, Color.valueOf("#4B51FF"));
            loadedRectangle.setArcWidth(30);
            loadedRectangle.setArcHeight(30);
            int campaignNum = idIndex + 1;
            Text title = new Text("Campaign " + modelId);
            title.getStyleClass().add("smallBlueText");
            Text loadedText = new Text(getFileNames(filenames));
            loadedText.getStyleClass().add("smallWhiteText");

            Button close = new Button("X");
            close.getStyleClass().add("delete-filter-button");

            HBox helperBox = new HBox();
            helperBox.setAlignment(Pos.CENTER_RIGHT);
            helperBox.getChildren().add(close);
            helperBox.setPadding(new Insets(0, 5, 0, 0));

            int finalI = modelId;
            close.setOnAction(e -> removeset(finalI));
            VBox vbox;
            if (ids.size() > 1) {
                vbox = new VBox(helperBox, title, loadedText);
            } else {
                vbox = new VBox(title, loadedText);
            }
            vbox.setStyle("-fx-background-color: transparent;");
            vbox.setAlignment(Pos.CENTER);
            StackPane loadedFiles = new StackPane(loadedRectangle, vbox);
//            longBarContent.getChildren().clear();
            longBarContent.getChildren().add(loadedFiles);
        }
    }
    /**
     * Method to remove a campaign from UI and from the list of models
     *
     * @param i the particular campaign to remove
     */
    private void removeset(int i) {
        clearListeners();
        controller.removeModel(i);
        noCampains = controller.getModels().size();
        logger.info("button " + i + " was pressed, dataset " + i + " was removed");
        logger.info("removeSet:number of campaigns:" + noCampains);
        switchView(new GraphView(appWindow, filenames),false); //todo:do we need to reload the view?
//      generateCampaigns();
    }


    /**
     * Switches to a new view, optionally clearing all listeners.
     * @param view The new view.
     * @param clearListeners A boolean, true if the listeners should be cleared.
     */
    public void switchView(BaseView view, boolean clearListeners) {
        if (clearListeners) {
            clearListeners();
        }
        appWindow.loadView(view);
    }

    /**
     * Switches to a new view, clearing all listeners.
     * @param view The new view.
     */
    private void switchView(BaseView view) {
        switchView(view,true);


    }


    /**
     * This deletes all the listeners on the models list that were created by this scene.
     */
    public void clearListeners() {
        logger.info("Clearing up filter listeners");
        for (Filter filter : filters) {
            System.out.println("Filter to remove " + filter);
            System.out.println("listener to remove " + filter.getListener());
            controller.getModels().removeListener(filter.getListener());
        }
    }

    /**
     * Removes a passed filter from the filter list and as a listener to the models.
     * @param f The filter to remove, which contains the listener to remove from the models.
     */
    public void removeFilter(Filter f) {
        controller.getModels().removeListener(f.getListener());
        filters.remove(f);
        Histogram histogram1 = null;
        for(Histogram histogram : histograms){
            if(histogram.getIndex() == f.getId()){
                histogram1 = histogram;
            }
        }
        histograms.remove(histogram1);
        histogramBox.getChildren().remove(histogram1.getChart());
    }
}
