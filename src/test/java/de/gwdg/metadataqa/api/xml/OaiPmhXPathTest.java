package de.gwdg.metadataqa.api.xml;

import de.gwdg.metadataqa.api.calculator.CalculatorFacade;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import de.gwdg.metadataqa.api.schema.edm.EdmOaiPmhXmlSchema;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.FileUtils;
import org.junit.Test;
import org.w3c.dom.Node;

import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class OaiPmhXPathTest {
  private static Map<String, String> prefixMap = new LinkedHashMap<String, String>() {{
    put("xsi", "http://www.w3.org/2001/XMLSchema-instance");
    put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
    put("dc", "http://purl.org/dc/elements/1.1/");
    put("dcterms", "http://purl.org/dc/terms/");
    put("edm", "http://www.europeana.eu/schemas/edm/");
    put("owl", "http://www.w3.org/2002/07/owl#");
    put("wgs84_pos", "http://www.w3.org/2003/01/geo/wgs84_pos#");
    put("skos", "http://www.w3.org/2004/02/skos/core#");
    put("rdaGr2", "http://rdvocab.info/ElementsGr2/");
    put("foaf", "http://xmlns.com/foaf/0.1/");
    put("ebucore", "http://www.ebu.ch/metadata/ontologies/ebucore/ebucore#");
    put("doap", "http://usefulinc.com/ns/doap#");
    put("odrl", "http://www.w3.org/ns/odrl/2/");
    put("cc", "http://creativecommons.org/ns#");
    put("ore", "http://www.openarchives.org/ore/terms/");
    put("svcs", "http://rdfs.org/sioc/services#");
    put("oa", "http://www.w3.org/ns/oa#");
    put("dqv", "http://www.w3.org/ns/dqv#");
    put("xml", "http://www.w3.org/XML/1998/namespace");
  }};

  private final String inputFile = "src/test/resources/general/europeana-oai-pmh.xml";

  @Test
  public void testNamespaces() {
    OaiPmhXPath oaiPmhXPath = new OaiPmhXPath(new File(inputFile));
    XPath xpathEngine = oaiPmhXPath.getXpathEngine();

    for (String prefix : prefixMap.keySet()) {
      String uri = prefixMap.get(prefix);
      assertEquals(prefix, xpathEngine.getNamespaceContext().getPrefix(uri));
      assertEquals(uri, xpathEngine.getNamespaceContext().getNamespaceURI(prefix));
    }
  }

  @Test
  public void testValueAndLanguage() {
    OaiPmhXPath oaiPmhXPath = new OaiPmhXPath(new File(inputFile));

    List<EdmFieldInstance> list = oaiPmhXPath.extractFieldInstanceList("//skos:prefLabel");
    assertEquals(346, list.size());

    assertEquals("Francis 'Frans' Smith", list.get(0).getValue());
    assertEquals("en", list.get(0).getLanguage());
    assertEquals(false, list.get(0).isUrl());
    assertEquals(false, list.get(0).hasResource());
    assertEquals(null, list.get(0).getResource());

    assertEquals("Eduardo Afonso Viana", list.get(1).getValue());
    assertEquals("pt", list.get(1).getLanguage());
  }

  @Test
  public void testResource() {
    OaiPmhXPath oaiPmhXPath = new OaiPmhXPath(new File(inputFile));

    List<EdmFieldInstance> list = oaiPmhXPath.extractFieldInstanceList("//rdaGr2:professionOrOccupation");
    assertEquals(5, list.size());

    assertEquals("", list.get(0).getValue());
    assertEquals(null, list.get(0).getLanguage());
    assertEquals(true, list.get(0).isUrl());
    assertEquals(true, list.get(0).hasResource());
    assertEquals("http://dbpedia.org/resource/Puisne_judge", list.get(0).getResource());

    assertEquals("Puisne Judge", list.get(1).getValue());
    assertEquals("en", list.get(1).getLanguage());
  }

  @Test
  public void testProxy() {
    OaiPmhXPath oaiPmhXPath = new OaiPmhXPath(new File(inputFile));

    List<EdmFieldInstance> list = oaiPmhXPath.extractFieldInstanceList(
      "//ore:Proxy[edm:europeanaProxy/text() = 'false']"
    );
    assertEquals(1, list.size());

    list = oaiPmhXPath.extractFieldInstanceList(
        "//ore:Proxy[edm:europeanaProxy/text() = 'true']"
    );
    assertEquals(1, list.size());
  }

  @Test
  public void testDifferentContexts() {
    OaiPmhXPath oaiPmhXPath = new OaiPmhXPath(new File(inputFile));

    List<Node> proxies = null;
    List<EdmFieldInstance> fields = null;

    proxies = oaiPmhXPath.extractNodes(
      "//ore:Proxy[edm:europeanaProxy/text() = 'false']"
    );
    assertEquals(1, proxies.size());

    fields = oaiPmhXPath.extractFieldInstanceList(proxies.get(0), "dcterms:temporal");
    assertEquals(2, fields.size());
    assertEquals("1968", fields.get(0).getValue());
    assertEquals("Séc. 19-20", fields.get(1).getValue());

    proxies = oaiPmhXPath.extractNodes(
        "//ore:Proxy[edm:europeanaProxy/text() = 'true']"
    );
    assertEquals(1, proxies.size());
    fields = oaiPmhXPath.extractFieldInstanceList(proxies.get(0), "dcterms:temporal");
    assertEquals(2, fields.size());
    assertEquals("http://semium.org/time/19xx", fields.get(0).getResource());
    assertEquals("http://semium.org/time/1968", fields.get(1).getResource());
  }

  @Test
  public void testTops() {
    OaiPmhXPath oaiPmhXPath = new OaiPmhXPath(new File(inputFile));

    List<EdmFieldInstance> list = oaiPmhXPath.extractFieldInstanceList(
      "//oai:header/oai:identifier"
    );
    assertEquals(1, list.size());
    assertEquals(
      "http://data.europeana.eu/item/00101/2F29675EB2C8ADFFA488087DB15C5C479C95A2C3",
      list.get(0).getValue());

    list = oaiPmhXPath.extractFieldInstanceList("//oai:header/oai:setSpec");
    assertEquals(1, list.size());
    assertEquals(
      "00101",
      list.get(0).getValue());

    list = oaiPmhXPath.extractFieldInstanceList("//ore:Aggregation[1]/edm:dataProvider[1]");
    assertEquals(1, list.size());
    assertEquals(
      "Fundação Calouste Gulbenkian - Portugal",
      list.get(0).getValue());
  }

  @Test
  public void testCalculator() throws IOException, URISyntaxException {
    CalculatorFacade calculatorFacade = new CalculatorFacade(true, true, true, false, true);
    calculatorFacade.setSchema(new EdmOaiPmhXmlSchema());
    calculatorFacade.configure();
    String expected = "0.208,1.0,0.181818,0.388889,0.272727,0.5,0.357143,0.75,0.363636,0.4,1,1,1,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,0,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,1,0,0,0,1,0,1,1,1,1,0,0,0,0,0,1,1,0,0,0,0,5,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,0,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,2,0,0,0,59,0,0,0,39,0,39,0.0,0.0,0.0";
    assertEquals(
      expected,
      calculatorFacade.measure(
        FileUtils.readContentFromResource(
          "general/europeana-oai-pmh-92062-BibliographicResource_1000126015451.xml")
      )
    );
    List<String> headers = calculatorFacade.getHeader();
    List<String> scores = Arrays.asList(expected.split(","));
    assertEquals(scores.size(), headers.size());
  }

  @Test
  public void testJsonBranch() throws IOException, URISyntaxException {
    Schema schema = new EdmOaiPmhXmlSchema();
    JsonBranch branch = schema.getPathByLabel("Concept");
    String path = branch.getJsonPath();
    assertEquals("//skos:Concept", path);

    OaiPmhXPath oaiPmhXPath = new OaiPmhXPath(
      FileUtils.readContentFromResource(
        "general/europeana-oai-pmh-92062-BibliographicResource_1000126015451.xml")
    );
    List<Node> attr = oaiPmhXPath.extractNodes("//skos:Concept[1]/@rdf:about");
    assertEquals(1, attr.size());
    assertEquals("http://data.europeana.eu/concept/base/106", attr.get(0).getNodeValue());
  }

  @Test
  public void testNamespaces_withCustomNS() {
    Map<String, String> namespaces = new LinkedHashMap<>();
    namespaces.put("xoai", "http://www.lyncode.com/xoai");
    namespaces.put("foaf", "http://xmlns.com/foaf/0.1/");

    OaiPmhXPath.setXpathEngine(XpathEngineFactory.initializeEngine(namespaces));

    OaiPmhXPath oaiPmhXPath = new OaiPmhXPath(new File(inputFile));
    XPath xpathEngine = oaiPmhXPath.getXpathEngine();

    for (String prefix : prefixMap.keySet()) {
      String uri = prefixMap.get(prefix);
      assertEquals(prefix, xpathEngine.getNamespaceContext().getPrefix(uri));
      assertEquals(uri, xpathEngine.getNamespaceContext().getNamespaceURI(prefix));
    }

    for (Map.Entry<String, String> entry : namespaces.entrySet()) {
      assertEquals(entry.getKey(), xpathEngine.getNamespaceContext().getPrefix(entry.getValue()));
      assertEquals(entry.getValue(), xpathEngine.getNamespaceContext().getNamespaceURI(entry.getKey()));
    }
  }

  @Test
  public void testNamespaces_withCustomNS_setXpathEngine() {
    Map<String, String> namespaces = new LinkedHashMap<>();
    namespaces.put("xoai", "http://www.lyncode.com/xoai");
    namespaces.put("foaf", "http://xmlns.com/foaf/0.1/");

    OaiPmhXPath.setXpathEngine(namespaces);

    OaiPmhXPath oaiPmhXPath = new OaiPmhXPath(new File(inputFile));
    XPath xpathEngine = oaiPmhXPath.getXpathEngine();

    for (String prefix : prefixMap.keySet()) {
      String uri = prefixMap.get(prefix);
      assertEquals(prefix, xpathEngine.getNamespaceContext().getPrefix(uri));
      assertEquals(uri, xpathEngine.getNamespaceContext().getNamespaceURI(prefix));
    }

    for (Map.Entry<String, String> entry : namespaces.entrySet()) {
      assertEquals(entry.getKey(), xpathEngine.getNamespaceContext().getPrefix(entry.getValue()));
      assertEquals(entry.getValue(), xpathEngine.getNamespaceContext().getNamespaceURI(entry.getKey()));
    }
  }

}
