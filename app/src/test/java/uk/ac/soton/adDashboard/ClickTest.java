package uk.ac.soton.adDashboard;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.ac.soton.adDashboard.records.Click;
import uk.ac.soton.adDashboard.records.Impression;

public class ClickTest {

  @Test
  void clickLegal() {
    assertDoesNotThrow(() -> new Click("2015-01-01 00:00:02", "23", "2.5"));
    assertDoesNotThrow(() -> new Click("2024-01-01 06:01:02", "453", "115"));
  }

  // Date
  @ParameterizedTest
  @ValueSource(strings = {"2015-01-01 24:00:02","2015-13-01 12:00:02", "2015-01-32 12:00:02", "2015-01-01 12:60:02", "2015-01-01 12:00:60"})
  void clicksParseDateThrows(String date) {
    assertThrows(Exception.class, () -> new Click(date, "23","2.5"));
  }

  // ID
  @ParameterizedTest
  @ValueSource(strings = {"-1", "gg"})
  void clicksIdThrows(String id) {
    assertThrows(Exception.class, () -> new Click("2015-01-01 23:00:02", id, "12.5"));
  }

  // Cost
  @ParameterizedTest
  @ValueSource(strings = {"-1", "-5", "k", " "})
  void clicksCostThrows(String cost) {
    assertThrows(Exception.class, () -> new Click("2015-01-01 23:00:02", "25", cost));
  }
}
