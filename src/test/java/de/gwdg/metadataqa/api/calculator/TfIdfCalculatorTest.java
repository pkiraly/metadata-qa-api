package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.schema.EdmOaiPmhXmlSchema;
import java.util.Arrays;
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
public class TfIdfCalculatorTest {
	
	public TfIdfCalculatorTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	@Test
	public void testGetHeaders() {
		TfIdfCalculator calculator = new TfIdfCalculator(new EdmOaiPmhXmlSchema());
		List<String> expected = Arrays.asList(
			"dc:title:sum", "dc:title:avg", "dcterms:alternative:sum",
			"dcterms:alternative:avg", "dc:description:sum", "dc:description:avg"
		);
		assertEquals(6, calculator.getHeader().size());
		assertEquals(expected, calculator.getHeader());
	}

	@Test
	public void getCalculatorName() throws Exception {
		TfIdfCalculator calculator = new TfIdfCalculator(new EdmOaiPmhXmlSchema());
		assertEquals("uniqueness", calculator.getCalculatorName());
	}

	@Test
	public void measure() throws Exception {
	}

	@Test
	public void getTermsCollection() throws Exception {
	}

	@Test
	public void enableTermCollection() throws Exception {
	}

	@Test
	public void getResultMap() throws Exception {
	}

	@Test
	public void getLabelledResultMap() throws Exception {
	}

	@Test
	public void getCsv() throws Exception {
	}

	@Test
	public void getHeader() throws Exception {
	}

	@Test
	public void getSolrHost() throws Exception {
	}

	@Test
	public void setSolrHost() throws Exception {
	}

	@Test
	public void getSolrPort() throws Exception {
	}

	@Test
	public void setSolrPort() throws Exception {
	}

	@Test
	public void getSolrPath() throws Exception {
	}

	@Test
	public void setSolrPath() throws Exception {
	}

	@Test
	public void setSolr() throws Exception {
	}

	@Test
	public void getSolrSearchPath() throws Exception {
	}


}
