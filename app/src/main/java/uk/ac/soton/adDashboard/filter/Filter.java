package uk.ac.soton.adDashboard.filter;

import java.time.LocalDateTime;
import uk.ac.soton.adDashboard.enums.Context;
import uk.ac.soton.adDashboard.enums.Gender;
import uk.ac.soton.adDashboard.enums.Income;
import uk.ac.soton.adDashboard.enums.Stat;

public class Filter {

  Stat stat = Stat.ANY;
  Context context = Context.ANY;
  String age = "";
  Income income = Income.ANY;
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
}
