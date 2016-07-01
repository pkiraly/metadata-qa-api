package de.gwdg.metadataqa.api.schema;

import java.util.List;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public interface ProblemCatalogSchema {

	List<String> getEmptyStringPaths();
	String getSubjectPath();
	String getTitlePath();
	String getDescriptionPath();

}
