package de.gwdg.metadataqa.api.rule;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RuleCheckingOutputTypeTest {

  @Test
  public void testValue() {
    assertEquals("NA", RuleCheckingOutputStatus.NA.value());
    assertEquals(0, RuleCheckingOutputStatus.FAILED.value());
    assertEquals(1, RuleCheckingOutputStatus.PASSED.value());
  }

  @Test
  public void testAsString() {
    assertEquals("NA", RuleCheckingOutputStatus.NA.asString());
    assertEquals("0", RuleCheckingOutputStatus.FAILED.asString());
    assertEquals("1", RuleCheckingOutputStatus.PASSED.asString());
  }

  @Test
  public void testCreate() {
    assertEquals(RuleCheckingOutputStatus.NA, RuleCheckingOutputStatus.create(true, true));
    assertEquals(RuleCheckingOutputStatus.FAILED, RuleCheckingOutputStatus.create(true, false));
    assertEquals(RuleCheckingOutputStatus.PASSED, RuleCheckingOutputStatus.create(false, true));
    assertEquals(RuleCheckingOutputStatus.FAILED, RuleCheckingOutputStatus.create(false, false));
  }

  @Test
  public void negate_passed() {
    RuleCheckingOutputStatus type = RuleCheckingOutputStatus.create(false, true);
    assertEquals(RuleCheckingOutputStatus.PASSED, type);
    assertEquals(RuleCheckingOutputStatus.FAILED, type.negate());
  }

  @Test
  public void negate_failed() {
    RuleCheckingOutputStatus type = RuleCheckingOutputStatus.create(false, false);
    assertEquals(RuleCheckingOutputStatus.FAILED, type);
    assertEquals(RuleCheckingOutputStatus.PASSED, type.negate());
  }

  @Test
  public void negate_NA() {
    RuleCheckingOutputStatus type = RuleCheckingOutputStatus.create(true, false);
    assertEquals(RuleCheckingOutputStatus.FAILED, type);
    assertEquals(RuleCheckingOutputStatus.PASSED, type.negate());
  }
}
