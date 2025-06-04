package de.gwdg.metadataqa.api.rule.logical;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.rule.RuleChecker;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.DependencyChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.MinCountChecker;
import de.gwdg.metadataqa.api.rule.logical.OrChecker;

import java.util.List;
import java.util.Map;

public class AndChecker extends LogicalChecker {

  private static final long serialVersionUID = 1114999259831619599L;
  public static final String PREFIX = "and";

  /**
   * @param field The field
   * @param checkers The list of checkers
   */
  public AndChecker(DataElement field, List<RuleChecker> checkers) {
    this(field, field.getLabel(), checkers);
  }

  public AndChecker(DataElement field, String header, List<RuleChecker> checkers) {
    super(field,header + ":" + PREFIX + ":" + getChildrenHeader(checkers));
    this.checkers = checkers;
  }

  @Override
  public void update(Selector selector, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + " alwaysCheckDependencies: " + alwaysCheckDependencies);

    var allPassed = true;
    var isNA = false;
    List<XmlFieldInstance> instances = selector.get(field);
    if (instances != null && !instances.isEmpty()) {
      FieldCounter<RuleCheckerOutput> localResults = new FieldCounter<>();
      for (RuleChecker checker : checkers) {
        if (checker instanceof DependencyChecker)
          ((DependencyChecker)checker).update(selector, localResults, outputType, results);
        else
          checker.update(selector, localResults, outputType);
        String key = outputType.equals(RuleCheckingOutputType.BOTH) ? checker.getIdOrHeader(RuleCheckingOutputType.SCORE) : checker.getIdOrHeader();
        RuleCheckingOutputStatus status = localResults.get(key).getStatus();
        if (status.equals(RuleCheckingOutputStatus.NA))
          isNA = true;
        if (!status.equals(RuleCheckingOutputStatus.PASSED)) {
          allPassed = false;
          break;
        }
      }
    } else {
      if (isDebug())
        LOGGER.info("empty branch");
      isNA = true;
      for (RuleChecker checker : checkers) {
        if (checker instanceof MinCountChecker) {
          if (isDebug())
            LOGGER.info("check MinCountChecker");
          MinCountChecker minCountChecker = (MinCountChecker) checker;
          if (!minCountChecker.isEmptyInstancesAllowed() || minCountChecker.getMinCount() > 0)
            allPassed = false;
        }
        else if (alwaysCheckDependencies) {
          if (isDebug())
            LOGGER.info("check DependencyChecker");
          if (checker instanceof DependencyChecker) {
            DependencyChecker dependencyChecker = (DependencyChecker) checker;
            Map<String, Boolean> result = dependencyChecker.getResult(outputType, results);
            if (isDebug())
              LOGGER.info("DependencyChecker result: " + result);
            allPassed = result.get("allPassed");
            isNA = result.get("isNA");
          } else if (checker instanceof OrChecker) {
            OrChecker orChecker = (OrChecker) checker;
            boolean hasDependency = false;
            for (RuleChecker current : orChecker.getCheckers()) {
              if (current instanceof DependencyChecker) {
                hasDependency = true;
                break;
              }
            }
            if (hasDependency) {
              orChecker.update(selector, results, outputType);
              Map<RuleCheckingOutputType, Object> orResult = orChecker.getResult(outputType, results);
              RuleCheckerOutput orStatus = (RuleCheckerOutput) orResult.get(RuleCheckingOutputType.STATUS);
              if (orStatus.getStatus().equals(RuleCheckingOutputStatus.FAILED)) {
                allPassed = false;
                isNA = false;
              }
            }
          }
        }

        if (!allPassed)
          break;
      }
    }
    if (isDebug())
      LOGGER.info(String.format("isNA: %s, allPassed: %s", isNA, allPassed));
    addOutput(results, isNA, allPassed, outputType);

    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") result: " + RuleCheckingOutputStatus.create(isNA, allPassed, isMandatory()));
  }
}
