package de.gwdg.metadataqa.api.schema;

import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.Category;
import de.gwdg.metadataqa.api.rule.RuleChecker;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GoogleDatasetSchema implements Schema, CsvAwareSchema {

  private static final Map<String, JsonBranch> PATHS = new LinkedHashMap<>();
  private static final Map<String, JsonBranch> COLLECTION_PATHS = new LinkedHashMap<>();
  private static final Map<String, JsonBranch> DIRECT_CHILDREN = new LinkedHashMap<>();
  private static Map<String, String> extractableFields = new LinkedHashMap<>();
  private static List<String> categories = null;
  private static List<RuleChecker> ruleCheckers = null;

  static {
    addPath(new JsonBranch("url", "url").setCategories(Category.MANDATORY));
    addPath(new JsonBranch("name", "name"));
    addPath(new JsonBranch("alternateName", "alternateName"));
    addPath(new JsonBranch("description", "description"));
    addPath(new JsonBranch("variablesMeasured", "variablesMeasured"));
    addPath(new JsonBranch("measurementTechnique", "measurementTechnique"));
    addPath(new JsonBranch("sameAs", "sameAs"));
    addPath(new JsonBranch("doi", "doi"));
    addPath(new JsonBranch("identifier", "identifier"));
    addPath(new JsonBranch("author", "author"));
    addPath(new JsonBranch("isAccessibleForFree", "isAccessibleForFree"));
    addPath(new JsonBranch("dateModified", "dateModified"));
    addPath(new JsonBranch("distribution", "distribution"));
    addPath(new JsonBranch("spatialCoverage", "spatialCoverage"));
    addPath(new JsonBranch("provider", "provider"));
    addPath(new JsonBranch("funder", "funder"));
    addPath(new JsonBranch("temporalCoverage", "temporalCoverage"));

    extractableFields.put("url", "url");
  }


  @Override
  public Format getFormat() {
    return Format.CSV;
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
    return new ArrayList<>();
  }

  @Override
  public List<String> getNoLanguageFields() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<JsonBranch> getIndexFields() {
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
    if (categories == null)
      categories = Category.extractCategories(PATHS.values());

    return categories;
  }

  @Override
  public List<RuleChecker> getRuleCheckers() {
    return ruleCheckers;
  }

  private static void addPath(JsonBranch branch) {
    PATHS.put(branch.getLabel(), branch);

    if (branch.getParent() == null)
      DIRECT_CHILDREN.put(branch.getLabel(), branch);

    if (branch.isCollection())
      COLLECTION_PATHS.put(branch.getLabel(), branch);
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
  public JsonBranch getRecordId() {
    return PATHS.get("url");
  }
}
