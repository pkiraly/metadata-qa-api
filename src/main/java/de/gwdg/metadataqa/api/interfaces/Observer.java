package de.gwdg.metadataqa.api.interfaces;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.JsonPathCache;

/**
 * Implementation of the Observer design pattern
 * (<a href="https://en.wikipedia.org/wiki/Observer_pattern">https://en.wikipedia.org/wiki/Observer_pattern</a>).
 * See comments of the Observable interface.
 * @see Observable
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public interface Observer {

  /**
   * A push method which is called by the subject, and which transport
   * information to the Observer objects.
   *
   * @param cache
   *   The JSON cache object
   * @param results
   *   The result map
   */
  void update(JsonPathCache cache, FieldCounter<Double> results);

  /**
   * Get a header, which is the name of metric, the observer measures.
   * @return The header (name of metric).
   */
  String getHeader();
}
