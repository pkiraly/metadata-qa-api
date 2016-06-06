package com.nsdr.metadataqa.api.json;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public interface Branches {

	List<JsonBranch> getPaths();
	List<FieldGroup> getFieldGroups();
	List<String> getNoLanguageFields();
	Map<String, String> getSolrFields();

}
