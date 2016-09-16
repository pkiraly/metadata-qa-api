package de.gwdg.metadataqa.api.calculator;

import com.jayway.jsonpath.InvalidJsonException;
import de.gwdg.metadataqa.api.counter.BasicCounter;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.schema.Schema;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class LanguageCalculator implements Calculator, Serializable {

	private static final Logger LOGGER = Logger.getLogger(LanguageCalculator.class.getCanonicalName());

	private String CALCULATOR_NAME = "languages";
	private String inputFileName;
	private FieldCounter<String> languageMap;
	private Map<String, Map<String, Integer>> rawLanguageMap;

	private Schema schema;

	public LanguageCalculator() {
		// this.recordID = null;
	}

	public LanguageCalculator(Schema schema) {
		this.schema = schema;
	}

	@Override
	public String getCalculatorName() {
		return CALCULATOR_NAME;
	}

	@Override
	public void measure(JsonPathCache cache)
			throws InvalidJsonException {

		languageMap = new FieldCounter<>();
		rawLanguageMap = new LinkedHashMap<>();
		for (JsonBranch jsonBranch : schema.getPaths()) {
			if (!schema.getNoLanguageFields().contains(jsonBranch.getLabel()))
				extractLanguageTags(jsonBranch, cache, languageMap, rawLanguageMap);
		}
	}

	@Override
	public List<String> getHeader() {
		List<String> headers = new ArrayList<>();
		for (JsonBranch jsonBranch : schema.getPaths())
			if (!schema.getNoLanguageFields().contains(jsonBranch.getLabel()))
				headers.add("lang:" + jsonBranch.getLabel());
		return headers;
	}

	private void extractLanguageTags(JsonBranch jsonBranch, JsonPathCache cache,
			FieldCounter<String> languageMap,
			Map<String, Map<String, Integer>> rawLanguageMap) {
		List<EdmFieldInstance> values = cache.get(jsonBranch.getJsonPath());
		Map<String, BasicCounter> languages = new HashMap<>();
		if (values != null && !values.isEmpty()) {
			for (EdmFieldInstance field : values) {
				if (field.hasValue()) {
					if (field.hasLanguage()) {
						increase(languages, field.getLanguage());
					} else {
						increase(languages, "_0");
					}
				} else {
					increase(languages, "_2");
				}
			}
		} else {
			increase(languages, "_1");
		}
		rawLanguageMap.put(jsonBranch.getLabel(), transformLanguages(languages));
		languageMap.put(jsonBranch.getLabel(), extractLanguages(languages));
	}

	private void increase(Map<String, BasicCounter> languages, String key) {
		if (!languages.containsKey(key)) {
			languages.put(key, new BasicCounter(1));
		} else {
			languages.get(key).increaseTotal();
		}
	}

	private String extractLanguages(Map<String, BasicCounter> languages) {
		String result = "";
		for (String lang : languages.keySet()) {
			if (result.length() > 0)
				result += ";";
			result += lang + ":" + ((Double)languages.get(lang).getTotal()).intValue();
		}
		return result;
	}

	private Map<String, Integer> transformLanguages(Map<String, BasicCounter> languages) {
		Map<String, Integer> result = new LinkedHashMap<>();
		for (String lang : languages.keySet()) {
			result.put(lang, ((Double)languages.get(lang).getTotal()).intValue());
		}
		return result;
	}

	public Map<String, String> getLanguageMap() {
		return languageMap.getMap();
	}

	@Override
	public Map<String, Map<String, ? extends Object>> getLabelledResultMap() {
		Map<String, Map<String, ? extends Object>> labelledResultMap = new LinkedHashMap<>();
		labelledResultMap.put(getCalculatorName(), rawLanguageMap);
		return labelledResultMap;
	}

	@Override
	public Map<String, ? extends Object> getResultMap() {
		return languageMap.getMap();
	}

	@Override
	public String getCsv(boolean withLabel, boolean compressed) {
		return languageMap.getList(withLabel, false);
	}

}
