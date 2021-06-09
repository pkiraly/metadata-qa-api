package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.PathCacheFactory;
import de.gwdg.metadataqa.api.model.pathcache.CsvPathCache;
import de.gwdg.metadataqa.api.rule.RuleChecker;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
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
    assertEquals(RuleCheckingOutputType.PASSED, run(0.9, TYPE.MIN_INCLUSIVE).getType());
    assertEquals(RuleCheckingOutputType.PASSED, run(1, TYPE.MIN_INCLUSIVE).getType());
  }

  @Test
  public void minInclusive_failed() {
    assertEquals(RuleCheckingOutputType.FAILED, run(1.1, TYPE.MIN_INCLUSIVE).getType());
  }

  @Test
  public void maxInclusive_passed() {
    assertEquals(RuleCheckingOutputType.PASSED, run(1, TYPE.MAX_INCLUSIVE).getType());
    assertEquals(RuleCheckingOutputType.PASSED, run(1.1, TYPE.MAX_INCLUSIVE).getType());
  }

  @Test
  public void maxInclusive_failed() {
    assertEquals(RuleCheckingOutputType.FAILED, run(0.9, TYPE.MAX_INCLUSIVE).getType());
  }

  @Test
  public void minExclusive_passed() {
    assertEquals(RuleCheckingOutputType.PASSED, run(0.9, TYPE.MIN_EXCLUSIVE).getType());
  }

  @Test
  public void minExclusive_failed() {
    assertEquals(RuleCheckingOutputType.FAILED, run(1, TYPE.MIN_EXCLUSIVE).getType());
  }

  @Test
  public void maxExclusive_passed() {
    assertEquals(RuleCheckingOutputType.PASSED, run(1.1, TYPE.MAX_EXCLUSIVE).getType());
  }

  @Test
  public void maxExclusive_failed() {
    assertEquals(RuleCheckingOutputType.FAILED, run(1, TYPE.MAX_EXCLUSIVE).getType());
  }

  @Test
  public void nonnumeric() {
    assertEquals(RuleCheckingOutputType.FAILED, run("alt", 1, TYPE.MAX_EXCLUSIVE).getType());
  }

  private RuleCheckerOutput run(double limit, NumericValueChecker.TYPE type) {
    return run("name", limit, type);
  }

  private RuleCheckerOutput run(String field, double limit, NumericValueChecker.TYPE type) {
    RuleChecker checker = new NumericValueChecker(schema.getPathByLabel(field), limit, type);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    return fieldCounter.get(checker.getHeader());
  }
}