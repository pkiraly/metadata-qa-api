package de.gwdg.metadataqa.api.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class StringDuplicationDetectorTest {

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
