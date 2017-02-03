package de.gwdg.metadataqa.api.model;

import de.gwdg.metadataqa.api.json.JsonUtils;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.spi.json.JsonProvider;
import de.gwdg.metadataqa.api.util.ExceptionUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class JsonPathCache<T extends XmlFieldInstance> {

	private static final Logger logger = Logger.getLogger(JsonPathCache.class.getCanonicalName());

	private final Object jsonDocument;
	private String recordId;
	private final Map<String, List<T>> cache = new HashMap<>();
	private final Map<String, Object> fragmentCache = new HashMap<>();
	private JsonProvider jsonProvider = Configuration.defaultConfiguration().jsonProvider();

	public JsonPathCache(String jsonString) throws InvalidJsonException {
		this.jsonDocument = jsonProvider.parse(jsonString);
	}

	public JsonPathCache(Object jsonDocument) {
		this.jsonDocument = jsonDocument;
	}

	private void set(String address, String jsonPath, Object jsonFragment) {
		List<T> instances = null;
		Object value = read(jsonPath, jsonFragment);
		if (value != null) {
			instances = (List<T>) JsonUtils.extractFieldInstanceList(value, recordId, jsonPath);
		}
		if (address.contains("010")) {
			System.err.println("put to cache: " + address);
		}
		cache.put(address, instances);
	}

	public Object read(String jsonPath, Object jsonFragment) {
		Object value = null;
		try {
			if (jsonFragment != null) {
				value = JsonPath.read(jsonFragment, jsonPath);
			} else {
				value = JsonPath.read(jsonDocument, jsonPath);
			}
		} catch (PathNotFoundException e) {
			// logger.severe("PathNotFound: " + jsonPath + " " + e.getLocalizedMessage() + extractRelevantPath(e));
		} catch (InvalidPathException e) {
			logger.log(Level.SEVERE, "Invalid Path: {0} {1}\n{2}", new Object[]{
				jsonPath, e.getLocalizedMessage(), ExceptionUtils.extractRelevantPath(e)
			});
		}
		return value;
	}

	public List<T> get(String jsonPath) {
		return get(jsonPath, jsonPath, null);
	}

	public List<T> get(String address, String jsonPath, Object jsonFragment) {
		if (!cache.containsKey(address)) {
			set(address, jsonPath, jsonFragment);
		}
		return cache.get(address);
	}

	public Object getFragment(String jsonPath) {
		Object jsonFragment = null;
		if (!fragmentCache.containsKey(jsonPath)) {
			jsonFragment = read(jsonPath, null);
			fragmentCache.put(jsonPath, jsonFragment);
		} else {
			jsonFragment = fragmentCache.get(jsonPath);
		}
		return jsonFragment;
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

	public Map<String, List<T>> getCache() {
		return cache;
	}

	public Map<String, Object> getFragmentCache() {
		return fragmentCache;
	}
}
