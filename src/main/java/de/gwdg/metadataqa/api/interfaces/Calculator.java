package de.gwdg.metadataqa.api.interfaces;

import de.gwdg.metadataqa.api.counter.Counters;
import de.gwdg.metadataqa.api.model.JsonPathCache;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public interface Calculator {

	void measure(JsonPathCache cache, Counters counters);
}
