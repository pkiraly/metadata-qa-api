package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.configuration.schema.ApplicationScope;
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
  private Boolean mandatory = false;
  private ApplicationScope scope;

  /**
   * A flag to denote if the RuleChecker should count the number instances and failures
   */
  private Boolean countInstances = false;

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

  public String getIdOrHeader() {
    return id != null ? id : header + ":" + getId();
  }

  public String getHeader(RuleCheckingOutputType outputType) {
    return getHeader() + getKeySuffix(outputType);
  }

  private static String getKeySuffix(RuleCheckingOutputType outputType) {
    String suffix = "";
    if (outputType.equals(RuleCheckingOutputType.STATUS))
      suffix = ":status";
    else if (outputType.equals(RuleCheckingOutputType.SCORE))
      suffix = ":score";
    return suffix;
  }

  public String getIdOrHeader(RuleCheckingOutputType outputType) {
    return getIdOrHeader() + getKeySuffix(outputType);
  }

  protected void addOutput(FieldCounter<RuleCheckerOutput> results,
                           boolean isNA,
                           boolean allPassed,
                           RuleCheckingOutputType outputType) {
    addOutput(results, isNA, allPassed, outputType, null, null);
  }

  protected void addOutput(FieldCounter<RuleCheckerOutput> results,
                           boolean isNA,
                           boolean allPassed,
                           RuleCheckingOutputType outputType,
                           Integer instanceCount,
                           Integer failureCount) {

    RuleCheckerOutput output = new RuleCheckerOutput(this, isNA, allPassed);
    if (instanceCount != null)
      output.setInstanceCount(instanceCount);
    if (failureCount != null)
      output.setFailureCount(failureCount);

    addOutput(results, output, outputType);
  }

  protected void addOutput(FieldCounter<RuleCheckerOutput> results,
                           RuleCheckerOutput output,
                           RuleCheckingOutputType outputType) {
    if (outputType.equals(RuleCheckingOutputType.STATUS) || outputType.equals(RuleCheckingOutputType.SCORE)) {
      results.put(getIdOrHeader(), output.setOutputType(outputType));
    } else {
      try {
        RuleCheckerOutput output2 = (RuleCheckerOutput) output.clone();
        results.put(getIdOrHeader(RuleCheckingOutputType.STATUS), output.setOutputType(RuleCheckingOutputType.STATUS));
        results.put(getIdOrHeader(RuleCheckingOutputType.SCORE), output2.setOutputType(RuleCheckingOutputType.SCORE));
      } catch (CloneNotSupportedException e) {
        e.printStackTrace(System.err);
      }
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

  @Override
  public void setMandatory() {
    this.mandatory = true;
  }

  @Override
  public boolean isMandatory() {
    return mandatory;
  }

  @Override
  public ApplicationScope getScope() {
    return scope;
  }

  @Override
  public void setScope(ApplicationScope scope) {
    this.scope = scope;
  }

  @Override
  public boolean hasScope() {
    return scope != null;
  }

  public boolean countInstances() {
    return countInstances;
  }

  public void setCountInstances(Boolean countInstances) {
    this.countInstances = countInstances;
  }

  /**
   * Decide if the criterium has been passed base on the results, and the scope
   * @param passCount The number of times the check has been passed
   * @param hasFailed Whether there were a failure
   * @return
   */
  public boolean isPassed(int passCount, boolean hasFailed) {
    boolean passed = false;
    if (hasScope()) {
      if (scopeIsAllOf()) {
        passed = passCount > 0 && !hasFailed;
      } else if (scopeIsAnyOf()) {
        passed = passCount > 0;
      } else if (scopeIsOneOf()) {
        passed = passCount == 1;
      }
    } else {
      passed = passCount > 0;
    }
    return passed;
  }

  protected boolean scopeIsAllOf() {
    return hasScope() && scope.equals(ApplicationScope.allOf);
  }

  protected boolean scopeIsAnyOf() {
    return hasScope() && scope.equals(ApplicationScope.anyOf);
  }

  protected boolean scopeIsOneOf() {
    return hasScope() && scope.equals(ApplicationScope.oneOf);
  }
}