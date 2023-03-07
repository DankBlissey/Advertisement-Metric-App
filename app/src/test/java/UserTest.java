package uk.ac.soton.adDashboard.records;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class UserTest {

  @ParameterizedTest
  @ValueSource(strings = {"-1", "-5", "k"})
  void idThrows(String id) {
    assertThrows(Exception.class, () -> new User(id, "<25", "Female", "High"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"25", "54", "26", "-1"})
  void ageThrows(String age) {
    assertThrows(Exception.class, () -> new User("25", age, "Female", "High"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"25", "20000", "15,000", "$30000"})
  void incomeThrows(String income) {
    assertThrows(Exception.class, () -> new User("25", "<25>", "Female", income));
  }

  @ParameterizedTest
  @ValueSource(strings = {"f", "o", "m", ""})
  void genderThrows(String gender) {
    assertThrows(Exception.class, () -> new User("25", "<25>", gender, "High"));
  }


  @Test
  void userLegal() {
    assertDoesNotThrow(() -> new User("25", "<25", "Female", "High"));
    assertDoesNotThrow(() -> new User("25", "<25", "Female", "Medium"));
    assertDoesNotThrow(() -> new User("25", "<25", "Female", "Low"));

    assertDoesNotThrow(() -> new User("25", "<25", "Male", "Medium"));

    assertDoesNotThrow(() -> new User("25", "25-34", "Female", "Medium"));
    assertDoesNotThrow(() -> new User("25", "35-44", "Female", "Medium"));
    assertDoesNotThrow(() -> new User("25", "45-54", "Female", "Medium"));
    assertDoesNotThrow(() -> new User("25", ">54", "Female", "Medium"));


  }
}
