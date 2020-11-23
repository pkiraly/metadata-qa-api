package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.rule.CheckerTestBase;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutput;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinCountCheckerTest extends CheckerTestBase {

  @Test
  public void prefix() {
    assertEquals("minCount", MinCountChecker.prefix);
  }

  @Test
  public void success() {
    MinCountChecker checker = new MinCountChecker(schema.getPathByLabel("name"), 1);

    FieldCounter fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("minCount:name", checker.getHeader());
    Assert.assertEquals(RuleCheckingOutput.PASSED, fieldCounter.get(checker.getHeader()));
  }

  @Test
  public void failure() {
    MinCountChecker checker = new MinCountChecker(schema.getPathByLabel("name"), 0);

    FieldCounter fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("minCount:name", checker.getHeader());
    assertEquals(RuleCheckingOutput.PASSED, fieldCounter.get(checker.getHeader()));
  }
}