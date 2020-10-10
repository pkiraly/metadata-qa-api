package de.gwdg.metadataqa.api.calculator.output;

import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.util.CompressionLevel;

public interface OutputCollector {
  enum TYPE {
    STRING,
    STRING_LIST,
    OBJECT_LIST,
    MAP
  }

  void addResult(Calculator calculator, CompressionLevel compressionLevel);
  Object getResults();
}
