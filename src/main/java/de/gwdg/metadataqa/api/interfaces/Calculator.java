package de.gwdg.metadataqa.api.interfaces;

import de.gwdg.metadataqa.api.counter.Counters;
import de.gwdg.metadataqa.api.model.JsonPathCache;

/**
 * Calculator does the actual measurements.
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public interface Calculator {

	/**
	 * Measure something from the JSON input, and saves the result into the Counters
	 * object
	 * @param cache
	 *   The JSON cache object
	 * @param counters
	 *   The object which stores the result of the measurements
	 */
	void measure(JsonPathCache cache, Counters counters);
}
