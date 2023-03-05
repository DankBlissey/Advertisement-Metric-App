package uk.ac.soton.adDashboard.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
        logger.info("Creating the list view View");
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

        Text subTitle = new Text("List view");
        subTitle.getStyleClass().add("subtitle");

        VBox vbox = new VBox(title, subTitle);

        vbox.setAlignment(Pos.CENTER);

        BorderPane borderPane = new BorderPane();

        borderPane.setTop(vbox);
        borderPane.setBackground(new Background(new BackgroundFill(
                Color.rgb(230, 230, 230, 0.9), CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().add(borderPane);

        GridPane gridPane = new GridPane();
        borderPane.setCenter(gridPane);
        gridPane.setAlignment(Pos.CENTER );
        gridPane.setHgap(20);
        gridPane.setVgap(20);

        // Create a button and add it to a pane
     //   Button button = new Button("Switch to Scene 2");
     //   gridPane.add(button,0,0);
     //   button.setOnAction(e -> appWindow.loadScene(new ListView(appWindow)));
        StackPane stack = new StackPane();
        Rectangle background = new Rectangle(100, 30, Color.PURPLE);
        background.setStroke(Color.LIGHTGRAY);
        background.setStrokeWidth(1);
        background.setArcWidth(30);
        background.setArcHeight(30);
        Text text = new Text("List      Graph");
        text.getStyleClass().add("buttonTitle");
        Rectangle toggle = new Rectangle(40, 27, Color.MEDIUMPURPLE);
        toggle.setOpacity(0.4);
        toggle.setStroke(Color.LIGHTGRAY);
        toggle.setStrokeWidth(0.5);
        toggle.setArcWidth(29);
        toggle.setArcHeight(30);
        toggle.setTranslateX(switchedOn ? -30 : 30);

        stack.getChildren().addAll(background, toggle, text);
        stack.setAlignment(Pos.CENTER);


        gridPane.add(stack,0,0);

        stack.setOnMouseClicked(event -> {
            switchedOn = !switchedOn;
            toggle.setTranslateX(switchedOn ? 30 : -30);
             appWindow.loadScene(new ListView(appWindow));
        });

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