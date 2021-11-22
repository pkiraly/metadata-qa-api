package de.gwdg.metadataqa.api.interfaces;

import de.gwdg.metadataqa.api.model.pathcache.PathCache;

import java.io.Serializable;
import java.util.List;

/**
 * Calculator does the actual measurements.
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public interface Calculator extends Serializable {

  /**
   * Measure something from the JSON input object.
   * @param cache
   *   The JSON cache object
   */
  List<MetricResult> measure(PathCache cache);

  /**
   * Get header (the name of metrics) as a list.
   * @return The list of metrics.
   */
  List<String> getHeader();

  /**
   * Get the name of the calculator.
   * @return The name of the calculator
   */
  String getCalculatorName();
}
