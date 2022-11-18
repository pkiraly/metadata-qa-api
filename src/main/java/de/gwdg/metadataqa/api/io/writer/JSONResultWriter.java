package de.gwdg.metadataqa.api.io.writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.json.JsonUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JSONResultWriter extends ResultWriter {

  public JSONResultWriter(String outputFile) throws IOException {
    super(outputFile);
  }

  public JSONResultWriter() {
  }

  private Object getJson(Map<String, List<MetricResult>> result) {
    Map<String, Object> output = new LinkedHashMap<>();

    for (Map.Entry<String, List<MetricResult>> entry : result.entrySet()) {
      Map<String, Object> calcResult = new LinkedHashMap<>();

      for (MetricResult metricResult : entry.getValue()) {
        calcResult.put(metricResult.getName(), metricResult.getResultMap());
      }
      output.put(entry.getKey(), calcResult);
    }

    try {
      return JsonUtils.toJson(output);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public void writeResult(Map<String, List<MetricResult>> result) throws IOException {
    Object json = getJson(result);

    this.outputWriter.write(json.toString());
    this.outputWriter.newLine();
  }

  @Override
  public void writeHeader(List<String> header) throws IOException {}
}
