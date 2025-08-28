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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AndChecker extends LogicalChecker {

  private static final Logger LOGGER = Logger.getLogger(AndChecker.class.getCanonicalName());

  private static final long serialVersionUID = 1114999259831619599L;
  public static final String PREFIX = "and";
  private boolean priorityOnFail = false;

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
      LOGGER.info(String.format("%s %s,  alwaysCheckDependencies: %s, priorityOnFail: %s", this.getClass().getSimpleName(), this.id, alwaysCheckDependencies, priorityOnFail));

    var allPassed = true;
    var isNA = false;
    List<RuleCheckingOutputStatus> statuses = new ArrayList<>();
    List<XmlFieldInstance> instances = selector.get(field);
    if (instances != null && !instances.isEmpty()) {
      FieldCounter<RuleCheckerOutput> localResults = new FieldCounter<>();
      for (RuleChecker checker : checkers) {
        if (checker instanceof DependencyChecker) {
          ((DependencyChecker) checker).update(selector, localResults, outputType, results);
        } else if (checker instanceof OrChecker) {
          ((OrChecker) checker).update(selector, localResults, outputType, results);
        } else if (checker instanceof NotChecker) {
          ((NotChecker) checker).update(selector, localResults, outputType, results);
        } else {
          checker.update(selector, localResults, outputType);
        }
        String key = outputType.equals(RuleCheckingOutputType.BOTH)
          ? checker.getIdOrHeader(RuleCheckingOutputType.SCORE)
          : checker.getIdOrHeader();
        RuleCheckingOutputStatus status = localResults.get(key).getStatus();
        statuses.add(status);
        if (status.equals(RuleCheckingOutputStatus.NA))
          isNA = true;

        if (!status.equals(RuleCheckingOutputStatus.PASSED) && !priorityOnFail) {
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
          MinCountChecker minCountChecker = (MinCountChecker) checker;
          if (!minCountChecker.isEmptyInstancesAllowed() || minCountChecker.getMinCount() > 0)
            allPassed = false;
        }
        else if (alwaysCheckDependencies) {
          if (checker instanceof DependencyChecker) {
            DependencyChecker dependencyChecker = (DependencyChecker) checker;
            Map<String, Boolean> localResult = dependencyChecker.getResult(outputType, results);
            if (isDebug())
              LOGGER.info("DependencyChecker result: " + localResult);
            allPassed = localResult.get("allPassed");
            isNA = localResult.get("isNA");
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

    if (priorityOnFail) {
      allPassed = !statuses.contains(RuleCheckingOutputStatus.FAILED);
      if (!allPassed)
        isNA = false;
    }

    if (isDebug())
      LOGGER.info(String.format("isNA: %s, allPassed: %s", isNA, allPassed));
    addOutput(results, isNA, allPassed, outputType);

    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") result: " + RuleCheckingOutputStatus.create(isNA, allPassed, isMandatory()));
  }

  public void setPriorityOnFail(boolean priorityOnFail) {
    this.priorityOnFail = priorityOnFail;
  }
}
