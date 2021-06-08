package de.gwdg.metadataqa.api.rule;

public abstract class BaseRuleChecker implements RuleChecker {
  protected Integer failureScore;
  protected Integer successScore;

  public Integer getFailureScore() {
    return failureScore;
  }

  public void setFailureScore(Integer failureScore) {
    this.failureScore = failureScore;
  }

  public Integer getSuccessScore() {
    return successScore;
  }

  public void setSuccessScore(Integer successScore) {
    this.successScore = successScore;
  }

}
