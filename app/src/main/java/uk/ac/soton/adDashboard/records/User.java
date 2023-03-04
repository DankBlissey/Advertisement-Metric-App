package uk.ac.soton.adDashboard.records;

import uk.ac.soton.adDashboard.enums.Context;
import uk.ac.soton.adDashboard.enums.Gender;
import uk.ac.soton.adDashboard.enums.Income;

public class User extends LogRow {


  private final String age;
  private final Gender gender;
  private final Income income;

  public User(long id, String age, String gender, String income) throws Exception {
    super(id);
    this.age = ageSetup(age);
    this.gender = genderSetup(gender);
    this.income = incomeSetup(income);
  }


  public String getAge() {
    return age;
  }

  public Gender getGender() {
    return gender;
  }

  public Income getIncome() {
    return income;
  }

  private Income incomeSetup(String income) throws Exception {
    if (income.equals("Low")) {
      return Income.LOW;
    } else if (income.equals("Medium")) {
      return Income.MEDIUM;
    } else if (income.equals("High")) {
      return Income.HIGH;
    } else {
      throw new Exception("invalid income");
    }
  }


  private Gender genderSetup(String gender) throws Exception {
    if (gender.equals("Female")) {
      return Gender.FEMALE;
    } else if (gender.equals("Prefer not to say")) {
      return Gender.PREFERNOTTOSAY;
    } else if (gender.equals("Other")) {
      return Gender.OTHER;
    } else if (gender.equals("Non-binary")) {
      return Gender.NONBINARY;
    } else if (gender.equals("Male")) {
      return Gender.MALE;
    } else {
      throw new Exception("gender setup failed");
    }
  }

  private String ageSetup(String age) throws Exception {
    if (age.equals("<25")) {
      return age;
    } else if (age.equals("25-34")) {
      return age;
    } else if (age.equals("35-44")) {
      return age;
    } else if (age.equals("45-54")) {
      return age;
    } else if (age.equals(">54")) {
      return age;
    } else {
      throw new Exception("invalid age");
    }
  }
}
