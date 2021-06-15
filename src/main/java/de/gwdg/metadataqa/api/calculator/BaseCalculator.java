package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;

import java.io.Serializable;

public abstract class BaseCalculator<T> implements Calculator, Serializable  {

  protected FieldCounter<T> resultMap;
}
