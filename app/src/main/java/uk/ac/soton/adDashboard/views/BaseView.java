package uk.ac.soton.adDashboard.views;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import uk.ac.soton.adDashboard.ui.AppPane;
import uk.ac.soton.adDashboard.ui.AppWindow;

/**
 * A Base Scene/view used in the app. Handles common functionality between all scenes.
 */
public abstract class BaseView {
    /**
     * App Window
     */
    protected final AppWindow appWindow;

    protected AppPane root;

    protected Scene view;

    /**
     * Create a new scene/view, passing in the AppWindow the scene will be displayed in
     * @param appWindow the game window
     */
    public BaseView(AppWindow appWindow) {
        this.appWindow = appWindow;
    }

    /**
     * Initialise this scene/view. Called after creation
     */
    public abstract void initialise();

    /**
     * Build the layout of the scene/view
     */
    public abstract void build();

    /**
     * Create a new JavaFX scene (aka view for us) using the root contained within this scene
     * @return JavaFX scene
     */
    public Scene setScene() {
        var previous = appWindow.getView();
        Scene view = new Scene(root, previous.getWidth(), previous.getHeight(), Color.BLUEVIOLET);
        view.getStylesheets().add(getClass().getResource("/style/app.css").toExternalForm());
        this.view = view;
        return view;
    }

    /**
     * Get the JavaFX scene (aka view for us) contained inside
     * @return JavaFX scene
     */
    public Scene getView() {
        return this.view;
    }
}
