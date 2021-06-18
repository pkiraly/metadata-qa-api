package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;

import java.io.Serializable;

/**
 * Implementation of the Observer design pattern
 * (<a href="https://en.wikipedia.org/wiki/Observer_pattern">https://en.wikipedia.org/wiki/Observer_pattern</a>).
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public interface RuleChecker extends Serializable {

  /**
   * A push method which is called by the subject, and which transport
   * information to the Observer objects.
   *  @param cache
   *   The JSON cache object
   * @param results
   */
  void update(PathCache cache, FieldCounter<RuleCheckerOutput> results);

  /**
   * Get a header, which is the name of metric, the observer measures.
   * @return The header (name of metric).
   */
  String getHeaderWithoutId();

  String getHeader();

  Integer getFailureScore();

  void setFailureScore(Integer failureScore);

  Integer getSuccessScore();

  void setSuccessScore(Integer successScore);

  String getId();

  void setId(String id);
}
