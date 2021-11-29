package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.problemcatalog.FieldCounterBasedResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnnotationCalculator implements Calculator, Serializable {

  public static final String CALCULATOR_NAME = "annotation";

  private Map<String, Object> annotaionColumns;
  private List<MetricResult> results;

  public AnnotationCalculator(Map<String, Object> annotaionColumns) {
    this.annotaionColumns = annotaionColumns;
  }

  @Override
  public List<MetricResult> measure(PathCache cache) {
    if (results == null) {
      FieldCounter<Object> resultMap = new FieldCounter<>();
      for (Map.Entry<String, Object> entry : annotaionColumns.entrySet())
        resultMap.put(entry.getKey(), entry.getValue());
      results = List.of(new FieldCounterBasedResult<>(getCalculatorName(), resultMap).withNoCompression());
    }
    return results;
  }

  @Override
  public List<String> getHeader() {
    return new ArrayList<>(annotaionColumns.keySet());
  }

  @Override
  public String getCalculatorName() {
    return CALCULATOR_NAME;
  }
}
