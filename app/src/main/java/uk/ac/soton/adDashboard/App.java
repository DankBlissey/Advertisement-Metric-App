package uk.ac.soton.adDashboard;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import uk.ac.soton.adDashboard.ui.AppWindow;
import uk.ac.soton.adDashboard.views.UploadCSVs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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

    @Override
    public void start(Stage stage) {
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        instance = this;
        this.stage = stage;
        openApp();
    }

    public void openApp() {
        logger.info("Opening app window");

        var appWindow = new AppWindow(this.stage, width, height);

        //Display the appWindow
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * Shutdown the game
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