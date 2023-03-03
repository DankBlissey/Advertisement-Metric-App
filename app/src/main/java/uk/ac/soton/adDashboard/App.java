package uk.ac.soton.adDashboard;

import javafx.application.Application;
import javafx.stage.Stage;
import uk.ac.soton.adDashboard.ui.AppWindow;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * JavaFX App
 */
public class App extends Application {

    /**
     * Base resolution width
     */
    private final int width = 800;

    /**
     * Base resolution height
     */
    private final int height = 600;

    private static App instance;
    private static final Logger logger = LogManager.getLogger(App.class);
    private Stage stage;

    /**
     * Start the app
     * @param args commandline arguments
     */
    public static void main(String[] args) {
        logger.info("Starting client");
        launch();
    }

    /**
     * Called by JavaFX with the primary stage as a parameter. Begins the app by opening the App Window
     * @param stage the default stage, main window
     */
    @Override
    public void start(Stage stage) {
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        instance = this;
        this.stage = stage;
        openApp();
    }

    /**
     * Create the AppWindow with the specified width and height
     */
    public void openApp() {
        logger.info("Opening app window");

        //Change the width and height in this class to change the base rendering resolution for all app parts
        var appWindow = new AppWindow(stage, width, height);

        //Display the appWindow
        stage.show();
    }

    /**
     * Shutdown the app
     */
    public void shutdown() {
        logger.info("Shutting down");
        System.exit(0);
    }

    /**
     * Get the singleton App instance
     * @return the app
     */
    public static App getInstance() {
        return instance;
    }
}