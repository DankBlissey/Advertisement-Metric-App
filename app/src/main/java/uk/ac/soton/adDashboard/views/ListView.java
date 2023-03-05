package uk.ac.soton.adDashboard.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.adDashboard.controller.Controller;
import uk.ac.soton.adDashboard.ui.AppPane;
import uk.ac.soton.adDashboard.ui.AppWindow;

public class ListView extends BaseView {
    private static final Logger logger = LogManager.getLogger(ListView.class);

    /**
     * App class (logic)
     */
    protected Controller controller;

    public ListView(AppWindow appWindow) {
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

        root.getChildren().add(borderPane);

        GridPane gridPane = new GridPane();

        // set some padding for the gridPane
        gridPane.setPadding(new Insets(10));

        // set the grid lines visible for debugging purposes
        gridPane.setGridLinesVisible(true);

        borderPane.setCenter(gridPane);
        Text title2 = new Text("title");
        VBox vbox2 = new VBox(title2);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPrefWidth(8000);
        gridPane.setPrefHeight(8000);
        // add some colored rectangles to the GridPane for demonstration purposes
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                Rectangle rectangle = new Rectangle(50, 50, Color.GRAY);
                StackPane stackPane = new StackPane(rectangle);
                gridPane.add(stackPane, col, row);
            }
        }
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