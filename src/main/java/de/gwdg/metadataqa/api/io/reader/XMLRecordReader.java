package de.gwdg.metadataqa.api.io.reader;

import de.gwdg.metadataqa.api.calculator.CalculatorFacade;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.xml.XpathEngineFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

public class XMLRecordReader extends RecordReader {

  private NodeList nodeList;
  private NamedNodeMap rootAttributes;
  private int current = 0;
  private Document xmlDocument;
  private CalculatorFacade calculator;

  public XMLRecordReader(BufferedReader inputReader, CalculatorFacade calculator) throws IOException {
    super(inputReader, calculator);
    this.calculator = calculator;
    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    builderFactory.setNamespaceAware(true);
    try {
      DocumentBuilder builder = builderFactory.newDocumentBuilder();
      InputSource inputSource = new InputSource(inputReader);
      xmlDocument = builder.parse(inputSource);
      rootAttributes = xmlDocument.getDocumentElement().getAttributes();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    }
  }

  public XMLRecordReader setRecordAddress(String xpathExpression) {
    XPath xPath = XpathEngineFactory.initializeEngine(calculator.getSchema().getNamespaces());
    try {
      XPathExpression expr = xPath.compile(xpathExpression);
      nodeList = (NodeList) expr.evaluate(xmlDocument, XPathConstants.NODESET);
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }
    return this;
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
  public Map<String, List<MetricResult>> next() {
    String record = nodeToString(nodeList.item(current++));
    return this.calculator.measureAsMetricResult(record);
  }
}
