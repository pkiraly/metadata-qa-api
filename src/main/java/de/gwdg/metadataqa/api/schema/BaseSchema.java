package de.gwdg.metadataqa.api.schema;

import de.gwdg.metadataqa.api.configuration.schema.Rule;
import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.Category;
import de.gwdg.metadataqa.api.rule.RuleChecker;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class BaseSchema implements Schema, CsvAwareSchema, Serializable {

  private static final long serialVersionUID = 6775942932769040511L;
  private static final Logger LOGGER = Logger.getLogger(BaseSchema.class.getCanonicalName());

  private final Map<String, DataElement> paths = new LinkedHashMap<>();
  private final Map<String, DataElement> collectionPaths = new LinkedHashMap<>();
  private final Map<String, DataElement> directChildren = new LinkedHashMap<>();
  private Map<String, String> extractableFields = new LinkedHashMap<>();
  private List<FieldGroup> fieldGroups = new ArrayList<>();
  private List<String> categories = null;
  private List<RuleChecker> ruleCheckers;
  private List<DataElement> indexFields;
  private DataElement recordId;

  private Format format;
  private Map<String, String> namespaces;

  public BaseSchema() {
    // initialize without parameters
  }

  public BaseSchema addField(DataElement branch) {
    branch.setSchema(this);
    paths.put(branch.getLabel(), branch);

    if (branch.getParent() == null)
      directChildren.put(branch.getLabel(), branch);

    if (branch.isCollection())
      collectionPaths.put(branch.getLabel(), branch);

    if (branch.isExtractable())
      addExtractableField(branch.getLabel(), branch.getPath());

    return this;
  }

  public BaseSchema addField(String fieldName) {
    addField(new DataElement(fieldName));
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
  public List<DataElement> getCollectionPaths() {
    return new ArrayList(collectionPaths.values());
  }

  @Override
  public List<DataElement> getRootChildrenPaths() {
    return new ArrayList(directChildren.values());
  }

  @Override
  public List<DataElement> getPaths() {
    return new ArrayList<>(paths.values());
  }

  @Override
  public DataElement getPathByLabel(String label) {
    return paths.get(label);
  }

  public BaseSchema addFieldGroup(FieldGroup fieldgroup) {
    fieldGroups.add(fieldgroup);
	return this;
  }

  @Override
  public List<FieldGroup> getFieldGroups() {
    return fieldGroups;
  }

  @Override
  public List<String> getNoLanguageFields() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<DataElement> getIndexFields() {
    if (indexFields == null) {
      indexFields = new ArrayList<>();
      for (DataElement dataElement : getPaths()) {
        if (StringUtils.isNotBlank(dataElement.getIndexField())) {
          indexFields.add(dataElement);
        } else if (dataElement.getRules() != null) {
          for (Rule rule : dataElement.getRules()) {
            if (rule.getUnique() != null && rule.getUnique().equals(Boolean.TRUE)) {
              LOGGER.warning(dataElement + " does not have index field");
              indexFields.add(dataElement);
            }
          }
        }
      }
    }
    return indexFields;
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
    if (categories == null) {
      categories = Category.extractCategories(paths.values());
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
  public List<String> getHeader() {
    List<String> headers = new ArrayList<>();
    for (DataElement branch : paths.values()) {
      headers.add(branch.getPath());
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

  public void setNamespaces(Map<String, String> namespaces) {
    this.namespaces = namespaces;
  }

  @Override
  public Map<String, String> getNamespaces() {
    return namespaces;
  }

  @Override
  public DataElement getRecordId() {
    return recordId;
  }

  public void setRecordId(DataElement recordId) {
    this.recordId = recordId;
  }
}
