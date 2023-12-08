package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import de.gwdg.metadataqa.api.calculator.CalculatorFacade;
import de.gwdg.metadataqa.api.configuration.ConfigurationReader;
import de.gwdg.metadataqa.api.configuration.MeasurementConfiguration;
import de.gwdg.metadataqa.api.rule.CheckerTestBase;
import de.gwdg.metadataqa.api.schema.CsvAwareSchema;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.CsvReader;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HiddenCheckerTest extends CheckerTestBase {

  @Test
  public void schema() throws IOException, CsvValidationException {
    String schemaFile = "src/test/resources/configuration/schema/rules/extra/with-hidden-checker.yaml";
    Schema schema = ConfigurationReader.readSchemaYaml(schemaFile).asSchema();

    CalculatorFacade facade = new CalculatorFacade(
      new MeasurementConfiguration()
        .enableCompletenessMeasurement()
        .enableFieldCardinalityMeasurement()
        .enableFieldExtractor()
        .enableRuleCatalogMeasurement()
      )
      .setSchema(schema)
      .setCsvReader(new CsvReader().setHeader(((CsvAwareSchema) schema).getHeader()));
    facade.configure();

    assertEquals(2, facade.getCalculators().size());
    assertEquals("RuleCatalog", facade.getCalculators().get(1).getClass().getSimpleName());
    assertEquals(
      Arrays.asList("completeness:TOTAL", "existence:thumbnail", "cardinality:thumbnail",
        "thumbnail:and:thumbnail:dependency:thumbnail:imageDimension:3.1", "ruleCatalog:score"),
      facade.getHeader());

    String fileName = "src/test/resources/csv/dependency-checker.csv";
    CSVIterator iterator = new CSVIterator(new CSVReaderHeaderAware(new FileReader(fileName)));
    List<List<String>> result = new ArrayList<>();
    while (iterator.hasNext()) {
      String line = CsvReader.toCsv(iterator.next());
      result.add(facade.measureAsList(line));
    }
    assertEquals(1, result.size());
    /*
    assertEquals(
      Arrays.asList("1.0", "1", "1", "0", "0"),
      result.get(0));
     */
  }
}