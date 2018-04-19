package de.gwdg.metadataqa.api.uniqueness;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JsonProvider;
import de.gwdg.metadataqa.api.schema.Schema;

import java.io.Serializable;
import java.util.*;

/**
 * Extracts TF-IDF information from Apache Solr
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class UniquenessExtractor implements Serializable {

	private static final JsonProvider jsonProvider = Configuration.defaultConfiguration().jsonProvider();
	private Schema schema;

	public UniquenessExtractor(Schema schema) {
		this.schema = schema;
	}

	private Map<String, List<TfIdf>> termsCollection;

		/**
		 * Extracts sums and average of TF-IDF value for the schema's Solr field array
		 *
		 * @param jsonString
		 *    The JSON string
		 * @param recordId
		 *    The record identifier
		 * @return
		 *    Sums and average of TF-IDF value
		 */
	public Integer extractNumFound(String jsonString, String recordId) {
		int numFound = 0;
		Object document = jsonProvider.parse(jsonString);
		if (document instanceof LinkedHashMap) {
			Map documentMap = (LinkedHashMap)document;
			if (documentMap.containsKey("response")) {
				Map response = (LinkedHashMap) documentMap.get("response");
				numFound = (int) response.get("numFound");
			}
		} else {
			System.err.println(">>" + document + "<<");
			System.err.println(document.getClass());
		}

		return numFound;
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
