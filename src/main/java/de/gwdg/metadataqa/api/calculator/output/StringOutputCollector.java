package de.gwdg.metadataqa.api.calculator.output;

import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.util.CompressionLevel;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class StringOutputCollector implements OutputCollector {

  List<String> result = new ArrayList<>();

  @Override
  public void addResult(Calculator calculator, List<MetricResult> metricResults, CompressionLevel compressionLevel) {
    for (MetricResult metricResult : metricResults)
      result.add(metricResult.getCsv(false, compressionLevel));
  }

  @Override
  public Object getResults() {
    return StringUtils.join(result,",");
  }
}
