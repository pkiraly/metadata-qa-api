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
    assertEquals(1, (int) rule.getAnd().get(0).getMinCount());
    assertEquals(2, (int) rule.getAnd().get(1).getMaxCount());
  }

  @Test
  public void testOr_setter() {

    Rule rule = new Rule();
    rule.setOr(Arrays.asList(
        new Rule().withMinCount(1),
        new Rule().withMaxCount(2)
      ));

    assertEquals(2, rule.getOr().size());
    assertEquals(1, (int) rule.getOr().get(0).getMinCount());
    assertEquals(2, (int) rule.getOr().get(1).getMaxCount());
  }

  @Test
  public void testOr_withOr() {

    Rule rule = new Rule()
      .withOr(Arrays.asList(
        new Rule().withMinCount(1),
        new Rule().withMaxCount(2)
      ));

    assertEquals(2, rule.getOr().size());
    assertEquals(1, (int) rule.getOr().get(0).getMinCount());
    assertEquals(2, (int) rule.getOr().get(1).getMaxCount());
  }

  @Test
  public void testProperties() {
    Rule rule = new Rule()
      .withPattern("^http")
      .withEquals("http")
      .withDisjoint("http")
      .withIn(Arrays.asList("a", "b", "c"))
      .withMinInclusive(1)
      .withMaxInclusive(2)
      .withMinExclusive(1)
      .withMaxExclusive(2)
      .withLessThan("uri")
      .withLessThanOrEquals("uri")
      .withHasValue("a")
      .withMinLength(1)
      .withMaxLength(2)
    ;

    assertEquals("^http", rule.getPattern());
    assertEquals("http", rule.getEquals());
    assertEquals("http", rule.getDisjoint());
    assertEquals(Arrays.asList("a", "b", "c"), rule.getIn());
    assertEquals(1, (int) rule.getMinInclusive());
    assertEquals(2, (int) rule.getMaxInclusive());
    assertEquals(1, (int) rule.getMinExclusive());
    assertEquals(2, (int) rule.getMaxExclusive());
    assertEquals("uri", rule.getLessThan());
    assertEquals("uri", rule.getLessThanOrEquals());
    assertEquals("a", rule.getHasValue());
    assertEquals(1, (int) rule.getMinLength());
    assertEquals(2, (int) rule.getMaxLength());
  }
}
