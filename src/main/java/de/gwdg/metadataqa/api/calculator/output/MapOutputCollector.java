package de.gwdg.metadataqa.api.calculator.output;

import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.util.CompressionLevel;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapOutputCollector implements OutputCollector {

  Map<String, Object> result = new LinkedHashMap<>();

  @Override
  public void addResult(Calculator calculator, CompressionLevel compressionLevel) {
    result.putAll(calculator.getResultMap());
  }

  @Override
  public Object getResults() {
    return result;
  }
}
