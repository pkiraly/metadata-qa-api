package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.schema.EdmOaiPmhXmlSchema;
import de.gwdg.metadataqa.api.util.CompressionLevel;
import de.gwdg.metadataqa.api.util.FileUtils;
import java.io.IOException;
import java.net.URISyntaxException;
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
public class TruncatedIDTest {

	JsonPathCache cache;
	FieldExtractor calculator;

	public TruncatedIDTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() throws URISyntaxException, IOException {
		calculator = new FieldExtractor(new EdmOaiPmhXmlSchema());
		String jsonContent = FileUtils.readFirstLine("issue-examples/issue41-truncatedID.json");
		cache = new JsonPathCache(jsonContent);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void truncation() {
		calculator.measure(cache);
		String csv = calculator.getCsv(true, CompressionLevel.NORMAL);
		assertEquals("\"recordId\":9200365/BibliographicResource_3000059507130,\"dataset\":9200365_Ag_EU_TEL_a0142_Gallica,\"dataProvider\":National Library of France", csv);
		assertEquals(
			"9200365/BibliographicResource_3000059507130",
			calculator.getResultMap()
				.get(calculator.FIELD_NAME)
		);
		assertEquals(3, calculator.getLabelledResultMap().get(calculator.getCalculatorName()).size());
		assertEquals(
			"9200365/BibliographicResource_3000059507130", 
			calculator
				.getLabelledResultMap()
					.get(calculator.getCalculatorName())
						.get(calculator.FIELD_NAME)
		);
		assertEquals(
			"9200365/BibliographicResource_3000059507130", 
			calculator
				.getLabelledResultMap()
					.get(calculator.getCalculatorName())
						.get("recordId")
		);
		assertEquals(
			"National Library of France", 
			calculator
				.getLabelledResultMap()
					.get(calculator.getCalculatorName())
						.get("dataProvider")
		);
		assertEquals(
			"9200365_Ag_EU_TEL_a0142_Gallica", 
			calculator
				.getLabelledResultMap()
					.get(calculator.getCalculatorName())
						.get("dataset")
		);
	}
}
