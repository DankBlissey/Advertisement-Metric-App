package uk.ac.soton.adDashboard.views;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import uk.ac.soton.adDashboard.controller.Controller;
import uk.ac.soton.adDashboard.ui.AppPane;
import uk.ac.soton.adDashboard.ui.AppWindow;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

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

    protected Controller controller;

    /**
     * Create a new scene/view, passing in the AppWindow the scene will be displayed in
     * @param appWindow the game window
     */
    public BaseView(AppWindow appWindow) {
        this.appWindow = appWindow;
        controller = AppWindow.getController();
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
        if(appWindow.getDarkMode()) {
            view.getStylesheets().add(getClass().getResource("/style/DarkApp.css").toExternalForm());
        } else {
            view.getStylesheets().add(getClass().getResource("/style/app.css").toExternalForm());
        }
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

    public void ScreenShot(Node node) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose where to save");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Portable Network Graphics file", "*.png")
        );
        File selectedFile = fileChooser.showSaveDialog(view.getWindow());

        WritableImage snap = node.snapshot(null,null);

        if(selectedFile != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(snap,null), "png", selectedFile);
            } catch (IOException e) {
                System.err.println("Error with saving image");
            }
        }

        

    }


}
