package de.gwdg.metadataqa.api.model;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public enum LanguageSaturation {

	NA          (0),
	STRING      (0),
	LANGUAGE    (1),
	TRANSLATION (2),
	LINK        (3);

	private final int value;

	LanguageSaturation(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}
}
