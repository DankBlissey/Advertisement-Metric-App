package uk.ac.soton.adDashboard.enums;

/**
 * Gender of the user.
 */
public enum Gender {
  MALE,
  FEMALE,
  NONBINARY,
  PREFERNOTTOSAY,
  OTHER,
  ANY;

  public static Gender parseGender(String gender) {
    return switch (gender) {
      case "Male" -> MALE;
      case "Female" -> FEMALE;
      case "Non-binary" -> NONBINARY;
      case "Prefer not to say" -> PREFERNOTTOSAY;
      case "Other" -> OTHER;
      default -> ANY;
    };

  }

}
