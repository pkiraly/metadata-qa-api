package de.gwdg.metadataqa.api.configuration;

import java.util.List;

public class Rule {

  private String pattern;
  private String equals;
  private String disjoint;
  private List<String> in;
  private List<Rule> and;
  private Integer minCount;
  private Integer maxCount;
  private Integer minExclusive;
  private Integer minInclusive;
  private Integer maxExclusive;
  private Integer maxInclusive;
  private Integer minLength;
  private Integer maxLength;
  private Integer lessThan;
  private Integer lessThanOrEquals;
  private String hasValue;

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

  public List<String> getIn() {
    return in;
  }

  public void setIn(List<String> in) {
    this.in = in;
  }

  public List<Rule> getAnd() {
    return and;
  }

  public void setAnd(List<Rule> and) {
    this.and = and;
  }

  public Integer getMinCount() {
    return minCount;
  }

  public void setMinCount(int minCount) {
    this.minCount = minCount;
  }

  public Integer getMaxCount() {
    return maxCount;
  }

  public void setMaxCount(int maxCount) {
    this.maxCount = maxCount;
  }

  public Integer getMinExclusive() {
    return minExclusive;
  }

  public void setMinExclusive(int minExclusive) {
    this.minExclusive = minExclusive;
  }

  public Integer getMinInclusive() {
    return minInclusive;
  }

  public void setMinInclusive(int minInclusive) {
    this.minInclusive = minInclusive;
  }

  public Integer getMaxExclusive() {
    return maxExclusive;
  }

  public void setMaxExclusive(int maxExclusive) {
    this.maxExclusive = maxExclusive;
  }

  public Integer getMaxInclusive() {
    return maxInclusive;
  }

  public void setMaxInclusive(int maxInclusive) {
    this.maxInclusive = maxInclusive;
  }

  public Integer getMinLength() {
    return minLength;
  }

  public void setMinLength(int minLength) {
    this.minLength = minLength;
  }

  public Integer getMaxLength() {
    return maxLength;
  }

  public void setMaxLength(int maxLength) {
    this.maxLength = maxLength;
  }

  public Integer getLessThan() {
    return lessThan;
  }

  public void setLessThan(int lessThan) {
    this.lessThan = lessThan;
  }

  public Integer getLessThanOrEquals() {
    return lessThanOrEquals;
  }

  public void setLessThanOrEquals(int lessThanOrEquals) {
    this.lessThanOrEquals = lessThanOrEquals;
  }

  public String getHasValue() {
    return hasValue;
  }

  public void setHasValue(String hasValue) {
    this.hasValue = hasValue;
  }
}
