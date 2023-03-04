package uk.ac.soton.adDashboard.views;

import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.adDashboard.ui.AppPane;
import uk.ac.soton.adDashboard.ui.AppWindow;

/**
 * The first view where you have to upload 3 CSV files
 */
public class LandingView extends BaseView {

    private static final Logger logger = LogManager.getLogger(LandingView.class);

    /**
     * Create a landing view
     * @param appWindow the App Window this will be displayed in
     */
    public LandingView(AppWindow appWindow) {
        super(appWindow);
        logger.info("Creating the Landing View");
    }

    /**
     * Build the Landing layout
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new AppPane(appWindow.getWidth(), appWindow.getHeight());

        /*
        Text title = new Text("Dashboard");
        title.getStyleClass().add("title");

        Text subtitle = new Text("In order to begin, please upload the relevant CSV files below.");
        subtitle.getStyleClass().add("subtitle");

        VBox vbox = new VBox(title, subtitle);

        BorderPane borderPane = new BorderPane(vbox);


           root.getChildren().add(borderPane);
        */

        var menuPane = new StackPane();
        menuPane.setMaxWidth(appWindow.getWidth());
        menuPane.setMaxHeight(appWindow.getHeight());
        menuPane.getStyleClass().add("menu-background");
        root.getChildren().add(menuPane);
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
