package de.gwdg.metadataqa.api.configuration.schema;

import de.gwdg.metadataqa.api.util.Dimension;

import java.io.Serializable;
import java.util.List;

public class Rule implements Serializable {

  private static final long serialVersionUID = 4101184421853217836L;

  private String id;
  private String description;
  private Integer failureScore;
  private Integer successScore;

  private String pattern;
  private String equals;
  private String disjoint;
  private List<String> in;
  private List<Rule> and;
  private List<Rule> or;
  private List<Rule> not;
  private Integer minCount;
  private Integer maxCount;
  private Double minExclusive;
  private Double minInclusive;
  private Double maxExclusive;
  private Double maxInclusive;
  private Integer minLength;
  private Integer maxLength;
  private String lessThan;
  private String lessThanOrEquals;
  private String hasValue;
  private Boolean unique;
  private List<String> contentType;
  private Integer maxWords;
  private Integer minWords;
  private Dimension dimension;
  private Boolean hidden = Boolean.FALSE;
  private List<String> dependencies;
  private Boolean skip = Boolean.FALSE;
  private Boolean debug = Boolean.FALSE;
  private Boolean allowEmptyInstances = Boolean.FALSE;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Rule withDescription(String description) {
    this.description = description;
    return this;
  }

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

  public List<Rule> getNot() {
    return not;
  }

  public void setNot(List<Rule> not) {
    this.not = not;
  }

  public Rule withNot(List<Rule> not) {
    setNot(not);
    return this;
  }

  public Integer getMinCount() {
    return minCount;
  }

  public void setMinCount(Integer minCount) {
    this.minCount = minCount;
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

  public void setMaxCount(Integer maxCount) {
    this.maxCount = maxCount;
  }

  public void setMaxCount(int maxCount) {
    this.maxCount = maxCount;
  }

  public Rule withMaxCount(int maxCount) {
    setMaxCount(maxCount);
    return this;
  }

  public Double getMinExclusive() {
    return minExclusive;
  }

  public void setMinExclusive(Double minExclusive) {
    this.minExclusive = minExclusive;
  }

  public Rule withMinExclusive(Double minExclusive) {
    setMinExclusive(minExclusive);
    return this;
  }

  public Rule withMinExclusive(Integer minExclusive) {
    setMinExclusive(Double.valueOf(minExclusive));
    return this;
  }

  public Double getMinInclusive() {
    return minInclusive;
  }

  public void setMinInclusive(Double minInclusive) {
    this.minInclusive = minInclusive;
  }

  public Rule withMinInclusive(Double minInclusive) {
    setMinInclusive(minInclusive);
    return this;
  }

  public Rule withMinInclusive(Integer minInclusive) {
    setMinInclusive(Double.valueOf(minInclusive));
    return this;
  }

  public Double getMaxExclusive() {
    return maxExclusive;
  }

  public void setMaxExclusive(Double maxExclusive) {
    this.maxExclusive = maxExclusive;
  }

  public Rule withMaxExclusive(Double maxExclusive) {
    setMaxExclusive(maxExclusive);
    return this;
  }

  public Rule withMaxExclusive(Integer maxExclusive) {
    setMaxExclusive(Double.valueOf(maxExclusive));
    return this;
  }

  public Double getMaxInclusive() {
    return maxInclusive;
  }

  public void setMaxInclusive(Double maxInclusive) {
    this.maxInclusive = maxInclusive;
  }

  public Rule withMaxInclusive(Double maxInclusive) {
    setMaxInclusive(maxInclusive);
    return this;
  }

  public Rule withMaxInclusive(Integer maxInclusive) {
    setMaxInclusive(Double.valueOf(maxInclusive));
    return this;
  }

  public Integer getMinLength() {
    return minLength;
  }

  public void setMinLength(Integer minLength) {
    this.minLength = minLength;
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

  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
  }

  public void setMaxLength(int maxLength) {
    this.maxLength = maxLength;
  }

  public Rule withMaxLength(int maxLength) {
    setMaxLength(maxLength);
    return this;
  }

  public String getLessThan() {
    return lessThan;
  }

  public void setLessThan(String lessThan) {
    this.lessThan = lessThan;
  }

  public Rule withLessThan(String lessThan) {
    setLessThan(lessThan);
    return this;
  }

  public String getLessThanOrEquals() {
    return lessThanOrEquals;
  }

  public void setLessThanOrEquals(String lessThanOrEquals) {
    this.lessThanOrEquals = lessThanOrEquals;
  }

  public Rule withLessThanOrEquals(String lessThanOrEquals) {
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

  public Integer getFailureScore() {
    return failureScore;
  }

  public void setFailureScore(Integer failureScore) {
    this.failureScore = failureScore;
  }

  public Rule withFailureScore(Integer failureScore) {
    setFailureScore(failureScore);
    return this;
  }

  public Integer getSuccessScore() {
    return successScore;
  }

  public void setSuccessScore(Integer successScore) {
    this.successScore = successScore;
  }

  public Rule withSuccessScore(Integer successScore) {
    setSuccessScore(successScore);
    return this;
  }

  public Boolean getUnique() {
    return unique;
  }

  public void setUnique(Boolean unique) {
    this.unique = unique;
  }

  public Rule withUnique(Boolean unique) {
    setUnique(unique);
    return this;
  }

  public List<String> getContentType() {
    return contentType;
  }

  public void setContentType(List<String> contentType) {
    this.contentType = contentType;
  }

  public Rule withContentType(List<String> contentType) {
    this.contentType = contentType;
    return this;
  }

  public Integer getMaxWords() {
    return maxWords;
  }

  public void setMaxWords(Integer maxWords) {
    this.maxWords = maxWords;
  }

  public void setMaxWords(int maxWords) {
    this.maxWords = maxWords;
  }

  public Rule withMaxWords(int maxWords) {
    setMaxWords(maxWords);
    return this;
  }

  public Integer getMinWords() {
    return minWords;
  }

  public void setMinWords(Integer minWords) {
    this.minWords = minWords;
  }

  public void setMinWords(int minWords) {
    this.minWords = minWords;
  }

  public Rule withMinWords(int minWords) {
    setMaxWords(minWords);
    return this;
  }

  public Dimension getDimension() {
    return dimension;
  }

  public void setDimension(Dimension dimension) {
    this.dimension = dimension;
  }

  public Rule withDimension(Dimension dimension) {
    this.dimension = dimension;
    return this;
  }

  public Boolean getHidden() {
    return hidden;
  }

  public void setHidden(Boolean hidden) {
    this.hidden = hidden;
  }

  public Rule withHidden(Boolean hidden) {
    this.hidden = hidden;
    return this;
  }

  public List<String> getDependencies() {
    return dependencies;
  }

  public void setDependencies(List<String> dependencies) {
    this.dependencies = dependencies;
  }

  public Rule withRulesAlreadyPassed(List<String> dependencies) {
    this.dependencies = dependencies;
    return this;
  }

  public Boolean getSkip() {
    return skip;
  }

  public void setSkip(Boolean skip) {
    this.skip = skip;
  }

  public Rule withSkip(Boolean skip) {
    this.skip = skip;
    return this;
  }

  public Boolean getDebug() {
    return debug;
  }

  public void setDebug(Boolean debug) {
    this.debug = debug;
  }

  public Rule withDebug(Boolean debug) {
    this.debug = debug;
    return this;
  }

  public Boolean getAllowEmptyInstances() {
    return allowEmptyInstances;
  }

  public void setAllowEmptyInstances(Boolean allowEmptyInstances) {
    this.allowEmptyInstances = allowEmptyInstances;
  }

  public Rule withAllowEmptyInstances(Boolean allowEmptyInstances) {
    this.allowEmptyInstances = allowEmptyInstances;
    return this;
  }
}
