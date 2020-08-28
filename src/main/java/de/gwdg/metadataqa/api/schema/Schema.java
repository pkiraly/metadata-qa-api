package de.gwdg.metadataqa.api.schema;

import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.Category;

import java.util.List;
import java.util.Map;

/**
 * The representation of a metadata schema.
 *
 * It does not cover all the fine details of a schema such as an XML Schema,
 * it concentrates only on those elements which take role in the measurement.
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public interface Schema {

  Format getFormat();

  /**
   * Return collection paths.
   * @return List of collection paths
   */
  List<JsonBranch> getCollectionPaths();

  /**
   * Return the paths of root's direct children.
   * @return Paths.
   */
  List<JsonBranch> getRootChildrenPaths();

  /**
   * The list of fields to investigate in most of the measurements.
   * @return
   *   List of field representations
   */
  List<JsonBranch> getPaths();

  /**
   * Returns a path by its label.
   * @param label The label to look for.
   * @return The branch.
   */
  JsonBranch getPathByLabel(String label);

  /**
   * Field groups used in completeness sub-dimensions.
   * Groupped values are optional elements in a sub-dimension.
   * @return
   *   List of groups
   */
  List<FieldGroup> getFieldGroups();

  /**
   * List of field which can be skipped in the language extraction.
   * @return
   *   List of skippable fields
   */
  List<String> getNoLanguageFields();

  /**
   * Map of fields covered in TF-IDF extraction.
   * The key should be qualified name, the value should be the Solr field name.
   * For example:
   * <code>
   * solrFields.put("Proxy/dc:title", "dc_title_txt");
   * </code>
   * @return
   *    The map of fields
   */
  Map<String, String> getSolrFields();

  /**
   * Get fields for which the values should be extracted from the records.
   * @return The map of extractable fields. The key is the label of a field,
   *   the value is a JSON path expression.
   */
  Map<String, String> getExtractableFields();

  /**
   * Set the extractable fields.
   * @see #getExtractableFields
   * @param extractableFields The extractable fields.
   */
  void setExtractableFields(Map<String, String> extractableFields);

  /**
   * Add a single field to the map of extractable fields.
   * @param label The label of the field.
   * @param jsonPath JSON path expression.
   */
  void addExtractableField(String label, String jsonPath);

  List<Category> getCategories();
}
