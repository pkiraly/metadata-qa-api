package de.gwdg.metadataqa.api.calculator.output;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.json.JsonUtils;
import de.gwdg.metadataqa.api.util.CompressionLevel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonCollector implements OutputCollector {
  Map<String, Object> result = new LinkedHashMap<>();

  @Override
  public void addResult(Calculator calculator, List<MetricResult> metricResults, CompressionLevel compressionLevel) {
    for (MetricResult metricResult : metricResults)
      result.put(calculator.getCalculatorName(), metricResult.getResultMap());
  }

  @Override
  public Object getResults() {
    try {
      return JsonUtils.toJson(result);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }
}
