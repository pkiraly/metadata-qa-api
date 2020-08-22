package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.pathcache.JsonPathCache;
import de.gwdg.metadataqa.api.schema.MarcJsonSchema;
import de.gwdg.metadataqa.api.util.FileUtils;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class MarcCompletenessCalculatorTest {

  JsonPathCache cache;
  CompletenessCalculator calculator;

  public MarcCompletenessCalculatorTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() throws URISyntaxException, IOException {
    calculator = new CompletenessCalculator(new MarcJsonSchema());
    cache = new JsonPathCache(FileUtils.readFirstLine("general/marc.json"));
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testCompleteness() throws URISyntaxException, IOException {

    calculator.measure(cache);
    FieldCounter<Double> fieldCounter = calculator.getCompletenessCounter().getFieldCounter();
    FieldCounter<Boolean> existenceCounter = calculator.getExistenceCounter();
    FieldCounter<Integer> cardinalityCounter = calculator.getCardinalityCounter();

    System.err.println(fieldCounter.getList(true));
    System.err.println(existenceCounter.getList(true));
    System.err.println(cardinalityCounter.getList(true));
  }
}
