
package de.gwdg.metadataqa.api.json;

import de.gwdg.metadataqa.api.model.Category;
import de.gwdg.metadataqa.api.schema.Format;

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
  private List<Category> categories;
  private String solrFieldName;
  private JsonBranch parent = null;
  private JsonBranch identifier = null;
  private List<JsonBranch> children = new ArrayList<>();
  private boolean collection = false;
  private boolean isActive = true;

  public JsonBranch(String label, String jsonPath, String solrFieldName) {
    this.label = label;
    this.jsonPath = jsonPath;
    this.solrFieldName = solrFieldName;
  }

  public JsonBranch(String label, String jsonPath, Category... categories) {
    this.label = label;
    this.jsonPath = jsonPath;
    this.categories = Arrays.asList(categories);
  }

  public JsonBranch(String label, JsonBranch parent, String jsonPath, Category... categories) {
    this.label = label;
    this.jsonPath = jsonPath;
    this.categories = Arrays.asList(categories);
    setParent(parent);
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getJsonPath() {
    return jsonPath;
  }

  public void setJsonPath(String jsonPath) {
    this.jsonPath = jsonPath;
  }

  public String getAbsoluteJsonPath() {
    return getAbsoluteJsonPath(Format.JSON);
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
      String currentPath = (i == -1)
        ? getJsonPath().replace("$.", "")
        : getJsonPath().replace("$.", "[" + i + "]");
      return parentPath + currentPath;
    }
    return getJsonPath();
  }

  public List<Category> getCategories() {
    return categories;
  }

  public void setCategories(List<Category> categories) {
    this.categories = categories;
  }

  public String getSolrFieldName() {
    return solrFieldName;
  }

  public void setSolrFieldName(String solrFieldName) {
    this.solrFieldName = solrFieldName;
  }

  public JsonBranch getParent() {
    return parent;
  }

  public void setParent(JsonBranch parent) {
    this.parent = parent;
    this.parent.addChild(this);
  }

  public void addChild(JsonBranch child) {
    if (!this.children.contains(child)) {
      this.children.add(child);
    }
  }

  public List<JsonBranch> getChildren() {
    return children;
  }

  public void setChildren(List<JsonBranch> children) {
    this.children = children;
  }

  public boolean isCollection() {
    return collection;
  }

  public void setCollection(boolean collection) {
    this.collection = collection;
  }

  public JsonBranch getIdentifier() {
    return identifier;
  }

  public void setIdentifier(JsonBranch identifier) {
    this.identifier = identifier;
  }

  public boolean isActive() {
    return isActive;
  }

  public JsonBranch setActive(boolean active) {
    isActive = active;
    return this;
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
          + ", nr of children=" + children.size()
          + ", collection=" + collection
          + '}';
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    JsonBranch cloned = (JsonBranch) super.clone();

    if (children != null && children.size() > 0) {
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
}
