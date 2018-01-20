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

	private final static List<FieldGroup> fieldGroups = new ArrayList<>();
	private final static List<String> noLanguageFields = new ArrayList<>();
	private final static Map<String, String> solrFields = new LinkedHashMap<>();
	private static Map<String, String> extractableFields = new LinkedHashMap<>();
	private final static List<String> emptyStrings = new ArrayList<>();
	private final static Map<String, JsonBranch> paths = new LinkedHashMap<>();
	private final static Map<String, JsonBranch> collectionPaths = new LinkedHashMap<>();

	private final static String longSubjectPath =
		"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:subject']";
	private final static String titlePath =
		"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:title']";
	private final static String descriptionPath =
		"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:description']";

	static {
		JsonBranch providedCHO = new JsonBranch("ProvidedCHO", "$.['edm:ProvidedCHO'][0]");
		providedCHO.setCollection(true);
		addPath(providedCHO);
		JsonBranch providedCHOIdentifier = new JsonBranch("ProvidedCHO/rdf:about",
			providedCHO, "$.['@about']", JsonBranch.Category.MANDATORY);
		providedCHO.setIdentifier(providedCHOIdentifier);
		addPath(providedCHOIdentifier);

		JsonBranch proxy = new JsonBranch("Proxy",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]");
		proxy.setCollection(true);
		addPath(proxy);
		JsonBranch proxyIdentifier = new JsonBranch("Proxy/rdf:about", proxy, "$.['@about']");
		proxy.setIdentifier(proxyIdentifier);
		addPath(proxyIdentifier);

		addPath(new JsonBranch("Proxy/dc:title", proxy, "$.['dc:title']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY,
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.MULTILINGUALITY));
		addPath(new JsonBranch("Proxy/dcterms:alternative",proxy, "$.['dcterms:alternative']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY,
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.MULTILINGUALITY));
		addPath(new JsonBranch("Proxy/dc:description", proxy, "$.['dc:description']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY,
			JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.IDENTIFICATION,
			JsonBranch.Category.MULTILINGUALITY));
		addPath(new JsonBranch("Proxy/dc:creator",proxy, "$.['dc:creator']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY,
			JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		addPath(new JsonBranch("Proxy/dc:publisher",proxy, "$.['dc:publisher']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.REUSABILITY));
		addPath(new JsonBranch("Proxy/dc:contributor", proxy, "$.['dc:contributor']",
			JsonBranch.Category.SEARCHABILITY));
		addPath(new JsonBranch("Proxy/dc:type", proxy, "$.['dc:type']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION,
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.BROWSING));
		addPath(new JsonBranch("Proxy/dc:identifier", proxy, "$.['dc:identifier']",
			JsonBranch.Category.IDENTIFICATION));
		addPath(new JsonBranch("Proxy/dc:language", proxy, "$.['dc:language']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.MULTILINGUALITY));
		addPath(new JsonBranch("Proxy/dc:coverage", proxy, "$.['dc:coverage']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION,
			JsonBranch.Category.BROWSING));
		addPath(new JsonBranch("Proxy/dcterms:temporal", proxy, "$.['dcterms:temporal']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION,
			JsonBranch.Category.BROWSING));
		addPath(new JsonBranch("Proxy/dcterms:spatial", proxy, "$.['dcterms:spatial']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION,
			JsonBranch.Category.BROWSING));
		addPath(new JsonBranch("Proxy/dc:subject", proxy, "$.['dc:subject']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY,
			JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.MULTILINGUALITY));
		addPath(new JsonBranch("Proxy/dc:date", proxy, "$.['dc:date']",
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.BROWSING,
			JsonBranch.Category.REUSABILITY));
		addPath(new JsonBranch("Proxy/dcterms:created", proxy, "$.['dcterms:created']",
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.REUSABILITY));
		addPath(new JsonBranch("Proxy/dcterms:issued", proxy, "$.['dcterms:issued']",
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.REUSABILITY));
		addPath(new JsonBranch("Proxy/dcterms:extent", proxy, "$.['dcterms:extent']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.REUSABILITY));
		addPath(new JsonBranch("Proxy/dcterms:medium", proxy, "$.['dcterms:medium']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.REUSABILITY));
		addPath(new JsonBranch("Proxy/dcterms:provenance", proxy, "$.['dcterms:provenance']",
			JsonBranch.Category.DESCRIPTIVENESS));
		addPath(new JsonBranch("Proxy/dcterms:hasPart", proxy, "$.['dcterms:hasPart']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION,
			JsonBranch.Category.BROWSING));
		addPath(new JsonBranch("Proxy/dcterms:isPartOf", proxy, "$.['dcterms:isPartOf']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION,
			JsonBranch.Category.BROWSING));
		addPath(new JsonBranch("Proxy/dc:format", proxy, "$.['dc:format']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.REUSABILITY));
		addPath(new JsonBranch("Proxy/dc:source", proxy, "$.['dc:source']",
			JsonBranch.Category.DESCRIPTIVENESS));
		addPath(new JsonBranch("Proxy/dc:rights", proxy, "$.['dc:rights']",
			JsonBranch.Category.REUSABILITY));
		addPath(new JsonBranch("Proxy/dc:relation", proxy, "$.['dc:relation']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION,
			JsonBranch.Category.BROWSING));
		addPath(new JsonBranch("Proxy/edm:isNextInSequence", proxy, "$.['edm:isNextInSequence']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION,
			JsonBranch.Category.BROWSING));
		addPath(new JsonBranch("Proxy/edm:type", proxy, "$.['edm:type']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.BROWSING));
		addPath(new JsonBranch("Proxy/edm:europeanaProxy", proxy, "$.['edm:europeanaProxy']"));
		addPath(new JsonBranch("Proxy/edm:year", proxy, "$.['edm:year']"));
		addPath(new JsonBranch("Proxy/edm:userTag", proxy, "$.['edm:userTag']"));
		addPath(new JsonBranch("Proxy/ore:proxyIn", proxy, "$.['ore:proxyIn']"));
		addPath(new JsonBranch("Proxy/ore:proxyFor", proxy, "$.['ore:proxyFor']"));
		addPath(new JsonBranch("Proxy/dcterms:conformsTo", proxy, "$.['dcterms:conformsTo']"));
		addPath(new JsonBranch("Proxy/dcterms:hasFormat", proxy, "$.['dcterms:hasFormat']"));
		addPath(new JsonBranch("Proxy/dcterms:hasVersion", proxy, "$.['dcterms:hasVersion']"));
		addPath(new JsonBranch("Proxy/dcterms:isFormatOf", proxy, "$.['dcterms:isFormatOf']"));
		addPath(new JsonBranch("Proxy/dcterms:isReferencedBy", proxy, "$.['dcterms:isReferencedBy']"));
		addPath(new JsonBranch("Proxy/dcterms:isReplacedBy", proxy, "$.['dcterms:isReplacedBy']"));
		addPath(new JsonBranch("Proxy/dcterms:isRequiredBy", proxy, "$.['dcterms:isRequiredBy']"));
		addPath(new JsonBranch("Proxy/dcterms:isVersionOf", proxy, "$.['dcterms:isVersionOf']"));
		addPath(new JsonBranch("Proxy/dcterms:references", proxy, "$.['dcterms:references']"));
		addPath(new JsonBranch("Proxy/dcterms:replaces", proxy, "$.['dcterms:replaces']"));
		addPath(new JsonBranch("Proxy/dcterms:requires", proxy, "$.['dcterms:requires']"));
		addPath(new JsonBranch("Proxy/dcterms:tableOfContents", proxy, "$.['dcterms:tableOfContents']"));
		addPath(new JsonBranch("Proxy/edm:currentLocation", proxy, "$.['edm:currentLocation']"));
		addPath(new JsonBranch("Proxy/edm:hasMet", proxy, "$.['edm:hasMet']"));
		addPath(new JsonBranch("Proxy/edm:hasType", proxy, "$.['edm:hasType']"));
		addPath(new JsonBranch("Proxy/edm:incorporates", proxy, "$.['edm:incorporates']"));
		addPath(new JsonBranch("Proxy/edm:isDerivativeOf", proxy, "$.['edm:isDerivativeOf']"));
		addPath(new JsonBranch("Proxy/edm:isRelatedTo", proxy, "$.['edm:isRelatedTo']"));
		addPath(new JsonBranch("Proxy/edm:isRepresentationOf", proxy, "$.['edm:isRepresentationOf']"));
		addPath(new JsonBranch("Proxy/edm:isSimilarTo", proxy, "$.['edm:isSimilarTo']"));
		addPath(new JsonBranch("Proxy/edm:isSuccessorOf", proxy, "$.['edm:isSuccessorOf']"));
		addPath(new JsonBranch("Proxy/edm:realizes", proxy, "$.['edm:realizes']"));
		addPath(new JsonBranch("Proxy/edm:wasPresentAt", proxy, "$.['edm:wasPresentAt']"));

		JsonBranch aggregation = new JsonBranch("Aggregation", "$.['ore:Aggregation']");
		aggregation.setCollection(true);
		addPath(aggregation);
		JsonBranch aggregationIdentifier = new JsonBranch("Aggregation/rdf:about", aggregation, "$.['@about']");
		addPath(aggregationIdentifier);
		aggregation.setIdentifier(aggregationIdentifier);

		addPath(new JsonBranch("Aggregation/edm:rights", aggregation, "$.['edm:rights']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.REUSABILITY));
		addPath(new JsonBranch("Aggregation/edm:provider", aggregation, "$.['edm:provider']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.SEARCHABILITY,
			JsonBranch.Category.IDENTIFICATION));
		addPath(new JsonBranch("Aggregation/edm:dataProvider", aggregation, "$.['edm:dataProvider']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.SEARCHABILITY,
			JsonBranch.Category.IDENTIFICATION));
		addPath(new JsonBranch("Aggregation/edm:isShownAt", aggregation, "$.['edm:isShownAt']",
			JsonBranch.Category.BROWSING, JsonBranch.Category.VIEWING));
		addPath(new JsonBranch("Aggregation/edm:isShownBy", aggregation, "$.['edm:isShownBy']",
			JsonBranch.Category.BROWSING, JsonBranch.Category.VIEWING,
			JsonBranch.Category.REUSABILITY));
		addPath(new JsonBranch("Aggregation/edm:object", aggregation, "$.['edm:object']",
			JsonBranch.Category.VIEWING, JsonBranch.Category.REUSABILITY));
		addPath(new JsonBranch("Aggregation/edm:hasView", aggregation, "$.['edm:hasView']",
			JsonBranch.Category.BROWSING, JsonBranch.Category.VIEWING));
		addPath(new JsonBranch("Aggregation/dc:rights", aggregation, "$.['dc:rights']"));
		addPath(new JsonBranch("Aggregation/edm:ugc", aggregation, "$.['edm:ugc']"));
		addPath(new JsonBranch("Aggregation/edm:aggregatedCHO", aggregation, "$.['edm:aggregatedCHO']"));
		addPath(new JsonBranch("Aggregation/edm:intermediateProvider", aggregation, "$.['edm:intermediateProvider']"));

		JsonBranch place = new JsonBranch("Place", "$.['edm:Place']");
		place.setCollection(true);
		addPath(place);
		JsonBranch placeIdentifier = new JsonBranch("Place/rdf:about", place, "$.['@about']");
		addPath(placeIdentifier);
		place.setIdentifier(placeIdentifier);

		addPath(new JsonBranch("Place/wgs84:lat", place, "$.['wgs84:lat']"));
		addPath(new JsonBranch("Place/wgs84:long", place, "$.['wgs84:long']"));
		addPath(new JsonBranch("Place/wgs84:alt", place, "$.['wgs84:alt']"));
		addPath(new JsonBranch("Place/dcterms:isPartOf", place, "$.['dcterms:isPartOf']"));
		addPath(new JsonBranch("Place/wgs84_pos:lat_long", place, "$.['wgs84_pos:lat_long']"));
		addPath(new JsonBranch("Place/dcterms:hasPart", place, "$.['dcterms:hasPart']"));
		addPath(new JsonBranch("Place/owl:sameAs", place, "$.['owl:sameAs']"));
		addPath(new JsonBranch("Place/skos:prefLabel", place, "$.['skos:prefLabel']"));
		addPath(new JsonBranch("Place/skos:altLabel", place, "$.['skos:altLabel']"));
		addPath(new JsonBranch("Place/skos:note", place, "$.['skos:note']"));

		JsonBranch agent = new JsonBranch("Agent", "$.['edm:Agent']");
		agent.setCollection(true);
		addPath(agent);

		JsonBranch agentIdentifier = new JsonBranch("Agent/rdf:about", agent, "$.['@about']");
		addPath(agentIdentifier);
		agent.setIdentifier(agentIdentifier);
		addPath(new JsonBranch("Agent/edm:begin", agent, "$.['edm:begin']"));
		addPath(new JsonBranch("Agent/edm:end", agent, "$.['edm:end']"));
		addPath(new JsonBranch("Agent/edm:hasMet", agent, "$.['edm:hasMet']"));
		addPath(new JsonBranch("Agent/edm:isRelatedTo", agent, "$.['edm:isRelatedTo']"));
		addPath(new JsonBranch("Agent/owl:sameAs", agent, "$.['owl:sameAs']"));
		addPath(new JsonBranch("Agent/foaf:name", agent, "$.['foaf:name']"));
		addPath(new JsonBranch("Agent/dc:date", agent, "$.['dc:date']"));
		addPath(new JsonBranch("Agent/dc:identifier", agent, "$.['dc:identifier']"));
		addPath(new JsonBranch("Agent/rdaGr2:dateOfBirth", agent, "$.['rdaGr2:dateOfBirth']"));
		addPath(new JsonBranch("Agent/rdaGr2:placeOfBirth", agent, "$.['rdaGr2:placeOfBirth']"));
		addPath(new JsonBranch("Agent/rdaGr2:dateOfDeath", agent, "$.['rdaGr2:dateOfDeath']"));
		addPath(new JsonBranch("Agent/rdaGr2:placeOfDeath", agent, "$.['rdaGr2:placeOfDeath']"));
		addPath(new JsonBranch("Agent/rdaGr2:dateOfEstablishment", agent, "$.['rdaGr2:dateOfEstablishment']"));
		addPath(new JsonBranch("Agent/rdaGr2:dateOfTermination", agent, "$.['rdaGr2:dateOfTermination']"));
		addPath(new JsonBranch("Agent/rdaGr2:gender", agent, "$.['rdaGr2:gender']"));
		addPath(new JsonBranch("Agent/rdaGr2:professionOrOccupation", agent, "$.['rdaGr2:professionOrOccupation']"));
		addPath(new JsonBranch("Agent/rdaGr2:biographicalInformation", agent, "$.['rdaGr2:biographicalInformation']"));
		addPath(new JsonBranch("Agent/skos:prefLabel", agent, "$.['skos:prefLabel']"));
		addPath(new JsonBranch("Agent/skos:altLabel", agent, "$.['skos:altLabel']"));
		addPath(new JsonBranch("Agent/skos:note", agent, "$.['skos:note']"));

		JsonBranch timespan = new JsonBranch("Timespan", "$.['edm:TimeSpan']");
		timespan.setCollection(true);
		addPath(timespan);

		JsonBranch timespanIdentifier = new JsonBranch("Timespan/rdf:about", timespan, "$.['@about']");
		addPath(timespanIdentifier);
		timespan.setIdentifier(timespanIdentifier);
		addPath(new JsonBranch("Timespan/edm:begin", timespan, "$.['edm:begin']"));
		addPath(new JsonBranch("Timespan/edm:end", timespan, "$.['edm:end']"));
		addPath(new JsonBranch("Timespan/dcterms:isPartOf", timespan, "$.['dcterms:isPartOf']"));
		addPath(new JsonBranch("Timespan/dcterms:hasPart", timespan, "$.['dcterms:hasPart']"));
		addPath(new JsonBranch("Timespan/edm:isNextInSequence", timespan, "$.['edm:isNextInSequence']"));
		addPath(new JsonBranch("Timespan/owl:sameAs", timespan, "$.['owl:sameAs']"));
		addPath(new JsonBranch("Timespan/skos:prefLabel", timespan, "$.['skos:prefLabel']"));
		addPath(new JsonBranch("Timespan/skos:altLabel", timespan, "$.['skos:altLabel']"));
		addPath(new JsonBranch("Timespan/skos:note", timespan, "$.['skos:note']"));

		JsonBranch concept = new JsonBranch("Concept", "$.['skos:Concept']");
		concept.setCollection(true);
		addPath(concept);

		JsonBranch conceptIdentifier = new JsonBranch("Concept/rdf:about", concept, "$.['@about']");
		addPath(conceptIdentifier);
		concept.setIdentifier(conceptIdentifier);
		addPath(new JsonBranch("Concept/skos:broader", concept, "$.['skos:broader']"));
		addPath(new JsonBranch("Concept/skos:narrower", concept, "$.['skos:narrower']"));
		addPath(new JsonBranch("Concept/skos:related", concept, "$.['skos:related']"));
		addPath(new JsonBranch("Concept/skos:broadMatch", concept, "$.['skos:broadMatch']"));
		addPath(new JsonBranch("Concept/skos:narrowMatch", concept, "$.['skos:narrowMatch']"));
		addPath(new JsonBranch("Concept/skos:relatedMatch", concept, "$.['skos:relatedMatch']"));
		addPath(new JsonBranch("Concept/skos:exactMatch", concept, "$.['skos:exactMatch']"));
		addPath(new JsonBranch("Concept/skos:closeMatch", concept, "$.['skos:closeMatch']"));
		addPath(new JsonBranch("Concept/skos:notation", concept, "$.['skos:notation']"));
		addPath(new JsonBranch("Concept/skos:inScheme", concept, "$.['skos:inScheme']"));
		addPath(new JsonBranch("Concept/skos:prefLabel", concept, "$.['skos:prefLabel']"));
		addPath(new JsonBranch("Concept/skos:altLabel", concept, "$.['skos:altLabel']"));
		addPath(new JsonBranch("Concept/skos:note", concept, "$.['skos:note']"));

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
			"ProvidedCHO", "ProvidedCHO/rdf:about",
			"Proxy",
			"Proxy/rdf:about", "Proxy/edm:isNextInSequence", "Proxy/edm:type",
			"Proxy/ore:proxyFor", "Proxy/ore:proxyIn", "Proxy/edm:europeanaProxy",
			"Aggregation",
			"Aggregation/edm:isShownAt",
			"Aggregation/edm:isShownBy", "Aggregation/edm:object",
			"Aggregation/edm:hasView", "Aggregation/rdf:about",
			"Place",
			"Place/rdf:about", "Place/wgs84:lat", "Place/wgs84:long",
			"Place/wgs84:alt", "Place/wgs84_pos:lat_long", "Place/owl:sameAs",
			"Agent", "Agent/rdf:about",
			"Timespan", "Timespan/rdf:about",
			"Concept", "Concept/rdf:about"
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
	public void setExtractableFields(Map<String, String> extractableFields) {
		this.extractableFields = extractableFields;
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

	@Override
	public List<JsonBranch> getCollectionPaths() {
		return new ArrayList(collectionPaths.values());
	}

	private static void addPath(JsonBranch branch) {
		paths.put(branch.getLabel(), branch);
		if (branch.isCollection())
			collectionPaths.put(branch.getLabel(), branch);
	}

	@Override
	public List<JsonBranch> getPaths() {
		return new ArrayList(paths.values());
	}

	@Override
	public List<JsonBranch> getRootChildrenPaths() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public JsonBranch getPathByLabel(String label) {
		return paths.get(label);
	}
}
