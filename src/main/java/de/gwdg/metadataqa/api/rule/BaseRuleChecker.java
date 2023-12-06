package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.counter.FieldCounter;

import java.util.logging.Logger;

public abstract class BaseRuleChecker implements RuleChecker {

  protected static final Logger LOGGER = Logger.getLogger(BaseRuleChecker.class.getCanonicalName());

  protected String id;
  protected Integer failureScore;
  protected Integer successScore;
  protected Integer naScore;
  protected String header;
  protected Boolean hidden = false;
  private Boolean debug = false;

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

  public RuleChecker withFailureScore(Integer failureScore) {
    this.failureScore = failureScore;
    return this;
  }

  @Override
  public Integer getSuccessScore() {
    return successScore;
  }

  @Override
  public void setSuccessScore(Integer successScore) {
    this.successScore = successScore;
  }

  public RuleChecker withSuccessScore(Integer successScore) {
    this.successScore = successScore;
    return this;
  }

  public void setNaScore(Integer naScore) {
    this.naScore = naScore;
  }

  public Integer getNaScore() {
    return naScore;
  }

  public RuleChecker withNaScore(Integer naScore) {
    this.naScore = naScore;
    return this;
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

  public void setDebug() {
    this.debug = true;
  }

  public boolean isDebug() {
    return debug;
  }
}