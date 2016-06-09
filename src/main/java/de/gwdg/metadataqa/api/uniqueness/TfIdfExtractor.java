package de.gwdg.metadataqa.api.uniqueness;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JsonProvider;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import de.gwdg.metadataqa.api.schema.Schema;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class TfIdfExtractor {

	private static final JsonProvider jsonProvider = Configuration.defaultConfiguration().jsonProvider();
	private final Schema branches;

	public TfIdfExtractor(Schema branches) {
		this.branches = branches;
	}

	private Map<String, List<TfIdf>> termsCollection;

	public Map<String, Double> extract(String jsonString, String recordId) {
		return extract(jsonString, recordId, false);
	}

	public Map<String, Double> extract(String jsonString, String recordId, boolean doCollectTerms) {
		Map<String, Double> results = new LinkedHashMap<>();
		termsCollection = new LinkedHashMap<>();
		Object document = jsonProvider.parse(jsonString);
		String path = String.format("$.termVectors.['%s']", recordId);
		Map value = (LinkedHashMap) JsonPath.read(document, path);
		for (String field : branches.getSolrFields().keySet()) {
			if (doCollectTerms)
				termsCollection.put(field, new ArrayList<>());
			String solrField = branches.getSolrFields().get(field);
			double sum = 0;
			double count = 0;
			if (value.containsKey(solrField)) {
				Map terms = (LinkedHashMap) value.get(solrField);
				for (String term : (Set<String>) terms.keySet()) {
					Map termInfo = (LinkedHashMap) terms.get(term);
					double tfIdf = getDouble(termInfo.get("tf-idf"));
					if (doCollectTerms) {
						int tf = getInt(termInfo.get("tf"));
						int df = getInt(termInfo.get("df"));
						termsCollection.get(field).add(new TfIdf(term, tf, df, tfIdf));
					}
					sum += tfIdf;
					count++;
				}
			}
			double avg = count > 0 ? sum / count : 0;
			results.put(field + ":sum", sum);
			results.put(field + ":avg", avg);
		}
		return results;
	}

	public Map<String, List<TfIdf>> getTermsCollection() {
		return termsCollection;
	}

	public Double getDouble(Object value) {
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

	public Integer getInt(Object value) {
		int intValue;
		switch (value.getClass().getCanonicalName()) {
			case "java.math.BigDecimal":
				intValue = ((BigDecimal) value).intValue();
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
}
