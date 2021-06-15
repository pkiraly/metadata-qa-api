package de.gwdg.metadataqa.api.calculator.output;

import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.util.CompressionLevel;

import java.util.List;

public interface OutputCollector {
  enum TYPE {
    STRING,
    STRING_LIST,
    OBJECT_LIST,
    MAP,
    METRIC,
    JSON
  }

  void addResult(Calculator calculator, List<MetricResult> metricResults, CompressionLevel compressionLevel);
  Object getResults();
}
