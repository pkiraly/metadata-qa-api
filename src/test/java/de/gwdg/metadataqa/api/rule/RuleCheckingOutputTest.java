package de.gwdg.metadataqa.api.rule;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RuleCheckingOutputTest {

  @Test
  public void testValue() {
    assertEquals("NA", RuleCheckingOutput.NA.value());
    assertEquals(0, RuleCheckingOutput.FAILED.value());
    assertEquals(1, RuleCheckingOutput.PASSED.value());
  }

  @Test
  public void testAsString() {
    assertEquals("NA", RuleCheckingOutput.NA.asString());
    assertEquals("0", RuleCheckingOutput.FAILED.asString());
    assertEquals("1", RuleCheckingOutput.PASSED.asString());
  }

  @Test
  public void testCreate() {
    assertEquals(RuleCheckingOutput.NA, RuleCheckingOutput.create(true, true));
    assertEquals(RuleCheckingOutput.NA, RuleCheckingOutput.create(true, false));
    assertEquals(RuleCheckingOutput.PASSED, RuleCheckingOutput.create(false, true));
    assertEquals(RuleCheckingOutput.FAILED, RuleCheckingOutput.create(false, false));
  }
}
