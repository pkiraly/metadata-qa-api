package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DependencyChecker extends SingleFieldChecker {

  private static final Logger LOGGER = Logger.getLogger(DependencyChecker.class.getCanonicalName());

  public static final String PREFIX = "dependency";
  protected List<String> dependencies;
  private RuleCheckingOutputStatus failedDepencencyStatus;

  public DependencyChecker(DataElement field, List<String> dependencies) {
    this(field, field.getLabel(), dependencies, RuleCheckingOutputStatus.FAILED);
  }

  public DependencyChecker(DataElement field, List<String> dependencies, RuleCheckingOutputStatus failedDepencencyStatus) {
    this(field, field.getLabel(), dependencies, failedDepencencyStatus);
  }

  public DependencyChecker(DataElement field,
                           String header,
                           List<String> dependencies,
                           RuleCheckingOutputStatus failedDepencencyStatus) {
    super(field, header + ":" + PREFIX);
    this.dependencies = dependencies;
    this.failedDepencencyStatus = failedDepencencyStatus;
  }

  @Override
  public void update(Selector cache, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    update(cache, results, outputType, null);
  }

  public void update(Selector cache, FieldCounter<RuleCheckerOutput> localResults, RuleCheckingOutputType outputType,
                     FieldCounter<RuleCheckerOutput> globalResults) {
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id);

    if (globalResults == null)
      globalResults = localResults;

    var allPassed = true;
    var isNA = true;
    List<XmlFieldInstance> instances = cache.get(field);
    if (instances != null && !instances.isEmpty()) {
      for (XmlFieldInstance instance : instances) {
        if (instance.hasValue()) {
          isNA = false;
          for (String ruleId : dependencies) {
            String keyEnd = outputType.equals(RuleCheckingOutputType.BOTH) ? ruleId + ":status" : ruleId;
            boolean found = false;
            for (Map.Entry<String, RuleCheckerOutput> entry : globalResults.getMap().entrySet()) {
              if (entry.getKey().endsWith(keyEnd)) {
                found = true;
                if (entry.getValue().getStatus().equals(RuleCheckingOutputStatus.FAILED)) {
                  allPassed = false;
                  break;
                }
              }
            }
            if (!found) {
              allPassed = false;
              break;
            }
          }
        }
      }
    }

    addOutput(localResults, isNA, allPassed, outputType);
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") result: " + RuleCheckingOutputStatus.create(isNA, allPassed));
  }
}
