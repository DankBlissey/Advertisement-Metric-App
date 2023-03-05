package uk.ac.soton.adDashboard.views;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.adDashboard.controller.Controller;
import uk.ac.soton.adDashboard.ui.AppPane;
import uk.ac.soton.adDashboard.ui.AppWindow;

public class ListView extends BaseView {
    private static final Logger logger = LogManager.getLogger(ListView.class);

    /**
     * App class (logic)
     */
    protected Controller controller;

    public ListView(AppWindow appWindow) {
        super(appWindow);
        logger.info("Creating the list view View");
    }

    /**
     * Build the Landing Window
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new AppPane(appWindow.getWidth(), appWindow.getHeight());

        Text title = new Text("Your data breakdown");
        title.getStyleClass().add("text");

        Text subTitle = new Text("List view");
        subTitle.getStyleClass().add("subtitle");

        VBox vbox = new VBox(title, subTitle);

        vbox.setAlignment(Pos.CENTER);

        BorderPane borderPane = new BorderPane();

        borderPane.setTop(vbox);
        borderPane.setBackground(new Background(new BackgroundFill(
                Color.rgb(230, 230, 230, 0.9), CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().add(borderPane);

        GridPane gridPane = new GridPane();
        borderPane.setCenter(gridPane);
        gridPane.setAlignment(Pos.CENTER  );
        gridPane.setHgap(20);
        gridPane.setVgap(20);

        // Create a button and add it to a pane
        Button button = new Button("Switch to Scene 2");
        borderPane.getChildren().add(button);



        Text totalClicks = new Text("Total clicks");
        totalClicks.getStyleClass().add("listTitle");
        Text number = new Text("100,000");
        number.getStyleClass().add("listNumbers");
        Text number2 = new Text("100,020");
        number2.getStyleClass().add("listNumbers");
        VBox clicksBox = new VBox(totalClicks,number,number2);
        clicksBox.setAlignment(Pos.TOP_LEFT);
        clicksBox.setBackground(new Background(new BackgroundFill(Color.WHITE,
                CornerRadii.EMPTY, Insets.EMPTY)));
        gridPane.add(clicksBox, 0, 0);

        Text totalUniques = new Text("Total uniques");
        totalUniques.getStyleClass().add("listTitle");
        VBox uniquesBox = new VBox(totalUniques);
        uniquesBox.setAlignment(Pos.TOP_LEFT);
        uniquesBox.setBackground(new Background(new BackgroundFill(Color.WHITE,
                CornerRadii.EMPTY, Insets.EMPTY)));
        gridPane.add(uniquesBox, 1, 0);

        Text totalImpressions = new Text("Total impressions");
        totalImpressions.getStyleClass().add("listTitle");
        VBox impressionsBox = new VBox(totalImpressions);
        impressionsBox.setAlignment(Pos.TOP_LEFT);
        impressionsBox.setBackground(new Background(new BackgroundFill(Color.WHITE,
                CornerRadii.EMPTY, Insets.EMPTY)));
        gridPane.add(impressionsBox, 2, 0);

        Text totalBounces = new Text("Total bounces");
        totalBounces.getStyleClass().add("listTitle");
        VBox bounceBox = new VBox(totalBounces);
        bounceBox.setAlignment(Pos.TOP_LEFT);
        bounceBox.setBackground(new Background(new BackgroundFill(Color.WHITE,
                CornerRadii.EMPTY, Insets.EMPTY)));
        gridPane.add(bounceBox, 0, 1);

        Text totalConversions = new Text("Total conversions");
        totalConversions.getStyleClass().add("listTitle");
        Text converions1 = new Text("100,000");
        converions1.getStyleClass().add("listNumbers");
        Text converions2 = new Text("100,020");
        converions2.getStyleClass().add("listNumbers");
        VBox convesionsBox = new VBox(totalConversions,converions1,converions2);
        convesionsBox.setAlignment(Pos.TOP_LEFT);
        convesionsBox.setBackground(new Background(new BackgroundFill(Color.WHITE,
                CornerRadii.EMPTY, Insets.EMPTY)));
        gridPane.add(convesionsBox, 1, 1);

        Text totalCost = new Text("Total cost");
        totalCost.getStyleClass().add("listTitle");
        Text cost1 = new Text("100,000");
        cost1.getStyleClass().add("listNumbers");
        Text cost2 = new Text("100,020");
        cost2.getStyleClass().add("listNumbers");
        VBox costBox = new VBox(totalCost,cost1,cost2);
        costBox.setAlignment(Pos.TOP_LEFT);
        costBox.setBackground(new Background(new BackgroundFill(Color.WHITE,
                CornerRadii.EMPTY, Insets.EMPTY)));
        gridPane.add(costBox, 2, 1);

        Text totalCTR = new Text("CTR");
        totalCTR.getStyleClass().add("listTitle");
        Text ctr1 = new Text("100,000");
        ctr1.getStyleClass().add("listNumbers");
        Text ctr2 = new Text("100,020");
        ctr2.getStyleClass().add("listNumbers");
        VBox ctrBox = new VBox(totalCTR,ctr1,ctr2);
        ctrBox.setAlignment(Pos.TOP_LEFT);
        ctrBox.setBackground(new Background(new BackgroundFill(Color.WHITE,
                CornerRadii.EMPTY, Insets.EMPTY)));
        gridPane.add(ctrBox, 3, 1);

        Text totalCPA = new Text("CPA");
        totalCPA.getStyleClass().add("listTitle");
        Text cpa1 = new Text("100,000");
        cpa1.getStyleClass().add("listNumbers");
        Text cpa2 = new Text("100,020");
        cpa2.getStyleClass().add("listNumbers");
        VBox cpaBox = new VBox(totalCPA,cpa1,cpa2);
        cpaBox.setAlignment(Pos.TOP_LEFT);
        cpaBox.setBackground(new Background(new BackgroundFill(Color.WHITE,
                CornerRadii.EMPTY, Insets.EMPTY)));
        gridPane.add(cpaBox, 0, 2);

        Text totalCPC = new Text("CPC");
        totalCPC.getStyleClass().add("listTitle");
        Text cpc1 = new Text("100,000");
        cpc1.getStyleClass().add("listNumbers");
        Text cpc2 = new Text("100,020");
        cpc2.getStyleClass().add("listNumbers");
        VBox cpcBox = new VBox(totalCPC,cpc1,cpc2);
        cpcBox.setAlignment(Pos.TOP_LEFT);
        cpcBox.setBackground(new Background(new BackgroundFill(Color.WHITE,
                CornerRadii.EMPTY, Insets.EMPTY)));
        gridPane.add(cpcBox, 1, 2);

        Text totalCPM = new Text("CPM");
        totalCPM.getStyleClass().add("listTitle");
        Text cpm1 = new Text("100,000");
        cpm1.getStyleClass().add("listNumbers");
        Text cpm2 = new Text("100,020");
        cpm2.getStyleClass().add("listNumbers");
        VBox cpmBox = new VBox(totalCPM,cpm1,cpm2);
        cpmBox.setAlignment(Pos.TOP_LEFT);
        cpmBox.setBackground(new Background(new BackgroundFill(Color.WHITE,
                CornerRadii.EMPTY, Insets.EMPTY)));
        gridPane.add(cpmBox, 2, 2);

        Text totalBounceRate = new Text("Bounce rate");
        totalBounceRate.getStyleClass().add("listTitle");
        Text bounceRate1 = new Text("100,000");
        bounceRate1.getStyleClass().add("listNumbers");
        Text bounceRate2 = new Text("100,020");
        cpm2.getStyleClass().add("listNumbers");
        VBox bounceRateBox = new VBox(totalBounceRate,bounceRate1,bounceRate2);
        bounceRateBox.setAlignment(Pos.TOP_LEFT);
        bounceRateBox.setBackground(new Background(new BackgroundFill(Color.WHITE,
                CornerRadii.EMPTY, Insets.EMPTY)));
        gridPane.add(bounceRateBox, 3, 2);
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