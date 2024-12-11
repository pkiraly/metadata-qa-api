package de.gwdg.metadataqa.api.json;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import de.gwdg.metadataqa.api.configuration.schema.Rule;
import de.gwdg.metadataqa.api.model.Category;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A data element of a schema
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataElement implements Cloneable, Serializable {

  private String label;
  private String path;
  private List<String> categories = new ArrayList<>();
  private String solrFieldName;
  private DataElement parent = null;
  private DataElement identifier = null;
  private List<DataElement> children = new ArrayList<>();
  private boolean collection = false;
  private boolean isActive = true;
  private boolean isExtractable = false;
  private boolean isMandatory = false;
  private List<Rule> rules;
  private Schema schema;
  private String indexField;
  private boolean asLanguageTagged = false;

  public DataElement(String label, String path, String solrFieldName) {
    this.label = label;
    this.path = path;
    this.solrFieldName = solrFieldName;
  }

  public DataElement(String path) { // String... categories
    this.label = path;
    this.path = path;
    // setCategories(Arrays.asList(categories));
  }

  public DataElement(String label, String path) { // String... categories
    this.label = label;
    this.path = path;
    // setCategories(Arrays.asList(categories));
  }

  public DataElement(String label, DataElement parent, String path) { // String... categories
    this.label = label;
    this.path = path;
    // setCategories(Arrays.asList(categories));
    setParent(parent);
  }

  @JsonGetter("name")
  public String getLabel() {
    return label;
  }

  public DataElement setLabel(String label) {
    this.label = label;
    return this;
  }

  public String getPath() {
    return path;
  }

  public DataElement setPath(String path) {
    this.path = path;
    return this;
  }

  @JsonIgnore
  public String getAbsolutePath() {
    Format format = hasFormat() ? schema.getFormat() : Format.JSON;
    return getAbsolutePath(format);
  }

  public boolean hasFormat() {
    return schema != null && schema.getFormat() != null;
  }

  public String getAbsolutePath(Format format) {
    if (getParent() != null) {
      if (format.equals(Format.JSON)) {
        return getParent().getPath() + getPath().replace("$.", "[*]");
      } else if (format.equals(Format.XML)) {
        return getParent().getPath() + "/" + getPath();
      }
    }
    return getPath();
  }

  public String getAbsolutePath(int i) {
    if (getParent() != null) {
      String parentPath = getParent().getPath();
      String currentPath = (i == -1 || parentPath.endsWith("[0]"))
        ? getPath().replace("$.", "")
        : getPath().replace("$.", "[" + i + "]");
      return parentPath + currentPath;
    }
    return getPath();
  }

  public List<String> getCategories() {
    return categories;
  }

  public DataElement setCategories(Category... categories) {
    List<String> categories2 = new ArrayList<>();
    for (Category category : categories)
      categories2.add(category.toString());
    return setCategories(categories2);
  }

  public DataElement setCategories(String... categories) {
    return setCategories(Arrays.asList(categories));
  }

  public DataElement setCategories(List<String> categories) {
    this.categories = categories;
    if (categories.contains(Category.MANDATORY.toString()))
      isMandatory = true;
    return this;
  }

  public String getSolrFieldName() {
    return solrFieldName;
  }

  public DataElement setSolrFieldName(String solrFieldName) {
    this.solrFieldName = solrFieldName;
    return this;
  }

  public DataElement getParent() {
    return parent;
  }

  public DataElement setParent(DataElement parent) {
    this.parent = parent;
    this.parent.addChild(this);
    return this;
  }

  public DataElement addChild(DataElement child) {
    if (!this.children.contains(child)) {
      this.children.add(child);
    }
    return this;
  }

  @JsonIgnore
  public List<DataElement> getChildren() {
    return children;
  }

  public DataElement setChildren(List<DataElement> children) {
    this.children = children;
    return this;
  }

  @JsonIgnore
  public boolean isCollection() {
    return collection;
  }

  public DataElement setCollection(boolean collection) {
    this.collection = collection;
    return this;
  }

  public DataElement getIdentifier() {
    return identifier;
  }

  public DataElement setIdentifier(DataElement identifier) {
    this.identifier = identifier;
    return this;
  }

  @JsonIgnore
  public boolean isActive() {
    return isActive;
  }

  public DataElement setActive(boolean active) {
    isActive = active;
    return this;
  }

  public boolean isExtractable() {
    return isExtractable;
  }

  public DataElement setExtractable() {
    isExtractable = true;
    return this;
  }

  public DataElement setExtractable(boolean extractable) {
    isExtractable = extractable;
    return this;
  }

  public List<Rule> getRules() {
    return rules;
  }

  public DataElement addRule(Rule rule) {
    if (this.rules == null)
      this.rules = new ArrayList<>();
    this.rules.add(rule);
    if (rule.getMinCount() != null && rule.getMinCount() >= 1) {
      isMandatory = true;
    }
    return this;
  }

  public DataElement setRule(List<Rule> rules) {
    this.rules = rules;
    for (Rule rule : rules) {
      if (rule.getMinCount() != null && rule.getMinCount() >= 1) {
        isMandatory = true;
      }
    }
    return this;
  }

  public DataElement setRules(List<Rule> rules) {
    this.rules = rules;
    return this;
  }

  @JsonIgnore
  public boolean isMandatory() {
    return isMandatory;
  }

  @Override
  public String toString() {
    return "DataElement{"
          + "label=" + label
          + ", path=" + path
          + ", categories=" + categories
          + ", solrFieldName=" + solrFieldName
          + ", parent=" + (parent == null ? "null" : parent.getLabel())
          + ", identifier=" + (identifier == null ? "null" : identifier.getLabel())
          + ", nr_of_children=" + children.size()
          + ", collection=" + collection
          + '}';
  }

  public static DataElement copy(DataElement other) throws CloneNotSupportedException {
    DataElement cloned = SerializationUtils.clone(other);

    if (other.children != null && !other.children.isEmpty()) {
      List<DataElement> clonedChildren = new ArrayList<>();
      for (DataElement child : other.children) {
        DataElement clonedChild = SerializationUtils.clone(child);
        clonedChild.parent = cloned;
        clonedChildren.add(clonedChild);
      }
      cloned.children = clonedChildren;
    }

    return cloned;
  }

  public void setSchema(Schema schema) {
    this.schema = schema;
  }

  @JsonIgnore
  public Schema getSchema() {
    return schema;
  }

  public String getIndexField() {
      return indexField;
  }

  public DataElement setIndexField(String indexField) {
    this.indexField = indexField;
    return this;
  }

  public String generateIndexField() {
    return label.replaceAll("\\W", "_");
  }

  public DataElement setAsLanguageTagged() {
    this.asLanguageTagged = true;
    return this;
  }

  public boolean isAsLanguageTagged() {
    return asLanguageTagged;
  }
}
