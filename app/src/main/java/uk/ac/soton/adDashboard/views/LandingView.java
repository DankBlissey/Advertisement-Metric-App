package uk.ac.soton.adDashboard.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.adDashboard.ui.AppPane;
import uk.ac.soton.adDashboard.ui.AppWindow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * The first view where you have to upload 3 CSV files
 */
public class LandingView extends BaseView {

    private static final Logger logger = LogManager.getLogger(LandingView.class);

    final FileChooser fileChooser = new FileChooser();
    final Button submitButton = new Button("Submit");

    private SimpleStringProperty impressionsFileName = new SimpleStringProperty();
    private SimpleStringProperty clicksFileName = new SimpleStringProperty();
    private SimpleStringProperty serverFileName = new SimpleStringProperty();

    private String impressionsFilePath;
    private String clickFilePath;
    private String serverFilePath;

    /**
     * Create a landing view
     * @param appWindow the App Window this will be displayed in
     */
    public LandingView(AppWindow appWindow) {
        super(appWindow);
        logger.info("Creating the Landing View");
    }

    /**
     * Build the Landing layout
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new AppPane(appWindow.getWidth(), appWindow.getHeight());

        StackPane mainPane = new StackPane();
        root.getChildren().add(mainPane);

        // Headings
        var title = new Text("Dashboard");
        var subtitle = new Text("In order to begin, please upload\nthe relevant CSV files below.");

        title.getStyleClass().add("title");
        subtitle.getStyleClass().add("subtitle");

        // Upload buttons
        var impressionLogLabel = new Text("Impression log");
        var impressionLogButton = new Button("Choose file");
        impressionsFileName.set("Choose file");
        impressionLogButton.textProperty().bind(impressionsFileName);

        var uploadBlock1 = new VBox(impressionLogLabel, impressionLogButton);
        uploadBlock1.setSpacing(10);
        uploadBlock1.setAlignment(Pos.CENTER);

        var clickLogLabel = new Text("Click log");
        var clickLogButton = new Button("Choose file");
        clicksFileName.set("Choose file");
        clickLogButton.textProperty().bind(clicksFileName);

        var uploadBlock2 = new VBox(clickLogLabel, clickLogButton);
        uploadBlock2.setSpacing(10);
        uploadBlock2.setAlignment(Pos.CENTER);

        var serverLogLabel = new Text("Server log");
        var serverLogButton = new Button("Choose file");
        serverFileName.set("Choose file");
        serverLogButton.textProperty().bind(serverFileName);

        var uploadBlock3 = new VBox(serverLogLabel, serverLogButton);
        uploadBlock3.setSpacing(10);
        uploadBlock3.setAlignment(Pos.CENTER);

        impressionLogLabel.getStyleClass().add("body-text");
        clickLogLabel.getStyleClass().add("body-text");
        serverLogLabel.getStyleClass().add("body-text");

        var uploadItems = new HBox(uploadBlock1, uploadBlock2, uploadBlock3);
        uploadItems.setSpacing(20);
        uploadItems.setAlignment(Pos.CENTER);

        submitButton.setDisable(true);

        var verticalPane = new VBox(title, subtitle, uploadItems, submitButton);
        verticalPane.setSpacing(10);
        verticalPane.setAlignment(Pos.CENTER);

        mainPane.getChildren().add(verticalPane);
        BorderPane.setAlignment(verticalPane, Pos.TOP_CENTER);

        // Actions for choose file buttons
        impressionLogButton.setOnAction(e -> openFileChooser("impressions"));
        clickLogButton.setOnAction(e -> openFileChooser("clicks"));
        serverLogButton.setOnAction(e -> openFileChooser("server"));
        submitButton.setOnAction(e -> submitCSVFiles());
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
     * Opens file chooser and updates the class variables with the name and the
     * absolute path of the chosen file depending on which button was selected
     * @param option tells the file chooser which "type" of file it is choosing
     */
    public void openFileChooser(String option) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Comma Separated Value Files", "*.csv")
        );
        File selectedFile = fileChooser.showOpenDialog(view.getWindow());

        if (selectedFile != null) {
            var fileName = selectedFile.getName();
            var filePath = selectedFile.getAbsolutePath();
            if (option.equals("impressions")) {
                impressionsFileName.set(fileName);
                impressionsFilePath = filePath;
            } else if (option.equals("clicks")) {
                clicksFileName.set(fileName);
                clickFilePath = filePath;
            } else if (option.equals("server")) {
                serverFileName.set(fileName);
                serverFilePath = filePath;
            }
            logger.info("Chose file " + fileName + " for " + option);
            changed();
        }
    }

    /**
     * Gets called when the submit button is pressed
     */
    public void submitCSVFiles() {
        logger.info("Submitting all 3 uploaded csv files...");
    }

    /**
     * Reads each line in a csv file that is at the absolute path
     * @param fileName absolute path
     */
    public void readCSVFile(String fileName) {
        String line = "";

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            while ((line = br.readLine()) != null)
            {
                String[] columns = line.split(",");    // use comma as separator
                logger.info(columns[0] + " " + columns[1] + " " + columns[2]);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Enables the submit button when all 3 files are chosen
     */
    public void changed() {
        if(impressionsFilePath != null && clickFilePath != null && serverFilePath != null) {
            logger.info("All 3 files were uploaded, enabling submit button");
            submitButton.setDisable(false);
        }
    }
}
