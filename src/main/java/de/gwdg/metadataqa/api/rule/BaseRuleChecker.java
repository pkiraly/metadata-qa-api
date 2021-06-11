package de.gwdg.metadataqa.api.rule;

public abstract class BaseRuleChecker implements RuleChecker {
  protected int id;
  protected Integer failureScore;
  protected Integer successScore;
  protected String header;

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public Integer getFailureScore() {
    return failureScore;
  }

  @Override
  public void setFailureScore(Integer failureScore) {
    this.failureScore = failureScore;
  }

  @Override
  public Integer getSuccessScore() {
    return successScore;
  }

  @Override
  public void setSuccessScore(Integer successScore) {
    this.successScore = successScore;
  }

  @Override
  public String getHeaderWithoutId() {
    return header;
  }

  public String getHeader() {
    return "rule:" + header + ":" + getId();
  }
}