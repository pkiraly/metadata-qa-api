package de.gwdg.metadataqa.api.schema;

import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.JsonPathCache;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.util.Converter;
import de.gwdg.metadataqa.api.util.FileUtils;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import net.minidev.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class MarcJsonTest {

  JsonPathCache cache;
  Schema schema = new MarcJsonSchema();
  Map<String, List<Map<String, List<String>>>> fixedValues;

  public MarcJsonTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() throws URISyntaxException, IOException {
    cache = new JsonPathCache(FileUtils.readFirstLine("general/marc.json"));
    initializeFixedValues();
    // "$.datafield[?(@.tag == '016')]"
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testAbsolutePath() {
    // String path = "$.datafield[?(@.tag == '016')][0]subfield[?(@.code == 'a')].content";
    String path = "$.datafield[?(@.tag == '016')]subfield[?(@.code == 'a')].content";
    List<XmlFieldInstance> values = cache.get(path);
    assertEquals("(OCoLC)20908784", values.get(0).getValue());

    path = "$.datafield[?(@.tag == '016')].size()";
    int value = (Integer) cache.get(path, Integer.class);
    assertEquals(4, value);

    Object fragment;
    fragment = cache.read("$.datafield[?(@.tag == '016')]", null);
    assertEquals(1, ((JSONArray) fragment).size());

    fragment = cache.read("$.datafield[?(@.tag == '935')]", null);
    assertEquals(2, ((JSONArray) fragment).size());

    path = "$..datafield[?(@.tag == '935')].size()";
    value = (Integer) cache.get(path, Integer.class);
    assertEquals(4, value);

    path = "$.datafield[?(@.tag == '935')]length()";
    value = (Integer) cache.get(path, Integer.class);
    assertEquals(4, value);

    path = "$.datafield.length()";
    value = (Integer) cache.get(path, Integer.class);
    assertEquals(34, value);
  }

  @Test
  public void testEmptyDatafield() {
    Object rawFragmentEmpty = cache.getFragment("$.datafield[?(@.tag == '1935')]");
    assertNotNull(rawFragmentEmpty);
    assertEquals(JSONArray.class, rawFragmentEmpty.getClass());
    JSONArray fragment = (JSONArray) rawFragmentEmpty;
    assertTrue(fragment.isEmpty());
    assertEquals(0, fragment.size());
  }

  @Test
  public void testDatafieldDeconstruction() {
    Object rawFragment = cache.getFragment("$.datafield[?(@.tag == '935')]");
    assertEquals(JSONArray.class, rawFragment.getClass());
    assertEquals(
        "[{\"tag\":\"935\",\"ind1\":\" \",\"ind2\":\" \"," +
            "\"subfield\":[{\"code\":\"a\",\"content\":\"mteo\"}]}," +
            "{\"tag\":\"935\",\"ind1\":\" \",\"ind2\":\" \"," +
            "\"subfield\":[{\"code\":\"b\",\"content\":\"druck\"}]}]",
        rawFragment.toString());

    JSONArray fragments = (JSONArray)rawFragment;
    assertEquals(2, fragments.size());
    assertEquals(LinkedHashMap.class, fragments.get(0).getClass());

    Map fragment1 = (Map)fragments.get(0);
    assertEquals("tag, ind1, ind2, subfield", StringUtils.join(fragment1.keySet(), ", "));
    assertEquals(String.class, fragment1.get("tag").getClass());
    assertEquals(String.class, fragment1.get("ind1").getClass());
    assertEquals(String.class, fragment1.get("ind2").getClass());
    assertEquals(JSONArray.class, fragment1.get("subfield").getClass());

    JSONArray subfields = (JSONArray) fragment1.get("subfield");
    assertEquals(1, subfields.size());
    assertEquals(LinkedHashMap.class, subfields.get(0).getClass());

    Map subfield = (Map) subfields.get(0);
    assertEquals("{code=a, content=mteo}", subfield.toString());
    assertEquals(2, subfield.size());
    assertEquals("code, content", StringUtils.join(subfield.keySet(), ", "));
    assertEquals("a", subfield.get("code"));
    assertEquals("mteo", subfield.get("content"));
  }

  @Test
  public void testDatafieldDeconstructionAdvanced() {
    Object rawFragment = cache.getFragment("$.datafield[?(@.tag == '935')]subfield");
    JSONArray fragments = (JSONArray)rawFragment;
    assertEquals(2, fragments.size());
    JSONArray fragment1 = (JSONArray)fragments.get(0);
    Map subfield = (Map) fragment1.get(0);
    assertEquals(2, subfield.size());
  }

  @Test
  public void testSubfield() {
    Object rawFragment = cache.getFragment("$.datafield[?(@.tag == '016')]subfield");
    assertNotNull(rawFragment);
    List<Object> fragments = Converter.jsonObjectToList(rawFragment, schema);
    assertEquals(1, fragments.size());
    assertEquals("net.minidev.json.JSONArray", fragments.get(0).getClass().getCanonicalName());
    Map first = (Map)((JSONArray)fragments.get(0)).get(0);
    assertEquals("{code=a, content=(OCoLC)20908784}", first.toString());
  }

  @Test
  public void testMarcSchemaIteration() {
    List<String> skippable = Arrays.asList("leader", "001", "003", "005", "006", "007", "008");
    // fixedValues
    for (JsonBranch branch : schema.getPaths()) {
      if (skippable.contains(branch.getLabel()) || branch.getParent() != null)
        continue;

      List<Map<String, List<String>>> expectedList = fixedValues.get(branch.getLabel());
      JSONArray fieldInstances = (JSONArray) cache.getFragment(branch.getJsonPath());
      for (int fieldInsanceNr = 0; fieldInsanceNr < fieldInstances.size(); fieldInsanceNr++) {
        Map<String, List<String>> expectedInstances = expectedList.get(fieldInsanceNr);
        Map fieldInstance = (Map)fieldInstances.get(fieldInsanceNr);
        for(JsonBranch subfieldDef : branch.getChildren()) {
          // alternative methods:
          // Object childInstance1 = cache.getFragment(child.getAbsoluteJsonPath(insanceNr), child.getJsonPath(), instance);
          // Object childInstance2 = cache.getFragment(child.getAbsoluteJsonPath(insanceNr));

          List<EdmFieldInstance> childInstances = (List<EdmFieldInstance>)
            cache.get(subfieldDef.getAbsoluteJsonPath(fieldInsanceNr),
              subfieldDef.getJsonPath(), fieldInstance);

          if (childInstances != null) {
            List<String> expected = expectedInstances.get(subfieldDef.getLabel());
            assertEquals(expected.size(), childInstances.size());
            for (int subfieldNr = 0; subfieldNr < childInstances.size(); subfieldNr++) {
              assertEquals(expected.get(subfieldNr), childInstances.get(subfieldNr).getValue());
            }
          }
        }
      }
    }
  }

  @Test
  public void testSubfieldMultiple() {
    Object rawFragment = cache.getFragment("$.datafield[?(@.tag == '924')]subfield");
    assertNotNull(rawFragment);
    List<Object> fragments = Converter.jsonObjectToList(rawFragment, schema);
    assertEquals(12, fragments.size());
    for (Object fragment : fragments) {
      // System.err.printf("fragment: '%s'\n", fragment);
    }
    assertEquals("net.minidev.json.JSONArray", fragments.get(0).getClass().getCanonicalName());
    Map first = (Map)((JSONArray)fragments.get(0)).get(0);
    // System.err.printf("values: '%s'\n", first);

    JsonBranch branch = schema.getPathByLabel("924");
    // System.err.printf("path: '%s'\n", branch.getJsonPath());

    rawFragment = cache.getFragment(branch.getJsonPath());
    assertNotNull(rawFragment);
    List<Object> fieldInstances = Converter.jsonObjectToList(rawFragment, schema);
    assertEquals(12, fragments.size());
    for (int i = 0; i < fieldInstances.size(); i++) {
      Object fieldInstance = fieldInstances.get(i);
      // System.err.printf("fieldInstance: '%s'\n", fieldInstance);
      List<Object> subfieldInstances = null;
      for (JsonBranch child : branch.getChildren()) {
        List<XmlFieldInstance> values = null;
        if (child.getJsonPath().startsWith("$.subfield")) {
          if (subfieldInstances == null)
            subfieldInstances = readSubfieldInstances(fieldInstance, branch.getJsonPath() + i);
          // System.err.printf("subfieldInstances: %d\n", subfieldInstances.size());
          for (int j = 0; j < subfieldInstances.size(); j++) {
            String address = child.getAbsoluteJsonPath((i * 100) + j);
            // System.err.printf("address: %s\n", address);
            // System.err.printf("subfieldInstance: %s\n", subfieldInstances.get(j));
            String path = child.getJsonPath().replace("subfield", "");
            List<XmlFieldInstance> partValues = cache.get(address,
                path, subfieldInstances.get(j));
            // System.err.printf("# %s: %b\n", path, (partValues == null));
            if (partValues != null)
              if (values == null)
                values = partValues;
              else
                values.addAll(partValues);
          }
        } else {
          values = cache.get(child.getAbsoluteJsonPath(), child.getJsonPath(), fieldInstance);
        }
        if (values != null) {
          // System.err.printf("%s nr of values: %d\n", child.getLabel(), values.size());
        }
      }
    }
  }

  private List<Object> readSubfieldInstances(Object fieldInstance, String address) {
    // System.err.printf("fieldInstance: %s\n", fieldInstance);
    // System.err.printf("address: %s\n", address);
    String subfieldPath = "$.subfield";
    Object rawJsonFragment = cache.getFragment(address, subfieldPath, fieldInstance);
    // System.err.printf("rawJsonFragment: %s\n", rawJsonFragment);
    List<Object> jsonFragments = new ArrayList<>();
    if (rawJsonFragment != null)
      jsonFragments = Converter.jsonObjectToList(rawJsonFragment, schema);
    return jsonFragments;
  }

  @Test
  public void testMarcStructure1() {
    List<XmlFieldInstance> values;
    assertEquals("$.datafield[?(@.tag == '016')][*]ind1", schema.getPathByLabel("016$ind1").getAbsoluteJsonPath());
    values = cache.get("$.datafield[?(@.tag == '016')]ind1");
    assertEquals(1, values.size());
    assertEquals(" ", values.get(0).getValue());

    JsonBranch branch = schema.getPathByLabel("016");
    String path = schema.getPathByLabel("016$ind1").getJsonPath();
    Object rawJsonFragment = cache.getFragment(branch.getJsonPath());
    if (rawJsonFragment != null) {
      List<Object> jsonFragments = Converter.jsonObjectToList(rawJsonFragment, schema);
      for (int i = 0, len = jsonFragments.size(); i < len; i++) {
        values = cache.get(path, path, jsonFragments.get(i));
        assertEquals(1, values.size());
        assertEquals(" ", values.get(0).getValue());
      }
    }
  }

  @Test
  public void testMarcStructure() {
    List<XmlFieldInstance> values;
    String subfieldPath = "$.subfield";

    for (JsonBranch branch : schema.getRootChildrenPaths()) {
      // System.err.println(branch.getLabel() + ": " + cache.read(branch.getJsonPath(), null).getClass());
      if (branch.isCollection()) {
        // System.err.printf("collection branch: label: %s, path: %s\n", branch.getLabel(), branch.getJsonPath());
        Object rawJsonFragment = cache.getFragment(branch.getJsonPath());
        if (rawJsonFragment != null) {
          List<Object> jsonFragments = Converter.jsonObjectToList(rawJsonFragment, schema);
          for (int i = 0, len = jsonFragments.size(); i < len; i++) {
            Object jsonFragment = jsonFragments.get(i);
            // System.err.printf("jsonFragment: %s\n", jsonFragment);
            for (JsonBranch child : branch.getChildren()) {
              // System.err.printf("Address: %s\n", child.getJsonPath());
              // System.err.printf("Child: %s\n", child);
              if (child.getJsonPath().startsWith("$.subfield")) {
                // System.err.printf("SUBFIELD\n");
                // System.err.printf("SUBFIELD %s\n", child.getAbsoluteJsonPath(i));
                Object rawSubfieldFragment = cache.getFragment(subfieldPath);
                if (rawSubfieldFragment != null) {
                  System.err.printf("not null\n");
                  List<Object> subfieldFragment = Converter.jsonObjectToList(rawSubfieldFragment);
                  for (int j = 0, subfieldLen = subfieldFragment.size(); j < subfieldLen; j++) {
                    values = cache.get(child.getJsonPath(), child.getJsonPath(), subfieldFragment.get(j));
                    // System.err.printf("values: %s\n", values);
                  }
                }
              } else {
                values = cache.get(child.getJsonPath(), child.getJsonPath(), jsonFragment);
                if (fixedValues.containsKey(branch.getLabel())) {
                  Map<String, List<String>> instance = fixedValues.get(branch.getLabel()).get(i);
                  if (instance.containsKey(child.getLabel())) {
                    assertNotNull(
                        String.format(
                            "Label (%s) should not be null", child.getLabel()),
                        values
                    );
                    assertEquals(child.getJsonPath(),
                        instance.get(child.getLabel()).size(),
                        values.size());
                  } else {
                    assertNull(child.getLabel(), values);
                  }
                } else {
                  assertNull(branch.getLabel(), values);
                }
              }
            }
          }
        }
      } else {
        values = (List<XmlFieldInstance>) cache.get(branch.getJsonPath());
        if (fixedValues.containsKey(branch.getLabel())) {
          assertEquals(branch.getJsonPath(), 
            fixedValues.get(branch.getLabel()).size(), values.size());
          // printValues(values, branch);
        } else {
          assertNull(branch.getLabel(), values);
        }
      }
    }
  }

  private void printValues(List<XmlFieldInstance> values, JsonBranch branch) {
    if (values != null)
      for (XmlFieldInstance value : values)
        System.err.println(String.format("%s: '%s'", branch.getLabel(), value.getValue()));
  }

  private void initializeFixedValues() {
    fixedValues = new HashMap<String, List<Map<String, List<String>>>>();

    fixedValues.put("leader",
      Arrays.asList(asSimpleMap("leader", "02341cam a2200505   4500")));

    fixedValues.put("001", Arrays.asList(
      asSimpleMap("001", "000003999")));
    fixedValues.put("003", Arrays.asList(
      asSimpleMap("003", "DE-576")));
    fixedValues.put("005", Arrays.asList(
      asSimpleMap("005", "20130328130004.0")));
    fixedValues.put("007", Arrays.asList(
      asSimpleMap("007", "tu")));
    fixedValues.put("008", Arrays.asList(
      asSimpleMap("008", "850101s1924    xx             00 0 ger c")));

    fixedValues.put("016",
      Arrays.asList(
        asSimpleMap("016$ind1", " ", "016$ind2", " ",
            "016$a", "(OCoLC)20908784")));

    fixedValues.put("035",
      Arrays.asList(
        asSimpleMap("035$ind1", " ", "035$ind2", " ",
            "035$a", "(DE-599)BSZ000003999")));

    fixedValues.put("040",
      Arrays.asList(
        asSimpleMap("040$ind1", " ", "040$ind2", " ",
        "040$a", "DE-576", "040$b", "ger", "040$c", "DE-576", "040$e", "rakwb")));

    fixedValues.put("041",
      Arrays.asList(
        asSimpleMap("041$ind1", "0", "041$ind2", " ", "041$a", "ger"),
        asSimpleMap("041$ind1", "0", "041$ind2", "7", "041$a", "dt.")
    ));

    fixedValues.put("082",
      Arrays.asList(
        asSimpleMap("082$ind1", "0", "082$ind2", " ", "082$a", "335.5")));

    fixedValues.put("084",
      Arrays.asList(
        asSimpleMap("084$ind1", " ", "084$ind2", " ", "084$a", "QV 100"),
        asSimpleMap("084$ind1", " ", "084$ind2", " ", "084$a", "NK 6805"),
        asSimpleMap("084$ind1", " ", "084$ind2", " ", "084$a", "NW 8300"),
        asSimpleMap("084$ind1", " ", "084$ind2", " ", "084$a", "1")
    ));

    fixedValues.put("100",
      Arrays.asList(
        asMap(
          asList("100$ind1", "1"),
          asList("100$ind2", " "),
          asList("100$0", "(DE-588)119067072", "(DE-576)162634978"),
          asList("100$a", "Quarck, Max"),
          asList("100$d", "1860 - 1930"))));

    fixedValues.put("245",
      Arrays.asList(
        asSimpleMap("245$ind1", "1", "245$ind2", "4",
          "245$a", "Die Erste deutsche Arbeiterbewegung :",
          "245$b", "Geschichte der Arbeiterverbrüderung 1848/49; ein Beitrag zur Theorie und Praxis des Marxismus /",
          "245$c", "von Max Quarck")));

    fixedValues.put("246",
      Arrays.asList(
        asSimpleMap("246$ind1", "3", "246$ind2", "0", "246$a", "1.  1848/1849")));

    fixedValues.put("260",
      Arrays.asList(
        asSimpleMap("260$ind1", " ", "260$ind2", " ", "260$a", "Leipzig :",
          "260$b", "Hirschfeld,", "260$c", "1924")));

    fixedValues.put("300",
      Arrays.asList(
        asSimpleMap("300$ind1", " ", "300$ind2", " ",
          "300$a", "VI, 400 S. :", "300$b", "Ill.")));

    fixedValues.put("500",
      Arrays.asList(
        asSimpleMap("500$ind1", " ", "500$ind2", " ", "500$a", "In Fraktur.")));

    fixedValues.put("591",
      Arrays.asList(
        asSimpleMap("591$ind1", " ", "591$ind2", " ", "591$a",
          "5090 maschinell aus ähnlichen Titelaufnahmen ergänzt")));

    fixedValues.put("924",
      Arrays.asList(
        asSimpleMap("924$ind1", "1", "924$ind2", " ", "924$b", "DE-21"),
        asSimpleMap("924$ind1", "1", "924$ind2", " ", "924$b", "DE-21-31"),
        asSimpleMap("924$ind1", "1", "924$ind2", " ", "924$b", "DE-25"),
        asSimpleMap("924$ind1", "1", "924$ind2", " ", "924$b", "DE-291-313"),
        asSimpleMap("924$ind1", "1", "924$ind2", " ", "924$b", "DE-14"),
        asSimpleMap("924$ind1", "1", "924$ind2", " ", "924$b", "DE-352"),
        asSimpleMap("924$ind1", "1", "924$ind2", " ", "924$b", "DE-24"),
        asSimpleMap("924$ind1", "1", "924$ind2", " ", "924$b", "DE-24"),
        asSimpleMap("924$ind1", "1", "924$ind2", " ", "924$b", "DE-180"),
        asSimpleMap("924$ind1", "1", "924$ind2", " ", "924$b", "DE-15"),
        asSimpleMap("924$ind1", "1", "924$ind2", " ", "924$b", "DE-16-160"),
        asSimpleMap("924$ind1", "1", "924$ind2", " ", "924$b", "DE-90")
      )
    );

    fixedValues.put("935",
      Arrays.asList(
        asSimpleMap("935$ind1", " ", "935$ind2", " ", "935$a", "mteo"),
        asSimpleMap("935$ind1", " ", "935$ind2", " ", "935$b", "druck")
      )
    );

    fixedValues.put("936",
      Arrays.asList(
        asMap(
          asList("936$ind1", "r"),
          asList("936$ind2", "v"),
          asList("936$a", "QV 100"),
          asList("936$b", "Geschichte"),
          asList("936$k", "Wirtschaftswissenschaften", "Arbeitnehmerfragen (Labor Economics)", "Geschichte")
        ),
        asMap(
          asList("936$ind1", "r"),
          asList("936$ind2", "v"),
          asList("936$a", "NK 6805"),
          asList("936$b", "Frühe Vereinigungen, Vorläufer"),
          asList("936$k", "Geschichte", "Weltgeschichte, Darstellungen Allgemeine Geschichte einzelner Staaten und Völker Teilgebiete der Geschichte, Sammelwerke", "Teilgebiete der Geschichte", "Geschichte der politischen und sozialen Ideen und Bewegungen und der politischen Parteien", "Parteiengeschichte von Sozialismus und Kommunismus", "Frühe Vereinigungen, Vorläufer")
        ),
        asMap(
          asList("936$ind1", "r"),
          asList("936$ind2", "v"),
          asList("936$a", "NW 8300"),
          asList("936$b", "Arbeiter (einschl. Streiks)"),
          asList("936$k", "Geschichte", "Wirtschafts- und Sozialgeschichte", "Spezielle Sozialgeschichte", "Einzelne Stände und Gesellschaftsgruppen", "Arbeiter (einschl. Streiks)")
        ),
        asMap(
          asList("936$ind1", "r"),
          asList("936$ind2", "v"),
          asList("936$a", "NW 8300"),
          asList("936$b", "Arbeiter (einschl. Streiks)")
        )
      )
    );

    // System.err.println("fixedValues.size: " + fixedValues.size());
  }

  private Map<String, List<String>> asMap(List<String>... values) {
    Map<String, List<String>> map = new HashMap<>();
    for (int i = 0; i<values.length; i++) {
      String key = values[i].remove(0);
      map.put(key, values[i]);
    }
    return map;
  }

  private Map<String, List<String>> asSimpleMap(String... values) {
    Map<String, List<String>> map = new HashMap<>();
    for (int i = 0; i<values.length; i += 2) {
      map.put(values[i], Arrays.asList(values[i + 1]));
    }
    return map;
  }

  private List<String> asList(String... values) {
    List<String> myList = new ArrayList<>();
    myList.addAll(Arrays.asList(values));
    return myList;
  }
}
