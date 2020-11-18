package de.gwdg.metadataqa.api.configuration;

import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.SchemaFactory;

import java.util.List;

public class Configuration {
  private String format;
  private List<Field> fields;
  private List<Group> groups;
  private List<String> categories;

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public List<Field> getFields() {
    return fields;
  }

  public void setFields(List<Field> fields) {
    this.fields = fields;
  }

  public List<Group> getGroups() {
    return groups;
  }

  public void setGroups(List<Group> groups) {
    this.groups = groups;
  }

  public List<String> getCategories() {
    return categories;
  }

  public boolean hasCategories() {
    return categories != null && !categories.isEmpty();
  }


  public void setCategories(List<String> categories) {
    this.categories = categories;
  }

  public Schema asSchema() {
    return SchemaFactory.fromConfig(this);
  }
}
