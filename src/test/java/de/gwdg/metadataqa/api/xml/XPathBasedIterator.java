package de.gwdg.metadataqa.api.xml;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;

public class XPathBasedIterator implements Iterator<String> {
  private NodeList nodeList;
  private NamedNodeMap rootAttributes;
  private int current = 0;

  public XPathBasedIterator(File input,
                            String expression,
                            Map<String, String> namespaces)
    throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
    this(new FileInputStream(input), expression, namespaces);
  }

  public XPathBasedIterator(InputStream fileIS,
                            String expression,
                            Map<String, String> namespaces)
    throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    builderFactory.setNamespaceAware(true);
    DocumentBuilder builder = builderFactory.newDocumentBuilder();
    Document xmlDocument = builder.parse(fileIS);
    rootAttributes = xmlDocument.getDocumentElement().getAttributes();
    XPath xPath = XpathEngineFactory.initializeEngine(namespaces);
    XPathExpression expr = xPath.compile(expression);
    nodeList = (NodeList) expr.evaluate(xmlDocument, XPathConstants.NODESET);
  }

  public String nodeToString(Node node) {
    injectNamespaces(node);

    StringWriter sw = new StringWriter();
    try {
      Transformer t = TransformerFactory.newInstance().newTransformer();
      t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      t.setOutputProperty(OutputKeys.INDENT, "yes");
      t.transform(new DOMSource(node), new StreamResult(sw));
    } catch (TransformerException te) {
      System.out.println("nodeToString Transformer Exception");
    }
    return sw.toString().replaceAll("\n\\s+\n", "\n");
  }

  private void injectNamespaces(Node node) {
    Element el = (Element) node;
    for (var i = 0; i < rootAttributes.getLength(); i++) {
      Attr attr = (Attr) rootAttributes.item(i);
      el.setAttribute(attr.getName(), attr.getValue());
    }
  }

  public int getLength() {
    return nodeList.getLength();
  }

  @Override
  public boolean hasNext() {
    return current < nodeList.getLength();
  }

  @Override
  public String next() {
    return nodeToString(nodeList.item(current++));
  }

  public Node nextNode() {
    return nodeList.item(current++);
  }
}
