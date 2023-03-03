package uk.ac.soton.adDashboard.ui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.adDashboard.App;
import uk.ac.soton.adDashboard.views.BaseView;
import uk.ac.soton.adDashboard.views.LandingView;

public class AppWindow {
    private static final Logger logger = LogManager.getLogger(AppWindow.class);

    private final int width;
    private final int height;

    private final Stage stage;

    private BaseView currentScene;
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
     * Get the current scene being displayed
     * @return scene
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Get the width of the Game Window
     * @return width
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Get the height of the Game Window
     * @return height
     */
    public int getHeight() {
        return this.height;
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

    /**
     * Setup the font and any other resources we need
     */
    private void setupResources() {
        logger.info("Loading resources");

        // Import any fonts or other stuff
        Font.loadFont(getClass().getResourceAsStream("/style/GoogleSans-Regular.ttf"),32);
        Font.loadFont(getClass().getResourceAsStream("/style/GoogleSans-Medium.ttf"),32);
        Font.loadFont(getClass().getResourceAsStream("/style/GoogleSans-MediumItalic.ttf"),32);
        Font.loadFont(getClass().getResourceAsStream("/style/GoogleSans-Italic.ttf"),32);
        Font.loadFont(getClass().getResourceAsStream("/style/GoogleSans-Bold.ttf"),32);
        Font.loadFont(getClass().getResourceAsStream("/style/GoogleSans-BoldItalic.ttf"),32);
    }

    /**
     * Setup the default scene (an empty black scene) when no scene is loaded
     */
    public void setupDefaultScene() {
        this.scene = new Scene(new Pane(), width, height, Color.BLACK);
        stage.setScene(this.scene);
    }

    /**
     * Display the main menu
     */
    public void uploadCSVWindow() {
        loadScene(new LandingView(this));
    }

    /**
     * When switching scenes, perform any cleanup needed, such as removing previous listeners
     */
    public void cleanup() {
        logger.info("Clearing up previous scene");
        // Clear listeners here
    }

    /**
     * Load a given scene which extends BaseScene and switch over.
     * @param newScene new scene to load
     */
    public void loadScene(BaseView newScene) {
        //Cleanup remains of the previous scene
        cleanup();

        //Create the new scene and set it up
        newScene.build();
        currentScene = newScene;
        scene = newScene.setScene();
        stage.setScene(scene);

        //Initialise the scene when ready
        Platform.runLater(() -> currentScene.initialise());
    }
}
