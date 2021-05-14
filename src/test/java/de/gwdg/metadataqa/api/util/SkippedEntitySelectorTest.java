package de.gwdg.metadataqa.api.util;

import de.gwdg.metadataqa.api.calculator.SkippedEntryChecker;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SkippedEntitySelectorTest {

  @Test
  public void testIsCollectionSkippable() {
    SkippedEntryChecker checker = new SkippedEntryCheckerImpl();
    SkippedEntitySelector selector = new SkippedEntitySelector(new SkippedEntryCheckerImpl());
  }

  @Test
  public void testSetSkippedEntryChecker() {
    SkippedEntitySelector selector = new SkippedEntitySelector();
    assertNull(selector.getSkippedEntryChecker());
    selector.setSkippedEntryChecker(new SkippedEntryCheckerImpl());
    assertNotNull(selector.getSkippedEntryChecker());
  }

  class SkippedEntryCheckerImpl implements SkippedEntryChecker {

    @Override
    public List<String> getSkippableCollectionIds(PathCache cache) {
      return null;
    }

    @Override
    public <T extends XmlFieldInstance> String extractId(T value) {
      return null;
    }
  }
}