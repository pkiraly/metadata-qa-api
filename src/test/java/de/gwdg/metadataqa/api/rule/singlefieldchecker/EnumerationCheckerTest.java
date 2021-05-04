package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.rule.CheckerTestBase;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutput;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class EnumerationCheckerTest extends CheckerTestBase {

  @Test
  public void prefix() {
    assertEquals("in", EnumerationChecker.PREFIX);
  }

  @Test
  public void success() {
    EnumerationChecker checker = new EnumerationChecker(schema.getPathByLabel("name"),
      Arrays.asList("a", "b"));

    FieldCounter fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("in:name", checker.getHeader());
    Assert.assertEquals(RuleCheckingOutput.PASSED, fieldCounter.get(checker.getHeader()));
  }

  @Test
  public void failure() {
    EnumerationChecker checker = new EnumerationChecker(schema.getPathByLabel("name"),
      Arrays.asList("c", "d"));

    FieldCounter fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("in:name", checker.getHeader());
    assertEquals(RuleCheckingOutput.FAILED, fieldCounter.get(checker.getHeader()));
  }
}