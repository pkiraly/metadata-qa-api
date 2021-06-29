package de.gwdg.metadataqa.api.util;

import de.gwdg.metadataqa.api.configuration.SchemaConfiguration;
import de.gwdg.metadataqa.api.configuration.schema.Field;
import de.gwdg.metadataqa.api.json.JsonBranch;
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

    for (Field field : config.getFields()) {
      var branch = new JsonBranch(field.getName());

      if (StringUtils.isNotBlank(field.getPath()))
        branch.setJsonPath(field.getPath());

      if (field.getCategories() != null) {
        List<String> categories = new ArrayList<>();
        for (String category : field.getCategories()) {
          if (hasCategories) {
            if (config.getCategories().contains(category))
              categories.add(category);
            else
              LOGGER.warning(String.format("Invalid category for field '%s': '%s'",
                field.getName(), category));
          } else {
            categories.add(category);
          }
        }
        branch.setCategories(categories);
      }

      if (field.isExtractable())
        branch.setExtractable();

      if (field.isEcho())
        branch.setEcho();

      if (field.getRules() != null)
        branch.setRule(field.getRules());

      if (StringUtils.isNotBlank(field.getIndexField()))
        branch.setIndexField(field.getIndexField());

      schema.addField(branch);
    }

    if (config.getNamespaces() != null)
      schema.setNamespaces(config.getNamespaces());

    schema.checkConsistency();
    return schema;
  }
}
