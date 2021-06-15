package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.counter.BasicCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;

import java.io.Serializable;
import java.util.Map;

public abstract class BaseLanguageCalculator implements Calculator, Serializable {

  protected String extractLanguages(Map<String, BasicCounter> languages) {
    var result = new StringBuilder();
    for (Map.Entry<String, BasicCounter> lang : languages.entrySet()) {
      if (result.length() > 0)
        result.append(";");
      result.append(lang.getKey() + ":" + ((Double) lang.getValue().getTotal()).intValue());
    }
    return result.toString();
  }


}
