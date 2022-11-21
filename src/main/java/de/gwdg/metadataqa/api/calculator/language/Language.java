package de.gwdg.metadataqa.api.calculator.language;

import de.gwdg.metadataqa.api.counter.BasicCounter;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.Converter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Language {
  private final PathCache cache;
  private final Schema schema;
  FieldCounter<String> languageMap;
  Map<String, SortedMap<String, Integer>> rawLanguageMap;

  public Language(Schema schema, PathCache cache) {
    this.schema = schema;
    this.cache = cache;
  }

  public FieldCounter<String> measure() {
    languageMap = new FieldCounter<>();
    rawLanguageMap = new LinkedHashMap<>();
    if (schema.getCollectionPaths().isEmpty()) {
      for (DataElement dataElement : schema.getPaths()) {
        if (dataElement.isActive()
          && !schema.getNoLanguageFields().contains(dataElement.getLabel())) {
          extractLanguageTags(null, dataElement, dataElement.getPath());
        }
      }
    } else {
      for (DataElement collection : schema.getCollectionPaths()) {
        Object rawJsonFragment = cache.getFragment(collection.getPath());
        if (rawJsonFragment == null) {
          for (DataElement child : collection.getChildren()) {
            if (child.isActive() && !schema.getNoLanguageFields().contains(child.getLabel())) {
              Map<String, BasicCounter> languages = new TreeMap<>();
              increase(languages, "_1");
              updateMaps(child.getLabel(), transformLanguages(languages));
            }
          }
        } else {
          List<Object> jsonFragments = Converter.jsonObjectToList(rawJsonFragment, schema);
          for (int i = 0, len = jsonFragments.size(); i < len; i++) {
            Object jsonFragment = jsonFragments.get(i);
            for (DataElement child : collection.getChildren()) {
              if (child.isActive() && !schema.getNoLanguageFields().contains(child.getLabel())) {
                var address = String.format("%s/%d/%s",
                  collection.getPath(), i, child.getPath());
                extractLanguageTags(jsonFragment, child, address);
              }
            }
          }
        }
      }
    }
    return languageMap;
  }

  private void extractLanguageTags(Object jsonFragment,
                                   DataElement dataElement,
                                   String address) {
    List<EdmFieldInstance> values = cache.get(address, dataElement.getPath(), jsonFragment);
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
    updateMaps(dataElement.getLabel(), transformLanguages(languages));
  }

  private void increase(Map<String, BasicCounter> languages, String key) {
    if (!languages.containsKey(key)) {
      languages.put(key, new BasicCounter(1));
    } else {
      languages.get(key).increaseTotal();
    }
  }

  private void updateMaps(String label,
                          SortedMap<String, Integer> instance) {
    if (!rawLanguageMap.containsKey(label)) {
      rawLanguageMap.put(label, instance);
    } else {
      Map<String, Integer> existing = rawLanguageMap.get(label);
      for (Map.Entry<String, Integer> entry : instance.entrySet()) {
        if (!existing.containsKey(entry.getKey())) {
          existing.put(entry.getKey(), entry.getValue());
        } else {
          if (entry.getKey() != null && !entry.getKey().equals("_1")) {
            existing.put(entry.getKey(), existing.get(entry.getKey()) + entry.getValue());
          }
        }
      }
    }
    languageMap.put(label, extractLanguagesFromRaw(rawLanguageMap.get(label)));
  }

  protected String extractLanguagesFromRaw(Map<String, Integer> languages) {
    var result = new StringBuilder();
    for (Map.Entry<String, Integer> lang : languages.entrySet()) {
      if (result.length() > 0)
        result.append(";");
      result.append(lang.getKey() + ":" + lang.getValue());
    }
    return result.toString();
  }

  private SortedMap<String, Integer> transformLanguages(Map<String, BasicCounter> languages) {
    SortedMap<String, Integer> result = new TreeMap<>();
    for (Map.Entry<String, BasicCounter> lang : languages.entrySet()) {
      result.put(lang.getKey(), ((Double) lang.getValue().getTotal()).intValue());
    }
    return result;
  }
}
