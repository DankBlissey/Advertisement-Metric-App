package uk.ac.soton.adDashboard.enums;

/**
 * User income bracket.
 */
public enum Income {
  LOW,
  MEDIUM,
  HIGH,
  ANY;


  public static Income parseIncome(String income) {
    return switch (income) {
      case "Low" -> LOW;
      case "Medium" -> MEDIUM;
      case "High" -> HIGH;
      default -> ANY;
    };
  }
}
