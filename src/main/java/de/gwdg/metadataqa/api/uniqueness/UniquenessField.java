package de.gwdg.metadataqa.api.uniqueness;

import java.io.Serializable;

/**
 * A value object holding information about uniqueness.
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class UniquenessField implements Serializable {
  /**
   * The schema label of the field.
   */
  String label;
  /**
   * A Solr field.
   */
  String solrField;

  /**
   * A JSON path expression.
   */
  String jsonPath;
  int total;
  double scoreForUniqueValue;

  public UniquenessField(String label) {
    this.label = label;
  }

  public String getSolrField() {
    return solrField;
  }

  public void setSolrField(String solrField) {
    this.solrField = solrField;
  }

  public String getJsonPath() {
    return jsonPath;
  }

  public void setJsonPath(String jsonPath) {
    this.jsonPath = jsonPath;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public void setScoreForUniqueValue(double scoreForUniqueValue) {
    this.scoreForUniqueValue = scoreForUniqueValue;
  }

  public double getScoreForUniqueValue() {
    return scoreForUniqueValue;
  }
}
