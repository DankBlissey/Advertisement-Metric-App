package uk.ac.soton.adDashboard.views;

import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.adDashboard.controller.Controller;
import uk.ac.soton.adDashboard.ui.AppPane;
import uk.ac.soton.adDashboard.ui.AppWindow;

public class LandingView extends BaseView {
    private static final Logger logger = LogManager.getLogger(LandingView.class);

    /**
     * App class (logic)
     */
    protected Controller controller;

    public LandingView(AppWindow appWindow) {
        super(appWindow);
        logger.info("Creating the Landing View");
    }

    /**
     * Build the Landing Window
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new AppPane(appWindow.getWidth(), appWindow.getHeight());

        Text title = new Text("Dashboard");
        title.getStyleClass().add("title");

        Text subtitle = new Text("In order to begin, please upload the relevant CSV files below.");
        subtitle.getStyleClass().add("subtitle");

        VBox vbox = new VBox(title, subtitle);

        BorderPane borderPane = new BorderPane(vbox);

        root.getChildren().add(borderPane);
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
