package de.gwdg.metadataqa.api.similarity;

import org.junit.Test;

import static org.junit.Assert.*;

public class TermTest {

  @Test
  public void constructor() {
    Term a = new Term("11111111111111111111001111111111111");
    assertEquals("11111111111111111111001111111111111", a.getValue());
  }

  @Test
  public void distance() {
    Term a = new Term("11111111111111111111001111111111111");
    Term b = new Term("11111111111111111111111111111111111");
    a.setDistance(b, 0.9771428571428572);

    assertTrue(a.hasDistance(b));
    assertEquals(0.9771428571428572, a.getDistance(b), 0.0001);
    assertEquals("{11111111111111111111111111111111111=0.977143}", a.formatDistances());
    assertEquals("Term{term='11111111111111111111001111111111111', " +
      "distances={11111111111111111111111111111111111=0.977143}}", a.toString());
  }

}