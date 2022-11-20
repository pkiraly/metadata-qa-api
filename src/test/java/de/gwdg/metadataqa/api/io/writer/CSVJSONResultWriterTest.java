package de.gwdg.metadataqa.api.io.writer;

import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.io.IOTestBase;
import de.gwdg.metadataqa.api.io.reader.CSVRecordReader;
import de.gwdg.metadataqa.api.io.reader.RecordReader;
import de.gwdg.metadataqa.api.schema.Format;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CSVJSONResultWriterTest extends IOTestBase {

  String inputFile = "src/test/resources/csv/meemoo-simple.csv";
  RecordReader reader;
  ResultWriter writer;
  PrintStream standardOut = System.out;
  ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

  @Before
  public void setUp() throws Exception {
    System.setOut(new PrintStream(outputStreamCaptor));

    BufferedReader inputReader = Files.newBufferedReader(Paths.get(inputFile));
    reader = new CSVRecordReader(inputReader, getCalculator(Format.CSV));
    writer = new CSVJSONResultWriter();
  }

  @After
  public void tearDown() throws Exception {
    System.setOut(standardOut);
  }

  @Test
  public void writeResult() throws IOException {
    Map<String, List<MetricResult>> result = reader.next();
    writer.writeResult(result);
    writer.close();

    assertEquals(
      "\"{\"\"fieldExtractor\"\":{\"\"fieldExtractor\"\":{\"\"url\"\":\"\"https://neurovault.org/images/384958/\"\",\"\"name\"\":\"\"massivea uditory lexical decision\"\"}}}\"",
      outputStreamCaptor.toString().trim());
  }

  @Test
  public void writeHeader() throws IOException {
    List<String> header = getCalculator(Format.CSV).getHeader();
    writer.writeHeader(header);
    writer.close();

    assertEquals("\"json_data\"", outputStreamCaptor.toString().trim());
  }
}