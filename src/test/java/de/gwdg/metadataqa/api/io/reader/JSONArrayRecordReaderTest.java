package de.gwdg.metadataqa.api.io.reader;

import de.gwdg.metadataqa.api.calculator.CalculatorFacade;
import de.gwdg.metadataqa.api.configuration.MeasurementConfiguration;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.io.IOTestBase;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JSONArrayRecordReaderTest extends IOTestBase {

  String inputFile = "src/test/resources/json/edm.json";
  BufferedReader inputReader;
  RecordReader reader;

  @Before
  public void setUp() throws Exception {
    inputReader = Files.newBufferedReader(Paths.get(inputFile));
  }

  @Test
  public void testHasNext() throws IOException {
    reader = new JSONArrayRecordReader(inputReader, getCalculator(Format.JSON));
    assertTrue(reader.hasNext());
  }

  @Test
  public void testNext_dcTitle() throws IOException {
    reader = new JSONArrayRecordReader(inputReader, getCalculator(Format.JSON, "dcTitle"));

    Map<String, List<MetricResult>> result = reader.next();
    assertNotNull(result);
    assertTrue(result instanceof LinkedHashMap);
    assertEquals(1, result.size());
  }

  @Test
  public void testNext_dcDate() throws IOException {
    reader = new JSONArrayRecordReader(inputReader, getCalculator(Format.JSON, "dcDate"));

    Map<String, List<MetricResult>> result = reader.next();
    assertNotNull(result);
    assertTrue(result instanceof LinkedHashMap);
    assertEquals(1, result.size());
  }

  protected CalculatorFacade getCalculator(Format format, String field) {
    MeasurementConfiguration config = new MeasurementConfiguration()
        .enableFieldExtractor()
        .disableCompletenessMeasurement();

    CalculatorFacade calculator = new CalculatorFacade(config).setSchema(getSchema(format, field));

    return calculator;
  }

  protected Schema getSchema(Format format, String field) {
    System.err.println(getPath(field));
    return new BaseSchema()
      .setFormat(format)
      .addField(
        new DataElement(getPath(field))
          .setExtractable()
          .setAsLanguageTagged()
      )
      // .addField(new DataElement("name").setExtractable())
      ;
  }

  private String getPath(String field) {
    return String.format("$.['object']['proxies'][?(@['europeanaProxy'] == false)]['%s']", field);
  }
}