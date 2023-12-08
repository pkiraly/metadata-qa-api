package de.gwdg.metadataqa.api.model;

import junit.framework.TestCase;

public class LanguageSaturationTypeTest extends TestCase {

  public void testIsTaggedLiteral() {
    assertTrue(LanguageSaturationType.LANGUAGE.isTaggedLiteral());
    assertTrue(LanguageSaturationType.TRANSLATION.isTaggedLiteral());
    assertFalse(LanguageSaturationType.NA.isTaggedLiteral());
    assertFalse(LanguageSaturationType.LINK.isTaggedLiteral());
    assertFalse(LanguageSaturationType.STRING.isTaggedLiteral());
  }
}