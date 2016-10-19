package de.gwdg.metadataqa.api.util;

import com.google.common.base.Optional;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
class QALanguageDetector {

	private List<LanguageProfile> languageProfiles;
	private LanguageDetector languageDetector;
	private TextObjectFactory textObjectFactory;

	public QALanguageDetector() throws IOException {
		languageProfiles = new LanguageProfileReader().readAllBuiltIn();

		//build language detector:
		languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
			.withProfiles(languageProfiles)
			.build();

		//create a text object factory
		textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();
	}

	public Optional<LdLocale> detect(String text) {
		//query:
		TextObject textObject = textObjectFactory.forText(text);
		Optional<LdLocale> lang = languageDetector.detect(textObject);
		return lang;
	}
}
