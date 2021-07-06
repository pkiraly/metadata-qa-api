package de.gwdg.metadataqa.api.util;

import com.github.pemistahl.lingua.api.Language;
import java.io.IOException;
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
public class QALanguageDetectorTest {

  public QALanguageDetectorTest() {
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

  }
}
