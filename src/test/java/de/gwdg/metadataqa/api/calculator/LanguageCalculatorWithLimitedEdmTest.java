package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.model.pathcache.JsonPathCache;
import de.gwdg.metadataqa.api.schema.EdmOaiPmLimitedJsonSchema;
import de.gwdg.metadataqa.api.util.CompressionLevel;
import de.gwdg.metadataqa.api.util.FileUtils;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class LanguageCalculatorWithLimitedEdmTest {

  @Test
  public void testConstructor() {
    LanguageCalculator calculator = new LanguageCalculator(new EdmOaiPmLimitedJsonSchema());
    assertNotNull(calculator);
  }

  @Test
  public void testMeasure() throws URISyntaxException, IOException {
    LanguageCalculator calculator = new LanguageCalculator(new EdmOaiPmLimitedJsonSchema());
    JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLine("general/test.json"));
    calculator.measure(cache);
    assertNotNull(calculator.getCsv(false, CompressionLevel.NORMAL));
    assertEquals("de:1,_1:1,_1:1,_1:1,_1:1,_1:1,_0:1,_0:1,_1:1,_1:1,_1:1,_1:1,de:4;en:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_2:1,_1:1,_1:1,_0:1,_1:1,_2:1,en:1,_0:1", calculator.getCsv(false, CompressionLevel.NORMAL));
  }

  @Test
  public void testCountersGetLanguageMap() throws URISyntaxException, IOException {
    LanguageCalculator calculator = new LanguageCalculator(new EdmOaiPmLimitedJsonSchema());
    JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLine("general/test.json"));
    calculator.measure(cache);
    String languages = calculator.getCsv(true, CompressionLevel.NORMAL);
    assertNotNull(languages);
    assertEquals("\"Proxy/dc:title\":de:1,\"Proxy/dcterms:alternative\":_1:1,\"Proxy/dc:description\":_1:1,\"Proxy/dc:creator\":_1:1,\"Proxy/dc:publisher\":_1:1,\"Proxy/dc:contributor\":_1:1,\"Proxy/dc:type\":_0:1,\"Proxy/dc:identifier\":_0:1,\"Proxy/dc:language\":_1:1,\"Proxy/dc:coverage\":_1:1,\"Proxy/dcterms:temporal\":_1:1,\"Proxy/dcterms:spatial\":_1:1,\"Proxy/dc:subject\":de:4;en:1,\"Proxy/dc:date\":_1:1,\"Proxy/dcterms:created\":_1:1,\"Proxy/dcterms:issued\":_1:1,\"Proxy/dcterms:extent\":_1:1,\"Proxy/dcterms:medium\":_1:1,\"Proxy/dcterms:provenance\":_1:1,\"Proxy/dcterms:hasPart\":_1:1,\"Proxy/dcterms:isPartOf\":_2:1,\"Proxy/dc:format\":_1:1,\"Proxy/dc:source\":_1:1,\"Proxy/dc:rights\":_0:1,\"Proxy/dc:relation\":_1:1,\"Aggregation/edm:rights\":_2:1,\"Aggregation/edm:provider\":en:1,\"Aggregation/edm:dataProvider\":_0:1", languages);
  }

  @Test
  public void testGetLanguageMap() throws URISyntaxException, IOException {
    LanguageCalculator calculator = new LanguageCalculator(new EdmOaiPmLimitedJsonSchema());
    JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLine("general/test.json"));
    calculator.measure(cache);

    Map<String, String> languages = calculator.getLanguageMap();
    assertNotNull(languages);
    assertEquals(28, languages.size());
    assertEquals("de:1", languages.get("Proxy/dc:title"));
    assertEquals("_1:1", languages.get("Proxy/dcterms:alternative"));
    assertEquals("_1:1", languages.get("Proxy/dc:description"));
    assertEquals("_1:1", languages.get("Proxy/dc:creator"));
    assertEquals("_1:1", languages.get("Proxy/dc:publisher"));
    assertEquals("_1:1", languages.get("Proxy/dc:contributor"));
    assertEquals("_0:1", languages.get("Proxy/dc:type"));
    assertEquals("_0:1", languages.get("Proxy/dc:identifier"));
    assertEquals("_1:1", languages.get("Proxy/dc:language"));
    assertEquals("_1:1", languages.get("Proxy/dc:coverage"));
    assertEquals("_1:1", languages.get("Proxy/dcterms:temporal"));
    assertEquals("_1:1", languages.get("Proxy/dcterms:spatial"));
    assertEquals("de:4;en:1", languages.get("Proxy/dc:subject"));
    assertEquals("_1:1", languages.get("Proxy/dc:date"));
    assertEquals("_1:1", languages.get("Proxy/dcterms:created"));
    assertEquals("_1:1", languages.get("Proxy/dcterms:issued"));
    assertEquals("_1:1", languages.get("Proxy/dcterms:extent"));
    assertEquals("_1:1", languages.get("Proxy/dcterms:medium"));
    assertEquals("_1:1", languages.get("Proxy/dcterms:provenance"));
    assertEquals("_1:1", languages.get("Proxy/dcterms:hasPart"));
    assertEquals("_2:1", languages.get("Proxy/dcterms:isPartOf"));
    assertEquals("_1:1", languages.get("Proxy/dc:format"));
    assertEquals("_1:1", languages.get("Proxy/dc:source"));
    assertEquals("_0:1", languages.get("Proxy/dc:rights"));
    assertEquals("_1:1", languages.get("Proxy/dc:relation"));
    assertEquals("_2:1", languages.get("Aggregation/edm:rights"));
    assertEquals("en:1", languages.get("Aggregation/edm:provider"));
    assertEquals("_0:1", languages.get("Aggregation/edm:dataProvider"));
  }

  @Test
  public void testGetHeaders() {
    LanguageCalculator calculator = new LanguageCalculator(new EdmOaiPmLimitedJsonSchema());
    List<String> expected = Arrays.asList("lang:Proxy/dc:title", "lang:Proxy/dcterms:alternative", "lang:Proxy/dc:description", "lang:Proxy/dc:creator", "lang:Proxy/dc:publisher", "lang:Proxy/dc:contributor", "lang:Proxy/dc:type", "lang:Proxy/dc:identifier", "lang:Proxy/dc:language", "lang:Proxy/dc:coverage", "lang:Proxy/dcterms:temporal", "lang:Proxy/dcterms:spatial", "lang:Proxy/dc:subject", "lang:Proxy/dc:date", "lang:Proxy/dcterms:created", "lang:Proxy/dcterms:issued", "lang:Proxy/dcterms:extent", "lang:Proxy/dcterms:medium", "lang:Proxy/dcterms:provenance", "lang:Proxy/dcterms:hasPart", "lang:Proxy/dcterms:isPartOf", "lang:Proxy/dc:format", "lang:Proxy/dc:source", "lang:Proxy/dc:rights", "lang:Proxy/dc:relation", "lang:Aggregation/edm:rights", "lang:Aggregation/edm:provider", "lang:Aggregation/edm:dataProvider");
    assertEquals(28, calculator.getHeader().size());
    assertEquals(expected, calculator.getHeader());
  }
}
