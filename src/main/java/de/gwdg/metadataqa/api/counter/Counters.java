package de.gwdg.metadataqa.api.counter;

import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.util.CompressionLevel;
import de.gwdg.metadataqa.api.util.Converter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class Counters {

	private static final String TOTAL = "TOTAL";
	private String recordId;
	private Map<String, Object> fields = new LinkedHashMap<>();
	private Map<String, Double> tfIdfList;
	private Map<String, Double> problemList;
	private Map<String, String> languageMap = new LinkedHashMap<>();
	private Map<String, String> results = new LinkedHashMap<>();
	private Map<String, Double> resultsDouble = new LinkedHashMap<>();

	private boolean returnFieldExistenceList = false;
	private boolean returnFieldInstanceList = false;
	private boolean returnTfIdf = false;
	private boolean returnProblems = false;
	private boolean returnLanguage = false;

	public Counters() {
	}

	public void addResult(Map<String, String> result) {
		results.putAll(result);
	}

	public void addResultDouble(Map<String, Double> result) {
		resultsDouble.putAll(result);
	}

	public Map<String, String> getResults() {
		return results;
	}

	public Map<String, Double> getResultsDouble() {
		return resultsDouble;
	}

	/*
	public Map<String, Double> getResults() {
		// calculateResults();
		Map<String, Double> result = new HashMap<>();
		for (Map.Entry<String, BasicCounter> entry : basicCounters.entrySet()) {
			result.put(entry.getKey(), entry.getValue().getResult());
		}

		if (returnTfIdf == true) {
			result.putAll(tfIdfList);
		}

		if (returnProblems == true)
			result.putAll(problemList);

		return result;
	}
	*/

	public List<String> getResultsAsList() {
		return getResultsAsList(true);
	}

	public List<String> getResultsAsList(boolean withLabel) {
		return getResultsAsList(withLabel, CompressionLevel.ZERO);
	}

	public List<String> getResultsAsList(boolean withLabel, CompressionLevel compressionLevel) {
		Map<String, Double> results = getResultsDouble();
		List<String> items = new ArrayList<>();
		addResultItem(withLabel, items, TOTAL, results.get(TOTAL), compressionLevel);
		for (JsonBranch.Category category : JsonBranch.Category.values()) {
			addResultItem(withLabel, items, category.name(), results.get(category.name()), compressionLevel);
		}
		return items;
	}

	private void addResultItem(boolean withLabel, List<String> items, String key, Double value, CompressionLevel compressionLevel) {
		String valueAsString = String.format("%f", value);
		if (compressionLevel != CompressionLevel.ZERO) {
			valueAsString = Converter.compressNumber(valueAsString, compressionLevel);
		}

		if (withLabel) {
			items.add(String.format("\"%s\":%s", key, valueAsString));
		} else {
			items.add(valueAsString);
		}
	}

	public String getResultsAsTSV(boolean withLabel) {
		return StringUtils.join(getResultsAsList(withLabel), "\t");
	}

	public String getResultsAsCSV(boolean withLabel) {
		return getResultsAsCSV(withLabel, CompressionLevel.ZERO);
	}

	public String getResultsAsCSV(boolean withLabel, CompressionLevel compressionLevel) {
		return StringUtils.join(getResultsAsList(withLabel, compressionLevel), ",");
	}

	/*
	public void printResults() {
		// calculateResults();
		for (Map.Entry<String, BasicCounter> entry : basicCounters.entrySet()) {
			System.err.println(entry.getKey() + ": " + entry.getValue().getResult());
		}
	}
	*/

	public void setTfIdfList(Map<String, Double> tdIdf) {
		this.tfIdfList = tdIdf;
	}

	public String getTfIdfList(boolean withLabel) {
		List<String> items = new ArrayList<>();
		for (Map.Entry<String, Double> entry : tfIdfList.entrySet()) {
			String item = "";
			if (withLabel) {
				item += String.format("\"%s\":", entry.getKey());
			}
			item += String.format("%.8f", entry.getValue());
			items.add(item);
		}
		return StringUtils.join(items, ',');
	}

	public void setProblemList(Map<String, Double> problemList) {
		this.problemList = problemList;
	}

	public String getProblemList(boolean withLabel, CompressionLevel compressionLevel) {
		List<String> items = new ArrayList<>();
		for (Map.Entry<String, Double> entry : problemList.entrySet()) {
			String item = "";
			if (withLabel) {
				item += String.format("\"%s\":", entry.getKey());
			}
			String nr = String.format("%.8f", entry.getValue());
			if (compressionLevel != CompressionLevel.ZERO) {
				nr = Converter.compressNumber(nr, compressionLevel);
			}
			item += nr;
			items.add(item);
		}
		return StringUtils.join(items, ',');
	}

	public String getLanguageMap(boolean withLabel) {
		List<String> items = new ArrayList<>();
		if (withLabel == true) {
			for (Map.Entry<String, String> entry : languageMap.entrySet()) {
				String item = withLabel
					? String.format("\"%s\":\"%s\"", entry.getKey(), entry.getValue())
					: entry.getValue();
				items.add(item);
			}
		} else {
			items = (List)languageMap.values();
		}
		return StringUtils.join(items, ",");
	}

	public void setLanguageMap(Map<String, String> languageMap) {
		this.languageMap = languageMap;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public void setField(String key, Object value) {
		fields.put(key, value);
	}

	public Object getField(String key) {
		return fields.get(key);
	}

	/**
	 * Returns the scores as a CSV string (without any label)
	 * @return Comma separated scores
	*/
	public String getFullResults() {
		return getFullResults(false, CompressionLevel.ZERO);
	}

	/**
	 * Returns the scores are CSV
	 * @param withLabel
	 *    Return labels
	 * @return Comma separated scores
	*/
	public String getFullResults(boolean withLabel) {
		return getFullResults(withLabel, CompressionLevel.ZERO);
	}

	public String getFullResults(boolean withLabel, CompressionLevel compressionLevel) {
		String result = "";
		if (StringUtils.isNotBlank((String)fields.get("datasetCode"))
			&& StringUtils.isNotBlank((String)fields.get("dataProviderCode"))) {
			result += String.format("%s,%s,",
				fields.get("datasetCode"), fields.get("dataProviderCode"));
		}
		if (StringUtils.isNotBlank(recordId)) {
			result += String.format("%s,", recordId);
		}
		result += getResultsAsCSV(withLabel, compressionLevel);
		/*
		if (returnFieldExistenceList == true) {
			result += ',' + getExistenceList(withLabel);
		}
		*/
		/*
		if (returnFieldInstanceList == true) {
			result += ',' + getInstanceList(withLabel);
		}
		*/
		if (returnTfIdf == true) {
			result += ',' + getTfIdfList(withLabel);
		}
		if (returnProblems == true) {
			result += ',' + getProblemList(withLabel, compressionLevel);
		}
		if (returnLanguage == true) {
			result += ',' + getLanguageMap(withLabel);
		}
		return result;
	}

	public void returnFieldExistenceList(boolean returnFieldExistenceList) {
		this.returnFieldExistenceList = returnFieldExistenceList;
	}

	public void returnFieldInstanceList(boolean returnFieldInstanceList) {
		this.returnFieldInstanceList = returnFieldInstanceList;
	}

	public void returnTfIdfList(boolean returnTfIdf) {
		this.returnTfIdf = returnTfIdf;
	}

	public void returnProblemList(boolean returnProblems) {
		this.returnProblems = returnProblems;
	}

	public void returnLanguage(boolean returnLanguage) {
		this.returnLanguage = returnLanguage;
	}
}
