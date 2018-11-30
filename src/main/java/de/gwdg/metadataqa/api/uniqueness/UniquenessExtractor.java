package de.gwdg.metadataqa.api.uniqueness;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JsonProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Extracts TF-IDF information from Apache Solr
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class UniquenessExtractor implements Serializable {

	private static final Logger logger = Logger.getLogger(UniquenessExtractor.class.getCanonicalName());
	private static final JsonProvider jsonProvider = Configuration.defaultConfiguration().jsonProvider();

	public UniquenessExtractor() {
	}

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
		int numFound = 1;
		if (StringUtils.isBlank(jsonString)) {
			return numFound;
		}

		Object document = jsonProvider.parse(jsonString);
		if (document instanceof LinkedHashMap) {
			Map documentMap = (LinkedHashMap)document;
			if (documentMap.containsKey("response")) {
				Map response = (LinkedHashMap) documentMap.get("response");
				numFound = (int) response.get("numFound");
			} else {
				logger.severe("No 'response' part in Solr response: " + jsonString);
			}
		} else {
			System.err.println(">>" + document + "<<");
			System.err.println(document.getClass());
		}

		return numFound;
	}
}
