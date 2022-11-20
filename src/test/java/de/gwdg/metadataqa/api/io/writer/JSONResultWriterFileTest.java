package de.gwdg.metadataqa.api.io.writer;

import de.gwdg.metadataqa.api.io.IOTestBase;
import de.gwdg.metadataqa.api.io.reader.CSVRecordReader;
import de.gwdg.metadataqa.api.io.reader.RecordReader;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.util.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JSONResultWriterFileTest extends IOTestBase {

  String inputFile = "src/test/resources/csv/meemoo-simple.csv";
  String outputFileName = "src/test/resources/output/output.json";
  File outputFile;
  RecordReader reader;
  ResultWriter writer;

  @Before
  public void setUp() throws Exception {
    BufferedReader inputReader = Files.newBufferedReader(Paths.get(inputFile));
    reader = new CSVRecordReader(inputReader, getCalculator(Format.CSV));
    writer = new JSONResultWriter(outputFileName);
    outputFile = new File(outputFileName);
  }

  @After
  public void tearDown() throws Exception {
    if (outputFile.exists())
      outputFile.delete();
  }

  @Test
  public void writeResult() throws IOException, URISyntaxException, InterruptedException {
    while (reader.hasNext()) {
      writer.writeResult(reader.next());
    }
    writer.close();

    assertTrue(outputFile.exists());

    List<String> output = FileUtils.readLinesFromFile(outputFileName);
    assertEquals(2, output.size());
    assertEquals(
      "{\"fieldExtractor\":{\"fieldExtractor\":{\"url\":\"https://neurovault.org/images/384958/\",\"name\":\"massivea uditory lexical decision\"}}}",
      output.get(0).trim());
    assertEquals(
      "{\"fieldExtractor\":{\"fieldExtractor\":{\"url\":\"https://neurovault.org/images/93390/\",\"name\":\"Language in the aging brain\"}}}",
      output.get(1).trim());
  }
}