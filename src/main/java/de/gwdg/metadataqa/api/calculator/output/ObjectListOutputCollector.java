package de.gwdg.metadataqa.api.calculator.output;

import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.util.CompressionLevel;

import java.util.ArrayList;
import java.util.List;

public class ObjectListOutputCollector implements OutputCollector {

  List<Object> result = new ArrayList<>();

  @Override
  public void addResult(Calculator calculator, CompressionLevel compressionLevel) {
    result.addAll(calculator.getCsv());
  }

  @Override
  public Object getResults() {
    return result;
  }
}
