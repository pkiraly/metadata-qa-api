package de.gwdg.metadataqa.api.problemcatalog;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.util.CompressionLevel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FieldCounterBasedResult<T> implements MetricResult {
  protected String name;
  protected FieldCounter<T> fieldCounter;
  protected boolean noCompression = false;

  public FieldCounterBasedResult(String name, FieldCounter<T> fieldCounter) {
    this.name = name;
    this.fieldCounter = fieldCounter;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Map<String, ? extends Object> getResultMap() {
    return fieldCounter.getMap();
  }

  @Override
  public Map<String, Map<String, ? extends Object>> getLabelledResultMap() {
    Map<String, Map<String, ? extends Object>> labelledResultMap = new LinkedHashMap<>();
    labelledResultMap.put(name, fieldCounter.getMap());
    return labelledResultMap;
  }

  @Override
  public String getCsv(boolean withLabels, CompressionLevel compressionLevel) {
    return fieldCounter.getCsv(withLabels, overrideCompressionLevel(compressionLevel));
  }

  @Override
  public List<Object> getCsv() {
    return fieldCounter.getCsv();
  }

  @Override
  public List<String> getList(boolean withLabels, CompressionLevel compressionLevel) {
    return fieldCounter.getList(withLabels, overrideCompressionLevel(compressionLevel));
  }

  private CompressionLevel overrideCompressionLevel(CompressionLevel compressionLevel) {
    return noCompression ? CompressionLevel.ZERO : compressionLevel;
  }

  public MetricResult withNoCompression() {
    noCompression = true;
    return this;
  }

  @Override
  public String toString() {
    return "FieldCounterBasedResult{" +
      "name='" + name + '\'' +
      ", fieldCounter=" + fieldCounter +
      ", noCompression=" + noCompression +
      '}';
  }
}
