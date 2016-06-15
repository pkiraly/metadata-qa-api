package de.gwdg.metadataqa.api.counter;

import de.gwdg.metadataqa.api.util.Converter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class FieldCounter<T> {

	private final Map<String, T> fieldMap;

	public FieldCounter() {
		fieldMap = new LinkedHashMap<>();
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

	public Map<String, T> getMap() {
		return fieldMap;
	}

	public String getList(boolean withLabel) {
		return getList(withLabel, false);
	}

	public String getList(boolean withLabel, boolean compressed) {
		List<String> items = new ArrayList<>();
		for (Map.Entry<String, T> entry : fieldMap.entrySet()) {
			String item = "";
			if (withLabel) {
				item += String.format("\"%s\":", entry.getKey());
			}
			String value = Converter.asString(entry.getValue());
			if (compressed == true)
				value = Converter.compressNumber(value);
			item += value;
			items.add(item);
		}
		return StringUtils.join(items, ',');
	}

	public List<Integer> getList() {
		List<Integer> values = new LinkedList<>();
		for (T value : fieldMap.values()) {
			values.add(Converter.asInteger(value));
		}
		return values;
	}

}
