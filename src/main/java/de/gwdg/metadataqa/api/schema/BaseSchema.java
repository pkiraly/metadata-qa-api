package de.gwdg.metadataqa.api.schema;

import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.Category;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BaseSchema implements Schema, CsvAwareSchema {

  private final Map<String, JsonBranch> PATHS = new LinkedHashMap<>();
  private final Map<String, JsonBranch> COLLECTION_PATHS = new LinkedHashMap<>();
  private final Map<String, JsonBranch> DIRECT_CHILDREN = new LinkedHashMap<>();
  private Map<String, String> extractableFields = new LinkedHashMap<>();
  private List<Category> categories = null;

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
  public List<Category> getCategories() {
    if (categories == null) {
      categories = Category.extractCategories(PATHS.values());
    }
    return categories;
  }

  private void addPath(JsonBranch branch) {
    PATHS.put(branch.getLabel(), branch);
    if (branch.getParent() == null) {
      DIRECT_CHILDREN.put(branch.getLabel(), branch);
    }
    if (branch.isCollection()) {
      COLLECTION_PATHS.put(branch.getLabel(), branch);
    }
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
}
