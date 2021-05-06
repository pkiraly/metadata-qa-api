package de.gwdg.metadataqa.api.xml;

import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import org.apache.ws.commons.util.NamespaceContextImpl;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathExpressionException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OaiPmhXPath implements Serializable {
  private static final long serialVersionUID = 3040547541095974755L;

  private static final Logger LOGGER = Logger.getLogger(OaiPmhXPath.class.getCanonicalName());

  private static final Map<String, String> prefixMap = new LinkedHashMap<>();
  static {
    prefixMap.put("xsi", "http://www.w3.org/2001/XMLSchema-instance");
    prefixMap.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
    prefixMap.put("dc", "http://purl.org/dc/elements/1.1/");
    prefixMap.put("dcterms", "http://purl.org/dc/terms/");
    prefixMap.put("edm", "http://www.europeana.eu/schemas/edm/");
    prefixMap.put("owl", "http://www.w3.org/2002/07/owl#");
    prefixMap.put("wgs84_pos", "http://www.w3.org/2003/01/geo/wgs84_pos#");
    prefixMap.put("skos", "http://www.w3.org/2004/02/skos/core#");
    prefixMap.put("rdaGr2", "http://rdvocab.info/ElementsGr2/");
    prefixMap.put("foaf", "http://xmlns.com/foaf/0.1/");
    prefixMap.put("ebucore", "http://www.ebu.ch/metadata/ontologies/ebucore/ebucore#");
    prefixMap.put("doap", "http://usefulinc.com/ns/doap#");
    prefixMap.put("odrl", "http://www.w3.org/ns/odrl/2/");
    prefixMap.put("cc", "http://creativecommons.org/ns#");
    prefixMap.put("ore", "http://www.openarchives.org/ore/terms/");
    prefixMap.put("svcs", "http://rdfs.org/sioc/services#");
    prefixMap.put("oa", "http://www.w3.org/ns/oa#");
    prefixMap.put("dqv", "http://www.w3.org/ns/dqv#");
    prefixMap.put("xml", "http://www.w3.org/XML/1998/namespace");
    prefixMap.put("oai", "http://www.openarchives.org/OAI/2.0/");
  }

  private final transient XPath xpathEngine = initializeEngine();
  private static final DocumentBuilder builder = initializeDocumentBuilder();

  private static XPath initializeEngine() {
    var xPathfactory = XPathFactory.newInstance();
    var xpathEngine = xPathfactory.newXPath();
    var nsContext = new NamespaceContextImpl();
    for (String prefix : prefixMap.keySet())
      nsContext.startPrefixMapping(prefix, prefixMap.get(prefix));
    xpathEngine.setNamespaceContext(nsContext);
    return xpathEngine;
  }

  private static DocumentBuilder initializeDocumentBuilder() {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    DocumentBuilder builder = null;
    try {
      builder = factory.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    }
    return builder;
  }

  Document document;

  public OaiPmhXPath(String input) {
    parseContent(input);
  }

  public OaiPmhXPath(File input) {
    parseFile(input.getPath());
  }

  public OaiPmhXPath(String input, boolean fromString) {
    if (fromString) {
      parseContent(input);
    } else {
      parseFile(input);
    }
  }

  private void parseFile(String path) {
    try {
      document = builder.parse(path);
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, "buildUrl", e);
    }
  }

  public void parseContent(String content) {
    parseContent(new ByteArrayInputStream(content.getBytes()));
  }

  private void parseContent(InputStream content) {
    try {
      document = builder.parse(content);
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, "parseContent", e);
    }
  }

  public List<EdmFieldInstance> extractFieldInstanceList(String xpath) {
    return extractFieldInstanceList(document, xpath);
  }

  public List<EdmFieldInstance> extractFieldInstanceList(Object context, String xpath) {
    List<EdmFieldInstance> list = new ArrayList<>();
    try {
      XPathExpression expr = xpathEngine.compile(xpath);
      NodeList nodes = (NodeList) expr.evaluate(context, XPathConstants.NODESET);
      for (var i = 0; i < nodes.getLength(); i++) {
        Node node = nodes.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
          String value = node.getTextContent();
          String lang = null;
          String resource = null;
          if (node.hasAttributes()) {
            NamedNodeMap attributes = node.getAttributes();
            lang = getAttribute(attributes, "xml", "lang");
            resource = getAttribute(attributes, "rdf", "resource");
          }
          list.add(new EdmFieldInstance(value, lang, resource));
        } else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
          String value = node.getNodeValue();
          list.add(new EdmFieldInstance(value, null, null));
        }
      }
    } catch (XPathExpressionException e) {
      LOGGER.log(Level.WARNING, "extractFieldInstanceList", e);
    }
    return list;
  }

  public List<Node> extractNodes(String xpath) {
    return extractNodes(document, xpath);
  }

  public List<Node> extractNodes(Object context, String xpath) {
    List<Node> list = new ArrayList<>();
    try {
      XPathExpression expr = xpathEngine.compile(xpath);
      NodeList nodes = (NodeList) expr.evaluate(context, XPathConstants.NODESET);
      for (var i = 0; i < nodes.getLength(); i++) {
        Node node = nodes.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
          list.add(node);
        } else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
          list.add(node);
        }
      }
    } catch (XPathExpressionException e) {
      LOGGER.log(Level.WARNING, "extractNodes", e);
    }
    return list;
  }

  public String getAttribute(NamedNodeMap attributes, String prefix, String name) {
    Node attribute = attributes.getNamedItemNS(prefixMap.get(prefix), name);
    String value = null;
    if (attribute != null) {
      value = attribute.getNodeValue();
    }
    return value;
  }

  public XPath getXpathEngine() {
    return xpathEngine;
  }

  public Document getDocument() {
    return document;
  }
}
