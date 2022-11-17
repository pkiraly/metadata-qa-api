package de.gwdg.metadataqa.api.util;

import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;

import java.io.IOException;
import java.util.SortedMap;

/**
 * Language detector wrapper
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
class QALanguageDetector {

  private LanguageDetector languageDetector;

  QALanguageDetector() throws IOException {
    languageDetector = LanguageDetectorBuilder
            .fromAllSpokenLanguages()
            .build();
  }

  public Language detect(String text) {
    final Language detectedLanguage = languageDetector.detectLanguageOf(text);
    return detectedLanguage;
  }

  public SortedMap<Language, Double> detectWithConfidence(String text) {
    final SortedMap<Language, Double> detectedLanguages = languageDetector.computeLanguageConfidenceValues(text);
    return detectedLanguages;
  }
}
