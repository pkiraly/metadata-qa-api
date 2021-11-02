package de.gwdg.metadataqa.api.calculator.output;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.json.JsonUtils;
import de.gwdg.metadataqa.api.util.CompressionLevel;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MetricCollector implements OutputCollector {

  Map<String, List<MetricResult>> results = new LinkedHashMap<>();

  @Override
  public void addResult(Calculator calculator, List<MetricResult> metricResults, CompressionLevel compressionLevel) {
    results.put(calculator.getCalculatorName(), metricResults);
  }

  @Override
  public Object getResults() {
    return results;
  }

  public Object createOutput(OutputCollector.TYPE type, CompressionLevel compressionLevel) {
    Object output = null;
    switch (type) {
      case METRIC:      output = results;                         break;
      case STRING:      output = getString(compressionLevel);     break;
      case STRING_LIST: output = getStringList(compressionLevel); break;
      case OBJECT_LIST: output = getObjectList(compressionLevel); break;
      case MAP:         output = getMap(compressionLevel);        break;
      case JSON:        output = getJson(compressionLevel);       break;
      default:
        throw new IllegalArgumentException("Unsupported type: " + type);
    }
    return output;
  }

  private Object getString(CompressionLevel compressionLevel) {
    List<String> result = new ArrayList<>();

    for (Map.Entry<String, List<MetricResult>> entry : results.entrySet())
      for (MetricResult metricResult : entry.getValue())
        result.add(metricResult.getCsv(false, compressionLevel));

    return StringUtils.join(result,",");
  }

  private Object getStringList(CompressionLevel compressionLevel) {
    List<String> result = new ArrayList<>();

    for (Map.Entry<String, List<MetricResult>> entry : results.entrySet())
      for (MetricResult metricResult : entry.getValue())
        result.addAll(metricResult.getList(false, compressionLevel));

    return result;
  }

  private Object getObjectList(CompressionLevel compressionLevel) {
    List<Object> result = new ArrayList<>();

    for (Map.Entry<String, List<MetricResult>> entry : results.entrySet())
      for (MetricResult metricResult : entry.getValue())
        result.addAll(metricResult.getCsv());

    return result;
  }

  private Object getMap(CompressionLevel compressionLevel) {
    Map<String, Object> result = new LinkedHashMap<>();

    for (Map.Entry<String, List<MetricResult>> entry : results.entrySet())
      for (MetricResult metricResult : entry.getValue())
        for (Map.Entry<String, ?> entry2 : metricResult.getResultMap().entrySet())
          result.put(metricResult.getName() + ":" + entry2.getKey(), entry2.getValue());

    return result;
  }

  private Object getJson(CompressionLevel compressionLevel) {
    Map<String, Object> result = new LinkedHashMap<>();

    for (Map.Entry<String, List<MetricResult>> entry : results.entrySet()) {
      Map<String, Object> calcResult = new LinkedHashMap<>();

      for (MetricResult metricResult : entry.getValue()) {
        calcResult.put(metricResult.getName(), metricResult.getResultMap());
      }
      result.put(entry.getKey(), calcResult);

    }

    try {
      return JsonUtils.toJson(result);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

}
