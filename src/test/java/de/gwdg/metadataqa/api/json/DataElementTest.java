package de.gwdg.metadataqa.api.json;

import de.gwdg.metadataqa.api.schema.*;
import de.gwdg.metadataqa.api.schema.edm.EdmFullBeanSchema;
import de.gwdg.metadataqa.api.schema.edm.EdmOaiPmhJsonSchema;
import de.gwdg.metadataqa.api.schema.edm.EdmOaiPmhXmlSchema;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class DataElementTest {

  @Test
  public void IfCloned_ObjectAreDifferent() {
    Schema schema = new EdmFullBeanSchema();
    DataElement providerProxy = schema.getPathByLabel("Proxy");

    DataElement europeanaProxy = null;
    try {
      europeanaProxy = DataElement.copy(providerProxy);
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }

    assertNotNull(europeanaProxy);
    europeanaProxy.setPath(
      providerProxy.getPath().replace("false", "true"));

    assertEquals("$.['proxies'][?(@['europeanaProxy'] == false)]", providerProxy.getPath());
    assertEquals(56, providerProxy.getChildren().size());
    assertEquals(providerProxy.hashCode(), providerProxy.getChildren().get(0).getParent().hashCode());
    assertEquals("$.['proxies'][?(@['europeanaProxy'] == false)]",
      providerProxy.getChildren().get(0).getParent().getPath());
    assertEquals("$.['proxies'][?(@['europeanaProxy'] == false)][*]['about']",
      providerProxy.getChildren().get(0).getAbsolutePath());

    assertEquals("$.['proxies'][?(@['europeanaProxy'] == true)]", europeanaProxy.getPath());
    assertEquals(56, europeanaProxy.getChildren().size());
    assertEquals(europeanaProxy.hashCode(), europeanaProxy.getChildren().get(0).getParent().hashCode());
    assertEquals("$.['proxies'][?(@['europeanaProxy'] == true)]",
      europeanaProxy.getChildren().get(0).getParent().getPath());
    assertEquals("$.['proxies'][?(@['europeanaProxy'] == true)][*]['about']",
      europeanaProxy.getChildren().get(0).getAbsolutePath());
  }

  @Test
  public void testParent() {
    Schema schema = new EdmOaiPmhXmlSchema();
    DataElement providerProxyXml = schema.getPathByLabel("Proxy");
    assertEquals("//ore:Proxy[edm:europeanaProxy/text() = 'false']/@rdf:about",
      providerProxyXml.getChildren().get(0).getAbsolutePath(schema.getFormat()));

    schema = new EdmOaiPmhJsonSchema();
    DataElement providerProxyJson = schema.getPathByLabel("Proxy");
    assertEquals("$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')][*]['@about']",
      providerProxyJson.getChildren().get(0).getAbsolutePath(schema.getFormat()));

    schema = new EdmFullBeanSchema();
    providerProxyJson = schema.getPathByLabel("Proxy");
    assertEquals("$.['proxies'][?(@['europeanaProxy'] == false)][*]['about']",
      providerProxyJson.getChildren().get(0).getAbsolutePath(schema.getFormat()));
  }

  @Test
  public void constructWithSolr() {
    DataElement path = new DataElement("author", "author", "author");
    assertEquals("author", path.getLabel());
    assertEquals("author", path.getPath());
    assertEquals("author", path.getSolrFieldName());
  }

  @Test
  public void setLabel() {
    DataElement path1 = new DataElement("author", "author", "author");
    DataElement path2 = path1.setLabel("author2");
    assertEquals(path1, path2);
    assertTrue(path1.equals(path2));
    assertEquals("author2", path1.getLabel());
    assertEquals("author2", path2.getLabel());
  }

  @Test
  public void setSolrFieldName() {
    DataElement path1 = new DataElement("author", "author", "author");
    DataElement path2 = path1.setSolrFieldName("author2");
    assertEquals(path1, path2);
    assertTrue(path1.equals(path2));
    assertEquals("author2", path1.getSolrFieldName());
    assertEquals("author2", path2.getSolrFieldName());
  }

  @Test
  public void setExtractable_notChained() {
    DataElement path1 = new DataElement("author", "author", "author");
    DataElement path2 = path1.setExtractable();
    assertEquals(path1, path2);
    assertTrue(path1.equals(path2));
    assertTrue(path1.isExtractable());
    assertTrue(path2.isExtractable());
  }

  @Test
  public void setExtractable_chained() {
    DataElement path1 = new DataElement("author", "author", "author")
      .setExtractable();
    assertEquals("author", path1.getSolrFieldName());
    assertTrue(path1.isExtractable());

    DataElement path2 = new DataElement("author", "author", "author")
      .setExtractable(true);
    assertEquals("author", path2.getSolrFieldName());
    assertTrue(path2.isExtractable());

    DataElement path3 = new DataElement("author", "author", "author")
      .setExtractable(false);
    assertEquals("author", path3.getSolrFieldName());
    assertFalse(path3.isExtractable());
  }

  @Test
  public void getAbsolutePath() {
    DataElement path = new DataElement("author", "author", "author");
    assertEquals("author", path.getAbsolutePath(Format.CSV));
    assertEquals("author", path.getAbsolutePath(0));
  }

  @Test
  public void test_toString() {
    DataElement path = new DataElement("author", "author");
    assertEquals(
      "DataElement{label=author, path=author, categories=[], " +
        "solrFieldName=null, parent=null, identifier=null, nr_of_children=0, collection=false}",
      path.toString());
  }

  @Test
  public void test_toString_withParent() {
    DataElement path = new DataElement("author", "author")
      .setParent(new DataElement("book", "book"));
    assertEquals(
      "DataElement{label=author, path=author, categories=[], " +
        "solrFieldName=null, parent=book, identifier=null, nr_of_children=0, collection=false}",
      path.toString());
  }

  @Test
  public void test_toString_withIdentifier() {
    DataElement path = new DataElement("author", "author")
      .setParent(new DataElement("book", "book"))
      .setIdentifier(new DataElement("id"));
    assertEquals(
      "DataElement{label=author, path=author, categories=[], " +
        "solrFieldName=null, parent=book, identifier=id, nr_of_children=0, collection=false}",
      path.toString());
  }

  @Test
  public void setChildren() {
    DataElement path = new DataElement("author", "author")
      .setChildren(Arrays.asList(
        new DataElement("name", "name"),
        new DataElement("date", "date")
      ));

    assertEquals(2, path.getChildren().size());
    assertEquals("author", path.getLabel());
    assertEquals("name", path.getChildren().get(0).getLabel());
    assertEquals("date", path.getChildren().get(1).getLabel());
  }

}