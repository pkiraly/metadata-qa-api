package de.gwdg.metadataqa.api.configuration;

import de.gwdg.metadataqa.api.configuration.schema.Rule;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
      .withMinInclusive(1.0)
      .withMaxInclusive(2.0)
      .withMinExclusive(1.0)
      .withMaxExclusive(2.0)
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
    assertEquals(1.0, rule.getMinInclusive(), 0.0001);
    assertEquals(2, rule.getMaxInclusive(), 0.0001);
    assertEquals(1, rule.getMinExclusive(), 0.0001);
    assertEquals(2, rule.getMaxExclusive(), 0.0001);
    assertEquals("uri", rule.getLessThan());
    assertEquals("uri", rule.getLessThanOrEquals());
    assertEquals("a", rule.getHasValue());
    assertEquals(1, (int) rule.getMinLength());
    assertEquals(2, (int) rule.getMaxLength());
  }

  @Test
  public void minCount() {
    Rule rule;
    rule = new Rule().withMinCount(3);
    assertEquals(3, rule.getMinCount().intValue());

    rule = new Rule().withMinCount(Integer.valueOf(3));
    assertEquals(3, rule.getMinCount().intValue());

    rule = new Rule();
    rule.setMinCount(3);
    assertEquals(3, rule.getMinCount().intValue());

    rule = new Rule();
    rule.setMinCount(Integer.valueOf(3));
    assertEquals(3, rule.getMinCount().intValue());
  }

  @Test
  public void maxCount() {
    Rule rule;
    rule = new Rule().withMaxCount(3);
    assertEquals(3, rule.getMaxCount().intValue());

    rule = new Rule().withMaxCount(Integer.valueOf(3));
    assertEquals(3, rule.getMaxCount().intValue());

    rule = new Rule();
    rule.setMaxCount(3);
    assertEquals(3, rule.getMaxCount().intValue());

    rule = new Rule();
    rule.setMaxCount(Integer.valueOf(3));
    assertEquals(3, rule.getMaxCount().intValue());
  }

  @Test
  public void minExclusive() {
    Rule rule;
    rule = new Rule().withMinExclusive(3.0);
    assertEquals(3, rule.getMinExclusive().intValue());

    rule = new Rule().withMinExclusive(Double.valueOf(3.0));
    assertEquals(3, rule.getMinExclusive().intValue());

    rule = new Rule();
    rule.setMinExclusive(3.0);
    assertEquals(3, rule.getMinExclusive().intValue());

    rule = new Rule();
    rule.setMinExclusive(Double.valueOf(3.0));
    assertEquals(3, rule.getMinExclusive().intValue());
  }

  @Test
  public void maxExclusive() {
    Rule rule;
    rule = new Rule().withMaxExclusive(3.0);
    assertEquals(3, rule.getMaxExclusive().intValue());

    rule = new Rule().withMaxExclusive(Double.valueOf(3.0));
    assertEquals(3, rule.getMaxExclusive().intValue());

    rule = new Rule();
    rule.setMaxExclusive(3.0);
    assertEquals(3, rule.getMaxExclusive().intValue());

    rule = new Rule();
    rule.setMaxExclusive(Double.valueOf(3.0));
    assertEquals(3, rule.getMaxExclusive().intValue());
  }

  @Test
  public void minInclusive() {
    Rule rule;
    rule = new Rule().withMinInclusive(3.0);
    assertEquals(3, rule.getMinInclusive().intValue());

    rule = new Rule().withMinInclusive(Double.valueOf(3.0));
    assertEquals(3, rule.getMinInclusive().intValue());

    rule = new Rule();
    rule.setMinInclusive(3.0);
    assertEquals(3, rule.getMinInclusive().intValue());

    rule = new Rule();
    rule.setMinInclusive(Double.valueOf(3.0));
    assertEquals(3, rule.getMinInclusive().intValue());
  }

  @Test
  public void maxInclusive() {
    Rule rule;
    rule = new Rule().withMaxInclusive(3.0);
    assertEquals(3, rule.getMaxInclusive().intValue());

    rule = new Rule().withMaxInclusive(Double.valueOf(3.0));
    assertEquals(3, rule.getMaxInclusive().intValue());

    rule = new Rule();
    rule.setMaxInclusive(3.0);
    assertEquals(3, rule.getMaxInclusive().intValue());

    rule = new Rule();
    rule.setMaxInclusive(Double.valueOf(3.0));
    assertEquals(3, rule.getMaxInclusive().intValue());
  }

  @Test
  public void minLength() {
    Rule rule;
    rule = new Rule().withMinLength(3);
    assertEquals(3, rule.getMinLength().intValue());

    rule = new Rule().withMinLength(Integer.valueOf(3));
    assertEquals(3, rule.getMinLength().intValue());

    rule = new Rule();
    rule.setMinLength(3);
    assertEquals(3, rule.getMinLength().intValue());

    rule = new Rule();
    rule.setMinLength(Integer.valueOf(3));
    assertEquals(3, rule.getMinLength().intValue());
  }

  @Test
  public void maxLength() {
    Rule rule;
    rule = new Rule().withMaxLength(3);
    assertEquals(3, rule.getMaxLength().intValue());

    rule = new Rule().withMaxLength(Integer.valueOf(3));
    assertEquals(3, rule.getMaxLength().intValue());

    rule = new Rule();
    rule.setMaxLength(3);
    assertEquals(3, rule.getMaxLength().intValue());

    rule = new Rule();
    rule.setMaxLength(Integer.valueOf(3));
    assertEquals(3, rule.getMaxLength().intValue());
  }

  @Test
  public void failureScore() {
    Rule rule;
    rule = new Rule().withFailureScore(3);
    assertEquals(3, rule.getFailureScore().intValue());

    rule = new Rule();
    rule.setFailureScore(3);
    assertEquals(3, rule.getFailureScore().intValue());
  }

  @Test
  public void successScore() {
    Rule rule;
    rule = new Rule().withSuccessScore(3);
    assertEquals(3, rule.getSuccessScore().intValue());

    rule = new Rule();
    rule.setSuccessScore(3);
    assertEquals(3, rule.getSuccessScore().intValue());
  }

  @Test
  public void unique() {
    Rule rule;

    // not set
    rule = new Rule();
    assertNull(rule.getUnique());

    // set true
    rule = new Rule().withUnique(true);
    assertTrue(rule.getUnique());

    rule = new Rule().withUnique(Boolean.TRUE);
    assertTrue(rule.getUnique());

    // set false
    rule = new Rule().withUnique(false);
    assertFalse(rule.getUnique());

    rule = new Rule().withUnique(Boolean.FALSE);
    assertFalse(rule.getUnique());
  }
}
