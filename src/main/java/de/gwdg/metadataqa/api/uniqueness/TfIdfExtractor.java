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
 * Extracts TF-IDF information from Apache Solr
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class TfIdfExtractor {

	private static final JsonProvider jsonProvider = Configuration.defaultConfiguration().jsonProvider();
	private final Schema schema;

	public TfIdfExtractor(Schema schema) {
		this.schema = schema;
	}

	private Map<String, List<TfIdf>> termsCollection;

	/**
	 * Extracts sums and average of TF-IDF value for the schema's Solr field array
	 * without collecting the terms
	 * 
	 * @param jsonString
	 *    The JSON string
	 * @param recordId
	 *    The record identifier
	 * @return
	 *    Sums and average of TF-IDF value
	 */
	public Map<String, Double> extract(String jsonString, String recordId) {
		return extract(jsonString, recordId, false);
	}

	/**
	 * Extracts sums and average of TF-IDF value for the schema's Solr field array
	 * 
	 * @param jsonString
	 *    The JSON string
	 * @param recordId
	 *    The record identifier
	 * @param doCollectTerms
	 *    A flag if the method collects terms
	 * @return
	 *    Sums and average of TF-IDF value
	 */
	public Map<String, Double> extract(String jsonString, String recordId, boolean doCollectTerms) {
		Map<String, Double> results = new LinkedHashMap<>();
		termsCollection = new LinkedHashMap<>();
		Object document = jsonProvider.parse(jsonString);
		String path = String.format("$.termVectors.['%s']", recordId);
		Map value = (LinkedHashMap) JsonPath.read(document, path);
		for (String field : schema.getSolrFields().keySet()) {
			if (doCollectTerms)
				termsCollection.put(field, new ArrayList<>());
			String solrField = schema.getSolrFields().get(field);
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

	/**
	 * Returns the term collection. The term collection is a map. The keys are the
	 * field names, the values are the list of TfIdf objects.
	 * @return 
	 *    The term collection
	 */
	public Map<String, List<TfIdf>> getTermsCollection() {
		return termsCollection;
	}

	/**
	 * Transforms different objects (BigDecimal, Integer) to Double
	 * @param value
	 *   The object to transform
	 * @return
	 *   The double value
	 */
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

	/**
	 * Transforms different objects (BigDecimal, Integer) to Integer
	 * @param value
	 *   The object to transform
	 * @return
	 *   The double value
	 */
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
