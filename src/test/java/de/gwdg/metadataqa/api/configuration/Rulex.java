package de.gwdg.metadataqa.api.configuration;

import java.util.List;

/**
 * The purpose of this class is only to test recursion
 */
public class Rulex {

  private String equals;
  private List<String> in;
  private List<Rulex> and;

  public String getEquals() {
    return equals;
  }

  public void setEquals(String equals) {
    this.equals = equals;
  }

  public List<String> getIn() {
    return in;
  }

  public void setIn(List<String> in) {
    this.in = in;
  }

  public List<Rulex> getAnd() {
    return and;
  }

  public void setAnd(List<Rulex> and) {
    this.and = and;
  }
}
