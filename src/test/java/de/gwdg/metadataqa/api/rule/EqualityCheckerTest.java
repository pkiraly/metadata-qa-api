package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class EqualityCheckerTest extends CheckerTestBase {

  @Test
  public void prefix() {
    assertEquals("equals", EqualityChecker.prefix);
  }

  @Test
  public void success() {
    EqualityChecker checker = new EqualityChecker(schema.getPathByLabel("name"),"a");

    FieldCounter fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("equals:name", checker.getHeader());
    assertEquals(RuleCheckingOutput.PASSED, fieldCounter.get(checker.getHeader()));
  }

  @Test
  public void failure() {
    EqualityChecker checker = new EqualityChecker(schema.getPathByLabel("name"),"b");

    FieldCounter fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("equals:name", checker.getHeader());
    assertEquals(RuleCheckingOutput.FAILED, fieldCounter.get(checker.getHeader()));
  }
}