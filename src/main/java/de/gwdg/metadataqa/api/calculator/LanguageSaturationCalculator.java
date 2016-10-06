package de.gwdg.metadataqa.api.calculator;

import com.jayway.jsonpath.InvalidJsonException;
import de.gwdg.metadataqa.api.counter.BasicCounter;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.model.LanguageSaturation;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.Converter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class LanguageSaturationCalculator implements Calculator, Serializable {

	private static final Logger LOGGER = Logger.getLogger(LanguageSaturationCalculator.class.getCanonicalName());
	private static final String NA = "n.a.";

	private String CALCULATOR_NAME = "languageSaturation";
	private String inputFileName;
	private FieldCounter<Double> saturationMap;
	private Map<String, List<SortedMap<LanguageSaturation, Double>>> rawLanguageMap;

	private Schema schema;

	public LanguageSaturationCalculator() {
		// this.recordID = null;
	}

	public LanguageSaturationCalculator(Schema schema) {
		this.schema = schema;
	}

	@Override
	public String getCalculatorName() {
		return CALCULATOR_NAME;
	}

	@Override
	public List<String> getHeader() {
		List<String> headers = new ArrayList<>();
		for (JsonBranch jsonBranch : schema.getPaths())
			if (!schema.getNoLanguageFields().contains(jsonBranch.getLabel()))
				headers.add("lang:" + jsonBranch.getLabel());
		return headers;
	}

	@Override
	public void measure(JsonPathCache cache)
			throws InvalidJsonException {

		rawLanguageMap = new LinkedHashMap<>();
		if (schema.getCollectionPaths().isEmpty()) {
			measureFlatSchema(cache);
		} else {
			measureHierarchicalSchema(cache);
		}
		saturationMap = calculateScore(rawLanguageMap);
	}

	private void measureFlatSchema(JsonPathCache cache) {
		for (JsonBranch jsonBranch : schema.getPaths()) {
			if (!schema.getNoLanguageFields().contains(jsonBranch.getLabel()))
				extractLanguageTags(null, jsonBranch, jsonBranch.getJsonPath(), cache, rawLanguageMap);
		}
	}

	private void measureHierarchicalSchema(JsonPathCache cache) {
		for (JsonBranch collection : schema.getCollectionPaths()) {
			Object rawJsonFragment = cache.getFragment(collection.getJsonPath());
			if (rawJsonFragment == null) {
				measureMissingCollection(collection);
			} else {
				measureExistingCollection(rawJsonFragment, collection, cache);
			}
		}
	}

	private void measureMissingCollection(JsonBranch collection) {
		for (JsonBranch child : collection.getChildren()) {
			if (!schema.getNoLanguageFields().contains(child.getLabel())) {
				Map<LanguageSaturation, BasicCounter> languages = new TreeMap<>();
				increase(languages, LanguageSaturation.NA);
				updateMaps(child.getLabel(), transformLanguages(languages));
			}
		}
	}

	private void measureExistingCollection(Object rawJsonFragment, JsonBranch collection, JsonPathCache cache) {
		List<Object> jsonFragments = Converter.jsonObjectToList(rawJsonFragment);
		for (int i = 0, len = jsonFragments.size(); i < len; i++) {
			Object jsonFragment = jsonFragments.get(i);
			for (JsonBranch child : collection.getChildren()) {
				if (!schema.getNoLanguageFields().contains(child.getLabel())) {
					String address = String.format("%s/%d/%s",
							  collection.getJsonPath(), i, child.getJsonPath());
					extractLanguageTags(jsonFragment, child, address, cache, rawLanguageMap);
				}
			}
		}
	}

	private void extractLanguageTags(
			Object jsonFragment,
			JsonBranch jsonBranch,
			String address,
			JsonPathCache cache,
			Map<String, List<SortedMap<LanguageSaturation, Double>>> rawLanguageMap
	) {
		List<EdmFieldInstance> values = cache.get(address, jsonBranch.getJsonPath(), jsonFragment);
		Map<LanguageSaturation, BasicCounter> languages = new TreeMap<>();
		if (values != null && !values.isEmpty()) {
			for (EdmFieldInstance field : values) {
				if (field.hasValue()) {
					if (field.hasLanguage()) {
						increase(languages, LanguageSaturation.LANGUAGE);
					} else {
						increase(languages, LanguageSaturation.STRING);
					}
				} else {
					increase(languages, LanguageSaturation.LINK);
				}
			}
		} else {
			increase(languages, LanguageSaturation.NA);
		}
		updateMaps(jsonBranch.getLabel(), transformLanguages(languages));
	}

	private void updateMaps(String label, SortedMap<LanguageSaturation, Double> instance) {
		if (!rawLanguageMap.containsKey(label)) {
			rawLanguageMap.put(label, new ArrayList<>());
		}
		rawLanguageMap.get(label).add(instance);
	}

	private void increase(Map<LanguageSaturation, BasicCounter> languages, LanguageSaturation key) {
		if (!languages.containsKey(key)) {
			languages.put(key, new BasicCounter(1));
		} else {
			languages.get(key).increaseTotal();
		}
	}

	private String extractLanguagesFromRaw(Map<String, Integer> languages) {
		String result = "";
		for (String lang : languages.keySet()) {
			if (result.length() > 0)
				result += ";";
			result += lang + ":" + languages.get(lang);
		}
		return result;
	}

	private String extractLanguages(Map<String, BasicCounter> languages) {
		String result = "";
		for (String lang : languages.keySet()) {
			if (result.length() > 0)
				result += ";";
			result += lang + ":" + languages.get(lang).getTotalAsInt();
		}
		return result;
	}

	private SortedMap<LanguageSaturation, Double> transformLanguages(Map<LanguageSaturation, BasicCounter> languages) {
		SortedMap<LanguageSaturation, Double> result = new TreeMap<>();
		for (LanguageSaturation lang : languages.keySet()) {
			result.put(lang, languages.get(lang).getTotal());
		}
		if (result.containsKey(LanguageSaturation.LANGUAGE) && result.get(LanguageSaturation.LANGUAGE) > 1) {
			Double count = result.remove(LanguageSaturation.LANGUAGE);
			result.put(LanguageSaturation.TRANSLATION, normalizeTranslationCount(count));
		}
		keepOnlyTheBest(result);
		return result;
	}

	private double normalizeTranslationCount(double count) {
		double normalized = 0;
		if (2 <= count && count <= 3) {
			normalized = 1.0;
		} else if (4 <= count && count <= 9) {
			normalized = 2.0;
		} else {
			normalized = 3.0;
		}
		return (normalized - 1) / 3.0;
	}

	public Map<String, Double> getSaturationMap() {
		return saturationMap.getMap();
	}

	@Override
	public Map<String, Map<String, ? extends Object>> getLabelledResultMap() {
		Map<String, Map<String, ? extends Object>> labelledResultMap = new LinkedHashMap<>();
		labelledResultMap.put(getCalculatorName(), rawLanguageMap);
		return labelledResultMap;
	}

	@Override
	public Map<String, ? extends Object> getResultMap() {
		return saturationMap.getMap();
	}

	@Override
	public String getCsv(boolean withLabel, boolean compressed) {
		return saturationMap.getList(withLabel, compressed);
	}

	private void keepOnlyTheBest(SortedMap<LanguageSaturation, Double> result) {
		if (result.size() > 1) {
			LanguageSaturation best = LanguageSaturation.NA;
			for (LanguageSaturation key : result.keySet())
				if (key.value() > best.value())
					best = key;
			if (best != LanguageSaturation.NA) {
				double modifier = 0.0;
				if (best == LanguageSaturation.TRANSLATION && result.containsKey(LanguageSaturation.STRING)) {
					modifier = -0.2;
				}
				SortedMap<LanguageSaturation, Double> replacement = new TreeMap<>();
				replacement.put(best, result.get(best) + modifier);
				result = replacement;
			}
		}
	}

	private FieldCounter<Double> calculateScore(Map<String, List<SortedMap<LanguageSaturation, Double>>> rawLanguageMap) {
		FieldCounter<Double> languageMap = new FieldCounter<>();
		for (String field : rawLanguageMap.keySet()) {
			List<SortedMap<LanguageSaturation, Double>> values = rawLanguageMap.get(field);
			double sum = 0.0;
			for (SortedMap<LanguageSaturation, Double> value : values) {
				double saturation = value.firstKey().value();
				double weight = value.get(value.firstKey());
				if (value.firstKey() == LanguageSaturation.TRANSLATION) {
					saturation += weight;
				}
				sum += saturation;
			}
			double average = sum / (double)values.size();
			double result = 1.0 - (1.0/(average + 1.0));
			if (average > 0) {
				// System.err.println(field + ": " + values);
				// System.err.println("result: " + result + " vs " + (average / 4.0));
			}
			languageMap.put(field, result);
		}
		return languageMap;
	}

}
