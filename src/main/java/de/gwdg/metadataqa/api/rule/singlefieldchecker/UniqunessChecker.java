package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.uniqueness.SolrClient;
import de.gwdg.metadataqa.api.uniqueness.UniquenessExtractor;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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
    if (isDebug())
      LOGGER.info(this.getClass() + " " + this.id);
    var allPassed = true;
    var isNA = true;
    List<XmlFieldInstance> instances = cache.get(field.getJsonPath());
    if (instances != null && !instances.isEmpty()) {
      for (XmlFieldInstance instance : instances) {
        if (instance.hasValue()) {
          isNA = false;
          if (isDebug())
            LOGGER.info("value: " + instance.getValue());
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
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") result: " + RuleCheckingOutputStatus.create(isNA, allPassed));
  }

  public void setSolrClient(SolrClient solrClient) {
    this.solrClient = solrClient;
  }
}
