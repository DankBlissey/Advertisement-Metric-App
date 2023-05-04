package uk.ac.soton.adDashboard.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.adDashboard.records.DataSet;
import uk.ac.soton.adDashboard.ui.AppPane;
import uk.ac.soton.adDashboard.ui.AppWindow;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListView extends BaseView {
    private static final Logger logger = LogManager.getLogger(ListView.class);
    protected DataSet dataSet;
    protected ArrayList<String> filenames;
    private DropShadow dropShadow;
    private GridPane gridPane;
    private double[] data;

    private int noCampains;
    HBox longBarContent;

    public ListView(AppWindow appWindow, ArrayList<String> filenames) {
        super(appWindow);
        this.dataSet = controller.getModel();
        this.filenames = filenames;
        noCampains = controller.getModels().size();
        logger.info("Creating the list view View");
        logger.info("Constructor:number of campaigns:" + noCampains);
    }

    /**
     * Build the Landing Window
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new AppPane(appWindow.getWidth(), appWindow.getHeight());

        //builds the text for the dashboard writing and sets its style class
        Text title = new Text("Dashboard");
        title.getStyleClass().add("mediumText");

        //empty space so that dashboard is on top left and top buttons are on the top right
        Region region = new Region();

        //button for going back to the input screen
        Button startAgain = new Button("Go Back");
        startAgain.getStyleClass().add("blueButton");
        startAgain.setOnAction(e -> {
            appWindow.bounceRateWindow(filenames, true);
        });

        //drop down button for dark and light theme
        MenuButton theme = new MenuButton("Theme");
        theme.getStyleClass().add("menu-item");
        MenuItem light = new MenuItem("Light");
        MenuItem dark = new MenuItem("Dark");
        theme.getItems().addAll(light, dark);
        light.setOnAction(e -> {
            appWindow.setDarkMode(false);
            appWindow.listViewWindow(filenames);
        });
        dark.setOnAction(e -> {
            appWindow.setDarkMode(true);
            appWindow.listViewWindow(filenames);
        });


        Button smaller = new Button("A-");
        smaller.getStyleClass().add("blueButton");
        Button bigger = new Button("A+");
        bigger.getStyleClass().add("blueButton");

        smaller.setOnAction(e -> {
            logger.info("Smaller");
            int currentSize = AppWindow.getController().getFontSize().get();
            if(currentSize != -1) {
                AppWindow.getController().setFontSize(currentSize - 1);
            }
        });
        bigger.setOnAction(e -> {
            logger.info("Bigger");
            int currentSize = AppWindow.getController().getFontSize().get();
            if(currentSize != 1) {
                AppWindow.getController().setFontSize(currentSize + 1);
            }
        });

        HBox sizeButtons = new HBox(smaller,bigger);
        sizeButtons.setAlignment(Pos.CENTER);

        HBox topButtons = new HBox(startAgain, theme, sizeButtons);

        //topButtons.getStyleClass().add("smallText");
        topButtons.setSpacing(10);
        topButtons.setAlignment(Pos.CENTER);
        topButtons.getStyleClass().add("topButtons");
        Rectangle upBar = new Rectangle(1280,50);
        upBar.getStyleClass().add("backBar");

        HBox hbox = new HBox(title, region, topButtons);
        HBox.setHgrow(region, Priority.ALWAYS);
        hbox.getStyleClass().add("top-buttons-fill");
        hbox.setEffect(new DropShadow(10.0,Color.GREY));

        hbox.setAlignment(Pos.CENTER);

        StackPane topBar = new StackPane(upBar,hbox);

        Rectangle backBar = new Rectangle(appWindow.getWidth(),150);
        backBar.getStyleClass().add("backBar");
        backBar.setEffect(new DropShadow(5,Color.GREY));

        //Button to add more campaigns
        Button anotherCampaign = new Button("+ Compare campaigns");
        anotherCampaign.getStyleClass().add("smallBlackText-button");
        anotherCampaign.setOnAction(e -> appWindow.loadView(new AnotherCampaignView(appWindow, this)));

        longBarContent = new HBox();
        generateCampaigns();
        longBarContent.getChildren().add(anotherCampaign);
        longBarContent.setAlignment(Pos.CENTER);
        longBarContent.setSpacing(10);

        StackPane longBar = new StackPane(backBar,longBarContent);


        VBox vbox = new VBox(topBar, longBar);

        //Design for the switch button - changes the scene between list and graph view
        Color switchBack = Color.web("#4B51FF"); // create a Color object with the hex value for purple for the back portion of switch button
        Color switchToggle = Color.web("4b0076"); // create a Color object with the hex value for purple for the toggle portion of switch button

        //Set scene background to grey
        Color backgroundPane = Color.web("#F6F6F6");

        //Making the objects in the view appear more 3d
        dropShadow = new DropShadow();
        dropShadow.setRadius(3);
        dropShadow.setColor(Color.LIGHTGRAY);

        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("apppane");
        borderPane.setTop(vbox);
        borderPane.setBackground(new Background(new BackgroundFill(
                backgroundPane, CornerRadii.EMPTY, Insets.EMPTY)));

        //Using the gridpane component to organise the list view objects
        gridPane = new GridPane();

        //vbox so that button is not part of the list objects
        VBox centerVbox = new VBox(20);
        centerVbox.setMaxWidth(600);

        borderPane.setCenter(centerVbox); // grid pane is set in the centre of the border pane as per storyboard
        //gridPane.setGridLinesVisible(true); //used for debugging
        gridPane.setAlignment(Pos.CENTER );
        gridPane.setHgap(20);
        gridPane.setVgap(20);


        StackPane stack = new StackPane(); // component holding toggle button
        Rectangle background = new Rectangle(100, 30); // setting size of toggle button
        background.setFill(switchBack); // setting colour of switch back
        background.setEffect(dropShadow); // making button appear 3d
        background.setArcWidth(30);
        background.setArcHeight(30);
        Text text = new Text("    List         Graph");
        text.getStyleClass().add("buttonTitle");
        Rectangle toggle = new Rectangle(50, 27, switchToggle);
        toggle.setOpacity(0.13);
        toggle.setStroke(Color.LIGHTGRAY);
        toggle.setStrokeWidth(0.5);
        toggle.setArcWidth(29);
        toggle.setArcHeight(30);
        stack.getChildren().addAll(background, toggle, text);
        VBox toggleButton = new VBox();
        BorderPane.setMargin(toggleButton, new Insets(10, 0, 0, 10));
        toggleButton.getChildren().add(stack);
        stack.setAlignment(Pos.CENTER_LEFT);

        // event handling when the button is pressed
        stack.setOnMouseClicked(event -> {
            appWindow.loadView(new GraphView(appWindow, filenames));
        });

        centerVbox.getChildren().addAll(gridPane);
        borderPane.setLeft(toggleButton);

        BorderPane.setMargin(vbox, new Insets(0, 0, 25, 0));
        if (controller.getModels().size()>1) {
            createListBlock("Key", -1, 3, 1);
        }
        createListBlock("Total clicks", 2, 0, 1 );
        createListBlock("Total uniques", 3, 1,1 );
        createListBlock("Total impressions", 1, 2,1 );
        createListBlock("Total bounces", 4, 0,2 );
        createListBlock("Total conversions", 5, 1,2 );
        createListBlock("Total cost", 6, 2,2 );
        createListBlock("CTR", 7, 3,2 );
        createListBlock("CPA", 8, 0,3 );
        createListBlock("CPC", 9, 1,3 );
        createListBlock("CPM", 10, 2,3 );
        createListBlock("Bounce rate", 11, 3,3 );

        Button saveButton = new Button("Save results");
        saveButton.getStyleClass().add("blueButton");
        saveButton.setOnAction(e -> {
            ScreenShot(centerVbox);
        });

        topButtons.getChildren().add(saveButton);

        backBar.widthProperty().bind(root.widthProperty());

        root.getChildren().addAll(borderPane);

        AppWindow.getController().getFontSize().addListener((obs, oldVal, newVal) -> {
            logger.info("Font-size changed from " + oldVal + " to " + newVal);

            if(newVal.intValue() == -1) {
                title.setStyle(title.getStyle() + "-fx-font-size: 30px;");
                startAgain.setStyle(startAgain.getStyle() + "-fx-font-size: 13px;");
                theme.setStyle(theme.getStyle() + "-fx-font-size: 13px;");
                smaller.setStyle(smaller.getStyle() + "-fx-font-size: 13px;");
                bigger.setStyle(bigger.getStyle() + "-fx-font-size: 13px;");
                saveButton.setStyle(saveButton.getStyle() + "-fx-font-size: 13px;");
                anotherCampaign.setStyle(anotherCampaign.getStyle() + "-fx-font-size: 13px;");
                saveButton.setStyle(saveButton.getStyle() + "-fx-font-size: 13px;");
            }
            else if(newVal.intValue() == 0) {
                title.setStyle(title.getStyle() + "-fx-font-size: 35px;");
                startAgain.setStyle(startAgain.getStyle() + "-fx-font-size: 15px;");
                theme.setStyle(theme.getStyle() + "-fx-font-size: 15px;");
                smaller.setStyle(smaller.getStyle() + "-fx-font-size: 15px;");
                bigger.setStyle(bigger.getStyle() + "-fx-font-size: 15px;");
                saveButton.setStyle(saveButton.getStyle() + "-fx-font-size: 15px;");
                anotherCampaign.setStyle(anotherCampaign.getStyle() + "-fx-font-size: 15px;");
                saveButton.setStyle(saveButton.getStyle() + "-fx-font-size: 15px;");
            }
            else if(newVal.intValue() == 1) {
                title.setStyle(title.getStyle() + "-fx-font-size: 40px;");
                startAgain.setStyle(startAgain.getStyle() + "-fx-font-size: 17px;");
                theme.setStyle(theme.getStyle() + "-fx-font-size: 17px;");
                smaller.setStyle(smaller.getStyle() + "-fx-font-size: 17px;");
                bigger.setStyle(bigger.getStyle() + "-fx-font-size: 17px;");
                saveButton.setStyle(saveButton.getStyle() + "-fx-font-size: 17px;");
                anotherCampaign.setStyle(anotherCampaign.getStyle() + "-fx-font-size: 17px;");
                saveButton.setStyle(saveButton.getStyle() + "-fx-font-size: 17px;");
            }
        });

        AppWindow.getController().setFontSize(AppWindow.getController().getFontSize().get() - 1);
        AppWindow.getController().setFontSize(AppWindow.getController().getFontSize().get() + 1);
    }

    /**
     * Takes the arraylist of filenames and outputs it as a string with line breaks
     * @param fileNames arraylist of filenames
     * @return string of the filenames with \n as linebreaks
     */
    private String getFileNames(ArrayList<String> fileNames) {
        StringBuilder output = new StringBuilder();
        for (String filename : filenames) {
            output.append(filename).append("\n");
        }
        return output.toString();
    }

    /**
     * Initialise the scene and start the app
     */
    @Override
    public void initialise() {
        logger.info("Initialising");
        //Initial stuff such as keyboard listeners
    }

    /**
     * Method for creating each block containing listview data
     * @param text the title of the block
     * @param dataIndex which index to fetch from dataset
     * @param xGrid x position of grid pane
     * @param yGrid y position of grid pane
     */
    private void createListBlock(String text, int dataIndex, int xGrid, int yGrid) {
        Text title = new Text(text);
        VBox vBox = new VBox(title);
        ArrayList<Integer> ids = controller.getModelIds();
        if (dataIndex==-1) {
            title.getStyleClass().add("listKeyTitle");

            for (int idIndex = 0; idIndex < ids.size(); idIndex++) {
                int modelId = ids.get(idIndex);
                Text valueText;
                valueText= new Text("Campaign " + modelId);
                valueText.getStyleClass().add("campaignTitle");
                vBox.getChildren().add(valueText);

                AppWindow.getController().getFontSize().addListener((obs, oldVal, newVal) -> {
                    logger.info("Font-size changed from " + oldVal + " to " + newVal);

                    if(newVal.intValue() == -1) {
                        valueText.setStyle(title.getStyle() + "-fx-font-size: 22px;");
                    }
                    else if(newVal.intValue() == 0) {
                        valueText.setStyle(title.getStyle() + "-fx-font-size: 25px;");
                    }
                    else if(newVal.intValue() == 1) {
                        valueText.setStyle(title.getStyle() + "-fx-font-size: 28px;");
                    }
                });
            }

        } else {
            title.getStyleClass().add("listTitle");


            for (int idIndex = 0; idIndex < ids.size(); idIndex++) {
                int modelId = ids.get(idIndex);
                DataSet dataset = controller.getModel(modelId);
                data = dataset.allStats(dataset.earliestDate(), dataset.latestDate());
                double value = data[dataIndex];
                Text valueText;
                if (text.equals("CTR")) {
                    value = value * 100;
                    DecimalFormat df = new DecimalFormat("#.##");
                    String formattedNumber = df.format(value);
                    valueText = new Text(formattedNumber + "%");
                } else if (text.equals("Bounce rate")) {
                    DecimalFormat df = new DecimalFormat("#,###.##");
                    String formattedNumber = df.format(value);
                    valueText = new Text(formattedNumber);
                } else if (text.equals("CPA") || text.equals("CPC")) {
                    value = value / 100;
                    DecimalFormat df = new DecimalFormat("#,###.##");
                    String formattedNumber = df.format(value);
                    valueText = new Text("£" + formattedNumber);
                } else if (text.equals("CPM") || text.equals("Total cost")) {
                    value = value / 100;
                    DecimalFormat df = new DecimalFormat("#,###");
                    String formattedNumber = df.format(value);
                    valueText = new Text("£" + formattedNumber);
                }
                else {
                    DecimalFormat df = new DecimalFormat("#,###");
                    String formattedNumber = df.format(value);
                    valueText = new Text(formattedNumber);
                }

                valueText.getStyleClass().add("listNumbers");
                vBox.getChildren().add(valueText);

                AppWindow.getController().getFontSize().addListener((obs, oldVal, newVal) -> {
                    logger.info("Font-size changed from " + oldVal + " to " + newVal);

                    if(newVal.intValue() == -1) {
                        valueText.setStyle(title.getStyle() + "-fx-font-size: 22px;");
                    }
                    else if(newVal.intValue() == 0) {
                        valueText.setStyle(title.getStyle() + "-fx-font-size: 25px;");
                    }
                    else if(newVal.intValue() == 1) {
                        valueText.setStyle(title.getStyle() + "-fx-font-size: 28px;");
                    }
                });
            }
        }
        vBox.setPadding(new Insets(10));
        vBox.setBackground(
            new Background(new BackgroundFill(Color.WHITE, new CornerRadii(17), null)));
        vBox.getStyleClass().add("card");
        vBox.setEffect(dropShadow);
        vBox.setAlignment(Pos.CENTER_LEFT);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(vBox, xGrid, yGrid);

        AppWindow.getController().getFontSize().addListener((obs, oldVal, newVal) -> {
            logger.info("Font-size changed from " + oldVal + " to " + newVal);

            if(newVal.intValue() == -1) {
                title.setStyle(title.getStyle() + "-fx-font-size: 13px;");
            }
            else if(newVal.intValue() == 0) {
                title.setStyle(title.getStyle() + "-fx-font-size: 15px;");
            }
            else if(newVal.intValue() == 1) {
                title.setStyle(title.getStyle() + "-fx-font-size: 17px;");
            }
        });
    }

    /**
     * Method for generating the objects listing the loaded files and for which campaign
     */
    private void generateCampaigns(){
        ArrayList<Integer> ids = controller.getModelIds();
        for (int idIndex = 0; idIndex < ids.size(); idIndex++) {
            int modelId = ids.get(idIndex);
                //Box containing first set of loaded files
                Rectangle loadedRectangle = new Rectangle(200, 130, Color.valueOf("#4B51FF"));
                loadedRectangle.setArcWidth(30);
                loadedRectangle.setArcHeight(30);
                int campaignNum = idIndex + 1;
                Text title = new Text("Campaign " + modelId);
                title.getStyleClass().add("smallBlueText");
                Text loadedText = new Text(getFileNames(filenames));
                loadedText.getStyleClass().add("smallWhiteText");

                Button close = new Button("X");
                close.getStyleClass().add("delete-filter-button");

                HBox helperBox = new HBox();
                helperBox.setAlignment(Pos.CENTER_RIGHT);
                helperBox.getChildren().add(close);
                helperBox.setPadding(new Insets(0, 5, 0, 0));

                int finalI = modelId;
                close.setOnAction(e -> removeset(finalI));
                VBox vbox;
                if (ids.size() > 1) {
                    vbox = new VBox(helperBox, title, loadedText);
                } else {
                    vbox = new VBox(title, loadedText);
                }
                vbox.setStyle("-fx-background-color: transparent;");
                vbox.setAlignment(Pos.CENTER);
                StackPane loadedFiles = new StackPane(loadedRectangle, vbox);
                longBarContent.getChildren().add(loadedFiles);
            }
        }


    /**
     * Method to remove a campaign from UI and from the list of models
     * @param i the particular campaign to remove
     */
    private void removeset(int i){
        controller.removeModel(i);
        noCampains = controller.getModels().size();
        logger.info("button " + i + " was pressed, dataset " + i + " was removed");
        logger.info("removeSet:number of campaigns:" + noCampains);
        appWindow.loadView(new ListView(appWindow,filenames));

    }

}


