package de.gwdg.metadataqa.api.configuration.schema;

public class Dimension {
  private Integer minWidth;
  private Integer maxWidth;
  private Integer minHeight;
  private Integer maxHeight;
  private Integer minShortside;
  private Integer maxShortside;
  private Integer minLongside;
  private Integer maxLongside;

  public Dimension() {}

  public Integer getMinWidth() {
    return minWidth;
  }

  public void setMinWidth(Integer minWidth) {
    this.minWidth = minWidth;
  }

  public Dimension withMinWidth(Integer minWidth) {
    this.minWidth = minWidth;
    return this;
  }

  public Integer getMaxWidth() {
    return maxWidth;
  }

  public void setMaxWidth(Integer maxWidth) {
    this.maxWidth = maxWidth;
  }

  public Dimension withMaxWidth(Integer maxWidth) {
    this.maxWidth = maxWidth;
    return this;
  }

  public Integer getMinHeight() {
    return minHeight;
  }

  public void setMinHeight(Integer minHeight) {
    this.minHeight = minHeight;
  }

  public Dimension withMinHeight(Integer minHeight) {
    this.minHeight = minHeight;
    return this;
  }

  public Integer getMaxHeight() {
    return maxHeight;
  }

  public void setMaxHeight(Integer maxHeight) {
    this.maxHeight = maxHeight;
  }

  public Dimension withMaxHeight(Integer maxHeight) {
    this.maxHeight = maxHeight;
    return this;
  }

  public Integer getMinShortside() {
    return minShortside;
  }

  public void setMinShortside(Integer minShortside) {
    this.minShortside = minShortside;
  }

  public Dimension withMinShortside(Integer minShort) {
    this.minShortside = minShort;
    return this;
  }

  public Integer getMaxShortside() {
    return maxShortside;
  }

  public void setMaxShortside(Integer maxShortside) {
    this.maxShortside = maxShortside;
  }

  public Dimension withMaxShortside(Integer maxShort) {
    this.maxShortside = maxShort;
    return this;
  }

  public Integer getMinLongside() {
    return minLongside;
  }

  public void setMinLongside(Integer minLongside) {
    this.minLongside = minLongside;
  }

  public Dimension withMinLongside(Integer minLong) {
    this.minLongside = minLong;
    return this;
  }

  public Integer getMaxLongside() {
    return maxLongside;
  }

  public void setMaxLongside(Integer maxLongside) {
    this.maxLongside = maxLongside;
  }

  public Dimension withMaxLongside(Integer maxLong) {
    this.maxLongside = maxLong;
    return this;
  }
}
