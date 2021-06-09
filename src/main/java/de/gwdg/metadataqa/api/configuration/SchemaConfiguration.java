package de.gwdg.metadataqa.api.configuration;

import de.gwdg.metadataqa.api.configuration.schema.Field;
import de.gwdg.metadataqa.api.configuration.schema.Group;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.SchemaFactory;

import java.util.List;
import java.util.Map;

public class SchemaConfiguration {
  private String format;
  private List<Field> fields;
  private List<Group> groups;
  private List<String> categories;
  private Map<String, String> namespaces;

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

  public Map<String, String> getNamespaces() {
    return namespaces;
  }

  public void setNamespaces(Map<String, String> namespaces) {
    this.namespaces = namespaces;
  }
}
