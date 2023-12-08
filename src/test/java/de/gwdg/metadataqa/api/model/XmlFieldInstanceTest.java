package de.gwdg.metadataqa.api.model;

import junit.framework.TestCase;
import org.junit.Test;

public class XmlFieldInstanceTest extends TestCase {

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
    assertTrue(a.equals(b));
    assertTrue(b.equals(a));
  }

  @Test
  public void testEquals_2() {
    XmlFieldInstance a = new XmlFieldInstance("test1");
    XmlFieldInstance b = new XmlFieldInstance("test2");
    assertFalse(a.equals(b));
    assertFalse(b.equals(a));
  }

  @Test
  public void testEquals_3() {
    XmlFieldInstance a = new XmlFieldInstance("test", "en");
    XmlFieldInstance b = new XmlFieldInstance("test", "en");
    assertTrue(a.equals(b));
    assertTrue(b.equals(a));
  }

  @Test
  public void testEquals_4() {
    XmlFieldInstance a = new XmlFieldInstance("test", "en");
    XmlFieldInstance b = new XmlFieldInstance("test", "de");
    assertFalse(a.equals(b));
    assertFalse(b.equals(a));
  }
}