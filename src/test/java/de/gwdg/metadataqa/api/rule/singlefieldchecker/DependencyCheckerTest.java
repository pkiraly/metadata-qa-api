package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import de.gwdg.metadataqa.api.calculator.CalculatorFacade;
import de.gwdg.metadataqa.api.configuration.ConfigurationReader;
import de.gwdg.metadataqa.api.configuration.MeasurementConfiguration;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.rule.CheckerTestBase;
import de.gwdg.metadataqa.api.rule.RuleChecker;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DependencyCheckerTest extends CheckerTestBase {

  @Test
  public void prefix() {
    assertEquals("dependency", DependencyChecker.PREFIX);
  }

  @Test
  public void schema() throws IOException, CsvValidationException {
    String schemaFile = "src/test/resources/configuration/schema/rules/extra/with-dependency-checker.yaml";
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

    for (RuleChecker ruleChecker : schema.getRuleCheckers()) {
      System.err.println(ruleChecker.getClass());
    }

    assertEquals(2, facade.getCalculators().size());
    assertEquals("RuleCatalog", facade.getCalculators().get(1).getClass().getSimpleName());
    assertEquals(
      Arrays.asList("completeness:TOTAL", "existence:thumbnail", "cardinality:thumbnail", "thumbnail:pattern:3.0",
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
      Arrays.asList("1.0", "1", "1", "0", "0", "0"),
      result.get(0));
     */
  }

  @Test
  public void getResult_single_passed() {
    FieldCounter<RuleCheckerOutput> globalResults = new FieldCounter<>();
    globalResults.put("Q1", new RuleCheckerOutput(RuleCheckingOutputStatus.PASSED, 0));

    DependencyChecker checker = new DependencyChecker(new DataElement("a"), List.of("Q1"));
    assertTrue(checker.getResult(RuleCheckingOutputType.STATUS, globalResults).get("allPassed"));
  }

  @Test
  public void getResult_single_failed() {
    FieldCounter<RuleCheckerOutput> globalResults = new FieldCounter<>();
    globalResults.put("Q1", new RuleCheckerOutput(RuleCheckingOutputStatus.FAILED, 0));

    DependencyChecker checker = new DependencyChecker(new DataElement("a"), List.of("Q1"));
    assertFalse(checker.getResult(RuleCheckingOutputType.STATUS, globalResults).get("allPassed"));
  }

  @Test
  public void getResult_single_na() {
    FieldCounter<RuleCheckerOutput> globalResults = new FieldCounter<>();
    globalResults.put("Q1", new RuleCheckerOutput(RuleCheckingOutputStatus.NA, 0));

    DependencyChecker checker = new DependencyChecker(new DataElement("a"), List.of("Q1"));
    assertFalse(checker.getResult(RuleCheckingOutputType.STATUS, globalResults).get("allPassed"));
  }
}