package de.gwdg.metadataqa.api.util;

public class IdentifierGenerator {
  private static int identifier = 0;
  private static String PREFIX = "UNKNOWN-ID-";

  public static String generate() {
    return PREFIX + ++identifier;
  }

  public static void setPrefix(String prefix) {
    PREFIX = prefix;
  }
}
