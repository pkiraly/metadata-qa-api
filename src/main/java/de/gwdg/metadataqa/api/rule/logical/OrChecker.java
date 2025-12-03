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
import java.util.HashMap;
import java.util.logging.Logger;

public class OrChecker extends LogicalChecker {

  private static final Logger LOGGER = Logger.getLogger(OrChecker.class.getCanonicalName());

  private static final long serialVersionUID = 1114999259831619599L;
  public static final String PREFIX = "or";
  private boolean priorityOnFail = false;

  /**
   * @param field The field
   * @param checkers The list of checkers
   */
  public OrChecker(DataElement field, List<RuleChecker> checkers) {
    this(field, field.getLabel(), checkers);
  }

  public OrChecker(DataElement field, String header, List<RuleChecker> checkers) {
    super(field, header + ":" + PREFIX + ":" + getChildrenHeader(checkers));
    this.checkers = checkers;
  }

  @Override
  public void update(Selector selector, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    update(selector, results, outputType, null);
  }

  public void update(Selector selector,
                     FieldCounter<RuleCheckerOutput> results,
                     RuleCheckingOutputType outputType,
                     FieldCounter<RuleCheckerOutput> globalResults) {
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ", alwaysCheckDependencies: " + alwaysCheckDependencies + ", priorityOnFail: " + priorityOnFail + " - " + results);

    if (globalResults == null) {
      globalResults = results;
    }

    // if NA and dependency checker FAILED -> FAILED
    var allPassed = false;
    var isNA = false;
    RuleCheckerOutput output = null;
    List<XmlFieldInstance> instances = selector.get(field);
    List<RuleCheckingOutputStatus> statuses = new ArrayList<>();
    if (instances != null && !instances.isEmpty()) {
      if (isDebug())
        LOGGER.info("non empty");
      FieldCounter<RuleCheckerOutput> localResults2 = new FieldCounter<>();
      for (RuleChecker checker : checkers) {
        if (checker instanceof DependencyChecker) {
          ((DependencyChecker) checker).update(selector, localResults2, outputType, results);
        } else if (checker instanceof AndChecker) {
          ((AndChecker) checker).update(selector, localResults2, outputType, results);
        } else {
          checker.update(selector, localResults2, outputType);
        }
        String key = outputType.equals(RuleCheckingOutputType.BOTH)
                   ? checker.getIdOrHeader(RuleCheckingOutputType.SCORE)
                   : checker.getIdOrHeader();
        RuleCheckingOutputStatus status = localResults2.get(key).getStatus();
        statuses.add(status);
        if (!priorityOnFail && status.equals(RuleCheckingOutputStatus.PASSED)) {
          allPassed = true;
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
        else if (alwaysCheckDependencies && checker instanceof DependencyChecker) {
          DependencyChecker dependencyChecker = (DependencyChecker) checker;
          Map<String, Boolean> localResult = dependencyChecker.getResult(outputType, results);
          boolean dependenciesPassed = localResult.get("allPassed");
          var localIsNA = localResult.get("isNA");
          if (localIsNA) {
            output = new RuleCheckerOutput(this, RuleCheckingOutputStatus.NA);
          } else {
            if (dependenciesPassed)
              output = new RuleCheckerOutput(this, RuleCheckingOutputStatus.PASSED);
            else
              output = new RuleCheckerOutput(this, RuleCheckingOutputStatus.FAILED);
          }
          statuses.add(output.getStatus());
        }

        // if (!allPassed)
        //   break;
      }
    }

    if (priorityOnFail) {
      output = null;
      allPassed = statuses.contains(RuleCheckingOutputStatus.PASSED);
      if (!statuses.isEmpty()
          && !containsOnlyNAs(statuses)
          && (!allPassed || statuses.contains(RuleCheckingOutputStatus.PASSED)))
        isNA = false;
    }

    if (output != null) {
      addOutput(results, output, outputType);
    } else {
      addOutput(results, isNA, allPassed, outputType);
    }

    if (isDebug()) {
      RuleCheckingOutputStatus status = output != null
        ? output.getStatus()
        : RuleCheckingOutputStatus.create(isNA, allPassed, isMandatory());
      LOGGER.info(String.format("%s %s) isNA: %s, allPassed: %s, result: %s",
        this.getClass().getSimpleName(), this.id, isNA, allPassed, status));
    }
  }

  public Map<RuleCheckingOutputType, Object> getResult(RuleCheckingOutputType outputType,
                                                       FieldCounter<RuleCheckerOutput> globalResults) {
    Map<RuleCheckingOutputType, Object> result = new HashMap();
    if (outputType.equals(RuleCheckingOutputType.STATUS)) {
      result.put(RuleCheckingOutputType.STATUS, globalResults.get(getIdOrHeader()));
    } else if (outputType.equals(RuleCheckingOutputType.SCORE)) {
      result.put(RuleCheckingOutputType.SCORE, globalResults.get(getIdOrHeader()));
    } else if (outputType.equals(RuleCheckingOutputType.BOTH)) {
      result.put(RuleCheckingOutputType.STATUS, globalResults.get(getIdOrHeader() + ":status"));
      result.put(RuleCheckingOutputType.SCORE, globalResults.get(getIdOrHeader() + ":score"));
    }
    return result;
  }

  public void setPriorityOnFail(boolean priorityOnFail) {
    this.priorityOnFail = priorityOnFail;
  }

  private boolean containsOnlyNAs(List<RuleCheckingOutputStatus> statuses) {
    for (RuleCheckingOutputStatus status : statuses) {
      if (!status.equals(RuleCheckingOutputStatus.NA))
        return false;
    }
    return true;
  }
}
