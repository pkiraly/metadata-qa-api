package de.gwdg.metadataqa.api.schema;

import de.gwdg.metadataqa.api.configuration.schema.Rule;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.rule.logical.AndChecker;
import de.gwdg.metadataqa.api.rule.logical.NotChecker;
import de.gwdg.metadataqa.api.rule.logical.OrChecker;
import de.gwdg.metadataqa.api.rule.pairchecker.DisjointChecker;
import de.gwdg.metadataqa.api.rule.pairchecker.LessThanPairChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.ContentTypeChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.EnumerationChecker;
import de.gwdg.metadataqa.api.rule.pairchecker.EqualityChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.HasValueChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.MaxCountChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.MaxLengthChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.MinCountChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.MinLengthChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.NumericValueChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.PatternChecker;
import de.gwdg.metadataqa.api.rule.RuleChecker;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SchemaUtils {

  private static final Logger LOGGER = Logger.getLogger(SchemaUtils.class.getCanonicalName());
  static int id = 0;

  /**
   * @param schema
   * @return
   */
  public static List<RuleChecker> getRuleCheckers(Schema schema) {
    setSchemaForFields(schema);
    id = 0;
    List<RuleChecker> allRuleCheckers = new ArrayList<>();
    for (JsonBranch branch : schema.getPaths()) {
      if (branch.getRules() != null) {
        List<Rule> rules = branch.getRules();
        for (Rule rule : rules) {
          List<RuleChecker> ruleCheckers = processRule(schema, branch, rule);
          if (!ruleCheckers.isEmpty())
            allRuleCheckers.addAll(ruleCheckers);
        }
      }
    }
    return allRuleCheckers;
  }

  private static List<RuleChecker> processRule(Schema schema, JsonBranch branch, Rule rule) {
    List<RuleChecker> ruleCheckers = new ArrayList<>();

    if (StringUtils.isNotBlank(rule.getPattern()))
      ruleCheckers.add(new PatternChecker(branch, rule.getPattern()));

    if (StringUtils.isNotBlank(rule.getEquals()))
      pair(schema, ruleCheckers, branch, rule.getEquals(), "equals");

    if (StringUtils.isNotBlank(rule.getDisjoint()))
      pair(schema, ruleCheckers, branch, rule.getDisjoint(), "disjoint");

    if (rule.getIn() != null && !rule.getIn().isEmpty())
      ruleCheckers.add(new EnumerationChecker(branch, rule.getIn()));

    if (rule.getMinCount() != null) {
      MinCountChecker checker = new MinCountChecker(branch, rule.getMinCount());
      ruleCheckers.add(checker);
    }

    if (rule.getMaxCount() != null)
      ruleCheckers.add(new MaxCountChecker(branch, rule.getMaxCount()));

    if (rule.getMinLength() != null)
      ruleCheckers.add(new MinLengthChecker(branch, rule.getMinLength()));

    if (rule.getMaxLength() != null)
      ruleCheckers.add(new MaxLengthChecker(branch, rule.getMaxLength()));

    if (StringUtils.isNotBlank(rule.getHasValue()))
      ruleCheckers.add(new HasValueChecker(branch, rule.getHasValue()));

    if (rule.getMinInclusive() != null)
      ruleCheckers.add(new NumericValueChecker(branch, rule.getMinInclusive(),
        NumericValueChecker.TYPE.MIN_INCLUSIVE));

    if (rule.getMaxInclusive() != null)
      ruleCheckers.add(new NumericValueChecker(branch, rule.getMinInclusive(),
        NumericValueChecker.TYPE.MAX_INCLUSIVE));

    if (rule.getMinExclusive() != null)
      ruleCheckers.add(new NumericValueChecker(branch, rule.getMinInclusive(),
        NumericValueChecker.TYPE.MIN_EXCLUSIVE));

    if (rule.getMaxExclusive() != null)
      ruleCheckers.add(new NumericValueChecker(branch, rule.getMinInclusive(),
        NumericValueChecker.TYPE.MAX_EXCLUSIVE));

    if (rule.getContentType() != null && !rule.getContentType().isEmpty())
      ruleCheckers.add(new ContentTypeChecker(branch, rule.getContentType()));

    if (rule.getLessThan() != null)
      pair(schema, ruleCheckers, branch, rule.getLessThan(), "LessThan");

    if (rule.getLessThanOrEquals() != null)
      pair(schema, ruleCheckers, branch, rule.getLessThan(), "lessThanOrEquals");

    if (rule.getAnd() != null) {
      List<RuleChecker> childRuleCheckers = getChildRuleCheckers(schema, branch, rule.getAnd());
      ruleCheckers.add(new AndChecker(branch, childRuleCheckers));
    }

    if (rule.getOr() != null) {
      List<RuleChecker> childRuleCheckers = getChildRuleCheckers(schema, branch, rule.getOr());
      ruleCheckers.add(new OrChecker(branch, childRuleCheckers));
    }

    if (rule.getNot() != null) {
      List<RuleChecker> childRuleCheckers = getChildRuleCheckers(schema, branch, rule.getNot());
      ruleCheckers.add(new NotChecker(branch, childRuleCheckers));
    }

    if (!ruleCheckers.isEmpty())
      for (RuleChecker ruleChecker : ruleCheckers) {
        ruleChecker.setFailureScore(rule.getFailureScore());
        ruleChecker.setSuccessScore(rule.getSuccessScore());
        String idValue = StringUtils.isNotBlank(rule.getId()) ? rule.getId() : String.valueOf(++id);
        ruleChecker.setId(idValue);
      }

    return ruleCheckers;
  }

  private static List<RuleChecker> getChildRuleCheckers(Schema schema, JsonBranch branch, List<Rule> rules) {
    List<RuleChecker> andRuleCheckers = new ArrayList<>();
    for (Rule andRule : rules) {
      List<RuleChecker> localAndRuleCheckers = processRule(schema, branch, andRule);
      if (!localAndRuleCheckers.isEmpty())
        andRuleCheckers.addAll(localAndRuleCheckers);
    }
    return andRuleCheckers;
  }

  private static void pair(Schema schema,
                           List<RuleChecker> ruleCheckers,
                           JsonBranch branch,
                           String fieldReference,
                           String type) {
    JsonBranch field2 = schema.getPathByLabel(fieldReference);
    if (field2 != null) {
      RuleChecker ruleChecker = null;
      if ("equals".equals(type)) {
        ruleChecker = new EqualityChecker(branch, field2);
      } else if ("disjoint".equals(type)) {
        ruleChecker = new DisjointChecker(branch, field2);
      } else if ("lessThan".equals(type)) {
        ruleChecker = new LessThanPairChecker(branch, field2, LessThanPairChecker.TYPE.LESS_THAN);
      } else if ("lessThanOrEquals".equals(type)) {
        ruleChecker = new LessThanPairChecker(branch, field2, LessThanPairChecker.TYPE.LESS_THAN_OR_EQUALS);
      }

      if (ruleChecker != null)
        ruleCheckers.add(ruleChecker);
    } else
      LOGGER.warning(
        String.format("invalid field reference in %s: %s", type, fieldReference));
  }

  public static void setSchemaForFields(Schema schema) {
    for (JsonBranch branch : schema.getPaths())
      branch.setSchema(schema);
  }
}
