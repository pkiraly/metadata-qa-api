package de.gwdg.metadataqa.api.util;

import de.gwdg.metadataqa.api.configuration.Configuration;
import de.gwdg.metadataqa.api.configuration.Field;
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

  public static Schema fromConfig(Configuration config) {
    BaseSchema schema = new BaseSchema()
      .setFormat(Format.valueOf(config.getFormat().toUpperCase()));

    boolean hasCategories = config.hasCategories();
    if (hasCategories)
      schema.setCategories(config.getCategories());

    for (Field field : config.getFields()) {
      JsonBranch branch = new JsonBranch(field.getName());

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
            /*
            try {
              Category c = Category.valueOf(category);
            } catch (IllegalArgumentException e) {
              LOGGER.warning("Invalid category: " + category);
            }
             */
            categories.add(category);
          }
        }
        branch.setCategories(categories);
      }

      if (field.isExtractable())
        branch.setExtractable();

      if (field.getRules() != null)
        branch.setRule(field.getRules());
      /*
        if (StringUtils.isNotBlank(field.getRules().getPattern()))
          branch.setPattern(field.getRules().getPattern());

      if (StringUtils.isNotBlank(field.getRules().getEquals()))
        branch.setEquals(field.getRules().getEquals());

      if (StringUtils.isNotBlank(field.getRules().getDisjoint()))
        branch.setDisjoint(field.getRules().getDisjoint());

      if (field.getRules().getIn() != null &&
          !field.getRules().getIn().isEmpty())
        branch.setIn(field.getRules().getIn());

      if (field.getRules().getMinCount() != null)
        branch.setM(field.getRules().getDisjoint());
       */

      schema.addField(branch);
    }

    return schema;
  }
}
