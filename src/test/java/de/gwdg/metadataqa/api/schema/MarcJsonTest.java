package de.gwdg.metadataqa.api.schema;

import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.util.Converter;
import de.gwdg.metadataqa.api.util.FileUtils;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class MarcJsonTest {

	JsonPathCache cache;
	Schema schema = new MarcJsonSchema();

	public MarcJsonTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() throws URISyntaxException, IOException {
		cache = new JsonPathCache(FileUtils.readFirstLine("general/marc.json"));
	}

	@After
	public void tearDown() {
	}

	@Test
	public void hello() {
		List<XmlFieldInstance> values;

		for (JsonBranch branch : schema.getRootChildrenPaths()) {
			// System.err.println(branch.getLabel() + ": " + cache.read(branch.getJsonPath(), null).getClass());
			if (branch.isCollection()) {
				Object rawJsonFragment = cache.getFragment(branch.getJsonPath());
				if (rawJsonFragment != null) {
					List<Object> jsonFragments = Converter.jsonObjectToList(rawJsonFragment);
					for (int i = 0, len = jsonFragments.size(); i < len; i++) {
						Object jsonFragment = jsonFragments.get(i);
						for (JsonBranch child : branch.getChildren()) {
							String address = String.format("%s/%d/%s", branch.getJsonPath(), i, child.getJsonPath());
							values = cache.get(address, child.getJsonPath(), jsonFragment);
							printValues(values, child);
						}
					}
				}
			} else {
				values = (List<XmlFieldInstance>) cache.get(branch.getJsonPath());
				printValues(values, branch);
			}
		}
	}

	private void printValues(List<XmlFieldInstance> values, JsonBranch branch) {
		if (values != null)
			for (XmlFieldInstance value : values)
				System.err.println(String.format("%s: '%s'", branch.getLabel(), value.getValue()));
	}
}
