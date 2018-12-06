package de.gwdg.metadataqa.api.util;

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
public class StringDuplicationDetectorTest {

  public StringDuplicationDetectorTest() {
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
  public void isDuplicatedStringFunction_repeatedString() {
    assertTrue(StringDuplicationDetector.isDuplicated("Europeana Food and DrinkEuropeana Food and Drink"));
  }

  @Test
  public void isDuplicatedStringFunction_commaSpaceSeparated() {
    assertTrue(StringDuplicationDetector.isDuplicated("1890; 1890"));
    assertTrue(StringDuplicationDetector.isDuplicated("1890, 1890"));
  }

  @Test
  public void isDuplicatedStringFunction_commaSeparated() {
    assertTrue(StringDuplicationDetector.isDuplicated("1890;1890"));
    assertTrue(StringDuplicationDetector.isDuplicated("1890,1890"));
  }

  @Test
  public void isDuplicatedStringFunction_spaceCommaSpaceSeparated() {
    assertTrue(StringDuplicationDetector.isDuplicated("1890 ; 1890"));
    assertTrue(StringDuplicationDetector.isDuplicated("1890 , 1890"));
  }

  @Test
  public void isDuplicatedStringFunction_lineBreakSeparated() {
    assertTrue(StringDuplicationDetector.isDuplicated("1890\n1890"));
  }

}
