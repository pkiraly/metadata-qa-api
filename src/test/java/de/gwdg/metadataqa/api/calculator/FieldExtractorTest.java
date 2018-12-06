package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.util.FileUtils;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class FieldExtractorTest {

  FieldExtractor calculator;
  JsonPathCache cache;

  public FieldExtractorTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() throws URISyntaxException, IOException {
    calculator = new FieldExtractor("$.identifier");
    cache = new JsonPathCache(FileUtils.readFirstLine("general/test.json"));
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testId() throws URISyntaxException, IOException {
    calculator.measure(cache);
    assertEquals("92062/BibliographicResource_1000126015451", calculator.getResultMap().get(calculator.FIELD_NAME));
  }

  @Test
  public void testGetHeaders() {
    List<String> expected = Arrays.asList("recordId");
    assertEquals(1, calculator.getHeader().size());
    assertEquals(expected, calculator.getHeader());
  }
}
