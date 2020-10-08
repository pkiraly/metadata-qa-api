package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MaxLenghCheckerTest extends CheckerTestBase {

  @Test
  public void prefix() {
    assertEquals("maxLength", MaxLengthChecker.prefix);
  }

  @Test
  public void success() {
    MaxLengthChecker checker = new MaxLengthChecker(schema.getPathByLabel("name"), 1);

    FieldCounter fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("maxLength:name", checker.getHeader());
    assertEquals(RuleCheckingOutput.PASSED, fieldCounter.get(checker.getHeader()));
  }

  @Test
  public void failure() {
    MaxLengthChecker checker = new MaxLengthChecker(schema.getPathByLabel("name"), 0);

    FieldCounter fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("maxLength:name", checker.getHeader());
    assertEquals(RuleCheckingOutput.FAILED, fieldCounter.get(checker.getHeader()));
  }
}