package de.gwdg.metadataqa.api.util;

import java.util.logging.Logger;

public class IdentifierGenerator {
  private static final Logger LOGGER = Logger.getLogger(IdentifierGenerator.class.getCanonicalName());

  private static int identifier = 0;
  public static String PREFIX = "UNKNOWN-ID-";

  public static String generate() {
    String id = PREFIX + String.valueOf(++identifier);
    LOGGER.info("Generated identifier " + id);
    return id;
  }

  public static void setPrefix(String prefix) {
    PREFIX = prefix;
  }
}
