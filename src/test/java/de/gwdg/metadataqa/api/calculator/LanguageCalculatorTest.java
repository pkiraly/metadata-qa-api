package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.schema.EdmOaiPmhXmlSchema;
import de.gwdg.metadataqa.api.util.FileUtils;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
public class LanguageCalculatorTest {

	public LanguageCalculatorTest() {
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
	public void testConstructor() {
		LanguageCalculator calculator = new LanguageCalculator(new EdmOaiPmhXmlSchema());
		assertNotNull(calculator);
	}

	@Test
	public void testMeasure() throws URISyntaxException, IOException {
		LanguageCalculator calculator = new LanguageCalculator(new EdmOaiPmhXmlSchema());
		JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLine("general/test.json"));
		calculator.measure(cache);
		assertNotNull(calculator.getCsv(false, true));
		assertEquals("de:1,_1:1,_1:1,_1:1,_1:1,_1:1,_0:1,_0:1,_1:1,_1:1,_1:1,_1:1,de:4;en:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_2:1,_1:1,_1:1,_0:1,_1:1,_2:1,en:1,_0:1,_1:1,_1:1,_2:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,de:1;sv:1;ru:1;pt:1;ja:1;en:1;it:1;pl:1;fr:1;zh:1;es:1;nl:1,_1:1,_1:1", calculator.getCsv(false, true));

		cache = new JsonPathCache(FileUtils.readFirstLine("general/test-place.json"));
		calculator.measure(cache);
		assertNotNull(calculator.getCsv(false, true));
		assertEquals("_0:1,_1:1,_0:11,_0:1,_1:1,_0:1,_0:3,_0:10,_0:1,_0:2,_0:2,_0:2,_0:3,_1:1,_1:1,_0:1,_0:1,_1:1,_1:1,_0:1,_1:1,_0:1,_1:1,_1:1,_1:1,_2:1,_0:1,_0:1,_1:1,_1:1,_2:1,_1:1,_2:2,_1:1,hi:1;ps:1;pt:3;hr:1;ht:1;hu:1;yi:2;hy:1;ia:1;id:2;qu:1;af:1;io:1;is:1;it:2;am:1;an:1;zh:1;ar:2;ja:2;rm:1;ro:1;be:1;ru:1;bg:1;bn:1;bo:2;sa:1;br:1;sc:1;bs:1;se:1;sh:1;ka:1;sk:1;sl:1;so:1;ca:1;sq:2;sr:2;km:1;sv:1;sw:1;ko:2;ks:1;ku:1;kw:1;co:1;ta:2;cs:1;tg:1;th:3;la:1;cy:1;lb:1;tl:1;to:1;da:1;li:1;tr:1;de:2;lo:1;ty:1;lt:1;lv:1;ug:1;dz:1;uk:2;ur:1;mk:1;ml:1;uz:1;mr:1;ms:2;mt:1;el:1;en:3;eo:2;es:3;et:1;eu:1;_0:7;vi:1;nb:1;ne:1;fa:1;nl:1;nn:1;no:1;fi:1;fr:2;fy:1;oc:1;ga:1;os:1;gl:1;gv:1;pl:1;he:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1", calculator.getCsv(false, true));
	}

	@Test
	public void testCountersGetLanguageMap() throws URISyntaxException, IOException {
		LanguageCalculator calculator = new LanguageCalculator(new EdmOaiPmhXmlSchema());
		JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLine("general/test.json"));
		calculator.measure(cache);
		String languages = calculator.getCsv(true, true);
		assertNotNull(languages);
		assertEquals("\"Proxy/dc:title\":de:1,\"Proxy/dcterms:alternative\":_1:1,\"Proxy/dc:description\":_1:1,\"Proxy/dc:creator\":_1:1,\"Proxy/dc:publisher\":_1:1,\"Proxy/dc:contributor\":_1:1,\"Proxy/dc:type\":_0:1,\"Proxy/dc:identifier\":_0:1,\"Proxy/dc:language\":_1:1,\"Proxy/dc:coverage\":_1:1,\"Proxy/dcterms:temporal\":_1:1,\"Proxy/dcterms:spatial\":_1:1,\"Proxy/dc:subject\":de:4;en:1,\"Proxy/dc:date\":_1:1,\"Proxy/dcterms:created\":_1:1,\"Proxy/dcterms:issued\":_1:1,\"Proxy/dcterms:extent\":_1:1,\"Proxy/dcterms:medium\":_1:1,\"Proxy/dcterms:provenance\":_1:1,\"Proxy/dcterms:hasPart\":_1:1,\"Proxy/dcterms:isPartOf\":_2:1,\"Proxy/dc:format\":_1:1,\"Proxy/dc:source\":_1:1,\"Proxy/dc:rights\":_0:1,\"Proxy/dc:relation\":_1:1,\"Aggregation/edm:rights\":_2:1,\"Aggregation/edm:provider\":en:1,\"Aggregation/edm:dataProvider\":_0:1,\"Aggregation/dc:rights\":_1:1,\"Aggregation/edm:ugc\":_1:1,\"Aggregation/edm:aggregatedCHO\":_2:1,\"Aggregation/edm:intermediateProvider\":_1:1,\"Place/dcterms:isPartOf\":_1:1,\"Place/dcterms:hasPart\":_1:1,\"Place/skos:prefLabel\":_1:1,\"Place/skos:altLabel\":_1:1,\"Place/skos:note\":_1:1,\"Agent/edm:begin\":_1:1,\"Agent/edm:end\":_1:1,\"Agent/edm:hasMet\":_1:1,\"Agent/edm:isRelatedTo\":_1:1,\"Agent/owl:sameAs\":_1:1,\"Agent/foaf:name\":_1:1,\"Agent/dc:date\":_1:1,\"Agent/dc:identifier\":_1:1,\"Agent/rdaGr2:dateOfBirth\":_1:1,\"Agent/rdaGr2:placeOfBirth\":_1:1,\"Agent/rdaGr2:dateOfDeath\":_1:1,\"Agent/rdaGr2:placeOfDeath\":_1:1,\"Agent/rdaGr2:dateOfEstablishment\":_1:1,\"Agent/rdaGr2:dateOfTermination\":_1:1,\"Agent/rdaGr2:gender\":_1:1,\"Agent/rdaGr2:professionOrOccupation\":_1:1,\"Agent/rdaGr2:biographicalInformation\":_1:1,\"Agent/skos:prefLabel\":_1:1,\"Agent/skos:altLabel\":_1:1,\"Agent/skos:note\":_1:1,\"Timespan/edm:begin\":_1:1,\"Timespan/edm:end\":_1:1,\"Timespan/dcterms:isPartOf\":_1:1,\"Timespan/dcterms:hasPart\":_1:1,\"Timespan/edm:isNextInSequence\":_1:1,\"Timespan/owl:sameAs\":_1:1,\"Timespan/skos:prefLabel\":_1:1,\"Timespan/skos:altLabel\":_1:1,\"Timespan/skos:note\":_1:1,\"Concept/skos:broader\":_1:1,\"Concept/skos:narrower\":_1:1,\"Concept/skos:related\":_1:1,\"Concept/skos:broadMatch\":_1:1,\"Concept/skos:narrowMatch\":_1:1,\"Concept/skos:relatedMatch\":_1:1,\"Concept/skos:exactMatch\":_1:1,\"Concept/skos:closeMatch\":_1:1,\"Concept/skos:notation\":_1:1,\"Concept/skos:inScheme\":_1:1,\"Concept/skos:prefLabel\":de:1;sv:1;ru:1;pt:1;ja:1;en:1;it:1;pl:1;fr:1;zh:1;es:1;nl:1,\"Concept/skos:altLabel\":_1:1,\"Concept/skos:note\":_1:1", languages);

		cache = new JsonPathCache(FileUtils.readFirstLine("general/test-place.json"));
		calculator.measure(cache);
		languages = calculator.getCsv(true, true);
		assertNotNull(languages);
		assertEquals("\"Proxy/dc:title\":_0:1,\"Proxy/dcterms:alternative\":_1:1,\"Proxy/dc:description\":_0:11,\"Proxy/dc:creator\":_0:1,\"Proxy/dc:publisher\":_1:1,\"Proxy/dc:contributor\":_0:1,\"Proxy/dc:type\":_0:3,\"Proxy/dc:identifier\":_0:10,\"Proxy/dc:language\":_0:1,\"Proxy/dc:coverage\":_0:2,\"Proxy/dcterms:temporal\":_0:2,\"Proxy/dcterms:spatial\":_0:2,\"Proxy/dc:subject\":_0:3,\"Proxy/dc:date\":_1:1,\"Proxy/dcterms:created\":_1:1,\"Proxy/dcterms:issued\":_0:1,\"Proxy/dcterms:extent\":_0:1,\"Proxy/dcterms:medium\":_1:1,\"Proxy/dcterms:provenance\":_1:1,\"Proxy/dcterms:hasPart\":_0:1,\"Proxy/dcterms:isPartOf\":_1:1,\"Proxy/dc:format\":_0:1,\"Proxy/dc:source\":_1:1,\"Proxy/dc:rights\":_1:1,\"Proxy/dc:relation\":_1:1,\"Aggregation/edm:rights\":_2:1,\"Aggregation/edm:provider\":_0:1,\"Aggregation/edm:dataProvider\":_0:1,\"Aggregation/dc:rights\":_1:1,\"Aggregation/edm:ugc\":_1:1,\"Aggregation/edm:aggregatedCHO\":_2:1,\"Aggregation/edm:intermediateProvider\":_1:1,\"Place/dcterms:isPartOf\":_2:2,\"Place/dcterms:hasPart\":_1:1,\"Place/skos:prefLabel\":hi:1;ps:1;pt:3;hr:1;ht:1;hu:1;yi:2;hy:1;ia:1;id:2;qu:1;af:1;io:1;is:1;it:2;am:1;an:1;zh:1;ar:2;ja:2;rm:1;ro:1;be:1;ru:1;bg:1;bn:1;bo:2;sa:1;br:1;sc:1;bs:1;se:1;sh:1;ka:1;sk:1;sl:1;so:1;ca:1;sq:2;sr:2;km:1;sv:1;sw:1;ko:2;ks:1;ku:1;kw:1;co:1;ta:2;cs:1;tg:1;th:3;la:1;cy:1;lb:1;tl:1;to:1;da:1;li:1;tr:1;de:2;lo:1;ty:1;lt:1;lv:1;ug:1;dz:1;uk:2;ur:1;mk:1;ml:1;uz:1;mr:1;ms:2;mt:1;el:1;en:3;eo:2;es:3;et:1;eu:1;_0:7;vi:1;nb:1;ne:1;fa:1;nl:1;nn:1;no:1;fi:1;fr:2;fy:1;oc:1;ga:1;os:1;gl:1;gv:1;pl:1;he:1,\"Place/skos:altLabel\":_1:1,\"Place/skos:note\":_1:1,\"Agent/edm:begin\":_1:1,\"Agent/edm:end\":_1:1,\"Agent/edm:hasMet\":_1:1,\"Agent/edm:isRelatedTo\":_1:1,\"Agent/owl:sameAs\":_1:1,\"Agent/foaf:name\":_1:1,\"Agent/dc:date\":_1:1,\"Agent/dc:identifier\":_1:1,\"Agent/rdaGr2:dateOfBirth\":_1:1,\"Agent/rdaGr2:placeOfBirth\":_1:1,\"Agent/rdaGr2:dateOfDeath\":_1:1,\"Agent/rdaGr2:placeOfDeath\":_1:1,\"Agent/rdaGr2:dateOfEstablishment\":_1:1,\"Agent/rdaGr2:dateOfTermination\":_1:1,\"Agent/rdaGr2:gender\":_1:1,\"Agent/rdaGr2:professionOrOccupation\":_1:1,\"Agent/rdaGr2:biographicalInformation\":_1:1,\"Agent/skos:prefLabel\":_1:1,\"Agent/skos:altLabel\":_1:1,\"Agent/skos:note\":_1:1,\"Timespan/edm:begin\":_1:1,\"Timespan/edm:end\":_1:1,\"Timespan/dcterms:isPartOf\":_1:1,\"Timespan/dcterms:hasPart\":_1:1,\"Timespan/edm:isNextInSequence\":_1:1,\"Timespan/owl:sameAs\":_1:1,\"Timespan/skos:prefLabel\":_1:1,\"Timespan/skos:altLabel\":_1:1,\"Timespan/skos:note\":_1:1,\"Concept/skos:broader\":_1:1,\"Concept/skos:narrower\":_1:1,\"Concept/skos:related\":_1:1,\"Concept/skos:broadMatch\":_1:1,\"Concept/skos:narrowMatch\":_1:1,\"Concept/skos:relatedMatch\":_1:1,\"Concept/skos:exactMatch\":_1:1,\"Concept/skos:closeMatch\":_1:1,\"Concept/skos:notation\":_1:1,\"Concept/skos:inScheme\":_1:1,\"Concept/skos:prefLabel\":_1:1,\"Concept/skos:altLabel\":_1:1,\"Concept/skos:note\":_1:1", languages);

	}

	@Test
	public void testGetLanguageMap() throws URISyntaxException, IOException {
		LanguageCalculator calculator = new LanguageCalculator(new EdmOaiPmhXmlSchema());
		JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLine("general/test.json"));
		calculator.measure(cache);

		Map<String, String> languages = calculator.getLanguageMap();
		assertNotNull(languages);
		assertEquals(79, languages.size());
		assertEquals("de:1", languages.get("Proxy/dc:title"));
		assertEquals("_1:1", languages.get("Proxy/dcterms:alternative"));
		assertEquals("_1:1", languages.get("Proxy/dc:description"));
		assertEquals("_1:1", languages.get("Proxy/dc:creator"));
		assertEquals("_1:1", languages.get("Proxy/dc:publisher"));
		assertEquals("_1:1", languages.get("Proxy/dc:contributor"));
		assertEquals("_0:1", languages.get("Proxy/dc:type"));
		assertEquals("_0:1", languages.get("Proxy/dc:identifier"));
		assertEquals("_1:1", languages.get("Proxy/dc:language"));
		assertEquals("_1:1", languages.get("Proxy/dc:coverage"));
		assertEquals("_1:1", languages.get("Proxy/dcterms:temporal"));
		assertEquals("_1:1", languages.get("Proxy/dcterms:spatial"));
		assertEquals("de:4;en:1", languages.get("Proxy/dc:subject"));
		assertEquals("_1:1", languages.get("Proxy/dc:date"));
		assertEquals("_1:1", languages.get("Proxy/dcterms:created"));
		assertEquals("_1:1", languages.get("Proxy/dcterms:issued"));
		assertEquals("_1:1", languages.get("Proxy/dcterms:extent"));
		assertEquals("_1:1", languages.get("Proxy/dcterms:medium"));
		assertEquals("_1:1", languages.get("Proxy/dcterms:provenance"));
		assertEquals("_1:1", languages.get("Proxy/dcterms:hasPart"));
		assertEquals("_2:1", languages.get("Proxy/dcterms:isPartOf"));
		assertEquals("_1:1", languages.get("Proxy/dc:format"));
		assertEquals("_1:1", languages.get("Proxy/dc:source"));
		assertEquals("_0:1", languages.get("Proxy/dc:rights"));
		assertEquals("_1:1", languages.get("Proxy/dc:relation"));
		assertEquals("_2:1", languages.get("Aggregation/edm:rights"));
		assertEquals("en:1", languages.get("Aggregation/edm:provider"));
		assertEquals("_0:1", languages.get("Aggregation/edm:dataProvider"));
	}

	@Test
	public void testGetLanguageMapWithPlace() throws URISyntaxException, IOException {
		LanguageCalculator calculator = new LanguageCalculator(new EdmOaiPmhXmlSchema());
		JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLine("general/test-place.json"));
		calculator.measure(cache);

		Map<String, String> languages = calculator.getLanguageMap();
		assertNotNull(languages);
		assertEquals(79, languages.size());
		assertEquals("_0:1", languages.get("Proxy/dc:title"));
		assertEquals("_1:1", languages.get("Proxy/dcterms:alternative"));
		assertEquals("_0:11", languages.get("Proxy/dc:description"));
		assertEquals("_0:1", languages.get("Proxy/dc:creator"));
		assertEquals("_1:1", languages.get("Proxy/dc:publisher"));
		assertEquals("_0:1", languages.get("Proxy/dc:contributor"));
		assertEquals("_0:3", languages.get("Proxy/dc:type"));
		assertEquals("_0:10", languages.get("Proxy/dc:identifier"));
		assertEquals("_0:1", languages.get("Proxy/dc:language"));
		assertEquals("_0:2", languages.get("Proxy/dc:coverage"));
		assertEquals("_0:2", languages.get("Proxy/dcterms:temporal"));
		assertEquals("_0:2", languages.get("Proxy/dcterms:spatial"));
		assertEquals("_0:3", languages.get("Proxy/dc:subject"));
		assertEquals("_1:1", languages.get("Proxy/dc:date"));
		assertEquals("_1:1", languages.get("Proxy/dcterms:created"));
		assertEquals("_0:1", languages.get("Proxy/dcterms:issued"));
		assertEquals("_0:1", languages.get("Proxy/dcterms:extent"));
		assertEquals("_1:1", languages.get("Proxy/dcterms:medium"));
		assertEquals("_1:1", languages.get("Proxy/dcterms:provenance"));
		assertEquals("_0:1", languages.get("Proxy/dcterms:hasPart"));
		assertEquals("_1:1", languages.get("Proxy/dcterms:isPartOf"));
		assertEquals("_0:1", languages.get("Proxy/dc:format"));
		assertEquals("_1:1", languages.get("Proxy/dc:source"));
		assertEquals("_1:1", languages.get("Proxy/dc:rights"));
		assertEquals("_1:1", languages.get("Proxy/dc:relation"));

		assertEquals("_2:1", languages.get("Aggregation/edm:rights"));
		assertEquals("_0:1", languages.get("Aggregation/edm:provider"));
		assertEquals("_0:1", languages.get("Aggregation/edm:dataProvider"));

		assertEquals("_2:2", languages.get("Place/dcterms:isPartOf"));
		assertEquals("_1:1", languages.get("Place/dcterms:hasPart"));
		assertEquals("hi:1;ps:1;pt:3;hr:1;ht:1;hu:1;yi:2;hy:1;ia:1;id:2;qu:1;af:1;io:1;is:1;it:2;am:1;an:1;zh:1;ar:2;ja:2;rm:1;ro:1;be:1;ru:1;bg:1;bn:1;bo:2;sa:1;br:1;sc:1;bs:1;se:1;sh:1;ka:1;sk:1;sl:1;so:1;ca:1;sq:2;sr:2;km:1;sv:1;sw:1;ko:2;ks:1;ku:1;kw:1;co:1;ta:2;cs:1;tg:1;th:3;la:1;cy:1;lb:1;tl:1;to:1;da:1;li:1;tr:1;de:2;lo:1;ty:1;lt:1;lv:1;ug:1;dz:1;uk:2;ur:1;mk:1;ml:1;uz:1;mr:1;ms:2;mt:1;el:1;en:3;eo:2;es:3;et:1;eu:1;_0:7;vi:1;nb:1;ne:1;fa:1;nl:1;nn:1;no:1;fi:1;fr:2;fy:1;oc:1;ga:1;os:1;gl:1;gv:1;pl:1;he:1", languages.get("Place/skos:prefLabel"));
		assertEquals("_1:1", languages.get("Place/skos:altLabel"));
		assertEquals("_1:1", languages.get("Place/skos:note"));
	}

	@Test
	public void testGetHeaders() {
		LanguageCalculator calculator = new LanguageCalculator(new EdmOaiPmhXmlSchema());
		List<String> expected = Arrays.asList("lang:Proxy/dc:title", "lang:Proxy/dcterms:alternative", "lang:Proxy/dc:description", "lang:Proxy/dc:creator", "lang:Proxy/dc:publisher", "lang:Proxy/dc:contributor", "lang:Proxy/dc:type", "lang:Proxy/dc:identifier", "lang:Proxy/dc:language", "lang:Proxy/dc:coverage", "lang:Proxy/dcterms:temporal", "lang:Proxy/dcterms:spatial", "lang:Proxy/dc:subject", "lang:Proxy/dc:date", "lang:Proxy/dcterms:created", "lang:Proxy/dcterms:issued", "lang:Proxy/dcterms:extent", "lang:Proxy/dcterms:medium", "lang:Proxy/dcterms:provenance", "lang:Proxy/dcterms:hasPart", "lang:Proxy/dcterms:isPartOf", "lang:Proxy/dc:format", "lang:Proxy/dc:source", "lang:Proxy/dc:rights", "lang:Proxy/dc:relation", "lang:Aggregation/edm:rights", "lang:Aggregation/edm:provider", "lang:Aggregation/edm:dataProvider", "lang:Aggregation/dc:rights", "lang:Aggregation/edm:ugc", "lang:Aggregation/edm:aggregatedCHO", "lang:Aggregation/edm:intermediateProvider", "lang:Place/dcterms:isPartOf", "lang:Place/dcterms:hasPart", "lang:Place/skos:prefLabel", "lang:Place/skos:altLabel", "lang:Place/skos:note", "lang:Agent/edm:begin", "lang:Agent/edm:end", "lang:Agent/edm:hasMet", "lang:Agent/edm:isRelatedTo", "lang:Agent/owl:sameAs", "lang:Agent/foaf:name", "lang:Agent/dc:date", "lang:Agent/dc:identifier", "lang:Agent/rdaGr2:dateOfBirth", "lang:Agent/rdaGr2:placeOfBirth", "lang:Agent/rdaGr2:dateOfDeath", "lang:Agent/rdaGr2:placeOfDeath", "lang:Agent/rdaGr2:dateOfEstablishment", "lang:Agent/rdaGr2:dateOfTermination", "lang:Agent/rdaGr2:gender", "lang:Agent/rdaGr2:professionOrOccupation", "lang:Agent/rdaGr2:biographicalInformation", "lang:Agent/skos:prefLabel", "lang:Agent/skos:altLabel", "lang:Agent/skos:note", "lang:Timespan/edm:begin", "lang:Timespan/edm:end", "lang:Timespan/dcterms:isPartOf", "lang:Timespan/dcterms:hasPart", "lang:Timespan/edm:isNextInSequence", "lang:Timespan/owl:sameAs", "lang:Timespan/skos:prefLabel", "lang:Timespan/skos:altLabel", "lang:Timespan/skos:note", "lang:Concept/skos:broader", "lang:Concept/skos:narrower", "lang:Concept/skos:related", "lang:Concept/skos:broadMatch", "lang:Concept/skos:narrowMatch", "lang:Concept/skos:relatedMatch", "lang:Concept/skos:exactMatch", "lang:Concept/skos:closeMatch", "lang:Concept/skos:notation", "lang:Concept/skos:inScheme", "lang:Concept/skos:prefLabel", "lang:Concept/skos:altLabel", "lang:Concept/skos:note");
		assertEquals(79, calculator.getHeader().size());
		assertEquals(expected, calculator.getHeader());
	}
}
