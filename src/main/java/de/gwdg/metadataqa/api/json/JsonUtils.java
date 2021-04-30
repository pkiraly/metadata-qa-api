package de.gwdg.metadataqa.api.json;

import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import com.jayway.jsonpath.JsonPath;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import net.minidev.json.JSONArray;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public final class JsonUtils {

  private static final Logger LOGGER = Logger.getLogger(JsonUtils.class.getCanonicalName());

  private JsonUtils() {
    throw new AssertionError();
  }

  public static Object extractField(Object document, String jsonPath) {
    return JsonPath.read(document, jsonPath);
  }

  public static List<String> extractList(Object value) {
    List<String> extracted = new ArrayList<>();
    if (value.getClass() == String.class) {
      extracted.add((String) value);
    } else if (value.getClass() == JSONArray.class) {
      JSONArray array1 = (JSONArray) value;
      for (int i = 0, l = array1.size(); i < l; i++) {
        if (array1.get(i).getClass() == JSONArray.class) {
          JSONArray array2 = (JSONArray) array1.get(i);
          for (int j = 0, l2 = array2.size(); j < l2; j++) {
            if (array2.get(j).getClass() == String.class) {
              extracted.add((String) array2.get(j));
            } else if (array2.get(j).getClass() == LinkedHashMap.class) {
              Map<String, String> map = (LinkedHashMap<String, String>) array2.get(j);
              if (map.containsKey("@about")) {
                extracted.add(map.get("@about"));
              } else if (map.containsKey("@resource")) {
                extracted.add(map.get("@resource"));
              } else if (map.containsKey("#value")) {
                extracted.add(map.get("#value"));
              } else {
                LOGGER.severe("Other type of map*: " + map.keySet());
              }
            } else {
              LOGGER.severe("Unhandled array2 type: " + getType(array2.get(j)));
            }
          }
        } else if (array1.get(i).getClass() == String.class) {
          extracted.add((String) array1.get(i));
        } else {
          LOGGER.severe("Unhandled array1 type: " + getType(array1.get(i)));
        }
      }
    } else if (value.getClass() == LinkedHashMap.class) {
      for (Object innerValue : ((Map)value).values()) {
        extracted.addAll(extractList(innerValue));
      }
    } else {
      LOGGER.severe("Unhandled object type: " + getType(value));
    }
    return extracted;
  }

  public static List<? extends XmlFieldInstance> extractFieldInstanceList(Object value,
                                                                          String recordId,
                                                                          String jsonPath) {
    List<EdmFieldInstance> extracted = new ArrayList<>();
    if (value.getClass() == String.class) {
      extracted.add(new EdmFieldInstance((String) value));
    } else if (value.getClass() == JSONArray.class) {
      JSONArray outerArray = (JSONArray) value;
      if (outerArray.isEmpty()) {
        return null;
      }

      for (int i = 0, l = outerArray.size(); i < l; i++) {
        Object outerVal = outerArray.get(i);
        if (outerVal == null) {
          continue;
        }

        if (outerVal.getClass() == String.class) {
          extracted.add(new EdmFieldInstance((String) outerVal));
        } else if (outerVal.getClass() == Boolean.class) {
          extracted.add(new EdmFieldInstance(Boolean.toString((Boolean) outerVal)));
        } else if (outerVal.getClass() == Double.class) {
          extracted.add(new EdmFieldInstance(Double.toString((Double) outerVal)));
        } else if (outerVal.getClass() == Long.class) {
          extracted.add(new EdmFieldInstance(Long.toString((Long) outerVal)));
        } else if (outerVal.getClass() == BigDecimal.class) {
          extracted.add(new EdmFieldInstance(((BigDecimal) outerVal).toString()));
        } else if (outerVal.getClass() == BigInteger.class) {
          extracted.add(new EdmFieldInstance(((BigInteger) outerVal).toString()));
        } else if (outerVal.getClass() == JSONArray.class) {
          extracted.addAll(extractInnerArray(outerVal, recordId, jsonPath));
        } else if (outerVal.getClass() == LinkedHashMap.class) {
          extracted.add(hashToFieldInstance(outerVal, recordId, jsonPath));
        } else {
          LOGGER.severe(String.format(
                "Unhandled outerArray type: %s, %s [record ID: %s, path: %s]",
                getType(outerVal), outerVal, recordId, jsonPath
          ));
        }
      }
    } else if (value.getClass() == LinkedHashMap.class) {
      extracted.add(hashToFieldInstance(value, recordId, jsonPath));
    } else if (value.getClass() == Integer.class) {
      extracted.add(new EdmFieldInstance(Integer.toString((int) value)));
    } else if (value.getClass() == Float.class) {
      extracted.add(new EdmFieldInstance(Float.toString((float) value)));
    } else if (value.getClass() == Double.class) {
      extracted.add(new EdmFieldInstance(Double.toString((double) value)));
    } else if (value.getClass() == Boolean.class) {
      extracted.add(new EdmFieldInstance(Boolean.toString((boolean) value)));
    } else if (value.getClass() == java.math.BigDecimal.class) {
      extracted.add(new EdmFieldInstance(((BigDecimal) value).toString()));
    } else if (value.getClass() == java.math.BigInteger.class) {
      extracted.add(new EdmFieldInstance(((BigInteger) value).toString()));
    } else {
      LOGGER.severe(String.format(
            "Unhandled object type: %s, [record ID: %s, path: %s]",
            getType(value), recordId, jsonPath
      ));
    }
    return extracted;
  }

  private static List<EdmFieldInstance> extractInnerArray(Object outerVal, String recordId, String jsonPath) {
    List<EdmFieldInstance> extracted = new ArrayList<>();
    JSONArray array = (JSONArray) outerVal;
    for (int j = 0, l2 = array.size(); j < l2; j++) {
      Object innerVal = array.get(j);
      if (innerVal.getClass() == String.class) {
        extracted.add(new EdmFieldInstance((String) innerVal));
      } else if (innerVal.getClass() == LinkedHashMap.class) {
        extracted.add(hashToFieldInstance(innerVal, recordId, jsonPath));
      } else if (innerVal.getClass() == JSONArray.class) {
        extracted.addAll(extractInnerArray(innerVal, recordId, jsonPath));
      } else {
        LOGGER.severe(String.format(
              "Unhandled inner array type: %s, [record ID: %s, path: %s]",
              getType(array.get(j)), recordId, jsonPath
        ));
      }
    }
    return extracted;
  }

  public static EdmFieldInstance hashToFieldInstance(Object innerVal, String recordId, String jsonPath)  {
    Map<String, Object> map = (LinkedHashMap<String, Object>) innerVal;
    EdmFieldInstance instance = new EdmFieldInstance();
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      Object value = entry.getValue();
      if (entry.getKey().equals("@about")) {
        instance.setResource((String) value);
      } else if (entry.getKey().equals("@resource")) {
        instance.setResource((String) value);
      } else if (entry.getKey().equals("#value")) {
        instance.setValue((String) value);
      } else if (entry.getKey().equals("def")) {
        if (value instanceof JSONArray) {
          JSONArray values = (JSONArray) value;
          if (values.size() > 1) {
            LOGGER.severe(String.format(
              "Multiple values in a 'def' value: %s, [record ID: %s, path: %s]",
              values, recordId, jsonPath
            ));
          } else {
            instance.setValue(values.get(0).toString());
          }
        } else if (value instanceof String) {
          instance.setValue(value.toString());
        } else {
          LOGGER.severe(String.format(
            "Unhandled type in a 'def' value: %s, [record ID: %s, path: %s]",
            value, recordId, jsonPath
          ));
        }
        // instance.setValue(map.get("def"));
      } else if (entry.getKey().equals("@lang")) {
        instance.setLanguage((String) value);
      } else {
        LOGGER.severe(String.format(
              "Other type (%s) of map: %s, [record ID: %s, path: %s]",
          entry.getKey(), map, recordId, jsonPath
        ));
      }
    }
    return instance;
  }

  public static String extractString(Object value) {
    String extracted = null;
    if (value.getClass() == String.class) {
      extracted = (String) value;
    } else if (value.getClass() == LinkedHashMap.class) {
      Map<String, Object> map = (LinkedHashMap<String, Object>) value;
      extracted = extractString(map.values().toArray()[0]);
    } else if (value.getClass() == JSONArray.class) {
      extracted = ((JSONArray) value).get(0).toString();
    } else {
      LOGGER.severe("Unhandled object type: " + getType(value));
    }
    return extracted;
  }

  public static String getType(Object obj) {
    return obj.getClass().getCanonicalName();
  }
}
