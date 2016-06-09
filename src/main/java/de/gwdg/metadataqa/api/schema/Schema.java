package de.gwdg.metadataqa.api.schema;

import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.JsonBranch;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public interface Schema {

	List<JsonBranch> getPaths();
	List<FieldGroup> getFieldGroups();
	List<String> getNoLanguageFields();
	Map<String, String> getSolrFields();

}
