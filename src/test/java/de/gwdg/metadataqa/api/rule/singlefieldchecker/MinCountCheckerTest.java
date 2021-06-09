package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.rule.CheckerTestBase;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinCountCheckerTest extends CheckerTestBase {

  @Test
  public void prefix() {
    assertEquals("minCount", MinCountChecker.PREFIX);
  }

  @Test
  public void success1() {
    MinCountChecker checker = new MinCountChecker(schema.getPathByLabel("name"), 1);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("minCount:name", checker.getHeader());
    Assert.assertEquals(RuleCheckingOutputType.PASSED, fieldCounter.get(checker.getHeader()).getType());
  }

  @Test
  public void success_with_0() {
    MinCountChecker checker = new MinCountChecker(schema.getPathByLabel("name"), 0);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("minCount:name", checker.getHeader());
    assertEquals(RuleCheckingOutputType.PASSED, fieldCounter.get(checker.getHeader()).getType());
  }

  @Test
  public void failure() {
    MinCountChecker checker = new MinCountChecker(schema.getPathByLabel("name"), 2);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("minCount:name", checker.getHeader());
    assertEquals(RuleCheckingOutputType.FAILED, fieldCounter.get(checker.getHeader()).getType());
  }
}