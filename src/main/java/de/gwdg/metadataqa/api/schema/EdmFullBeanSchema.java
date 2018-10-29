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

	// private final static List<JsonBranch> paths = new ArrayList<>();
	private final static List<FieldGroup> fieldGroups = new ArrayList<>();
	private final static List<String> noLanguageFields = new ArrayList<>();
	private final static Map<String, String> solrFields = new LinkedHashMap<>();
	private Map<String, String> extractableFields = new LinkedHashMap<>();
	private final static List<String> emptyStrings = new ArrayList<>();
	private final static Map<String, JsonBranch> paths = new LinkedHashMap<>();
	private final static Map<String, JsonBranch> collectionPaths = new LinkedHashMap<>();

	private final static String longSubjectPath =
		"$.['proxies'][?(@['europeanaProxy'] == false)]['dcSubject']";
	private final static String titlePath =
		"$.['proxies'][?(@['europeanaProxy'] == false)]['dcTitle']";
	private final static String descriptionPath =
		"$.['proxies'][?(@['europeanaProxy'] == false)]['dcDescription']";

	static {
		JsonBranch providedCHO = new JsonBranch("ProvidedCHO", "$.['providedCHOs'][0]");
		providedCHO.setCollection(true);
		addPath(providedCHO);

		JsonBranch providedCHOIdentifier = new JsonBranch("ProvidedCHO/rdf:about",
			providedCHO, "$.['about']", JsonBranch.Category.MANDATORY);
		providedCHO.setIdentifier(providedCHOIdentifier);
		addPath(providedCHOIdentifier);

		JsonBranch proxy = new JsonBranch("Proxy", "$.['proxies'][?(@['europeanaProxy'] == false)]");
		proxy.setCollection(true);
		addPath(proxy);

		JsonBranch proxyIdentifier = new JsonBranch("Proxy/rdf:about", proxy, "$.['about']");
		proxy.setIdentifier(proxyIdentifier);
		addPath(proxyIdentifier);

		addPath(new JsonBranch("Proxy/dc:title", proxy, "$.['dcTitle']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY,
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.MULTILINGUALITY));
		addPath(new JsonBranch("Proxy/dcterms:alternative", proxy, "$.['dctermsAlternative']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.IDENTIFICATION,
			JsonBranch.Category.MULTILINGUALITY));
		addPath(new JsonBranch("Proxy/dc:description", proxy, "$.['dcDescription']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY,
			JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.MULTILINGUALITY));
		addPath(new JsonBranch("Proxy/dc:creator", proxy, "$.['dcCreator']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION,
			JsonBranch.Category.BROWSING));
		addPath(new JsonBranch("Proxy/dc:publisher", proxy, "$.['dcPublisher']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.REUSABILITY));
		addPath(new JsonBranch("Proxy/dc:contributor", proxy, "$.['dcContributor']",
			JsonBranch.Category.SEARCHABILITY));
		addPath(new JsonBranch("Proxy/dc:type", proxy, "$.['dcType']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION,
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.BROWSING));
		addPath(new JsonBranch("Proxy/dc:identifier", proxy, "$.['dcIdentifier']",
			JsonBranch.Category.IDENTIFICATION));
		addPath(new JsonBranch("Proxy/dc:language", proxy, "$.['dcLanguage']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.MULTILINGUALITY));
		addPath(new JsonBranch("Proxy/dc:coverage", proxy, "$.['dcCoverage']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION,
			JsonBranch.Category.BROWSING));
		addPath(new JsonBranch("Proxy/dcterms:temporal", proxy, "$.['dctermsTemporal']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		addPath(new JsonBranch("Proxy/dcterms:spatial", proxy, "$.['dctermsSpatial']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION,
			JsonBranch.Category.BROWSING));
		addPath(new JsonBranch("Proxy/dc:subject", proxy, "$.['dcSubject']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY,
			JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.MULTILINGUALITY));
		addPath(new JsonBranch("Proxy/dc:date", proxy, "$.['dcDate']",
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.BROWSING, JsonBranch.Category.REUSABILITY));
		addPath(new JsonBranch("Proxy/dcterms:created", proxy, "$.['dctermsCreated']",
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.REUSABILITY));
		addPath(new JsonBranch("Proxy/dcterms:issued", proxy, "$.['dctermsIssued']",
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.REUSABILITY));
		addPath(new JsonBranch("Proxy/dcterms:extent", proxy, "$.['dctermsExtent']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.REUSABILITY));
		addPath(new JsonBranch("Proxy/dcterms:medium", proxy, "$.['dctermsMedium']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.REUSABILITY));
		addPath(new JsonBranch("Proxy/dcterms:provenance", proxy, "$.['dctermsProvenance']",
			JsonBranch.Category.DESCRIPTIVENESS));
		addPath(new JsonBranch("Proxy/dcterms:hasPart", proxy, "$.['dctermsHasPart']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		addPath(new JsonBranch("Proxy/dcterms:isPartOf", proxy, "$.['dctermsIsPartOf']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		addPath(new JsonBranch("Proxy/dc:format", proxy, "$.['dcFormat']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.REUSABILITY));
		addPath(new JsonBranch("Proxy/dc:source", proxy, "$.['dcSource']",
			JsonBranch.Category.DESCRIPTIVENESS));
		addPath(new JsonBranch("Proxy/dc:rights", proxy, "$.['dcRights']",
			JsonBranch.Category.REUSABILITY));
		addPath(new JsonBranch("Proxy/dc:relation", proxy, "$.['dcRelation']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		addPath(new JsonBranch("Proxy/edm:isNextInSequence", proxy, "$.['edmIsNextInSequence']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		addPath(new JsonBranch("Proxy/edm:type", proxy, "$.['edmType']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.BROWSING));
		/*
		addPath(new JsonBranch("Proxy/edm:rights", proxy, "$.['edm:rights']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.REUSABILITY));
		*/

		addPath(new JsonBranch("Proxy/edm:europeanaProxy", proxy, "$.['europeanaProxy']"));
		addPath(new JsonBranch("Proxy/edm:year", proxy, "$.['year']"));
		addPath(new JsonBranch("Proxy/edm:userTag", proxy, "$.['userTags']"));
		addPath(new JsonBranch("Proxy/ore:proxyIn", proxy, "$.['proxyIn']"));
		addPath(new JsonBranch("Proxy/ore:proxyFor", proxy, "$.['proxyFor']"));
		addPath(new JsonBranch("Proxy/dcterms:conformsTo", proxy, "$.['dctermsConformsTo']"));
		addPath(new JsonBranch("Proxy/dcterms:hasFormat", proxy, "$.['dctermsHasFormat']"));
		addPath(new JsonBranch("Proxy/dcterms:hasVersion", proxy, "$.['dctermsHasVersion']"));
		addPath(new JsonBranch("Proxy/dcterms:isFormatOf", proxy, "$.['dctermsIsFormatOf']"));
		addPath(new JsonBranch("Proxy/dcterms:isReferencedBy", proxy, "$.['dctermsIsReferencedBy']"));
		addPath(new JsonBranch("Proxy/dcterms:isReplacedBy", proxy, "$.['dctermsIsReplacedBy']"));
		addPath(new JsonBranch("Proxy/dcterms:isRequiredBy", proxy, "$.['dctermsIsRequiredBy']"));
		addPath(new JsonBranch("Proxy/dcterms:isVersionOf", proxy, "$.['dctermsIsVersionOf']"));
		addPath(new JsonBranch("Proxy/dcterms:references", proxy, "$.['dctermsReferences']"));
		addPath(new JsonBranch("Proxy/dcterms:replaces", proxy, "$.['dctermsReplaces']"));
		addPath(new JsonBranch("Proxy/dcterms:requires", proxy, "$.['dctermsRequires']"));
		addPath(new JsonBranch("Proxy/dcterms:tableOfContents", proxy, "$.['dctermsTOC']"));
		addPath(new JsonBranch("Proxy/edm:currentLocation", proxy, "$.['edmCurrentLocation']"));
		addPath(new JsonBranch("Proxy/edm:hasMet", proxy, "$.['edmHasMet']"));
		addPath(new JsonBranch("Proxy/edm:hasType", proxy, "$.['edmHasType']"));
		addPath(new JsonBranch("Proxy/edm:incorporates", proxy, "$.['edmIncorporates']"));
		addPath(new JsonBranch("Proxy/edm:isDerivativeOf", proxy, "$.['edmIsDerivativeOf']"));
		addPath(new JsonBranch("Proxy/edm:isRelatedTo", proxy, "$.['edmIsRelatedTo']"));
		addPath(new JsonBranch("Proxy/edm:isRepresentationOf", proxy, "$.['edmIsRepresentationOf']"));
		addPath(new JsonBranch("Proxy/edm:isSimilarTo", proxy, "$.['edmIsSimilarTo']"));
		addPath(new JsonBranch("Proxy/edm:isSuccessorOf", proxy, "$.['edmIsSuccessorOf']"));
		addPath(new JsonBranch("Proxy/edm:realizes", proxy, "$.['edmRealizes']"));
		addPath(new JsonBranch("Proxy/edm:wasPresentAt", proxy, "$.['edmWasPresentAt']"));

		JsonBranch aggregation = new JsonBranch("Aggregation", "$.['aggregations'][0]");
		aggregation.setCollection(true);
		addPath(aggregation);
		JsonBranch aggregationIdentifier = new JsonBranch("Aggregation/rdf:about", aggregation, "$.['about']");
		addPath(aggregationIdentifier);
		aggregation.setIdentifier(aggregationIdentifier);

		addPath(new JsonBranch("Aggregation/edm:rights", aggregation, "$.['edmRights']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.REUSABILITY));
		addPath(new JsonBranch("Aggregation/edm:provider", aggregation, "$.['edmProvider']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.SEARCHABILITY,
			JsonBranch.Category.IDENTIFICATION));
		addPath(new JsonBranch("Aggregation/edm:dataProvider", aggregation, "$.['edmDataProvider']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.SEARCHABILITY,
			JsonBranch.Category.IDENTIFICATION));
		addPath(new JsonBranch("Aggregation/edm:isShownAt", aggregation, "$.['edmIsShownAt']",
			JsonBranch.Category.BROWSING, JsonBranch.Category.VIEWING));
		addPath(new JsonBranch("Aggregation/edm:isShownBy", aggregation, "$.['edmIsShownBy']",
			JsonBranch.Category.BROWSING, JsonBranch.Category.VIEWING,
			JsonBranch.Category.REUSABILITY));
		addPath(new JsonBranch("Aggregation/edm:object", aggregation, "$.['edmObject']",
			JsonBranch.Category.VIEWING, JsonBranch.Category.REUSABILITY));
		addPath(new JsonBranch("Aggregation/edm:hasView", aggregation, "$.['hasView']",
			JsonBranch.Category.BROWSING, JsonBranch.Category.VIEWING));
		addPath(new JsonBranch("Aggregation/dc:rights", aggregation, "$.['dcRights']"));
		addPath(new JsonBranch("Aggregation/edm:ugc", aggregation, "$.['edmUgc']"));
		addPath(new JsonBranch("Aggregation/edm:aggregatedCHO", aggregation, "$.['aggregatedCHO']"));
		addPath(new JsonBranch("Aggregation/edm:intermediateProvider", aggregation, "$.['edmIntermediateProvider']"));

		JsonBranch place = new JsonBranch("Place", "$.['places']");
		place.setCollection(true);
		addPath(place);
		JsonBranch placeIdentifier = new JsonBranch("Place/rdf:about", place, "$.['about']");
		addPath(placeIdentifier);
		place.setIdentifier(placeIdentifier);

		addPath(new JsonBranch("Place/rdf:about", place, "$.['about']"));
		addPath(new JsonBranch("Place/wgs84:lat", place, "$.['latitude']"));
		addPath(new JsonBranch("Place/wgs84:long", place, "$.['longitude']"));
		addPath(new JsonBranch("Place/wgs84:alt", place, "$.['altitude']"));
		addPath(new JsonBranch("Place/dcterms:isPartOf", place, "$.['isPartOf']"));
		addPath(new JsonBranch("Place/wgs84_pos:lat_long", place, "$.['position']"));
		addPath(new JsonBranch("Place/dcterms:hasPart", place, "$.['dctermsHasPart']"));
		addPath(new JsonBranch("Place/owl:sameAs", place, "$.['owlSameAs']"));
		addPath(new JsonBranch("Place/skos:prefLabel", place, "$.['prefLabel']"));
		addPath(new JsonBranch("Place/skos:altLabel", place, "$.['altLabel']"));
		addPath(new JsonBranch("Place/skos:note", place, "$.['note']"));

		JsonBranch agent = new JsonBranch("Agent", "$.['agents']");
		agent.setCollection(true);
		addPath(agent);
		JsonBranch agentIdentifier = new JsonBranch("Agent/rdf:about", agent, "$.['about']");
		addPath(agentIdentifier);
		agent.setIdentifier(agentIdentifier);

		addPath(new JsonBranch("Agent/edm:begin", agent, "$.['begin']"));
		addPath(new JsonBranch("Agent/edm:end", agent, "$.['end']"));
		addPath(new JsonBranch("Agent/edm:hasMet", agent, "$.['edmHasMet']"));
		addPath(new JsonBranch("Agent/edm:isRelatedTo", agent, "$.['edmIsRelatedTo']"));
		addPath(new JsonBranch("Agent/owl:sameAs", agent, "$.['owlSameAs']"));
		addPath(new JsonBranch("Agent/foaf:name", agent, "$.['foafName']"));
		addPath(new JsonBranch("Agent/dc:date", agent, "$.['dcDate']"));
		addPath(new JsonBranch("Agent/dc:identifier", agent, "$.['dcIdentifier']"));
		addPath(new JsonBranch("Agent/rdaGr2:dateOfBirth", agent, "$.['rdaGr2DateOfBirth']"));
		addPath(new JsonBranch("Agent/rdaGr2:placeOfBirth", agent, "$.['rdaGr2PlaceOfBirth']"));
		addPath(new JsonBranch("Agent/rdaGr2:dateOfDeath", agent, "$.['rdaGr2DateOfDeath']"));
		addPath(new JsonBranch("Agent/rdaGr2:placeOfDeath", agent, "$.['rdaGr2PlaceOfDeath']"));
		addPath(new JsonBranch("Agent/rdaGr2:dateOfEstablishment", agent, "$.['rdaGr2DateOfEstablishment']"));
		addPath(new JsonBranch("Agent/rdaGr2:dateOfTermination", agent, "$.['rdaGr2DateOfTermination']"));
		addPath(new JsonBranch("Agent/rdaGr2:gender", agent, "$.['rdaGr2Gender']"));
		addPath(new JsonBranch("Agent/rdaGr2:professionOrOccupation", agent, "$.['rdaGr2ProfessionOrOccupation']"));
		addPath(new JsonBranch("Agent/rdaGr2:biographicalInformation", agent, "$.['rdaGr2BiographicalInformation']"));
		addPath(new JsonBranch("Agent/skos:prefLabel", agent, "$.['prefLabel']"));
		addPath(new JsonBranch("Agent/skos:altLabel", agent, "$.['altLabel']"));
		addPath(new JsonBranch("Agent/skos:note", agent, "$.['note']"));

		JsonBranch timespan = new JsonBranch("Timespan", "$.['timespans']");
		timespan.setCollection(true);
		addPath(timespan);

		JsonBranch timespanIdentifier = new JsonBranch("Timespan/rdf:about", timespan, "$.['about']");
		addPath(timespanIdentifier);
		timespan.setIdentifier(timespanIdentifier);

		addPath(new JsonBranch("Timespan/rdf:about", timespan, "$.['about']"));
		addPath(new JsonBranch("Timespan/edm:begin", timespan, "$.['begin']"));
		addPath(new JsonBranch("Timespan/edm:end", timespan, "$.['end']"));
		addPath(new JsonBranch("Timespan/dcterms:isPartOf", timespan, "$.['isPartOf']"));
		addPath(new JsonBranch("Timespan/dcterms:hasPart", timespan, "$.['hasPart']"));
		addPath(new JsonBranch("Timespan/edm:isNextInSequence", timespan, "$.['edm:isNextInSequence']"));
		addPath(new JsonBranch("Timespan/owl:sameAs", timespan, "$.['owlSameAs']"));
		addPath(new JsonBranch("Timespan/skos:prefLabel", timespan, "$.['prefLabel']"));
		addPath(new JsonBranch("Timespan/skos:altLabel", timespan, "$.['altLabel']"));
		addPath(new JsonBranch("Timespan/skos:note", timespan, "$.['note']"));

		JsonBranch concept = new JsonBranch("Concept", "$.['concepts']");
		concept.setCollection(true);
		addPath(concept);

		JsonBranch conceptIdentifier = new JsonBranch("Concept/rdf:about", concept, "$.['about']");
		addPath(conceptIdentifier);
		concept.setIdentifier(conceptIdentifier);

		addPath(new JsonBranch("Concept/rdf:about", concept, "$.['about']"));
		addPath(new JsonBranch("Concept/skos:broader", concept, "$.['broader']"));
		addPath(new JsonBranch("Concept/skos:narrower", concept, "$.['narrower']"));
		addPath(new JsonBranch("Concept/skos:related", concept, "$.['related']"));
		addPath(new JsonBranch("Concept/skos:broadMatch", concept, "$.['broadMatch']"));
		addPath(new JsonBranch("Concept/skos:narrowMatch", concept, "$.['narrowMatch']"));
		addPath(new JsonBranch("Concept/skos:relatedMatch", concept, "$.['relatedMatch']"));
		addPath(new JsonBranch("Concept/skos:exactMatch", concept, "$.['exactMatch']"));
		addPath(new JsonBranch("Concept/skos:closeMatch", concept, "$.['closeMatch']"));
		addPath(new JsonBranch("Concept/skos:notation", concept, "$.['notation']"));
		addPath(new JsonBranch("Concept/skos:inScheme", concept, "$.['inScheme']"));
		addPath(new JsonBranch("Concept/skos:prefLabel", concept, "$.['prefLabel']"));
		addPath(new JsonBranch("Concept/skos:altLabel", concept, "$.['altLabel']"));
		addPath(new JsonBranch("Concept/skos:note", concept, "$.['note']"));

		JsonBranch europeanaAggregation = new JsonBranch("EuropeanaAggregation", "$.['europeanaAggregation']")
			.setActive(false);
		europeanaAggregation.setCollection(true);
		addPath(europeanaAggregation);
		addPath(new JsonBranch("EuropeanaAggregation/edm:country", europeanaAggregation, "$.['edmCountry']")
			.setActive(false));
		addPath(new JsonBranch("EuropeanaAggregation/edm:language", europeanaAggregation, "$.['edmLanguage']")
			.setActive(false));

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

		solrFields.put("Proxy/dc:title", "dc_title_txt");
		solrFields.put("Proxy/dcterms:alternative", "dcterms_alternative_txt");
		solrFields.put("Proxy/dc:description", "dc_description_txt");

		// extractableFields.put("country", "$.['europeanaAggregation'][0]['edmCountry'][0]");
		// extractableFields.put("language", "$.['europeanaAggregation'][0]['edmLanguage'][0]");

		emptyStrings.add("$.['proxies'][?(@['europeanaProxy'] == false)]['dcTitle']");
		emptyStrings.add("$.['proxies'][?(@['europeanaProxy'] == false)]['dcDescription']");
		emptyStrings.add("$.['proxies'][?(@['europeanaProxy'] == false)]['dcSubject']");
	}

	public EdmFullBeanSchema() {
		extractableFields.put("recordId", "$.identifier");
		extractableFields.put("dataset", "$.sets[0]");
		extractableFields.put("dataProvider", "$.['aggregations'][0]['edmDataProvider'][0]");
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
	public void addExtractableField(String key, String jsonPath) {
		extractableFields.put(key, jsonPath);
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
