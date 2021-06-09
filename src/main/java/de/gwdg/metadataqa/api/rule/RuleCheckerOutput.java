package de.gwdg.metadataqa.api.rule;

public class RuleCheckerOutput {
  private final RuleCheckingOutputType type;
  private Integer score;

  public RuleCheckerOutput(RuleChecker ruleChecker, boolean isNA, boolean passed) {
    this(ruleChecker, RuleCheckingOutputType.create(isNA, passed));
  }

  public RuleCheckerOutput(RuleChecker ruleChecker, RuleCheckingOutputType type) {
    this.type = type;
    if (type.equals(RuleCheckingOutputType.FAILED))
      score = ruleChecker.getFailureScore();
    else if (type.equals(RuleCheckingOutputType.PASSED))
      score = ruleChecker.getSuccessScore();
  }

  public RuleCheckerOutput(RuleCheckingOutputType type, Integer score) {
    this.type = type;
    this.score = score;
  }

  public RuleCheckingOutputType getType() {
    return type;
  }

  public Integer getScore() {
    return score;
  }

  public String toString() {
    return score != null ? score.toString() : type.asString();
  }
}
