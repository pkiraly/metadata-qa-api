package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.rule.CheckerTestBase;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HasValueCheckerTest extends CheckerTestBase {

  @Test
  public void prefix() {
    assertEquals("hasValue", HasValueChecker.PREFIX);
  }

  @Test
  public void success() {
    HasValueChecker checker = new HasValueChecker(schema.getPathByLabel("name"), "a");

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("hasValue:name", checker.getHeader());
    Assert.assertEquals(RuleCheckingOutputType.PASSED, fieldCounter.get("hasValue:name").getType());
  }

  @Test
  public void failure() {
    HasValueChecker checker = new HasValueChecker(schema.getPathByLabel("name"), "b");

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("hasValue:name", checker.getHeader());
    assertEquals(RuleCheckingOutputType.FAILED, fieldCounter.get("hasValue:name").getType());
  }
}
