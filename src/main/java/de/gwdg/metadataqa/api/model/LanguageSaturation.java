package de.gwdg.metadataqa.api.model;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public enum LanguageSaturation {

	NA          (0),
	STRING      (1),
	LANGUAGE    (2),
	TRANSLATION (3),
	LINK        (4);

	private final int value;

	LanguageSaturation(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}
}
