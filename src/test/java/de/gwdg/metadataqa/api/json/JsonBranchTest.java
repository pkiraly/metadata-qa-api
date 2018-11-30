package de.gwdg.metadataqa.api.json;

import de.gwdg.metadataqa.api.schema.EdmFullBeanSchema;
import de.gwdg.metadataqa.api.schema.Schema;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
}
