
package de.gwdg.metadataqa.api.json;

import de.gwdg.metadataqa.api.configuration.Rule;
import de.gwdg.metadataqa.api.model.Category;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class JsonBranch implements Cloneable, Serializable {

  private String label;
  private String jsonPath;
  private List<String> categories = new ArrayList<>();
  private String solrFieldName;
  private JsonBranch parent = null;
  private JsonBranch identifier = null;
  private List<JsonBranch> children = new ArrayList<>();
  private boolean collection = false;
  private boolean isActive = true;
  private boolean isExtractable = false;
  private boolean isMandatory = false;
  private List<Rule> rules;
  private Schema schema;

  public JsonBranch(String label, String jsonPath, String solrFieldName) {
    this.label = label;
    this.jsonPath = jsonPath;
    this.solrFieldName = solrFieldName;
  }

  public JsonBranch(String jsonPath) { // String... categories
    this.label = jsonPath;
    this.jsonPath = jsonPath;
    // setCategories(Arrays.asList(categories));
  }

  public JsonBranch(String label, String jsonPath) { // String... categories
    this.label = label;
    this.jsonPath = jsonPath;
    // setCategories(Arrays.asList(categories));
  }

  public JsonBranch(String label, JsonBranch parent, String jsonPath) { // String... categories
    this.label = label;
    this.jsonPath = jsonPath;
    // setCategories(Arrays.asList(categories));
    setParent(parent);
  }

  public String getLabel() {
    return label;
  }

  public JsonBranch setLabel(String label) {
    this.label = label;
    return this;
  }

  public String getJsonPath() {
    return jsonPath;
  }

  public JsonBranch setJsonPath(String jsonPath) {
    this.jsonPath = jsonPath;
    return this;
  }

  public String getAbsoluteJsonPath() {
    Format format = hasFormat() ? schema.getFormat() : Format.JSON;
    return getAbsoluteJsonPath(format);
  }

  public boolean hasFormat() {
    return schema != null && schema.getFormat() != null;
  }

  public String getAbsoluteJsonPath(Format format) {
    if (getParent() != null) {
      if (format.equals(Format.JSON)) {
        return getParent().getJsonPath() + getJsonPath().replace("$.", "[*]");
      } else if (format.equals(Format.XML)) {
        return getParent().getJsonPath() + "/" + getJsonPath();
      }
    }
    return getJsonPath();
  }

  public String getAbsoluteJsonPath(int i) {
    if (getParent() != null) {
      String parentPath = getParent().getJsonPath();
      String currentPath = (i == -1 || parentPath.endsWith("[0]"))
        ? getJsonPath().replace("$.", "")
        : getJsonPath().replace("$.", "[" + i + "]");
      return parentPath + currentPath;
    }
    return getJsonPath();
  }

  public List<String> getCategories() {
    return categories;
  }

  public JsonBranch setCategories(Category... categories) {
    List<String> categories2 = new ArrayList<>();
    for (Category category : categories)
      categories2.add(category.toString());
    return setCategories(categories2);
  }

  public JsonBranch setCategories(String... categories) {
    return setCategories(Arrays.asList(categories));
  }

  public JsonBranch setCategories(List<String> categories) {
    this.categories = categories;
    if (categories.contains(Category.MANDATORY.toString()))
      isMandatory = true;
    return this;
  }

  public String getSolrFieldName() {
    return solrFieldName;
  }

  public JsonBranch setSolrFieldName(String solrFieldName) {
    this.solrFieldName = solrFieldName;
    return this;
  }

  public JsonBranch getParent() {
    return parent;
  }

  public JsonBranch setParent(JsonBranch parent) {
    this.parent = parent;
    this.parent.addChild(this);
    return this;
  }

  public JsonBranch addChild(JsonBranch child) {
    if (!this.children.contains(child)) {
      this.children.add(child);
    }
    return this;
  }

  public List<JsonBranch> getChildren() {
    return children;
  }

  public JsonBranch setChildren(List<JsonBranch> children) {
    this.children = children;
    return this;
  }

  public boolean isCollection() {
    return collection;
  }

  public JsonBranch setCollection(boolean collection) {
    this.collection = collection;
    return this;
  }

  public JsonBranch getIdentifier() {
    return identifier;
  }

  public JsonBranch setIdentifier(JsonBranch identifier) {
    this.identifier = identifier;
    return this;
  }

  public boolean isActive() {
    return isActive;
  }

  public JsonBranch setActive(boolean active) {
    isActive = active;
    return this;
  }

  public boolean isExtractable() {
    return isExtractable;
  }

  public JsonBranch setExtractable() {
    isExtractable = true;
    return this;
  }

  public JsonBranch setExtractable(boolean extractable) {
    isExtractable = extractable;
    return this;
  }

  public List<Rule> getRules() {
    return rules;
  }

  public JsonBranch addRule(Rule rule) {
    if (this.rules == null)
      this.rules = new ArrayList<>();
    this.rules.add(rule);
    return this;
  }

  public JsonBranch setRule(Rule rule) {
    this.rules = Arrays.asList(rule);
    return this;
  }

  public JsonBranch setRules(List<Rule> rules) {
    this.rules = rules;
    return this;
  }

  public boolean isMandatory() {
    return isMandatory;
  }

  @Override
  public String toString() {
    return "JsonBranch{"
          + "label=" + label
          + ", jsonPath=" + jsonPath
          + ", categories=" + categories
          + ", solrFieldName=" + solrFieldName
          + ", parent=" + (parent == null ? "null" : parent.getLabel())
          + ", identifier=" + (identifier == null ? "null" : identifier.getLabel())
          + ", nr_of_children=" + children.size()
          + ", collection=" + collection
          + '}';
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    JsonBranch cloned = (JsonBranch) super.clone();

    if (children != null && !children.isEmpty()) {
      List<JsonBranch> clonedChildren = new ArrayList<JsonBranch>();
      for (JsonBranch child : children) {
        JsonBranch clonedChild = (JsonBranch) child.clone();
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

  public Schema getSchema() {
    return schema;
  }
}
