package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.schema.EdmOaiPmhXmlSchema;
import de.gwdg.metadataqa.api.util.CompressionLevel;
import de.gwdg.metadataqa.api.util.FileUtils;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
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
		assertNotNull(calculator.getCsv(false, CompressionLevel.NORMAL));
		assertEquals("de:1,_1:1,_1:1,_1:1,_1:1,_1:1,_0:1,_0:1,_1:1,_1:1,_1:1,_1:1,de:4;en:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_2:1,_1:1,_1:1,_0:1,_1:1,_1:1,_1:1,_1:1,_0:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_2:1,en:1,_0:1,_1:1,_1:1,_2:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,de:1;en:1;es:1;fr:1;it:1;ja:1;nl:1;pl:1;pt:1;ru:1;sv:1;zh:1,_1:1,_1:1", 
				calculator.getCsv(false, CompressionLevel.NORMAL));

		cache = new JsonPathCache(FileUtils.readFirstLine("general/test-place.json"));
		calculator.measure(cache);
		assertNotNull(calculator.getCsv(false, CompressionLevel.NORMAL));
		assertEquals("_0:1,_1:1,_0:11,_0:1,_1:1,_0:1,_0:3,_0:10,_0:1,_0:2,_0:2,_0:2,_0:3,_1:1,_1:1,_0:1,_0:1,_1:1,_1:1,_0:1,_1:1,_0:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_2:1,_0:1,_0:1,_1:1,_1:1,_2:1,_1:1,_1:1;_2:2,_1:1,_0:7;af:1;am:1;an:1;ar:2;be:1;bg:1;bn:1;bo:2;br:1;bs:1;ca:1;co:1;cs:1;cy:1;da:1;de:2;dz:1;el:1;en:3;eo:2;es:3;et:1;eu:1;fa:1;fi:1;fr:2;fy:1;ga:1;gl:1;gv:1;he:1;hi:1;hr:1;ht:1;hu:1;hy:1;ia:1;id:2;io:1;is:1;it:2;ja:2;ka:1;km:1;ko:2;ks:1;ku:1;kw:1;la:1;lb:1;li:1;lo:1;lt:1;lv:1;mk:1;ml:1;mr:1;ms:2;mt:1;nb:1;ne:1;nl:1;nn:1;no:1;oc:1;os:1;pl:1;ps:1;pt:3;qu:1;rm:1;ro:1;ru:1;sa:1;sc:1;se:1;sh:1;sk:1;sl:1;so:1;sq:2;sr:2;sv:1;sw:1;ta:2;tg:1;th:3;tl:1;to:1;tr:1;ty:1;ug:1;uk:2;ur:1;uz:1;vi:1;yi:2;zh:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_0:4,_0:4,_1:1;_2:2,_1:1,_1:1,_1:1,_0:2;en:2;ru:2,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1,_1:1", calculator.getCsv(false, CompressionLevel.NORMAL));
	}

	@Test
	public void testCountersGetLanguageMap() throws URISyntaxException, IOException {
		LanguageCalculator calculator = new LanguageCalculator(new EdmOaiPmhXmlSchema());
		JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLine("general/test.json"));
		calculator.measure(cache);
		String languages = calculator.getCsv(true, CompressionLevel.NORMAL);
		assertNotNull(languages);
		assertEquals("\"Proxy/dc:title\":de:1,\"Proxy/dcterms:alternative\":_1:1,\"Proxy/dc:description\":_1:1,\"Proxy/dc:creator\":_1:1,\"Proxy/dc:publisher\":_1:1,\"Proxy/dc:contributor\":_1:1,\"Proxy/dc:type\":_0:1,\"Proxy/dc:identifier\":_0:1,\"Proxy/dc:language\":_1:1,\"Proxy/dc:coverage\":_1:1,\"Proxy/dcterms:temporal\":_1:1,\"Proxy/dcterms:spatial\":_1:1,\"Proxy/dc:subject\":de:4;en:1,\"Proxy/dc:date\":_1:1,\"Proxy/dcterms:created\":_1:1,\"Proxy/dcterms:issued\":_1:1,\"Proxy/dcterms:extent\":_1:1,\"Proxy/dcterms:medium\":_1:1,\"Proxy/dcterms:provenance\":_1:1,\"Proxy/dcterms:hasPart\":_1:1,\"Proxy/dcterms:isPartOf\":_2:1,\"Proxy/dc:format\":_1:1,\"Proxy/dc:source\":_1:1,\"Proxy/dc:rights\":_0:1,\"Proxy/dc:relation\":_1:1,\"Proxy/edm:year\":_1:1,\"Proxy/edm:userTag\":_1:1,\"Proxy/dcterms:conformsTo\":_1:1,\"Proxy/dcterms:hasFormat\":_0:1,\"Proxy/dcterms:hasVersion\":_1:1,\"Proxy/dcterms:isFormatOf\":_1:1,\"Proxy/dcterms:isReferencedBy\":_1:1,\"Proxy/dcterms:isReplacedBy\":_1:1,\"Proxy/dcterms:isRequiredBy\":_1:1,\"Proxy/dcterms:isVersionOf\":_1:1,\"Proxy/dcterms:references\":_1:1,\"Proxy/dcterms:replaces\":_1:1,\"Proxy/dcterms:requires\":_1:1,\"Proxy/dcterms:tableOfContents\":_1:1,\"Proxy/edm:currentLocation\":_1:1,\"Proxy/edm:hasMet\":_1:1,\"Proxy/edm:hasType\":_1:1,\"Proxy/edm:incorporates\":_1:1,\"Proxy/edm:isDerivativeOf\":_1:1,\"Proxy/edm:isRelatedTo\":_1:1,\"Proxy/edm:isRepresentationOf\":_1:1,\"Proxy/edm:isSimilarTo\":_1:1,\"Proxy/edm:isSuccessorOf\":_1:1,\"Proxy/edm:realizes\":_1:1,\"Proxy/edm:wasPresentAt\":_1:1,\"Aggregation/edm:rights\":_2:1,\"Aggregation/edm:provider\":en:1,\"Aggregation/edm:dataProvider\":_0:1,\"Aggregation/dc:rights\":_1:1,\"Aggregation/edm:ugc\":_1:1,\"Aggregation/edm:aggregatedCHO\":_2:1,\"Aggregation/edm:intermediateProvider\":_1:1,\"Place/dcterms:isPartOf\":_1:1,\"Place/dcterms:hasPart\":_1:1,\"Place/skos:prefLabel\":_1:1,\"Place/skos:altLabel\":_1:1,\"Place/skos:note\":_1:1,\"Agent/edm:begin\":_1:1,\"Agent/edm:end\":_1:1,\"Agent/edm:hasMet\":_1:1,\"Agent/edm:isRelatedTo\":_1:1,\"Agent/owl:sameAs\":_1:1,\"Agent/foaf:name\":_1:1,\"Agent/dc:date\":_1:1,\"Agent/dc:identifier\":_1:1,\"Agent/rdaGr2:dateOfBirth\":_1:1,\"Agent/rdaGr2:placeOfBirth\":_1:1,\"Agent/rdaGr2:dateOfDeath\":_1:1,\"Agent/rdaGr2:placeOfDeath\":_1:1,\"Agent/rdaGr2:dateOfEstablishment\":_1:1,\"Agent/rdaGr2:dateOfTermination\":_1:1,\"Agent/rdaGr2:gender\":_1:1,\"Agent/rdaGr2:professionOrOccupation\":_1:1,\"Agent/rdaGr2:biographicalInformation\":_1:1,\"Agent/skos:prefLabel\":_1:1,\"Agent/skos:altLabel\":_1:1,\"Agent/skos:note\":_1:1,\"Timespan/edm:begin\":_1:1,\"Timespan/edm:end\":_1:1,\"Timespan/dcterms:isPartOf\":_1:1,\"Timespan/dcterms:hasPart\":_1:1,\"Timespan/edm:isNextInSequence\":_1:1,\"Timespan/owl:sameAs\":_1:1,\"Timespan/skos:prefLabel\":_1:1,\"Timespan/skos:altLabel\":_1:1,\"Timespan/skos:note\":_1:1,\"Concept/skos:broader\":_1:1,\"Concept/skos:narrower\":_1:1,\"Concept/skos:related\":_1:1,\"Concept/skos:broadMatch\":_1:1,\"Concept/skos:narrowMatch\":_1:1,\"Concept/skos:relatedMatch\":_1:1,\"Concept/skos:exactMatch\":_1:1,\"Concept/skos:closeMatch\":_1:1,\"Concept/skos:notation\":_1:1,\"Concept/skos:inScheme\":_1:1,\"Concept/skos:prefLabel\":de:1;en:1;es:1;fr:1;it:1;ja:1;nl:1;pl:1;pt:1;ru:1;sv:1;zh:1,\"Concept/skos:altLabel\":_1:1,\"Concept/skos:note\":_1:1", languages);

		cache = new JsonPathCache(FileUtils.readFirstLine("general/test-place.json"));
		calculator.measure(cache);
		languages = calculator.getCsv(true, CompressionLevel.NORMAL);
		assertNotNull(languages);
		assertEquals("\"Proxy/dc:title\":_0:1,\"Proxy/dcterms:alternative\":_1:1,\"Proxy/dc:description\":_0:11,\"Proxy/dc:creator\":_0:1,\"Proxy/dc:publisher\":_1:1,\"Proxy/dc:contributor\":_0:1,\"Proxy/dc:type\":_0:3,\"Proxy/dc:identifier\":_0:10,\"Proxy/dc:language\":_0:1,\"Proxy/dc:coverage\":_0:2,\"Proxy/dcterms:temporal\":_0:2,\"Proxy/dcterms:spatial\":_0:2,\"Proxy/dc:subject\":_0:3,\"Proxy/dc:date\":_1:1,\"Proxy/dcterms:created\":_1:1,\"Proxy/dcterms:issued\":_0:1,\"Proxy/dcterms:extent\":_0:1,\"Proxy/dcterms:medium\":_1:1,\"Proxy/dcterms:provenance\":_1:1,\"Proxy/dcterms:hasPart\":_0:1,\"Proxy/dcterms:isPartOf\":_1:1,\"Proxy/dc:format\":_0:1,\"Proxy/dc:source\":_1:1,\"Proxy/dc:rights\":_1:1,\"Proxy/dc:relation\":_1:1,\"Proxy/edm:year\":_1:1,\"Proxy/edm:userTag\":_1:1,\"Proxy/dcterms:conformsTo\":_1:1,\"Proxy/dcterms:hasFormat\":_1:1,\"Proxy/dcterms:hasVersion\":_1:1,\"Proxy/dcterms:isFormatOf\":_1:1,\"Proxy/dcterms:isReferencedBy\":_1:1,\"Proxy/dcterms:isReplacedBy\":_1:1,\"Proxy/dcterms:isRequiredBy\":_1:1,\"Proxy/dcterms:isVersionOf\":_1:1,\"Proxy/dcterms:references\":_1:1,\"Proxy/dcterms:replaces\":_1:1,\"Proxy/dcterms:requires\":_1:1,\"Proxy/dcterms:tableOfContents\":_1:1,\"Proxy/edm:currentLocation\":_1:1,\"Proxy/edm:hasMet\":_1:1,\"Proxy/edm:hasType\":_1:1,\"Proxy/edm:incorporates\":_1:1,\"Proxy/edm:isDerivativeOf\":_1:1,\"Proxy/edm:isRelatedTo\":_1:1,\"Proxy/edm:isRepresentationOf\":_1:1,\"Proxy/edm:isSimilarTo\":_1:1,\"Proxy/edm:isSuccessorOf\":_1:1,\"Proxy/edm:realizes\":_1:1,\"Proxy/edm:wasPresentAt\":_1:1,\"Aggregation/edm:rights\":_2:1,\"Aggregation/edm:provider\":_0:1,\"Aggregation/edm:dataProvider\":_0:1,\"Aggregation/dc:rights\":_1:1,\"Aggregation/edm:ugc\":_1:1,\"Aggregation/edm:aggregatedCHO\":_2:1,\"Aggregation/edm:intermediateProvider\":_1:1,\"Place/dcterms:isPartOf\":_1:1;_2:2,\"Place/dcterms:hasPart\":_1:1,\"Place/skos:prefLabel\":_0:7;af:1;am:1;an:1;ar:2;be:1;bg:1;bn:1;bo:2;br:1;bs:1;ca:1;co:1;cs:1;cy:1;da:1;de:2;dz:1;el:1;en:3;eo:2;es:3;et:1;eu:1;fa:1;fi:1;fr:2;fy:1;ga:1;gl:1;gv:1;he:1;hi:1;hr:1;ht:1;hu:1;hy:1;ia:1;id:2;io:1;is:1;it:2;ja:2;ka:1;km:1;ko:2;ks:1;ku:1;kw:1;la:1;lb:1;li:1;lo:1;lt:1;lv:1;mk:1;ml:1;mr:1;ms:2;mt:1;nb:1;ne:1;nl:1;nn:1;no:1;oc:1;os:1;pl:1;ps:1;pt:3;qu:1;rm:1;ro:1;ru:1;sa:1;sc:1;se:1;sh:1;sk:1;sl:1;so:1;sq:2;sr:2;sv:1;sw:1;ta:2;tg:1;th:3;tl:1;to:1;tr:1;ty:1;ug:1;uk:2;ur:1;uz:1;vi:1;yi:2;zh:1,\"Place/skos:altLabel\":_1:1,\"Place/skos:note\":_1:1,\"Agent/edm:begin\":_1:1,\"Agent/edm:end\":_1:1,\"Agent/edm:hasMet\":_1:1,\"Agent/edm:isRelatedTo\":_1:1,\"Agent/owl:sameAs\":_1:1,\"Agent/foaf:name\":_1:1,\"Agent/dc:date\":_1:1,\"Agent/dc:identifier\":_1:1,\"Agent/rdaGr2:dateOfBirth\":_1:1,\"Agent/rdaGr2:placeOfBirth\":_1:1,\"Agent/rdaGr2:dateOfDeath\":_1:1,\"Agent/rdaGr2:placeOfDeath\":_1:1,\"Agent/rdaGr2:dateOfEstablishment\":_1:1,\"Agent/rdaGr2:dateOfTermination\":_1:1,\"Agent/rdaGr2:gender\":_1:1,\"Agent/rdaGr2:professionOrOccupation\":_1:1,\"Agent/rdaGr2:biographicalInformation\":_1:1,\"Agent/skos:prefLabel\":_1:1,\"Agent/skos:altLabel\":_1:1,\"Agent/skos:note\":_1:1,\"Timespan/edm:begin\":_0:4,\"Timespan/edm:end\":_0:4,\"Timespan/dcterms:isPartOf\":_1:1;_2:2,\"Timespan/dcterms:hasPart\":_1:1,\"Timespan/edm:isNextInSequence\":_1:1,\"Timespan/owl:sameAs\":_1:1,\"Timespan/skos:prefLabel\":_0:2;en:2;ru:2,\"Timespan/skos:altLabel\":_1:1,\"Timespan/skos:note\":_1:1,\"Concept/skos:broader\":_1:1,\"Concept/skos:narrower\":_1:1,\"Concept/skos:related\":_1:1,\"Concept/skos:broadMatch\":_1:1,\"Concept/skos:narrowMatch\":_1:1,\"Concept/skos:relatedMatch\":_1:1,\"Concept/skos:exactMatch\":_1:1,\"Concept/skos:closeMatch\":_1:1,\"Concept/skos:notation\":_1:1,\"Concept/skos:inScheme\":_1:1,\"Concept/skos:prefLabel\":_1:1,\"Concept/skos:altLabel\":_1:1,\"Concept/skos:note\":_1:1", languages);

	}

	@Test
	public void testGetLanguageMap() throws URISyntaxException, IOException {
		LanguageCalculator calculator = new LanguageCalculator(new EdmOaiPmhXmlSchema());
		JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLine("general/test.json"));
		calculator.measure(cache);

		Map<String, String> languages = calculator.getLanguageMap();
		assertNotNull(languages);
		assertEquals(104, languages.size());
		assertEquals("de:1", languages.get("Proxy/dc:title"));
		languages.remove("Proxy/dc:title");
		assertEquals("_1:1", languages.get("Proxy/dcterms:alternative"));
		languages.remove("Proxy/dcterms:alternative");
		assertEquals("_1:1", languages.get("Proxy/dc:description"));
		languages.remove("Proxy/dc:description");
		assertEquals("_1:1", languages.get("Proxy/dc:creator"));
		languages.remove("Proxy/dc:creator");
		assertEquals("_1:1", languages.get("Proxy/dc:publisher"));
		languages.remove("Proxy/dc:publisher");
		assertEquals("_1:1", languages.get("Proxy/dc:contributor"));
		languages.remove("Proxy/dc:contributor");
		assertEquals("_0:1", languages.get("Proxy/dc:type"));
		languages.remove("Proxy/dc:type");
		assertEquals("_0:1", languages.get("Proxy/dc:identifier"));
		languages.remove("Proxy/dc:identifier");
		assertEquals("_1:1", languages.get("Proxy/dc:language"));
		languages.remove("Proxy/dc:language");
		assertEquals("_1:1", languages.get("Proxy/dc:coverage"));
		languages.remove("Proxy/dc:coverage");
		assertEquals("_1:1", languages.get("Proxy/dcterms:temporal"));
		languages.remove("Proxy/dcterms:temporal");
		assertEquals("_1:1", languages.get("Proxy/dcterms:spatial"));
		languages.remove("Proxy/dcterms:spatial");
		assertEquals("de:4;en:1", languages.get("Proxy/dc:subject"));
		languages.remove("Proxy/dc:subject");
		assertEquals("_1:1", languages.get("Proxy/dc:date"));
		languages.remove("Proxy/dc:date");
		assertEquals("_1:1", languages.get("Proxy/dcterms:created"));
		languages.remove("Proxy/dcterms:created");
		assertEquals("_1:1", languages.get("Proxy/dcterms:issued"));
		languages.remove("Proxy/dcterms:issued");
		assertEquals("_1:1", languages.get("Proxy/dcterms:extent"));
		languages.remove("Proxy/dcterms:extent");
		assertEquals("_1:1", languages.get("Proxy/dcterms:medium"));
		languages.remove("Proxy/dcterms:medium");
		assertEquals("_1:1", languages.get("Proxy/dcterms:provenance"));
		languages.remove("Proxy/dcterms:provenance");
		assertEquals("_1:1", languages.get("Proxy/dcterms:hasPart"));
		languages.remove("Proxy/dcterms:hasPart");
		assertEquals("_2:1", languages.get("Proxy/dcterms:isPartOf"));
		languages.remove("Proxy/dcterms:isPartOf");
		assertEquals("_1:1", languages.get("Proxy/dc:format"));
		languages.remove("Proxy/dc:format");
		assertEquals("_1:1", languages.get("Proxy/dc:source"));
		languages.remove("Proxy/dc:source");
		assertEquals("_0:1", languages.get("Proxy/dc:rights"));
		languages.remove("Proxy/dc:rights");
		assertEquals("_1:1", languages.get("Proxy/dc:relation"));
		languages.remove("Proxy/dc:relation");
		assertEquals("_2:1", languages.get("Aggregation/edm:rights"));
		languages.remove("Aggregation/edm:rights");
		assertEquals("en:1", languages.get("Aggregation/edm:provider"));
		languages.remove("Aggregation/edm:provider");
		assertEquals("_0:1", languages.get("Aggregation/edm:dataProvider"));
		languages.remove("Aggregation/edm:dataProvider");

		assertEquals("_1:1", languages.get("Proxy/edm:year")); languages.remove("Proxy/edm:year");
		assertEquals("_1:1", languages.get("Proxy/edm:userTag")); languages.remove("Proxy/edm:userTag");
		assertEquals("_1:1", languages.get("Proxy/dcterms:conformsTo")); languages.remove("Proxy/dcterms:conformsTo");
		assertEquals("_0:1", languages.get("Proxy/dcterms:hasFormat")); languages.remove("Proxy/dcterms:hasFormat");
		assertEquals("_1:1", languages.get("Proxy/dcterms:hasVersion")); languages.remove("Proxy/dcterms:hasVersion");
		assertEquals("_1:1", languages.get("Proxy/dcterms:isFormatOf")); languages.remove("Proxy/dcterms:isFormatOf");
		assertEquals("_1:1", languages.get("Proxy/dcterms:isReferencedBy")); languages.remove("Proxy/dcterms:isReferencedBy");
		assertEquals("_1:1", languages.get("Proxy/dcterms:isReplacedBy")); languages.remove("Proxy/dcterms:isReplacedBy");
		assertEquals("_1:1", languages.get("Proxy/dcterms:isRequiredBy")); languages.remove("Proxy/dcterms:isRequiredBy");
		assertEquals("_1:1", languages.get("Proxy/dcterms:isVersionOf")); languages.remove("Proxy/dcterms:isVersionOf");
		assertEquals("_1:1", languages.get("Proxy/dcterms:references")); languages.remove("Proxy/dcterms:references");
		assertEquals("_1:1", languages.get("Proxy/dcterms:replaces")); languages.remove("Proxy/dcterms:replaces");
		assertEquals("_1:1", languages.get("Proxy/dcterms:requires")); languages.remove("Proxy/dcterms:requires");
		assertEquals("_1:1", languages.get("Proxy/dcterms:tableOfContents")); languages.remove("Proxy/dcterms:tableOfContents");
		assertEquals("_1:1", languages.get("Proxy/edm:currentLocation")); languages.remove("Proxy/edm:currentLocation");
		assertEquals("_1:1", languages.get("Proxy/edm:hasMet")); languages.remove("Proxy/edm:hasMet");
		assertEquals("_1:1", languages.get("Proxy/edm:hasType")); languages.remove("Proxy/edm:hasType");
		assertEquals("_1:1", languages.get("Proxy/edm:incorporates")); languages.remove("Proxy/edm:incorporates");
		assertEquals("_1:1", languages.get("Proxy/edm:isDerivativeOf")); languages.remove("Proxy/edm:isDerivativeOf");
		assertEquals("_1:1", languages.get("Proxy/edm:isRelatedTo")); languages.remove("Proxy/edm:isRelatedTo");
		assertEquals("_1:1", languages.get("Proxy/edm:isRepresentationOf")); languages.remove("Proxy/edm:isRepresentationOf");
		assertEquals("_1:1", languages.get("Proxy/edm:isSimilarTo")); languages.remove("Proxy/edm:isSimilarTo");
		assertEquals("_1:1", languages.get("Proxy/edm:isSuccessorOf")); languages.remove("Proxy/edm:isSuccessorOf");
		assertEquals("_1:1", languages.get("Proxy/edm:realizes")); languages.remove("Proxy/edm:realizes");
		assertEquals("_1:1", languages.get("Proxy/edm:wasPresentAt")); languages.remove("Proxy/edm:wasPresentAt");
		assertEquals("_1:1", languages.get("Aggregation/dc:rights")); languages.remove("Aggregation/dc:rights");
		assertEquals("_1:1", languages.get("Aggregation/edm:ugc")); languages.remove("Aggregation/edm:ugc");
		assertEquals("_2:1", languages.get("Aggregation/edm:aggregatedCHO")); languages.remove("Aggregation/edm:aggregatedCHO");
		assertEquals("_1:1", languages.get("Aggregation/edm:intermediateProvider")); languages.remove("Aggregation/edm:intermediateProvider");
		assertEquals("_1:1", languages.get("Place/dcterms:isPartOf")); languages.remove("Place/dcterms:isPartOf");
		assertEquals("_1:1", languages.get("Place/dcterms:hasPart")); languages.remove("Place/dcterms:hasPart");
		assertEquals("_1:1", languages.get("Place/skos:prefLabel")); languages.remove("Place/skos:prefLabel");
		assertEquals("_1:1", languages.get("Place/skos:altLabel")); languages.remove("Place/skos:altLabel");
		assertEquals("_1:1", languages.get("Place/skos:note")); languages.remove("Place/skos:note");
		assertEquals("_1:1", languages.get("Agent/edm:begin")); languages.remove("Agent/edm:begin");
		assertEquals("_1:1", languages.get("Agent/edm:end")); languages.remove("Agent/edm:end");
		assertEquals("_1:1", languages.get("Agent/edm:hasMet")); languages.remove("Agent/edm:hasMet");
		assertEquals("_1:1", languages.get("Agent/edm:isRelatedTo")); languages.remove("Agent/edm:isRelatedTo");
		assertEquals("_1:1", languages.get("Agent/owl:sameAs")); languages.remove("Agent/owl:sameAs");
		assertEquals("_1:1", languages.get("Agent/foaf:name")); languages.remove("Agent/foaf:name");
		assertEquals("_1:1", languages.get("Agent/dc:date")); languages.remove("Agent/dc:date");
		assertEquals("_1:1", languages.get("Agent/dc:identifier")); languages.remove("Agent/dc:identifier");
		assertEquals("_1:1", languages.get("Agent/rdaGr2:dateOfBirth")); languages.remove("Agent/rdaGr2:dateOfBirth");
		assertEquals("_1:1", languages.get("Agent/rdaGr2:placeOfBirth")); languages.remove("Agent/rdaGr2:placeOfBirth");
		assertEquals("_1:1", languages.get("Agent/rdaGr2:dateOfDeath")); languages.remove("Agent/rdaGr2:dateOfDeath");
		assertEquals("_1:1", languages.get("Agent/rdaGr2:placeOfDeath")); languages.remove("Agent/rdaGr2:placeOfDeath");
		assertEquals("_1:1", languages.get("Agent/rdaGr2:dateOfEstablishment")); languages.remove("Agent/rdaGr2:dateOfEstablishment");
		assertEquals("_1:1", languages.get("Agent/rdaGr2:dateOfTermination")); languages.remove("Agent/rdaGr2:dateOfTermination");
		assertEquals("_1:1", languages.get("Agent/rdaGr2:gender")); languages.remove("Agent/rdaGr2:gender");
		assertEquals("_1:1", languages.get("Agent/rdaGr2:professionOrOccupation")); languages.remove("Agent/rdaGr2:professionOrOccupation");
		assertEquals("_1:1", languages.get("Agent/rdaGr2:biographicalInformation")); languages.remove("Agent/rdaGr2:biographicalInformation");
		assertEquals("_1:1", languages.get("Agent/skos:prefLabel")); languages.remove("Agent/skos:prefLabel");
		assertEquals("_1:1", languages.get("Agent/skos:altLabel")); languages.remove("Agent/skos:altLabel");
		assertEquals("_1:1", languages.get("Agent/skos:note")); languages.remove("Agent/skos:note");
		assertEquals("_1:1", languages.get("Timespan/edm:begin")); languages.remove("Timespan/edm:begin");
		assertEquals("_1:1", languages.get("Timespan/edm:end")); languages.remove("Timespan/edm:end");
		assertEquals("_1:1", languages.get("Timespan/dcterms:isPartOf")); languages.remove("Timespan/dcterms:isPartOf");
		assertEquals("_1:1", languages.get("Timespan/dcterms:hasPart")); languages.remove("Timespan/dcterms:hasPart");
		assertEquals("_1:1", languages.get("Timespan/edm:isNextInSequence")); languages.remove("Timespan/edm:isNextInSequence");
		assertEquals("_1:1", languages.get("Timespan/owl:sameAs")); languages.remove("Timespan/owl:sameAs");
		assertEquals("_1:1", languages.get("Timespan/skos:prefLabel")); languages.remove("Timespan/skos:prefLabel");
		assertEquals("_1:1", languages.get("Timespan/skos:altLabel")); languages.remove("Timespan/skos:altLabel");
		assertEquals("_1:1", languages.get("Timespan/skos:note")); languages.remove("Timespan/skos:note");
		assertEquals("_1:1", languages.get("Concept/skos:broader")); languages.remove("Concept/skos:broader");
		assertEquals("_1:1", languages.get("Concept/skos:narrower")); languages.remove("Concept/skos:narrower");
		assertEquals("_1:1", languages.get("Concept/skos:related")); languages.remove("Concept/skos:related");
		assertEquals("_1:1", languages.get("Concept/skos:broadMatch")); languages.remove("Concept/skos:broadMatch");
		assertEquals("_1:1", languages.get("Concept/skos:narrowMatch")); languages.remove("Concept/skos:narrowMatch");
		assertEquals("_1:1", languages.get("Concept/skos:relatedMatch")); languages.remove("Concept/skos:relatedMatch");
		assertEquals("_1:1", languages.get("Concept/skos:exactMatch")); languages.remove("Concept/skos:exactMatch");
		assertEquals("_1:1", languages.get("Concept/skos:closeMatch")); languages.remove("Concept/skos:closeMatch");
		assertEquals("_1:1", languages.get("Concept/skos:notation")); languages.remove("Concept/skos:notation");
		assertEquals("_1:1", languages.get("Concept/skos:inScheme")); languages.remove("Concept/skos:inScheme");
		assertEquals("de:1;en:1;es:1;fr:1;it:1;ja:1;nl:1;pl:1;pt:1;ru:1;sv:1;zh:1", languages.get("Concept/skos:prefLabel")); languages.remove("Concept/skos:prefLabel");
		assertEquals("_1:1", languages.get("Concept/skos:altLabel")); languages.remove("Concept/skos:altLabel");
		assertEquals("_1:1", languages.get("Concept/skos:note")); languages.remove("Concept/skos:note");

		assertEquals("Language has the following values: " + StringUtils.join(languages.keySet(), ", "), 
			0, languages.size());
	}

	@Test
	public void testGetLanguageMapWithPlace() throws URISyntaxException, IOException {
		LanguageCalculator calculator = new LanguageCalculator(new EdmOaiPmhXmlSchema());
		JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLine("general/test-place.json"));
		calculator.measure(cache);

		Map<String, String> languages = calculator.getLanguageMap();
		assertNotNull(languages);
		assertEquals(104, languages.size());
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

		assertEquals("_1:1;_2:2", languages.get("Place/dcterms:isPartOf"));
		assertEquals("_1:1", languages.get("Place/dcterms:hasPart"));
		assertEquals("_0:7;af:1;am:1;an:1;ar:2;be:1;bg:1;bn:1;bo:2;br:1;bs:1;ca:1;co:1;cs:1;cy:1;da:1;de:2;dz:1;el:1;en:3;eo:2;es:3;et:1;eu:1;fa:1;fi:1;fr:2;fy:1;ga:1;gl:1;gv:1;he:1;hi:1;hr:1;ht:1;hu:1;hy:1;ia:1;id:2;io:1;is:1;it:2;ja:2;ka:1;km:1;ko:2;ks:1;ku:1;kw:1;la:1;lb:1;li:1;lo:1;lt:1;lv:1;mk:1;ml:1;mr:1;ms:2;mt:1;nb:1;ne:1;nl:1;nn:1;no:1;oc:1;os:1;pl:1;ps:1;pt:3;qu:1;rm:1;ro:1;ru:1;sa:1;sc:1;se:1;sh:1;sk:1;sl:1;so:1;sq:2;sr:2;sv:1;sw:1;ta:2;tg:1;th:3;tl:1;to:1;tr:1;ty:1;ug:1;uk:2;ur:1;uz:1;vi:1;yi:2;zh:1", languages.get("Place/skos:prefLabel"));
		assertEquals("_1:1", languages.get("Place/skos:altLabel"));
		assertEquals("_1:1", languages.get("Place/skos:note"));
	}

	@Test
	public void testGetHeaders() {
		LanguageCalculator calculator = new LanguageCalculator(new EdmOaiPmhXmlSchema());
		List<String> expected = Arrays.asList("lang:Proxy/dc:title", "lang:Proxy/dcterms:alternative", "lang:Proxy/dc:description", "lang:Proxy/dc:creator", "lang:Proxy/dc:publisher", "lang:Proxy/dc:contributor", "lang:Proxy/dc:type", "lang:Proxy/dc:identifier", "lang:Proxy/dc:language", "lang:Proxy/dc:coverage", "lang:Proxy/dcterms:temporal", "lang:Proxy/dcterms:spatial", "lang:Proxy/dc:subject", "lang:Proxy/dc:date", "lang:Proxy/dcterms:created", "lang:Proxy/dcterms:issued", "lang:Proxy/dcterms:extent", "lang:Proxy/dcterms:medium", "lang:Proxy/dcterms:provenance", "lang:Proxy/dcterms:hasPart", "lang:Proxy/dcterms:isPartOf", "lang:Proxy/dc:format", "lang:Proxy/dc:source", "lang:Proxy/dc:rights", "lang:Proxy/dc:relation", "lang:Proxy/edm:year", "lang:Proxy/edm:userTag", "lang:Proxy/dcterms:conformsTo", "lang:Proxy/dcterms:hasFormat", "lang:Proxy/dcterms:hasVersion", "lang:Proxy/dcterms:isFormatOf", "lang:Proxy/dcterms:isReferencedBy", "lang:Proxy/dcterms:isReplacedBy", "lang:Proxy/dcterms:isRequiredBy", "lang:Proxy/dcterms:isVersionOf", "lang:Proxy/dcterms:references", "lang:Proxy/dcterms:replaces", "lang:Proxy/dcterms:requires", "lang:Proxy/dcterms:tableOfContents", "lang:Proxy/edm:currentLocation", "lang:Proxy/edm:hasMet", "lang:Proxy/edm:hasType", "lang:Proxy/edm:incorporates", "lang:Proxy/edm:isDerivativeOf", "lang:Proxy/edm:isRelatedTo", "lang:Proxy/edm:isRepresentationOf", "lang:Proxy/edm:isSimilarTo", "lang:Proxy/edm:isSuccessorOf", "lang:Proxy/edm:realizes", "lang:Proxy/edm:wasPresentAt", "lang:Aggregation/edm:rights", "lang:Aggregation/edm:provider", "lang:Aggregation/edm:dataProvider", "lang:Aggregation/dc:rights", "lang:Aggregation/edm:ugc", "lang:Aggregation/edm:aggregatedCHO", "lang:Aggregation/edm:intermediateProvider", "lang:Place/dcterms:isPartOf", "lang:Place/dcterms:hasPart", "lang:Place/skos:prefLabel", "lang:Place/skos:altLabel", "lang:Place/skos:note", "lang:Agent/edm:begin", "lang:Agent/edm:end", "lang:Agent/edm:hasMet", "lang:Agent/edm:isRelatedTo", "lang:Agent/owl:sameAs", "lang:Agent/foaf:name", "lang:Agent/dc:date", "lang:Agent/dc:identifier", "lang:Agent/rdaGr2:dateOfBirth", "lang:Agent/rdaGr2:placeOfBirth", "lang:Agent/rdaGr2:dateOfDeath", "lang:Agent/rdaGr2:placeOfDeath", "lang:Agent/rdaGr2:dateOfEstablishment", "lang:Agent/rdaGr2:dateOfTermination", "lang:Agent/rdaGr2:gender", "lang:Agent/rdaGr2:professionOrOccupation", "lang:Agent/rdaGr2:biographicalInformation", "lang:Agent/skos:prefLabel", "lang:Agent/skos:altLabel", "lang:Agent/skos:note", "lang:Timespan/edm:begin", "lang:Timespan/edm:end", "lang:Timespan/dcterms:isPartOf", "lang:Timespan/dcterms:hasPart", "lang:Timespan/edm:isNextInSequence", "lang:Timespan/owl:sameAs", "lang:Timespan/skos:prefLabel", "lang:Timespan/skos:altLabel", "lang:Timespan/skos:note", "lang:Concept/skos:broader", "lang:Concept/skos:narrower", "lang:Concept/skos:related", "lang:Concept/skos:broadMatch", "lang:Concept/skos:narrowMatch", "lang:Concept/skos:relatedMatch", "lang:Concept/skos:exactMatch", "lang:Concept/skos:closeMatch", "lang:Concept/skos:notation", "lang:Concept/skos:inScheme", "lang:Concept/skos:prefLabel", "lang:Concept/skos:altLabel", "lang:Concept/skos:note");
		assertEquals(104, calculator.getHeader().size());
		assertEquals(expected, calculator.getHeader());
	}
}
