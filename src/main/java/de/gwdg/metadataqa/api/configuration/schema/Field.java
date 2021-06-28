package de.gwdg.metadataqa.api.configuration.schema;

import java.util.List;

public class Field {
  private String name;
  private String path;
  private List<String> categories;
  private boolean extractable;
  private boolean echo;
  private List<Rule> rules;
  private String indexField;

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

  public boolean isEcho() {
    return echo;
  }

  public void setEcho(boolean echo) {
    this.echo= echo;
  }


  public List<Rule> getRules() {
    return rules;
  }

  public void setRules(List<Rule> rules) {
    this.rules = rules;
  }

  public String getIndexField() {
    return indexField;
  }

  public void setIndexField(String indexField) {
    this.indexField = indexField;
  }
}
