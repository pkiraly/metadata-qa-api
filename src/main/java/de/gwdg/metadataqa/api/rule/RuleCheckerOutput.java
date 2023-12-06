package de.gwdg.metadataqa.api.rule;

public class RuleCheckerOutput {
  private final RuleCheckingOutputStatus status;
  private RuleCheckingOutputType outputType;
  private Integer score = 0;

  public RuleCheckerOutput(RuleChecker ruleChecker, boolean isNA, boolean passed) {
    this(ruleChecker, RuleCheckingOutputStatus.create(isNA, passed));
  }

  public RuleCheckerOutput(RuleChecker ruleChecker, RuleCheckingOutputStatus status) {
    this.status = status;
    if (status.equals(RuleCheckingOutputStatus.FAILED))
      score = ruleChecker.getFailureScore();
    else if (status.equals(RuleCheckingOutputStatus.PASSED))
      score = ruleChecker.getSuccessScore();
    else if (status.equals(RuleCheckingOutputStatus.NA))
      score = ruleChecker.getNaScore();
  }

  public RuleCheckerOutput(RuleCheckingOutputStatus status, Integer score) {
    this.status = status;
    this.score = score;
  }

  public RuleCheckingOutputStatus getStatus() {
    return status;
  }

  public Integer getScore() {
    return score;
  }

  public String toString() {
    return outputType.equals(RuleCheckingOutputType.STATUS)
      ? status.asString()
      : score == null
        ? "0" : score.toString();
  }

  public RuleCheckerOutput setOutputType(RuleCheckingOutputType outputType) {
    this.outputType = outputType;
    return this;
  }
}
