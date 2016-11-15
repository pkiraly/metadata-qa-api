package de.gwdg.metadataqa.api.model;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public enum LanguageSaturation {

	NA          (-1.0),
	STRING      (0.0),
	LANGUAGE    (1.0),
	TRANSLATION (2.0),
	LINK        (3.0);

	private final double value;

	LanguageSaturation(double value) {
		this.value = value;
	}

	public double value() {
		return value;
	}
}
