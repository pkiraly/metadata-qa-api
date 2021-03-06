package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;

/**
 * Implementation of the Observer design pattern
 * (<a href="https://en.wikipedia.org/wiki/Observer_pattern">https://en.wikipedia.org/wiki/Observer_pattern</a>).
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public interface RuleChecker {

  /**
   * A push method which is called by the subject, and which transport
   * information to the Observer objects.
   *
   * @param cache
   *   The JSON cache object
   * @param results
   *   The result map
   */
  void update(PathCache cache, FieldCounter<RuleCheckingOutput> results);

  /**
   * Get a header, which is the name of metric, the observer measures.
   * @return The header (name of metric).
   */
  String getHeader();
}
