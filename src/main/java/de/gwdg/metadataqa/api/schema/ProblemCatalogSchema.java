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
   * @return List of empty string paths
   */
  List<String> getEmptyStringPaths();

  /**
   * Get the subject path.
   * @return The subject path
   */
  String getSubjectPath();

  /**
   * Get the title path.
   * @return The title path
   */
  String getTitlePath();

  /**
   * Get the description path.
   * @return The description path
   */
  String getDescriptionPath();

}
