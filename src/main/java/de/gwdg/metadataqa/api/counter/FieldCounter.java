package de.gwdg.metadataqa.api.counter;

import de.gwdg.metadataqa.api.util.CompressionLevel;
import de.gwdg.metadataqa.api.util.Converter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.BooleanUtils;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 * @param <T> the type of elements held in this collection
 */
public class FieldCounter<T> {

  private final Map<String, T> fieldMap;

  public FieldCounter() {
    fieldMap = new LinkedHashMap<>();
  }

  public boolean has(String key) {
    return fieldMap.containsKey(key);
  }

  public T get(String key) {
    return fieldMap.get(key);
  }

  public void put(String key, T value) {
    fieldMap.put(key, value);
  }

  public int size() {
    return fieldMap.size();
  }

  public void putAll(FieldCounter<T> other) {
    fieldMap.putAll(other.getMap());
  }

  public Map<String, T> getMap() {
    return fieldMap;
  }

  public String getCsv(boolean withLabel) {
    return getCsv(withLabel, CompressionLevel.ZERO);
  }

  public String getCsv(boolean withLabel, CompressionLevel compressionLevel) {
    List<String> items = getList(withLabel, compressionLevel);
    return StringUtils.join(items, ',');
  }

  public List<String> getList(boolean withLabel, CompressionLevel compressionLevel) {
    List<String> items = new ArrayList<>();
    for (Map.Entry<String, T> entry : fieldMap.entrySet()) {
      String item = "";
      if (withLabel) {
        item += String.format("\"%s\":", entry.getKey());
      }
      String value = Converter.asString(entry.getValue());
      if (!(entry.getValue() instanceof Integer)
          && compressionLevel != CompressionLevel.ZERO) {
        value = Converter.compressNumber(value, compressionLevel);
      }
      item += value;
      items.add(item);
    }
    return items;
  }

  public List<T> getCsv() {
    List<T> values = new LinkedList<>();
    for (T value : fieldMap.values()) {
      values.add(value);
    }
    return values;
  }

}
