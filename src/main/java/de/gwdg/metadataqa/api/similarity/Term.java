package de.gwdg.metadataqa.api.similarity;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Term {
	String value;
	Map<String, Double> distances = new HashMap<>();

	public Term(String value) {
		this.value = value;
	}

	public void setDistance(Term other, double distance) {
		distances.put(other.value, distance);
	}

	public String getValue() {
		return value;
	}

	public double getDistance(Term other) {
		return distances.get(other.value);
	}

	public boolean hasDistance(Term other) {
		return distances.containsKey(other.value);
	}

	public String formatDistances() {
		List<String> formattedDistances = new ArrayList<>();
		for (String t : distances.keySet()) {
			formattedDistances.add(String.format("%s=%f", t, distances.get(t)));
		}
		return "{" + StringUtils.join(formattedDistances, ", ") + "}";
	}

	@Override
	public String toString() {
		return "Term{"
			+ "term='" + value + '\''
			+ ", distances=" + formatDistances()
			+ '}';
	}
}
