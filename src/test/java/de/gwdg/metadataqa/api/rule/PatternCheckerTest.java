package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PatternCheckerTest extends CheckerTestBase {

  @Test
  public void prefix() {
    assertEquals("pattern", PatternChecker.prefix);
  }

  @Test
  public void success() {
    PatternChecker checker = new PatternChecker(schema.getPathByLabel("name"), "^a$");

    FieldCounter fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("pattern:name", checker.getHeader());
    assertEquals(RuleCheckingOutput.PASSED, fieldCounter.get(checker.getHeader()));
  }

  @Test
  public void failure() {
    PatternChecker checker = new PatternChecker(schema.getPathByLabel("name"), "^b$");

    FieldCounter fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("pattern:name", checker.getHeader());
    assertEquals(RuleCheckingOutput.FAILED, fieldCounter.get(checker.getHeader()));
  }
}