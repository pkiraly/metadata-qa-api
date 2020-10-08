package de.gwdg.metadataqa.api.json;

import de.gwdg.metadataqa.api.schema.*;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class JsonBranchTest {

  @Test
  public void IfCloned_ObjectAreDifferent() {
    Schema schema = new EdmFullBeanSchema();
    JsonBranch providerProxy = schema.getPathByLabel("Proxy");

    JsonBranch europeanaProxy = null;
    try {
      europeanaProxy = (JsonBranch) providerProxy.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }

    assertNotNull(europeanaProxy);
    europeanaProxy.setJsonPath(
      providerProxy.getJsonPath().replace("false", "true"));

    assertEquals("$.['proxies'][?(@['europeanaProxy'] == false)]", providerProxy.getJsonPath());
    assertEquals(56, providerProxy.getChildren().size());
    assertEquals(providerProxy.hashCode(), providerProxy.getChildren().get(0).getParent().hashCode());
    assertEquals("$.['proxies'][?(@['europeanaProxy'] == false)]",
      providerProxy.getChildren().get(0).getParent().getJsonPath());
    assertEquals("$.['proxies'][?(@['europeanaProxy'] == false)][*]['about']",
      providerProxy.getChildren().get(0).getAbsoluteJsonPath());

    assertEquals("$.['proxies'][?(@['europeanaProxy'] == true)]", europeanaProxy.getJsonPath());
    assertEquals(56, europeanaProxy.getChildren().size());
    assertEquals(europeanaProxy.hashCode(), europeanaProxy.getChildren().get(0).getParent().hashCode());
    assertEquals("$.['proxies'][?(@['europeanaProxy'] == true)]",
      europeanaProxy.getChildren().get(0).getParent().getJsonPath());
    assertEquals("$.['proxies'][?(@['europeanaProxy'] == true)][*]['about']",
      europeanaProxy.getChildren().get(0).getAbsoluteJsonPath());
  }

  @Test
  public void testParent() {
    Schema schema = new EdmOaiPmhXmlSchema();
    JsonBranch providerProxyXml = schema.getPathByLabel("Proxy");
    assertEquals("//ore:Proxy[edm:europeanaProxy/text() = 'false']/@rdf:about",
      providerProxyXml.getChildren().get(0).getAbsoluteJsonPath(schema.getFormat()));

    schema = new EdmOaiPmhJsonSchema();
    JsonBranch providerProxyJson = schema.getPathByLabel("Proxy");
    assertEquals("$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')][*]['@about']",
      providerProxyJson.getChildren().get(0).getAbsoluteJsonPath(schema.getFormat()));

    schema = new EdmFullBeanSchema();
    providerProxyJson = schema.getPathByLabel("Proxy");
    assertEquals("$.['proxies'][?(@['europeanaProxy'] == false)][*]['about']",
      providerProxyJson.getChildren().get(0).getAbsoluteJsonPath(schema.getFormat()));
  }

  @Test
  public void constructWithSolr() {
    JsonBranch path = new JsonBranch("author", "author", "author");
    assertEquals("author", path.getLabel());
    assertEquals("author", path.getJsonPath());
    assertEquals("author", path.getSolrFieldName());
  }

  @Test
  public void setLabel() {
    JsonBranch path1 = new JsonBranch("author", "author", "author");
    JsonBranch path2 = path1.setLabel("author2");
    assertEquals(path1, path2);
    assertTrue(path1.equals(path2));
    assertEquals("author2", path1.getLabel());
    assertEquals("author2", path2.getLabel());
  }

  @Test
  public void setSolrFieldName() {
    JsonBranch path1 = new JsonBranch("author", "author", "author");
    JsonBranch path2 = path1.setSolrFieldName("author2");
    assertEquals(path1, path2);
    assertTrue(path1.equals(path2));
    assertEquals("author2", path1.getSolrFieldName());
    assertEquals("author2", path2.getSolrFieldName());
  }

  @Test
  public void setExtractable_notChained() {
    JsonBranch path1 = new JsonBranch("author", "author", "author");
    JsonBranch path2 = path1.setExtractable();
    assertEquals(path1, path2);
    assertTrue(path1.equals(path2));
    assertTrue(path1.isExtractable());
    assertTrue(path2.isExtractable());
  }

  @Test
  public void setExtractable_chained() {
    JsonBranch path1 = new JsonBranch("author", "author", "author")
      .setExtractable();
    assertEquals("author", path1.getSolrFieldName());
    assertTrue(path1.isExtractable());

    JsonBranch path2 = new JsonBranch("author", "author", "author")
      .setExtractable(true);
    assertEquals("author", path2.getSolrFieldName());
    assertTrue(path2.isExtractable());

    JsonBranch path3 = new JsonBranch("author", "author", "author")
      .setExtractable(false);
    assertEquals("author", path3.getSolrFieldName());
    assertFalse(path3.isExtractable());
  }

  @Test
  public void getAbsolutePath() {
    JsonBranch path = new JsonBranch("author", "author", "author");
    assertEquals("author", path.getAbsoluteJsonPath(Format.CSV));
    assertEquals("author", path.getAbsoluteJsonPath(0));
  }

  @Test
  public void test_toString() {
    JsonBranch path1 = new JsonBranch("author", "author");
    assertEquals(
      "JsonBranch{label=author, jsonPath=author, categories=[], " +
        "solrFieldName=null, parent=null, identifier=null, nr_of_children=0, collection=false}",
      path1.toString());
  }

  @Test
  public void test_toString_withParent() {
    JsonBranch path1 = new JsonBranch("author", "author")
      .setParent(new JsonBranch("book", "book"));
    assertEquals(
      "JsonBranch{label=author, jsonPath=author, categories=[], " +
        "solrFieldName=null, parent=book, identifier=null, nr_of_children=0, collection=false}",
      path1.toString());
  }

  @Test
  public void test_toString_withIdentifier() {
    JsonBranch path1 = new JsonBranch("author", "author")
      .setParent(new JsonBranch("book", "book"))
      .setIdentifier(new JsonBranch("id"));
    assertEquals(
      "JsonBranch{label=author, jsonPath=author, categories=[], " +
        "solrFieldName=null, parent=book, identifier=id, nr_of_children=0, collection=false}",
      path1.toString());
  }

  @Test
  public void setChildren() {
    JsonBranch path = new JsonBranch("author", "author")
      .setChildren(Arrays.asList(
        new JsonBranch("name", "name"),
        new JsonBranch("date", "date")
      ));

    assertEquals(2, path.getChildren().size());
    assertEquals("author", path.getLabel());
    assertEquals("name", path.getChildren().get(0).getLabel());
    assertEquals("date", path.getChildren().get(1).getLabel());
  }

}