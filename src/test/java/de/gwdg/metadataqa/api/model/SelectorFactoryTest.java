package de.gwdg.metadataqa.api.model;

import de.gwdg.metadataqa.api.model.selector.CsvSelector;
import de.gwdg.metadataqa.api.model.selector.JsonSelector;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.model.selector.SelectorFactory;
import de.gwdg.metadataqa.api.model.selector.XmlSelector;
import de.gwdg.metadataqa.api.schema.Format;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SelectorFactoryTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

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

  @Test
  public void none() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Unrecognized format: null");
    Selector<? extends XmlFieldInstance> cache = SelectorFactory.getInstance(null, "a,b");
    assertTrue(cache instanceof CsvSelector);
    fail("Exception did not thrown.");
  }
}