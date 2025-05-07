package de.gwdg.metadataqa.api.io.reader;

import de.gwdg.metadataqa.api.calculator.CalculatorFacade;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class JSONArrayRecordReader extends RecordReader {

  private JSONArray records;
  private int current = 0;

  public JSONArrayRecordReader(BufferedReader inputReader, CalculatorFacade calculator) throws IOException {
    super(inputReader, calculator);
    StringBuilder jsonBuilder = new StringBuilder();
    String line;
    while ((line = inputReader.readLine()) != null) {
      jsonBuilder.append(line).append("\n");
    }
    records = (JSONArray) JSONValue.parse(jsonBuilder.toString());
  }

  @Override
  public boolean hasNext() {
    return records.size() > (current + 1);
  }

  @Override
  public Map<String, List<MetricResult>> next() {
    if (!hasNext())
      throw new NoSuchElementException();
    String record = records.get(current++).toString();
    return this.calculator.measureAsMetricResult(record);
  }
}
