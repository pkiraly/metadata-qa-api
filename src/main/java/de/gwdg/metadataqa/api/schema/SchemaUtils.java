package de.gwdg.metadataqa.api.schema;

import de.gwdg.metadataqa.api.configuration.Rule;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.rule.DisjointChecker;
import de.gwdg.metadataqa.api.rule.EnumerationChecker;
import de.gwdg.metadataqa.api.rule.EqualityChecker;
import de.gwdg.metadataqa.api.rule.HasValueChecker;
import de.gwdg.metadataqa.api.rule.MaxCountChecker;
import de.gwdg.metadataqa.api.rule.MaxLengthChecker;
import de.gwdg.metadataqa.api.rule.MinCountChecker;
import de.gwdg.metadataqa.api.rule.MinLengthChecker;
import de.gwdg.metadataqa.api.rule.PatternChecker;
import de.gwdg.metadataqa.api.rule.RuleChecker;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SchemaUtils {

  private static final Logger LOGGER = Logger.getLogger(SchemaUtils.class.getCanonicalName());

  /**
   * @param schema
   * @return
   */
  public static List<RuleChecker> getRuleCheckers(Schema schema) {
    setSchemaForFields(schema);
    List<RuleChecker> ruleCheckers = new ArrayList<>();
    for (JsonBranch branch : schema.getPaths()) {
      if (branch.getRules() != null) {
        List<Rule> rules = branch.getRules();
        for (Rule rule : rules) {
          if (StringUtils.isNotBlank(rule.getPattern()))
            ruleCheckers.add(new PatternChecker(branch, rule.getPattern()));
          if (StringUtils.isNotBlank(rule.getEquals()))
            ruleCheckers.add(new EqualityChecker(branch, rule.getEquals()));
          if (StringUtils.isNotBlank(rule.getDisjoint())) {
            JsonBranch field2 = schema.getPathByLabel(rule.getDisjoint());
            if (field2 != null)
              ruleCheckers.add(new DisjointChecker(branch, field2));
            else
              LOGGER.warning("invalid field reference in disjoint: " + rule.getDisjoint());
          }
          if (rule.getIn() != null && !rule.getIn().isEmpty())
            ruleCheckers.add(new EnumerationChecker(branch, rule.getIn()));
          if (rule.getMinCount() != null)
            ruleCheckers.add(new MinCountChecker(branch, rule.getMinCount()));
          if (rule.getMaxCount() != null)
            ruleCheckers.add(new MaxCountChecker(branch, rule.getMaxCount()));
          if (rule.getMinLength() != null)
            ruleCheckers.add(new MinLengthChecker(branch, rule.getMinLength()));
          if (rule.getMaxLength() != null)
            ruleCheckers.add(new MaxLengthChecker(branch, rule.getMaxLength()));
          if (StringUtils.isNotBlank(rule.getHasValue()))
            ruleCheckers.add(new HasValueChecker(branch, rule.getHasValue()));
        }

        //  private Integer minExclusive;
        //  private Integer minInclusive;
        //  private Integer maxExclusive;
        //  private Integer maxInclusive;
        //  private Integer lessThan;
        //  private Integer lessThanOrEquals;
      }
    }
    return ruleCheckers;
  }

  public static void setSchemaForFields(Schema schema) {
    for (JsonBranch branch : schema.getPaths())
      branch.setSchema(schema);
  }
}
