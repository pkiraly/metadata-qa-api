package de.gwdg.metadataqa.api.io.writer;

import com.opencsv.CSVWriter;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.util.CompressionLevel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CSVResultWriter extends ResultWriter {
  protected final CSVWriter csvWriter;

  public CSVResultWriter(String outputFile) throws IOException {
    super(outputFile);
    this.csvWriter = new CSVWriter(outputWriter);
  }

  public CSVResultWriter() {
    super();
    this.csvWriter = new CSVWriter(outputWriter);
  }

  @Override
  public void writeResult(Map<String, List<MetricResult>> result) throws IOException {
    List<String> output = new ArrayList<>();

    for (Map.Entry<String, List<MetricResult>> entry : result.entrySet())
      for (MetricResult metricResult : entry.getValue()) {
        List<String> list = metricResult.getList(false, CompressionLevel.NORMAL);
        output.addAll(list);
      }

    this.csvWriter.writeNext(output.toArray(new String[0]));
  }

  @Override
  public void writeHeader(List<String> header) throws IOException {
    List<String> outputHeader = header.stream()
      .map(
        s -> s.replaceAll("(:|/|\\.)", "_")
               .toLowerCase()
      )
      .collect(Collectors.toList());
    this.csvWriter.writeNext(outputHeader.toArray(new String[0]));
  }
}
