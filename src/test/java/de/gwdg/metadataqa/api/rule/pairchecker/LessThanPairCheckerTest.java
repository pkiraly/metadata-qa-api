package de.gwdg.metadataqa.api.rule.pairchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.PathCacheFactory;
import de.gwdg.metadataqa.api.model.pathcache.CsvPathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutput;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.CsvAwareSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.CsvReader;
import de.gwdg.metadataqa.api.rule.pairchecker.LessThanPairChecker.TYPE;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LessThanPairCheckerTest {

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

    cache = (CsvPathCache) PathCacheFactory.getInstance(schema.getFormat(), "12,2,2");
    cache.setCsvReader(new CsvReader().setHeader(((CsvAwareSchema) schema).getHeader()));
  }

  @Test
  public void lessThan_success() {
    Assert.assertEquals(RuleCheckingOutput.PASSED, run("title", "name", TYPE.LessThan));
  }

  @Test
  public void lessThan_failure() {
    Assert.assertEquals(RuleCheckingOutput.FAILED, run("name", "title", TYPE.LessThan));
  }

  @Test
  public void lessThanOrEquals_success() {
    Assert.assertEquals(RuleCheckingOutput.PASSED, run("title", "alt", TYPE.LessThanOrEquals));
  }

  @Test
  public void lessThanOrEquals_failure() {
    Assert.assertEquals(RuleCheckingOutput.FAILED, run("name", "alt", TYPE.LessThanOrEquals));
  }

  @Test
  public void lessThan() {
    assertFalse(LessThanPairChecker.lessThan("alma", "alma"));
    assertTrue(LessThanPairChecker.lessThan("a", "b"));
    assertTrue(LessThanPairChecker.lessThan("1", "2"));
    assertTrue(LessThanPairChecker.lessThan("1.4", "2"));
    assertFalse(LessThanPairChecker.lessThan("14", "2"));
    assertTrue(LessThanPairChecker.lessThan("14", "20"));
  }

  @Test
  public void lessThanOrEquals() {
    assertTrue(LessThanPairChecker.lessThanOrEquals("alma", "alma"));
    assertTrue(LessThanPairChecker.lessThan("a", "b"));
    assertTrue(LessThanPairChecker.lessThan("1", "2"));
    assertTrue(LessThanPairChecker.lessThan("1.4", "2"));
    assertFalse(LessThanPairChecker.lessThan("14", "2"));
    assertTrue(LessThanPairChecker.lessThan("14", "20"));
    assertTrue(LessThanPairChecker.lessThan("14.8", "20"));
  }

  @Test
  public void isNumeric() {
    assertTrue(LessThanPairChecker.isNumeric("1.4"));
    assertTrue(LessThanPairChecker.isNumeric("2"));
    assertTrue(LessThanPairChecker.isNumeric("2.0"));
  }

  public RuleCheckingOutput run(String field1, String field2, TYPE type) {
    LessThanPairChecker checker = new LessThanPairChecker(
      schema.getPathByLabel(field1), schema.getPathByLabel(field2), type);

    FieldCounter<RuleCheckingOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals(String.format("%s:%s-%s", type.getPrefix(), field1, field2), checker.getHeader());
    return fieldCounter.get(checker.getHeader());
  }

}