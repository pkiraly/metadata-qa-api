package de.gwdg.metadataqa.api.configuration;

import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ConfigurationReaderTest {

  @Test
  public void readSchemaJson() {
  }

  @Test
  public void readSchemaYaml() {
  }

  @Test
  public void readMeasurementYaml() throws FileNotFoundException {
    MeasurementConfiguration configuration = ConfigurationReader
      .readMeasurementYaml("src/test/resources/configuration/measurement/configuration.yaml");
    assertNotNull(configuration);
    assertFalse(configuration.isFieldExtractorEnabled());
    assertTrue(configuration.isFieldExistenceMeasurementEnabled());
    assertTrue(configuration.isFieldCardinalityMeasurementEnabled());
    assertTrue(configuration.isCompletenessMeasurementEnabled());
    assertFalse(configuration.isTfIdfMeasurementEnabled());
    assertFalse(configuration.isProblemCatalogMeasurementEnabled());
    assertFalse(configuration.isRuleCatalogMeasurementEnabled());
    assertFalse(configuration.isLanguageMeasurementEnabled());
    assertFalse(configuration.isMultilingualSaturationMeasurementEnabled());
    assertFalse(configuration.collectTfIdfTerms());
    assertFalse(configuration.isUniquenessMeasurementEnabled());
    assertFalse(configuration.completenessCollectFields());
    assertFalse(configuration.isSaturationExtendedResult());
    assertFalse(configuration.isCheckSkippableCollections());
  }
}