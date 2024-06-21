package de.gwdg.metadataqa.api.cli;

public enum InputFormat {
  NDJSON(    "ndjson",     "line delimited JSON"),
  JSON_ARRAY("json-array", "JSON file that contains an array of objects");

  private final String abbreviation;
  private final String description;

  InputFormat(String abbreviation, String description) {
    this.abbreviation = abbreviation;
    this.description = description;
  }

  public static InputFormat byCode(String value) {
    for (InputFormat type : values())
      if (type.abbreviation.equals(value))
        return type;
    return null;
  }
}
