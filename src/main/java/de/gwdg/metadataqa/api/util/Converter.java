package de.gwdg.metadataqa.api.util;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import de.gwdg.metadataqa.api.rule.RuleCheckingOutput;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public abstract class Converter {

  private Converter() {
  }

  /**
   * Transforms different objects (BigDecimal, Integer) to Double.
   *
   * @param value
   *   The object to transform
   * @return
   *   The double value
   */
  public static Double asDouble(Object value) {
    double doubleValue;
    switch (value.getClass().getCanonicalName()) {
      case "java.math.BigDecimal":
        doubleValue = ((BigDecimal) value).doubleValue();
        break;
      case "java.lang.Integer":
        doubleValue = ((Integer) value).doubleValue();
        break;
      default:
        doubleValue = (Double) value;
        break;
    }
    return doubleValue;
  }

  /**
   * Transforms different objects (BigDecimal, Integer) to Integer.
   *
   * @param value
   *   The object to transform
   * @return
   *   The double value
   */
  public static Integer asInteger(Object value) {
    int intValue;
    switch (value.getClass().getCanonicalName()) {
      case "java.math.BigDecimal":
        intValue = ((BigDecimal) value).intValue();
        break;
      case "java.lang.Boolean":
        intValue = (Boolean) value ? 1 : 0;
        break;
      case "java.lang.Integer":
        intValue = (Integer) value;
        break;
      case "java.lang.Double":
        intValue = (Integer) ((Long) Math.round((Double) value)).intValue();
        break;
      case "java.lang.Long":
        intValue = (Integer) ((Long) value).intValue();
        break;
      case "java.lang.Float":
        intValue = (Integer) Math.round((float)value);
        break;
      case "java.lang.String":
        intValue = asInteger(Double.parseDouble((String)value));
        break;
      default:
        intValue = (Integer) value;
        break;
    }
    return intValue;
  }

  public static String asString(Object value) {
    String text = "";
    if (value == null) {
      text = "null";
    } else if (value instanceof Boolean) {
      text = (Boolean) value ? "1" : "0";
    } else if (value instanceof Integer) {
      text = Integer.toString((Integer) value);
    } else if (value instanceof Double) {
      text = String.format("%.6f", (Double) value);
    } else if (value instanceof String) {
      text = (String) value;
    } else if (value instanceof List) {
      text = StringUtils.join(((List) value), ", ");
    } else if (value instanceof RuleCheckingOutput) {
      text = ((RuleCheckingOutput)value).asString();
    } else if (value instanceof BigDecimal) {
      text = value.toString();
    } else {
      throw new IllegalArgumentException("Object has an unhandled type: " + value.getClass().getCanonicalName() + " " + value);
    }
    return text;
  }

  /**
   * Removes the unnecessary 0-s from the end of a number.
   * For example 0.7000 becomes 0.7, 0.00000 becomes 0.0.
   * @param value A string representation of a number
   * @param compressionLevel The level of compression
   * @return The "compressed" representation without zeros at the end
   */
  public static String compressNumber(String value, CompressionLevel compressionLevel) {
    value = value.replaceAll("([0-9])0+$", "$1");
    if (compressionLevel.equals(CompressionLevel.NORMAL)) {
      value = value.replaceAll("\\.0+$", ".0");
    } else if (compressionLevel.equals(CompressionLevel.WITHOUT_TRAILING_ZEROS)) {
      value = value.replaceAll("\\.0+$", "");
    }
    return value;
  }

  public static List<Object> jsonObjectToList(Object jsonFragment, Schema schema) {
    List<Object> list = null;
    if (schema.getFormat().equals(Format.JSON))
      list = Converter.jsonObjectToList(jsonFragment);
    else
      list = (List<Object>) jsonFragment;
    return list;
  }

  public static List<Object> jsonObjectToList(Object jsonFragment) {
    List<Object> list = new ArrayList<>();
    if (jsonFragment != null) {
      if (jsonFragment instanceof JSONArray) {
        List<Object> objects = Arrays.asList(((JSONArray) jsonFragment).toArray());
        if (!objects.isEmpty()) {
          list.addAll(objects);
        }
      } else if (jsonFragment instanceof LinkedHashMap) {
        if (!((LinkedHashMap) jsonFragment).isEmpty()) {
          list.add(jsonFragment);
        }
      } else {
        list.add(jsonFragment);
      }
    }
    return list;
  }
}
