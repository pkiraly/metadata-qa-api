package de.gwdg.metadataqa.api.model;

import de.gwdg.metadataqa.api.json.JsonUtils;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class JsonPathCache<T extends XmlFieldInstance> {

	private final Object jsonDocument;
	private final Map<String, List<T>> cache = new HashMap<>();
	private String recordId;

	public JsonPathCache(String jsonString) throws InvalidJsonException {
		this.jsonDocument = Configuration.defaultConfiguration().jsonProvider().parse(jsonString);
	}

	public JsonPathCache(Object jsonDocument) {
		this.jsonDocument = jsonDocument;
	}

	private void set(String jsonPath) {
		List<T> instances = null;
		try {
			Object value = JsonPath.read(jsonDocument, jsonPath);
			if (value != null) {
				instances = (List<T>) JsonUtils.extractFieldInstanceList(value, recordId, jsonPath);
			}
		} catch (PathNotFoundException e) {
			//
		}
		cache.put(jsonPath, instances);
	}

	public List<T> get(String jsonPath) {
		if (!cache.containsKey(jsonPath)) {
			set(jsonPath);
		}
		return cache.get(jsonPath);
	}

	/**
	 * Gets the record identifier
	 * @return 
	 *   The record identifier
	 */
	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
}
