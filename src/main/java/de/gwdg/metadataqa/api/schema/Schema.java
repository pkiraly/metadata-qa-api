package de.gwdg.metadataqa.api.schema;

import de.gwdg.metadataqa.api.configuration.schema.Rule;
import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.rule.RuleChecker;

import java.io.Serializable;
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
public interface Schema extends Serializable {

  Format getFormat();

  /**
   * Return collection paths.
   * @return List of collection paths
   */
  List<DataElement> getCollectionPaths();

  /**
   * Return the paths of root's direct children.
   * @return Paths.
   */
  List<DataElement> getRootChildrenPaths();

  /**
   * The list of fields to investigate in most of the measurements.
   * @return
   *   List of field representations
   */
  List<DataElement> getPaths();

  /**
   * Returns a path by its label.
   * @param label The label to look for.
   * @return The branch.
   */
  DataElement getPathByLabel(String label);

  /**
   * Returns a path of the record id.
   * @return The branch.
   */
  DataElement getRecordId();

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
  List<DataElement> getIndexFields();

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
   * @param path A path expression (XPath, JSONPath).
   */
  void addExtractableField(String label, String path);

  List<String> getCategories();

  List<RuleChecker> getRuleCheckers();

  default Map<String, String> getNamespaces() {
    return null;
  }

  default void checkConsistency() {
    for (DataElement path : getPaths()) {
      List<Rule> rules = path.getRules();
      if (rules != null && !rules.isEmpty()) {
        for (Rule rule : rules) {
          if (rule.getEquals() != null && getPathByLabel(rule.getEquals()) == null)
            throw new IllegalArgumentException(String.format("%s refers to a nonexistent field in 'equals: %s'", path.getLabel(), rule.getEquals()));
          if (rule.getDisjoint() != null && getPathByLabel(rule.getDisjoint()) == null)
            throw new IllegalArgumentException(String.format("%s refers to a nonexistent field in 'disjoint: %s'", path.getLabel(), rule.getDisjoint()));
          if (rule.getLessThan() != null && getPathByLabel(rule.getLessThan()) == null)
            throw new IllegalArgumentException(String.format("%s refers to a nonexistent field in 'lessThan: %s'", path.getLabel(), rule.getLessThan()));
          if (rule.getLessThanOrEquals() != null && getPathByLabel(rule.getLessThanOrEquals()) == null)
            throw new IllegalArgumentException(String.format("%s refers to a nonexistent field in 'lessThanOrEquals: %s'", path.getLabel(), rule.getLessThanOrEquals()));
        }
      }
    }
  }

  /**
   * Merge the properties of another schema to this schema
   * @param other The other schema to merge
   * @param allowOverwrite Flad to denote if overwrite of rules is allowed
   */
  void merge(Schema other, boolean allowOverwrite);
}
