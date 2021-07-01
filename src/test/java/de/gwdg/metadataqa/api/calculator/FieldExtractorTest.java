package de.gwdg.metadataqa.api.calculator;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import de.gwdg.metadataqa.api.configuration.MeasurementConfiguration;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.pathcache.JsonPathCache;
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
  JsonPathCache cache;

  @Before
  public void setUp() throws URISyntaxException, IOException {
    calculator = new FieldExtractor("$.identifier");
    cache = new JsonPathCache(FileUtils.readFirstLineFromResource("general/test.json"));
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
  public void noId() throws URISyntaxException, IOException, CsvValidationException {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField(new JsonBranch("url").setExtractable())
      .addField(new JsonBranch("name"));

    MeasurementConfiguration config = new MeasurementConfiguration()
      .enableFieldExtractor()
      .disableCompletenessMeasurement();

    CalculatorFacade facade = new CalculatorFacade(config)
      .setSchema(schema)
      .setCsvReader(new CsvReader().setHeader(((CsvAwareSchema) schema).getHeader()));

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
}
