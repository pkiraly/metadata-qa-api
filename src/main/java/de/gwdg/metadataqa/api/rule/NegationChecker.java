package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;

public class NegationChecker implements RuleChecker {

  public static final String prefix = "not";

  private RuleChecker ruleChecker;
  protected String header;

  public NegationChecker(RuleChecker ruleChecker) {
    this.ruleChecker = ruleChecker;
    this.header = prefix + ":" + ruleChecker.getHeader();
  }

  @Override
  public String getHeader() {
    return header;
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckingOutput> results) {
    FieldCounter<RuleCheckingOutput> fieldCounter = new FieldCounter<>();
    ruleChecker.update(cache, fieldCounter);

    RuleCheckingOutput result = fieldCounter.get(ruleChecker.getHeader());
    // negate
    result = result.equals(RuleCheckingOutput.FAILED)
      ? RuleCheckingOutput.PASSED : RuleCheckingOutput.FAILED;

    results.put(header, result);
  }
}
