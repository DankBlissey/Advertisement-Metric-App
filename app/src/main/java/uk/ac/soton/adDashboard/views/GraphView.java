package uk.ac.soton.adDashboard.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.adDashboard.controller.Controller;
import uk.ac.soton.adDashboard.ui.AppPane;
import uk.ac.soton.adDashboard.ui.AppWindow;

public class GraphView extends BaseView {
    private static final Logger logger = LogManager.getLogger(GraphView.class);
    private boolean switchedOn = false;
    /**
     * App class (logic)
     */
    protected Controller controller;

    public GraphView(AppWindow appWindow) {
        super(appWindow);
        logger.info("Creating the graph view View");
    }

    /**
     * Build the Landing Window
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new AppPane(appWindow.getWidth(), appWindow.getHeight());

        Text title = new Text("Your data breakdown");
        title.getStyleClass().add("text");

        Text subTitle = new Text("Graph view");
        subTitle.getStyleClass().add("subtitle");

        VBox vbox = new VBox(title, subTitle);

        vbox.setAlignment(Pos.CENTER);
///

        Color switchBack = Color.web("#4B51FF"); // create a Color object with the hex value for purple
        Color switchToggle = Color.web("4b0076");
        Color backgroundPane = Color.web("#F6F6F6");
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(4);
        dropShadow.setSpread(0.05);
        dropShadow.setColor(Color.GREY);

        BorderPane borderPane = new BorderPane();

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
            appWindow.loadScene(new ListView(appWindow));
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