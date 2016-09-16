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
public class EdmOaiPmhXmlLimitedSchema extends EdmSchema implements Serializable {

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
		paths.add(new JsonBranch("Aggregation/edm:rights",
			"$.['ore:Aggregation'][0]['edm:rights']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Aggregation/edm:provider",
			"$.['ore:Aggregation'][0]['edm:provider']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.IDENTIFICATION));
		paths.add(new JsonBranch("Aggregation/edm:dataProvider",
			"$.['ore:Aggregation'][0]['edm:dataProvider']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.SEARCHABILITY,
			JsonBranch.Category.IDENTIFICATION));
		paths.add(new JsonBranch("Aggregation/edm:isShownAt",
			"$.['ore:Aggregation'][0]['edm:isShownAt']",
			JsonBranch.Category.BROWSING, JsonBranch.Category.VIEWING));
		paths.add(new JsonBranch("Aggregation/edm:isShownBy",
			"$.['ore:Aggregation'][0]['edm:isShownBy']",
			JsonBranch.Category.BROWSING, JsonBranch.Category.VIEWING,
			JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Aggregation/edm:object",
			"$.['ore:Aggregation'][0]['edm:object']",
			JsonBranch.Category.VIEWING, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Aggregation/edm:hasView",
			"$.['ore:Aggregation'][0]['edm:hasView']",
			JsonBranch.Category.BROWSING, JsonBranch.Category.VIEWING));

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
			"Aggregation/edm:hasView"));

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
