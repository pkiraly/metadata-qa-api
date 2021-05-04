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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EqualityCheckerTest {

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

    cache = (CsvPathCache) PathCacheFactory.getInstance(schema.getFormat(), "a,b,a");
    cache.setCsvReader(new CsvReader().setHeader(((CsvAwareSchema) schema).getHeader()));
  }

  @Test
  public void prefix() {
    assertEquals("equals", EqualityChecker.PREFIX);
  }

  @Test
  public void success() {
    EqualityChecker checker = new EqualityChecker(
      schema.getPathByLabel("name"), schema.getPathByLabel("alt"));

    FieldCounter fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("equals:name-alt", checker.getHeader());
    Assert.assertEquals(RuleCheckingOutput.PASSED, fieldCounter.get(checker.getHeader()));
  }

  @Test
  public void failure() {
    EqualityChecker checker = new EqualityChecker(
      schema.getPathByLabel("name"), schema.getPathByLabel("title"));

    FieldCounter fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("equals:name-title", checker.getHeader());
    assertEquals(RuleCheckingOutput.FAILED, fieldCounter.get(checker.getHeader()));
  }
}