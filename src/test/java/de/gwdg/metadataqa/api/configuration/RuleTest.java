package de.gwdg.metadataqa.api.configuration;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class RuleTest {

  @Test
  public void testAnd() {

    Rule rule = new Rule()
      .withAnd(Arrays.asList(
        new Rule().withMinCount(1),
        new Rule().withMaxCount(2)
      ));

    assertEquals(2, rule.getAnd().size());
  }

  @Test
  public void testProperties() {
    Rule rule = new Rule()
      .withMinInclusive(1)
      .withMaxInclusive(2)
      .withMinExclusive(1)
      .withMaxExclusive(2)
      .withLessThan(1)
      .withLessThanOrEquals(1)
      .withHasValue("a");

    assertEquals(1, (int) rule.getMinInclusive());
    assertEquals(2, (int) rule.getMaxInclusive());
    assertEquals(1, (int) rule.getMinExclusive());
    assertEquals(2, (int) rule.getMaxExclusive());
    assertEquals(1, (int) rule.getLessThan());
    assertEquals(1, (int) rule.getLessThanOrEquals());
    assertEquals("a", rule.getHasValue());
  }
}
