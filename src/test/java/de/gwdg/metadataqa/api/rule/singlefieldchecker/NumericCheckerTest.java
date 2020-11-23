package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.PathCacheFactory;
import de.gwdg.metadataqa.api.model.pathcache.CsvPathCache;
import de.gwdg.metadataqa.api.rule.RuleChecker;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutput;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.CsvAwareSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.CsvReader;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.NumericValueChecker.TYPE;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumericCheckerTest {

  protected Schema schema;
  protected CsvPathCache cache;

  @Before
  public void setUp() throws Exception {
    schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField("name")
      .addField("title")
      .addField("alt")
    ;

    cache = (CsvPathCache) PathCacheFactory.getInstance(schema.getFormat(), "1,2,auto");
    cache.setCsvReader(new CsvReader().setHeader(((CsvAwareSchema) schema).getHeader()));
  }

  @Test
  public void minInclusive_passed() {
    Assert.assertEquals(RuleCheckingOutput.PASSED, run(0.9, TYPE.MinInclusive));
    assertEquals(RuleCheckingOutput.PASSED, run(1, TYPE.MinInclusive));
  }

  @Test
  public void minInclusive_failed() {
    assertEquals(RuleCheckingOutput.FAILED, run(1.1, TYPE.MinInclusive));
  }

  @Test
  public void maxInclusive_passed() {
    assertEquals(RuleCheckingOutput.PASSED, run(1, TYPE.MaxInclusive));
    assertEquals(RuleCheckingOutput.PASSED, run(1.1, TYPE.MaxInclusive));
  }

  @Test
  public void maxInclusive_failed() {
    assertEquals(RuleCheckingOutput.FAILED, run(0.9, TYPE.MaxInclusive));
  }

  @Test
  public void minExclusive_passed() {
    assertEquals(RuleCheckingOutput.PASSED, run(0.9, TYPE.MinExclusive));
  }

  @Test
  public void minExclusive_failed() {
    assertEquals(RuleCheckingOutput.FAILED, run(1, TYPE.MinExclusive));
  }

  @Test
  public void maxExclusive_passed() {
    assertEquals(RuleCheckingOutput.PASSED, run(1.1, TYPE.MaxExclusive));
  }

  @Test
  public void maxExclusive_failed() {
    assertEquals(RuleCheckingOutput.FAILED, run(1, TYPE.MaxExclusive));
  }

  @Test
  public void nonnumeric() {
    assertEquals(RuleCheckingOutput.FAILED, run("alt", 1, TYPE.MaxExclusive));
  }

  private RuleCheckingOutput run(double limit, NumericValueChecker.TYPE type) {
    return run("name", limit, type);
  }

  private RuleCheckingOutput run(String field, double limit, NumericValueChecker.TYPE type) {
    RuleChecker checker = new NumericValueChecker(schema.getPathByLabel(field), limit, type);

    FieldCounter<RuleCheckingOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    return fieldCounter.get(checker.getHeader());
  }
}