package uk.ac.soton.adDashboard.components;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.adDashboard.controller.Controller;
import uk.ac.soton.adDashboard.enums.Context;
import uk.ac.soton.adDashboard.enums.Gender;
import uk.ac.soton.adDashboard.enums.Income;
import uk.ac.soton.adDashboard.ui.AppWindow;

import uk.ac.soton.adDashboard.filter.Filter;
import uk.ac.soton.adDashboard.ui.AppWindow;
import uk.ac.soton.adDashboard.views.GraphView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class FilterSet extends VBox {
    private final Controller controller= AppWindow.getController();
    private static final Logger logger = LogManager.getLogger(GraphView.class);

    private final String dateRangeOptions[] = {"Last week", "Last 30 days", "Last 90 days", "Last 12 months", "Custom"};
    private final String genderOptions[] = {"Any", "Male", "Female"};
    private final String ageOptions[] = {"Any", "<25", "25-34", "35-44", "45-54", ">54"};
    private final String incomeOptions[] = {"Any", "Low", "Medium", "High"};
    private final String contextOptions[] = {"Any", "News", "Shopping", "Social", "Blog", "Hobbies", "Travel"};

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
        updatedFilter("dateRange", "Last week");

        // ---------- Gender filter ----------
        renderFilter("Gender:", genderOptions, "gender");

        // ---------- Age filter ----------
        renderFilter("Age:", ageOptions, "age");

        // ---------- Income filter ----------
        renderFilter("Income:", incomeOptions, "income");

        // ---------- Context filter ----------
        renderFilter("Context:", contextOptions, "context");

        AppWindow.getController().filterUpdated(filter);
    }

    public void renderDatePicker(HBox filterBox) {
        Text from = new Text("from");
        from.getStyleClass().add("extraSmallWhiteText");

        DatePicker startDate = new DatePicker();
        startDate.getStyleClass().add("custom-date-picker");

        Text to = new Text("to");
        to.getStyleClass().add("extraSmallWhiteText");

        DatePicker endDate = new DatePicker();
        endDate.getStyleClass().add("custom-date-picker");

        LocalDate today = LocalDate.now();
        LocalDate oneWeekAgo = today.minusWeeks(1);

        startDate.setValue(oneWeekAgo);
        endDate.setValue(today);

        startDate.setOnAction(e -> {
            LocalDateTime selected = startDate.getValue().atStartOfDay();
            ZoneId zoneId = ZoneId.systemDefault();
            selected = selected.atZone(zoneId).toLocalDateTime();

            updatedFilter("startDate", selected);
        });

        endDate.setOnAction(e -> {
            LocalDateTime selected = endDate.getValue().atStartOfDay();
            ZoneId zoneId = ZoneId.systemDefault();
            selected = selected.atZone(zoneId).toLocalDateTime();

            updatedFilter("endDate", selected);
        });

        from.setVisible(false);
        startDate.setVisible(false);
        to.setVisible(false);
        endDate.setVisible(false);

        filterBox.getChildren().addAll(from, startDate, to, endDate);
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

        if(filterType.equals("dateRange")) {
            renderDatePicker(filterBox);
        }

        getChildren().add(filterBox);
    }

    public void updatedFilter(String filterType, String newValue) {

        if (filterType.equals("gender")) {
            filter.setGender(Gender.parseGender(newValue));
        } else if (filterType.equals("age")) {
            filter.setAge(newValue);
        } else if (filterType.equals("income")) {
            filter.setIncome(Income.parseIncome(newValue));
        }  else if (filterType.equals("context")) {
            filter.setContext(Context.parseContext(newValue));
        } else if (filterType.equals("dateRange")) {
            LocalDateTime today = LocalDateTime.now();
            LocalDateTime oneWeekAgo = today.minusWeeks(1);
            LocalDateTime oneMonthAgo = today.minusMonths(1);
            LocalDateTime twoMonthsAgo = today.minusMonths(2);
            LocalDateTime oneYearAgo = today.minusYears(1);

            filter.setEndDate(today);

            datePickerVisible(false);

            if(newValue.equals("Last week")) {
                filter.setStartDate(oneWeekAgo);
            } else if(newValue.equals("Last 30 days")) {
                filter.setStartDate(oneMonthAgo);
            } else if(newValue.equals("Last 90 days")) {
                filter.setStartDate(twoMonthsAgo);
            } else if(newValue.equals("Last 12 months")) {
                filter.setStartDate(oneYearAgo);
            } else if(newValue.equals("Custom")) {
                filter.setStartDate(oneWeekAgo);
                datePickerVisible(true);
            }
        }
        logger.info("Changed filter " + filterType + " to value: " + newValue);
        AppWindow.getController().filterUpdated(this.filter);
    }

    public void updatedFilter(String filterType, LocalDateTime date) {
        if(filterType.equals("startDate")) {
            filter.setStartDate(date);
        } else if(filterType.equals("endDate")) {
            filter.setEndDate(date);
        }

        logger.info("Changed filter " + filterType + " to value: " + date);
        AppWindow.getController().filterUpdated(this.filter);
    }

    public void datePickerVisible(Boolean visible) {
        HBox dateBox = (HBox) getChildren().get(1);
        Node to = dateBox.getChildren().get(2);
        Node startDate = dateBox.getChildren().get(3);
        Node from = dateBox.getChildren().get(4);
        Node endDate = dateBox.getChildren().get(5);

        if(visible) {
            to.setVisible(true);
            startDate.setVisible(true);
            from.setVisible(true);
            endDate.setVisible(true);
        } else {
            to.setVisible(false);
            startDate.setVisible(false);
            from.setVisible(false);
            endDate.setVisible(false);
        }
    }
}
