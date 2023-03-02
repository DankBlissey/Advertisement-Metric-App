package uk.ac.soton.adDashboard.views;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class UploadCSVs extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Label label = new Label("File:");
        TextField tf = new TextField();
        Button btn = new Button("Choose File");
        Button btn2 = new Button("Read from CSV file");
        Label info = new Label();
        info.textProperty().bindBidirectional(this.info);

        btn.setOnAction(e -> {
            FileChooser file = new FileChooser();
            file.setTitle("Open file");
            file.showOpenDialog(stage);
        });

        btn2.setOnAction(e -> {
            readCSV(info);
        });

        HBox root = new HBox();
        root.setSpacing(20);
        root.getChildren().addAll(label,tf,btn,btn2,info);

        var scene = new Scene(root, 640, 480);

    }

    public void readCSV(Label info) {
        String CsvFile = "test.csv";
        String FieldDelimiter = ",";

        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(CsvFile));

            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(FieldDelimiter, -1);
                System.out.println(fields[0]);
            }

            this.info.setValue("File read successfully");


        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException");
            this.info.setValue("File not found");
        } catch (IOException ex) {
            System.out.println("IOException");

            this.info.setValue("IOException");
        }
    }
}
