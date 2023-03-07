package uk.ac.soton.adDashboard.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.adDashboard.controller.Controller;
import uk.ac.soton.adDashboard.records.DataSet;
import uk.ac.soton.adDashboard.ui.AppPane;
import uk.ac.soton.adDashboard.ui.AppWindow;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GraphView extends BaseView {
    private static final Logger logger = LogManager.getLogger(GraphView.class);
    private boolean switchedOn = false;
    /**
     * App class (logic)
     */
    protected Controller controller;

    protected DataSet dataSet;
    protected ArrayList<String> filenames;

    public GraphView(AppWindow appWindow, ArrayList<String> filenames) {
        super(appWindow);
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
        dropShadow.setRadius(4);
        dropShadow.setSpread(0.05);
        dropShadow.setColor(Color.GREY);

        BorderPane borderPane = new BorderPane();

        borderPane.getStyleClass().add("apppane");

        borderPane.setTop(vbox);
        borderPane.setBackground(new Background(new BackgroundFill(
                backgroundPane, CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().add(borderPane);

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
        toggle.setTranslateX(switchedOn ? -50 : 50);
        stack.getChildren().addAll(background, toggle, text);
        stack.setAlignment(Pos.CENTER_LEFT);

        stack.setOnMouseClicked(event -> {
              switchedOn = !switchedOn;
              toggle.setTranslateX(switchedOn ? -30 : 30);
            appWindow.loadView(new ListView(appWindow,dataSet,filenames));
        });
        gridPane.add(stack,0,0);


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