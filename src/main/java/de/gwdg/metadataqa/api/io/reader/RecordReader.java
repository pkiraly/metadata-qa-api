package de.gwdg.metadataqa.api.io.reader;

import de.gwdg.metadataqa.api.calculator.CalculatorFacade;
import de.gwdg.metadataqa.api.interfaces.MetricResult;

import java.io.BufferedReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class RecordReader implements Iterator<Map<String, List<MetricResult>>> {

  protected final BufferedReader inputReader;
  protected final CalculatorFacade calculator;

  public RecordReader(BufferedReader inputReader, CalculatorFacade calculator) {
    this.inputReader = inputReader;
    this.calculator = calculator;
  }
}
