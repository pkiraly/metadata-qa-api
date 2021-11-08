package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import de.gwdg.metadataqa.api.calculator.CalculatorFacade;
import de.gwdg.metadataqa.api.configuration.ConfigurationReader;
import de.gwdg.metadataqa.api.configuration.MeasurementConfiguration;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.model.PathCacheFactory;
import de.gwdg.metadataqa.api.model.pathcache.CsvPathCache;
import de.gwdg.metadataqa.api.rule.CheckerTestBase;
import de.gwdg.metadataqa.api.rule.RuleChecker;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.CsvAwareSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.CsvReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ContentTypeCheckerTest extends CheckerTestBase {

  @Test
  public void prefix() {
    assertEquals("contentType", ContentTypeChecker.PREFIX);
  }

  @Test
  public void success() {
    cache = (CsvPathCache) PathCacheFactory.getInstance(schema.getFormat(), "http://vb.uni-wuerzburg.de/ub/books/mchf91/folio-std/DE-20__M_ch_f_91__0003__0002r.jpg");
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));

    ContentTypeChecker checker = new ContentTypeChecker(schema.getPathByLabel("name"),
      Arrays.asList("image/jpeg", "image/png", "image/tiff", "image/tiff-fx", "image/gif", "image/svg+xml", "application/pdf"));

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("name:contentType", checker.getHeaderWithoutId());
    Assert.assertEquals(RuleCheckingOutputType.PASSED, fieldCounter.get(checker.getHeader()).getType());
  }

  @Test
  public void failure() {
    cache = (CsvPathCache) PathCacheFactory.getInstance(schema.getFormat(), "http://creativecommons.org/licenses/by-nc-sa/4.0/");
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));

    ContentTypeChecker checker = new ContentTypeChecker(schema.getPathByLabel("name"),
      Arrays.asList("c", "d"));

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("name:contentType", checker.getHeaderWithoutId());
    assertEquals(RuleCheckingOutputType.FAILED, fieldCounter.get(checker.getHeader()).getType());
  }

  @Test
  public void schema() throws IOException, CsvValidationException {
    String schemaFile = "src/test/resources/configuration/schema/with-content-type-checker.yaml";
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

    System.err.println("Calculators: " + facade.getCalculators().size());
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
    assertEquals(2, result.size());
    assertEquals(
      Arrays.asList("1.0", "1", "1", "1", "0"),
      result.get(0));
    assertEquals(
      Arrays.asList("1.0", "1", "1", "-9", "-9"),
      result.get(1));



  }

}