package de.gwdg.metadataqa.api.util;

import de.gwdg.metadataqa.api.configuration.Configuration;
import de.gwdg.metadataqa.api.configuration.Field;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.Category;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SchemaFactory {

  public static Schema fromConfig(Configuration config) {
    BaseSchema schema = new BaseSchema()
      .setFormat(Format.valueOf(config.getFormat().toUpperCase()));

    for (Field field : config.getFields()) {
      JsonBranch branch = new JsonBranch(field.getName());

      if (StringUtils.isNotBlank(field.getPath()))
        branch.setJsonPath(field.getPath());

      if (field.getCategories() != null) {
        List<Category> categories = new ArrayList<>();
        for (String category : field.getCategories()) {
          categories.add(Category.valueOf(category));
        }
        branch.setCategories(categories);
      }

      if (field.isExtractable())
        branch.setExtractable();

      if (field.getRules() != null)
        branch.setRules(field.getRules());
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
