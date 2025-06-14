package de.gwdg.metadataqa.api.schema;

import de.gwdg.metadataqa.api.configuration.schema.Rule;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.rule.logical.AndChecker;
import de.gwdg.metadataqa.api.rule.logical.LogicalChecker;
import de.gwdg.metadataqa.api.rule.logical.NotChecker;
import de.gwdg.metadataqa.api.rule.logical.OrChecker;
import de.gwdg.metadataqa.api.rule.pairchecker.DisjointChecker;
import de.gwdg.metadataqa.api.rule.pairchecker.LessThanPairChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.ContentTypeChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.DependencyChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.EnumerationChecker;
import de.gwdg.metadataqa.api.rule.pairchecker.EqualityChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.HasChildrenChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.HasValueChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.ImageDimensionChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.LanguageTagChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.LinkValidityChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.MQAFPatternChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.MaxCountChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.MaxLengthChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.MaxWordsChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.MinCountChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.MinLengthChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.MinWordsChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.MultilingualChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.NumericValueChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.PatternChecker;
import de.gwdg.metadataqa.api.rule.RuleChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.UniquenessChecker;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static de.gwdg.metadataqa.api.rule.singlefieldchecker.NumericValueChecker.TYPE.MAX_EXCLUSIVE;
import static de.gwdg.metadataqa.api.rule.singlefieldchecker.NumericValueChecker.TYPE.MAX_INCLUSIVE;
import static de.gwdg.metadataqa.api.rule.singlefieldchecker.NumericValueChecker.TYPE.MIN_EXCLUSIVE;
import static de.gwdg.metadataqa.api.rule.singlefieldchecker.NumericValueChecker.TYPE.MIN_INCLUSIVE;

public class SchemaUtils {

  private static final Logger LOGGER = Logger.getLogger(SchemaUtils.class.getCanonicalName());
  static int id = 0;

  /**
   * Create rule checkers based on the schema
   *
   * @param schema
   * @return The list of rule checkers
   */
  public static List<RuleChecker> getRuleCheckers(Schema schema) {
    setSchemaForFields(schema);
    id = 0;
    List<RuleChecker> allRuleCheckers = new ArrayList<>();
    for (DataElement dataElement : schema.getPaths()) {
      if (dataElement.getRules() != null) {
        List<Rule> rules = dataElement.getRules();
        for (Rule rule : rules) {
          List<RuleChecker> ruleCheckers = processRule(schema, dataElement, rule);
          if (!ruleCheckers.isEmpty())
            allRuleCheckers.addAll(ruleCheckers);
        }
      }
    }
    return allRuleCheckers;
  }

  private static List<RuleChecker> processRule(Schema schema, DataElement dataElement, Rule rule) {
    List<RuleChecker> ruleCheckers = new ArrayList<>();
    if (rule.getSkip().equals(Boolean.TRUE))
      return ruleCheckers;

    if (StringUtils.isNotBlank(rule.getPattern()))
      ruleCheckers.add(new PatternChecker(dataElement, rule.getPattern()));

    if (rule.getMqafPattern() != null)
      ruleCheckers.add(new MQAFPatternChecker(dataElement, rule.getMqafPattern()));

    if (StringUtils.isNotBlank(rule.getEquals()))
      pair(schema, ruleCheckers, dataElement, rule.getEquals(), "equals");

    if (StringUtils.isNotBlank(rule.getDisjoint()))
      pair(schema, ruleCheckers, dataElement, rule.getDisjoint(), "disjoint");

    if (rule.getIn() != null && !rule.getIn().isEmpty())
      ruleCheckers.add(new EnumerationChecker(dataElement, rule.getIn()));

    if (rule.getMinCount() != null)
      ruleCheckers.add(new MinCountChecker(dataElement, rule.getMinCount(), rule.getAllowEmptyInstances()));

    if (rule.getMaxCount() != null)
      ruleCheckers.add(new MaxCountChecker(dataElement, rule.getMaxCount(), rule.getAllowEmptyInstances()));

    if (rule.getMinLength() != null)
      ruleCheckers.add(new MinLengthChecker(dataElement, rule.getMinLength()));

    if (rule.getMaxLength() != null)
      ruleCheckers.add(new MaxLengthChecker(dataElement, rule.getMaxLength()));

    if (rule.getMaxWords() != null)
      ruleCheckers.add(new MaxWordsChecker(dataElement, rule.getMaxWords()));

    if (rule.getMinWords() != null)
      ruleCheckers.add(new MinWordsChecker(dataElement, rule.getMinWords()));

    if (StringUtils.isNotBlank(rule.getHasValue()))
      ruleCheckers.add(new HasValueChecker(dataElement, rule.getHasValue()));

    if (rule.getHasChildren() != null && !rule.getHasChildren().isEmpty())
      ruleCheckers.add(new HasChildrenChecker(dataElement, rule.getHasChildren()));

    if (rule.getMinInclusive() != null)
      ruleCheckers.add(new NumericValueChecker(dataElement, rule.getMinInclusive(), MIN_INCLUSIVE));

    if (rule.getMaxInclusive() != null)
      ruleCheckers.add(new NumericValueChecker(dataElement, rule.getMinInclusive(), MAX_INCLUSIVE));

    if (rule.getMinExclusive() != null)
      ruleCheckers.add(new NumericValueChecker(dataElement, rule.getMinInclusive(), MIN_EXCLUSIVE));

    if (rule.getMaxExclusive() != null)
      ruleCheckers.add(new NumericValueChecker(dataElement, rule.getMinInclusive(), MAX_EXCLUSIVE));

    if (rule.getContentType() != null && !rule.getContentType().isEmpty()) {
      ContentTypeChecker contentTypeChecker = rule.getTimeout() == null
        ? new ContentTypeChecker(dataElement, rule.getContentType())
        : new ContentTypeChecker(dataElement, dataElement.getLabel(), rule.getContentType(), rule.getTimeout());
      ruleCheckers.add(contentTypeChecker);
    }

    if (rule.getValidLink() != null) {
      LinkValidityChecker checker = rule.getTimeout() == null
        ? new LinkValidityChecker(dataElement, rule.getValidLink())
        : new LinkValidityChecker(dataElement, dataElement.getLabel(), rule.getValidLink(), rule.getTimeout());
      ruleCheckers.add(checker);
    }

    if (rule.getDimension() != null)
      ruleCheckers.add(new ImageDimensionChecker(dataElement, rule.getDimension()));

    if (rule.getDependencies() != null && !rule.getDependencies().isEmpty())
      ruleCheckers.add(new DependencyChecker(dataElement, rule.getDependencies()));

    if (rule.getUnique() != null && rule.getUnique().equals(Boolean.TRUE))
      ruleCheckers.add(new UniquenessChecker(dataElement));

    if (rule.getMultilingual() != null && rule.getMultilingual().equals(Boolean.TRUE))
      ruleCheckers.add(new MultilingualChecker(dataElement));

    if (rule.getHasLanguageTag() != null)
      ruleCheckers.add(new LanguageTagChecker(dataElement).withScope(rule.getHasLanguageTag()));

    if (rule.getLessThan() != null)
      pair(schema, ruleCheckers, dataElement, rule.getLessThan(), "LessThan");

    if (rule.getLessThanOrEquals() != null)
      pair(schema, ruleCheckers, dataElement, rule.getLessThan(), "lessThanOrEquals");

    if (rule.getLessThanOrEquals() != null)
      ruleCheckers.add(new DependencyChecker(dataElement, rule.getDependencies()));

    if (rule.getAnd() != null) {
      List<RuleChecker> childRuleCheckers = getChildRuleCheckers(schema, dataElement, rule.getAnd(), rule.getId());
      AndChecker checker = new AndChecker(dataElement, childRuleCheckers);
      if (rule.getAlwaysCheckDependencies().equals(Boolean.TRUE))
        checker.setAlwaysCheckDependencies(true);
      ruleCheckers.add(checker);
    }

    if (rule.getOr() != null) {
      List<RuleChecker> childRuleCheckers = getChildRuleCheckers(schema, dataElement, rule.getOr(), rule.getId());
      OrChecker checker = new OrChecker(dataElement, childRuleCheckers);
      if (rule.getAlwaysCheckDependencies().equals(Boolean.TRUE))
        checker.setAlwaysCheckDependencies(true);
      ruleCheckers.add(checker);
    }

    if (rule.getNot() != null) {
      List<RuleChecker> childRuleCheckers = getChildRuleCheckers(schema, dataElement, rule.getNot(), rule.getId());
      ruleCheckers.add(new NotChecker(dataElement, childRuleCheckers));
    }

    // General properties
    // List<String> debuggables = new ArrayList<>();
    if (!ruleCheckers.isEmpty()) {
      for (RuleChecker ruleChecker : ruleCheckers) {
        ruleChecker.setFailureScore(rule.getFailureScore());
        ruleChecker.setSuccessScore(rule.getSuccessScore());
        ruleChecker.setNaScore(rule.getNaScore());
        String idValue = StringUtils.isNotBlank(rule.getId()) ? rule.getId() : String.valueOf(++id);
        ruleChecker.setId(idValue);
        ruleChecker.setScope(rule.getScope());
        if (rule.getHidden().equals(Boolean.TRUE))
          ruleChecker.setHidden();
        if (rule.getMandatory().equals(Boolean.TRUE))
          ruleChecker.setMandatory();
        if (rule.getDebug().equals(Boolean.TRUE)) {
          ruleChecker.setDebug();
          if (ruleChecker instanceof LogicalChecker) {
            for (RuleChecker child : ((LogicalChecker) ruleChecker).getCheckers()) {
              child.setDebug();
            }
          }
          // if (ruleChecker instanceof DependencyChecker)
          //   debuggables.addAll(((DependencyChecker) ruleChecker).getDependencies());
        }
        if (StringUtils.isNotBlank(rule.getValuePath()))
          ruleChecker.setValuePath(rule.getValuePath());
      }
    }

    /*
    if (!debuggables.isEmpty()) {
      for (RuleChecker ruleChecker : ruleCheckers) {
        if (debuggables.contains(ruleChecker.getId()))
          ruleChecker.setDebug();
      }
    }
     */

    return ruleCheckers;
  }

  private static List<RuleChecker> getChildRuleCheckers(Schema schema, DataElement dataElement, List<Rule> rules, String id) {
    List<RuleChecker> childRuleCheckers = new ArrayList<>();
    for (Rule childRule : rules) {
      if (StringUtils.isBlank(childRule.getId()))
        childRule.setId(id);
      List<RuleChecker> localChildRuleCheckers = processRule(schema, dataElement, childRule);
      if (!localChildRuleCheckers.isEmpty())
        childRuleCheckers.addAll(localChildRuleCheckers);
    }
    return childRuleCheckers;
  }

  private static void pair(Schema schema,
                           List<RuleChecker> ruleCheckers,
                           DataElement dataElement,
                           String fieldReference,
                           String type) {
    DataElement field2 = schema.getPathByLabel(fieldReference);
    if (field2 != null) {
      RuleChecker ruleChecker = null;
      if ("equals".equals(type)) {
        ruleChecker = new EqualityChecker(dataElement, field2);
      } else if ("disjoint".equals(type)) {
        ruleChecker = new DisjointChecker(dataElement, field2);
      } else if ("lessThan".equals(type)) {
        ruleChecker = new LessThanPairChecker(dataElement, field2, LessThanPairChecker.TYPE.LESS_THAN);
      } else if ("lessThanOrEquals".equals(type)) {
        ruleChecker = new LessThanPairChecker(dataElement, field2, LessThanPairChecker.TYPE.LESS_THAN_OR_EQUALS);
      }

      if (ruleChecker != null)
        ruleCheckers.add(ruleChecker);
    } else
      LOGGER.warning(
        String.format("invalid field reference in %s: %s", type, fieldReference));
  }

  public static void setSchemaForFields(Schema schema) {
    for (DataElement dataElement : schema.getPaths())
      dataElement.setSchema(schema);
  }

  /**
   * Get a rule object by its identifier
   * @param schema The schema
   * @param id The identifier of the rule
   * @return the rule object
   */
  public static Rule getRuleById(Schema schema, String id) {
    for (DataElement dataElement : schema.getPaths()) {
      for (Rule rule : dataElement.getRules()) {
        if (rule.getId().equals(id))
          return rule;
      }
    }
    return null;
  }

  /**
   * Get the direct dependencies of a rule
   * @param rule A rule
   * @return List of the rule identifiers on that the current rule depends on
   */
  public static List<String> getDependencies(Rule rule) {
    List<String> dependencies = new ArrayList<>();
    if (rule.getDependencies() != null) {
      dependencies.addAll(rule.getDependencies());
    }
    if (rule.getAnd() != null && !rule.getAnd().isEmpty()) {
      for (Rule childRule : rule.getAnd()) {
        dependencies.addAll(getDependencies(childRule));
      }
    }
    if (rule.getOr() != null && !rule.getOr().isEmpty()) {
      for (Rule childRule : rule.getOr()) {
        dependencies.addAll(getDependencies(childRule));
      }
    }
    if (rule.getNot() != null && !rule.getNot().isEmpty()) {
      for (Rule childRule : rule.getNot()) {
        dependencies.addAll(getDependencies(childRule));
      }
    }
    return dependencies;
  }

  /**
   * Returns all dependencies
   * @param schema The schema
   * @param rule A rule in the schema
   * @return List of the rule identifiers on that the current rule directly or independently depends on
   */
  public static List<String> getAllDependencies(Schema schema, Rule rule) {
    Map<String, Boolean> dependenciesMap = new LinkedHashMap<>();
    getDependencies(rule).stream().forEach(e -> dependenciesMap.put(e, false));
    boolean runAgain = !dependenciesMap.isEmpty();
    while (runAgain) {
      runAgain = false;
      for (Map.Entry<String, Boolean> entry : dependenciesMap.entrySet()) {
        String ruleId = entry.getKey();
        boolean processed = entry.getValue();
        if (!processed) {
          Rule depRule = getRuleById(schema, ruleId);
          List<String> additionalOnes = getDependencies(depRule);
          int size = dependenciesMap.size();
          for (String additionalOne : additionalOnes)
            dependenciesMap.putIfAbsent(additionalOne, false);
          if (dependenciesMap.size() > size)
            runAgain = true;
          dependenciesMap.put(ruleId, true);
        }
      }
    }
    return dependenciesMap.keySet().stream().collect(Collectors.toList());
  }
}