package de.gwdg.metadataqa.api.rule.pairchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.PathCacheFactory;
import de.gwdg.metadataqa.api.model.pathcache.CsvPathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.CsvAwareSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.CsvReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DisjointCheckerTest {

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
    assertEquals("disjoint", DisjointChecker.PREFIX);
  }

  @Test
  public void success() {
    DisjointChecker checker = new DisjointChecker(
      schema.getPathByLabel("name"), schema.getPathByLabel("title"));

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(1, fieldCounter.size());
    assertEquals("name:disjoint:title", checker.getHeaderWithoutId());
    Assert.assertEquals(RuleCheckingOutputStatus.PASSED, fieldCounter.get(checker.getHeader()).getStatus());
  }

  @Test
  public void failure() {
    DisjointChecker checker = new DisjointChecker(
      schema.getPathByLabel("name"), schema.getPathByLabel("alt"));

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(1, fieldCounter.size());
    assertEquals("name:disjoint:alt", checker.getHeaderWithoutId());
    assertEquals(RuleCheckingOutputStatus.FAILED, fieldCounter.get(checker.getHeader()).getStatus());
  }

  @Test(expected = IllegalArgumentException.class)
  public void failure_constructor1() {
    DisjointChecker checker = new DisjointChecker(schema.getPathByLabel("nameX"), schema.getPathByLabel("altX"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void failure_constructor2() {
    DisjointChecker checker = new DisjointChecker(schema.getPathByLabel("nameX"), schema.getPathByLabel("altX"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void failure_constructor3() {
    DisjointChecker checker = new DisjointChecker(schema.getPathByLabel("name"), schema.getPathByLabel("altX"));
  }

}