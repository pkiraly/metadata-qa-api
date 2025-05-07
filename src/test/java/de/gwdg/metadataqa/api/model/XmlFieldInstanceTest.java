package de.gwdg.metadataqa.api.model;

import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

public class XmlFieldInstanceTest extends TestCase {

  @Test
  public void test_isEmpty_1() {
    XmlFieldInstance x = new XmlFieldInstance("test");
    assertFalse(x.isEmpty());
  }

  @Test
  public void test_isEmpty_2() {
    XmlFieldInstance x = new XmlFieldInstance(null, "en");
    assertFalse(x.isEmpty());
  }

  @Test
  public void test_isEmpty_3() {
    XmlFieldInstance x = new XmlFieldInstance(null);
    assertTrue(x.isEmpty());
  }

  @Test
  public void test_isEmpty_4() {
    XmlFieldInstance x = new XmlFieldInstance(null, null);
    assertTrue(x.isEmpty());
  }

  @Test
  public void testToString_noLang() {
    XmlFieldInstance x = new XmlFieldInstance("test");
    assertEquals("XmlFieldInstance{value=test, language=null}", x.toString());
  }

  @Test
  public void testToString_lang() {
    XmlFieldInstance x = new XmlFieldInstance("test", "en");
    assertEquals("XmlFieldInstance{value=test, language=en}", x.toString());
  }

  @Test
  public void testHashCode() {
    XmlFieldInstance x = new XmlFieldInstance("test");
    assertEquals(67575267, x.hashCode());
  }

  @Test
  public void testHashCode_lang() {
    XmlFieldInstance x = new XmlFieldInstance("test", "en");
    assertEquals(67578508, x.hashCode());
  }

  @Test
  public void testEquals_1() {
    XmlFieldInstance a = new XmlFieldInstance("test");
    XmlFieldInstance b = new XmlFieldInstance("test");
    assertEquals(a, b);
    assertEquals(b, a);
  }

  @Test
  public void testEquals_2() {
    XmlFieldInstance a = new XmlFieldInstance("test1");
    XmlFieldInstance b = new XmlFieldInstance("test2");
    assertNotEquals(a, b);
    assertNotEquals(b, a);
  }

  @Test
  public void testEquals_3() {
    XmlFieldInstance a = new XmlFieldInstance("test", "en");
    XmlFieldInstance b = new XmlFieldInstance("test", "en");
    assertEquals(a, b);
    assertEquals(b, a);
  }

  @Test
  public void testEquals_4() {
    XmlFieldInstance a = new XmlFieldInstance("test", "en");
    XmlFieldInstance b = new XmlFieldInstance("test", "de");
    assertNotEquals(a, b);
    assertNotEquals(b, a);
  }

  @Test
  public void testEquals_5() {
    XmlFieldInstance a = new XmlFieldInstance("test");
    assertEquals(a, a);
  }

  @Test
  public void testEquals_6() {
    XmlFieldInstance a = new XmlFieldInstance("test");
    String b = "test";
    assertNotEquals(a, b);
    assertNotEquals(b, a);
  }
}