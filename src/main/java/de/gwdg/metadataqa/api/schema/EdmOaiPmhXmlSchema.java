package de.gwdg.metadataqa.api.schema;

import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.JsonBranch;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The Europeana Data Model (EDM) representation of the metadata schema interface.
 * This class represents what fields will be analyzed in different measurements.
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class EdmOaiPmhXmlSchema extends EdmSchema implements Serializable {

	private final static List<JsonBranch> paths = new ArrayList<>();
	private final static List<FieldGroup> fieldGroups = new ArrayList<>();
	private final static List<String> noLanguageFields = new ArrayList<>();
	private final static Map<String, String> solrFields = new LinkedHashMap<>();
	private final static Map<String, String> extractableFields = new LinkedHashMap<>();
	private final static List<String> emptyStrings = new ArrayList<>();
	private final static String longSubjectPath =
		"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:subject']";
	private final static String titlePath =
		"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:title']";
	private final static String descriptionPath =
		"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:description']";

	static {
		paths.add(new JsonBranch("edm:ProvidedCHO/@about",
			"$.['edm:ProvidedCHO'][0]['@about']",
			JsonBranch.Category.MANDATORY));
		paths.add(new JsonBranch("Proxy/dc:title",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:title']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY,
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.MULTILINGUALITY));
		paths.add(new JsonBranch("Proxy/dcterms:alternative",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:alternative']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.IDENTIFICATION,
			JsonBranch.Category.MULTILINGUALITY));
		paths.add(new JsonBranch("Proxy/dc:description",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:description']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY,
			JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.MULTILINGUALITY));
		paths.add(new JsonBranch("Proxy/dc:creator",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:creator']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION,
			JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/dc:publisher",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:publisher']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dc:contributor",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:contributor']",
			JsonBranch.Category.SEARCHABILITY));
		paths.add(new JsonBranch("Proxy/dc:type",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:type']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION,
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/dc:identifier",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:identifier']",
			JsonBranch.Category.IDENTIFICATION));
		paths.add(new JsonBranch("Proxy/dc:language",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:language']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.MULTILINGUALITY));
		paths.add(new JsonBranch("Proxy/dc:coverage",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:coverage']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION,
			JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/dcterms:temporal",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:temporal']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/dcterms:spatial",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:spatial']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION,
			JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/dc:subject",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:subject']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY,
			JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.MULTILINGUALITY));
		paths.add(new JsonBranch("Proxy/dc:date",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:date']",
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.BROWSING, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dcterms:created",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:created']",
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dcterms:issued",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:issued']",
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dcterms:extent",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:extent']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dcterms:medium",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:medium']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dcterms:provenance",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:provenance']",
			JsonBranch.Category.DESCRIPTIVENESS));
		paths.add(new JsonBranch("Proxy/dcterms:hasPart",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:hasPart']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/dcterms:isPartOf",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:isPartOf']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/dc:format",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:format']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dc:source",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:source']",
			JsonBranch.Category.DESCRIPTIVENESS));
		paths.add(new JsonBranch("Proxy/dc:rights",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:rights']",
			JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dc:relation",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:relation']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/edm:isNextInSequence",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['edm:isNextInSequence']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/edm:type",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['edm:type']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.BROWSING));
		/*
		paths.add(new JsonBranch("Proxy/edm:rights",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['edm:rights']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.REUSABILITY));
		*/
		paths.add(new JsonBranch("Proxy/edm:europeanaProxy", 
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['edm:europeanaProxy']"));
		paths.add(new JsonBranch("Proxy/edm:year",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['edm:year']"));
		paths.add(new JsonBranch("Proxy/edm:userTag",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['edm:userTag']"));
		paths.add(new JsonBranch("Proxy/ore:proxyIn",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['ore:proxyIn']"));
		paths.add(new JsonBranch("Proxy/ore:proxyFor",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['ore:proxyFor']"));
		paths.add(new JsonBranch("Proxy/dc:conformsTo",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:conformsTo']"));
		paths.add(new JsonBranch("Proxy/dcterms:hasFormat",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:hasFormat']"));
		paths.add(new JsonBranch("Proxy/dcterms:hasVersion",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:hasVersion']"));
		paths.add(new JsonBranch("Proxy/dcterms:isFormatOf",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:isFormatOf']"));
		paths.add(new JsonBranch("Proxy/dcterms:isReferencedBy",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:isReferencedBy']"));
		paths.add(new JsonBranch("Proxy/dcterms:isReplacedBy",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:isReplacedBy']"));
		paths.add(new JsonBranch("Proxy/dcterms:isRequiredBy",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:isRequiredBy']"));
		paths.add(new JsonBranch("Proxy/dcterms:isVersionOf",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:isVersionOf']"));
		paths.add(new JsonBranch("Proxy/dcterms:references",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:references']"));
		paths.add(new JsonBranch("Proxy/dcterms:replaces",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:replaces']"));
		paths.add(new JsonBranch("Proxy/dcterms:requires",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:requires']"));
		paths.add(new JsonBranch("Proxy/dcterms:tableOfContents",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:tableOfContents']"));
		paths.add(new JsonBranch("Proxy/edm:currentLocation",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['edm:currentLocation']"));
		paths.add(new JsonBranch("Proxy/edm:hasMet",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['edm:hasMet']"));
		paths.add(new JsonBranch("Proxy/edm:hasType",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['edm:hasType']"));
		paths.add(new JsonBranch("Proxy/edm:incorporates",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['edm:incorporates']"));
		paths.add(new JsonBranch("Proxy/edm:isDerivativeOf",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['edm:isDerivativeOf']"));
		paths.add(new JsonBranch("Proxy/edm:isRelatedTo",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['edm:isRelatedTo']"));
		paths.add(new JsonBranch("Proxy/edm:isRepresentationOf",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['edm:isRepresentationOf']"));
		paths.add(new JsonBranch("Proxy/edm:isSimilarTo",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['edm:isSimilarTo']"));
		paths.add(new JsonBranch("Proxy/edm:isSuccessorOf",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['edm:isSuccessorOf']"));
		paths.add(new JsonBranch("Proxy/edm:realizes",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['edm:realizes']"));
		paths.add(new JsonBranch("Proxy/edm:wasPresentAt",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['edm:wasPresentAt']"));

		paths.add(new JsonBranch("Aggregation/edm:rights", "$.['ore:Aggregation'][0]['edm:rights']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Aggregation/edm:provider", "$.['ore:Aggregation'][0]['edm:provider']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.IDENTIFICATION));
		paths.add(new JsonBranch("Aggregation/edm:dataProvider", "$.['ore:Aggregation'][0]['edm:dataProvider']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.SEARCHABILITY,
			JsonBranch.Category.IDENTIFICATION));
		paths.add(new JsonBranch("Aggregation/edm:isShownAt", "$.['ore:Aggregation'][0]['edm:isShownAt']",
			JsonBranch.Category.BROWSING, JsonBranch.Category.VIEWING));
		paths.add(new JsonBranch("Aggregation/edm:isShownBy", "$.['ore:Aggregation'][0]['edm:isShownBy']",
			JsonBranch.Category.BROWSING, JsonBranch.Category.VIEWING,
			JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Aggregation/edm:object", "$.['ore:Aggregation'][0]['edm:object']",
			JsonBranch.Category.VIEWING, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Aggregation/edm:hasView", "$.['ore:Aggregation'][0]['edm:hasView']",
			JsonBranch.Category.BROWSING, JsonBranch.Category.VIEWING));

		paths.add(new JsonBranch("Aggregation/dc:rights", "$.['ore:Aggregation'][0]['dc:rights']"));
		paths.add(new JsonBranch("Aggregation/edm:ugc", "$.['ore:Aggregation'][0]['edm:ugc']"));
		paths.add(new JsonBranch("Aggregation/edm:aggregatedCHO", "$.['ore:Aggregation'][0]['edm:aggregatedCHO']"));
		paths.add(new JsonBranch("Aggregation/edm:intermediateProvider", "$.['ore:Aggregation'][0]['edm:intermediateProvider']"));
		paths.add(new JsonBranch("Aggregation/rdf:about", "$.['ore:Aggregation'][0]['@about']"));

		paths.add(new JsonBranch("Place/wgs84:lat", "$.['edm:Place'][*]['wgs84:lat']"));
		paths.add(new JsonBranch("Place/wgs84:long", "$.['edm:Place'][*]['wgs84:long']"));
		paths.add(new JsonBranch("Place/wgs84:alt", "$.['edm:Place'][*]['wgs84:alt']"));
		paths.add(new JsonBranch("Place/dcterms:isPartOf", "$.['edm:Place'][*]['dcterms:isPartOf']"));
		paths.add(new JsonBranch("Place/wgs84_pos:lat_long", "$.['edm:Place'][*]['wgs84_pos:lat_long']"));
		paths.add(new JsonBranch("Place/dcterms:hasPart", "$.['edm:Place'][*]['dcterms:hasPart']"));
		paths.add(new JsonBranch("Place/owl:sameAs", "$.['edm:Place'][*]['owl:sameAs']"));
		paths.add(new JsonBranch("Place/skos:prefLabel", "$.['edm:Place'][*]['skos:prefLabel']"));
		paths.add(new JsonBranch("Place/skos:altLabel", "$.['edm:Place'][*]['skos:altLabel']"));
		paths.add(new JsonBranch("Place/skos:note", "$.['edm:Place'][*]['skos:note']"));
		paths.add(new JsonBranch("Place/rdf:about", "$.['edm:Place'][*]['@about']"));

		paths.add(new JsonBranch("Agent/rdf:about", "$.['edm:Agent'][*]['@about']"));
		paths.add(new JsonBranch("Agent/edm:begin", "$.['edm:Agent'][*]['edm:begin']"));
		paths.add(new JsonBranch("Agent/edm:end", "$.['edm:Agent'][*]['edm:end']"));
		paths.add(new JsonBranch("Agent/edm:hasMet", "$.['edm:Agent'][*]['edm:hasMet']"));
		paths.add(new JsonBranch("Agent/edm:isRelatedTo", "$.['edm:Agent'][*]['edm:isRelatedTo']"));
		paths.add(new JsonBranch("Agent/owl:sameAs", "$.['edm:Agent'][*]['owl:sameAs']"));
		paths.add(new JsonBranch("Agent/foaf:name", "$.['edm:Agent'][*]['foaf:name']"));
		paths.add(new JsonBranch("Agent/dc:date", "$.['edm:Agent'][*]['dc:date']"));
		paths.add(new JsonBranch("Agent/dc:identifier", "$.['edm:Agent'][*]['dc:identifier']"));
		paths.add(new JsonBranch("Agent/rdaGr2:dateOfBirth", "$.['edm:Agent'][*]['rdaGr2:dateOfBirth']"));
		paths.add(new JsonBranch("Agent/rdaGr2:placeOfBirth", "$.['edm:Agent'][*]['rdaGr2:placeOfBirth']"));
		paths.add(new JsonBranch("Agent/rdaGr2:dateOfDeath", "$.['edm:Agent'][*]['rdaGr2:dateOfDeath']"));
		paths.add(new JsonBranch("Agent/rdaGr2:placeOfDeath", "$.['edm:Agent'][*]['rdaGr2:placeOfDeath']"));
		paths.add(new JsonBranch("Agent/rdaGr2:dateOfEstablishment", "$.['edm:Agent'][*]['rdaGr2:dateOfEstablishment']"));
		paths.add(new JsonBranch("Agent/rdaGr2:dateOfTermination", "$.['edm:Agent'][*]['rdaGr2:dateOfTermination']"));
		paths.add(new JsonBranch("Agent/rdaGr2:gender", "$.['edm:Agent'][*]['rdaGr2:gender']"));
		paths.add(new JsonBranch("Agent/rdaGr2:professionOrOccupation", "$.['edm:Agent'][*]['rdaGr2:professionOrOccupation']"));
		paths.add(new JsonBranch("Agent/rdaGr2:biographicalInformation", "$.['edm:Agent'][*]['rdaGr2:biographicalInformation']"));
		paths.add(new JsonBranch("Agent/skos:prefLabel", "$.['edm:Agent'][*]['skos:prefLabel']"));
		paths.add(new JsonBranch("Agent/skos:altLabel", "$.['edm:Agent'][*]['skos:altLabel']"));
		paths.add(new JsonBranch("Agent/skos:note", "$.['edm:Agent'][*]['skos:note']"));

		paths.add(new JsonBranch("Timespan/rdf:about", "$.['edm:TimeSpan'][*]['@about']"));
		paths.add(new JsonBranch("Timespan/edm:begin", "$.['edm:TimeSpan'][*]['edm:begin']"));
		paths.add(new JsonBranch("Timespan/edm:end", "$.['edm:TimeSpan'][*]['edm:end']"));
		paths.add(new JsonBranch("Timespan/dcterms:isPartOf", "$.['edm:TimeSpan'][*]['dcterms:isPartOf']"));
		paths.add(new JsonBranch("Timespan/dcterms:hasPart", "$.['edm:TimeSpan'][*]['dcterms:hasPart']"));
		paths.add(new JsonBranch("Timespan/edm:isNextInSequence", "$.['edm:TimeSpan'][*]['edm:isNextInSequence']"));
		paths.add(new JsonBranch("Timespan/owl:sameAs", "$.['edm:TimeSpan'][*]['owl:sameAs']"));
		paths.add(new JsonBranch("Timespan/skos:prefLabel", "$.['edm:TimeSpan'][*]['skos:prefLabel']"));
		paths.add(new JsonBranch("Timespan/skos:altLabel", "$.['edm:TimeSpan'][*]['skos:altLabel']"));
		paths.add(new JsonBranch("Timespan/skos:note", "$.['edm:TimeSpan'][*]['skos:note']"));

		paths.add(new JsonBranch("Concept/rdf:about", "$.['skos:Concept'][*]['@about']"));
		paths.add(new JsonBranch("Concept/skos:broader", "$.['skos:Concept'][*]['skos:broader']"));
		paths.add(new JsonBranch("Concept/skos:narrower", "$.['skos:Concept'][*]['skos:narrower']"));
		paths.add(new JsonBranch("Concept/skos:related", "$.['skos:Concept'][*]['skos:related']"));
		paths.add(new JsonBranch("Concept/skos:broadMatch", "$.['skos:Concept'][*]['skos:broadMatch']"));
		paths.add(new JsonBranch("Concept/skos:narrowMatch", "$.['skos:Concept'][*]['skos:narrowMatch']"));
		paths.add(new JsonBranch("Concept/skos:relatedMatch", "$.['skos:Concept'][*]['skos:relatedMatch']"));
		paths.add(new JsonBranch("Concept/skos:exactMatch", "$.['skos:Concept'][*]['skos:exactMatch']"));
		paths.add(new JsonBranch("Concept/skos:closeMatch", "$.['skos:Concept'][*]['skos:closeMatch']"));
		paths.add(new JsonBranch("Concept/skos:notation", "$.['skos:Concept'][*]['skos:notation']"));
		paths.add(new JsonBranch("Concept/skos:inScheme", "$.['skos:Concept'][*]['skos:inScheme']"));
		paths.add(new JsonBranch("Concept/skos:prefLabel", "$.['skos:Concept'][*]['skos:prefLabel']"));
		paths.add(new JsonBranch("Concept/skos:altLabel", "$.['skos:Concept'][*]['skos:altLabel']"));
		paths.add(new JsonBranch("Concept/skos:note", "$.['skos:Concept'][*]['skos:note']"));

		fieldGroups.add(
			new FieldGroup(
				JsonBranch.Category.MANDATORY,
				"Proxy/dc:title", "Proxy/dc:description"));
		fieldGroups.add(
			new FieldGroup(
				JsonBranch.Category.MANDATORY,
				"Proxy/dc:type", "Proxy/dc:subject", "Proxy/dc:coverage",
				"Proxy/dcterms:temporal", "Proxy/dcterms:spatial"));
		fieldGroups.add(
			new FieldGroup(
				JsonBranch.Category.MANDATORY,
				"Aggregation/edm:isShownAt", "Aggregation/edm:isShownBy"));

		noLanguageFields.addAll(Arrays.asList(
			"edm:ProvidedCHO/@about", "Proxy/edm:isNextInSequence",
			"Proxy/edm:type", "Aggregation/edm:isShownAt",
			"Aggregation/edm:isShownBy", "Aggregation/edm:object",
			"Aggregation/edm:hasView", "Aggregation/rdf:about",
			"Place/rdf:about", "Place/wgs84:lat", "Place/wgs84:long", "Place/wgs84:alt",
			"Place/wgs84_pos:lat_long", "Place/owl:sameAs",
			"Agent/rdf:about",
			"Timespan/rdf:about",
			"Concept/rdf:about"
		));

		solrFields.put("dc:title", "dc_title_txt");
		solrFields.put("dcterms:alternative", "dcterms_alternative_txt");
		solrFields.put("dc:description", "dc_description_txt");

		extractableFields.put("recordId", "$.identifier");
		extractableFields.put("dataset", "$.sets[0]");
		extractableFields.put("dataProvider", "$.['ore:Aggregation'][0]['edm:dataProvider'][0]");

		emptyStrings.add("$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:title']");
		emptyStrings.add("$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:description']");
		emptyStrings.add("$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:subject']");

	}

	@Override
	public List<JsonBranch> getPaths() {
		return paths;
	}

	@Override
	public List<FieldGroup> getFieldGroups() {
		return fieldGroups;
	}

	@Override
	public List<String> getNoLanguageFields() {
		return noLanguageFields;
	}

	@Override
	public Map<String, String> getSolrFields() {
		return solrFields;
	}

	@Override
	public Map<String, String> getExtractableFields() {
		return extractableFields;
	}

	@Override
	public List<String> getEmptyStringPaths() {
		return emptyStrings;
	}

	@Override
	public String getSubjectPath() {
		return longSubjectPath;
	}

	@Override
	public String getTitlePath() {
		return titlePath;
	}

	@Override
	public String getDescriptionPath() {
		return descriptionPath;
	}
}
