package de.gwdg.metadataqa.api.calculator;

import com.jayway.jsonpath.InvalidJsonException;
import de.gwdg.metadataqa.api.counter.BasicCounter;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.CompressionLevel;
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
public class LanguageCalculator implements Calculator, Serializable {

  public static final String CALCULATOR_NAME = "languages";

  private static final Logger LOGGER = Logger.getLogger(LanguageCalculator.class.getCanonicalName());

  private String inputFileName;
  private FieldCounter<String> languageMap;
  private Map<String, SortedMap<String, Integer>> rawLanguageMap;

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
  public List<String> getHeader() {
    List<String> headers = new ArrayList<>();
    for (JsonBranch jsonBranch : schema.getPaths()) {
      if (jsonBranch.isActive()
        && !schema.getNoLanguageFields().contains(jsonBranch.getLabel())) {
        headers.add("lang:" + jsonBranch.getLabel());
      }
    }
    return headers;
  }

  @Override
  public void measure(JsonPathCache cache)
      throws InvalidJsonException {

    languageMap = new FieldCounter<>();
    rawLanguageMap = new LinkedHashMap<>();
    if (schema.getCollectionPaths().isEmpty()) {
      for (JsonBranch jsonBranch : schema.getPaths()) {
        if (jsonBranch.isActive()
          && !schema.getNoLanguageFields().contains(jsonBranch.getLabel())) {
          extractLanguageTags(null, jsonBranch, jsonBranch.getJsonPath(), cache, languageMap, rawLanguageMap);
        }
      }
    } else {
      for (JsonBranch collection : schema.getCollectionPaths()) {
        Object rawJsonFragment = cache.getFragment(collection.getJsonPath());
        if (rawJsonFragment == null) {
          for (JsonBranch child : collection.getChildren()) {
            if (child.isActive() && !schema.getNoLanguageFields().contains(child.getLabel())) {
              Map<String, BasicCounter> languages = new TreeMap<>();
              increase(languages, "_1");
              updateMaps(child.getLabel(), transformLanguages(languages));
            }
          }
        } else {
          List<Object> jsonFragments = Converter.jsonObjectToList(rawJsonFragment);
          for (int i = 0, len = jsonFragments.size(); i < len; i++) {
            Object jsonFragment = jsonFragments.get(i);
            for (JsonBranch child : collection.getChildren()) {
              if (child.isActive() && !schema.getNoLanguageFields().contains(child.getLabel())) {
                String address = String.format("%s/%d/%s",
                  collection.getJsonPath(), i, child.getJsonPath());
                extractLanguageTags(jsonFragment, child, address, cache, languageMap, rawLanguageMap);
              }
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
      JsonPathCache cache,
      FieldCounter<String> languageMap,
      Map<String, SortedMap<String, Integer>> rawLanguageMap) {
    List<EdmFieldInstance> values = cache.get(address, jsonBranch.getJsonPath(), jsonFragment);
    Map<String, BasicCounter> languages = new TreeMap<>();
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
    updateMaps(jsonBranch.getLabel(), transformLanguages(languages));
  }

  private void updateMaps(String label, SortedMap<String, Integer> instance) {
    if (!rawLanguageMap.containsKey(label)) {
      rawLanguageMap.put(label, instance);
    } else {
      Map<String, Integer> existing = rawLanguageMap.get(label);
      for (String key : instance.keySet()) {
        if (!existing.containsKey(key)) {
          existing.put(key, instance.get(key));
        } else {
          if (key != "_1") {
            existing.put(key, existing.get(key) + instance.get(key));
          }
        }
      }
    }
    languageMap.put(label, extractLanguagesFromRaw(rawLanguageMap.get(label)));
  }

  private void increase(Map<String, BasicCounter> languages, String key) {
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
      result += lang + ":" + ((Double) languages.get(lang).getTotal()).intValue();
    }
    return result;
  }

  private SortedMap<String, Integer> transformLanguages(Map<String, BasicCounter> languages) {
    SortedMap<String, Integer> result = new TreeMap<>();
    for (String lang : languages.keySet()) {
      result.put(lang, ((Double) languages.get(lang).getTotal()).intValue());
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
  public String getCsv(boolean withLabel, CompressionLevel compressionLevel) {
    return languageMap.getList(withLabel, compressionLevel.ZERO);
  }

}
