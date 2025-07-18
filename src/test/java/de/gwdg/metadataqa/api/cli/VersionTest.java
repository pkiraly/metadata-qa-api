package de.gwdg.metadataqa.api.cli;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VersionTest {

  private static final String EXPECTED_VERSION = "0.9.9-SNAPSHOT";

  @Test
  public void getVersion() {
    assertEquals(EXPECTED_VERSION, Version.getVersion());
  }

  @Test
  public void readVersionFromPropertyFile() {
    assertEquals(EXPECTED_VERSION, Version.readVersionFromPropertyFile());
  }
}