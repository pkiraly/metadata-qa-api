package de.gwdg.metadataqa.api.configuration.schema;

import java.util.regex.Pattern;

public class MQAFPattern {
  String pattern;
  Pattern compitedPattern;
  ApplicationScope scope;

  public MQAFPattern() {
  }

  public String getPattern() {
    return pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
    this.compitedPattern = Pattern.compile(pattern);
  }

  public MQAFPattern withPattern(String pattern) {
    setPattern(pattern);
    return this;
  }

  public ApplicationScope getScope() {
    return scope;
  }

  public void setScope(ApplicationScope scope) {
    this.scope = scope;
  }

  public MQAFPattern withScope(ApplicationScope scope) {
    this.scope = scope;
    return this;
  }

  public Pattern getCompitedPattern() {
    return compitedPattern;
  }
}
