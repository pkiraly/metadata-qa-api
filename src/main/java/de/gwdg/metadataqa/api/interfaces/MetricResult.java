package de.gwdg.metadataqa.api.interfaces;

import de.gwdg.metadataqa.api.util.CompressionLevel;

import java.util.List;
import java.util.Map;

public interface MetricResult {

  String getName();

  /**
   * Get results as a simple map.
   * @return A map where the keys are name of measured features (metrics),
   *   the values are the values of the metrics.
   */
  Map<String, ? extends Object> getResultMap();

  /**
   * Get hierarchical result map.
   * @return The main keys are the name of the indivitual calculators,
   *   the values are simple maps with metric name - value pairs.
   */
  Map<String, Map<String, ? extends Object>> getLabelledResultMap();

  /**
   * Get result as comma separated string.
   * @param withLabels Flag whether the individal parts should contain the
   *                   metric or not.
   * @param compressionLevel If the value is a double, it is possible to
   *                         remove the zeroes from the end, so make the
   *                         value a bit compressed.
   * @return result as comma separated string.
   */
  String getCsv(boolean withLabels, CompressionLevel compressionLevel);

  List<Object> getCsv();

  List<String> getList(boolean withLabels, CompressionLevel compressionLevel);

}
