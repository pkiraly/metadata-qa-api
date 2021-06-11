package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.problemcatalog.BaseProblemCatalog;
import de.gwdg.metadataqa.api.schema.Schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RuleCatalog extends BaseProblemCatalog<RuleCheckerOutput> implements Serializable {

  private static final Logger LOGGER = Logger.getLogger(RuleCatalog.class.getCanonicalName());

  private static final String CALCULATOR_NAME = "ruleCatalog";
  private Schema schema;

  public RuleCatalog(Schema schema) {
    this.schema = schema;
  }

  @Override
  public void measure(PathCache cache) {
    this.fieldCounter = new FieldCounter<>();
    var totalScore = 0;
    for (RuleChecker ruleChecker : schema.getRuleCheckers()) {
      ruleChecker.update(cache, fieldCounter);
      Integer score = fieldCounter.get(ruleChecker.getHeader()).getScore();
      if (score != null)
        totalScore += score.intValue();
    }
    fieldCounter.put(CALCULATOR_NAME + ":score", new RuleCheckerOutput(RuleCheckingOutputType.NA, totalScore));
  }

  @Override
  public List<String> getHeader() {
    List<String> headers = new ArrayList<>();
    for (RuleChecker ruleChecker : schema.getRuleCheckers()) {
      headers.add(ruleChecker.getHeader());
    }
    headers.add(CALCULATOR_NAME + ":score");
    return headers;
  }

  @Override
  public String getCalculatorName() {
    return CALCULATOR_NAME;
  }
}
