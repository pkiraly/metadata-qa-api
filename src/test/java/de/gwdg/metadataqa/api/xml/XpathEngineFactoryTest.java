package de.gwdg.metadataqa.api.xml;

import junit.framework.TestCase;

import javax.xml.xpath.XPath;
import java.util.LinkedHashMap;
import java.util.Map;

public class XpathEngineFactoryTest extends TestCase {

  public void testInitializeEngine() {
    XPath xpath = XpathEngineFactory.initializeEngine(null);
    assertEquals("http://www.openarchives.org/OAI/2.0/", xpath.getNamespaceContext().getNamespaceURI("oai"));
    assertEquals("oai", xpath.getNamespaceContext().getPrefix("http://www.openarchives.org/OAI/2.0/"));
  }

  public void testInitializeEngine_withExtraNamespaces() {
    Map<String, String> namespaces = new LinkedHashMap<>();
    namespaces.put("xoai", "http://www.lyncode.com/xoai");
    namespaces.put("foaf", "http://xmlns.com/foaf/0.1/");
    XPath xpath = XpathEngineFactory.initializeEngine(namespaces);
    assertEquals("http://www.openarchives.org/OAI/2.0/", xpath.getNamespaceContext().getNamespaceURI("oai"));
    assertEquals("oai", xpath.getNamespaceContext().getPrefix("http://www.openarchives.org/OAI/2.0/"));

    assertEquals("http://www.lyncode.com/xoai", xpath.getNamespaceContext().getNamespaceURI("xoai"));
    assertEquals("http://xmlns.com/foaf/0.1/", xpath.getNamespaceContext().getNamespaceURI("foaf"));
    assertEquals("xoai", xpath.getNamespaceContext().getPrefix("http://www.lyncode.com/xoai"));
    assertEquals("foaf", xpath.getNamespaceContext().getPrefix("http://xmlns.com/foaf/0.1/"));
  }
}