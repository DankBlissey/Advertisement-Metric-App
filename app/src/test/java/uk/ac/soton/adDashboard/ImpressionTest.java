package uk.ac.soton.adDashboard;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.ac.soton.adDashboard.records.Impression;

class ImpressionTest {

  // Legal
  @Test
  void impressionsLegal() {
    assertDoesNotThrow(() -> new Impression("2015-01-01 23:00:02", "25", "12.5", "Shopping"));
    assertDoesNotThrow(() -> new Impression("2015-01-01 00:00:02", "25", "144", "Hobbies"));

  }

  // Date
  @ParameterizedTest
  @ValueSource(strings = {"2015-01-01 24:00:02", "2015-13-01 12:00:02", "2015-01-32 12:00:02", "2015-01-01 12:60:02", "2015-01-01 12:00:60"})
  void impressionsParseDateThrows(String date) {
    assertThrows(Exception.class, () -> new Impression(date, "24", "12.5", "Shopping"));
  }

  // ID
  @ParameterizedTest
  @ValueSource(strings = {"-1", "k"})
  void impressionsIdThrows(String id) {
    assertThrows(Exception.class,
            () -> new Impression("2015-01-01 23:00:02", id, "12.5", "Shopping"));
  }

  // Cost
  @ParameterizedTest
  @ValueSource(strings = {"-14", "-1", " ", "abc"})
  void impressionsCostThrows(String cost) {
    assertThrows(Exception.class,
            () -> new Impression("2015-01-01 23:00:02", "25", cost, "Shopping"));
  }

  // Context
  @ParameterizedTest
  @ValueSource(strings = {"Walking", "", " "})
  void impressionsContextThrows(String context) {
    assertThrows(Exception.class, () -> new Impression("2015-01-01 23:00:02", "25", "25", context));
  }

}
