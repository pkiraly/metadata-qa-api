package de.gwdg.metadataqa.api.rule;

public enum RuleCheckingOutput {
  NA(-1),
  FAILED(0),
  PASSED(1);

  private final int numeric;

  RuleCheckingOutput(int value) {
    this.numeric = value;
  }

  public Object value() {
    if (numeric == -1)
      return "NA";
    return numeric;
  }

  public String asString() {
    if (numeric == -1)
      return "NA";
    return Integer.toString(numeric);
  }

  public static RuleCheckingOutput create(boolean isNA, boolean value) {
    if (isNA)
      return NA;
    else if (value)
      return PASSED;
    else
      return FAILED;
  }
}