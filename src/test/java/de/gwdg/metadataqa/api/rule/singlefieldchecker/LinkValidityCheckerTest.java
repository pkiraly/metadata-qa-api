package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import de.gwdg.metadataqa.api.calculator.CalculatorFacade;
import de.gwdg.metadataqa.api.configuration.ConfigurationReader;
import de.gwdg.metadataqa.api.configuration.MeasurementConfiguration;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.selector.CsvSelector;
import de.gwdg.metadataqa.api.model.selector.SelectorFactory;
import de.gwdg.metadataqa.api.rule.CheckerTestBase;
import de.gwdg.metadataqa.api.rule.RuleChecker;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.schema.CsvAwareSchema;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.CsvReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LinkValidityCheckerTest extends CheckerTestBase {

  @Test
  public void prefix() {
    assertEquals("validLink", LinkValidityChecker.PREFIX);
  }

  @Test
  public void success() {
    cache = (CsvSelector) SelectorFactory.getInstance(schema.getFormat(),
      "\"https://live.staticflickr.com/65535/51913751065_71aa3cf8e6_b.jpg\"");
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));

    LinkValidityChecker checker = new LinkValidityChecker(
      schema.getPathByLabel("name"),
      true
    );
    assertFalse(checker.countInstances());

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:validLink", checker.getHeaderWithoutId());
    Assert.assertEquals(
      RuleCheckingOutputStatus.PASSED,
      fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
    Assert.assertNull(fieldCounter.get(checker.getHeader(RuleCheckingOutputType.SCORE)).getScore());
    Assert.assertEquals(0,
      (int) fieldCounter.get(checker.getHeader(RuleCheckingOutputType.SCORE)).getInstanceCount());
    Assert.assertEquals(0,
      (int) fieldCounter.get(checker.getHeader(RuleCheckingOutputType.SCORE)).getFailureCount());
  }

  @Test
  public void success_withCountInstance() {
    cache = (CsvSelector) SelectorFactory.getInstance(schema.getFormat(),
      "\"https://live.staticflickr.com/65535/51913751065_71aa3cf8e6_b.jpg\"");
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));

    LinkValidityChecker checker = new LinkValidityChecker(
      schema.getPathByLabel("name"),
      true
    );
    checker.setCountInstances(true);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:validLink", checker.getHeaderWithoutId());
    Assert.assertEquals(RuleCheckingOutputStatus.PASSED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
    Assert.assertNull(fieldCounter.get(checker.getHeader(RuleCheckingOutputType.SCORE)).getScore());
    Assert.assertEquals(1, (int) fieldCounter.get(checker.getHeader(RuleCheckingOutputType.SCORE)).getInstanceCount());
    Assert.assertEquals(0, (int) fieldCounter.get(checker.getHeader(RuleCheckingOutputType.SCORE)).getFailureCount());
  }

  @Test
  public void failure() {
    cache = (CsvSelector) SelectorFactory.getInstance(schema.getFormat(),
      "http://creativecommons.org/licenses/by-nc-sa/4.0/");
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));

    LinkValidityChecker checker = new LinkValidityChecker(schema.getPathByLabel("name"), false);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:validLink", checker.getHeaderWithoutId());
    assertEquals(RuleCheckingOutputStatus.FAILED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }

  @Test
  public void schema() throws IOException, CsvValidationException {
    String schemaFile = "src/test/resources/configuration/schema/rules/extra/with-content-type-checker.yaml";
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
      Arrays.asList("completeness:TOTAL", "existence:thumbnail", "cardinality:thumbnail", "thumbnail:contentType:3.1", "ruleCatalog:score"),
      facade.getHeader());

    String fileName = "src/test/resources/csv/content-type-checker.csv";
    CSVIterator iterator = new CSVIterator(new CSVReaderHeaderAware(new FileReader(fileName)));
    List<List<String>> result = new ArrayList<>();
    while (iterator.hasNext()) {
      String line = CsvReader.toCsv(iterator.next());
      result.add(facade.measureAsList(line));
    }
    assertEquals(3, result.size());
    /*
    assertEquals(
      Arrays.asList("1.0", "1", "1", "0", "0"),
      result.get(0));
     */
    assertEquals(
      Arrays.asList("1.0", "1", "1", "-9", "-9"),
      result.get(1));
  }

  @Test
  public void unaccessible() {
    cache = (CsvSelector) SelectorFactory.getInstance(schema.getFormat(),
      "http://vb.uni-wuerzburg.de/ub/books/36z1197_57156733/folio-std/unexisting.jpg");
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));

    LinkValidityChecker checker = new LinkValidityChecker(schema.getPathByLabel("name"), true);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:validLink", checker.getHeaderWithoutId());
    Assert.assertEquals(RuleCheckingOutputStatus.FAILED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }

  @Test
  public void t301() {
    cache = (CsvSelector) SelectorFactory.getInstance(schema.getFormat(),
      "http://creativecommons.org/licenses/by-nc-sa/4.0/");
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));

    LinkValidityChecker checker = new LinkValidityChecker(schema.getPathByLabel("name"), true);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();

    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:validLink", checker.getHeaderWithoutId());
    assertEquals(RuleCheckingOutputStatus.PASSED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }

}