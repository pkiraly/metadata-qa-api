package de.gwdg.metadataqa.api.util;

import junit.framework.TestCase;

public class ExceptionUtilsTest extends TestCase {

  public void testExtractRelevantPath() {
    assertEquals(
      "de.gwdg.metadataqa.api.util.ExceptionUtilsTest.testExtractRelevantPath(ExceptionUtilsTest.java:8)",
      ExceptionUtils.extractRelevantPath(new IllegalArgumentException("bad argument"))
    );
  }
}