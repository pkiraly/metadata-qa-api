package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;

public class NegationChecker extends BaseRuleChecker {

  public static final String PREFIX = "not";

  private RuleChecker ruleChecker;
  protected String header;

  public NegationChecker(RuleChecker ruleChecker) {
    this.ruleChecker = ruleChecker;
    this.header = PREFIX + ":" + ruleChecker.getHeader();
  }

  @Override
  public String getHeader() {
    return header;
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckerOutput> results) {
    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    ruleChecker.update(cache, fieldCounter);

    RuleCheckerOutput atomicResult = fieldCounter.get(ruleChecker.getHeader());

    results.put(header, new RuleCheckerOutput(this, atomicResult.getType().negate()));
  }
}
