package de.gwdg.metadataqa.api.util;

public class DimensionDao {
  private Integer width;
  private Integer height;

  public DimensionDao(Integer width, Integer height) {
    this.width = width;
    this.height = height;
  }

  public Integer getWidth() {
    return width;
  }

  public Integer getHeight() {
    return height;
  }

  public Integer getShort() {
    return width <= height ? width : height;
  }

  public Integer getLong() {
    return width >= height ? width : height;
  }

}
