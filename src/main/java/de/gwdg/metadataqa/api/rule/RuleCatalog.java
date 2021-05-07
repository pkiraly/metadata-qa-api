package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.problemcatalog.BaseProblemCatalog;
import de.gwdg.metadataqa.api.schema.Schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RuleCatalog extends BaseProblemCatalog<RuleCheckingOutput> implements Serializable {

  private static final Logger LOGGER = Logger.getLogger(RuleCatalog.class.getCanonicalName());

  private final List<RuleChecker> ruleCheckers = new ArrayList<>();
  private static final String CALCULATOR_NAME = "ruleCatalog";
  private Schema schema;

  public RuleCatalog(Schema schema) {
    this.schema = schema;
  }

  public void addRuleChecker(RuleChecker ruleChecker) {
    ruleCheckers.add(ruleChecker);
  }

  @Override
  public void measure(PathCache cache) {
    this.fieldCounter = new FieldCounter<>();
    for (RuleChecker ruleChecker : schema.getRuleCheckers()) {
      ruleChecker.update(cache, fieldCounter);
    }
  }

  @Override
  public List<String> getHeader() {
    List<String> headers = new ArrayList<>();
    for (RuleChecker ruleChecker : schema.getRuleCheckers()) {
      headers.add(ruleChecker.getHeader());
    }
    return headers;
  }

  @Override
  public String getCalculatorName() {
    return CALCULATOR_NAME;
  }
}
