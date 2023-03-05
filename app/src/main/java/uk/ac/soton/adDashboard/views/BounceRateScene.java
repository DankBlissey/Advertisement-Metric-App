package uk.ac.soton.adDashboard.views;


import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.adDashboard.controller.Controller;
import uk.ac.soton.adDashboard.ui.AppPane;
import uk.ac.soton.adDashboard.ui.AppWindow;

public class BounceRateScene extends BaseView {
    private static final Logger logger = LogManager.getLogger(BounceRateScene.class);

    /**
     * App class (logic)
     */
    protected Controller controller;

    public BounceRateScene(AppWindow appWindow) {
        super(appWindow);
        logger.info("Creating the Landing View");
    }

    /**
     * Build the Landing Window
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new AppPane(appWindow.getWidth(), appWindow.getHeight());

        Text title = new Text("Almost there!");
        title.getStyleClass().add("title");

        Text subtext1 = new Text("Please define how a bounce is registered");
        subtext1.getStyleClass().add("subtitle");

        Text subtext2 = new Text("This cannot be changed later.");
        subtext2.getStyleClass().add("subtitle");

        //Text input for the custom range.
        TextField custom = new TextField();
        custom.setPromptText("Enter value");
        custom.setMaxWidth(100);
        custom.setTranslateX(155);
        custom.setTranslateY(-34);
        custom.visibleProperty().setValue(false);

        //Text input for the maximum amount of pages visited.
        TextField pages = new TextField();
        pages.setPromptText("Enter value");
        pages.setMaxWidth(100);
        pages.setTranslateX(155);
        pages.setTranslateY(-22);
        pages.visibleProperty().setValue(false);

        // text
        Text text1 = new Text("Time spent on website");
        text1.getStyleClass().add("bounceRateText");

        ComboBox<Object> itemBox = new ComboBox<>();
        itemBox.setTranslateX(270);
        itemBox.setTranslateY(-75);
        itemBox.visibleProperty().setValue(false);

        itemBox.getItems().addAll("Seconds", "Minutes");
        itemBox.setPromptText("Select Units");

        // text
        Text text2 = new Text("Number of pages visited");
        text2.getStyleClass().add("bounceRateText");

        CheckBox customCheckBox = new CheckBox();
        customCheckBox.setTranslateX(-90);
        customCheckBox.setTranslateY(-69);

        CheckBox pageCheckBox = new CheckBox();
        pageCheckBox.setTranslateX(-90);
        pageCheckBox.setTranslateY(-69);

        Button finished = new Button();
        finished.setText("Finish");
        finished.setVisible(false);
        finished.setTranslateY(40);

        Button back = new Button();
        back.setText("Back");
        back.setTranslateX(-615);
        back.setTranslateY(-400);

        VBox vbox1 = new VBox(title, subtext1, subtext2, back);

        VBox vbox3 = new VBox(text2, pages, finished, pageCheckBox, text1, custom, itemBox, customCheckBox);

        vbox1.setAlignment(Pos.CENTER);
        vbox3.setAlignment(Pos.CENTER);

        text1.setTranslateY(-40);
        custom.setTranslateY(-60);
        itemBox.setTranslateY(-85);
        customCheckBox.setTranslateY(-110);
        vbox3.setTranslateY(180);

        StackPane stackPane = new StackPane(vbox1, vbox3);

        customCheckBox.setOnAction((event) -> {

            //selecting the custom check box
            if(customCheckBox.selectedProperty().getValue()){
                custom.visibleProperty().setValue(true);
                itemBox.visibleProperty().setValue(true);
                finished.setVisible(true);
                pages.visibleProperty().setValue(false);
                pageCheckBox.selectedProperty().setValue(false);
            }

            //deselecting the custom check box
            else{
                custom.visibleProperty().setValue(false);
                itemBox.visibleProperty().setValue(false);
                finished.setVisible(false);
            }
        });

        pageCheckBox.setOnAction((event) -> {

            //selecting the page check box
            if(pageCheckBox.selectedProperty().getValue()){
                pages.visibleProperty().setValue(true);
                finished.setVisible(true);
                custom.visibleProperty().setValue(false);
                itemBox.visibleProperty().setValue(false);
                customCheckBox.selectedProperty().setValue(false);
            }

            //deselecting the page check box
            else{
                pages.visibleProperty().setValue(false);
                finished.setVisible(false);
            }
        });

        back.setOnAction((event) -> {
            // add previous scene to here
        });

        finished.setOnAction((event) -> {
            // add next scene to here
        });

        root.getChildren().addAll(stackPane);
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