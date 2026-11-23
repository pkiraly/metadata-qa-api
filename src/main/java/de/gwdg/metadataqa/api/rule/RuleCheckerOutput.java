package de.gwdg.metadataqa.api.rule;

import java.util.logging.Logger;

public class RuleCheckerOutput {
  private static final Logger LOGGER = Logger.getLogger(RuleCheckerOutput.class.getCanonicalName());

  private final RuleCheckingOutputStatus status;
  private RuleCheckingOutputType outputType;
  private Integer score = null;
  private Integer instanceCount;
  private Integer failureCount;

  public RuleCheckerOutput(RuleChecker ruleChecker, boolean isNA, boolean passed) {
    this(ruleChecker, RuleCheckingOutputStatus.create(isNA, passed, ruleChecker.isMandatory()));
  }

  public RuleCheckerOutput(RuleChecker ruleChecker, RuleCheckingOutputStatus status) {
    LOGGER.info("type 1 " + ruleChecker);
    this.status = status;
    LOGGER.info("status: " + status);
    if (status.equals(RuleCheckingOutputStatus.FAILED))
      score = ruleChecker.getFailureScore();
    else if (status.equals(RuleCheckingOutputStatus.PASSED))
      score = ruleChecker.getSuccessScore();
    else if (status.equals(RuleCheckingOutputStatus.NA))
      score = ruleChecker.getNaScore();
    LOGGER.info("score: " + score);
  }

  public RuleCheckerOutput(RuleCheckingOutputStatus status, Integer score) {
    LOGGER.info("type 2");
    this.status = status;
    this.score = score;
  }

  public RuleCheckingOutputStatus getStatus() {
    return status;
  }

  public Integer getScore() {
    return score;
  }

  public Integer getInstanceCount() {
    return instanceCount;
  }

  public RuleCheckerOutput setInstanceCount(Integer instanceCount) {
    this.instanceCount = instanceCount;
    return this;
  }

  public Integer getFailureCount() {
    return failureCount;
  }

  public RuleCheckerOutput setFailureCount(Integer failureCount) {
    this.failureCount = failureCount;
    return this;
  }

  public String toString() {
    return outputType != null && outputType.equals(RuleCheckingOutputType.STATUS)
      ? status.asString()
      : score == null
      ? null : score.toString();
  }

  protected Object copy() throws CloneNotSupportedException {
    return new RuleCheckerOutput(status, score)
      .setInstanceCount(instanceCount)
      .setFailureCount(failureCount);
  }

  public RuleCheckerOutput setOutputType(RuleCheckingOutputType outputType) {
    this.outputType = outputType;
    return this;
  }
}
