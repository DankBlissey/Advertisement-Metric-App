package uk.ac.soton.adDashboard.views;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Font;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.adDashboard.controller.Controller;
import uk.ac.soton.adDashboard.records.DataSet;
import uk.ac.soton.adDashboard.ui.AppPane;
import uk.ac.soton.adDashboard.ui.AppWindow;

public class ListView extends BaseView {
    private static final Logger logger = LogManager.getLogger(ListView.class);

    protected DataSet dataSet;

    /**
     * App class (logic)
     */
    protected Controller controller;

    public ListView(AppWindow appWindow, DataSet dataset) {
        super(appWindow);
        this.dataSet = dataset;
        logger.info("Creating the list view View");
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

        //empty space so that dashboard is on top left and top buttons are on the top right
        Region region = new Region();

        //button for going back to the input screen
        Button startAgain = new Button("Start Again");
        startAgain.getStyleClass().add("blueButton");
        startAgain.setOnAction(e -> {
            appWindow.bounceRateWindow(dataSet);
        });

        //drop down button for dark and light theme
        MenuButton theme = new MenuButton("Theme");
        theme.getStyleClass().add("blueButton");
        MenuItem light = new MenuItem("Light");
        MenuItem dark = new MenuItem("Dark");
        theme.getItems().addAll(light, dark);
        light.setOnAction(e -> {
            appWindow.setDarkMode(false);
            appWindow.listViewWindow(dataSet);
        });
        dark.setOnAction(e -> {
            appWindow.setDarkMode(true);
            appWindow.listViewWindow(dataSet);
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

        Text loadedText = new Text("Impressions_log.csv");
        loadedText.getStyleClass().add("smallWhiteText");

        StackPane loadedFiles = new StackPane(loadedRectangle,loadedText);

        HBox longBarContent = new HBox(loadedFiles);

        longBarContent.setAlignment(Pos.CENTER);

        StackPane longBar = new StackPane(backBar,longBarContent);

        VBox vbox = new VBox(hbox, longBar);
///

        Color switchBack = Color.web("#4B51FF"); // create a Color object with the hex value for purple
        Color switchToggle = Color.web("4b0076");
        Color backgroundPane = Color.web("#F6F6F6");
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(3);
        //dropShadow.setSpread(0.05);
        dropShadow.setColor(Color.LIGHTGRAY);

        BorderPane borderPane = new BorderPane();

        borderPane.getStyleClass().add("apppane");

        borderPane.setTop(vbox);
        borderPane.setBackground(new Background(new BackgroundFill(
                backgroundPane, CornerRadii.EMPTY, Insets.EMPTY)));


        GridPane gridPane = new GridPane();
        borderPane.setCenter(gridPane);
      //  gridPane.setGridLinesVisible(true);
        gridPane.setAlignment(Pos.CENTER  );
        gridPane.setHgap(20);
        gridPane.setVgap(20);

        // Create a button and add it to a pane
      //  Button button = new Button("Switch to Scene 2");
        //gridPane.add(button,0,0);
        //button.setOnAction(e -> appWindow.loadScene(new GraphView(appWindow)));

      //  SwitchButton switchButton = new SwitchButton();
       //switchButton.setOnDragDone(e -> appWindow.loadScene(new GraphView(appWindow)));



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
        //toggle.setTranslateX(switchedOn ? 10 : -10);
        stack.getChildren().addAll(background, toggle, text);
        stack.setAlignment(Pos.CENTER_LEFT);

        stack.setOnMouseClicked(event -> {
          //  switchedOn = !switchedOn;
          //  toggle.setTranslateX(switchedOn ? 30 : -30);
            appWindow.loadView(new GraphView(appWindow));
        });
        gridPane.add(stack,0,0);



        Text totalClicks = new Text("  Total clicks");
        totalClicks.getStyleClass().add("listTitle");
        Text clicks1 = new Text(" 224,332");
        clicks1.getStyleClass().add("listNumbers");
        Text clicks2 = new Text(" 434,351");
        clicks2.getStyleClass().add("listNumbers");
        VBox clicksBox = new VBox(totalClicks,clicks1,clicks2);
        clicksBox.setAlignment(Pos.CENTER_LEFT);
        Rectangle clickBG = new Rectangle(140,100);
        clickBG.getStyleClass().add("card");
        clickBG.setOpacity(0.98);
        clickBG.setArcWidth(30);
        clickBG.setArcHeight(30);
        clickBG.setEffect(dropShadow);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(clickBG,0,1);
        gridPane.add(clicksBox, 0, 1);



        Text totalUniques = new Text("  Total uniques");
        totalUniques.getStyleClass().add("listTitle");
        Text uniques1 = new Text(" 194,754");
        uniques1.getStyleClass().add("listNumbers");
        Text uniques2 = new Text(" 398,112");
        uniques2.getStyleClass().add("listNumbers");
        VBox uniquesBox = new VBox(totalUniques,uniques1,uniques2);
        Rectangle totalBG = new Rectangle(140,100);
        totalBG.getStyleClass().add("card");
        totalBG.setOpacity(0.98);
        totalBG.setArcWidth(30);
        totalBG.setArcHeight(30);
        totalBG.setEffect(dropShadow);
        gridPane.add(totalBG,1,1);
        uniquesBox.setAlignment(Pos.CENTER_LEFT);
        gridPane.add(uniquesBox, 1, 1);

        Text totalImpressions = new Text("  Total impressions");
        totalImpressions.getStyleClass().add("listTitle");
        Text impressions1 = new Text(" 4,560,452");
        impressions1.getStyleClass().add("listNumbers");
        Text impressions2 = new Text(" 12,424,583");
        impressions2.getStyleClass().add("listNumbers");
        VBox impressionsBox = new VBox(totalImpressions,impressions1,impressions2);
        impressionsBox.setAlignment(Pos.CENTER_LEFT);
        Rectangle impressionBG = new Rectangle(140,100);
        impressionBG.getStyleClass().add("card");
        impressionBG.setOpacity(0.98);
        impressionBG.setArcWidth(30);
        impressionBG.setArcHeight(30);
        impressionBG.setEffect(dropShadow);
        gridPane.add(impressionBG,2,1);
        gridPane.add(impressionsBox, 2, 1);

        Text totalBounces = new Text("  Total bounces");
        totalBounces.getStyleClass().add("listTitle");
        Text bounces1 = new Text(" 4,560,452");
        bounces1.getStyleClass().add("listNumbers");
        Text bounces2 = new Text(" 12,424,583");
        bounces2.getStyleClass().add("listNumbers");
        VBox bounceBox = new VBox(totalBounces,bounces1,bounces2);
        bounceBox.setAlignment(Pos.CENTER_LEFT);
        Rectangle bounceBG = new Rectangle(140,100);
        bounceBG.getStyleClass().add("card");
        bounceBG.setOpacity(0.98);
        bounceBG.setArcWidth(30);
        bounceBG.setArcHeight(30);
        bounceBG.setEffect(dropShadow);
        gridPane.add(bounceBG,0,2);
        gridPane.add(bounceBox, 0, 2);

        Text totalConversions = new Text("  Total conversions");
        totalConversions.getStyleClass().add("listTitle");
        Text converions1 = new Text(" 56,134");
        converions1.getStyleClass().add("listNumbers");
        Text converions2 = new Text(" 77,396");
        converions2.getStyleClass().add("listNumbers");
        VBox convesionsBox = new VBox(totalConversions,converions1,converions2);
        convesionsBox.setAlignment(Pos.CENTER_LEFT);
        Rectangle conversionBG = new Rectangle(140,100);
        conversionBG.getStyleClass().add("card");
        conversionBG.setOpacity(0.98);
        conversionBG.setArcWidth(30);
        conversionBG.setArcHeight(30);
        conversionBG.setEffect(dropShadow);
        gridPane.add(conversionBG,1,2);
        gridPane.add(convesionsBox, 1, 2);

        Text totalCost = new Text("  Total cost");
        totalCost.getStyleClass().add("listTitle");
        Text cost1 = new Text(" 405,532");
        cost1.getStyleClass().add("listNumbers");
        Text cost2 = new Text(" 77,396");
        cost2.getStyleClass().add("listNumbers");
        VBox costBox = new VBox(totalCost,cost1,cost2);
        costBox.setAlignment(Pos.CENTER_LEFT);
        Rectangle costBG = new Rectangle(140,100);
        costBG.getStyleClass().add("card");
        costBG.setOpacity(0.98);
        costBG.setArcWidth(30);
        costBG.setArcHeight(30);
        costBG.setEffect(dropShadow);
        gridPane.add(costBG,2,2);
        gridPane.add(costBox, 2, 2);

        Text totalCTR = new Text("  CTR");
        totalCTR.getStyleClass().add("listTitle");
        Text ctr1 = new Text(" 12.4%");
        ctr1.getStyleClass().add("listNumbers");
        Text ctr2 = new Text(" 7.3%");
        ctr2.getStyleClass().add("listNumbers");
        VBox ctrBox = new VBox(totalCTR,ctr1,ctr2);
        ctrBox.setAlignment(Pos.CENTER_LEFT);
        Rectangle ctrBG = new Rectangle(140,100);
        ctrBG.getStyleClass().add("card");
        ctrBG.setOpacity(0.98);
        ctrBG.setArcWidth(30);
        ctrBG.setArcHeight(30);
        ctrBG.setEffect(dropShadow);
        gridPane.add(ctrBG,3,2);
        gridPane.add(ctrBox, 3, 2);

        Text totalCPA = new Text("  CPA");
        totalCPA.getStyleClass().add("listTitle");
        Text cpa1 = new Text(" 11.42");
        cpa1.getStyleClass().add("listNumbers");
        Text cpa2 = new Text(" 12.32");
        cpa2.getStyleClass().add("listNumbers");
        VBox cpaBox = new VBox(totalCPA,cpa1,cpa2);
        cpaBox.setAlignment(Pos.CENTER_LEFT);
        Rectangle cpaBG = new Rectangle(140,100);
        cpaBG.getStyleClass().add("card");
        cpaBG.setOpacity(0.98);
        cpaBG.setArcWidth(30);
        cpaBG.setArcHeight(30);
        cpaBG.setEffect(dropShadow);
        gridPane.add(cpaBG,0,3);
        gridPane.add(cpaBox, 0, 3);

        Text totalCPC = new Text("  CPC");
        totalCPC.getStyleClass().add("listTitle");
        Text cpc1 = new Text(" 5.43");
        cpc1.getStyleClass().add("listNumbers");
        Text cpc2 = new Text(" 3.24");
        cpc2.getStyleClass().add("listNumbers");
        VBox cpcBox = new VBox(totalCPC,cpc1,cpc2);
        cpcBox.setAlignment(Pos.CENTER_LEFT);
        Rectangle cpcBG = new Rectangle(140,100);
        cpcBG.getStyleClass().add("card");
        cpcBG.setOpacity(0.98);
        cpcBG.setArcWidth(30);
        cpcBG.setArcHeight(30);
        cpcBG.setEffect(dropShadow);
        gridPane.add(cpcBG,1,3);
        gridPane.add(cpcBox, 1, 3);

        Text totalCPM = new Text("  CPM");
        totalCPM.getStyleClass().add("listTitle");
        Text cpm1 = new Text(" 12,043");
        cpm1.getStyleClass().add("listNumbers");
        Text cpm2 = new Text(" 16,425");
        cpm2.getStyleClass().add("listNumbers");
        VBox cpmBox = new VBox(totalCPM,cpm1,cpm2);
        cpmBox.setAlignment(Pos.CENTER_LEFT);
        Rectangle cpmBG = new Rectangle(140,100);
        cpmBG.getStyleClass().add("card");
        cpmBG.setOpacity(0.98);
        cpmBG.setArcWidth(30);
        cpmBG.setArcHeight(30);
        cpmBG.setEffect(dropShadow);
        gridPane.add(cpmBG,2,3);
        gridPane.add(cpmBox, 2, 3);

        Text totalBounceRate = new Text("  Bounce rate");
        totalBounceRate.getStyleClass().add("listTitle");
        Text bounceRate1 = new Text(" 8.54");
        bounceRate1.getStyleClass().add("listNumbers");
        Text bounceRate2 = new Text(" 13.56");
        bounceRate2.getStyleClass().add("listNumbers");
        VBox bounceRateBox = new VBox(totalBounceRate,bounceRate1,bounceRate2);
        bounceRateBox.setAlignment(Pos.CENTER_LEFT);
        Rectangle bounceRateBG = new Rectangle(140,100);
        bounceRateBG.getStyleClass().add("card");
        bounceRateBG.setOpacity(0.98);
        bounceRateBG.setArcWidth(30);
        bounceRateBG.setArcHeight(30);
        bounceRateBG.setEffect(dropShadow);
        gridPane.add(bounceRateBG,3,3);
        gridPane.add(bounceRateBox, 3, 3);

        root.getChildren().addAll(borderPane);
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


