package de.gwdg.metadataqa.api.counter;

import org.junit.Test;

// import static java.lang.Double.NaN;
import static org.junit.Assert.*;

public class BasicCounterTest {

  @Test
  public void zeroWhenNoAction() {
    BasicCounter counter = new BasicCounter();
    assertEquals(0, counter.getTotalAsInt());
    assertEquals(0.0, counter.getTotal(),0.0001);
    assertEquals(0.0, counter.getInstance(),0.0001);
    counter.calculate();
    assertEquals(Double.NaN, counter.getResult(),0.0001);
    assertEquals("BasicCounter{total=0.0, instance=0.0, result=NaN}", counter.toString());
  }

  @Test
  public void increase() {
    BasicCounter counter = new BasicCounter();
    counter.increaseTotal();
    counter.increaseInstance();
    assertEquals(1, counter.getTotalAsInt());
    assertEquals(1.0, counter.getTotal(),0.0001);
    assertEquals(1.0, counter.getInstance(),0.0001);
    counter.calculate();
    assertEquals(1.0, counter.getResult(),0.0001);
    assertEquals("BasicCounter{total=1.0, instance=1.0, result=1.0}", counter.toString());
  }
}