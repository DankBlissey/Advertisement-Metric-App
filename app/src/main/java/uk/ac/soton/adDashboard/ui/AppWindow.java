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
import uk.ac.soton.adDashboard.records.DataSet;
import uk.ac.soton.adDashboard.views.BaseView;
import uk.ac.soton.adDashboard.views.BounceRateView;
import uk.ac.soton.adDashboard.views.LandingView;

/**
 * The AppWindow is the single window for the app where everything takes place. To move between screens in the app,
 * we simply change the scene aka view in our app.
 *
 * The appWindow has methods to launch each of the different parts of the app by switching scenes (aka views). You can add more
 * methods here to add more screens to the app.
 */
public class AppWindow {
    private static final Logger logger = LogManager.getLogger(AppWindow.class);

    private final int width;
    private final int height;

    private final Stage stage;

    private BaseView currentView;
    private Scene view;

    /**
     * Create a new appWindow attached to the given stage with the specified width and height
     * @param stage stage
     * @param width width
     * @param height height
     */
    public AppWindow(Stage stage, int width, int height) {
        this.width = width;
        this.height = height;

        this.stage = stage;

        //Setup window
        setupStage();

        //Setup resources
        setupResources();

        //Setup default scene/view
        setupDefaultView();

        //Go to first menu (upload 3 CSV files)
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
     * Setup the default view/scene (an empty black scene) when no scene is loaded
     */
    public void setupDefaultView() {
        this.view = new Scene(new Pane(), width, height, Color.BLACK);
        stage.setScene(this.view);
    }

    /**
     * Display the first menu(scene/view) where you upload three CSV files
     */
    public void uploadCSVWindow() {
        loadView(new LandingView(this));
    }

    /**
     * Display the second menu(scene/view) where you select the bounce rate
     */
    public void bounceRateWindow(DataSet dataSet) {
        loadView(new BounceRateView(this, dataSet));
    }

    /**
     * Load a given scene which extends BaseView and switch over.
     * @param newView new scene to load
     */
    public void loadView(BaseView newView) {
        //Cleanup remains of the previous scene
        cleanup();

        //Create the new scene/view and set it up
        newView.build();
        currentView = newView;
        view = newView.setScene();
        stage.setScene(view);

        //Initialise the scene when ready
        Platform.runLater(() -> currentView.initialise());
    }

    /**
     * When switching scenes/views, perform any cleanup needed, such as removing previous listeners
     */
    public void cleanup() {
        logger.info("Clearing up previous scene");
        // Clear listeners here if using any
    }

    /**
     * Get the current scene/view being displayed
     * @return scene
     */
    public Scene getView() {
        return view;
    }

    /**
     * Get the width of the App Window
     * @return width
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Get the height of the App Window
     * @return height
     */
    public int getHeight() {
        return this.height;
    }
}
