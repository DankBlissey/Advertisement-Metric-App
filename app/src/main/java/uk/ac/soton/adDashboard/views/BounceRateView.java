package uk.ac.soton.adDashboard.views;


import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.adDashboard.records.DataSet;
import uk.ac.soton.adDashboard.ui.AppPane;
import uk.ac.soton.adDashboard.ui.AppWindow;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BounceRateView extends BaseView {
    private static final Logger logger = LogManager.getLogger(BounceRateView.class);


    protected DataSet dataSet;
    protected ArrayList<String> filenames;
    private boolean isListView;

    public BounceRateView(AppWindow appWindow, ArrayList<String> filenames, boolean isListView) {
        super(appWindow);
        this.isListView = isListView;
        this.dataSet = controller.getModel();
        this.filenames = filenames;
        logger.info("Creating the BounceRate View");
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

        //Text input for the custom range.
        TextField custom = new TextField();
        custom.setPromptText("Enter value");
        custom.setMaxWidth(100);
        custom.setTranslateX(155);
        custom.setTranslateY(-34);
        custom.visibleProperty().setValue(false);
        custom.getStyleClass().add("white-textbox");

        //Text input for the maximum amount of pages visited.
        TextField pages = new TextField();
        pages.setPromptText("Enter value");
        pages.setMaxWidth(100);
        pages.setTranslateX(155);
        pages.setTranslateY(-22);
        pages.visibleProperty().setValue(false);
        pages.getStyleClass().add("white-textbox");

        // text
        Text text1 = new Text("Time spent on website");
        text1.getStyleClass().add("smallText");

        ComboBox<Object> itemBox = new ComboBox<>();
        itemBox.setTranslateX(270);
        itemBox.setTranslateY(-75);
        itemBox.visibleProperty().setValue(false);

        itemBox.getItems().addAll("Seconds", "Minutes");
        itemBox.setPromptText("Select Units");
        itemBox.getStyleClass().add("bounce-dropdown");

        // text
        Text text2 = new Text("  Number of pages visited");
        text2.getStyleClass().add("smallText");

        Text error = new Text("Invalid input!!");
        error.setVisible(false);
        error.setTranslateY(-400);
        error.getStyleClass().add("smallText");

        CheckBox customCheckBox = new CheckBox();
        customCheckBox.getStyleClass().add("check-box");
        customCheckBox.setTranslateX(-98);
        customCheckBox.setTranslateY(-80);

        CheckBox pageCheckBox = new CheckBox();
        pageCheckBox.getStyleClass().add("check-box");
        pageCheckBox.setTranslateX(-98);
        pageCheckBox.setTranslateY(-75);

        Button finished = new Button();
        finished.setText("Finish");
        finished.setVisible(false);
        finished.setTranslateY(40);
        finished.getStyleClass().add("card");
        finished.setEffect(new DropShadow(5,Color.valueOf("555BFF")));

        Button back = new Button();
        back.setText("Back");
        back.getStyleClass().add("card");
        back.setEffect(new DropShadow(5, Color.valueOf("555BFF")));
        back.setTranslateX(-600);
        back.setTranslateY(-375);
        back.getStyleClass().add("blueButton");

        VBox vbox1 = new VBox(title, subtext1, back);

        VBox vbox3 = new VBox(text2, pages, finished, pageCheckBox, text1, custom, itemBox, customCheckBox,error);

        vbox1.setAlignment(Pos.CENTER);
        vbox3.setAlignment(Pos.CENTER);

        text1.setTranslateY(-40);
        custom.setTranslateY(-60);
        itemBox.setTranslateY(-85);
        customCheckBox.setTranslateY(-110);
        vbox3.setTranslateY(180);

        StackPane stackPane = new StackPane(vbox1, vbox3);

        customCheckBox.setOnAction((event) -> {
            logger.info("customCheckBox selected");

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
            logger.info("pageCheckBox selected");

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
            logger.info("back button clicked");
            appWindow.uploadCSVWindow();
        });

        finished.setOnAction((event) -> {
            logger.info("finished button clicked");

            if(pageCheckBox.selectedProperty().getValue()){
                try{
                    logger.info("Maximum page number read");
                    dataSet.setPagesForBounce(Integer.parseInt(pages.getText()));
                    dataSet.setPagesViewedBounceMetric(true);
                    if(isListView){
                        appWindow.listViewWindow(filenames);
                    }
                    else{
                        appWindow.graphViewWindow(filenames);
                    }

                } catch (NumberFormatException ignored) {}
                TimerTask wait = new TimerTask() {
                    @Override
                    public void run() {
                        error.setVisible(false);
                    }
                };

                Timer time = new Timer();
                error.setVisible(true);
                time.schedule(wait,1000);
            }

            else{
                try {
                    if(itemBox.getValue() != null) {
                        logger.info("Maximum time read");
                        if (itemBox.getValue() == "Seconds") {
                            logger.info("Seconds selected");
                            dataSet.setInterval(Integer.parseInt(custom.getText()));
                        } else {
                            logger.info("Minutes selected");
                            dataSet.setInterval(Integer.parseInt(custom.getText()) * 60);
                        }
                        dataSet.setPagesViewedBounceMetric(false);
                        if(isListView){
                            appWindow.listViewWindow(filenames);
                        }
                        else{
                            appWindow.graphViewWindow(filenames);
                        }

                    }
                } catch (NumberFormatException ignored) {}
                TimerTask wait = new TimerTask() {
                    @Override
                    public void run() {
                        error.setVisible(false);
                    }
                };

                Timer time = new Timer();
                error.setVisible(true);
                time.schedule(wait,1000);
            }
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