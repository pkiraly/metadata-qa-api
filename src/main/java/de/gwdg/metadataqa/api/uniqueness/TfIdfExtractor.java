package de.gwdg.metadataqa.api.uniqueness;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JsonProvider;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.Converter;

/**
 * Extracts TF-IDF information from Apache Solr
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class TfIdfExtractor {

	private static final JsonProvider JSON_PROVIDER = Configuration.defaultConfiguration().jsonProvider();
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
	public FieldCounter<Double> extract(String jsonString, String recordId) {
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
	public FieldCounter<Double> extract(String jsonString, String recordId, boolean doCollectTerms) {
		FieldCounter<Double> results = new FieldCounter<>();
		termsCollection = new LinkedHashMap<>();
		Object document = JSON_PROVIDER.parse(jsonString);
		String path = String.format("$.termVectors.['%s']", recordId);
		Map value = (LinkedHashMap) JsonPath.read(document, path);
		for (String field : schema.getSolrFields().keySet()) {
			if (doCollectTerms) {
				termsCollection.put(field, new ArrayList<>());
			}
			String solrField = schema.getSolrFields().get(field);
			double sum = 0;
			double count = 0;
			if (value.containsKey(solrField)) {
				Map terms = (LinkedHashMap) value.get(solrField);
				for (String term : (Set<String>) terms.keySet()) {
					Map termInfo = (LinkedHashMap) terms.get(term);
					double tfIdf = Converter.asDouble(termInfo.get("tf-idf"));
					if (doCollectTerms) {
						int tf = Converter.asInteger(termInfo.get("tf"));
						int df = Converter.asInteger(termInfo.get("df"));
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

}
