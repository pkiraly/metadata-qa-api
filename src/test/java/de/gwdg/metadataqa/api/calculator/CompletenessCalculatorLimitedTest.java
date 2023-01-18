package de.gwdg.metadataqa.api.calculator;

import com.jayway.jsonpath.InvalidJsonException;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.Category;
import de.gwdg.metadataqa.api.model.pathcache.JsonPathCache;
import de.gwdg.metadataqa.api.schema.edm.EdmOaiPmLimitedJsonSchema;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.CompressionLevel;
import de.gwdg.metadataqa.api.util.FileUtils;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.junit.After;
import org.junit.AfterClass;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class CompletenessCalculatorLimitedTest {

  private JsonPathCache cache;
  private CompletenessCalculator calculator;

  public CompletenessCalculatorLimitedTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() throws URISyntaxException, IOException {
    calculator = new CompletenessCalculator(new EdmOaiPmLimitedJsonSchema());
    cache = new JsonPathCache(FileUtils.readFirstLineFromResource("general/test.json"));
  }

  @After
  public void tearDown() {
    calculator = null;
    cache = null;
  }

  @Test
  public void testCompleteness() throws URISyntaxException, IOException {

    assertEquals("de.gwdg.metadataqa.api.schema.edm.EdmOaiPmLimitedJsonSchema",
      calculator.getSchema().getClass().getName());
    assertEquals(0, calculator.getSchema().getCollectionPaths().size());
    assertEquals(35, calculator.getSchema().getPaths().size());
    calculator.measure(cache);
    FieldCounter<Double> fieldCounter = calculator.getCompletenessCounter().getFieldCounter();
    FieldCounter<Boolean> existenceCounter = calculator.getExistenceCounter();
    FieldCounter<Integer> cardinalityCounter = calculator.getCardinalityCounter();
    // calculator.getExistenceMap();
    assertEquals((Double) 0.4, fieldCounter.get("TOTAL"));
    // printCheck(Category.VIEWING, calculator);
    assertEquals((Double) 0.75, fieldCounter.get("VIEWING"));
    // printCheck(Category.CONTEXTUALIZATION, calculator);
    assertEquals((Double) 0.2727272727272727, fieldCounter.get("CONTEXTUALIZATION"));
    // printCheck(Category.MANDATORY, calculator);
    assertEquals((Double) 1.0, fieldCounter.get("MANDATORY"));
    // printCheck(Category.MULTILINGUALITY, calculator);
    assertEquals((Double) 0.4, fieldCounter.get("MULTILINGUALITY"));
    // printCheck(Category.DESCRIPTIVENESS, calculator);
    assertEquals((Double) 0.18181818181818182, fieldCounter.get("DESCRIPTIVENESS"));
    // printCheck(Category.BROWSING, calculator);
    assertEquals((Double) 0.35714285714285715, fieldCounter.get("BROWSING"));
    // printCheck(Category.IDENTIFICATION, calculator);
    assertEquals((Double) 0.5, fieldCounter.get("IDENTIFICATION"));
    // printCheck(Category.SEARCHABILITY, calculator);
    assertEquals((Double) 0.3888888888888889, fieldCounter.get("SEARCHABILITY"));
    // printCheck(Category.REUSABILITY, calculator);
    assertEquals((Double) 0.36363636363636365, fieldCounter.get("REUSABILITY"));
    assertEquals(35, existenceCounter.size());
    assertEquals(35, cardinalityCounter.size());

    String expected = "0.4,1.0,0.181818,0.388889,0.272727,0.5,0.357143,0.75,0.363636,0.4,1,1,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0,1,1,0,0,0,0,0,1,1,0,0,0,0,5,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0";

    assertEquals(expected, calculator.getCsv(false, CompressionLevel.NORMAL));
  }

  private List<String> getFieldsByCategory(Category category, Schema schema) {
    List<String> labels = new ArrayList<>();
    for (DataElement dataElement : schema.getPaths()) {
      if (dataElement.getCategories().contains(category)) {
        labels.add(dataElement.getLabel());
      }
    }

    for (FieldGroup group : schema.getFieldGroups()) {
      if (group.getCategory().equals(category)) {
        for (String field : group.getFields()) {
          if (!labels.contains(field)) {
            labels.add(field);
          }
        }
      }
    }
    return labels;
  }

  private void printCheck(Category category, CompletenessCalculator calculator) {
    EdmOaiPmLimitedJsonSchema schema = new EdmOaiPmLimitedJsonSchema();

    FieldCounter<Double> fieldCounter = calculator.getCompletenessCounter().getFieldCounter();
    FieldCounter<Boolean> existenceCounter = calculator.getExistenceCounter();
    FieldCounter<Integer> cardinalityCounter = calculator.getCardinalityCounter();

    List<String> fields = getFieldsByCategory(category, schema);
    var i = 0;
    for (String field : fields) {
      System.err.println(
            String.format(
                 "%d) field %s: %s, %d",
                 ++i,
                 field,
                 existenceCounter.get(field).toString(),
                 cardinalityCounter.get(field)
            )
      );
    }
  }

  @Test
  public void testExistenceMap() throws URISyntaxException, IOException {
    calculator.collectFields(true);

    calculator.measure(cache);
    Map<String, Boolean> existenceMap = calculator.getExistenceMap();
    assertEquals(35, existenceMap.size());

    var existingFieldCounter = 0;
    for (boolean existing : existenceMap.values())
      if (existing)
        existingFieldCounter++;
    assertEquals(14, existingFieldCounter);

    assertTrue(existenceMap.get("edm:ProvidedCHO/@about"));
    assertTrue(existenceMap.get("Proxy/dc:title"));
    assertFalse(existenceMap.get("Proxy/dcterms:alternative"));
    assertFalse(existenceMap.get("Proxy/dc:description"));
    assertFalse(existenceMap.get("Proxy/dc:creator"));
    assertFalse(existenceMap.get("Proxy/dc:publisher"));
    assertFalse(existenceMap.get("Proxy/dc:contributor"));
    assertTrue(existenceMap.get("Proxy/dc:type"));
    assertTrue(existenceMap.get("Proxy/dc:identifier"));
    assertFalse(existenceMap.get("Proxy/dc:language"));
    assertFalse(existenceMap.get("Proxy/dc:coverage"));
    assertFalse(existenceMap.get("Proxy/dcterms:temporal"));
    assertFalse(existenceMap.get("Proxy/dcterms:spatial"));
    assertTrue(existenceMap.get("Proxy/dc:subject"));
    assertFalse(existenceMap.get("Proxy/dc:date"));
    assertFalse(existenceMap.get("Proxy/dcterms:created"));
    assertFalse(existenceMap.get("Proxy/dcterms:issued"));
    assertFalse(existenceMap.get("Proxy/dcterms:extent"));
    assertFalse(existenceMap.get("Proxy/dcterms:medium"));
    assertFalse(existenceMap.get("Proxy/dcterms:provenance"));
    assertFalse(existenceMap.get("Proxy/dcterms:hasPart"));
    assertTrue(existenceMap.get("Proxy/dcterms:isPartOf"));
    assertFalse(existenceMap.get("Proxy/dc:format"));
    assertFalse(existenceMap.get("Proxy/dc:source"));
    assertTrue(existenceMap.get("Proxy/dc:rights"));
    assertFalse(existenceMap.get("Proxy/dc:relation"));
    assertFalse(existenceMap.get("Proxy/edm:isNextInSequence"));
    assertTrue(existenceMap.get("Proxy/edm:type"));
    assertTrue(existenceMap.get("Aggregation/edm:rights"));
    assertTrue(existenceMap.get("Aggregation/edm:provider"));
    assertTrue(existenceMap.get("Aggregation/edm:dataProvider"));
    assertTrue(existenceMap.get("Aggregation/edm:isShownAt"));
    assertTrue(existenceMap.get("Aggregation/edm:isShownBy"));
    assertTrue(existenceMap.get("Aggregation/edm:object"));
    assertFalse(existenceMap.get("Aggregation/edm:hasView"));
  }

  @Test
  public void testExistenceList() throws URISyntaxException, IOException {
    calculator.collectFields(true);
    calculator.measure(cache);
    List<Integer> expected = Arrays.asList(new Integer[]{
      1,1,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0});
    assertEquals(35, calculator.getExistenceCounter().size());
    assertEquals(expected,
      calculator.getExistenceCounter().getCsv().stream()
        .map(v -> BooleanUtils.toInteger((boolean)v))
        .collect(toList()));
  }

  @Test
  public void testGetHeaders() {
    calculator = new CompletenessCalculator(new EdmOaiPmLimitedJsonSchema());
    List<String> headers = calculator.getHeader();
    List<String> expected = Arrays.asList("completeness:TOTAL", "completeness:MANDATORY", "completeness:DESCRIPTIVENESS", "completeness:SEARCHABILITY", "completeness:CONTEXTUALIZATION", "completeness:IDENTIFICATION", "completeness:BROWSING", "completeness:VIEWING", "completeness:REUSABILITY", "completeness:MULTILINGUALITY", "existence:edm:ProvidedCHO/@about", "existence:Proxy/dc:title", "existence:Proxy/dcterms:alternative", "existence:Proxy/dc:description", "existence:Proxy/dc:creator", "existence:Proxy/dc:publisher", "existence:Proxy/dc:contributor", "existence:Proxy/dc:type", "existence:Proxy/dc:identifier", "existence:Proxy/dc:language", "existence:Proxy/dc:coverage", "existence:Proxy/dcterms:temporal", "existence:Proxy/dcterms:spatial", "existence:Proxy/dc:subject", "existence:Proxy/dc:date", "existence:Proxy/dcterms:created", "existence:Proxy/dcterms:issued", "existence:Proxy/dcterms:extent", "existence:Proxy/dcterms:medium", "existence:Proxy/dcterms:provenance", "existence:Proxy/dcterms:hasPart", "existence:Proxy/dcterms:isPartOf", "existence:Proxy/dc:format", "existence:Proxy/dc:source", "existence:Proxy/dc:rights", "existence:Proxy/dc:relation", "existence:Proxy/edm:isNextInSequence", "existence:Proxy/edm:type", "existence:Aggregation/edm:rights", "existence:Aggregation/edm:provider", "existence:Aggregation/edm:dataProvider", "existence:Aggregation/edm:isShownAt", "existence:Aggregation/edm:isShownBy", "existence:Aggregation/edm:object", "existence:Aggregation/edm:hasView", "cardinality:edm:ProvidedCHO/@about", "cardinality:Proxy/dc:title", "cardinality:Proxy/dcterms:alternative", "cardinality:Proxy/dc:description", "cardinality:Proxy/dc:creator", "cardinality:Proxy/dc:publisher", "cardinality:Proxy/dc:contributor", "cardinality:Proxy/dc:type", "cardinality:Proxy/dc:identifier", "cardinality:Proxy/dc:language", "cardinality:Proxy/dc:coverage", "cardinality:Proxy/dcterms:temporal", "cardinality:Proxy/dcterms:spatial", "cardinality:Proxy/dc:subject", "cardinality:Proxy/dc:date", "cardinality:Proxy/dcterms:created", "cardinality:Proxy/dcterms:issued", "cardinality:Proxy/dcterms:extent", "cardinality:Proxy/dcterms:medium", "cardinality:Proxy/dcterms:provenance", "cardinality:Proxy/dcterms:hasPart", "cardinality:Proxy/dcterms:isPartOf", "cardinality:Proxy/dc:format", "cardinality:Proxy/dc:source", "cardinality:Proxy/dc:rights", "cardinality:Proxy/dc:relation", "cardinality:Proxy/edm:isNextInSequence", "cardinality:Proxy/edm:type", "cardinality:Aggregation/edm:rights", "cardinality:Aggregation/edm:provider", "cardinality:Aggregation/edm:dataProvider", "cardinality:Aggregation/edm:isShownAt", "cardinality:Aggregation/edm:isShownBy", "cardinality:Aggregation/edm:object", "cardinality:Aggregation/edm:hasView");
    assertEquals(expected, headers);
  }

  @Test
  public void testExistenceFlag() throws URISyntaxException, IOException {
    calculator.measure(cache);
    calculator.setExistence(false);
    calculator.setCardinality(false);
    assertEquals("\"TOTAL\":0.400000,\"MANDATORY\":1.000000,\"DESCRIPTIVENESS\":0.181818,\"SEARCHABILITY\":0.388889,\"CONTEXTUALIZATION\":0.272727,\"IDENTIFICATION\":0.500000,\"BROWSING\":0.357143,\"VIEWING\":0.750000,\"REUSABILITY\":0.363636,\"MULTILINGUALITY\":0.400000",
      calculator.getCsv(true, CompressionLevel.ZERO));
    assertEquals("0.400000,1.000000,0.181818,0.388889,0.272727,0.500000,0.357143,0.750000,0.363636,0.400000",
      calculator.getCsv(false, CompressionLevel.ZERO));
    assertEquals("0.4,1.0,0.181818,0.388889,0.272727,0.5,0.357143,0.75,0.363636,0.4",
      calculator.getCsv(false, CompressionLevel.NORMAL));

    calculator.setExistence(true);
    assertEquals("\"TOTAL\":0.400000,\"MANDATORY\":1.000000,\"DESCRIPTIVENESS\":0.181818,\"SEARCHABILITY\":0.388889,\"CONTEXTUALIZATION\":0.272727,\"IDENTIFICATION\":0.500000,\"BROWSING\":0.357143,\"VIEWING\":0.750000,\"REUSABILITY\":0.363636,\"MULTILINGUALITY\":0.400000,\"edm:ProvidedCHO/@about\":1,\"Proxy/dc:title\":1,\"Proxy/dcterms:alternative\":0,\"Proxy/dc:description\":0,\"Proxy/dc:creator\":0,\"Proxy/dc:publisher\":0,\"Proxy/dc:contributor\":0,\"Proxy/dc:type\":1,\"Proxy/dc:identifier\":1,\"Proxy/dc:language\":0,\"Proxy/dc:coverage\":0,\"Proxy/dcterms:temporal\":0,\"Proxy/dcterms:spatial\":0,\"Proxy/dc:subject\":1,\"Proxy/dc:date\":0,\"Proxy/dcterms:created\":0,\"Proxy/dcterms:issued\":0,\"Proxy/dcterms:extent\":0,\"Proxy/dcterms:medium\":0,\"Proxy/dcterms:provenance\":0,\"Proxy/dcterms:hasPart\":0,\"Proxy/dcterms:isPartOf\":1,\"Proxy/dc:format\":0,\"Proxy/dc:source\":0,\"Proxy/dc:rights\":1,\"Proxy/dc:relation\":0,\"Proxy/edm:isNextInSequence\":0,\"Proxy/edm:type\":1,\"Aggregation/edm:rights\":1,\"Aggregation/edm:provider\":1,\"Aggregation/edm:dataProvider\":1,\"Aggregation/edm:isShownAt\":1,\"Aggregation/edm:isShownBy\":1,\"Aggregation/edm:object\":1,\"Aggregation/edm:hasView\":0",
      calculator.getCsv(true, CompressionLevel.ZERO));
    assertEquals("0.400000,1.000000,0.181818,0.388889,0.272727,0.500000,0.357143,0.750000,0.363636,0.400000,1,1,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0",
      calculator.getCsv(false, CompressionLevel.ZERO));
    assertEquals("0.4,1.0,0.181818,0.388889,0.272727,0.5,0.357143,0.75,0.363636,0.4,1,1,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0",
      calculator.getCsv(false, CompressionLevel.NORMAL));
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testInvalidRecord() throws URISyntaxException, IOException {

    thrown.expect(InvalidJsonException.class);
    thrown.expectMessage("Unexpected character (:) at position 28");

    cache = new JsonPathCache(FileUtils.readFirstLineFromResource("general/invalid.json"));
    calculator.measure(cache);
    fail("Should throw an exception if the JSON string is invalid.");
  }

  @Test
  public void testFullResults() {
    calculator.measure(cache);
    assertEquals("\"TOTAL\":0.400000,\"MANDATORY\":1.000000,\"DESCRIPTIVENESS\":0.181818,\"SEARCHABILITY\":0.388889,\"CONTEXTUALIZATION\":0.272727,\"IDENTIFICATION\":0.500000,\"BROWSING\":0.357143,\"VIEWING\":0.750000,\"REUSABILITY\":0.363636,\"MULTILINGUALITY\":0.400000,\"edm:ProvidedCHO/@about\":1,\"Proxy/dc:title\":1,\"Proxy/dcterms:alternative\":0,\"Proxy/dc:description\":0,\"Proxy/dc:creator\":0,\"Proxy/dc:publisher\":0,\"Proxy/dc:contributor\":0,\"Proxy/dc:type\":1,\"Proxy/dc:identifier\":1,\"Proxy/dc:language\":0,\"Proxy/dc:coverage\":0,\"Proxy/dcterms:temporal\":0,\"Proxy/dcterms:spatial\":0,\"Proxy/dc:subject\":1,\"Proxy/dc:date\":0,\"Proxy/dcterms:created\":0,\"Proxy/dcterms:issued\":0,\"Proxy/dcterms:extent\":0,\"Proxy/dcterms:medium\":0,\"Proxy/dcterms:provenance\":0,\"Proxy/dcterms:hasPart\":0,\"Proxy/dcterms:isPartOf\":1,\"Proxy/dc:format\":0,\"Proxy/dc:source\":0,\"Proxy/dc:rights\":1,\"Proxy/dc:relation\":0,\"Proxy/edm:isNextInSequence\":0,\"Proxy/edm:type\":1,\"Aggregation/edm:rights\":1,\"Aggregation/edm:provider\":1,\"Aggregation/edm:dataProvider\":1,\"Aggregation/edm:isShownAt\":1,\"Aggregation/edm:isShownBy\":1,\"Aggregation/edm:object\":1,\"Aggregation/edm:hasView\":0,\"edm:ProvidedCHO/@about\":1,\"Proxy/dc:title\":1,\"Proxy/dcterms:alternative\":0,\"Proxy/dc:description\":0,\"Proxy/dc:creator\":0,\"Proxy/dc:publisher\":0,\"Proxy/dc:contributor\":0,\"Proxy/dc:type\":1,\"Proxy/dc:identifier\":1,\"Proxy/dc:language\":0,\"Proxy/dc:coverage\":0,\"Proxy/dcterms:temporal\":0,\"Proxy/dcterms:spatial\":0,\"Proxy/dc:subject\":5,\"Proxy/dc:date\":0,\"Proxy/dcterms:created\":0,\"Proxy/dcterms:issued\":0,\"Proxy/dcterms:extent\":0,\"Proxy/dcterms:medium\":0,\"Proxy/dcterms:provenance\":0,\"Proxy/dcterms:hasPart\":0,\"Proxy/dcterms:isPartOf\":1,\"Proxy/dc:format\":0,\"Proxy/dc:source\":0,\"Proxy/dc:rights\":1,\"Proxy/dc:relation\":0,\"Proxy/edm:isNextInSequence\":0,\"Proxy/edm:type\":1,\"Aggregation/edm:rights\":1,\"Aggregation/edm:provider\":1,\"Aggregation/edm:dataProvider\":1,\"Aggregation/edm:isShownAt\":1,\"Aggregation/edm:isShownBy\":1,\"Aggregation/edm:object\":1,\"Aggregation/edm:hasView\":0",
      calculator.getCsv(true, CompressionLevel.ZERO));

    assertEquals("0.400000,1.000000,0.181818,0.388889,0.272727,0.500000,0.357143,0.750000,0.363636,0.400000,1,1,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0,1,1,0,0,0,0,0,1,1,0,0,0,0,5,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0",
      calculator.getCsv(false, CompressionLevel.ZERO));
    assertEquals("0.4,1.0,0.181818,0.388889,0.272727,0.5,0.357143,0.75,0.363636,0.4,1,1,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0,1,1,0,0,0,0,0,1,1,0,0,0,0,5,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0",
    calculator.getCsv(false, CompressionLevel.NORMAL));
  }
}
