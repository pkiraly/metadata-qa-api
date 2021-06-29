package de.gwdg.metadataqa.api.schema.edm;

import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.Category;
import de.gwdg.metadataqa.api.rule.RuleChecker;
import de.gwdg.metadataqa.api.schema.ProblemCatalogSchema;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.schema.SchemaUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public abstract class EdmSchema implements Schema, ProblemCatalogSchema {

  protected final Map<String, JsonBranch> paths = new LinkedHashMap<>();
  protected final Map<String, JsonBranch> collectionPaths = new LinkedHashMap<>();
  protected final List<FieldGroup> fieldGroups = new ArrayList<>();
  protected final List<String> noLanguageFields = new ArrayList<>();
  protected List<JsonBranch> indexFields;
  protected final List<String> emptyStrings = new ArrayList<>();
  protected String longSubjectPath;
  protected String titlePath;
  protected String descriptionPath;

  protected List<String> categories = null;
  protected List<RuleChecker> ruleCheckers;

  protected Map<String, String> extractableFields = new LinkedHashMap<>();
  protected Map<String, String> echoFields = new LinkedHashMap<>();

  protected void addPath(JsonBranch branch) {
    paths.put(branch.getLabel(), branch);
    if (branch.isCollection())
      collectionPaths.put(branch.getLabel(), branch);
  }

  @Override
  public List<JsonBranch> getPaths() {
    return new ArrayList(paths.values());
  }

  @Override
  public List<JsonBranch> getRootChildrenPaths() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public JsonBranch getPathByLabel(String label) {
    return paths.get(label);
  }

  @Override
  public List<String> getCategories() {
    if (categories == null) {
      categories = Category.extractCategories(paths.values(), true);
    }
    return categories;
  }

  @Override
  public List<RuleChecker> getRuleCheckers() {
    if (ruleCheckers == null) {
      ruleCheckers = SchemaUtils.getRuleCheckers(this);
    }
    return ruleCheckers;
  }

  @Override
  public List<FieldGroup> getFieldGroups() {
    return fieldGroups;
  }

  @Override
  public List<String> getNoLanguageFields() {
    return noLanguageFields;
  }

  @Override
  public List<JsonBranch> getIndexFields() {
    if (indexFields == null) {
      indexFields = new ArrayList<>();
      for (JsonBranch jsonBranch : getPaths())
        if (StringUtils.isNotBlank(jsonBranch.getIndexField()))
          indexFields.add(jsonBranch);
    }
    return indexFields;

  }

  @Override
  public List<String> getEmptyStringPaths() {
    return emptyStrings;
  }

  @Override
  public String getSubjectPath() {
    return longSubjectPath;
  }

  @Override
  public String getTitlePath() {
    return titlePath;
  }

  @Override
  public String getDescriptionPath() {
    return descriptionPath;
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
  public Map<String, String> getEchoFields() {
    return echoFields;
  }

  @Override
  public void setEchoFields(Map<String, String> echoFields) {
    this.echoFields = echoFields;
  }

  @Override
  public void addEchoField(String label, String jsonPath) {
    echoFields.put(label, jsonPath);
  }
}
