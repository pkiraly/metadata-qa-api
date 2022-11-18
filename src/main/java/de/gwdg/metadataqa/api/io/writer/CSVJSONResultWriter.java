package de.gwdg.metadataqa.api.io.writer;

import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.json.JsonUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CSVJSONResultWriter extends CSVResultWriter {

  public CSVJSONResultWriter(String outputFile) throws IOException {
    super(outputFile);
  }

  public CSVJSONResultWriter() {
    super();
  }

  @Override
  public void writeResult(Map<String, List<MetricResult>> result) throws IOException {
    Map<String, Object> map = new LinkedHashMap<>();
    for (Map.Entry<String, List<MetricResult>> entry : result.entrySet()) {
      Map<String, Object> calcResult = new LinkedHashMap<>();

      for (MetricResult metricResult : entry.getValue()) {
        calcResult.put(metricResult.getName(), metricResult.getResultMap());
      }
      map.put(entry.getKey(), calcResult);
    }

    String json = JsonUtils.toJson(map);

    csvWriter.writeNext(new String[]{json});
  }

  @Override
  public void writeHeader(List<String> header) throws IOException {
    this.csvWriter.writeNext(new String[]{"json_data"});
  }
}
