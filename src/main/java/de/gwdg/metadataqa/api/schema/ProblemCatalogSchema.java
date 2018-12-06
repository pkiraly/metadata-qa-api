package de.gwdg.metadataqa.api.schema;

import java.util.List;

/**
 * An interface for problem catalog aware schema.
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public interface ProblemCatalogSchema {

  /**
   * Get path of empty strings.
   * @return
   */
  List<String> getEmptyStringPaths();

  /**
   * Get the subject path.
   * @return
   */
  String getSubjectPath();

  /**
   * Get the title path.
   * @return
   */
  String getTitlePath();

  /**
   * Get the description path.
   * @return
   */
  String getDescriptionPath();

}
