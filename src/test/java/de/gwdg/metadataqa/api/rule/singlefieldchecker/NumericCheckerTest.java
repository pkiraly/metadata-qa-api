package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.selector.SelectorFactory;
import de.gwdg.metadataqa.api.model.selector.CsvSelector;
import de.gwdg.metadataqa.api.rule.RuleChecker;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.CsvAwareSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.CsvReader;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.NumericValueChecker.TYPE;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumericCheckerTest {

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

    cache = (CsvSelector) SelectorFactory.getInstance(schema.getFormat(), "1,2,auto");
    cache.setCsvReader(new CsvReader().setHeader(((CsvAwareSchema) schema).getHeader()));
  }

  @Test
  public void minInclusive_passed() {
    assertEquals(RuleCheckingOutputStatus.PASSED, run(0.9, TYPE.MIN_INCLUSIVE).getStatus());
    assertEquals(RuleCheckingOutputStatus.PASSED, run(1, TYPE.MIN_INCLUSIVE).getStatus());
  }

  @Test
  public void minInclusive_failed() {
    assertEquals(RuleCheckingOutputStatus.FAILED, run(1.1, TYPE.MIN_INCLUSIVE).getStatus());
  }

  @Test
  public void maxInclusive_passed() {
    assertEquals(RuleCheckingOutputStatus.PASSED, run(1, TYPE.MAX_INCLUSIVE).getStatus());
    assertEquals(RuleCheckingOutputStatus.PASSED, run(1.1, TYPE.MAX_INCLUSIVE).getStatus());
  }

  @Test
  public void maxInclusive_failed() {
    assertEquals(RuleCheckingOutputStatus.FAILED, run(0.9, TYPE.MAX_INCLUSIVE).getStatus());
  }

  @Test
  public void minExclusive_passed() {
    assertEquals(RuleCheckingOutputStatus.PASSED, run(0.9, TYPE.MIN_EXCLUSIVE).getStatus());
  }

  @Test
  public void minExclusive_failed() {
    assertEquals(RuleCheckingOutputStatus.FAILED, run(1, TYPE.MIN_EXCLUSIVE).getStatus());
  }

  @Test
  public void maxExclusive_passed() {
    assertEquals(RuleCheckingOutputStatus.PASSED, run(1.1, TYPE.MAX_EXCLUSIVE).getStatus());
  }

  @Test
  public void maxExclusive_failed() {
    assertEquals(RuleCheckingOutputStatus.FAILED, run(1, TYPE.MAX_EXCLUSIVE).getStatus());
  }

  @Test
  public void nonnumeric() {
    assertEquals(RuleCheckingOutputStatus.FAILED, run("alt", 1, TYPE.MAX_EXCLUSIVE).getStatus());
  }

  private RuleCheckerOutput run(double limit, NumericValueChecker.TYPE type) {
    return run("name", limit, type);
  }

  private RuleCheckerOutput run(String field, double limit, NumericValueChecker.TYPE type) {
    RuleChecker checker = new NumericValueChecker(schema.getPathByLabel(field), limit, type);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    return fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS));
  }
}