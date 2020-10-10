package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.CompressionLevel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RuleCatalog implements Calculator, Serializable {

  private final List<RuleChecker> ruleCheckers = new ArrayList<>();
  private PathCache cache;
  private FieldCounter<RuleCheckingOutput> fieldCounter;
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
    this.cache = cache;
    this.fieldCounter = new FieldCounter<>();
    for (RuleChecker ruleChecker : schema.getRuleCheckers()) {
      ruleChecker.update(cache, fieldCounter);
    }
  }

  @Override
  public Map<String, ? extends Object> getResultMap() {
    return fieldCounter.getMap();
  }

  @Override
  public Map<String, Map<String, ? extends Object>> getLabelledResultMap() {
    Map<String, Map<String, ? extends Object>> labelledResultMap = new LinkedHashMap<>();
    labelledResultMap.put(getCalculatorName(), fieldCounter.getMap());
    return labelledResultMap;
  }

  @Override
  public String getCsv(boolean withLabels, CompressionLevel compressionLevel) {
    return fieldCounter.getCsv(withLabels, compressionLevel);
  }

  @Override
  public List<Object> getCsv() {
    return fieldCounter.getCsv();
  }

  @Override
  public List<String> getList(boolean withLabels, CompressionLevel compressionLevel) {
    return fieldCounter.getList(withLabels, compressionLevel);
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
