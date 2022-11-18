package de.gwdg.metadataqa.api.io.reader;

import de.gwdg.metadataqa.api.calculator.CalculatorFacade;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import org.apache.commons.io.LineIterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JSONRecordReader extends RecordReader {
  private final LineIterator jsonIterator;

  public JSONRecordReader(BufferedReader inputReader, CalculatorFacade calculator) throws IOException {
    super(inputReader, calculator);
    jsonIterator = new LineIterator(this.inputReader);
  }

  @Override
  public boolean hasNext() {
    return jsonIterator.hasNext();
  }

  @Override
  public Map<String, List<MetricResult>> next() {
    String record = jsonIterator.next();
    return this.calculator.measureAsMetricResult(record);
  }
}
