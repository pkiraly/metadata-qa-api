package de.gwdg.metadataqa.api.io.reader;

import de.gwdg.metadataqa.api.calculator.CalculatorFacade;
import de.gwdg.metadataqa.api.configuration.MeasurementConfiguration;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CSVRecordReaderTest {

  String inputFile = "src/test/resources/csv/meemoo-simple.csv";
  CSVRecordReader csvReader;

  @Before
  public void setUp() throws Exception {
    Path inputPath = Paths.get(inputFile);
    BufferedReader inputReader = Files.newBufferedReader(inputPath);
    CalculatorFacade facade = getCalculator();
    csvReader = new CSVRecordReader(inputReader, facade);
  }

  @Test
  public void hasNext() {
    assertTrue(csvReader.hasNext());
  }

  @Test
  public void next() {
    Map<String, List<MetricResult>> result = csvReader.next();
    assertNotNull(result);
    assertTrue(result instanceof LinkedHashMap);
    assertEquals(1, result.size());
    List<MetricResult> fields = result.get("fieldExtractor");
    assertEquals(1, fields.size());
    assertEquals("https://neurovault.org/images/384958/", fields.get(0).getResultMap().get("url"));
    assertEquals("massivea uditory lexical decision", fields.get(0).getResultMap().get("name"));
  }

  private CalculatorFacade getCalculator() {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField(new JsonBranch("url").setExtractable())
      .addField(new JsonBranch("name").setExtractable());

    MeasurementConfiguration config = new MeasurementConfiguration()
      .enableFieldExtractor()
      .disableCompletenessMeasurement();

    CalculatorFacade facade = new CalculatorFacade(config).setSchema(schema);

    return facade;
  }
}