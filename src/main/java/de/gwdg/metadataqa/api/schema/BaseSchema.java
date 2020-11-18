package de.gwdg.metadataqa.api.schema;

import de.gwdg.metadataqa.api.configuration.Rule;
import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.Category;
import de.gwdg.metadataqa.api.rule.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BaseSchema implements Schema, CsvAwareSchema {

  private final Map<String, JsonBranch> PATHS = new LinkedHashMap<>();
  private final Map<String, JsonBranch> COLLECTION_PATHS = new LinkedHashMap<>();
  private final Map<String, JsonBranch> DIRECT_CHILDREN = new LinkedHashMap<>();
  private Map<String, String> extractableFields = new LinkedHashMap<>();
  private List<String> categories = null;
  private List<RuleChecker> ruleCheckers;

  private Format format;

  public BaseSchema() {
  }

  public BaseSchema addField(JsonBranch branch) {
    PATHS.put(branch.getLabel(), branch);

    if (branch.getParent() == null)
      DIRECT_CHILDREN.put(branch.getLabel(), branch);

    if (branch.isCollection())
      COLLECTION_PATHS.put(branch.getLabel(), branch);

    if (branch.isExtractable())
      extractableFields.put(branch.getLabel(), branch.getJsonPath());

    return this;
  }

  public BaseSchema addField(String fieldName) {
    addField(new JsonBranch(fieldName));
    return this;
  }

  public BaseSchema addFields(String... fields) {
    for (String fieldName : fields) {
      addField(fieldName);
    }
    return this;
  }

  public BaseSchema setFormat(Format format) {
    this.format = format;
    return this;
  }

  @Override
  public Format getFormat() {
    return format;
  }

  @Override
  public List<JsonBranch> getCollectionPaths() {
    return new ArrayList(COLLECTION_PATHS.values());
  }

  @Override
  public List<JsonBranch> getRootChildrenPaths() {
    return new ArrayList(DIRECT_CHILDREN.values());
  }

  @Override
  public List<JsonBranch> getPaths() {
    return new ArrayList(PATHS.values());
  }

  @Override
  public JsonBranch getPathByLabel(String label) {
    return PATHS.get(label);
  }

  @Override
  public List<FieldGroup> getFieldGroups() {
    return new ArrayList<FieldGroup>();
  }

  @Override
  public List<String> getNoLanguageFields() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Map<String, String> getSolrFields() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Map<String, String> getExtractableFields() {
    return extractableFields;
  }

  @Override
  public void setExtractableFields(Map<String, String> extractableFields) {
    this.extractableFields = extractableFields;
  }

  @Override
  public void addExtractableField(String label, String jsonPath) {
    extractableFields.put(label, jsonPath);
  }

  @Override
  public List<String> getCategories() {
    if (categories == null) {
      categories = Category.extractCategories(PATHS.values());
    }
    return categories;
  }

  @Override
  public List<RuleChecker> getRuleCheckers() {
    if (ruleCheckers == null) {
      ruleCheckers = new ArrayList<>();
      for (JsonBranch branch : PATHS.values()) {
        if (branch.getRules() != null) {
          Rule rules = branch.getRules();
          if (StringUtils.isNotBlank(rules.getPattern()))
            ruleCheckers.add(new PatternChecker(branch, rules.getPattern()));
          if (StringUtils.isNotBlank(rules.getEquals()))
            ruleCheckers.add(new EqualityChecker(branch, rules.getEquals()));
          if (StringUtils.isNotBlank(rules.getDisjoint()))
            ruleCheckers.add(new DisjointChecker(branch, rules.getDisjoint()));
          if (rules.getIn() != null && !rules.getIn().isEmpty())
            ruleCheckers.add(new EnumerationChecker(branch, rules.getIn()));
          if (rules.getMinCount() != null)
            ruleCheckers.add(new MinCountChecker(branch, rules.getMinCount()));
          if (rules.getMaxCount() != null)
            ruleCheckers.add(new MaxCountChecker(branch, rules.getMaxCount()));
          if (rules.getMinLength() != null)
            ruleCheckers.add(new MinLengthChecker(branch, rules.getMinLength()));
          if (rules.getMaxLength() != null)
            ruleCheckers.add(new MaxLengthChecker(branch, rules.getMaxLength()));
          if (StringUtils.isNotBlank(rules.getHasValue()))
            ruleCheckers.add(new HasValueChecker(branch, rules.getHasValue()));

          //  private Integer minExclusive;
          //  private Integer minInclusive;
          //  private Integer maxExclusive;
          //  private Integer maxInclusive;
          //  private Integer lessThan;
          //  private Integer lessThanOrEquals;
        }
      }
      categories = Category.extractCategories(PATHS.values());
    }
    return ruleCheckers;
  }

  @Override
  public List<String> getHeader() {
    List<String> headers = new ArrayList<>();
    for (JsonBranch branch : PATHS.values()) {
      headers.add(branch.getJsonPath());
    }
    return headers;
  }

  @Override
  public String toString() {
    return "BaseSchema{" +
      "categories=" + categories +
      ", format=" + format +
      '}';
  }

  public void setCategories(List<String> categories) {
    this.categories = categories;
  }
}
