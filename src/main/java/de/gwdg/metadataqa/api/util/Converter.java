package de.gwdg.metadataqa.api.util;

import java.math.BigDecimal;
import java.security.InvalidParameterException;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class Converter {

	/**
	 * Transforms different objects (BigDecimal, Integer) to Double
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
	 * Transforms different objects (BigDecimal, Integer) to Integer
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
				intValue = ((Boolean) value) == true ? 1 : 0;
				break;
			case "java.lang.Integer":
				intValue = (Integer) value;
				break;
			default:
				intValue = (Integer) value;
				break;
		}
		return intValue;
	}

	public static String asString(Object value) {
		String text = "";
		if (value instanceof Boolean) {
			text = ((Boolean)value == true) ? "1" : "0";
		} else if (value instanceof Integer) {
			text = Integer.toString((Integer)value);
		} else if (value instanceof Double) {
			text = String.format("%.6f", (Double)value);
		} else if (value instanceof String) {
			text = (String)value;
		} else {
			throw new InvalidParameterException("Object has an unhandled type: " + value.getClass().getCanonicalName());
		}
		return text;
	}

	/**
	 * Removes the unnecessary 0-s from the end of a number
	 * For example 0.7000 becomes 0.7, 0.00000 becomes 0.0
	 * @param value A string representation of a number
	 * @return The "compressed" representation without zeros at the end
	 */
	public static String compressNumber(String value) {
		return value.replaceAll("([0-9])0+$", "$1").replaceAll("\\.0+$", ".0");
	}
}
