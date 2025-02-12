package de.gwdg.metadataqa.api.util;

import de.gwdg.metadataqa.api.configuration.SchemaConfiguration;
import de.gwdg.metadataqa.api.configuration.schema.Field;
import de.gwdg.metadataqa.api.configuration.schema.Group;
import de.gwdg.metadataqa.api.configuration.schema.Rule;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SchemaFactory {

  private static final Logger LOGGER = Logger.getLogger(SchemaFactory.class.getCanonicalName());

  private SchemaFactory() {}

  public static Schema fromConfig(SchemaConfiguration config) {
    BaseSchema schema = new BaseSchema()
      .setFormat(Format.valueOf(config.getFormat().toUpperCase()));

    boolean hasCategories = config.hasCategories();
    if (hasCategories)
      schema.setCategories(config.getCategories());

    // containers used for checking duplicates
    List<String> names = new ArrayList<>();
    List<String> ruleIds = new ArrayList<>();

    for (Field field : config.getFields()) {
      String name = field.getName();
      if (names.contains(name))
        LOGGER.warning("Duplicate field name: " + name);
      names.add(name);
      var dataElement = new DataElement(name);

      if (StringUtils.isNotBlank(field.getPath()))
        dataElement.setPath(field.getPath());

      if (field.getCategories() != null) {
        List<String> categories = new ArrayList<>();
        for (String category : field.getCategories()) {
          if (hasCategories) {
            if (config.getCategories().contains(category))
              categories.add(category);
            else
              LOGGER.warning(String.format("Invalid category for field '%s': '%s'", name, category));
          } else {
            categories.add(category);
          }
        }
        dataElement.setCategories(categories);
      }

      if (field.isExtractable())
        dataElement.setExtractable();

      if (field.isInactive())
        dataElement.setActive(false);

      if (field.getRules() != null) {
        dataElement.setRule(field.getRules());
        for (Rule rule : field.getRules()) {
          if (ruleIds.contains(rule.getId()))
            LOGGER.warning("Duplicate rule id: " + rule.getId());
          ruleIds.add(rule.getId());
        }
      }

      if (StringUtils.isNotBlank(field.getIndexField()))
        dataElement.setIndexField(field.getIndexField());

      if (field.isAsLanguageTagged())
        dataElement.setAsLanguageTagged();

      schema.addField(dataElement);

      if (field.isIdentifierField())
        schema.setRecordId(dataElement);
    }

    if (config.getGroups() != null)
      for (Group group : config.getGroups())
        for (String category : group.getCategories())
          schema.addFieldGroup(new FieldGroup(category, group.getFields()));

    if (config.getNamespaces() != null)
      schema.setNamespaces(config.getNamespaces());

    schema.checkConsistency();
    return schema;
  }
}
