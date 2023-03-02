package uk.ac.soton.adDashboard.ui;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.adDashboard.App;

public class AppWindow {
    private static final Logger logger = LogManager.getLogger(AppWindow.class);

    private final int width;
    private final int height;

    private final Stage stage;

    private BaseScene currentScene;
    private Scene scene;

    public AppWindow(Stage stage, int width, int height) {
        this.width = width;
        this.height = height;

        this.stage = stage;

        //Setup window
        setupStage();

        //Setup resources
        setupResources();

        //Setup default scene
        setupDefaultScene();

        //Go to menu
        uploadCSVWindow();
    }

    /**
     * Setup the default settings for the stage itself (the window), such as the title and minimum width and height.
     */
    public void setupStage() {
        stage.setTitle("Ad Auction Dashboard");
        stage.setMinWidth(width);
        stage.setMinHeight(height + 20);
        stage.setOnCloseRequest(ev -> App.getInstance().shutdown());
    }
}
