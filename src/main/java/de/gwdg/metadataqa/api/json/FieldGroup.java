package de.gwdg.metadataqa.api.json;

import de.gwdg.metadataqa.api.model.Category;

import java.util.Arrays;
import java.util.List;

/**
 * Groups fields together by a category.
 *
 * The usage scenario is the following. In schemas it is possible to set rules
 * that the record should have at least one of field A or field B. In
 * completeness measure we can add plus point if that condition is met.
 *
 * For example in EDM schema "dc:title" or "dc:description" is mandatory, and
 * the record should get wrong score only if both are missing.
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class FieldGroup {

  /**
   * The list of fields as in the label of JsonBranch objects.
   */
  private List<String> fields;

  /**
   * The sub dimension or category.
   */
  private String category;

  /**
   * Construct a new FieldGroup object.
   *
   * @param sCategory
   *   A sub-dimension of completeness
   * @param sFields
   *   The field names as in the label of JsonBranch objects
   *
   * @see JsonBranch
   */
  public FieldGroup(String sCategory, List<String> sFields) {
    this.category = sCategory;
    this.fields = sFields;
  }

  public FieldGroup(Category pCategory, String... pFields) {
    this.category = pCategory.toString();
    this.fields = Arrays.asList(pFields);
  }

  /**
   * Get the list of field names.
   *
   * @return List of field names
   */
  public List<String> getFields() {
    return fields;
  }

  /**
   * Get the category.
   *
   * @return The category
   */
  public String getCategory() {
    return category;
  }

}
