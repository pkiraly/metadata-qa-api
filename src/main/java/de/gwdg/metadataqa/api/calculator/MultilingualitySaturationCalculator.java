package de.gwdg.metadataqa.api.calculator;

import com.jayway.jsonpath.InvalidJsonException;
import de.gwdg.metadataqa.api.counter.BasicCounter;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import de.gwdg.metadataqa.api.model.LanguageSaturationType;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.CompressionLevel;
import de.gwdg.metadataqa.api.util.Converter;
import de.gwdg.metadataqa.api.util.SkippedEntitySelector;
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
import java.util.logging.Logger;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class MultilingualitySaturationCalculator implements Calculator, Serializable {

  public static final String CALCULATOR_NAME = "multilingualitySaturation";

  private static final Logger LOGGER = Logger.getLogger(
    MultilingualitySaturationCalculator.class.getCanonicalName());
  private static final String NA = "n.a.";
  public static final double NORMALIZED_LOW = 0.0;
  public static final double NORMALIZED_MIDDLE = 0.3;
  public static final double NORMALIZED_HIGH = 0.6;
  public static final int MIDDLE_FROM = 4;
  public static final int MIDDLE_TO = 9;
  public static final int LOW_FROM = 2;
  public static final int LOW_TO = 3;
  public static final double TRANSLATION_MODIFIER = -0.2;

  /**
   * The result type of multilinguality.
   */
  public enum ResultTypes {
    NORMAL(0),
    /**
     * Adds sum, average and normalized values.
     */
    EXTENDED(1);

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
  private Map<String, List<SortedMap<LanguageSaturationType, Double>>> rawLanguageMap;

  private Schema schema;
  private SkippedEntryChecker skippedEntryChecker = null;
  private SkippedEntitySelector skippedEntitySelector = new SkippedEntitySelector();

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
      if (jsonBranch.isActive() && !schema.getNoLanguageFields().contains(jsonBranch.getLabel())) {
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
  public void measure(PathCache cache)
      throws InvalidJsonException {

    rawLanguageMap = new LinkedHashMap<>();
    if (schema.getCollectionPaths().isEmpty()) {
      measureFlatSchema(cache);
    } else {
      measureHierarchicalSchema(cache);
    }
    saturationMap = calculateScore(rawLanguageMap);
  }

  private void measureFlatSchema(PathCache cache) {
    for (JsonBranch jsonBranch : schema.getPaths()) {
      if (jsonBranch.isActive()
        && !schema.getNoLanguageFields().contains(jsonBranch.getLabel())) {
        extractLanguageTags(null, jsonBranch, jsonBranch.getJsonPath(), cache, rawLanguageMap);
      }
    }
  }

  private void measureHierarchicalSchema(PathCache cache) {
    List<String> skippableIds = getSkippableIds(cache);
    for (JsonBranch collection : schema.getCollectionPaths()) {
      if (!collection.isActive()) {
        continue;
      }
      Object rawJsonFragment = cache.getFragment(collection.getJsonPath());
      if (rawJsonFragment == null) {
        measureMissingCollection(collection);
      } else {
        measureExistingCollection(rawJsonFragment, collection, cache, skippableIds);
      }
    }
  }

  private List<String> getSkippableIds(PathCache cache) {
    return skippedEntryChecker != null
          ? skippedEntryChecker.getSkippableCollectionIds(cache)
          : new ArrayList<>();
  }

  private void measureMissingCollection(JsonBranch collection) {
    for (JsonBranch child : collection.getChildren()) {
      if (child.isActive() && !schema.getNoLanguageFields().contains(child.getLabel())) {
        Map<LanguageSaturationType, BasicCounter> languages = new TreeMap<>();
        increase(languages, LanguageSaturationType.NA);
        updateMaps(child.getLabel(), transformLanguages(languages, 0));
      }
    }
  }

  private void measureExistingCollection(Object rawJsonFragment,
        JsonBranch collection, PathCache cache, List<String> skippableIds) {
    List<Object> jsonFragments = Converter.jsonObjectToList(rawJsonFragment, schema);
    if (jsonFragments.isEmpty()) {
      measureMissingCollection(collection);
    } else {
      for (int i = 0, len = jsonFragments.size(); i < len; i++) {
        Object jsonFragment = jsonFragments.get(i);
        boolean skip = skippedEntitySelector.isCollectionSkippable(
          skippableIds, collection, i, cache, jsonFragment
        );
        if (skip) {
          // LOGGER.info(String.format("skip %s (%s)", collection.getLabel(), ((LinkedHashMap)jsonFragment).get("@about")));
          measureMissingCollection(collection);
          // TODO???
        } else {
          for (JsonBranch child : collection.getChildren()) {
            if (child.isActive()
              && !schema.getNoLanguageFields().contains(child.getLabel())) {
              String address = String.format(
                "%s/%d/%s",
                collection.getJsonPath(), i, child.getJsonPath()
              );
              extractLanguageTags(jsonFragment, child, address, cache, rawLanguageMap);
            }
          }
        }
      }
    }
  }

  private void extractLanguageTags(
      Object jsonFragment,
      JsonBranch jsonBranch,
      String address,
      PathCache cache,
      Map<String, List<SortedMap<LanguageSaturationType, Double>>> rawLanguageMap) {
    List<EdmFieldInstance> values = cache.get(address, jsonBranch.getJsonPath(), jsonFragment);
    Map<LanguageSaturationType, BasicCounter> languages = new TreeMap<>();
    Set<String> individualLanguages = new HashSet<>();
    if (values != null && !values.isEmpty()) {
      for (EdmFieldInstance field : values) {
        if (field.hasValue()) {
          if (field.hasLanguage()) {
            individualLanguages.add(field.getLanguage());
            increase(languages, LanguageSaturationType.LANGUAGE);
          } else {
            increase(languages, LanguageSaturationType.STRING);
          }
        } else {
          increase(languages, LanguageSaturationType.LINK);
        }
      }
    } else {
      increase(languages, LanguageSaturationType.NA);
    }

    updateMaps(jsonBranch.getLabel(), transformLanguages(languages, individualLanguages.size()));
  }

  private void updateMaps(String label, SortedMap<LanguageSaturationType, Double> instance) {
    if (!rawLanguageMap.containsKey(label)) {
      rawLanguageMap.put(label, new ArrayList<>());
    }
    rawLanguageMap.get(label).add(instance);
  }

  private void increase(Map<LanguageSaturationType, BasicCounter> languages, LanguageSaturationType key) {
    if (!languages.containsKey(key)) {
      languages.put(key, new BasicCounter(1));
    } else {
      languages.get(key).increaseTotal();
    }
  }

  private String extractLanguagesFromRaw(Map<String, Integer> languages) {
    String result = "";
    for (String lang : languages.keySet()) {
      if (result.length() > 0) {
        result += ";";
      }
      result += lang + ":" + languages.get(lang);
    }
    return result;
  }

  private String extractLanguages(Map<String, BasicCounter> languages) {
    String result = "";
    for (String lang : languages.keySet()) {
      if (result.length() > 0) {
        result += ";";
      }
      result += lang + ":" + languages.get(lang).getTotalAsInt();
    }
    return result;
  }

  private SortedMap<LanguageSaturationType, Double> transformLanguages(
      Map<LanguageSaturationType, BasicCounter> languages, int languageCount) {
    SortedMap<LanguageSaturationType, Double> result = new TreeMap<>();
    for (LanguageSaturationType lang : languages.keySet()) {
      result.put(lang, languages.get(lang).getTotal());
    }
    if (result.containsKey(LanguageSaturationType.LANGUAGE)
        && result.get(LanguageSaturationType.LANGUAGE) > 1
        && languageCount > 1) {
      Double count = result.remove(LanguageSaturationType.LANGUAGE);
      result.put(LanguageSaturationType.TRANSLATION, normalizeTranslationCount(languageCount));
    }
    if (languageCount > 1) {
      result = keepOnlyTheBest(result);
    }
    return result;
  }

  private double normalizeTranslationCount(double count) {
    double normalized = 0.0;
    if (isLow(count)) {
      normalized = NORMALIZED_LOW;
    } else if (isMiddle(count)) {
      normalized = NORMALIZED_MIDDLE;
    } else {
      normalized = NORMALIZED_HIGH;
    }
    return normalized;
  }

  private boolean isMiddle(double count) {
    return MIDDLE_FROM <= count && count <= MIDDLE_TO;
  }

  private boolean isLow(double count) {
    return LOW_FROM <= count && count <= LOW_TO;
  }

  public Map<String, Double> getSaturationMap() {
    return saturationMap.getMap();
  }

  @Override
  public Map<String, Map<String, ? extends Object>> getLabelledResultMap() {
    Map<String, Map<String, ? extends Object>> labelledResultMap = new LinkedHashMap<>();
//     labelledResultMap.put(getCalculatorName(), rawLanguageMap);
//    labelledResultMap.put(getCalculatorName(), saturationMap.getMap());
    labelledResultMap.put(getCalculatorName(), mergeMaps());
    return labelledResultMap;
  }

  private Map<String, Map<String, Object>> mergeMaps() {
    Map<String, Map<String, Object>> map = new LinkedHashMap<>();
    for (String key : rawLanguageMap.keySet()) {
      Map<String, Object> entry = new LinkedHashMap<>();
      List<Object> list = new ArrayList<>();
      entry.put("instances", normalizeRawValue(rawLanguageMap.get(key)));
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
    return saturationMap.getCsv(withLabel, compressionLevel);
  }

  @Override
  public List<String> getList(boolean withLabel, CompressionLevel compressionLevel) {
    return saturationMap.getList(withLabel, compressionLevel);
  }

  private SortedMap<LanguageSaturationType, Double> keepOnlyTheBest(SortedMap<LanguageSaturationType, Double> result) {
    if (result.size() > 1) {
      LanguageSaturationType best = LanguageSaturationType.NA;
      for (LanguageSaturationType key : result.keySet()) {
        if (key.value() > best.value()) {
          best = key;
        }
      }

      if (best != LanguageSaturationType.NA) {
        double modifier = 0.0;
        if (best == LanguageSaturationType.TRANSLATION
            && result.containsKey(LanguageSaturationType.STRING)) {
          modifier = TRANSLATION_MODIFIER;
        }
        SortedMap<LanguageSaturationType, Double> replacement = new TreeMap<>();
        replacement.put(best, result.get(best) + modifier);
        result = replacement;
      }
    }
    return result;
  }

  private FieldCounter<Double> calculateScore(Map<String,
      List<SortedMap<LanguageSaturationType, Double>>> rawLanguageMap) {
    double sum, average, normalized;
    List<Double> sums = new ArrayList<>();
    FieldCounter<Double> languageMap = new FieldCounter<>();
    for (String field : rawLanguageMap.keySet()) {
      Map<String, Double> fieldMap = new LinkedHashMap<>();
      List<SortedMap<LanguageSaturationType, Double>> values = rawLanguageMap.get(field);
      sum = 0.0;
      boolean isSet = false;
      for (SortedMap<LanguageSaturationType, Double> value : values) {
        double saturation = value.firstKey().value();
        if (saturation == -1.0) {
          continue;
        }
        double weight = value.get(value.firstKey());
        if (value.firstKey() == LanguageSaturationType.TRANSLATION) {
          saturation += weight;
        }
        sum += saturation;
        isSet = true;
      }
      if (!isSet) {
        average = LanguageSaturationType.NA.value();
        normalized = LanguageSaturationType.NA.value();
        sum = LanguageSaturationType.NA.value();
      } else {
        average = sum / (double) values.size();
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
    for (Double item : sums) {
      sum += item;
    }
    return sum;
  }

  private static double normalize(double average) {
    return 1.0 - (1.0 / (average + 1.0));
  }

  private Object normalizeRawValue(List<SortedMap<LanguageSaturationType, Double>> values) {
    List<SortedMap<LanguageSaturationType, Double>> normalized = new LinkedList<>();
    for (SortedMap<LanguageSaturationType, Double> value : values) {
      SortedMap<LanguageSaturationType, Double> norm = new TreeMap<>();
      double saturation = value.firstKey().value();
      double weight = value.get(value.firstKey());
      if (value.firstKey() == LanguageSaturationType.TRANSLATION) {
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

  public SkippedEntryChecker getSkippedEntryChecker() {
    return skippedEntryChecker;
  }

  public void setSkippedEntryChecker(SkippedEntryChecker skippedEntryChecker) {
    this.skippedEntryChecker = skippedEntryChecker;
    skippedEntitySelector.setSkippedEntryChecker(skippedEntryChecker);
  }

}
