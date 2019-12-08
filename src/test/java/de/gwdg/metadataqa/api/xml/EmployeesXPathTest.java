package de.gwdg.metadataqa.api.xml;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EmployeesXPathTest {

  @Test
  public void test() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse("src/test/resources/xml/employees.xml");
    XPathFactory xPathfactory = XPathFactory.newInstance();
    XPath xpath = xPathfactory.newXPath();

    assertEquals("Meghan", getEmployeeNameById(doc, xpath, 4));
    assertEquals(Arrays.asList("Lisa", "Tom"), getEmployeeNameWithAge(doc, xpath, 30));
    assertEquals(Arrays.asList("Lisa", "Meghan"), getFemaleEmployeesName(doc, xpath));
  }

  private static List<String> getFemaleEmployeesName(Document doc, XPath xpath) {
    List<String> list = new ArrayList<>();
    try {
      //create XPathExpression object
      XPathExpression expr =
          xpath.compile("/Employees/Employee[gender='Female']/name/text()");
      //evaluate expression result on XML document
      NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
      for (int i = 0; i < nodes.getLength(); i++)
        list.add(nodes.item(i).getNodeValue());
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }
    return list;
  }


  private static List<String> getEmployeeNameWithAge(Document doc, XPath xpath, int age) {
    List<String> list = new ArrayList<>();
    try {
      XPathExpression expr =
          xpath.compile("/Employees/Employee[age>" + age + "]/name/text()");
      NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
      for (int i = 0; i < nodes.getLength(); i++)
        list.add(nodes.item(i).getNodeValue());
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }
    return list;
  }


  private static String getEmployeeNameById(Document doc, XPath xpath, int id) {
    String name = null;
    try {
      XPathExpression expr =
          xpath.compile("/Employees/Employee[@id='" + id + "']/name/text()");
      name = (String) expr.evaluate(doc, XPathConstants.STRING);
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }

    return name;
  }
}
