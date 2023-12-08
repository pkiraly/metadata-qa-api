package de.gwdg.metadataqa.api.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ComparisonFailure;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class ConverterTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  public ConverterTest() {
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
  public void testCompressNumber() {
    assertEquals("0.5", Converter.compressNumber("0.50000", CompressionLevel.NORMAL));
    assertEquals("0.0", Converter.compressNumber("0.00000", CompressionLevel.NORMAL));
  }

  @Test
  public void asDouble() {
    assertEquals(1.0, Converter.asDouble(new BigDecimal(1)), 0.00001);
    assertEquals(1.0, Converter.asDouble(new Integer(1)), 0.00001);
    assertEquals(1.0, Converter.asDouble(1), 0.00001);
    assertEquals(1.0, Converter.asDouble(1.0), 0.00001);
    assertEquals(1.0, Converter.asDouble("1.0"), 0.00001);
    assertEquals(1.0, Converter.asDouble("1"), 0.00001);
  }

  @Test
  public void asDouble_withAlpha() {
    thrown.expect(NumberFormatException.class);
    thrown.expectMessage("For input string: \"text\"");
    assertEquals(Double.valueOf(2), Converter.asDouble("text"));

    fail("Exception was not thrown.");
  }

  @Test
  public void asInteger() {
    assertEquals(Integer.valueOf(1), Converter.asInteger(new BigDecimal(1)));
    assertEquals(Integer.valueOf(1), Converter.asInteger(Boolean.valueOf(true)));
    assertEquals(Integer.valueOf(1), Converter.asInteger(true));
    assertEquals(Integer.valueOf(0), Converter.asInteger(Boolean.valueOf(false)));
    assertEquals(Integer.valueOf(0), Converter.asInteger(false));
    assertEquals(Integer.valueOf(1), Converter.asInteger(Integer.valueOf(1)));
    assertEquals(Integer.valueOf(1), Converter.asInteger(Long.valueOf(1)));
    assertEquals(Integer.valueOf(1), Converter.asInteger(1));
    assertEquals(Integer.valueOf(1), Converter.asInteger(1.0));
    assertEquals(Integer.valueOf(2), Converter.asInteger(1.9));
    assertEquals(Integer.valueOf(1), Converter.asInteger(Double.valueOf(1.0)));
    assertEquals(Integer.valueOf(1), Converter.asInteger(Float.valueOf("1.0")));
    assertEquals(Integer.valueOf(2), Converter.asInteger(Float.valueOf("1.9")));
    assertEquals(Integer.valueOf(1), Converter.asInteger("1"));
    assertEquals(Integer.valueOf(1), Converter.asInteger("1.0"));
    assertEquals(Integer.valueOf(2), Converter.asInteger("1.9"));
  }

  @Test
  public void asInteger_withList() {
    thrown.expect(ClassCastException.class);
    thrown.expectMessage("class java.util.ArrayList cannot be cast to class java.lang.Integer (java.util.ArrayList and java.lang.Integer are in module java.base of loader 'bootstrap')");

    assertEquals(new Integer(2), Converter.asInteger(new ArrayList()));

    fail("Exception was not thrown.");
  }

  @Test
  public void asInteger_withAlpha() {
    thrown.expect(NumberFormatException.class);
    thrown.expectMessage("For input string: \"text\"");

    assertEquals(new Integer(2), Converter.asInteger("text"));

    fail("Exception was not thrown.");
  }

  @Test
  public void asString() {
    assertEquals("null", Converter.asString(null));
    assertEquals("1", Converter.asString(new BigDecimal(1)));
    assertEquals("1", Converter.asString(new Boolean(true)));
    assertEquals("1", Converter.asString(true));
    assertEquals("0", Converter.asString(new Boolean(false)));
    assertEquals("0", Converter.asString(false));
    assertEquals("1", Converter.asString(new Integer(1)));
    assertEquals("1", Converter.asString(1));
    assertEquals("1.000000", Converter.asString(1.0));
    assertEquals("1, 2", Converter.asString(Arrays.asList(1, 2)));
    assertEquals("a, b", Converter.asString(Arrays.asList("a", "b")));
    assertEquals("NaN", Converter.asString(Double.NaN));
  }

  @Test
  public void asString_withInvalidInput() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Object has an unhandled type: java.util.HashMap {}");

    assertEquals("NaN", Converter.asString(new HashMap()));

    fail("Test failed");
  }
}
