package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.selector.Selector;

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
   *
   * @param cache The JSON cache object
   * @param results The result collector
   * @param outputType The type of output to put into the result collector
   */
  void update(Selector cache, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType);

  /**
   * Get a header, which is the name of metric, the observer measures.
   * @return The header (name of metric).
   */
  String getHeaderWithoutId();

  String getHeader();

  String getHeader(RuleCheckingOutputType outputType);

  Integer getFailureScore();

  void setFailureScore(Integer failureScore);
  RuleChecker withFailureScore(Integer failureScore);

  Integer getSuccessScore();

  void setSuccessScore(Integer successScore);
  RuleChecker withSuccessScore(Integer successScore);

  Integer getNaScore();

  void setNaScore(Integer naScore);
  RuleChecker withNaScore(Integer naScore);

  String getId();

  void setId(String id);

  void setHidden();

  boolean isHidden();

  void setDebug();
}
