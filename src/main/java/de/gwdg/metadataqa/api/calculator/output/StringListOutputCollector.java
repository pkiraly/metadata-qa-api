package de.gwdg.metadataqa.api.calculator.output;

import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.util.CompressionLevel;

import java.util.ArrayList;
import java.util.List;

public class StringListOutputCollector implements OutputCollector {

  List<String> result = new ArrayList<>();

  @Override
  public void addResult(Calculator calculator, CompressionLevel compressionLevel) {
    result.addAll(calculator.getList(false, compressionLevel));
  }

  @Override
  public Object getResults() {
    return result;
  }
}
