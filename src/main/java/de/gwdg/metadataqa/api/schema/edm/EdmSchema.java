package de.gwdg.metadataqa.api.schema.edm;

import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.DataElement;
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
 * Abstract EDM schema
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public abstract class EdmSchema implements Schema, ProblemCatalogSchema {

  protected final Map<String, DataElement> paths = new LinkedHashMap<>();
  protected final Map<String, DataElement> collectionPaths = new LinkedHashMap<>();
  protected final List<FieldGroup> fieldGroups = new ArrayList<>();
  protected final List<String> noLanguageFields = new ArrayList<>();
  protected List<DataElement> indexFields;
  protected final List<String> emptyStrings = new ArrayList<>();
  protected String longSubjectPath;
  protected String titlePath;
  protected String descriptionPath;

  protected List<String> categories = null;
  protected List<RuleChecker> ruleCheckers;

  protected Map<String, String> extractableFields = new LinkedHashMap<>();

  protected void addPath(DataElement dataElement) {
    paths.put(dataElement.getLabel(), dataElement);
    if (dataElement.isCollection())
      collectionPaths.put(dataElement.getLabel(), dataElement);
  }

  @Override
  public List<DataElement> getPaths() {
    return new ArrayList(paths.values());
  }

  @Override
  public List<DataElement> getRootChildrenPaths() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public DataElement getPathByLabel(String label) {
    return paths.get(label);
  }

  @Override
  public List<String> getCategories() {
    if (categories == null)
      categories = Category.extractCategories(paths.values(), true);

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
  public List<DataElement> getIndexFields() {
    if (indexFields == null) {
      indexFields = new ArrayList<>();
      for (DataElement dataElement : getPaths())
        if (StringUtils.isNotBlank(dataElement.getIndexField()))
          indexFields.add(dataElement);
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
  public void addExtractableField(String label, String path) {
    extractableFields.put(label, path);
  }

  @Override
  public DataElement getRecordId() {
    return null;
  }
}
