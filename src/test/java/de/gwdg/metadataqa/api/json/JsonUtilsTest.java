package de.gwdg.metadataqa.api.json;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JsonProvider;
import de.gwdg.metadataqa.api.calculator.CompletenessCalculator;
import de.gwdg.metadataqa.api.model.pathcache.JsonPathCache;
import de.gwdg.metadataqa.api.schema.EdmFullBeanSchema;
import de.gwdg.metadataqa.api.util.CompressionLevel;
import de.gwdg.metadataqa.api.util.FileUtils;
import net.minidev.json.JSONArray;
import org.junit.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JsonUtilsTest {

  Object jsonDoc;
  String jsonString;
  String problemFileName = "issue-examples/issue48.json";
  private static final JsonProvider JSON_PROVIDER = Configuration.defaultConfiguration()
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
    calculator.getCsv(false, CompressionLevel.NORMAL);
  }

  @Test
  public void extractList_string() {
    Object jsonDoc = JSON_PROVIDER.parse("a");
    List<String> list = JsonUtils.extractList(jsonDoc);
    assertEquals(1, list.size());
    assertEquals(Arrays.asList("a"), list);
  }

  @Test
  public void extractList_list() {
    Object jsonDoc = JSON_PROVIDER.parse("[\"a\", \"b\"]");
    List<String> list = JsonUtils.extractList(jsonDoc);
    assertEquals(2, list.size());
    assertEquals(Arrays.asList("a", "b"), list);
  }

  @Test
  public void extractList_listOfList() {
    Object jsonDoc = JSON_PROVIDER.parse("[[\"a\", \"b\"]]");
    List<String> list = JsonUtils.extractList(jsonDoc);
    assertEquals(2, list.size());
    assertEquals(Arrays.asList("a", "b"), list);
  }

  @Test
  public void extract() {
    Object jsonDoc = JSON_PROVIDER.parse("{\"a\":[\"a\", \"b\"]}");
    Object value = JsonUtils.extractField(jsonDoc, "$.a");
    assertEquals(JSONArray.class, value.getClass());
    JSONArray list = (JSONArray) value;

    assertEquals(2, list.size());
    assertEquals("a", list.get(0));
    assertEquals("b", list.get(1));
    assertEquals(Arrays.asList("a", "b"), list);
  }

  @Test
  public void getType() {
    assertEquals(
      "java.lang.String",
      JsonUtils.getType(JSON_PROVIDER.parse("\"a\"")));

    assertEquals(
      "java.lang.String",
      JsonUtils.getType(JSON_PROVIDER.parse("a")));

    assertEquals(
      "net.minidev.json.JSONArray",
      JsonUtils.getType(JSON_PROVIDER.parse("[1, 2]")));

    assertEquals(
      "net.minidev.json.JSONArray",
      JsonUtils.getType(JSON_PROVIDER.parse("[\"a\", \"b\"]")));

    assertEquals(
      "java.util.LinkedHashMap",
      JsonUtils.getType(JSON_PROVIDER.parse("{\"a\":[\"a\", \"b\"]}")));
  }

  @Test
  public void extractString() {
    assertEquals(
      "a",
      JsonUtils.extractString(JSON_PROVIDER.parse("\"a\"")));

    assertEquals(
      "a",
      JsonUtils.extractString(JSON_PROVIDER.parse("a")));

    assertEquals(
      "1",
      JsonUtils.extractString(JSON_PROVIDER.parse("[1, 2]")));

    assertEquals(
      "a",
      JsonUtils.extractString(JSON_PROVIDER.parse("[\"a\", \"b\"]")));

    assertEquals(
      "a",
      JsonUtils.extractString(JSON_PROVIDER.parse("{\"a\":[\"a\", \"b\"]}")));
  }
}
