package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.selector.CsvSelector;
import de.gwdg.metadataqa.api.model.selector.SelectorFactory;
import de.gwdg.metadataqa.api.rule.CheckerTestBase;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.schema.CsvAwareSchema;
import de.gwdg.metadataqa.api.util.CsvReader;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MaxLenghCheckerTest extends CheckerTestBase {

  @Test
  public void prefix() {
    assertEquals("maxLength", MaxLengthChecker.PREFIX);
  }

  @Test
  public void success() {
    MaxLengthChecker checker = new MaxLengthChecker(schema.getPathByLabel("name"), 1);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:maxLength", checker.getHeaderWithoutId());
    Assert.assertEquals(RuleCheckingOutputStatus.PASSED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }

  @Test
  public void failure() {
    MaxLengthChecker checker = new MaxLengthChecker(schema.getPathByLabel("name"), 0);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:maxLength", checker.getHeaderWithoutId());
    assertEquals(RuleCheckingOutputStatus.FAILED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }

  @Test
  public void na() {
    MaxLengthChecker checker = (MaxLengthChecker) new MaxLengthChecker(schema.getPathByLabel("name"), 1)
      .withSuccessScore(1)
      .withNaScore(-1)
      .withFailureScore(-2);

    cache = (CsvSelector) SelectorFactory.getInstance(schema.getFormat(), "");
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);
    System.err.println(checker.getHeader());

    assertEquals(2, fieldCounter.size());
    assertEquals("name:maxLength", checker.getHeaderWithoutId());
    Assert.assertEquals(RuleCheckingOutputStatus.NA, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
    Assert.assertEquals(-1, (int) fieldCounter.get(checker.getHeader(RuleCheckingOutputType.SCORE)).getScore());
  }
}