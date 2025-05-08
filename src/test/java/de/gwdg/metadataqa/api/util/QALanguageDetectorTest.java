package de.gwdg.metadataqa.api.util;

import com.github.pemistahl.lingua.api.Language;
import java.io.IOException;
import java.util.SortedMap;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class QALanguageDetectorTest {

  @Test
  public void constructionTest() throws IOException {
    QALanguageDetector languageDetector = new QALanguageDetector();
    Language langs = languageDetector.detect("There are no plans to deprecate this class in the foreseeable future.");
    assertNotNull(langs);
    assertEquals("en", langs.getIsoCode639_1().toString());

    langs = languageDetector.detect("Der Literaturnobelpreis 2016 ging an den Musiker Bob Dylan. Mit dieser Entscheidung erkannte die Jury zum ersten Mal die literarische Qualität von Songtexten an. Nicht jeder fand das gut.");
    assertNotNull(langs);
    assertEquals("de", langs.getIsoCode639_1().toString());

    langs = languageDetector.detect("Ég a napmelegtől a kopár szík sarja.");
    assertNotNull(langs);
    assertEquals("hu", langs.getIsoCode639_1().toString());

    langs = languageDetector.detect("1984.");
    assertNotNull(langs);
    //assertFalse(langs);

    SortedMap<Language, Double> confidences = languageDetector.detectWithConfidence("Der Literaturnobelpreis 2016 ging an den Musiker Bob Dylan. Mit dieser Entscheidung erkannte die Jury zum ersten Mal die literarische Qualität von Songtexten an. Nicht jeder fand das gut.");
    assertNotNull(confidences);
    assertEquals("de", confidences.firstKey().getIsoCode639_1().toString());

  }
}
