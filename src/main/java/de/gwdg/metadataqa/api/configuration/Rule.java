package de.gwdg.metadataqa.api.configuration;

import java.util.List;

public class Rule {

  private String pattern;
  private String equals;
  private String disjoint;
  private List<String> in;
  private List<Rule> and;
  private List<Rule> or;
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

  public Rule withPattern(String pattern) {
    setPattern(pattern);
    return this;
  }

  public String getEquals() {
    return equals;
  }

  public void setEquals(String equals) {
    this.equals = equals;
  }

  public Rule withEquals(String equals) {
    setEquals(equals);
    return this;
  }

  public String getDisjoint() {
    return disjoint;
  }

  public void setDisjoint(String disjoint) {
    this.disjoint = disjoint;
  }

  public Rule withDisjoint(String disjoint) {
    setDisjoint(disjoint);
    return this;
  }

  public List<String> getIn() {
    return in;
  }

  public void setIn(List<String> in) {
    this.in = in;
  }

  public Rule withIn(List<String> in) {
    setIn(in);
    return this;
  }

  public List<Rule> getAnd() {
    return and;
  }

  public void setAnd(List<Rule> and) {
    this.and = and;
  }

  public Rule withAnd(List<Rule> and) {
    setAnd(and);
    return this;
  }

  public List<Rule> getOr() {
    return or;
  }

  public void setOr(List<Rule> or) {
    this.or = or;
  }

  public Rule withOr(List<Rule> or) {
    setOr(or);
    return this;
  }

  public Integer getMinCount() {
    return minCount;
  }

  public void setMinCount(int minCount) {
    this.minCount = minCount;
  }

  public Rule withMinCount(int minCount) {
    setMinCount(minCount);
    return this;
  }

  public Integer getMaxCount() {
    return maxCount;
  }

  public void setMaxCount(int maxCount) {
    this.maxCount = maxCount;
  }

  public Rule withMaxCount(int maxCount) {
    setMaxCount(maxCount);
    return this;
  }

  public Integer getMinExclusive() {
    return minExclusive;
  }

  public void setMinExclusive(int minExclusive) {
    this.minExclusive = minExclusive;
  }

  public Rule withMinExclusive(int minExclusive) {
    setMinExclusive(minExclusive);
    return this;
  }

  public Integer getMinInclusive() {
    return minInclusive;
  }

  public void setMinInclusive(int minInclusive) {
    this.minInclusive = minInclusive;
  }

  public Rule withMinInclusive(int minInclusive) {
    setMinInclusive(minInclusive);
    return this;
  }

  public Integer getMaxExclusive() {
    return maxExclusive;
  }

  public void setMaxExclusive(int maxExclusive) {
    this.maxExclusive = maxExclusive;
  }

  public Rule withMaxExclusive(int maxExclusive) {
    setMaxExclusive(maxExclusive);
    return this;
  }

  public Integer getMaxInclusive() {
    return maxInclusive;
  }

  public void setMaxInclusive(int maxInclusive) {
    this.maxInclusive = maxInclusive;
  }

  public Rule withMaxInclusive(int maxInclusive) {
    setMaxInclusive(maxInclusive);
    return this;
  }

  public Integer getMinLength() {
    return minLength;
  }

  public void setMinLength(int minLength) {
    this.minLength = minLength;
  }

  public Rule withMinLength(int minLength) {
    setMinLength(minLength);
    return this;
  }

  public Integer getMaxLength() {
    return maxLength;
  }

  public void setMaxLength(int maxLength) {
    this.maxLength = maxLength;
  }

  public Rule withMaxLength(int maxLength) {
    setMaxLength(maxLength);
    return this;
  }

  public Integer getLessThan() {
    return lessThan;
  }

  public void setLessThan(int lessThan) {
    this.lessThan = lessThan;
  }

  public Rule withLessThan(int lessThan) {
    setLessThan(lessThan);
    return this;
  }

  public Integer getLessThanOrEquals() {
    return lessThanOrEquals;
  }

  public void setLessThanOrEquals(int lessThanOrEquals) {
    this.lessThanOrEquals = lessThanOrEquals;
  }

  public Rule withLessThanOrEquals(int lessThanOrEquals) {
    setLessThanOrEquals(lessThanOrEquals);
    return this;
  }

  public String getHasValue() {
    return hasValue;
  }

  public void setHasValue(String hasValue) {
    this.hasValue = hasValue;
  }

  public Rule withHasValue(String hasValue) {
    setHasValue(hasValue);
    return this;
  }
}
