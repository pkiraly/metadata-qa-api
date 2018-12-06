package de.gwdg.metadataqa.api.uniqueness;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TfIdfTest {

  @Test
  public void test() {
    TfIdf tfIdf = new TfIdf("the", 3, 4, 3.4);
    assertEquals("the", tfIdf.getTerm());
    assertEquals(3, tfIdf.getTf());
    assertEquals(4, tfIdf.getDf());
    assertEquals(3.4, tfIdf.getTfIdf(), 0.0002);
    assertEquals("the(tf=3, df=4, tfIdf=3.4)", tfIdf.toString());
  }
}
