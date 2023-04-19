package uk.ac.soton.adDashboard.filter;

import java.time.LocalDateTime;
import javafx.collections.MapChangeListener;
import uk.ac.soton.adDashboard.enums.Context;
import uk.ac.soton.adDashboard.enums.Gender;
import uk.ac.soton.adDashboard.enums.Income;
import uk.ac.soton.adDashboard.records.DataSet;

/**
 * Represents a set of filters.
 */
public class Filter {

  /**
   * The context filter which is the enum ANY if no filter is applied.
   */
  private Context context = Context.ANY;
  /**
   * The age filter which is the empty string if no filter is applied.
   */
  private String age = "";
  /**
   * The income filter which is the enum ANY if no filter is applied.
   */
  private Income income = Income.ANY;
  /**
   * The gender filter which is the enum ANY if no filter is applied.
   */
  private Gender gender = Gender.ANY;
  private LocalDateTime startDate = null;
  private LocalDateTime endDate = null;

  private int id;

  private int dataSetId=0;

  public MapChangeListener<? super Integer, ? super DataSet> getListener() {
    return listener;
  }

  public void setListener(
      MapChangeListener<? super Integer, ? super DataSet> listener) {
    this.listener = listener;
  }

  private MapChangeListener<? super Integer, ? super DataSet> listener;

  public Context getContext() {
    return context;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  public String getAge() {
    return age;

  }

  /**
   * Sets the age. "Any" as an input sets the age to any age allowed.
   * @param age Age to filter by.
   */
  public void setAge(String age) {
    if (age.equals("Any")) {
      this.age= "";
    } else {
      this.age = age;
    }
  }

  public Income getIncome() {
    return income;
  }

  public void setIncome(Income income) {
    this.income = income;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public LocalDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDateTime startDate) {
    this.startDate = startDate;
  }

  public LocalDateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDateTime endDate) {
    this.endDate = endDate;
  }

  public int getDataSetId() {
    return dataSetId;
  }

  public void setDataSetId(int dataSetId) {
    this.dataSetId = dataSetId;

  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}

