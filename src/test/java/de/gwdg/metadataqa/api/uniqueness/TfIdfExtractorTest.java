package de.gwdg.metadataqa.api.uniqueness;

import de.gwdg.metadataqa.api.util.FileUtils;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JsonProvider;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.schema.EdmOaiPmhJsonSchema;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class TfIdfExtractorTest {

  public String readContent(String fileName) throws URISyntaxException, IOException {
    // Path path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
    // List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
    return StringUtils.join(FileUtils.readLinesFromResource(fileName), "");
  }

  @Test
  public void extract() throws URISyntaxException, IOException {
    JsonProvider jsonProvider = Configuration.defaultConfiguration().jsonProvider();
    String recordId = "2022320/3F61C612ED9C42CCB85E533B4736795E8BDC7E77";
    String jsonString = readContent("general/td-idf-response.json");
    assertEquals("{", jsonString.substring(0,1));

    var extractor = new TfIdfExtractor(new EdmOaiPmhJsonSchema());
    FieldCounter<Double> results = extractor.extract(jsonString, recordId);
    assertEquals(6, results.size());
    assertEquals(new Double(0.0017653998874690505), results.get("Proxy/dc:title:avg"));
    assertEquals(new Double(0.008826999437345252), results.get("Proxy/dc:title:sum"));
    assertEquals(new Double(0), results.get("Proxy/dcterms:alternative:avg"));
    assertEquals(new Double(0), results.get("Proxy/dcterms:alternative:sum"));
    assertEquals(new Double(0), results.get("Proxy/dc:description:avg"));
    assertEquals(new Double(0), results.get("Proxy/dc:description:sum"));
  }
}
