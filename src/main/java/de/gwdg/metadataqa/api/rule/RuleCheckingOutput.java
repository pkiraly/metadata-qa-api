package de.gwdg.metadataqa.api.rule;

public enum RuleCheckingOutput {
  NA(-1),
  FAILED(0),
  PASSED(1);

  private final int numeric;

  RuleCheckingOutput(int value) {
    this.numeric = value;
  }

  /**
   * Get the value as an object
   * @return "NA" (string) if it is NA, 0 (int) if failed, 1 (int) if passed
   */
  public Object value() {
    if (numeric == -1)
      return "NA";
    return numeric;
  }

  /**
   * Get the value as a string
   * @return "NA" if it is NA, "0" if failed, "1" if passed
   */
  public String asString() {
    if (numeric == -1)
      return "NA";
    return Integer.toString(numeric);
  }

  /**
   * Create from boolean conditions
   *
   * @param isNA Is NA?
   * @param passed Is rule passed?
   * @return
   */
  public static RuleCheckingOutput create(boolean isNA, boolean passed) {
    if (isNA)
      return NA;
    else if (passed)
      return PASSED;
    else
      return FAILED;
  }
}