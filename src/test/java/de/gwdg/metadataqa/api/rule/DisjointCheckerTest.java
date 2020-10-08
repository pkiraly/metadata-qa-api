package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DisjointCheckerTest extends CheckerTestBase {

  @Test
  public void prefix() {
    assertEquals("disjoint", DisjointChecker.prefix);
  }

  @Test
  public void success() {
    DisjointChecker checker = new DisjointChecker(schema.getPathByLabel("name"), "b");

    FieldCounter fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("disjoint:name", checker.getHeader());
    assertEquals(RuleCheckingOutput.PASSED, fieldCounter.get(checker.getHeader()));
  }

  @Test
  public void failure() {
    DisjointChecker checker = new DisjointChecker(schema.getPathByLabel("name"), "a");

    FieldCounter fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("disjoint:name", checker.getHeader());
    assertEquals(RuleCheckingOutput.FAILED, fieldCounter.get(checker.getHeader()));
  }
}