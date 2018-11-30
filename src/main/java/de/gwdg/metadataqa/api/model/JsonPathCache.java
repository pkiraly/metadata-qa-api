package de.gwdg.metadataqa.api.model;

import de.gwdg.metadataqa.api.json.JsonUtils;
import de.gwdg.metadataqa.api.util.ExceptionUtils;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.spi.json.JsonProvider;
import net.minidev.json.JSONArray;

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

	private static final Logger LOGGER = Logger.getLogger(JsonPathCache.class.getCanonicalName());

	private final Object jsonDocument;
	private String recordId;
	private String jsonString;
	private final Map<String, List<T>> cache = new HashMap<>();
	private final Map<String, Object> typedCache = new HashMap<>();
	private final Map<String, Object> fragmentCache = new HashMap<>();
	private JsonProvider jsonProvider = Configuration.defaultConfiguration().jsonProvider();

	public JsonPathCache(String jsonString) throws InvalidJsonException {
		this.jsonString = jsonString;
		this.jsonDocument = jsonProvider.parse(jsonString);
	}

	public JsonPathCache(Object jsonDocument) {
		this.jsonDocument = jsonDocument;
	}

	private void set(String address, String jsonPath, Object jsonFragment, Class clazz) {
		List<T> instances = null;
		Object value = read(jsonPath, jsonFragment);
		if (value != null) {
			if (clazz == null) {
				instances = (List<T>) JsonUtils.extractFieldInstanceList(value, recordId, jsonPath);
			} else {
				if (value instanceof JSONArray) {
					typedCache.put(address, clazz.cast(((JSONArray) value).get(0)));
				} else {
					typedCache.put(address, value);
				}
			}
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
			// LOGGER.severe("PathNotFound: " + jsonPath + " " + e.getLocalizedMessage() + extractRelevantPath(e));
		} catch (InvalidPathException e) {
			LOGGER.log(Level.SEVERE, "Invalid Path: {0} {1}\n{2}", new Object[]{
				jsonPath, e.getLocalizedMessage(), ExceptionUtils.extractRelevantPath(e)
			});
		}
		return value;
	}

	public List<T> get(String jsonPath) {
		return get(jsonPath, jsonPath, null, null);
	}

	public <E> E get(String jsonPath, Class clazz) {
		if (!typedCache.containsKey(jsonPath)) {
			set(jsonPath, jsonPath, null, clazz);
		}
		return (E) typedCache.get(jsonPath);
	}

	public List<T> get(String address, String jsonPath, Object jsonFragment) {
		return get(address, jsonPath, jsonFragment, null);
	}

	public List<T> get(String address, String jsonPath, Object jsonFragment, Class clazz) {
		if (!cache.containsKey(address)) {
			set(address, jsonPath, jsonFragment, clazz);
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
	 * Get a JSON fragment from cache
	 * @param address - a unique address for cahce
	 * @param jsonPath - a JSON path expression
	 * @param jsonFragment - a JSON fragment in which the path should be searched for
	 * @return
	 */
	public Object getFragment(String address, String jsonPath, Object jsonFragment) {
		Object jsonFragment2 = null;
		if (!fragmentCache.containsKey(address)) {
			jsonFragment2 = read(jsonPath, jsonFragment);
			fragmentCache.put(address, jsonFragment2);
		} else {
			jsonFragment2 = fragmentCache.get(address);
		}
		return jsonFragment2;
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

	public String getJsonString() {
		return jsonString;
	}
}
