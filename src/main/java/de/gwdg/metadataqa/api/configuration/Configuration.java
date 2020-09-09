package de.gwdg.metadataqa.api.configuration;

import java.util.List;

public class Configuration {
  private String format;
  private List<Field> fields;
  private List<Group> groups;

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
}
