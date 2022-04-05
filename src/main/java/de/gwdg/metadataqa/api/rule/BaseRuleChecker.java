package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.counter.FieldCounter;

public abstract class BaseRuleChecker implements RuleChecker {
  protected String id;
  protected Integer failureScore;
  protected Integer successScore;
  protected String header;
  protected Boolean hidden = false;

  @Override
  public String getId() {
    return id == null ? String.valueOf(0) : id;
  }

  @Override
  public void setId(String id) {
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
    return header + ":" + getId();
  }

  public String getHeader(RuleCheckingOutputType outputType) {
    String suffix = "";
    if (outputType.equals(RuleCheckingOutputType.STATUS))
      suffix = ":status";
    else if (outputType.equals(RuleCheckingOutputType.SCORE))
      suffix = ":score";
    return header + ":" + getId() + suffix;
  }

  protected void addOutput(FieldCounter<RuleCheckerOutput> results, boolean isNA, boolean allPassed, RuleCheckingOutputType outputType) {
    if (outputType.equals(RuleCheckingOutputType.STATUS) || outputType.equals(RuleCheckingOutputType.SCORE)) {
      results.put(getHeader(), new RuleCheckerOutput(this, isNA, allPassed).setOutputType(outputType));
    } else {
      results.put(getHeader(RuleCheckingOutputType.STATUS), new RuleCheckerOutput(this, isNA, allPassed).setOutputType(RuleCheckingOutputType.STATUS));
      results.put(getHeader(RuleCheckingOutputType.SCORE), new RuleCheckerOutput(this, isNA, allPassed).setOutputType(RuleCheckingOutputType.SCORE));
    }
  }

  public void setHidden() {
    this.hidden = true;
  }

  public boolean isHidden() {
    return hidden;
  }
}