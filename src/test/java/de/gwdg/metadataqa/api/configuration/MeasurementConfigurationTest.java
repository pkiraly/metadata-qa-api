package de.gwdg.metadataqa.api.configuration;

import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.uniqueness.SolrClient;
import de.gwdg.metadataqa.api.uniqueness.SolrClientMock;
import de.gwdg.metadataqa.api.uniqueness.SolrConfiguration;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MeasurementConfigurationTest {

  @Test
  public void emptyConstructor() {
    MeasurementConfiguration configuration = new MeasurementConfiguration();

    assertTrue(configuration.isFieldExistenceMeasurementEnabled());
    assertTrue(configuration.isFieldCardinalityMeasurementEnabled());
    assertTrue(configuration.isCompletenessMeasurementEnabled());
    assertFalse(configuration.isTfIdfMeasurementEnabled());
    assertFalse(configuration.isProblemCatalogMeasurementEnabled());
    assertFalse(configuration.isLanguageMeasurementEnabled());
    assertFalse(configuration.collectTfIdfTerms());
    assertFalse(configuration.isCompletenessFieldCollectingEnabled());
  }

  @Test
  public void enableFieldExistenceMeasurement() {
    MeasurementConfiguration conf = new MeasurementConfiguration().enableFieldExistenceMeasurement();
    assertTrue(conf.isFieldExistenceMeasurementEnabled());
  }

  @Test
  public void enableLanguageMeasurement() {
    MeasurementConfiguration conf = new MeasurementConfiguration().enableLanguageMeasurement();
    assertTrue(conf.isLanguageMeasurementEnabled());
  }

  @Test
  public void disableLanguageMeasurement() {
    MeasurementConfiguration conf = new MeasurementConfiguration().disableLanguageMeasurement();
    assertFalse(conf.isLanguageMeasurementEnabled());
  }

  @Test
  public void enableLanguageMeasurement_boolean() {
    MeasurementConfiguration conf = new MeasurementConfiguration().enableLanguageMeasurement(true);
    assertTrue(conf.isLanguageMeasurementEnabled());
  }

  @Test
  public void enableMultilingualSaturationMeasurement() {
    MeasurementConfiguration conf = new MeasurementConfiguration()
      .enableMultilingualSaturationMeasurement();
    assertTrue(conf.isMultilingualSaturationMeasurementEnabled());
  }

  @Test
  public void enableMultilingualSaturationMeasurement_boolean() {
    MeasurementConfiguration conf = new MeasurementConfiguration()
      .enableMultilingualSaturationMeasurement(true);
    assertTrue(conf.isMultilingualSaturationMeasurementEnabled());
  }

  @Test
  public void disableMultilingualSaturationMeasurement() {
    MeasurementConfiguration conf = new MeasurementConfiguration()
      .disableMultilingualSaturationMeasurement();
    assertFalse(conf.isMultilingualSaturationMeasurementEnabled());
  }

  @Test
  public void enableProblemCatalogMeasurement() {
    MeasurementConfiguration conf = new MeasurementConfiguration()
      .enableProblemCatalogMeasurement();
    assertTrue(conf.isProblemCatalogMeasurementEnabled());
  }

  @Test
  public void enableProblemCatalogMeasurement_boolean() {
    MeasurementConfiguration conf = new MeasurementConfiguration()
      .enableProblemCatalogMeasurement(true);
    assertTrue(conf.isProblemCatalogMeasurementEnabled());
  }

  @Test
  public void disableProblemCatalogMeasurement() {
    MeasurementConfiguration conf = new MeasurementConfiguration()
      .disableProblemCatalogMeasurement();
    assertFalse(conf.isProblemCatalogMeasurementEnabled());
  }

  @Test
  public void disableRuleCatalogMeasurement() {
    MeasurementConfiguration conf = new MeasurementConfiguration()
      .disableRuleCatalogMeasurement();
    assertFalse(conf.isRuleCatalogMeasurementEnabled());
  }

  @Test
  public void disableUniquenessMeasurement() {
    MeasurementConfiguration conf = new MeasurementConfiguration().disableUniquenessMeasurement();
    assertFalse(conf.isUniquenessMeasurementEnabled());
  }

  @Test
  public void collectTfIdfTerms() {
    MeasurementConfiguration conf = new MeasurementConfiguration().collectTfIdfTerms(true);
    assertTrue(conf.collectTfIdfTerms());
  }

  @Test
  public void completenessCollectFields() {
    MeasurementConfiguration conf = new MeasurementConfiguration().enableCompletenessFieldCollecting(true);
    assertTrue(conf.isCompletenessFieldCollectingEnabled());
  }

  @Test
  public void enableSaturationExtendedResult() {
    MeasurementConfiguration conf = new MeasurementConfiguration().enableSaturationExtendedResult(true);
    assertTrue(conf.isSaturationExtendedResult());
  }

  @Test
  public void enableCheckSkippableCollections() {
    MeasurementConfiguration conf = new MeasurementConfiguration().enableCheckSkippableCollections(true);
    assertTrue(conf.isCheckSkippableCollections());
  }

  @Test
  public void withSolrConfiguration() {
    MeasurementConfiguration conf = new MeasurementConfiguration().withSolrConfiguration("localhost", "8983", "solr");
    assertEquals("localhost", conf.getSolrHost());
    assertEquals("8983", conf.getSolrPort());
    assertEquals("solr", conf.getSolrPath());
  }

  @Test
  public void withSolrClient() {
    SolrClient solrClient = new SolrClientMock(
      new SolrConfiguration("localhost", "8983", "solr")
    );
    MeasurementConfiguration conf = new MeasurementConfiguration().withSolrClient(solrClient);
    assertNotNull(conf.getSolrClient());
  }

  @Test
  public void withRuleCheckingOutputType() {
    MeasurementConfiguration conf = new MeasurementConfiguration().withRuleCheckingOutputType(RuleCheckingOutputType.BOTH);
    assertNotNull(conf.getRuleCheckingOutputType());
    assertEquals(RuleCheckingOutputType.BOTH, conf.getRuleCheckingOutputType());
  }

  @Test
  public void withAnnotationColumns_string() {
    MeasurementConfiguration conf = new MeasurementConfiguration().withAnnotationColumns("{\"file\":\"test\"}");
    assertNotNull(conf.getAnnottaionColumns());
    assertEquals(1, conf.getAnnottaionColumns().size());
    assertEquals("file", conf.getAnnottaionColumns().keySet().toArray()[0]);
    assertEquals("test", conf.getAnnottaionColumns().get("file"));
  }

  @Test
  public void withAnnotationColumns_int() {
    MeasurementConfiguration conf = new MeasurementConfiguration().withAnnotationColumns("{\"file\":1}");
    assertNotNull(conf.getAnnottaionColumns());
    assertEquals(1, conf.getAnnottaionColumns().size());
    assertEquals("file", conf.getAnnottaionColumns().keySet().toArray()[0]);
    assertEquals(1, conf.getAnnottaionColumns().get("file"));
  }

  @Test
  public void withAnnotationColumns_float() {
    MeasurementConfiguration conf = new MeasurementConfiguration().withAnnotationColumns("{\"file\":1.0}");
    assertNotNull(conf.getAnnottaionColumns());
    assertEquals(1, conf.getAnnottaionColumns().size());
    assertEquals("file", conf.getAnnottaionColumns().keySet().toArray()[0]);
    assertEquals(1.0, conf.getAnnottaionColumns().get("file"));
  }
}