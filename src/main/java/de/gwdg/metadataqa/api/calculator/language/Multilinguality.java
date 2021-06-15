package de.gwdg.metadataqa.api.calculator.language;

import de.gwdg.metadataqa.api.calculator.MultilingualitySaturationCalculator;
import de.gwdg.metadataqa.api.calculator.SkippedEntryChecker;
import de.gwdg.metadataqa.api.counter.BasicCounter;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import de.gwdg.metadataqa.api.model.LanguageSaturationType;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.Converter;
import de.gwdg.metadataqa.api.util.SkippedEntitySelector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class Multilinguality {
  private static final String NA = "n.a.";
  public static final double NORMALIZED_LOW = 0.0;
  public static final double NORMALIZED_MIDDLE = 0.3;
  public static final double NORMALIZED_HIGH = 0.6;
  public static final int MIDDLE_FROM = 4;
  public static final int MIDDLE_TO = 9;
  public static final int LOW_FROM = 2;
  public static final int LOW_TO = 3;
  public static final double TRANSLATION_MODIFIER = -0.2;
  public static final String SUM = MultilingualitySaturationCalculator.CALCULATOR_NAME + ":sum";
  public static final String AVERAGE = MultilingualitySaturationCalculator.CALCULATOR_NAME + ":average";
  public static final String NORMALIZED = MultilingualitySaturationCalculator.CALCULATOR_NAME + ":normalized";

  private Schema schema;
  private PathCache cache;
  private MultilingualitySaturationCalculator.ResultTypes resultType;
  private SkippedEntryChecker skippedEntryChecker;
  private SkippedEntitySelector skippedEntitySelector;

  Map<String, List<SortedMap<LanguageSaturationType, Double>>> rawLanguageMap = new LinkedHashMap<>();
  Map<String, Map<String, Double>> rawScoreMap = new LinkedHashMap<>();

  public Multilinguality(Schema schema,
                         PathCache cache,
                         MultilingualitySaturationCalculator.ResultTypes resultType,
                         SkippedEntryChecker skippedEntryChecker,
                         SkippedEntitySelector skippedEntitySelector) {
    this.schema = schema;
    this.cache = cache;
    this.resultType = resultType;
    this.skippedEntryChecker = skippedEntryChecker;
    this.skippedEntitySelector = skippedEntitySelector;
  }

  public FieldCounter<Double> measure() {
    if (schema.getCollectionPaths().isEmpty()) {
      measureFlatSchema();
    } else {
      measureHierarchicalSchema();
    }
    FieldCounter<Double> saturationMap = calculateScore();
    Map<String, Map<String, Object>> mergedMap = mergeMaps(rawLanguageMap, rawScoreMap);
    return saturationMap;
  }

  private List<String> getSkippableIds() {
    return skippedEntryChecker != null
      ? skippedEntryChecker.getSkippableCollectionIds(cache)
      : new ArrayList<>();
  }

  private void measureFlatSchema() {
    for (JsonBranch jsonBranch : schema.getPaths()) {
      if (jsonBranch.isActive()
        && !schema.getNoLanguageFields().contains(jsonBranch.getLabel())) {
        extractLanguageTags(null, jsonBranch, jsonBranch.getJsonPath());
      }
    }
  }

  private void measureHierarchicalSchema() {
    List<String> skippableIds = getSkippableIds();
    for (JsonBranch collection : schema.getCollectionPaths()) {
      if (!collection.isActive()) {
        continue;
      }
      Object rawJsonFragment = cache.getFragment(collection.getJsonPath());
      if (rawJsonFragment == null) {
        measureMissingCollection(collection);
      } else {
        measureExistingCollection(rawJsonFragment, collection, skippableIds);
      }
    }
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
                                         JsonBranch collection,
                                         List<String> skippableIds) {
    List<Object> jsonFragments = Converter.jsonObjectToList(rawJsonFragment, schema);
    if (jsonFragments.isEmpty()) {
      measureMissingCollection(collection);
    } else {
      for (int i = 0, len = jsonFragments.size(); i < len; i++) {
        Object jsonFragment = jsonFragments.get(i);
        boolean skip = skippedEntitySelector.isCollectionSkippable(skippableIds, collection, i, cache, jsonFragment);
        if (skip) {
          // LOGGER.info(String.format("skip %s (%s)", collection.getLabel(), ((LinkedHashMap)jsonFragment).get("@about")));
          measureMissingCollection(collection);
          // TODO???
        } else {
          for (JsonBranch child : collection.getChildren()) {
            if (child.isActive()
              && !schema.getNoLanguageFields().contains(child.getLabel())) {
              var address = String.format(
                "%s/%d/%s",
                collection.getJsonPath(), i, child.getJsonPath()
              );
              extractLanguageTags(jsonFragment, child, address);
            }
          }
        }
      }
    }
  }

  private void extractLanguageTags(Object jsonFragment,
                                   JsonBranch jsonBranch,
                                   String address) {
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

  private void updateMaps(String label,
                          SortedMap<LanguageSaturationType, Double> instance) {
    rawLanguageMap.computeIfAbsent(label, s -> new ArrayList<>());
    rawLanguageMap.get(label).add(instance);
  }

  private void increase(Map<LanguageSaturationType, BasicCounter> languages, LanguageSaturationType key) {
    if (!languages.containsKey(key)) {
      languages.put(key, new BasicCounter(1));
    } else {
      languages.get(key).increaseTotal();
    }
  }

  private SortedMap<LanguageSaturationType, Double> transformLanguages(Map<LanguageSaturationType, BasicCounter> languages,
                                                                       int languageCount) {
    SortedMap<LanguageSaturationType, Double> result = new TreeMap<>();
    for (Map.Entry<LanguageSaturationType, BasicCounter> lang : languages.entrySet())
      result.put(lang.getKey(), lang.getValue().getTotal());

    if (result.containsKey(LanguageSaturationType.LANGUAGE)
      && result.get(LanguageSaturationType.LANGUAGE) > 1
      && languageCount > 1) {
      result.remove(LanguageSaturationType.LANGUAGE);
      result.put(LanguageSaturationType.TRANSLATION, normalizeTranslationCount(languageCount));
    }
    if (languageCount > 1) {
      result = keepOnlyTheBest(result);
    }
    return result;
  }

  private double normalizeTranslationCount(double count) {
    var normalized = 0.0;
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

  private Map<String, Map<String, Object>> mergeMaps(Map<String, List<SortedMap<LanguageSaturationType, Double>>> rawLanguageMap,
                                                     Map<String, Map<String, Double>> rawScoreMap) {
    Map<String, Map<String, Object>> map = new LinkedHashMap<>();
    for (Map.Entry<String, List<SortedMap<LanguageSaturationType, Double>>> rawEntry : rawLanguageMap.entrySet()) {
      Map<String, Object> entry = new LinkedHashMap<>();
      entry.put("instances", normalizeRawValue(rawEntry.getValue()));
      entry.put("score", rawScoreMap.get(rawEntry.getKey()));
      map.put(rawEntry.getKey(), entry);
    }
    return map;
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
        var modifier = 0.0;
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

  private FieldCounter<Double> calculateScore() {
    double sum;
    double average;
    double normalized;
    List<Double> sums = new ArrayList<>();
    FieldCounter<Double> languageMap = new FieldCounter<>();
    for (Map.Entry<String, List<SortedMap<LanguageSaturationType, Double>>> field : rawLanguageMap.entrySet()) {
      Map<String, Double> fieldMap = new LinkedHashMap<>();
      List<SortedMap<LanguageSaturationType, Double>> values = field.getValue();
      sum = 0.0;
      var isSet = false;
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
      rawScoreMap.put(field.getKey(), fieldMap);

      if (resultType.equals(MultilingualitySaturationCalculator.ResultTypes.NORMAL)) {
        languageMap.put(field.getKey(), normalized);
      } else {
        languageMap.put(field.getKey() + ":sum", sum);
        languageMap.put(field.getKey() + ":average", average);
        languageMap.put(field.getKey() + ":normalized", normalized);
      }
    }
    sum = summarize(sums);
    average = sum / (double) sums.size();
    normalized = normalize(average);
    if (resultType.equals(MultilingualitySaturationCalculator.ResultTypes.EXTENDED)) {
      languageMap.put(SUM, sum);
      languageMap.put(AVERAGE, average);
    }
    languageMap.put(NORMALIZED, normalized);

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
}
