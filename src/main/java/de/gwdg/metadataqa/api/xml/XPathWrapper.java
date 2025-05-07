package de.gwdg.metadataqa.api.xml;

import de.gwdg.metadataqa.api.model.EdmFieldInstance;
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
import javax.xml.xpath.XPathExpressionException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XPathWrapper implements Serializable {
  private static final long serialVersionUID = 3040547541095974755L;

  private static final Logger LOGGER = Logger.getLogger(XPathWrapper.class.getCanonicalName());

  private static transient XPath xpathEngine;
  private static final DocumentBuilder builder = initializeDocumentBuilder();
  private static transient Map<String, String> namespaces;
  private boolean namespaceChanged = false;
  private static Map<String, XPathExpression> xpathCache = new HashMap<>();

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

  public XPathWrapper(String input) {
    parseContent(input);
  }

  public XPathWrapper(String input, Map<String, String> customNamespaces) {
    if (customNamespaces != null && this.namespaces != customNamespaces) {
      namespaceChanged = true;
      this.namespaces = customNamespaces;
    }
    parseContent(input);
  }

  public XPathWrapper(File input) {
    parseFile(input.getPath());
  }

  public XPathWrapper(File input, Map<String, String> customNamespaces) {
    if (this.namespaces != customNamespaces) {
      namespaceChanged = true;
      this.namespaces = customNamespaces;
    }
    parseFile(input.getPath());
  }

  private void initialize() {
    if (xpathEngine == null || namespaceChanged) {
      LOGGER.info("initialize xpathEngine");
      if (namespaces == null) {
        LOGGER.info("initialize without namespaces");
        xpathEngine = XpathEngineFactory.initializeEngine();
      } else {
        xpathEngine = XpathEngineFactory.initializeEngine(namespaces);
      }
    }
  }

  public XPathWrapper(String input, boolean fromString) {
    if (fromString) {
      parseContent(input);
    } else {
      parseFile(input);
    }
  }

  private void parseFile(String path) {
    initialize();
    try {
      document = builder.parse(path);
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, "buildUrl", e);
    }
  }

  public void parseContent(String content) {
    initialize();
    parseContent(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
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
      if (!xpathCache.containsKey(xpath))
        xpathCache.put(xpath, xpathEngine.compile(xpath));
      XPathExpression expr = xpathCache.get(xpath);
      if (xpath.endsWith(")")) {
        String value = String.valueOf(expr.evaluate(context, XPathConstants.STRING));
        list.add(new EdmFieldInstance(value, null, null));
      } else {
        NodeList nodes = (NodeList) expr.evaluate(context, XPathConstants.NODESET);
        for (var i = 0; i < nodes.getLength(); i++) {
          Node node = nodes.item(i);
          if (node.getNodeType() == Node.ELEMENT_NODE) {
            String value = node.getTextContent().trim();
            if (value.contains("\n"))
              value = value.replaceAll("(\\r|\\n)", " ").replaceAll("\\s+", " ");
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
      }
    } catch (XPathExpressionException e) {
      LOGGER.log(Level.WARNING, "error in extractFieldInstanceList() with xpath: '" + xpath + "' " + e.getMessage());
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
    Node attribute = attributes.getNamedItemNS(xpathEngine.getNamespaceContext().getNamespaceURI(prefix), name);
    String value = null;
    if (attribute != null) {
      value = attribute.getNodeValue();
      if (value != null)
        value = value.trim();
    }
    return value;
  }

  public XPath getXpathEngine() {
    return xpathEngine;
  }

  public Document getDocument() {
    return document;
  }

  public static void setXpathEngine(XPath xpathEngine) {
    XPathWrapper.xpathEngine = xpathEngine;
  }

  public static void setXpathEngine(Map<String, String> namespaces) {
    xpathEngine = XpathEngineFactory.initializeEngine(namespaces);
  }
}
