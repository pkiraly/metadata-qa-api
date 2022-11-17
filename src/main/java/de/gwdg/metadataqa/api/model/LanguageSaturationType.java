package de.gwdg.metadataqa.api.model;

/**
 * Types for language saturation
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public enum LanguageSaturationType {

  NA(-1.0),
  STRING(0.0),
  LANGUAGE(1.0),
  TRANSLATION(2.0),
  LINK(3.0);

  private final double value;

  LanguageSaturationType(double value) {
    this.value = value;
  }

  public boolean isTaggedLiteral() {
    return (this.equals(LANGUAGE) || this.equals(TRANSLATION));
  }

  public double value() {
    return value;
  }
}
