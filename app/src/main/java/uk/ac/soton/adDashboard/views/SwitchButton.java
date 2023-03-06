package uk.ac.soton.adDashboard.views;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class SwitchButton extends Application {

    private final String ON_STYLE = "-fx-background-color: #4cd964;";
    private final String OFF_STYLE = "-fx-background-color: #dcdcdc;";
    private boolean switchedOn = false;

    @Override
    public void start(Stage primaryStage) {
        StackPane stack = new StackPane();
        Rectangle background = new Rectangle(60, 30, Color.WHITE);
        background.setStroke(Color.LIGHTGRAY);
        background.setStrokeWidth(2);
        Circle circle = new Circle(15, Color.WHITE);
        circle.setStroke(Color.LIGHTGRAY);
        circle.setStrokeWidth(2);
        circle.setTranslateX(switchedOn ? 30 : -30);

        stack.getChildren().addAll(background, circle);
        stack.setAlignment(Pos.CENTER_LEFT);
        stack.setStyle(switchedOn ? ON_STYLE : OFF_STYLE);

        stack.setOnMouseClicked(event -> {
            switchedOn = !switchedOn;
            circle.setTranslateX(switchedOn ? 30 : -30);
            stack.setStyle(switchedOn ? ON_STYLE : OFF_STYLE);
        });

        Scene scene = new Scene(stack, 80, 40);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
