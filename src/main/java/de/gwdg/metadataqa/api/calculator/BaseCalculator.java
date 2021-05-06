package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;

import java.io.Serializable;
import java.util.List;

public abstract class BaseCalculator implements Calculator, Serializable  {

  protected FieldCounter<Object> resultMap;

  @Override
  public List<Object> getCsv() {
    return resultMap.getCsv();
  }

}
