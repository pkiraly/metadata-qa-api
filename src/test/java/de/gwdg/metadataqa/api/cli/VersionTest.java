package de.gwdg.metadataqa.api.cli;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VersionTest {

  @Test
  public void getVersion() {
    assertEquals("0.9.0", Version.getVersion());
  }

  @Test
  public void readVersionFromPropertyFile() {
    assertEquals("0.9.0", Version.readVersionFromPropertyFile());
  }
}