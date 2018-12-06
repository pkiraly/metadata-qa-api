package de.gwdg.metadataqa.api.uniqueness;

import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Calculator for uniqueness of a field.
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class UniquenessFieldCalculator {

  private static final Logger LOGGER = Logger.getLogger(
      UniquenessFieldCalculator.class.getCanonicalName()
  );

  private JsonPathCache cache;
  private String recordId;
  private UniquenessField uniquenessField;
  private SolrClient solrClient;

  List<Double> counts = new ArrayList<>();
  List<Double> scores = new ArrayList<>();
  UniquenessExtractor extractor;
  double averageCount;
  double averageScore;

  public UniquenessFieldCalculator(JsonPathCache cache, String recordId, SolrClient solrClient, UniquenessField solrField) {
    this.cache = cache;
    this.recordId = recordId;
    this.solrClient = solrClient;
    this.uniquenessField = solrField;
    extractor = new UniquenessExtractor();
  }

  public void calculate() {
    List<XmlFieldInstance> values = cache.get(uniquenessField.getJsonPath());
    if (values != null) {
      for (XmlFieldInstance fieldInstance : values) {
        String value = fieldInstance.getValue();
        if (StringUtils.isNotBlank(value)) {
          String solrResponse = solrClient.getSolrSearchResponse(uniquenessField.getSolrField(), value);
          int count = extractor.extractNumFound(solrResponse, recordId);
          if (count == 0) {
            count = 1;
          }
          double score = Math.pow(
            (calculateScore(uniquenessField.getTotal(), count) / uniquenessField.getScoreForUniqueValue()),
            3.0
          );

          counts.add((double) count);
          scores.add(score);
        }
      }
    }
    averageCount = getAverage(counts, recordId, "count");
    averageScore = getAverage(scores, recordId, "score");
  }

  public static double calculateScore(double total, double actual) {
    return Math.log(1 + (total - actual + 0.5) / (actual + 0.5));
  }

  private Double getAverage(List<Double> numbers, String recordId, String type) {
    Double result = 0.0;
    if (!numbers.isEmpty()) {
      if (numbers.size() == 1) {
        result = numbers.get(0);
      } else {
        double total = 0;
        for (double number : numbers) {
          total += number;
        }
        result = total / numbers.size();
      }
      if (type.equals("score") && (result < 0.0 || result > 1.0)) {
        List<String> pairs = new ArrayList<>();
        for (int i = 0; i < numbers.size(); i++) {
          pairs.add(String.format("%f -> %f", counts.get(i), numbers.get(i)));
        }
        LOGGER.severe(String.format("EXTREME AVERAGE at %s: %f <- average of %s",
          recordId, result, StringUtils.join(pairs, ", ")));
      }
    }
    return result;
  }


  public double getAverageCount() {
    return averageCount;
  }

  public double getAverageScore() {
    return averageScore;
  }

}
