package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.calculator.edm.EnhancementIdExtractor;
import de.gwdg.metadataqa.api.model.selector.JsonSelector;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.model.selector.XmlSelector;
import de.gwdg.metadataqa.api.schema.edm.EdmOaiPmhJsonSchema;
import de.gwdg.metadataqa.api.schema.edm.EdmOaiPmhXmlSchema;
import de.gwdg.metadataqa.api.util.FileUtils;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class EnhancementExtractorTest {

  Selector cache;

  @Before
  public void setUp() throws URISyntaxException, IOException {
    cache = new JsonSelector(FileUtils.readFirstLineFromResource("general/test.json"));
  }

  @Test
  public void testJson() {
    List<String> enhancementIds = EnhancementIdExtractor.extractIds(cache, new EdmOaiPmhJsonSchema());
    assertTrue(!enhancementIds.isEmpty());
    assertEquals(1, enhancementIds.size());
    assertEquals("http://dbpedia.org/resource/Portrait", enhancementIds.get(0));
  }

  @Test
  public void testXml() throws IOException, URISyntaxException {
    cache = new XmlSelector(FileUtils.readContentFromResource("general/europeana-oai-pmh.xml"));
    List<String> enhancementIds = EnhancementIdExtractor.extractIds(cache, new EdmOaiPmhXmlSchema());
    assertTrue(!enhancementIds.isEmpty());
    assertEquals(27, enhancementIds.size());
    assertEquals("http://data.europeana.eu/agent/base/142035", enhancementIds.get(0));
  }
}
