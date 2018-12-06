package de.gwdg.metadataqa.api.util;

import com.google.common.base.Optional;
import com.optimaize.langdetect.i18n.LdLocale;
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
    Optional<LdLocale> langs = languageDetector.detect("There are no plans to deprecate this class in the foreseeable future.");
    assertNotNull(langs);
    assertTrue(langs.isPresent());
    assertEquals("en", langs.get().getLanguage());

    langs = languageDetector.detect("Der Literaturnobelpreis 2016 ging an den Musiker Bob Dylan. Mit dieser Entscheidung erkannte die Jury zum ersten Mal die literarische Qualität von Songtexten an. Nicht jeder fand das gut.");
    assertNotNull(langs);
    assertTrue(langs.isPresent());
    assertEquals("de", langs.get().getLanguage());

    langs = languageDetector.detect("Ég a napmelegtől a kopár szík sarja.");
    assertNotNull(langs);
    assertTrue(langs.isPresent());
    assertEquals("hu", langs.get().getLanguage());

    langs = languageDetector.detect("1984.");
    assertNotNull(langs);
    assertFalse(langs.isPresent());

  }
}
