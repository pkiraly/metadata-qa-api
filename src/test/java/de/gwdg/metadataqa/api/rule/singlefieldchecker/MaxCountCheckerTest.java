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

import static org.junit.Assert.*;

public class MaxCountCheckerTest extends CheckerTestBase {

  @Test
  public void prefix() {
    assertEquals("maxCount", MaxCountChecker.PREFIX);
  }

  @Test
  public void success() {
    MaxCountChecker checker = (MaxCountChecker) new MaxCountChecker(schema.getPathByLabel("name"), 1)
      .withSuccessScore(1)
      .withNaScore(-1)
      .withFailureScore(-2);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:maxCount", checker.getHeaderWithoutId());
    Assert.assertEquals(RuleCheckingOutputStatus.PASSED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
    Assert.assertEquals(1, (int) fieldCounter.get(checker.getHeader(RuleCheckingOutputType.SCORE)).getScore());
  }


  @Test
  public void failure() {
    MaxCountChecker checker = (MaxCountChecker) new MaxCountChecker(schema.getPathByLabel("name"), 0)
      .withSuccessScore(1)
      .withNaScore(-1)
      .withFailureScore(-2);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:maxCount", checker.getHeaderWithoutId());
    Assert.assertEquals(RuleCheckingOutputStatus.FAILED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
    Assert.assertEquals(-2, (int) fieldCounter.get(checker.getHeader(RuleCheckingOutputType.SCORE)).getScore());
  }

  @Test
  public void na() {
    MaxCountChecker checker = (MaxCountChecker) new MaxCountChecker(schema.getPathByLabel("name"), 1)
      .withSuccessScore(1)
      .withNaScore(-1)
      .withFailureScore(-2);

    cache = (CsvSelector) SelectorFactory.getInstance(schema.getFormat(), "");
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:maxCount", checker.getHeaderWithoutId());
    Assert.assertEquals(RuleCheckingOutputStatus.NA, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
    Assert.assertEquals(-1, (int) fieldCounter.get(checker.getHeader(RuleCheckingOutputType.SCORE)).getScore());
  }
}