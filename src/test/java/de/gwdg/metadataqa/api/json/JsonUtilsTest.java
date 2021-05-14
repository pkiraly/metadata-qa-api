package de.gwdg.metadataqa.api.json;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JsonProvider;
import de.gwdg.metadataqa.api.calculator.CompletenessCalculator;
import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.JsonPathCache;
import de.gwdg.metadataqa.api.schema.edm.EdmFullBeanSchema;
import de.gwdg.metadataqa.api.util.CompressionLevel;
import de.gwdg.metadataqa.api.util.FileUtils;
import net.minidev.json.JSONArray;
import org.junit.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonUtilsTest {

  Object jsonDoc;
  String jsonString;
  String problemFileName = "issue-examples/issue48.json";
  private static final JsonProvider PARSER = Configuration.defaultConfiguration()
    .jsonProvider();

  public JsonUtilsTest() throws IOException, URISyntaxException {
    jsonString = FileUtils.readFirstLineFromResource(problemFileName);
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testSimpleValue() {

    CompletenessCalculator calculator = new CompletenessCalculator(new EdmFullBeanSchema());
    calculator.collectFields(true);
    calculator.setExistence(true);
    calculator.setCardinality(true);

    JsonPathCache cache = new JsonPathCache(jsonString);
    calculator.measure(cache);
    String csv = calculator.getCsv(false, CompressionLevel.NORMAL);
    assertEquals(
      "0.30814,1.0,0.545455,0.388889,0.272727,0.7,0.285714,0.75,0.636364,0.6,1,1,1,0,1,0,0,0,1,1,0,0,0,0,1,0,1,0,1,0,1,0,0,1,0,1,0,0,1,1,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,1,0,0,0,2,2,0,0,0,0,4,0,1,0,1,0,1,0,0,1,0,1,0,0,1,1,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,10,4,4,5,0,0,0,19,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
      csv
    );
  }

  @Test
  public void extractList_string() {
    Object jsonDoc = PARSER.parse("a");
    List<String> list = JsonUtils.extractList(jsonDoc);
    assertEquals(1, list.size());
    assertEquals(Arrays.asList("a"), list);
  }

  @Test
  public void extractList_list() {
    Object jsonDoc = PARSER.parse("[\"a\", \"b\"]");
    List<String> list = JsonUtils.extractList(jsonDoc);
    assertEquals(2, list.size());
    assertEquals(Arrays.asList("a", "b"), list);
  }

  @Test
  public void extractList_listOfList() {
    Object jsonDoc = PARSER.parse("[[\"a\", \"b\"]]");
    List<String> list = JsonUtils.extractList(jsonDoc);
    assertEquals(2, list.size());
    assertEquals(Arrays.asList("a", "b"), list);
  }

  @Test
  public void extractList_listOfListOfMap() {
    assertEquals(Arrays.asList("a", "d"),
      JsonUtils.extractList(PARSER.parse("[[\"a\", {\"@about\": \"d\"}]]")));

    assertEquals(Arrays.asList("a", "d"),
      JsonUtils.extractList(PARSER.parse("[[\"a\", {\"@resource\": \"d\"}]]")));

    assertEquals(Arrays.asList("a", "d"),
      JsonUtils.extractList(PARSER.parse("[[\"a\", {\"#value\": \"d\"}]]")));
  }

  @Test
  public void extractList_map() {
    Object jsonDoc = PARSER.parse("{\"a\": \"b\"}");
    List<String> list = JsonUtils.extractList(jsonDoc);
    assertEquals(1, list.size());
    assertEquals(Arrays.asList("b"), list);
  }

  @Test
  public void extractList_mapOfList() {
    Object jsonDoc = PARSER.parse("{\"a\": [\"a\", \"b\"]}");
    List<String> list = JsonUtils.extractList(jsonDoc);
    assertEquals(2, list.size());
    assertEquals(Arrays.asList("a", "b"), list);
  }

  @Test
  public void extractList_mapOfMap() {
    Object jsonDoc = PARSER.parse("{\"a\": [\"a\", \"b\"], \"b\": \"c\"}");
    List<String> list = JsonUtils.extractList(jsonDoc);
    assertEquals(3, list.size());
    assertEquals(Arrays.asList("a", "b", "c"), list);
  }

  @Test
  public void extract() {
    Object jsonDoc = PARSER.parse("{\"a\":[\"a\", \"b\"]}");
    Object value = JsonUtils.extractField(jsonDoc, "$.a");
    assertEquals(JSONArray.class, value.getClass());
    JSONArray list = (JSONArray) value;

    assertEquals(2, list.size());
    assertEquals("a", list.get(0));
    assertEquals("b", list.get(1));
    assertEquals(Arrays.asList("a", "b"), list);
  }

  @Test
  public void testGetType() {
    assertEquals(
      "java.lang.String",
      JsonUtils.getType(PARSER.parse("\"a\"")));

    assertEquals(
      "java.lang.String",
      JsonUtils.getType(PARSER.parse("a")));

    assertEquals(
      "net.minidev.json.JSONArray",
      JsonUtils.getType(PARSER.parse("[1, 2]")));

    assertEquals(
      "net.minidev.json.JSONArray",
      JsonUtils.getType(PARSER.parse("[\"a\", \"b\"]")));

    assertEquals(
      "java.util.LinkedHashMap",
      JsonUtils.getType(PARSER.parse("{\"a\":[\"a\", \"b\"]}")));
  }

  @Test
  public void testExtractString() {
    assertEquals(
      "a",
      JsonUtils.extractString(PARSER.parse("\"a\"")));

    assertEquals(
      "a",
      JsonUtils.extractString(PARSER.parse("a")));

    assertEquals(
      "1",
      JsonUtils.extractString(PARSER.parse("[1, 2]")));

    assertEquals(
      "a",
      JsonUtils.extractString(PARSER.parse("[\"a\", \"b\"]")));

    assertEquals(
      "a",
      JsonUtils.extractString(PARSER.parse("{\"a\":[\"a\", \"b\"]}")));
  }

  @Test
  public void testExtractFieldInstanceList() {
    assertEquals(
      Arrays.asList(new EdmFieldInstance("a")),
      extractFieldInstanceList("\"a\"", ""));

    assertNull(extractFieldInstanceList("[]", ""));

    assertEquals(
      Arrays.asList(new EdmFieldInstance("a")),
      extractFieldInstanceList("[\"a\"]", ""));

    assertEquals(
      createFieldList("a", "b"),
      extractFieldInstanceList("[\"a\", \"b\"]", ""));

    assertEquals(
      createFieldList("a", "b"),
      extractFieldInstanceList("[null, \"a\", \"b\"]", ""));

    assertEquals(
      createFieldList("a", "true"),
      extractFieldInstanceList("[null, \"a\", true]", ""));

    assertEquals(
      createFieldList("a", "false"),
      extractFieldInstanceList("[null, \"a\", false]", ""));

    assertEquals(
      createFieldList("a", "12.3"),
      extractFieldInstanceList("[null, \"a\", 12.3]", ""));

    assertEquals(
      createFieldList("a", "123000000000000"),
      extractFieldInstanceList("[null, \"a\", 123000000000000]", ""));

    assertEquals(
      createFieldList("a", "12.30000000000001"),
      extractFieldInstanceList("[null, \"a\", 12.30000000000001]", ""));

    assertEquals(
      createFieldList("a", "b", "c"),
      extractFieldInstanceList("[null, \"a\", [\"b\", \"c\"]]", ""));

    List<EdmFieldInstance> expected = (List<EdmFieldInstance>) createFieldList("a");
    expected.add(new EdmFieldInstance(null, null, "c"));
    assertEquals(
      expected,
      extractFieldInstanceList("[null, \"a\", {\"@about\": \"c\"}]", ""));

    expected = (List<EdmFieldInstance>) createFieldList("a");
    expected.add(new EdmFieldInstance(null, null, "c"));
    assertEquals(
      expected,
      extractFieldInstanceList("[null, \"a\", {\"@resource\": \"c\"}]", ""));

    assertEquals(
      createFieldList("a", "c"),
      extractFieldInstanceList("[null, \"a\", {\"#value\": \"c\"}]", ""));

    expected = (List<EdmFieldInstance>) createFieldList("a");
    expected.add(new EdmFieldInstance(null, "c", null));
    assertEquals(
      expected,
      extractFieldInstanceList("[null, \"a\", {\"@lang\": \"c\"}]", ""));

    assertEquals(
      createFieldList("a", "c"),
      extractFieldInstanceList("[null, \"a\", {\"def\": \"c\"}]", ""));

    expected = (List<EdmFieldInstance>) createFieldList("a");
    expected.add(new EdmFieldInstance(null, null, null));
    assertEquals(
      expected,
      extractFieldInstanceList("[null, \"a\", {\"def\": [\"c\", \"d\"]}]", ""));

    assertEquals(
      createFieldList("a", "c"),
      extractFieldInstanceList("[null, \"a\", {\"def\": [\"c\"]}]", ""));

    assertEquals(
      Arrays.asList(new EdmFieldInstance("1")),
      extractFieldInstanceList("1", ""));

    assertEquals(
      Arrays.asList(new EdmFieldInstance("1.2")),
      extractFieldInstanceList("1.2", ""));

    assertEquals(
      Arrays.asList(new EdmFieldInstance("100.0")),
      extractFieldInstanceList("1.0E+2", ""));

    assertEquals(
      Arrays.asList(new EdmFieldInstance("100.0")),
      extractFieldInstanceList("1E+2", ""));

    assertEquals(
      Arrays.asList(new EdmFieldInstance("10000000000000000000")),
      extractFieldInstanceList("10000000000000000000", ""));

    assertEquals(
      Arrays.asList(new EdmFieldInstance("10.000000000000000001")),
      extractFieldInstanceList("10.000000000000000001", ""));

    assertEquals(
      Arrays.asList(new EdmFieldInstance("true")),
      extractFieldInstanceList("true", ""));

    assertEquals(
      Arrays.asList(new EdmFieldInstance("false")),
      extractFieldInstanceList("false", ""));

  }

  private List<? extends XmlFieldInstance> extractFieldInstanceList(String json, String path) {
    return JsonUtils.extractFieldInstanceList(PARSER.parse(json), "1", path);
  }

  private List<? extends XmlFieldInstance> createFieldList(String... args) {
    List<EdmFieldInstance> list = new ArrayList<>();
    for (String var : args)
      list.add(new EdmFieldInstance(var));

    return list;
  }
}
