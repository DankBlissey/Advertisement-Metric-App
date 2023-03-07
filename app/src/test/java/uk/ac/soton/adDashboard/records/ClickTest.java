package uk.ac.soton.adDashboard.records;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ClickTest {

  @Test
  void clickLegal() {
    assertDoesNotThrow(() -> new Click("2015-01-01 00:00:02", "23", "2.5"));

  }

  @ParameterizedTest
  @ValueSource(strings = {"-1", "-5"})
  void clicksIdThrows(String id) {
    assertThrows(Exception.class, () -> new Click("2015-01-01 24:00:02", id, "12.5"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"-1", "-5"})
  void clicksCostThrows(String cost) {
    assertThrows(Exception.class, () -> new Click("2015-01-01 24:00:02", "25", cost));
  }
}
