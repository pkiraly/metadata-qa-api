package de.gwdg.metadataqa.api.util;

public class IdentifierGenerator {
  private static int identifier = 0;
  public static String prefix = "UNKNOWN-ID-";

  public static String generate() {
    return prefix + ++identifier;
  }

  public static void setPrefix(String prefix) {
    IdentifierGenerator.prefix = prefix;
  }
}
