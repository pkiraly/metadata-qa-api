package de.gwdg.metadataqa.api.counter;

import de.gwdg.metadataqa.api.util.CompressionLevel;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;

public class FieldCounterTest {

  private FieldCounter counter;

  @Before
  public void setup() {
    counter = new FieldCounter<Double>();
  }

  @Test
  public void put() {
    counter.put("dc:title", 1.0);
    assertEquals(1.0, counter.get("dc:title"));
    assertTrue(counter.has("dc:title"));

    Map<String, Double> map = counter.getMap();
    assertEquals(1, map.size());
    assertEquals(1.0, (double) map.get("dc:title"), 0.0001);
  }

  @Test
  public void map() {
    counter.put("dc:title", 1.0);
    Map<String, Double> map = counter.getMap();

    assertEquals(1, map.size());
    assertEquals(1.0, (double) map.get("dc:title"), 0.0001);
  }

  @Test
  public void getCsv_withParams() {
    counter.put("dc:title", 1.0);
    assertEquals("\"dc:title\":1.000000", counter.getCsv(true));
    assertEquals("1.000000", counter.getCsv(false));

    assertEquals("\"dc:title\":1", counter.getCsv(true, CompressionLevel.WITHOUT_TRAILING_ZEROS));
    assertEquals("\"dc:title\":1.0", counter.getCsv(true, CompressionLevel.NORMAL));

    assertEquals("1", counter.getCsv(false, CompressionLevel.WITHOUT_TRAILING_ZEROS));
    assertEquals("1.0", counter.getCsv(false, CompressionLevel.NORMAL));
  }

  @Test
  public void list() {
    counter.put("dc:title", 1.0);
    assertEquals(Arrays.asList("\"dc:title\":1"), counter.getList(true, CompressionLevel.WITHOUT_TRAILING_ZEROS));
    assertEquals(Arrays.asList("\"dc:title\":1.0"), counter.getList(true, CompressionLevel.NORMAL));

    assertEquals(Arrays.asList("1"), counter.getList(false, CompressionLevel.WITHOUT_TRAILING_ZEROS));
    assertEquals(Arrays.asList("1.0"), counter.getList(false, CompressionLevel.NORMAL));
  }

  @Test
  public void pureCsv() {
    counter.put("dc:title", 1.0);

    assertEquals(Double.class, counter.getCsv().get(0).getClass());
    assertEquals(Arrays.asList(1.0), counter.getCsv());
  }

  @Test
  public void quote() {
    String v = "Rio de Janeiro: See \"Lagoa de freitas\", Dois Irmãos (Zwei Brüder-Felsen)";
    assertTrue(v.contains("\""));
    assertEquals("Rio de Janeiro: See \"\"Lagoa de freitas\"\", Dois Irmãos (Zwei Brüder-Felsen)",
      v.replaceAll("\"", "\"\""));

    counter.put("x", v);
    assertEquals("\"x\":\"Rio de Janeiro: See \"\"Lagoa de freitas\"\", Dois Irmãos (Zwei Brüder-Felsen)\"", counter.getCsv(true));

    counter.put("x", "\"Matuto\" (Afrobrasilianer). Pernambuco");
    assertEquals("\"x\":\"\"\"Matuto\"\" (Afrobrasilianer). Pernambuco\"", counter.getCsv(true));
  }
}