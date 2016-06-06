package com.nsdr.metadataqa.api.interfaces;

import com.nsdr.metadataqa.api.counter.Counters;
import com.nsdr.metadataqa.api.model.JsonPathCache;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public interface Calculator {

	void calculate(JsonPathCache cache, Counters counters);
}
