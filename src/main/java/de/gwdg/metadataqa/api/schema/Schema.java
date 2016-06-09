package de.gwdg.metadataqa.api.schema;

import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.JsonBranch;
import java.util.List;
import java.util.Map;

/**
 * The representation of a metadata schema. It does not cover all the fine details
 * of a schema such as an XML Schema, it concentrates only on those elements which
 * take role in the measurement.
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public interface Schema {

	/**
	 * The list of fields to investigate in most of the measurements
	 * @return
	 *   List of field representations
	 */
	List<JsonBranch> getPaths();

	/**
	 * Field groups used in completenes sub-dimensions.
	 * Groupped values are optional elements in a sub-dimension.
	 * @return
	 *   List of groups
	 */
	List<FieldGroup> getFieldGroups();

	/**
	 * List of field which can be skipped in the language extraction
	 * @return
	 *   List of skippable fields
	 */
	List<String> getNoLanguageFields();

	/**
	 * Map of fields covered in TF-IDF extraction.
	 * The key should be qualified name, the value should be the Solr field name.
	 * For example:
	 * <code>
	 * solrFields.put("dc:title", "dc_title_txt");
	 * </code>
	 * @return
	 *    The map of fields
	 */
	Map<String, String> getSolrFields();

}
