package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.rule.CheckerTestBase;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
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
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("maxLength:name", checker.getHeader());
    Assert.assertEquals(RuleCheckingOutputType.PASSED, fieldCounter.get(checker.getHeader()).getType());
  }

  @Test
  public void failure() {
    MaxLengthChecker checker = new MaxLengthChecker(schema.getPathByLabel("name"), 0);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("maxLength:name", checker.getHeader());
    assertEquals(RuleCheckingOutputType.FAILED, fieldCounter.get(checker.getHeader()).getType());
  }
}