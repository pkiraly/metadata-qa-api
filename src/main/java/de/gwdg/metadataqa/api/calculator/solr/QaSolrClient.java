package de.gwdg.metadataqa.api.calculator.solr;

import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.uniqueness.SolrClient;
import de.gwdg.metadataqa.api.uniqueness.UniquenessExtractor;
import de.gwdg.metadataqa.api.uniqueness.UniquenessField;
import de.gwdg.metadataqa.api.uniqueness.UniquenessFieldCalculator;

import java.util.ArrayList;
import java.util.List;

public class QaSolrClient {

  public static final String SUFFIX = "_ss";

  protected List<UniquenessField> solrFields;

  protected final SolrClient solrClient;
  protected UniquenessExtractor extractor = new UniquenessExtractor();
  protected final Schema schema;

  public QaSolrClient(SolrClient solrClient, Schema schema) {
    this.solrClient = solrClient;
    this.schema = schema;
    initialize(schema);
  }

  private void initialize(Schema schema) {
    solrFields = new ArrayList<>();
    for (JsonBranch jsonBranch : schema.getIndexFields()) {
      var field = new UniquenessField(jsonBranch.getLabel());
      field.setJsonPath(
        jsonBranch.getAbsoluteJsonPath().replace("[*]", "")
      );

      if (schema.getRecordId() != null && jsonBranch.equals(schema.getRecordId()))
        continue;

      String solrField = getSolrField(jsonBranch);
      field.setSolrField(solrField);

      var solrResponse = solrClient.getSolrSearchResponse(solrField, "*");
      var numFound = extractor.extractNumFound(solrResponse);
      field.setTotal(numFound);
      field.setScoreForUniqueValue(
        UniquenessFieldCalculator.calculateScore(numFound, 1.0)
      );

      solrFields.add(field);
    }
  }

  public static String getSolrField(JsonBranch jsonBranch) {
    String solrField = jsonBranch.getIndexField() != null ? jsonBranch.getIndexField() : jsonBranch.generateIndexField();
    if (solrField.endsWith("_txt")) {
      solrField = solrField.replaceAll("_txt$", "_ss");
    } else if (!solrField.endsWith(SUFFIX)) {
      solrField = solrField + SUFFIX;
    }
    return solrField;
  }
}
