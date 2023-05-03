package uk.ac.soton.adDashboard.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
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
import uk.ac.soton.adDashboard.records.DataSet;
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
        // ---------- Campaign filter ----------
        renderCampaignFilter();
        AppWindow.getController().filterUpdated(filter);

        AppWindow.getController().getFontSize().addListener((obs, oldVal, newVal) -> {
            if(newVal.intValue() == -1) {
                filterSetTitle.setStyle(filterSetTitle.getStyle() + "-fx-font-size: 13px;");
                if(deleteButton != null)
                    deleteButton.setStyle(deleteButton.getStyle() + "-fx-font-size: 8px;");
            }
            else if(newVal.intValue() == 0) {
                filterSetTitle.setStyle(filterSetTitle.getStyle() + "-fx-font-size: 15px;");
                if(deleteButton != null)
                    deleteButton.setStyle(deleteButton.getStyle() + "-fx-font-size: 10px;");
            }
            else if(newVal.intValue() == 1) {
                filterSetTitle.setStyle(filterSetTitle.getStyle() + "-fx-font-size: 17px;");
                if(deleteButton != null)
                    deleteButton.setStyle(deleteButton.getStyle() + "-fx-font-size: 12px;");
            }
        });
    }

    public void renderCampaignFilter() {
        Text title = new Text("Campaign:");
        title.getStyleClass().add("extraSmallWhiteText");

        ComboBox<String> options = new ComboBox<>(FXCollections.observableArrayList(
        controller.getModels().keySet().stream().map(i -> Integer.toString(i))
            .toArray(String[]::new)));
    //If the models change update the options.
        MapChangeListener<? super Integer, ? super DataSet> listener =  new MapChangeListener<Integer, DataSet>() {
            @Override
            public void onChanged(Change<? extends Integer, ? extends DataSet> change) {
                Platform.runLater(
                    () -> {
                        System.out.println("TRIGGER POINT - controller models changed "+ filter);

                        String value = options.getValue();
                        options.setItems(FXCollections.observableArrayList(
                            controller.getModels().keySet().stream().map(i -> Integer.toString(i))
                                .toArray(String[]::new)));
                        if (options.getItems()
                            .contains(value)) { //todo: needs further testing after linking up.
                            options.setValue(value);
                        } else {
                            options.getSelectionModel().selectFirst();
                        }
                    });
            }
        };

        filter.setListener(listener); //vital for ensuring the listener is removed on scene cleanup.
        controller.getModels().addListener(listener);

        options.getSelectionModel().selectFirst();
        options.getStyleClass().add("filter-dropdown");
        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        filterBox.getChildren().addAll(title, options);
        getChildren().add(filterBox);

        //If options changes update the filter.
        options.valueProperty().addListener((observable, oldValue, newValue) -> {
//          System.out.println("options updated");
//          System.out.println("oldValue = " + oldValue + " newValue = " + newValue);
          if (newValue != null) {
            updatedFilter("campaign", newValue);
          }
        });

        AppWindow.getController().getFontSize().addListener((obs, oldVal, newVal) -> {
            if(newVal.intValue() == -1) {
                title.setStyle(title.getStyle() + "-fx-font-size: 8px;");
                options.setStyle(options.getStyle() + "-fx-font-size: 8px;");
            }
            else if(newVal.intValue() == 0) {
                title.setStyle(title.getStyle() + "-fx-font-size: 10px;");
                options.setStyle(options.getStyle() + "-fx-font-size: 10px;");
            }
            else if(newVal.intValue() == 1) {
                title.setStyle(title.getStyle() + "-fx-font-size: 12px;");
                options.setStyle(options.getStyle() + "-fx-font-size: 12px;");
            }
        });
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

        startDate.setValue(filter.getStartDate().toLocalDate());
        endDate.setValue(filter.getEndDate().toLocalDate());

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

        AppWindow.getController().getFontSize().addListener((obs, oldVal, newVal) -> {
            if(newVal.intValue() == -1) {
                from.setStyle(from.getStyle() + "-fx-font-size: 8px;");
                startDate.setStyle(startDate.getStyle() + "-fx-font-size: 8px;");
                to.setStyle(to.getStyle() + "-fx-font-size: 8px;");
                endDate.setStyle(endDate.getStyle() + "-fx-font-size: 8px;");
            }
            else if(newVal.intValue() == 0) {
                from.setStyle(from.getStyle() + "-fx-font-size: 10px;");
                startDate.setStyle(startDate.getStyle() + "-fx-font-size: 10px;");
                to.setStyle(to.getStyle() + "-fx-font-size: 10px;");
                endDate.setStyle(endDate.getStyle() + "-fx-font-size: 10px;");
            }
            else if(newVal.intValue() == 1) {
                from.setStyle(from.getStyle() + "-fx-font-size: 12px;");
                startDate.setStyle(startDate.getStyle() + "-fx-font-size: 12px;");
                to.setStyle(to.getStyle() + "-fx-font-size: 12px;");
                endDate.setStyle(endDate.getStyle() + "-fx-font-size: 12px;");
            }
        });
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

        AppWindow.getController().getFontSize().addListener((obs, oldVal, newVal) -> {
            if(newVal.intValue() == -1) {
                title.setStyle(title.getStyle() + "-fx-font-size: 8px;");
                options.setStyle(options.getStyle() + "-fx-font-size: 8px;");
            }
            else if(newVal.intValue() == 0) {
                title.setStyle(title.getStyle() + "-fx-font-size: 10px;");
                options.setStyle(options.getStyle() + "-fx-font-size: 10px;");
            }
            else if(newVal.intValue() == 1) {
                title.setStyle(title.getStyle() + "-fx-font-size: 12px;");
                options.setStyle(options.getStyle() + "-fx-font-size: 12px;");
            }
        });
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
        } else if (filterType.equals("campaign")) {
            filter.setDataSetId(Integer.parseInt(newValue));
            //TODO: should this call updatedFilter("dateRange", "Custom") to auto match the date range of the new campaign?
        } else if (filterType.equals("dateRange")) {
            LocalDateTime today = LocalDateTime.now();
            LocalDateTime oneWeekAgo = today.minusWeeks(1);
            LocalDateTime oneMonthAgo = today.minusMonths(1);
            LocalDateTime twoMonthsAgo = today.minusMonths(2);
            LocalDateTime oneYearAgo = today.minusYears(1);
            if (!newValue.equals("Custom")) {
                filter.setEndDate(today);
            } else {
                filter.setEndDate(controller.getModel(filter.getDataSetId()).latestDate()); //auto date matching.
            }
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

                filter.setStartDate(controller.getModel(filter.getDataSetId()).earliestDate());
                datePickerVisible(true);
                filter.setStartDate(controller.getModel(filter.getDataSetId()).earliestDate());

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
