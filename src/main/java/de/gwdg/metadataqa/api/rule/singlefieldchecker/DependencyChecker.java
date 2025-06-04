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

  public DependencyChecker(DataElement field,
                           List<String> dependencies,
                           RuleCheckingOutputStatus failedDepencencyStatus) {
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
  public void update(Selector selector, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    update(selector, results, outputType, null);
  }

  public void update(Selector cache,
                     FieldCounter<RuleCheckerOutput> localResults,
                     RuleCheckingOutputType outputType,
                     FieldCounter<RuleCheckerOutput> globalResults) {
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id);

    if (globalResults == null)
      globalResults = localResults;

    var isNA = true;
    var allPassed = true;
    List<XmlFieldInstance> instances = cache.get(field);
    if (instances != null && !instances.isEmpty()) {
      for (XmlFieldInstance instance : instances) {
        isNA = false;
      }
    }
    Map<String, Boolean> result = getResult(outputType, globalResults);
    allPassed = result.get("allPassed");
    isNA = result.get("isNA");

    addOutput(localResults, isNA, allPassed, outputType);
    if (isDebug())
      LOGGER.info(String.format("%s %s) result: %s [isNA: %s, allPassed: %s, isMandatory: %s]", this.getClass().getSimpleName(), this.id, RuleCheckingOutputStatus.create(isNA, allPassed, isMandatory()), isNA, allPassed, isMandatory()));
  }

  public List<String> getDependencies() {
    return dependencies;
  }

  public Map<String, Boolean> getResult(RuleCheckingOutputType outputType,
                                        FieldCounter<RuleCheckerOutput> globalResults) {
    boolean allPassed = true;
    boolean isNA = true;
    for (String ruleId : dependencies) {
      String keyEnd = outputType.equals(RuleCheckingOutputType.BOTH) ? ruleId + ":status" : ruleId;
      if (globalResults.has(keyEnd)) {
        var status = globalResults.get(keyEnd).getStatus();

        if (!status.equals(RuleCheckingOutputStatus.PASSED))
          allPassed = false;
        if (!status.equals(RuleCheckingOutputStatus.NA))
          isNA = false;
      } else {
        allPassed = false;
        break;
      }
    }
    return Map.of("allPassed", allPassed, "isNA", isNA);
  }
}
