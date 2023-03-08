package uk.ac.soton.adDashboard;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.ac.soton.adDashboard.records.Click;

public class ClickTest {

  @Test
  void clickLegal() {
    assertDoesNotThrow(() -> new Click("2015-01-01 00:00:02", "23", "2.5"));

  }

  @ParameterizedTest
  @ValueSource(strings = {"-1", "-5", "k"})
  void clicksIdThrows(String id) {
    assertThrows(Exception.class, () -> new Click("2015-01-01 24:00:02", id, "12.5"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"-1", "-5", "k"})
  void clicksCostThrows(String cost) {
    assertThrows(Exception.class, () -> new Click("2015-01-01 24:00:02", "25", cost));
  }
}
