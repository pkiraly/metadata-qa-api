package de.gwdg.metadataqa.api.configuration.schema;

import java.util.List;

public class Group {
  private List<String> fields;
  private List<String> categories;

  public List<String> getFields() {
    return fields;
  }

  public void setFields(List<String> fields) {
    this.fields = fields;
  }

  public List<String> getCategories() {
    return categories;
  }

  public void setCategories(List<String> categories) {
    this.categories = categories;
  }
}
