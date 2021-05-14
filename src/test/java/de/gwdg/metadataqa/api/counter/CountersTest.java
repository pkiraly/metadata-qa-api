package de.gwdg.metadataqa.api.counter;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CountersTest extends TestCase {

  public void testAddResult_single() {
    Counters counters = new Counters();
    Map<String, String> res1 = new LinkedHashMap<>();
    res1.put("a", "b");
    counters.addResult(res1);

    Map<String, String> res2 = counters.getResults();
    assertEquals(1, res2.size());
    assertEquals("b", res2.get("a"));
  }

  public void testAddResult_multi() {
    Counters counters = new Counters();
    Map<String, String> input1 = new LinkedHashMap<>();
    input1.put("a", "b");
    counters.addResult(input1);

    Map<String, String> input2 = new LinkedHashMap<>();
    input2.put("a", "c");
    counters.addResult(input2);

    Map<String, String> result = counters.getResults();
    assertEquals(1, result.size());
    assertEquals("c", result.get("a"));
  }

  public void testAddResultDouble() {
    Counters counters = new Counters();
    Map<String, Double> res1 = new LinkedHashMap<>();
    res1.put("a", 1.2);
    counters.addResultDouble(res1);

    Map<String, Double> res2 = counters.getResultsDouble();
    assertEquals(1, res2.size());
    assertEquals(1.2, res2.get("a"));
  }

  public void testAddResultDouble_multi() {
    Counters counters = new Counters();
    Map<String, Double> res1 = new LinkedHashMap<>();
    res1.put("a", 1.2);
    res1.put("b", 1.3);
    counters.addResultDouble(res1);

    Map<String, Double> res2 = counters.getResultsDouble();
    assertEquals(2, res2.size());
    assertEquals(1.2, res2.get("a"));
    assertEquals(1.3, res2.get("b"));
  }

  public void testGetResults() {
  }

  public void testGetResultsDouble() {
  }
}