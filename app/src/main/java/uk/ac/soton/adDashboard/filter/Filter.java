package uk.ac.soton.adDashboard.filter;

import java.time.LocalDateTime;
import uk.ac.soton.adDashboard.enums.Context;
import uk.ac.soton.adDashboard.enums.Gender;
import uk.ac.soton.adDashboard.enums.Income;
import uk.ac.soton.adDashboard.enums.Stat;

/**
 * Represents a set of filters.
 */
public class Filter {
  /**
   * The filter for the type of data to calculate which is the enum ANY if no filter is applied.
   */
  Stat stat = Stat.ANY;
  /**
   * The context filter which is the enum ANY if no filter is applied.
   */
  Context context = Context.ANY;
  /**
   * The age filter which is the empty string if no filter is applied.
   */
  String age = "";
  /**
   * The income filter which is the enum ANY if no filter is applied.
   */
  Income income = Income.ANY;
  /**
   * The gender filter which is the enum ANY if no filter is applied.
   */
  Gender gender = Gender.ANY;
  LocalDateTime startDate = null;
  LocalDateTime endDate = null;

  public Context getContext() {
    return context;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  public String getAge() {
    return age;

  }

  public void setAge(String age) {
    this.age = age;
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


  public Stat getStat() {
    return stat;
  }

  public void setStat(Stat stat) {
    this.stat = stat;
  }
}

