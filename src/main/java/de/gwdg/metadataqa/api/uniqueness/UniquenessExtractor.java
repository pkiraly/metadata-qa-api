package de.gwdg.metadataqa.api.uniqueness;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JsonProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Extracts uniqueness information based on TF-IDF information stored
 * in Apache Solr.
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class UniquenessExtractor implements Serializable {

  private static final Logger LOGGER = Logger.getLogger(UniquenessExtractor.class.getCanonicalName());
  private static final JsonProvider JSON_PROVIDER = Configuration.defaultConfiguration().jsonProvider();
  private static final long serialVersionUID = -4924105546725077045L;

  public UniquenessExtractor() {
  }

  /**
   * Extracts sums and average of TF-IDF value for the schema's Solr field array.
   *
   * @param jsonString
   *    The JSON string
   * @param recordId
   *    The record identifier
   * @return
   *    Sums and average of TF-IDF value
   */
  public Integer extractNumFound(String jsonString, String recordId) {
    var numFound = 1;
    if (StringUtils.isBlank(jsonString))
      return numFound;

    Object document = JSON_PROVIDER.parse(jsonString);
    if (document instanceof LinkedHashMap) {
      Map documentMap = (LinkedHashMap) document;
      if (documentMap.containsKey("response")) {
        Map response = (LinkedHashMap) documentMap.get("response");
        numFound = (int) response.get("numFound");
      } else {
        LOGGER.severe("No 'response' part in Solr response: " + jsonString);
      }
    } else {
      LOGGER.severe("Problem with parsing Solr response: >>" + document
          + "<< class:" + document.getClass());
    }

    return numFound;
  }
}
