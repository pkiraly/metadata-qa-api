package de.gwdg.metadataqa.api.configuration;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RuleTest {

  @Test
  public void testAnd() {
    List<Rule> and = new ArrayList<>();
    Rule r1 = new Rule();
    r1.setMinCount(1);
    Rule r2 = new Rule();
    r2.setMaxCount(2);

    Rule rule = new Rule();
    rule.setAnd(Arrays.asList(r1, r2));

    assertEquals(2, rule.getAnd().size());
  }

  @Test
  public void testProperties() {
    Rule rule = new Rule();
    rule.setMinInclusive(1);
    rule.setMaxInclusive(2);
    rule.setMinExclusive(1);
    rule.setMaxExclusive(2);
    rule.setLessThan(1);
    rule.setLessThanOrEquals(1);
    rule.setHasValue("a");

    assertEquals(1, (int) rule.getMinInclusive());
    assertEquals(2, (int) rule.getMaxInclusive());
    assertEquals(1, (int) rule.getMinExclusive());
    assertEquals(2, (int) rule.getMaxExclusive());
    assertEquals(1, (int) rule.getLessThan());
    assertEquals(1, (int) rule.getLessThanOrEquals());
    assertEquals("a", rule.getHasValue());
  }
}
