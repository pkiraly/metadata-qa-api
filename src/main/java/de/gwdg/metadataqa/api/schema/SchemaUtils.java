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
import java.util.List;
import java.util.logging.Logger;

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

    if (rule.getContentType() != null && !rule.getContentType().isEmpty())
      ruleCheckers.add(new ContentTypeChecker(dataElement, rule.getContentType()));

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
        }
      }
    }

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
}
