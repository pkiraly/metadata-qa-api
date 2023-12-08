package de.gwdg.metadataqa.api.configuration;

import com.jayway.jsonpath.InvalidJsonException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ConfigurationReaderTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void readSchemaJson() {
  }

  @Test
  public void readSchemaYaml() {
  }

  @Test
  public void readMeasurementJson() throws FileNotFoundException {
    MeasurementConfiguration configuration = ConfigurationReader
      .readMeasurementJson("src/test/resources/configuration/measurement/configuration.json");

    checkMeasurementConfiguration(configuration);
  }

  @Test
  public void readNonexistentConfiguration() throws FileNotFoundException {
    thrown.expect(FileNotFoundException.class);
    thrown.expectMessage("src/test/resources/configuration/measurement/non-existent-configuration.json (No such file or directory)");

    MeasurementConfiguration configuration = ConfigurationReader
      .readMeasurementJson("src/test/resources/configuration/measurement/non-existent-configuration.json");

    fail("Should throw an exception if the JSON file is not existent.");
  }

    @Test
  public void readMeasurementYaml() throws FileNotFoundException {
    MeasurementConfiguration configuration = ConfigurationReader
      .readMeasurementYaml("src/test/resources/configuration/measurement/configuration.yaml");

    checkMeasurementConfiguration(configuration);
  }

  private void checkMeasurementConfiguration(MeasurementConfiguration configuration) {
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
    assertFalse(configuration.isCompletenessFieldCollectingEnabled());
    assertFalse(configuration.isSaturationExtendedResult());
    assertFalse(configuration.isCheckSkippableCollections());
  }
}