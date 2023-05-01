package de.gwdg.metadataqa.api.model;

import de.gwdg.metadataqa.api.model.selector.CsvSelector;
import de.gwdg.metadataqa.api.model.selector.JsonSelector;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.model.selector.SelectorFactory;
import de.gwdg.metadataqa.api.model.selector.XmlSelector;
import de.gwdg.metadataqa.api.schema.Format;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SelectorFactoryTest {

  @Test
  public void json() {
    Selector<? extends XmlFieldInstance> cache = SelectorFactory.getInstance(Format.JSON, "[\"a\",\"b\"]");
    assertTrue(cache instanceof JsonSelector);
  }

  @Test
  public void xml() {
    Selector<? extends XmlFieldInstance> cache = SelectorFactory.getInstance(Format.XML, "<a/>");
    assertTrue(cache instanceof XmlSelector);
  }

  @Test
  public void csv() {
    Selector<? extends XmlFieldInstance> cache = SelectorFactory.getInstance(Format.CSV, "a,b");
    assertTrue(cache instanceof CsvSelector);
  }

  @Test(expected = IllegalArgumentException.class)
  public void none() {
    Selector<? extends XmlFieldInstance> cache = SelectorFactory.getInstance(null, "a,b");
    assertTrue(cache instanceof CsvSelector);
  }
}