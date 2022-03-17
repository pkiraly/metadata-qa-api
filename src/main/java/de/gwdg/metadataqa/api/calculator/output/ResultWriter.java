package de.gwdg.metadataqa.api.calculator.output;

import de.gwdg.metadataqa.api.interfaces.MetricResult;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public abstract class ResultWriter implements Closeable {

  protected final BufferedWriter outputWriter;

  public ResultWriter(String outputFile) throws IOException {
    Path outputPath = Paths.get(outputFile);
    this.outputWriter = Files.newBufferedWriter(outputPath);
  }

  public ResultWriter() {
    this.outputWriter = new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8));
  }

  @Override
  public void close() throws IOException {
    this.outputWriter.flush();
    this.outputWriter.close();
  }

  abstract void writeResult(Map<String, List<MetricResult>> result) throws IOException;
  abstract void writeHeader(List<String> header) throws IOException;
}
