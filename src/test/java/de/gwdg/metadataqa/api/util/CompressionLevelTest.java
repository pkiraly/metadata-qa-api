package de.gwdg.metadataqa.api.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class CompressionLevelTest {

  @Test
  public void all() {
    assertEquals(3, CompressionLevel.values().length);
    assertEquals(0, CompressionLevel.ZERO.value());
    assertEquals(1, CompressionLevel.NORMAL.value());
    assertEquals(2, CompressionLevel.WITHOUT_TRAILING_ZEROS.value());
  }
}