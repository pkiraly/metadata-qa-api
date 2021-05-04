package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.rule.CheckerTestBase;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutput;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PatternCheckerTest extends CheckerTestBase {

  @Test
  public void prefix() {
    assertEquals("pattern", PatternChecker.PREFIX);
  }

  @Test
  public void success() {
    PatternChecker checker = new PatternChecker(schema.getPathByLabel("name"), "^a$");

    FieldCounter fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("pattern:name", checker.getHeader());
    Assert.assertEquals(RuleCheckingOutput.PASSED, fieldCounter.get(checker.getHeader()));
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