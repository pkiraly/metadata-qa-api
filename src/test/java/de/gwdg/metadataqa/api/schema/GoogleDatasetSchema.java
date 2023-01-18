package de.gwdg.metadataqa.api.schema;

import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.Category;
import de.gwdg.metadataqa.api.rule.RuleChecker;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GoogleDatasetSchema implements Schema, CsvAwareSchema {

  private static final Map<String, DataElement> PATHS = new LinkedHashMap<>();
  private static final Map<String, DataElement> COLLECTION_PATHS = new LinkedHashMap<>();
  private static final Map<String, DataElement> DIRECT_CHILDREN = new LinkedHashMap<>();
  private static Map<String, String> extractableFields = new LinkedHashMap<>();
  private static List<String> categories = null;
  private static List<RuleChecker> ruleCheckers = null;

  static {
    addPath(new DataElement("url", "url").setCategories(Category.MANDATORY));
    addPath(new DataElement("name", "name"));
    addPath(new DataElement("alternateName", "alternateName"));
    addPath(new DataElement("description", "description"));
    addPath(new DataElement("variablesMeasured", "variablesMeasured"));
    addPath(new DataElement("measurementTechnique", "measurementTechnique"));
    addPath(new DataElement("sameAs", "sameAs"));
    addPath(new DataElement("doi", "doi"));
    addPath(new DataElement("identifier", "identifier"));
    addPath(new DataElement("author", "author"));
    addPath(new DataElement("isAccessibleForFree", "isAccessibleForFree"));
    addPath(new DataElement("dateModified", "dateModified"));
    addPath(new DataElement("distribution", "distribution"));
    addPath(new DataElement("spatialCoverage", "spatialCoverage"));
    addPath(new DataElement("provider", "provider"));
    addPath(new DataElement("funder", "funder"));
    addPath(new DataElement("temporalCoverage", "temporalCoverage"));

    extractableFields.put("url", "url");
  }


  @Override
  public Format getFormat() {
    return Format.CSV;
  }

  @Override
  public List<DataElement> getCollectionPaths() {
    return new ArrayList(COLLECTION_PATHS.values());
  }

  @Override
  public List<DataElement> getRootChildrenPaths() {
    return new ArrayList(DIRECT_CHILDREN.values());
  }

  @Override
  public List<DataElement> getPaths() {
    return new ArrayList(PATHS.values());
  }

  @Override
  public DataElement getPathByLabel(String label) {
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
  public List<DataElement> getIndexFields() {
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
  public void addExtractableField(String label, String path) {
    extractableFields.put(label, path);
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

  private static void addPath(DataElement dataElement) {
    PATHS.put(dataElement.getLabel(), dataElement);

    if (dataElement.getParent() == null)
      DIRECT_CHILDREN.put(dataElement.getLabel(), dataElement);

    if (dataElement.isCollection())
      COLLECTION_PATHS.put(dataElement.getLabel(), dataElement);
  }

  @Override
  public List<String> getHeader() {
    List<String> headers = new ArrayList<>();
    for (DataElement dataElement : PATHS.values()) {
      headers.add(dataElement.getPath());
    }
    return headers;
  }

  @Override
  public DataElement getRecordId() {
    return PATHS.get("url");
  }
}
