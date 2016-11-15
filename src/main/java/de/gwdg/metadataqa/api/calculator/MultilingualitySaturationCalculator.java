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
import de.gwdg.metadataqa.api.util.CompressionLevel;
import de.gwdg.metadataqa.api.util.Converter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class MultilingualitySaturationCalculator implements Calculator, Serializable {

	public static final String CALCULATOR_NAME = "multilingualitySaturation";

	private static final Logger LOGGER = Logger.getLogger(MultilingualitySaturationCalculator.class.getCanonicalName());
	private static final String NA = "n.a.";

	public enum ResultTypes {
		NORMAL        (0),
		EXTENDED      (1);

		private final int value;

		ResultTypes(int value) {
			this.value = value;
		}

		public int value() {
			return value;
		}
	}

	private ResultTypes resultType = ResultTypes.NORMAL;
	private String inputFileName;
	private FieldCounter<Double> saturationMap;
	private Map<String, Map<String, Double>> rawScoreMap = new LinkedHashMap<>();
	private Map<String, List<SortedMap<LanguageSaturation, Double>>> rawLanguageMap;

	private Schema schema;

	public MultilingualitySaturationCalculator() {
		// this.recordID = null;
	}

	public MultilingualitySaturationCalculator(Schema schema) {
		this.schema = schema;
	}

	@Override
	public String getCalculatorName() {
		return CALCULATOR_NAME;
	}

	@Override
	public List<String> getHeader() {

		List<String> headers = new ArrayList<>();
		for (JsonBranch jsonBranch : schema.getPaths()) {
			if (!schema.getNoLanguageFields().contains(jsonBranch.getLabel())) {
				switch (resultType) {
					case EXTENDED:
						headers.add("lang:sum:" + jsonBranch.getLabel());
						headers.add("lang:average:" + jsonBranch.getLabel());
						headers.add("lang:normalized:" + jsonBranch.getLabel());
						break;
					case NORMAL:
					default:
						headers.add("lang:" + jsonBranch.getLabel());
						break;
				}
			}
		}
		if (resultType.equals(ResultTypes.EXTENDED)) {
			headers.add(CALCULATOR_NAME + ":sum");
			headers.add(CALCULATOR_NAME + ":average");
		}
		headers.add(CALCULATOR_NAME + ":normalized");

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
				updateMaps(child.getLabel(), transformLanguages(languages, 0));
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
		Set<String> individualLanguages = new HashSet<>();
		if (values != null && !values.isEmpty()) {
			for (EdmFieldInstance field : values) {
				if (field.hasValue()) {
					if (field.hasLanguage()) {
						individualLanguages.add(field.getLanguage());
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

		updateMaps(jsonBranch.getLabel(), transformLanguages(languages, individualLanguages.size()));
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

	private SortedMap<LanguageSaturation, Double> transformLanguages(
			Map<LanguageSaturation, BasicCounter> languages, int languageCount) {
		SortedMap<LanguageSaturation, Double> result = new TreeMap<>();
		for (LanguageSaturation lang : languages.keySet()) {
			result.put(lang, languages.get(lang).getTotal());
		}
		if (result.containsKey(LanguageSaturation.LANGUAGE)
				&& result.get(LanguageSaturation.LANGUAGE) > 1
				&& languageCount > 1) {
			Double count = result.remove(LanguageSaturation.LANGUAGE);
			result.put(LanguageSaturation.TRANSLATION, normalizeTranslationCount(languageCount));
		}
		if (languageCount > 1) {
			result = keepOnlyTheBest(result);
		}
		return result;
	}

	private double normalizeTranslationCount(double count) {
		double normalized = 0;
		if (2 <= count && count <= 3) {
			normalized = 0.0;
		} else if (4 <= count && count <= 9) {
			normalized = 0.3;
		} else {
			normalized = 0.6;
		}
		return normalized;
	}

	public Map<String, Double> getSaturationMap() {
		return saturationMap.getMap();
	}

	@Override
	public Map<String, Map<String, ? extends Object>> getLabelledResultMap() {
		Map<String, Map<String, ? extends Object>> labelledResultMap = new LinkedHashMap<>();
// 		labelledResultMap.put(getCalculatorName(), rawLanguageMap);
//		labelledResultMap.put(getCalculatorName(), saturationMap.getMap());
		labelledResultMap.put(getCalculatorName(), mergeMaps());
		return labelledResultMap;
	}

	private Map<String, Map<String, Object>> mergeMaps() {
		Map<String, Map<String, Object>> map = new LinkedHashMap<>();
		for (String key : rawLanguageMap.keySet()) {
			Map<String, Object> entry = new LinkedHashMap<>();
			List<Object> list = new ArrayList<>();
			entry.put("raw", normalizeRawValue(rawLanguageMap.get(key)));
			entry.put("score", rawScoreMap.get(key));
			map.put(key, entry);
		}
		return map;
	}

	@Override
	public Map<String, ? extends Object> getResultMap() {
		return saturationMap.getMap();
	}

	@Override
	public String getCsv(boolean withLabel, CompressionLevel compressionLevel) {
		return saturationMap.getList(withLabel, compressionLevel);
	}

	private SortedMap<LanguageSaturation, Double> keepOnlyTheBest(SortedMap<LanguageSaturation, Double> result) {
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
		return result;
	}

	private FieldCounter<Double> calculateScore(Map<String, 
			List<SortedMap<LanguageSaturation, Double>>> rawLanguageMap) {
		double sum, average, normalized;
		List<Double> sums = new ArrayList<>();
		FieldCounter<Double> languageMap = new FieldCounter<>();
		for (String field : rawLanguageMap.keySet()) {
			Map<String, Double> fieldMap = new LinkedHashMap<>();
			List<SortedMap<LanguageSaturation, Double>> values = rawLanguageMap.get(field);
			sum = 0.0;
			boolean isSet = false;
			for (SortedMap<LanguageSaturation, Double> value : values) {
				double saturation = value.firstKey().value();
				if (saturation == -1.0)
					continue;
				double weight = value.get(value.firstKey());
				if (value.firstKey() == LanguageSaturation.TRANSLATION) {
					saturation += weight;
				}
				sum += saturation;
				isSet = true;
			}
			if (!isSet) {
				sum = average = normalized = LanguageSaturation.NA.value();
			} else {
				average = sum / (double)values.size();
				normalized = normalize(average);
				sums.add(sum);
			}

			fieldMap.put("sum", sum);
			fieldMap.put("average", average);
			fieldMap.put("normalized", normalized);
			rawScoreMap.put(field, fieldMap);

			if (resultType.equals(ResultTypes.NORMAL)) {
				languageMap.put(field, normalized);
			} else {
				languageMap.put(field + ":sum", sum);
				languageMap.put(field + ":average", average);
				languageMap.put(field + ":normalized", normalized);
			}
		}
		sum = summarize(sums);
		average = sum / (double) sums.size();
		normalized = normalize(average);
		if (resultType.equals(ResultTypes.EXTENDED)) {
			languageMap.put(CALCULATOR_NAME + ":sum", sum);
			languageMap.put(CALCULATOR_NAME + ":average", average);
		}
		languageMap.put(CALCULATOR_NAME + ":normalized", normalized);

		return languageMap;
	}

	private double summarize(List<Double> sums) {
		double sum;
		sum = 0.0;
		for (Double item : sums)
			sum += item;
		return sum;
	}

	private static double normalize(double average) {
		return 1.0 - (1.0/(average + 1.0));
	}

	private Object normalizeRawValue(List<SortedMap<LanguageSaturation, Double>> values) {
		List<SortedMap<LanguageSaturation, Double>> normalized = new LinkedList<>();
		for (SortedMap<LanguageSaturation, Double> value : values) {
			SortedMap<LanguageSaturation, Double> norm = new TreeMap<>();
			double saturation = value.firstKey().value();
			double weight = value.get(value.firstKey());
			if (value.firstKey() == LanguageSaturation.TRANSLATION) {
				saturation += weight;
			}
			norm.put(value.firstKey(), saturation);
			normalized.add(norm);
		}
		return normalized;
	}

	public ResultTypes getResultType() {
		return resultType;
	}

	public void setResultType(ResultTypes resultType) {
		this.resultType = resultType;
	}
}
