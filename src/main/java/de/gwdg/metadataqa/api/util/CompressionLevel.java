package de.gwdg.metadataqa.api.util;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public enum CompressionLevel {
	ZERO                   (0),
	NORMAL                 (1),
	/**
	 * No trailing zeros (1.0 -> 1)
	 */
	WITHOUT_TRAILING_ZEROS (2);
	private final int value;

	CompressionLevel(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}
	
}
