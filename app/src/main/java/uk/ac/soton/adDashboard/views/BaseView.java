package uk.ac.soton.adDashboard.views;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import uk.ac.soton.adDashboard.ui.AppPane;
import uk.ac.soton.adDashboard.ui.AppWindow;

public abstract class BaseView {
    /**
     * App Window
     */
    protected final AppWindow appWindow;

    /**
     * App Pane
     */
    protected AppPane root;

    /**
     * App Scene
     */
    protected Scene scene;


    public BaseView(AppWindow appWindow) {
        this.appWindow = appWindow;
    }

    /**
     * Initialise this scene. Called after creation
     */
    public abstract void initialise();

    /**
     * Build the layout of the scene
     */
    public abstract void build();

    /**
     * Create a new JavaFX scene using the root contained within this scene
     * @return JavaFX scene
     */
    public Scene setScene() {
        var previous = appWindow.getScene();
        Scene scene = new Scene(root, previous.getWidth(), previous.getHeight(), Color.BLUEVIOLET);
        scene.getStylesheets().add(getClass().getResource("/style/app.css").toExternalForm());
        this.scene = scene;
        return scene;
    }

    /**
     * Get the JavaFX scene contained inside
     * @return JavaFX scene
     */
    public Scene getScene() {
        return this.scene;
    }
}
