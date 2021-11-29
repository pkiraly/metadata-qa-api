package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.rule.CheckerTestBase;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
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
    MaxCountChecker checker = new MaxCountChecker(schema.getPathByLabel("name"), 1);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(1, fieldCounter.size());
    assertEquals("name:maxCount", checker.getHeaderWithoutId());
    Assert.assertEquals(RuleCheckingOutputStatus.PASSED, fieldCounter.get(checker.getHeader()).getStatus());
  }

  @Test
  public void failure() {
    MaxCountChecker checker = new MaxCountChecker(schema.getPathByLabel("name"), 0);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(1, fieldCounter.size());
    assertEquals("name:maxCount", checker.getHeaderWithoutId());
    Assert.assertEquals(RuleCheckingOutputStatus.FAILED, fieldCounter.get(checker.getHeader()).getStatus());
  }
}