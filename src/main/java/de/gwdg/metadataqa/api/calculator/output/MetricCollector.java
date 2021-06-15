package de.gwdg.metadataqa.api.calculator.output;

import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.util.CompressionLevel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MetricCollector implements OutputCollector {

  Map<String, List<MetricResult>> result = new LinkedHashMap<>();

  @Override
  public void addResult(Calculator calculator, List<MetricResult> metricResults, CompressionLevel compressionLevel) {
    result.put(calculator.getCalculatorName(), metricResults);
    // for (MetricResult metricResult : metricResults)
    //   result.put(calculator.getCalculatorName(), metricResult.getResultMap());
  }

  @Override
  public Object getResults() {
    return result;
  }
}
