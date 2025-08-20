package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.uniqueness.SolrClient;
import de.gwdg.metadataqa.api.uniqueness.UniquenessExtractor;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class UniquenessChecker extends SingleFieldChecker {

  private static final long serialVersionUID = -1432138574479246596L;
  public static final String PREFIX = "uniqueness";

  private static final Logger LOGGER = Logger.getLogger(UniquenessChecker.class.getCanonicalName());

  protected String solrField;
  private SolrClient solrClient;
  private enum METHOD {ALL_AT_ONCE, INDIVIDUAL};
  private METHOD method = METHOD.ALL_AT_ONCE;

  public UniquenessChecker(DataElement field) {
    this(field, field.getLabel());
  }

  public UniquenessChecker(DataElement field, String header) {
    super(field, header + ":" + PREFIX);
    // this.solrField = field.getLabel().equals("recordId") ? "id" : field.getIndexField() + "_ss";
    this.solrField = field.getIndexField() + "_ss";
  }

  @Override
  public void update(Selector selector, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass() + " " + this.id);
    var allPassed = true;
    var isNA = true;
    List<XmlFieldInstance> instances = selector.get(field);
    if (instances != null && !instances.isEmpty()) {
      Set<String> values = new HashSet<>();
      for (XmlFieldInstance instance : instances) {
        if (instance.hasValue()) {
          isNA = false;
          if (method == METHOD.INDIVIDUAL) {
            if (isDebug())
              LOGGER.info(String.format("field: '%s', value: '%s'", solrField, instance.getValue()));
            String solrResponse = solrClient.getSolrSearchResponse(solrField, instance.getValue());
            int numFound = UniquenessExtractor.extractNumFound(solrResponse);
            if (isDebug())
              LOGGER.info(String.format("numFound: '%d'", numFound));
            if (numFound > 1) {
              allPassed = false;
              break;
            }
          } else if (method == METHOD.ALL_AT_ONCE) {
            values.add(instance.getValue());
          }
        }
      }
      if (method == METHOD.ALL_AT_ONCE) {
        if (isDebug())
          LOGGER.info(String.format("field: '%s', value: '%s'", solrField, StringUtils.join(values, " --- ")));
        String solrResponse = solrClient.getSolrSearchResponse(solrField, values);
        int numFound = UniquenessExtractor.extractNumFound(solrResponse);
        if (isDebug())
          LOGGER.info(String.format("numFound: '%d'", numFound));
        if (numFound > 1) {
          allPassed = false;
        }
      }
    }

    addOutput(results, isNA, allPassed, outputType);
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") result: " + RuleCheckingOutputStatus.create(isNA, allPassed, isMandatory()));
  }

  public void setSolrClient(SolrClient solrClient) {
    this.solrClient = solrClient;
  }
}
