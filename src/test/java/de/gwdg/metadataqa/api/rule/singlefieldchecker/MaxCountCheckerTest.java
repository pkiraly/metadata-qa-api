package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.rule.CheckerTestBase;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutput;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class MaxCountCheckerTest extends CheckerTestBase {

  @Test
  public void prefix() {
    assertEquals("maxCount", MaxCountChecker.PREFIX);
  }

  @Test
  public void name() {
    MaxCountChecker checker = new MaxCountChecker(schema.getPathByLabel("name"), 1);

    FieldCounter fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("maxCount:name", checker.getHeader());
    Assert.assertEquals(RuleCheckingOutput.PASSED, fieldCounter.get(checker.getHeader()));
  }
}