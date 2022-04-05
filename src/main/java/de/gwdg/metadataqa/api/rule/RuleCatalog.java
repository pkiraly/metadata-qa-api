package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.problemcatalog.FieldCounterBasedResult;
import de.gwdg.metadataqa.api.schema.Schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RuleCatalog implements Calculator, Serializable {

  private static final String CALCULATOR_NAME = "ruleCatalog";
  private Schema schema;
  private boolean onlyIdInHeader = false;
  private RuleCheckingOutputType outputType = RuleCheckingOutputType.BOTH;
  private boolean hiddenIdsCollected = false;
  private List<String> hiddenIds = new ArrayList<>();

  public RuleCatalog(Schema schema) {
    this.schema = schema;
  }

  @Override
  public List<MetricResult> measure(PathCache cache) {
    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    var totalScore = 0;

    for (RuleChecker ruleChecker : schema.getRuleCheckers()) {
      ruleChecker.update(cache, fieldCounter, outputType);
      if (outputType != RuleCheckingOutputType.STATUS && !ruleChecker.isHidden()) {
        String key = outputType.equals(RuleCheckingOutputType.BOTH) ? ruleChecker.getHeader(RuleCheckingOutputType.SCORE) : ruleChecker.getHeader();
        Integer score = fieldCounter.get(key).getScore();
        if (score != null)
          totalScore += score.intValue();
      }
      if (!hiddenIdsCollected)
        collectHiddenIds(hiddenIds, ruleChecker);
    }
    if (!hiddenIdsCollected)
      hiddenIdsCollected = true;
    if (outputType != RuleCheckingOutputType.STATUS)
      fieldCounter.put(CALCULATOR_NAME + ":score", new RuleCheckerOutput(RuleCheckingOutputStatus.NA, totalScore).setOutputType(outputType));

    removeHiddenScores(fieldCounter, hiddenIds);

    return List.of(new FieldCounterBasedResult<>(getCalculatorName(), fieldCounter));
  }

  @Override
  public List<String> getHeader() {
    List<String> headers = new ArrayList<>();
    for (RuleChecker ruleChecker : schema.getRuleCheckers()) {
      if (!ruleChecker.isHidden()) {
        if (outputType != RuleCheckingOutputType.BOTH)
          headers.add(onlyIdInHeader ? ruleChecker.getId() : ruleChecker.getHeader());
        else {
          headers.add(onlyIdInHeader ? ruleChecker.getId() + ":status" : ruleChecker.getHeader(RuleCheckingOutputType.STATUS));
          headers.add(onlyIdInHeader ? ruleChecker.getId() + ":score" : ruleChecker.getHeader(RuleCheckingOutputType.SCORE));
        }
      }
    }
    if (outputType != RuleCheckingOutputType.STATUS)
      headers.add(CALCULATOR_NAME + ":score");
    return headers;
  }

  @Override
  public String getCalculatorName() {
    return CALCULATOR_NAME;
  }

  public RuleCatalog setOnlyIdInHeader(boolean onlyIdInHeader) {
    this.onlyIdInHeader = onlyIdInHeader;
    return this;
  }

  public RuleCatalog setOutputType(RuleCheckingOutputType outputType) {
    this.outputType = outputType;
    return this;
  }

  private void collectHiddenIds(List<String> hiddenIds, RuleChecker ruleChecker) {
    if (ruleChecker.isHidden()) {
      if (outputType.equals(RuleCheckingOutputType.BOTH)) {
        hiddenIds.add(ruleChecker.getHeader(RuleCheckingOutputType.SCORE));
        hiddenIds.add(ruleChecker.getHeader(RuleCheckingOutputType.STATUS));
      } else {
        hiddenIds.add(ruleChecker.getHeader());
      }
    }
  }

  private void removeHiddenScores(FieldCounter<RuleCheckerOutput> fieldCounter, List<String> hiddenIds) {
    if (!hiddenIds.isEmpty())
      for (String hiddenId : hiddenIds)
        fieldCounter.remove(hiddenId);
  }
}
