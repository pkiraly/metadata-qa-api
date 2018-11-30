package de.gwdg.metadataqa.api.problemcatalog;

import de.gwdg.metadataqa.api.interfaces.Observer;
import de.gwdg.metadataqa.api.interfaces.Observable;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.schema.ProblemCatalogSchema;
import de.gwdg.metadataqa.api.util.CompressionLevel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class ProblemCatalog implements Calculator, Serializable, Observable {

	private static final Logger LOGGER = Logger.getLogger(ProblemCatalog.class.getCanonicalName());

	private static final String CALCULATOR_NAME = "problemCatalog";

	private final List<Observer> problems = new ArrayList<>();
	private String jsonString;
	private Object jsonDocument;
	private JsonPathCache cache;
	private FieldCounter<Double> fieldCounter;
	private ProblemCatalogSchema schema;

	public ProblemCatalog(ProblemCatalogSchema schema) {
		this.schema = schema;
	}

	@Override
	public String getCalculatorName() {
		return CALCULATOR_NAME;
	}

	public String getJsonString() {
		return jsonString;
	}

	public Object getJsonDocument() {
		return jsonDocument;
	}

	@Override
	public void addObserver(Observer observer) {
		problems.add(observer);
	}

	@Override
	public void deleteObserver(Observer observer) {
		if (problems.contains(observer)) {
			problems.remove(observer);
		}
	}

	@Override
	public void notifyObservers() {
		for (Observer observer : problems) {
			observer.update(cache, fieldCounter);
		}
	}

	@Override
	public void measure(JsonPathCache cache) {
		this.cache = cache;
		this.fieldCounter = new FieldCounter<>();
		notifyObservers();
	}

	@Override
	public Map<String, ? extends Object> getResultMap() {
		return fieldCounter.getMap();
	}

	@Override
	public Map<String, Map<String, ? extends Object>> getLabelledResultMap() {
		Map<String, Map<String, ? extends Object>> labelledResultMap = new LinkedHashMap<>();
		labelledResultMap.put(getCalculatorName(), fieldCounter.getMap());
		return labelledResultMap;
	}

	@Override
	public String getCsv(boolean withLabels, CompressionLevel compressionLevel) {
		return fieldCounter.getList(withLabels, compressionLevel);
	}

	@Override
	public List<String> getHeader() {
		List<String> headers = new ArrayList<>();
		for (Observer observer : problems) {
			headers.add(observer.getHeader());
		}
		return headers;
	}

	public ProblemCatalogSchema getSchema() {
		return schema;
	}

}
