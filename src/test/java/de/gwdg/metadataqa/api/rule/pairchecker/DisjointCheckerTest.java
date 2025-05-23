package de.gwdg.metadataqa.api.rule.pairchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.selector.SelectorFactory;
import de.gwdg.metadataqa.api.model.selector.CsvSelector;
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
import static org.junit.Assert.assertThrows;

public class DisjointCheckerTest {
  protected Schema schema;
  protected CsvSelector cache;

  @Before
  public void setUp() throws Exception {
    schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField("name")
      .addField("title")
      .addField("alt")
    ;

    cache = (CsvSelector) SelectorFactory.getInstance(schema.getFormat(), "a,b,a");
    cache.setCsvReader(new CsvReader().setHeader(((CsvAwareSchema) schema).getHeader()));
  }

  @Test
  public void prefix() {
    assertEquals("disjoint", DisjointChecker.PREFIX);
  }

  @Test
  public void success() {
    DisjointChecker checker = new DisjointChecker(schema.getPathByLabel("name"), schema.getPathByLabel("title"));
    checker.setId("1");
    checker.setDebug();

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:disjoint:title", checker.getHeaderWithoutId());
    Assert.assertEquals(RuleCheckingOutputStatus.PASSED, fieldCounter.get(checker.getIdOrHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }

  @Test
  public void failure() {
    DisjointChecker checker = new DisjointChecker(
      schema.getPathByLabel("name"), schema.getPathByLabel("alt"));

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:disjoint:alt", checker.getHeaderWithoutId());
    assertEquals(RuleCheckingOutputStatus.FAILED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }

  @Test
  public void failure_constructor1() {
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      DisjointChecker checker = new DisjointChecker(schema.getPathByLabel("nameX"), schema.getPathByLabel("altX"));
    });
    assertEquals("field1 should not be null", e.getMessage());
  }

  @Test
  public void failure_constructor2() {
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      DisjointChecker checker = new DisjointChecker(schema.getPathByLabel("nameX"), schema.getPathByLabel("altX"));
    });
    assertEquals("field1 should not be null", e.getMessage());
  }

  @Test
  public void failure_constructor3() {
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      DisjointChecker checker = new DisjointChecker(schema.getPathByLabel("name"), schema.getPathByLabel("altX"));
    });
    assertEquals("field2 should not be null", e.getMessage());
  }
}