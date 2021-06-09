package de.gwdg.metadataqa.api.rule;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RuleCheckingOutputTypeTest {

  @Test
  public void testValue() {
    assertEquals("NA", RuleCheckingOutputType.NA.value());
    assertEquals(0, RuleCheckingOutputType.FAILED.value());
    assertEquals(1, RuleCheckingOutputType.PASSED.value());
  }

  @Test
  public void testAsString() {
    assertEquals("NA", RuleCheckingOutputType.NA.asString());
    assertEquals("0", RuleCheckingOutputType.FAILED.asString());
    assertEquals("1", RuleCheckingOutputType.PASSED.asString());
  }

  @Test
  public void testCreate() {
    assertEquals(RuleCheckingOutputType.NA, RuleCheckingOutputType.create(true, true));
    assertEquals(RuleCheckingOutputType.NA, RuleCheckingOutputType.create(true, false));
    assertEquals(RuleCheckingOutputType.PASSED, RuleCheckingOutputType.create(false, true));
    assertEquals(RuleCheckingOutputType.FAILED, RuleCheckingOutputType.create(false, false));
  }
}
