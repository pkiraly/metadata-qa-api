package de.gwdg.metadataqa.api.configuration.schema;

import java.util.List;

public class Field {
  private String name;
  private String path;
  private List<String> categories;
  private boolean extractable;
  private List<Rule> rules;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public List<String> getCategories() {
    return categories;
  }

  public void setCategories(List<String> categories) {
    this.categories = categories;
  }

  public boolean isExtractable() {
    return extractable;
  }

  public void setExtractable(boolean extractable) {
    this.extractable = extractable;
  }

  public List<Rule> getRules() {
    return rules;
  }

  public void setRules(List<Rule> rules) {
    this.rules = rules;
  }
}
