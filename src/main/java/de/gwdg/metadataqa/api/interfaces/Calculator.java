package de.gwdg.metadataqa.api.interfaces;

import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.util.CompressionLevel;
import java.util.List;
import java.util.Map;

/**
 * Calculator does the actual measurements.
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public interface Calculator {

	/**
	 * Measure something from the JSON input
	 * object
	 * @param cache
	 *   The JSON cache object
	 */
	void measure(JsonPathCache cache);

	Map<String, ? extends Object> getResultMap();

	Map<String, Map<String, ? extends Object>> getLabelledResultMap();

	String getCsv(boolean withLabels, CompressionLevel compressionLevel);

	List<String> getHeader();

	String getCalculatorName();
}
