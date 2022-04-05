package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.uniqueness.SolrClient;
import de.gwdg.metadataqa.api.uniqueness.UniquenessExtractor;

import java.util.List;

public class UniqunessChecker extends SingleFieldChecker {

  private static final long serialVersionUID = -1432138574479246596L;
  public static final String PREFIX = "uniquness";
  protected String solrField;
  private SolrClient solrClient;

  public UniqunessChecker(JsonBranch field) {
    this(field, field.getLabel());
  }

  public UniqunessChecker(JsonBranch field, String header) {
    super(field, header + ":" + PREFIX);
    this.solrField = field.getLabel().equals("recordId") ? "id" : field.getIndexField() + "_ss";
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    var allPassed = true;
    var isNA = true;
    List<XmlFieldInstance> instances = cache.get(field.getJsonPath());
    if (instances != null && !instances.isEmpty()) {
      for (XmlFieldInstance instance : instances) {
        if (instance.hasValue()) {
          isNA = false;
          String solrResponse = solrClient.getSolrSearchResponse(solrField, instance.getValue());
          int numFound = UniquenessExtractor.extractNumFound(solrResponse);
          if (numFound > 1) {
            allPassed = false;
            break;
          }
        }
      }
    }
    addOutput(results, isNA, allPassed, outputType);
  }

  public void setSolrClient(SolrClient solrClient) {
    this.solrClient = solrClient;
  }
}
