package de.gwdg.metadataqa.api.counter;

import de.gwdg.metadataqa.api.model.Category;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class CompletenessCounter {

  public static final String TOTAL = "TOTAL";
  private Map<String, BasicCounter> basicCounters;

  public CompletenessCounter() {
    initialize();
  }

  public BasicCounter get(String key) {
    return basicCounters.get(key);
  }

  public FieldCounter<Double> getFieldCounter() {
    FieldCounter<Double> fieldCounter = new FieldCounter<>();
    for (Map.Entry<String, BasicCounter> counter : basicCounters.entrySet()) {
      counter.getValue().calculate();
      fieldCounter.put(counter.getKey(), counter.getValue().getResult());
    }
    return fieldCounter;
  }

  public void calculateResults() {
    for (BasicCounter counter : basicCounters.values()) {
      counter.calculate();
    }
  }

  public void increaseInstance(List<Category> categories) {
    basicCounters.get(TOTAL).increaseInstance();
    for (Category category : categories) {
      basicCounters.get(category.name()).increaseInstance();
    }
  }

  public void increaseInstance(Category category, boolean increase) {
    basicCounters.get(category.name()).increaseTotal();
    if (increase) {
      basicCounters.get(category.name()).increaseInstance();
    }
  }

  public void increaseTotal(List<Category> categories) {
    basicCounters.get(TOTAL).increaseTotal();
    for (Category category : categories) {
      basicCounters.get(category.name()).increaseTotal();
    }
  }

  private void initialize() {
    basicCounters = new LinkedHashMap<>();
    for (String name : getHeaders()) {
      basicCounters.put(name, new BasicCounter());
    }
  }

  public static List<String> getHeaders() {
    List<String> headers = new ArrayList<>();
    headers.add(TOTAL);
    for (Category category : Category.values()) {
      headers.add(category.name());
    }
    return headers;
  }

  public BasicCounter getStatComponent(Category category) {
    return basicCounters.get(category.name());
  }

}
