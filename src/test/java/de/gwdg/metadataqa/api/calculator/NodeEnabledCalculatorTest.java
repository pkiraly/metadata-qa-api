package de.gwdg.metadataqa.api.calculator;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.schema.EdmOaiPmhXmlSchema;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.FileUtils;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import net.minidev.json.JSONArray;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class NodeEnabledCalculatorTest {

	Schema schema = new EdmOaiPmhXmlSchema();

	public NodeEnabledCalculatorTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@Before
	public void setUp() {
	}

	@Test
	public void hello() throws URISyntaxException, IOException {
		String jsonString = FileUtils.readFirstLine("general/test.json");

		Object jsonDocument = Configuration.defaultConfiguration().jsonProvider().parse(jsonString);
		for (JsonBranch collectionBranch : schema.getCollectionPaths()) {
			Object rawCollection = null;
			try {
				rawCollection = JsonPath.read(jsonDocument, collectionBranch.getJsonPath());
			} catch (PathNotFoundException e) {}
			if (rawCollection != null) {
				if (rawCollection instanceof JSONArray) {
					JSONArray collection = (JSONArray)rawCollection;
					collection.forEach(
						node -> {
							processNode(node, collectionBranch.getChildren());
						}
					);
				} else {
					processNode(rawCollection, collectionBranch.getChildren());
				}
			}
		}
	}

	private void processNode(Object node, List<JsonBranch> fields) {
		for (JsonBranch fieldBranch : fields) {
			try {
				Object val = JsonPath.read(node, fieldBranch.getJsonPath());
				if (val != null) {
				}
			} catch (PathNotFoundException e) {}
		}
	}
}
