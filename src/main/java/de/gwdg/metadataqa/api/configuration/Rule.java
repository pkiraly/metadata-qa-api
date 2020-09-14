package de.gwdg.metadataqa.api.configuration;

public class Rule {

  private String pattern;
  private String equals;
  private String disjoint;

  public String getPattern() {
    return pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public String getEquals() {
    return equals;
  }

  public void setEquals(String equals) {
    this.equals = equals;
  }

  public String getDisjoint() {
    return disjoint;
  }

  public void setDisjoint(String disjoint) {
    this.disjoint = disjoint;
  }
}
