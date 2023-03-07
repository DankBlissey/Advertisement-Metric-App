package uk.ac.soton.adDashboard.records;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ServerAccessTest {

  @ParameterizedTest
  @ValueSource(strings = {"2015-01-01 24:00:02", "2015-13-01 12:00:02", "2015-13-01    12:00:02"})
  void accessParseDateThrows(String date) {
    assertThrows(Exception.class, () -> new Impression(date, "24", "12.5", "Shopping"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"-1", "-5"})
  void accessIdThrows(String id) {
    assertThrows(Exception.class,
        () -> new ServerAccess("2015-01-01 23:00:02", id, "2015-01-02 23:00:02", "2", "Yes"));
  }

  @Test
  void conversion() {
    assertThrows(Exception.class,
        () -> new ServerAccess("2015-01-01 23:00:02", "25", "2015-01-02 23:00:02", "2", ""));
    assertThrows(Exception.class,
        () -> new ServerAccess("2015-01-01 23:00:02", "25", "2015-01-02 23:00:02", "2", "a"));

  }

  @Test
  void accessLegal() {
    assertDoesNotThrow(
        () -> new ServerAccess("2015-01-01 23:00:02", "25", "2015-01-02 23:00:02", "2", "Yes"));
    assertDoesNotThrow(
        () -> new ServerAccess("2015-01-01 23:00:02", "25", "n/a", "2", "Yes"));
    try {
      ServerAccess s = new ServerAccess("2015-01-01 23:00:02", "25", "n/a", "2", "No");
      assertNull(s.getEndDate());
      
    } catch (Exception e) {
      fail("exception in server");

    }

  }
}
