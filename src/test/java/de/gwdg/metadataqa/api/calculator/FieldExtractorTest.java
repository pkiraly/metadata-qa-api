package de.gwdg.metadataqa.api.calculator;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import de.gwdg.metadataqa.api.configuration.ConfigurationReader;
import de.gwdg.metadataqa.api.configuration.MeasurementConfiguration;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.selector.JsonSelector;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.CsvAwareSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.CsvReader;
import de.gwdg.metadataqa.api.util.FileUtils;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class FieldExtractorTest {

  FieldExtractor calculator;
  JsonSelector cache;

  @Before
  public void setUp() throws URISyntaxException, IOException {
    calculator = new FieldExtractor("$.identifier");
    cache = new JsonSelector(FileUtils.readFirstLineFromResource("general/test.json"));
  }

  @Test
  public void testId() throws URISyntaxException, IOException {
    List<MetricResult> results = calculator.measure(cache);
    assertEquals("92062/BibliographicResource_1000126015451", results.get(0).getResultMap().get(calculator.FIELD_NAME));
  }

  @Test
  public void testGetHeaders() {
    List<String> expected = Arrays.asList("recordId");
    assertEquals(1, calculator.getHeader().size());
    assertEquals(expected, calculator.getHeader());
  }

  @Test
  public void testHeader() throws URISyntaxException, IOException, CsvValidationException {
    CalculatorFacade facade = configureTest();
    assertEquals(List.of("url"), facade.getHeader());
  }

  @Test
  public void testHeaderWithComma() throws URISyntaxException, IOException, CsvValidationException {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField(new DataElement("url"))
      .addField(new DataElement("name,with,comma").setExtractable());

    assertFalse(schema.getPathByLabel("url").isMandatory());

    MeasurementConfiguration config = new MeasurementConfiguration()
      .enableFieldExtractor()
      .disableCompletenessMeasurement();

    CalculatorFacade facade = new CalculatorFacade(config)
      .setSchema(schema)
      .setCsvReader(new CsvReader().setHeader(((CsvAwareSchema) schema).getHeader()));

    assertEquals(List.of("\"name,with,comma\""), facade.getHeader());
  }

  @Test
  public void noId() throws URISyntaxException, IOException, CsvValidationException {
    CalculatorFacade facade = configureTest();

    assertEquals(1, facade.getSchema().getExtractableFields().size());
    assertTrue(facade.getSchema().getExtractableFields().containsKey("url"));
    assertEquals("url", facade.getSchema().getExtractableFields().get("url"));

    assertEquals(List.of("url"), facade.getHeader());

    String fileName = "src/test/resources/csv/meemoo-simple.csv";

    CSVIterator iterator = new CSVIterator(new CSVReaderHeaderAware(new FileReader(fileName)));
    List<List<String>> result = new ArrayList<>();
    while (iterator.hasNext()) {
      String line = CsvReader.toCsv(iterator.next());
      result.add(facade.measureAsList(line));
    }
    assertEquals(2, result.size());
    assertEquals(List.of("https://neurovault.org/images/384958/"), result.get(0));
    assertEquals(List.of("https://neurovault.org/images/93390/"), result.get(1));
  }

  @Test
  public void vAndA() throws URISyntaxException, IOException, CsvValidationException {
    String schemaFile = "src/test/resources/configuration/schema/v-and-a.schema.yaml";
    Schema schema = ConfigurationReader.readSchemaYaml(schemaFile).asSchema();

    CalculatorFacade facade = new CalculatorFacade(
      new MeasurementConfiguration()
        .enableCompletenessMeasurement()
        .enableFieldCardinalityMeasurement()
        .enableFieldExtractor()
      )
      .setSchema(schema);
    facade.configure();

    String content = FileUtils.readContentFromResource("general/v-and-a-sample-01.json");
    assertEquals(Arrays.asList("\"Hurry, Leslie\"", "", "0.5", "1", "0", "1", "0", "1", "0", "1", "0"), facade.measureAsList(content));
    assertEquals("\"Hurry, Leslie\",,0.5,1,0,1,0,1,0,1,0", facade.measure(content));
  }

  @Test
  public void issue82() throws URISyntaxException, IOException, CsvValidationException {
    String schemaFile = "src/test/resources/configuration/schema/issue82.yaml";
    Schema schema = ConfigurationReader.readSchemaYaml(schemaFile).asSchema();

    CalculatorFacade facade = new CalculatorFacade(
      new MeasurementConfiguration()
        .enableCompletenessMeasurement()
        .enableFieldCardinalityMeasurement()
        .enableFieldExtractor()
      )
      .setSchema(schema);
    facade.configure();

    String content = FileUtils.readContentFromResource("general/issue82-sample-input.json");
    assertEquals(Arrays.asList("recordId", "completeness:TOTAL", "completeness:mandatory_if_present", "existence:recordId", "cardinality:recordId"), facade.getHeader());
    assertEquals(
      Arrays.asList("0000008c000a4b2cb90eefb7b131289d728fc57cc25946c2aca6ccb0820857da69f1ef620c2b4c99a668cb38062bf45c", "1.0", "1.0", "1", "1"),
      facade.measureAsList(content));
    assertEquals("0000008c000a4b2cb90eefb7b131289d728fc57cc25946c2aca6ccb0820857da69f1ef620c2b4c99a668cb38062bf45c,1.0,1.0,1,1",
      facade.measure(content));
  }

  private CalculatorFacade configureTest() {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField(new DataElement("url").setExtractable())
      .addField(new DataElement("name"));

    MeasurementConfiguration config = new MeasurementConfiguration()
      .enableFieldExtractor()
      .disableCompletenessMeasurement();

    CalculatorFacade facade = new CalculatorFacade(config)
      .setSchema(schema)
      .setCsvReader(new CsvReader().setHeader(((CsvAwareSchema) schema).getHeader()));

    return facade;
  }


}
