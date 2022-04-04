package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import de.gwdg.metadataqa.api.calculator.CalculatorFacade;
import de.gwdg.metadataqa.api.configuration.ConfigurationReader;
import de.gwdg.metadataqa.api.configuration.MeasurementConfiguration;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.PathCacheFactory;
import de.gwdg.metadataqa.api.model.pathcache.CsvPathCache;
import de.gwdg.metadataqa.api.rule.CheckerTestBase;
import de.gwdg.metadataqa.api.rule.RuleChecker;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.schema.CsvAwareSchema;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.CsvReader;
import de.gwdg.metadataqa.api.util.Dimension;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ImageDimensionCheckerTest extends CheckerTestBase {

  @Test
  public void prefix() {
    assertEquals("contentType", ContentTypeChecker.PREFIX);
  }

  @Test
  public void successMinWidthHeight() {
    cache = (CsvPathCache) PathCacheFactory.getInstance(schema.getFormat(),
      "\"https://iiif.deutsche-digitale-bibliothek.de/image/2/b152b0e5-55da-453a-8a53-6530189e98ac/full/!800,600/0/default.jpg\"");
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));

    ImageDimensionChecker checker = new ImageDimensionChecker(schema.getPathByLabel("name"), new Dimension().withMinWidth(200).withMinHeight(200));

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:imageDimension", checker.getHeaderWithoutId());
    Assert.assertEquals(RuleCheckingOutputStatus.PASSED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }

  @Test
  public void successMinShortLong() {
    cache = (CsvPathCache) PathCacheFactory.getInstance(schema.getFormat(),
      "\"https://iiif.deutsche-digitale-bibliothek.de/image/2/b152b0e5-55da-453a-8a53-6530189e98ac/full/!800,600/0/default.jpg\"");
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));

    ImageDimensionChecker checker = new ImageDimensionChecker(schema.getPathByLabel("name"), new Dimension().withMinShortside(200).withMinLongside(200));

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:imageDimension", checker.getHeaderWithoutId());
    Assert.assertEquals(RuleCheckingOutputStatus.PASSED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }

  @Test
  public void failure() {
    cache = (CsvPathCache) PathCacheFactory.getInstance(schema.getFormat(),
      "\"https://iiif.deutsche-digitale-bibliothek.de/image/2/b152b0e5-55da-453a-8a53-6530189e98ac/full/!800,600/0/default.jpg\"");
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));

    ImageDimensionChecker checker = new ImageDimensionChecker(schema.getPathByLabel("name"), new Dimension().withMinWidth(700).withMinHeight(700));

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:imageDimension", checker.getHeaderWithoutId());
    assertEquals(RuleCheckingOutputStatus.FAILED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }

  @Test
  public void schema() throws IOException, CsvValidationException {
    String schemaFile = "src/test/resources/configuration/schema/with-image-dimension-checker.yaml";
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
      Arrays.asList("completeness:TOTAL", "existence:thumbnail", "cardinality:thumbnail", "thumbnail:imageDimension:3.1", "ruleCatalog:score"),
      facade.getHeader());

    String fileName = "src/test/resources/csv/content-type-checker.csv";
    CSVIterator iterator = new CSVIterator(new CSVReaderHeaderAware(new FileReader(fileName)));
    List<List<String>> result = new ArrayList<>();
    while (iterator.hasNext()) {
      String line = CsvReader.toCsv(iterator.next());
      result.add(facade.measureAsList(line));
    }
    assertEquals(3, result.size());
    assertEquals(
      Arrays.asList("1.0", "1", "1", "0", "0"),
      result.get(0));
    assertEquals(
      Arrays.asList("1.0", "1", "1", "-9", "-9"),
      result.get(1));
  }

  @Test
  public void unaccessible() {
    cache = (CsvPathCache) PathCacheFactory.getInstance(schema.getFormat(),
      "http://vb.uni-wuerzburg.de/ub/books/36z1197_57156733/folio-std/unexisting.jpg");
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));

    ImageDimensionChecker checker = new ImageDimensionChecker(schema.getPathByLabel("name"), new Dimension().withMinWidth(200).withMinHeight(200));

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:imageDimension", checker.getHeaderWithoutId());
    Assert.assertEquals(RuleCheckingOutputStatus.FAILED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }
}