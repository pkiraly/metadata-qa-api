package de.gwdg.metadataqa.api.configuration;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
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
    assertFalse(configuration.completenessCollectFields());
  }

}