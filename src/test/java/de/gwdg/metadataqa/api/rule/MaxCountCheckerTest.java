package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import org.junit.Test;

import static org.junit.Assert.*;

public class MaxCountCheckerTest extends CheckerTestBase {

  @Test
  public void prefix() {
    assertEquals("maxCount", MaxCountChecker.prefix);
  }

  @Test
  public void name() {
    MaxCountChecker checker = new MaxCountChecker(schema.getPathByLabel("name"), 1);

    FieldCounter fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("maxCount:name", checker.getHeader());
    assertEquals(RuleCheckingOutput.PASSED, fieldCounter.get(checker.getHeader()));
  }
}