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
import uk.ac.soton.adDashboard.records.Click;
import uk.ac.soton.adDashboard.records.Impression;
import uk.ac.soton.adDashboard.records.ServerAccess;
import uk.ac.soton.adDashboard.records.User;
import uk.ac.soton.adDashboard.ui.AppPane;
import uk.ac.soton.adDashboard.ui.AppWindow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

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

        logger.info("Reading the impressions file");
        var temp = getImpressionsFromCSV(impressionsFilePath);
        ArrayList<Impression> impressions = temp.getKey();
        HashMap<Long, User> users = temp.getValue();
        logger.info("Successfully created objects: impressions("+ impressions.size() + " entries) and users(" + users.size() + ")");

        logger.info("Reading the clicks file");
        ArrayList<Click> clicks = getClicksFromCSV(clickFilePath);
        logger.info("Successfully created object clicks("+ clicks.size() + " entries)");

        logger.info("Reading the server file");
        ArrayList<ServerAccess> serverAccesses = getServerAccessFromCSV(serverFilePath);
        logger.info("Successfully created object serverAccess("+ serverAccesses.size() + " entries)");
    }

    /**
     * Reads each line in a csv file that is at the absolute path
     * and returns an ArrayList of each impression and a HashMap of all users
     * @param filePath absolute path
     */
    public Pair<ArrayList<Impression>, HashMap<Long, User>> getImpressionsFromCSV(String filePath) {

        String line = "";
        var impressions = new ArrayList<Impression>();
        var users = new HashMap<Long, User>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            // Skips the first line which includes the headers
            br.readLine();

            while ((line = br.readLine()) != null)
            {
                String[] columns = line.split(",");
                //logger.info("Reading the line with ID = " + columns[1] + " and date = " + columns[0]);
                var date = columns[0];
                var id = Long.parseLong(columns[1]);
                var gender = columns[2];
                var age = columns[3];
                var income = columns[4];
                var context = columns[5];
                var cost = Double.parseDouble(columns[6]);

                try {
                    impressions.add(new Impression(date, id, cost, context));
                    users.put(id, new User(id, age, gender, income));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return new Pair<>(impressions, users);
    }

    /**
     * Reads each line in a csv file that is at the absolute path
     * and returns an ArrayList of each click
     * @param filePath absolute path
     */
    public ArrayList<Click> getClicksFromCSV(String filePath) {

        String line = "";
        var clicks = new ArrayList<Click>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            // Skips the first line which includes the headers
            br.readLine();

            while ((line = br.readLine()) != null)
            {
                String[] columns = line.split(",");
                //logger.info("Reading the line with ID = " + columns[1] + " and date = " + columns[0]);
                var date = columns[0];
                var id = Long.parseLong(columns[1]);
                var cost = Double.parseDouble(columns[2]);

                try {
                    clicks.add(new Click(date, id, cost));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return clicks;
    }

    /**
     * Reads each line in a csv file that is at the absolute path
     * and returns an ArrayList of each server
     * @param filePath absolute path
     */
    public ArrayList<ServerAccess> getServerAccessFromCSV(String filePath) {

        String line = "";
        var serverAccess = new ArrayList<ServerAccess>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            // Skips the first line which includes the headers
            br.readLine();

            while ((line = br.readLine()) != null)
            {
                String[] columns = line.split(",");
                //logger.info("Reading the line with ID = " + columns[1] + " and pagesViewed = " + columns[3]);
                var entryDate = columns[0];
                var id = Long.parseLong(columns[1]);
                var exitDate = columns[2];
                var pagesViewed = Integer.parseInt(columns[3]);
                Boolean conversion;

                if(columns[4].equals("Yes")) {
                    conversion = true;
                } else {
                    conversion = false;
                }

                try {
                    serverAccess.add(new ServerAccess(entryDate, id, exitDate, pagesViewed, conversion));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return serverAccess;
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
