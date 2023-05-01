package de.gwdg.metadataqa.api.rule.logical;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.selector.SelectorFactory;
import de.gwdg.metadataqa.api.model.selector.CsvSelector;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.rule.pairchecker.DisjointChecker;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.CsvAwareSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.CsvReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NotCheckerTest {

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
    assertEquals("not", NotChecker.PREFIX);
  }

  @Test
  public void success() {
    NotChecker checker = new NotChecker(
      schema.getPathByLabel("name"),
      List.of(new DisjointChecker(schema.getPathByLabel("name"), schema.getPathByLabel("title"))));

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:not:name:disjoint:title", checker.getHeaderWithoutId());
    assertTrue(Pattern.compile("^name:not:name:disjoint:title:\\d+$").matcher(checker.getHeader()).matches());
    Assert.assertEquals(RuleCheckingOutputStatus.FAILED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }

  @Test
  public void failure() {
    NotChecker checker = new NotChecker(
      schema.getPathByLabel("name"),
      List.of(new DisjointChecker(schema.getPathByLabel("name"), schema.getPathByLabel("alt"))));

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:not:name:disjoint:alt", checker.getHeaderWithoutId());
    assertTrue(Pattern.compile("^name:not:name:disjoint:alt:\\d+$").matcher(checker.getHeader()).matches());
    assertEquals(RuleCheckingOutputStatus.PASSED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }
}