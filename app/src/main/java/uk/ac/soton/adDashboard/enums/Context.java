package uk.ac.soton.adDashboard.enums;

/**
 * Context of the impression.
 */
public enum Context {
  News,
  Shopping,
  Social,
  Blog,
  Hobbies,
  Travel,
  ANY;

  public static Context parseContext(String context) {
    return switch (context) {
      case "News" -> News;
      case "Shopping" -> Shopping;
      case "Social" -> Social;
      case "Blog" -> Blog;
      case "Hobbies" -> Hobbies;
      case "Travel" -> Travel;
      default -> ANY;
    };
  }
}
