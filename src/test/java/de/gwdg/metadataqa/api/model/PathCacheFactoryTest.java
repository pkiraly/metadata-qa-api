package de.gwdg.metadataqa.api.model;

import de.gwdg.metadataqa.api.model.pathcache.CsvPathCache;
import de.gwdg.metadataqa.api.model.pathcache.JsonPathCache;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.model.pathcache.XmlPathCache;
import de.gwdg.metadataqa.api.schema.Format;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PathCacheFactoryTest {

  @Test
  public void json() {
    PathCache<? extends XmlFieldInstance> cache = PathCacheFactory.getInstance(Format.JSON, "[\"a\",\"b\"]");
    assertTrue(cache instanceof JsonPathCache);
  }

  @Test
  public void xml() {
    PathCache<? extends XmlFieldInstance> cache = PathCacheFactory.getInstance(Format.XML, "<a/>");
    assertTrue(cache instanceof XmlPathCache);
  }

  @Test
  public void csv() {
    PathCache<? extends XmlFieldInstance> cache = PathCacheFactory.getInstance(Format.CSV, "a,b");
    assertTrue(cache instanceof CsvPathCache);
  }

  @Test(expected = IllegalArgumentException.class)
  public void none() {
    PathCache<? extends XmlFieldInstance> cache = PathCacheFactory.getInstance(null, "a,b");
    assertTrue(cache instanceof CsvPathCache);
  }
}