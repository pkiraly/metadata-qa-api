package de.gwdg.metadataqa.api.similarity;

import java.util.HashMap;
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
		System.err.println(formatDistances());
		System.err.println("other: " + other);
		System.err.println("has it?: " + distances.containsKey(other.value));
		return distances.get(other.value);
	}

	public String formatDistances() {
		String text = "";
		for (String t : distances.keySet()) {
			if (text != "")
				text += ", ";
			text += String.format("%s=%f", t, distances.get(t));
		}
		return text;
	}

	@Override
	public String toString() {
		return "Term{" +
			"value='" + value + '\'' +
			'}';
	}
}
