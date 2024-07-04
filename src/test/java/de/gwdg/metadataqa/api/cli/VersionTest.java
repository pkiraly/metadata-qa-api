package de.gwdg.metadataqa.api.cli;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VersionTest {

  private final String EXPECTED_VERSION = "0.9.5";

  @Test
  public void getVersion() {
    assertEquals(EXPECTED_VERSION, Version.getVersion());
  }

  @Test
  public void readVersionFromPropertyFile() {
    assertEquals(EXPECTED_VERSION, Version.readVersionFromPropertyFile());
  }
}