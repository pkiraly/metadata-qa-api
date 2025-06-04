package de.gwdg.metadataqa.api.xml;

import de.gwdg.metadataqa.api.configuration.ConfigurationReader;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.model.selector.SelectorFactory;
import de.gwdg.metadataqa.api.rule.RuleChecker;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.schema.Schema;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class ValuePathTest {

  private Schema schema;
  private String recordAddress = "//oai:record | //rdf:Description";
  private XPathWrapper xPathWrapper;
  private String xml;

  public void setup(String fileName) throws Exception {
    System.err.println(fileName);
    // this.getClass().getClassLoader().getResource()
    URL url = this.getClass().getClassLoader().getResource(fileName);
    File file = new File(url.getFile());
    assertTrue(file.exists());

    try {
      schema = ConfigurationReader.readSchemaYaml("src/test/resources/configuration/schema/rules/hierarchy.yaml").asSchema();
      XPathBasedIterator iterator = new XPathBasedIterator(file, recordAddress, schema.getNamespaces());
      xml = iterator.next();
      xPathWrapper = new XPathWrapper(xml, schema.getNamespaces());
    } catch (XPathExpressionException | IOException | ParserConfigurationException | SAXException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void brokenLink() throws Exception {
    setup("xml/valuePath.xml");
    Selector selector = SelectorFactory.getInstance(schema.getFormat(), xml);
    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    List<String> ids = List.of("1.1");
    for (RuleChecker checker : schema.getRuleCheckers()) {
      if (ids.contains(checker.getId())) {
        checker.setDebug();
        checker.update(selector, fieldCounter, RuleCheckingOutputType.STATUS);
      }
    }
    System.err.println(fieldCounter);
    assertEquals(
      RuleCheckingOutputStatus.PASSED,
      fieldCounter.get("1.1").getStatus()
    );
  }
}
