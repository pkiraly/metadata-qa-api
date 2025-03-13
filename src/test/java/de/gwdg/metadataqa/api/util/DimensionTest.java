package de.gwdg.metadataqa.api.util;

import de.gwdg.metadataqa.api.configuration.schema.Dimension;
import org.junit.Test;

import static org.junit.Assert.*;

public class DimensionTest {

  @Test
  public void getMinWidth() {
    Dimension dimension = new Dimension();
    dimension.setMinWidth(100);
    assertEquals(100, (long) dimension.getMinWidth());
  }

  @Test
  public void withMinWidth() {
    Dimension dimension = new Dimension().withMinWidth(100);
    assertEquals(100, (long) dimension.getMinWidth());
  }

  @Test
  public void getMaxWidth() {
    Dimension dimension = new Dimension();
    dimension.setMaxWidth(100);
    assertEquals(100, (long) dimension.getMaxWidth());
  }

  @Test
  public void withMaxWidth() {
    Dimension dimension = new Dimension().withMaxWidth(100);
    assertEquals(100, (long) dimension.getMaxWidth());
  }

  @Test
  public void getMinHeight() {
    Dimension dimension = new Dimension();
    dimension.setMinHeight(100);
    assertEquals(100, (long) dimension.getMinHeight());
  }

  @Test
  public void withMinHeight() {
    Dimension dimension = new Dimension().withMinHeight(100);
    assertEquals(100, (long) dimension.getMinHeight());
  }

  @Test
  public void getMaxHeight() {
    Dimension dimension = new Dimension();
    dimension.setMaxHeight(100);
    assertEquals(100, (long) dimension.getMaxHeight());
  }

  @Test
  public void withMaxHeight() {
    Dimension dimension = new Dimension().withMaxHeight(100);
    assertEquals(100, (long) dimension.getMaxHeight());
  }

  @Test
  public void getMinShortside() {
    Dimension dimension = new Dimension();
    dimension.setMinShortside(100);
    assertEquals(100, (long) dimension.getMinShortside());
  }

  @Test
  public void withMinShortside() {
    Dimension dimension = new Dimension().withMinShortside(100);
    assertEquals(100, (long) dimension.getMinShortside());
  }

  @Test
  public void getMaxShortside() {
    Dimension dimension = new Dimension();
    dimension.setMaxShortside(100);
    assertEquals(100, (long) dimension.getMaxShortside());
  }

  @Test
  public void withMaxShortside() {
    Dimension dimension = new Dimension().withMaxShortside(100);
    assertEquals(100, (long) dimension.getMaxShortside());
  }

  @Test
  public void getMinLongside() {
    Dimension dimension = new Dimension();
    dimension.setMinLongside(100);
    assertEquals(100, (long) dimension.getMinLongside());
  }

  @Test
  public void withMinLongside() {
    Dimension dimension = new Dimension().withMinLongside(100);
    assertEquals(100, (long) dimension.getMinLongside());
  }

  @Test
  public void getMaxLongside() {
    Dimension dimension = new Dimension();
    dimension.setMaxLongside(100);
    assertEquals(100, (long) dimension.getMaxLongside());
  }

  @Test
  public void withMaxLongside() {
    Dimension dimension = new Dimension().withMaxLongside(100);
    assertEquals(100, (long) dimension.getMaxLongside());
  }
}