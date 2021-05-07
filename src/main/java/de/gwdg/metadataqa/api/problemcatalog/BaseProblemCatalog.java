package de.gwdg.metadataqa.api.problemcatalog;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.util.CompressionLevel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseProblemCatalog<T> implements Calculator {
  protected FieldCounter<T> fieldCounter;

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
}
