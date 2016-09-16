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
public class EdmFullBeanSchema extends EdmSchema implements Serializable {

	private final static List<JsonBranch> paths = new ArrayList<>();
	private final static List<FieldGroup> fieldGroups = new ArrayList<>();
	private final static List<String> noLanguageFields = new ArrayList<>();
	private final static Map<String, String> solrFields = new LinkedHashMap<>();
	private final static Map<String, String> extractableFields = new LinkedHashMap<>();
	private final static List<String> emptyStrings = new ArrayList<>();
	private final static String longSubjectPath =
		"$.['proxies'][?(@['europeanaProxy'] == false)]['dcSubject']";
	private final static String titlePath =
		"$.['proxies'][?(@['europeanaProxy'] == false)]['dcTitle']";
	private final static String descriptionPath =
		"$.['proxies'][?(@['europeanaProxy'] == false)]['dcDescription']";

	static {
		paths.add(new JsonBranch("edm:ProvidedCHO/@about",
			"$.['providedCHOs'][0]['about']",
			JsonBranch.Category.MANDATORY));
		paths.add(new JsonBranch("Proxy/dc:title",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dcTitle']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY,
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.MULTILINGUALITY));
		paths.add(new JsonBranch("Proxy/dcterms:alternative",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsAlternative']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.IDENTIFICATION,
			JsonBranch.Category.MULTILINGUALITY));
		paths.add(new JsonBranch("Proxy/dc:description",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dcDescription']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY,
			JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.MULTILINGUALITY));
		paths.add(new JsonBranch("Proxy/dc:creator",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dcCreator']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION,
			JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/dc:publisher",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dcPublisher']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dc:contributor",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dcContributor']",
			JsonBranch.Category.SEARCHABILITY));
		paths.add(new JsonBranch("Proxy/dc:type",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dcType']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION,
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/dc:identifier",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dcIdentifier']",
			JsonBranch.Category.IDENTIFICATION));
		paths.add(new JsonBranch("Proxy/dc:language",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dcLanguage']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.MULTILINGUALITY));
		paths.add(new JsonBranch("Proxy/dc:coverage",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dcCoverage']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION,
			JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/dcterms:temporal",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsTemporal']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/dcterms:spatial",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsSpatial']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION,
			JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/dc:subject",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dcSubject']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY,
			JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.MULTILINGUALITY));
		paths.add(new JsonBranch("Proxy/dc:date",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dcDate']",
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.BROWSING, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dcterms:created",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsCreated']",
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dcterms:issued",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsIssued']",
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dcterms:extent",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsExtent']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dcterms:medium",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsMedium']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dcterms:provenance",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsProvenance']",
			JsonBranch.Category.DESCRIPTIVENESS));
		paths.add(new JsonBranch("Proxy/dcterms:hasPart",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsHasPart']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/dcterms:isPartOf",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsIsPartOf']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/dc:format",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dcFormat']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dc:source",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dcSource']",
			JsonBranch.Category.DESCRIPTIVENESS));
		paths.add(new JsonBranch("Proxy/dc:rights",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dcRights']",
			JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dc:relation",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['dcRelation']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/edm:isNextInSequence",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['edmIsNextInSequence']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/edm:type",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['edmType']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.BROWSING));
		/*
		paths.add(new JsonBranch("Proxy/edm:rights",
			"$.['proxies'][?(@['europeanaProxy'] == false)]['edm:rights']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.REUSABILITY));
		*/
		paths.add(new JsonBranch("Aggregation/edm:rights",
			"$.['aggregations'][0]['edmRights']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Aggregation/edm:provider",
			"$.['aggregations'][0]['edmProvider']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.IDENTIFICATION));
		paths.add(new JsonBranch("Aggregation/edm:dataProvider",
			"$.['aggregations'][0]['edmDataProvider']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.SEARCHABILITY,
			JsonBranch.Category.IDENTIFICATION));
		paths.add(new JsonBranch("Aggregation/edm:isShownAt",
			"$.['aggregations'][0]['edmIsShownAt']",
			JsonBranch.Category.BROWSING, JsonBranch.Category.VIEWING));
		paths.add(new JsonBranch("Aggregation/edm:isShownBy",
			"$.['aggregations'][0]['edmIsShownBy']",
			JsonBranch.Category.BROWSING, JsonBranch.Category.VIEWING,
			JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Aggregation/edm:object",
			"$.['aggregations'][0]['edmObject']",
			JsonBranch.Category.VIEWING, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Aggregation/edm:hasView",
			"$.['aggregations'][0]['hasView']",
			JsonBranch.Category.BROWSING, JsonBranch.Category.VIEWING));

		paths.add(new JsonBranch("Place/wgs84:lat", "$.['places'][*]['latitude']"));
		paths.add(new JsonBranch("Place/wgs84:long", "$.['places'][*]['longitude']"));
		paths.add(new JsonBranch("Place/wgs84:alt", "$.['places'][*]['altitude']"));
		paths.add(new JsonBranch("Place/dcterms:isPartOf", "$.['places'][*]['isPartOf']"));
		paths.add(new JsonBranch("Place/wgs84_pos:lat_long", "$.['places'][*]['position']"));
		paths.add(new JsonBranch("Place/dcterms:hasPart", "$.['places'][*]['dctermsHasPart']"));
		paths.add(new JsonBranch("Place/owl:sameAs", "$.['places'][*]['owlSameAs']"));
		paths.add(new JsonBranch("Place/skos:prefLabel", "$.['places'][*]['prefLabel']"));
		paths.add(new JsonBranch("Place/skos:altLabel", "$.['places'][*]['altLabel']"));
		paths.add(new JsonBranch("Place/skos:note", "$.['places'][*]['note']"));
		paths.add(new JsonBranch("Place/rdf:about", "$.['places'][*]['about']"));

		paths.add(new JsonBranch("Agent/rdf:about", "$.['agents'][*]['about']"));
		paths.add(new JsonBranch("Agent/edm:begin", "$.['agents'][*]['begin']"));
		paths.add(new JsonBranch("Agent/edm:end", "$.['agents'][*]['end']"));
		paths.add(new JsonBranch("Agent/edm:hasMet", "$.['agents'][*]['edmHasMet']"));
		paths.add(new JsonBranch("Agent/edm:isRelatedTo", "$.['agents'][*]['edmIsRelatedTo']"));
		paths.add(new JsonBranch("Agent/owl:sameAs", "$.['agents'][*]['owlSameAs']"));
		paths.add(new JsonBranch("Agent/foaf:name", "$.['agents'][*]['foafName']"));
		paths.add(new JsonBranch("Agent/dc:date", "$.['agents'][*]['dcDate']"));
		paths.add(new JsonBranch("Agent/dc:identifier", "$.['agents'][*]['dcIdentifier']"));
		paths.add(new JsonBranch("Agent/rdaGr2:dateOfBirth", "$.['agents'][*]['rdaGr2DateOfBirth']"));
		paths.add(new JsonBranch("Agent/rdaGr2:placeOfBirth", "$.['agents'][*]['rdaGr2PlaceOfBirth']"));
		paths.add(new JsonBranch("Agent/rdaGr2:dateOfDeath", "$.['agents'][*]['rdaGr2DateOfDeath']"));
		paths.add(new JsonBranch("Agent/rdaGr2:placeOfDeath", "$.['agents'][*]['rdaGr2PlaceOfDeath']"));
		paths.add(new JsonBranch("Agent/rdaGr2:dateOfEstablishment", "$.['agents'][*]['rdaGr2DateOfEstablishment']"));
		paths.add(new JsonBranch("Agent/rdaGr2:dateOfTermination", "$.['agents'][*]['rdaGr2DateOfTermination']"));
		paths.add(new JsonBranch("Agent/rdaGr2:gender", "$.['agents'][*]['rdaGr2Gender']"));
		paths.add(new JsonBranch("Agent/rdaGr2:professionOrOccupation", "$.['agents'][*]['rdaGr2ProfessionOrOccupation']"));
		paths.add(new JsonBranch("Agent/rdaGr2:biographicalInformation", "$.['agents'][*]['rdaGr2BiographicalInformation']"));
		paths.add(new JsonBranch("Agent/skos:prefLabel", "$.['agents'][*]['prefLabel']"));
		paths.add(new JsonBranch("Agent/skos:altLabel", "$.['agents'][*]['altLabel']"));
		paths.add(new JsonBranch("Agent/skos:note", "$.['agents'][*]['note']"));

		paths.add(new JsonBranch("Timespan/rdf:about", "$.['edm:Timespan'][*]['about']"));
		paths.add(new JsonBranch("Timespan/edm:begin", "$.['edm:Timespan'][*]['begin']"));
		paths.add(new JsonBranch("Timespan/edm:end", "$.['edm:Timespan'][*]['end']"));
		paths.add(new JsonBranch("Timespan/dcterms:isPartOf", "$.['edm:Timespan'][*]['isPartOf']"));
		paths.add(new JsonBranch("Timespan/dcterms:hasPart", "$.['edm:Timespan'][*]['hasPart']"));
		paths.add(new JsonBranch("Timespan/owl:sameAs", "$.['edm:Timespan'][*]['owlSameAs']"));
		paths.add(new JsonBranch("Timespan/skos:prefLabel", "$.['edm:Timespan'][*]['prefLabel']"));
		paths.add(new JsonBranch("Timespan/skos:altLabel", "$.['edm:Timespan'][*]['altLabel']"));
		paths.add(new JsonBranch("Timespan/skos:note", "$.['edm:Timespan'][*]['note']"));

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
			"Aggregation/edm:hasView",
			"Place/rdf:about", "Place/wgs84:lat", "Place/wgs84:long", "Place/wgs84:alt",
			"Place/wgs84_pos:lat_long", "Place/owl:sameAs",
			"Agent/rdf:about",
			"Timespan/rdf:about"
		));

		solrFields.put("dc:title", "dc_title_txt");
		solrFields.put("dcterms:alternative", "dcterms_alternative_txt");
		solrFields.put("dc:description", "dc_description_txt");

		extractableFields.put("recordId", "$.identifier");
		extractableFields.put("dataset", "$.sets[0]");
		extractableFields.put("dataProvider", "$.['aggregations'][0]['edmDataProvider'][0]");

		emptyStrings.add("$.['proxies'][?(@['europeanaProxy'] == false)]['dcTitle']");
		emptyStrings.add("$.['proxies'][?(@['europeanaProxy'] == false)]['dcDescription']");
		emptyStrings.add("$.['proxies'][?(@['europeanaProxy'] == false)]['dcSubject']");
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
