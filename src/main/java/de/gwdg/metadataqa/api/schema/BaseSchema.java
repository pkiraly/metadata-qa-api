package de.gwdg.metadataqa.api.schema;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
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

  public BaseSchema addField(DataElement dataElement) {
    dataElement.setSchema(this);
    paths.put(dataElement.getLabel(), dataElement);

    if (dataElement.getParent() == null)
      directChildren.put(dataElement.getLabel(), dataElement);

    if (dataElement.isCollection())
      collectionPaths.put(dataElement.getLabel(), dataElement);

    if (dataElement.isExtractable())
      addExtractableField(dataElement.getLabel(), dataElement.getPath());

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
  @JsonIgnore
  public List<DataElement> getCollectionPaths() {
    return new ArrayList(collectionPaths.values());
  }

  @Override
  @JsonIgnore
  public List<DataElement> getRootChildrenPaths() {
    return new ArrayList(directChildren.values());
  }

  @Override
  @JsonGetter("fields")
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
  @JsonGetter("groups")
  public List<FieldGroup> getFieldGroups() {
    return fieldGroups;
  }

  @Override
  @JsonIgnore
  public List<String> getNoLanguageFields() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  @JsonIgnore
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
  @JsonIgnore
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
  @JsonIgnore
  public List<RuleChecker> getRuleCheckers() {
    if (ruleCheckers == null) {
      ruleCheckers = SchemaUtils.getRuleCheckers(this);
    }
    return ruleCheckers;
  }

  @Override
  @JsonIgnore
  public List<String> getHeader() {
    List<String> headers = new ArrayList<>();
    for (DataElement dataElement : paths.values()) {
      headers.add(dataElement.getPath());
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

  public BaseSchema withNamespaces(Map<String, String> namespaces) {
    this.namespaces = namespaces;
    return this;
  }

  @Override
  public Map<String, String> getNamespaces() {
    return namespaces;
  }

  public BaseSchema addNamespace(String prefix, String uri) {
    if (namespaces == null) {
      namespaces = new LinkedHashMap<>();
    }
    namespaces.put(prefix, uri);
    return this;
  }

  @Override
  public DataElement getRecordId() {
    return recordId;
  }

  public void setRecordId(DataElement recordId) {
    this.recordId = recordId;
  }

  @Override
  public void merge(Schema other, boolean allowOverwrite) {
    for (DataElement path : other.getPaths()) {
      DataElement myDataELement = getPathByLabel(path.getLabel());
      if (myDataELement == null) {
        addField(path);
      } else {
        if (!allowOverwrite) {
          for (Rule rule : path.getRules()) {
            myDataELement.addRule(rule);
          }
        } else {
          List<String> myRules;
          for (Rule myRule : myDataELement.getRules()) {
            myRules = myRule.getRulenames();
            for (Rule rule : path.getRules()) {
              List<String> otherRules = rule.getRulenames();
              Set<String> commonKeys = myRules.stream()
                .distinct()
                .filter(otherRules::contains)
                .collect(Collectors.toSet());
              if (!commonKeys.isEmpty()) {
                for (String key : commonKeys)
                  myRule.set(key, rule.get(key));
              } else {
                myDataELement.addRule(rule);
              }
            }
          }
        }
      }
    }
  }
}
