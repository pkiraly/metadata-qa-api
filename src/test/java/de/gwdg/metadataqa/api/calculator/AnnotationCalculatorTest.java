package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.model.pathcache.JsonPathCache;
import de.gwdg.metadataqa.api.util.FileUtils;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class AnnotationCalculatorTest extends TestCase {

  JsonPathCache cache;
  AnnotationCalculator calculator;

  @Before
  public void setUp() throws URISyntaxException, IOException {
    calculator = new AnnotationCalculator(Map.of("one", "1"));
    cache = new JsonPathCache(FileUtils.readFirstLineFromResource("general/test.json"));
  }

  @Test
  public void testName() {
    List<MetricResult> result = calculator.measure(cache);
    assertEquals(1, result.size());
    assertEquals("annotation", result.get(0).getName());
    assertEquals(1, result.get(0).getResultMap().size());
    assertEquals("1", result.get(0).getResultMap().get("one"));
  }
}