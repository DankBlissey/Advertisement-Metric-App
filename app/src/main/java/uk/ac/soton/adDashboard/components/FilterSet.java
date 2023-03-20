package uk.ac.soton.adDashboard.components;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.adDashboard.controller.Controller;
import uk.ac.soton.adDashboard.ui.AppWindow;

import uk.ac.soton.adDashboard.filter.Filter;
import uk.ac.soton.adDashboard.ui.AppWindow;
import uk.ac.soton.adDashboard.views.GraphView;

public class FilterSet extends VBox {
    private final Controller controller= AppWindow.getController();
    private static final Logger logger = LogManager.getLogger(GraphView.class);

    private final String dateRangeOptions[] = {"Last week", "Last 30 days", "Last 90 days", "Last 12 months"};
    private final String genderOptions[] = {"Any", "Male", "Female"};
    private final String ageOptions[] = {"Any", "<25", "25-34", "35-44", "45-54", ">54"};
    private final String incomeOptions[] = {"Any", "Low", "Medium", "High"};
    private final String contextOptions[] = {"Any", "News", "Shopping", "Social", "Media", "Blog", "Hobbies", "Travel"};

    private final Filter filter;

    protected AppWindow appWindow;

    public FilterSet(String title, Filter filter, Button deleteButton, AppWindow appWindow) {
        this.filter = filter;
        this.appWindow = appWindow;

        setSpacing(10);
        setPrefWidth(300);
        getStyleClass().add("filter-set");

        // ---------- Filter Title ----------
        Text filterSetTitle = new Text(title);
        filterSetTitle.getStyleClass().add("smallWhiteText");

        // ---------- Delete button ----------
        if(deleteButton != null) {
            deleteButton.getStyleClass().add("delete-filter-button");
            getChildren().add(deleteButton);

            HBox top = new HBox();
            Region spacer = new Region();
            top.getChildren().addAll(filterSetTitle, spacer, deleteButton);

            HBox.setHgrow(spacer, Priority.ALWAYS);

            getChildren().add(top);
        } else {
            getChildren().add(filterSetTitle);
        }

        // ---------- Date range filter ----------
        renderFilter("Date range:", dateRangeOptions, "dateRange");

        // ---------- Gender filter ----------
        renderFilter("Gender:", genderOptions, "gender");

        // ---------- Age filter ----------
        renderFilter("Age:", ageOptions, "age");

        // ---------- Income filter ----------
        renderFilter("Income:", incomeOptions, "income");

        // ---------- Context filter ----------
        renderFilter("Context:", contextOptions, "context");

        //todo: appWindow.getController().filterUpdated(filter);
    }

    public void renderFilter(String filterTitle, String[] optionsText, String filterType) {
        Text title = new Text(filterTitle);
        title.getStyleClass().add("extraSmallWhiteText");

        ComboBox options = new ComboBox(FXCollections.observableArrayList(optionsText));
        options.getSelectionModel().selectFirst();
        options.getStyleClass().add("filter-dropdown");

        options.valueProperty().addListener((observable, oldValue, newValue) -> {
            updatedFilter(filterType, newValue.toString());
        });

        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        filterBox.getChildren().addAll(title, options);

        getChildren().add(filterBox);
    }

    public void updatedFilter(String filterType, String newValue) {
        logger.info("Changed filter " + filterType + " to value: " + newValue);
        //todo: appWindow.getController().filterUpdated(this.filter);
    }
}
