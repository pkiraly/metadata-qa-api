package de.gwdg.metadataqa.api.configuration.schema;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Pattern;

public class MQAFPattern {
  List<String> pattern;
  Pattern compiledPattern;
  ApplicationScope scope = ApplicationScope.anyOf;

  public MQAFPattern() {
  }

  public List<String> getPattern() {
    return pattern;
  }

  public void setPattern(List<String> pattern) {
    this.pattern = pattern;
    this.compiledPattern = Pattern.compile(StringUtils.join(pattern, "|"));
  }

  public MQAFPattern withPattern(List<String> pattern) {
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

  public Pattern getCompiledPattern() {
    return compiledPattern;
  }
}
