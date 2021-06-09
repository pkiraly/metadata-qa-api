package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.rule.CheckerTestBase;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
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

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("in:name", checker.getHeader());
    Assert.assertEquals(RuleCheckingOutputType.PASSED, fieldCounter.get(checker.getHeader()).getType());
  }

  @Test
  public void failure() {
    EnumerationChecker checker = new EnumerationChecker(schema.getPathByLabel("name"),
      Arrays.asList("c", "d"));

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("in:name", checker.getHeader());
    assertEquals(RuleCheckingOutputType.FAILED, fieldCounter.get(checker.getHeader()).getType());
  }
}