package uk.ac.soton.adDashboard.views;

import java.lang.reflect.Array;
import java.util.HashSet;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.adDashboard.records.*;
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

        submitButton.getStyleClass().add("card");

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
        impressionLogButton.getStyleClass().add("card");
        impressionLogButton.setEffect(new DropShadow(5,Color.valueOf("555BFF")));

        var uploadBlock1 = new VBox(impressionLogLabel, impressionLogButton);
        uploadBlock1.setSpacing(10);
        uploadBlock1.setAlignment(Pos.CENTER);

        var clickLogLabel = new Text("Click log");
        var clickLogButton = new Button("Choose file");
        clicksFileName.set("Choose file");
        clickLogButton.textProperty().bind(clicksFileName);
        clickLogButton.getStyleClass().add("card");
        clickLogButton.setEffect(new DropShadow(5,Color.valueOf("555BFF")));

        var uploadBlock2 = new VBox(clickLogLabel, clickLogButton);
        uploadBlock2.setSpacing(10);
        uploadBlock2.setAlignment(Pos.CENTER);

        var serverLogLabel = new Text("Server log");
        var serverLogButton = new Button("Choose file");
        serverFileName.set("Choose file");
        serverLogButton.textProperty().bind(serverFileName);
        serverLogButton.getStyleClass().add("card");
        serverLogButton.setEffect(new DropShadow(5,Color.valueOf("555BFF")));

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

    public void showAlert(String message) {

        ObservableList<Node> children = root.getChildren();
        if(!children.isEmpty()) {
            Node topNode = children.get(children.size() - 1);
            if(topNode instanceof VBox) {
                children.remove(topNode);
            }
        }

        var errorText = new Text("Error");
        var errorInfo = new Text(message);

        errorText.getStyleClass().add("body-text");
        errorInfo.getStyleClass().add("error-text");

        var alertContent = new VBox(5);
        alertContent.getChildren().addAll(errorText, errorInfo);

        root.setAlignment(alertContent, Pos.TOP_CENTER);

        alertContent.getStyleClass().add("alert");
        alertContent.setPrefSize(10,10);
        alertContent.setMaxWidth(20);
        alertContent.setMaxHeight(20);

        root.getChildren().add(alertContent);
        root.setPadding(new Insets(20, 0, 0, 0));
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
        LogRow.setResolver();
        HashMap<Long, User> users = getUsersFromCSV(impressionsFilePath);
        ArrayList<Impression> impressions = getImpressionsFromCSV(impressionsFilePath);
        logger.info("Successfully created objects: impressions("+ impressions.size() + " entries) and users(" + users.size() + ")");

        logger.info("Reading the clicks file");
        ArrayList<Click> clicks = getClicksFromCSV(clickFilePath);
        logger.info("Successfully created object clicks("+ clicks.size() + " entries)");

        logger.info("Reading the server file");
        ArrayList<ServerAccess> serverAccesses = getServerAccessFromCSV(serverFilePath);
        logger.info("Successfully created object serverAccess("+ serverAccesses.size() + " entries)");


        DataSet dataSet = new DataSet();
        dataSet.setClicks(clicks);
        dataSet.setImpressions(impressions);
        dataSet.setUsers(users);
        dataSet.setServerAccess(serverAccesses);


        

        ArrayList<String> filenames = new ArrayList<>();
        filenames.add(impressionsFileName.getValue());
        filenames.add(clicksFileName.getValue());
        filenames.add(serverFileName.getValue());


        appWindow.getController().setModel(dataSet);
        appWindow.bounceRateWindow( filenames);
    }

    public ArrayList<Impression> getImpressionsFromCSV(String filePath) {
        ArrayList<Impression> impressions = new ArrayList<>();
        String line = "";

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            // Skips the first line which includes the headers
            try {
                if (!isRightCSVColumns(br.readLine(), "impressions")) {
                    throw new Exception();
                }

                while ((line = br.readLine()) != null) {

                    try {
                        String[] columns = line.split(",");
                        var date = columns[0];
                        var id = columns[1];
                        var context = columns[5];
                        var cost = columns[6];
                        Impression impression = new Impression(date, id, cost, context);
                        impressions.add(impression);
                    } catch (Exception e) {
                        showAlert(e.getMessage());
                        throw new RuntimeException(e);
                    }

                }
            } catch (Exception e) {
                showAlert("Impressions file doesn't have the right format");
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("parsed impressions");

        return impressions;
    }

    /**
     * Reads each line in a csv file that is at the absolute path and returns an ArrayList of each
     * impression and a HashMap of all users
     *
     * @param filePath absolute path
     */
    public HashMap<Long, User> getUsersFromCSV(String filePath) {

        String line = "";

        HashMap<Long, User> users = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            // Skips the first line which includes the headers
            try {
                if (!isRightCSVColumns(br.readLine(), "impressions")) {
                    throw new Exception();
                }
                logger.info("getting users");
                ArrayList<String> rows = new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    rows.add(line);
                }
                logger.info("file read");

                //ArrayList<User> users = new ArrayList<>(); ???
                rows.parallelStream().forEach(string -> {
                    try {
                        String[] columns = string.split(",");
                        //logger.info("Reading the line with ID = " + columns[1] + " and date = " + columns[0]);
                        var id = columns[1];
                        var gender = columns[2];
                        var age = columns[3];
                        var income = columns[4];
                        User user = new User(id, age, gender, income);
                        synchronized (users) {
                            users.put(user.getId(), user);
                        }
                    } catch (Exception e) {
                        showAlert(e.getMessage());
                        throw new RuntimeException(e);
                    }
                });
                logger.info("users parsed");
            } catch (Exception e) {
                showAlert("Impressions file doesn't have the right format");
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
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

            try {
                if (!isRightCSVColumns(br.readLine(), "clicks")) {
                    throw new Exception();
                }

                while ((line = br.readLine()) != null)
                {
                    String[] columns = line.split(",");
                    //logger.info("Reading the line with ID = " + columns[1] + " and date = " + columns[0]);
                    var date = columns[0];
                    var id = columns[1];
                    var cost = columns[2];

                    try {
                        clicks.add(new Click(date, id, cost));
                    } catch (Exception e) {
                        showAlert(e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            } catch (Exception e) {
                showAlert("Clicks file doesn't have the right format");
                throw new RuntimeException(e);
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

            try {
                // Skips the first line which includes the headers
                if (!isRightCSVColumns(br.readLine(), "server")) {
                    throw new Exception();
                }

                while ((line = br.readLine()) != null)
                {
                    String[] columns = line.split(",");
                    //logger.info("Reading the line with ID = " + columns[1] + " and pagesViewed = " + columns[3]);
                    var entryDate = columns[0];
                    var id =columns[1];
                    var exitDate = columns[2];
                    var pagesViewed = columns[3];
                    var conversion = columns[4];

                    try {
                        serverAccess.add(new ServerAccess(entryDate, id, exitDate, pagesViewed, conversion));
                    } catch (Exception e) {
                        showAlert(e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            } catch (Exception e) {
                showAlert("Server log file doesn't have the right format");
                throw new RuntimeException(e);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return serverAccess;
    }

    public Boolean isRightCSVColumns(String line, String fileType) {

        String[] columns = line.split(",");

        logger.info("Checking if the inputted file is of type " + fileType + "for the following line: " + line);

        if(fileType.equals("impressions") && columns.length == 7) {
            if(columns[0].equals("Date") && columns[1].equals("ID") &&
                    columns[2].equals("Gender") && columns[3].equals("Age") &&
                    columns[4].equals("Income") && columns[5].equals("Context") &&
                    columns[6].equals("Impression Cost")) {
                return true;
            }
        } else if (fileType.equals("clicks") && columns.length == 3) {
            if(columns[0].equals("Date") && columns[1].equals("ID") &&
                    columns[2].equals("Click Cost")) {
                return true;
            }
        } else if (fileType.equals("server") && columns.length == 5) {
            if(columns[0].equals("Entry Date") && columns[1].equals("ID") &&
                    columns[2].equals("Exit Date") && columns[3].equals("Pages Viewed") &&
                    columns[4].equals("Conversion")) {
                return true;
            }
        }

        return false;
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
