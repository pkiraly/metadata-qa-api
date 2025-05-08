package de.gwdg.metadataqa.api.uniqueness;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JsonProvider;
import de.gwdg.metadataqa.api.model.selector.JsonSelector;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.util.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class UniquenessExtractorTest {

  public String readContent(String fileName) throws URISyntaxException, IOException {
    return StringUtils.join(FileUtils.readLinesFromResource(fileName), "");
  }

  @Test
  public void testExtraction() throws URISyntaxException, IOException {
    JsonProvider jsonProvider = Configuration.defaultConfiguration().jsonProvider();
    String recordId = "2022320/3F61C612ED9C42CCB85E533B4736795E8BDC7E77";
    String jsonString = readContent("general/uniqueness-response.json");
    assertEquals("{", jsonString.substring(0,1));

    UniquenessExtractor extractor = new UniquenessExtractor();
    int numFound = extractor.extractNumFound(jsonString);
    assertEquals(199, numFound);
  }

  @Test
  public void testJsonPath() throws URISyntaxException, IOException {
    JsonSelector cache = new JsonSelector(FileUtils.readFirstLineFromResource("general/edm-fullbean.json"));
    String path = "$.['proxies'][?(@['europeanaProxy'] == false)]['dcTitle']";
    List<XmlFieldInstance> values = (List<XmlFieldInstance>) cache.get(path);
    assertEquals(1, values.size());
    assertEquals("Окръжно N 752 : 27 февруарий 1914 год., гр. Варна", values.get(0).getValue());
  }

}
