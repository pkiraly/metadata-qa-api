package de.gwdg.metadataqa.api.calculator;

import com.jayway.jsonpath.InvalidJsonException;
import de.gwdg.metadataqa.api.counter.CompletenessCounter;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.CompressionLevel;
import de.gwdg.metadataqa.api.util.Converter;
import de.gwdg.metadataqa.api.util.SkippedEntitySelector;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import static java.util.stream.Collectors.toList;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 * @param <T>
 */
public class CompletenessCalculator<T extends XmlFieldInstance>
      implements Calculator, Serializable {

  public static final String CALCULATOR_NAME = "completeness";

  private static final Logger LOGGER = Logger.getLogger(
    CompletenessCalculator.class.getCanonicalName()
  );

  private String inputFileName;

  private CompletenessCounter completenessCounter;
  private FieldCounter<Boolean> existenceCounter;
  private FieldCounter<Integer> cardinalityCounter;

  // private Counters counters;
  private List<String> missingFields;
  private List<String> emptyFields;
  private List<String> existingFields;
  private Schema schema;

  private boolean collectFields = false;
  private boolean completeness = true;
  private boolean existence = true;
  private boolean cardinality = true;
  private SkippedEntryChecker skippedEntryChecker = null;
  private SkippedEntitySelector skippedEntitySelector = new SkippedEntitySelector();

  public CompletenessCalculator() {
    // this.recordID = null;
  }

  public CompletenessCalculator(Schema schema) {
    this.schema = schema;
  }

  @Override
  public String getCalculatorName() {
    return CALCULATOR_NAME;
  }

  @Override
  public void measure(PathCache cache)
      throws InvalidJsonException {
    initializeCounters();

    List<String> skippableIds = skippedEntryChecker != null
          ? skippedEntryChecker.getSkippableCollectionIds(cache)
          : new ArrayList<>();

    if (schema.getCollectionPaths() == null || schema.getCollectionPaths().isEmpty()) {
      for (JsonBranch jsonBranch : schema.getPaths()) {
        if (!jsonBranch.isActive()) {
          continue;
        }
        evaluateJsonBranch(jsonBranch, cache, completenessCounter,
              jsonBranch.getLabel(), null);
      }
    } else {
      for (JsonBranch collection : schema.getCollectionPaths()) {
        if (!collection.isActive()) {
          continue;
        }
        Object rawJsonFragment = cache.getFragment(collection.getJsonPath());
        List<Object> jsonFragments = Converter.jsonObjectToList(rawJsonFragment, schema);
        if (jsonFragments.isEmpty()) {
          handleEmptyFragment(collection);
        } else {
          for (int i = 0, len = jsonFragments.size(); i < len; i++) {
            Object jsonFragment = jsonFragments.get(i);
            if (skippedEntitySelector.isCollectionSkippable(skippableIds, collection, i, cache, jsonFragment)) {
              handleEmptyFragment(collection);
            } else {
              for (JsonBranch child : collection.getChildren()) {
                if (!child.isActive()) {
                  continue;
                }
                var address = String.format("%s/%d/%s", collection.getJsonPath(), i, child.getJsonPath());
                evaluateJsonBranch(child, cache, completenessCounter, address, jsonFragment);
              }
            }
          }
        }
      }
    }

    if (schema.getFieldGroups() != null) {
      for (FieldGroup fieldGroup : schema.getFieldGroups()) {
        var existing = false;
        for (String field : fieldGroup.getFields()) {
          if (Boolean.TRUE.equals(existenceCounter.get(field))) {
            existing = true;
            break;
          }
        }
        completenessCounter.increaseInstance(fieldGroup.getCategory(), existing);
      }
    }
  }

  public void initializeCounters() {
    completenessCounter = new CompletenessCounter(schema);
    existenceCounter = new FieldCounter<>();
    cardinalityCounter = new FieldCounter<>();
    if (collectFields) {
      missingFields = new ArrayList<>();
      emptyFields = new ArrayList<>();
      existingFields = new ArrayList<>();
    }
  }

  public void handleEmptyFragment(JsonBranch collection) {
    for (JsonBranch child : collection.getChildren()) {
      if (!child.isActive()) {
        continue;
      }
      handleValues(completenessCounter, child, null);
    }
  }

  /*
  private boolean isCollectionSkippable(
      List<String> skippableIds,
      JsonBranch collection,
      int i,
      JsonPathCache cache,
      Object jsonFragment)
  {
    var skippable = false;
    JsonBranch identifierPath = collection.getIdentifier();
    if (!skippableIds.isEmpty() && identifierPath != null) {
      String address = String.format("%s/%d/%s",
        collection.getJsonPath(), i, identifierPath.getJsonPath());
      List<T> values = cache.get(address, identifierPath.getJsonPath(), jsonFragment);
      String id = (skippedEntryChecker != null)
            ? skippedEntryChecker.extractId(values.get(0))
            : values.get(0).getValue();
      skippable = skippableIds.contains(id);
    }
    return skippable;
  }
  */

  public void evaluateJsonBranch(JsonBranch jsonBranch,
                                 PathCache cache,
                                 CompletenessCounter completenessCounter,
                                 String address,
                                 Object jsonFragment) {
    List<T> values = cache.get(address, jsonBranch.getJsonPath(), jsonFragment);
    handleValues(completenessCounter, jsonBranch, values);
  }

  private void handleValues(CompletenessCounter completenessCounter,
                            JsonBranch jsonBranch,
                            List<T> values) {
    if (completeness) {
      completenessCounter.increaseTotal(jsonBranch.getCategories());
    }

    if (values != null && !values.isEmpty()) {
      handleNonNullValues(completenessCounter, jsonBranch, values);
    } else {
      handleNullValues(jsonBranch);
    }
  }

  private void handleNonNullValues(CompletenessCounter completenessCounter,
                                   JsonBranch jsonBranch,
                                   List<T> values) {
    final String label = jsonBranch.getLabel();

    if (completeness) {
      completenessCounter.increaseInstance(jsonBranch.getCategories());
    }

    if (existence) {
      existenceCounter.put(label, true);
    }

    if (cardinality) {
      if (!cardinalityCounter.has(label)) {
        cardinalityCounter.put(label, values.size());
      } else {
        cardinalityCounter.put(label, values.size() + cardinalityCounter.get(label));
      }
    }

    if (collectFields) {
      existingFields.add(label);
    }
  }

  private void handleNullValues(JsonBranch jsonBranch) {
    if (existence && !existenceCounter.has(jsonBranch.getLabel()))
      existenceCounter.put(jsonBranch.getLabel(), false);

    if (cardinality && !cardinalityCounter.has(jsonBranch.getLabel()))
      cardinalityCounter.put(jsonBranch.getLabel(), 0);

    if (collectFields && !missingFields.contains(jsonBranch.getLabel()))
      missingFields.add(jsonBranch.getLabel());
  }

  public void collectFields(boolean collectFields) {
    this.collectFields = collectFields;
  }

  public List<String> getMissingFields() {
    return missingFields;
  }

  public List<String> getEmptyFields() {
    return emptyFields;
  }

  public List<String> getExistingFields() {
    return existingFields;
  }

  public void setInputFileName(String inputFileName) {
    this.inputFileName = inputFileName;
  }

  public String getInputFileName() {
    return inputFileName;
  }

  @Override
  public Map<String, ? extends Object> getResultMap() {
    Map<String, Object> resultMap = new LinkedHashMap<>();

    if (completeness) {
      resultMap.putAll(completenessCounter.getFieldCounter().getMap());
    }

    if (existence) {
      for (Entry<String, Boolean> e : existenceCounter.getMap().entrySet()) {
        resultMap.put(
          "existence:" + e.getKey(),
          BooleanUtils.toInteger(e.getValue()));
      }
    }

    if (cardinality) {
      for (Entry<String, Integer> e : cardinalityCounter.getMap().entrySet()) {
        resultMap.put("cardinality:" + e.getKey(), e.getValue());
      }
    }

    return resultMap;
  }

  @Override
  public Map<String, Map<String, ? extends Object>> getLabelledResultMap() {
    Map<String, Map<String, ? extends Object>> resultMap = new LinkedHashMap<>();
    if (completeness) {
      resultMap.put("completeness", completenessCounter.getFieldCounter().getMap());
    }
    if (existence) {
      resultMap.put("existence", existenceCounter.getMap());
    }
    if (cardinality) {
      resultMap.put("cardinality", cardinalityCounter.getMap());
    }
    return resultMap;
  }

  @Override
  public String getCsv(boolean withLabel, CompressionLevel compressionLevel) {
    List<String> csvs = getList(withLabel, compressionLevel);
    return StringUtils.join(csvs, ",");
  }

  @Override
  public List<Object> getCsv() {
    List<Object> csvs = new ArrayList<>();
    if (completeness) {
      csvs.addAll(completenessCounter.getFieldCounter().getCsv());
    }

    if (existence) {
      csvs.addAll(existenceCounter.getCsv().stream()
        .map(v -> BooleanUtils.toInteger((boolean)v))
        .collect(toList()));
    }

    if (cardinality) {
      csvs.addAll(cardinalityCounter.getCsv());
    }

    return csvs;
  }


  @Override
  public List<String> getList(boolean withLabel, CompressionLevel compressionLevel) {
    List<String> csvs = new ArrayList<>();
    if (completeness) {
      csvs.addAll(completenessCounter.getFieldCounter().getList(withLabel, compressionLevel));
    }

    if (existence) {
      csvs.addAll(existenceCounter.getList(withLabel, compressionLevel));
    }

    if (cardinality) {
      csvs.addAll(cardinalityCounter.getList(withLabel, compressionLevel));
    }

    return csvs;
  }

  @Override
  public List<String> getHeader() {
    List<String> headers = new ArrayList<>();

    if (completeness) {
      for (String name : CompletenessCounter.getHeaders(schema)) {
        headers.add("completeness:" + name);
      }
    }

    if (existence) {
      for (JsonBranch jsonBranch : schema.getPaths()) {
        if (!jsonBranch.isCollection() && jsonBranch.isActive()) {
          headers.add("existence:" + jsonBranch.getLabel());
        }
      }
    }

    if (cardinality) {
      for (JsonBranch jsonBranch : schema.getPaths()) {
        if (!jsonBranch.isCollection() && jsonBranch.isActive()) {
          headers.add("cardinality:" + jsonBranch.getLabel());
        }
      }
    }

    return headers;
  }

  public Map<String, Boolean> getExistenceMap() {
    return existenceCounter.getMap();
  }

  public Map<String, Integer> getCardinalityMap() {
    return cardinalityCounter.getMap();
  }

  public CompletenessCounter getCompletenessCounter() {
    return completenessCounter;
  }

  public FieldCounter<Boolean> getExistenceCounter() {
    return existenceCounter;
  }

  public FieldCounter<Integer> getCardinalityCounter() {
    return cardinalityCounter;
  }

  public boolean isCompleteness() {
    return completeness;
  }

  public void setCompleteness(boolean completeness) {
    this.completeness = completeness;
  }

  public boolean isExistence() {
    return existence;
  }

  public void setExistence(boolean existence) {
    this.existence = existence;
  }

  public boolean isCardinality() {
    return cardinality;
  }

  public void setCardinality(boolean cardinality) {
    this.cardinality = cardinality;
  }

  public Schema getSchema() {
    return schema;
  }

  public SkippedEntryChecker getSkippedEntryChecker() {
    return skippedEntryChecker;
  }

  public void setSkippedEntryChecker(SkippedEntryChecker skippedEntryChecker) {
    this.skippedEntryChecker = skippedEntryChecker;
    skippedEntitySelector.setSkippedEntryChecker(skippedEntryChecker);
  }
}
