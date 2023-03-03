package uk.ac.soton.adDashboard.views;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
