package de.gwdg.metadataqa.api.counter;

import de.gwdg.metadataqa.api.json.JsonBranch;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class CompletenessCounter {

	public static final String TOTAL = "TOTAL";
	private Map<String, BasicCounter> basicCounters;

	public CompletenessCounter() {
		initialize();
	}
	
	public BasicCounter get(String key) {
		return basicCounters.get(key);
	}

	public FieldCounter<Double> getFieldCounter() {
		FieldCounter<Double> fieldCounter = new FieldCounter<>();
		for (Map.Entry<String, BasicCounter> counter : basicCounters.entrySet()) {
			counter.getValue().calculate();
			fieldCounter.put(counter.getKey(), counter.getValue().getResult());
		}
		return fieldCounter;
	}

	public void calculateResults() {
		for (BasicCounter counter : basicCounters.values()) {
			counter.calculate();
		}
	}

	public void increaseInstance(List<JsonBranch.Category> categories) {
		basicCounters.get(TOTAL).increaseInstance();
		for (JsonBranch.Category category : categories) {
			basicCounters.get(category.name()).increaseInstance();
		}
	}

	public void increaseInstance(JsonBranch.Category category, boolean increase) {
		basicCounters.get(category.name()).increaseTotal();
		if (increase) {
			basicCounters.get(category.name()).increaseInstance();
		}
	}

	public void increaseTotal(List<JsonBranch.Category> categories) {
		basicCounters.get(TOTAL).increaseTotal();
		for (JsonBranch.Category category : categories) {
			basicCounters.get(category.name()).increaseTotal();
		}
	}

	private void initialize() {
		basicCounters = new LinkedHashMap<>();
		for (String name : getHeaders()) {
			basicCounters.put(name, new BasicCounter());
		}
	}

	public static List<String> getHeaders() {
		List<String> headers = new ArrayList<>();
		headers.add(TOTAL);
		for (JsonBranch.Category category : JsonBranch.Category.values()) {
			headers.add(category.name());
		}
		return headers;
	}

	public BasicCounter getStatComponent(JsonBranch.Category category) {
		return basicCounters.get(category.name());
	}

}
