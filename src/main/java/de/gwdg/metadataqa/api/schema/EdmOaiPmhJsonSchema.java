package de.gwdg.metadataqa.api.schema;

import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.Category;
import de.gwdg.metadataqa.api.rule.RuleChecker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The Europeana Data Model (EDM) representation of the metadata schema interface.
 * This class represents what fields will be analyzed in different measurements.
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class EdmOaiPmhJsonSchema extends EdmSchema implements Serializable {

  private static final Logger LOGGER = Logger.getLogger(EdmOaiPmhJsonSchema.class.getCanonicalName());

  private static final List<FieldGroup> FIELD_GROUPS = new ArrayList<>();
  private static final List<String> NO_LANGUAGE_FIELDS = new ArrayList<>();
  private static final Map<String, String> SOLR_FIELDS = new LinkedHashMap<>();
  private Map<String, String> extractableFields = new LinkedHashMap<>();
  private static final List<String> EMPTY_STRINGS = new ArrayList<>();
  private static final Map<String, JsonBranch> PATHS = new LinkedHashMap<>();
  private static final Map<String, JsonBranch> COLLECTION_PATHS = new LinkedHashMap<>();
  private static List<String> categories = null;
  private static List<RuleChecker> ruleCheckers;

  private static final String LONG_SUBJECT_PATH =
    "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:subject']";
  private static final String TITLE_PATH =
    "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:title']";
  private static final String DESCRIPTION_PATH =
    "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:description']";

  static {
    JsonBranch providedCHO = new JsonBranch("ProvidedCHO", "$.['edm:ProvidedCHO'][0]");
    providedCHO.setCollection(true);
    addPath(providedCHO);
    JsonBranch providedCHOIdentifier = new JsonBranch("ProvidedCHO/rdf:about",
      providedCHO, "$.['@about']")
      .setCategories(Category.MANDATORY);
    providedCHO.setIdentifier(providedCHOIdentifier);
    addPath(providedCHOIdentifier);

    JsonBranch proxy = new JsonBranch("Proxy",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]");
    proxy.setCollection(true);
    addPath(proxy);
    JsonBranch proxyIdentifier = new JsonBranch("Proxy/rdf:about", proxy, "$.['@about']");
    proxy.setIdentifier(proxyIdentifier);
    addPath(proxyIdentifier);

    addPath(new JsonBranch("Proxy/dc:title", proxy, "$.['dc:title']")
      .setCategories(Category.DESCRIPTIVENESS, Category.SEARCHABILITY,
      Category.IDENTIFICATION, Category.MULTILINGUALITY));
    addPath(new JsonBranch("Proxy/dcterms:alternative", proxy, "$.['dcterms:alternative']")
      .setCategories(Category.DESCRIPTIVENESS, Category.SEARCHABILITY,
      Category.IDENTIFICATION, Category.MULTILINGUALITY));
    addPath(new JsonBranch("Proxy/dc:description", proxy, "$.['dc:description']")
      .setCategories(Category.DESCRIPTIVENESS, Category.SEARCHABILITY,
      Category.CONTEXTUALIZATION, Category.IDENTIFICATION,
      Category.MULTILINGUALITY));
    addPath(new JsonBranch("Proxy/dc:creator", proxy, "$.['dc:creator']")
      .setCategories(Category.DESCRIPTIVENESS, Category.SEARCHABILITY,
      Category.CONTEXTUALIZATION, Category.BROWSING));
    addPath(new JsonBranch("Proxy/dc:publisher", proxy, "$.['dc:publisher']")
      .setCategories(Category.SEARCHABILITY, Category.REUSABILITY));
    addPath(new JsonBranch("Proxy/dc:contributor", proxy, "$.['dc:contributor']")
      .setCategories(Category.SEARCHABILITY));
    addPath(new JsonBranch("Proxy/dc:type", proxy, "$.['dc:type']")
      .setCategories(Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.IDENTIFICATION, Category.BROWSING));
    addPath(new JsonBranch("Proxy/dc:identifier", proxy, "$.['dc:identifier']")
      .setCategories(Category.IDENTIFICATION));
    addPath(new JsonBranch("Proxy/dc:language", proxy, "$.['dc:language']")
      .setCategories(Category.DESCRIPTIVENESS, Category.MULTILINGUALITY));
    addPath(new JsonBranch("Proxy/dc:coverage", proxy, "$.['dc:coverage']")
      .setCategories(Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.BROWSING));
    addPath(new JsonBranch("Proxy/dcterms:temporal", proxy, "$.['dcterms:temporal']")
      .setCategories(Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.BROWSING));
    addPath(new JsonBranch("Proxy/dcterms:spatial", proxy, "$.['dcterms:spatial']")
      .setCategories(Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.BROWSING));
    addPath(new JsonBranch("Proxy/dc:subject", proxy, "$.['dc:subject']")
      .setCategories(Category.DESCRIPTIVENESS, Category.SEARCHABILITY,
      Category.CONTEXTUALIZATION, Category.MULTILINGUALITY));
    addPath(new JsonBranch("Proxy/dc:date", proxy, "$.['dc:date']")
      .setCategories(Category.IDENTIFICATION, Category.BROWSING,
      Category.REUSABILITY));
    addPath(new JsonBranch("Proxy/dcterms:created", proxy, "$.['dcterms:created']")
      .setCategories(Category.IDENTIFICATION, Category.REUSABILITY));
    addPath(new JsonBranch("Proxy/dcterms:issued", proxy, "$.['dcterms:issued']")
      .setCategories(Category.IDENTIFICATION, Category.REUSABILITY));
    addPath(new JsonBranch("Proxy/dcterms:extent", proxy, "$.['dcterms:extent']")
      .setCategories(Category.DESCRIPTIVENESS, Category.REUSABILITY));
    addPath(new JsonBranch("Proxy/dcterms:medium", proxy, "$.['dcterms:medium']")
      .setCategories(Category.DESCRIPTIVENESS, Category.REUSABILITY));
    addPath(new JsonBranch("Proxy/dcterms:provenance", proxy, "$.['dcterms:provenance']")
      .setCategories(Category.DESCRIPTIVENESS));
    addPath(new JsonBranch("Proxy/dcterms:hasPart", proxy, "$.['dcterms:hasPart']")
      .setCategories(Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.BROWSING));
    addPath(new JsonBranch("Proxy/dcterms:isPartOf", proxy, "$.['dcterms:isPartOf']")
      .setCategories(Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.BROWSING));
    addPath(new JsonBranch("Proxy/dc:format", proxy, "$.['dc:format']")
      .setCategories(Category.DESCRIPTIVENESS, Category.REUSABILITY));
    addPath(new JsonBranch("Proxy/dc:source", proxy, "$.['dc:source']")
      .setCategories(Category.DESCRIPTIVENESS));
    addPath(new JsonBranch("Proxy/dc:rights", proxy, "$.['dc:rights']")
      .setCategories(Category.REUSABILITY));
    addPath(new JsonBranch("Proxy/dc:relation", proxy, "$.['dc:relation']")
      .setCategories(Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.BROWSING));
    addPath(new JsonBranch("Proxy/edm:isNextInSequence", proxy, "$.['edm:isNextInSequence']")
      .setCategories(Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.BROWSING));
    addPath(new JsonBranch("Proxy/edm:type", proxy, "$.['edm:type']")
      .setCategories(Category.SEARCHABILITY, Category.BROWSING));
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

    addPath(new JsonBranch("Aggregation/edm:rights", aggregation, "$.['edm:rights']")
      .setCategories(Category.MANDATORY, Category.REUSABILITY));
    addPath(new JsonBranch("Aggregation/edm:provider", aggregation, "$.['edm:provider']")
      .setCategories(Category.MANDATORY, Category.SEARCHABILITY,
      Category.IDENTIFICATION));
    addPath(new JsonBranch("Aggregation/edm:dataProvider", aggregation, "$.['edm:dataProvider']")
      .setCategories(Category.MANDATORY, Category.SEARCHABILITY,
      Category.IDENTIFICATION));
    addPath(new JsonBranch("Aggregation/edm:isShownAt", aggregation, "$.['edm:isShownAt']")
      .setCategories(Category.BROWSING, Category.VIEWING));
    addPath(new JsonBranch("Aggregation/edm:isShownBy", aggregation, "$.['edm:isShownBy']")
      .setCategories(Category.BROWSING, Category.VIEWING,
      Category.REUSABILITY));
    addPath(new JsonBranch("Aggregation/edm:object", aggregation, "$.['edm:object']")
      .setCategories(Category.VIEWING, Category.REUSABILITY));
    addPath(new JsonBranch("Aggregation/edm:hasView", aggregation, "$.['edm:hasView']")
      .setCategories(Category.BROWSING, Category.VIEWING));
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

    JsonBranch europeanaAggregation = new JsonBranch("EuropeanaAggregation", "$.['edm:EuropeanaAggregation']")
      .setActive(false);
    europeanaAggregation.setCollection(true);
    addPath(europeanaAggregation);
    addPath(new JsonBranch("EuropeanaAggregation/edm:country", europeanaAggregation, "$.['edm:country']")
      .setActive(false));
    addPath(new JsonBranch("EuropeanaAggregation/edm:language", europeanaAggregation, "$.['edm:language']")
      .setActive(false));

    // extractableFields.put("country", "$.['edm:EuropeanaAggregation'][0]['edm:country'][0]");
    // extractableFields.put("language", "$.['edm:EuropeanaAggregation'][0]['edm:language'][0]");

    FIELD_GROUPS.add(
      new FieldGroup(
        Category.MANDATORY,
        "Proxy/dc:title", "Proxy/dc:description"));
    FIELD_GROUPS.add(
      new FieldGroup(
        Category.MANDATORY,
        "Proxy/dc:type", "Proxy/dc:subject", "Proxy/dc:coverage",
        "Proxy/dcterms:temporal", "Proxy/dcterms:spatial"));
    FIELD_GROUPS.add(
      new FieldGroup(
        Category.MANDATORY,
        "Aggregation/edm:isShownAt", "Aggregation/edm:isShownBy"));

    NO_LANGUAGE_FIELDS.addAll(Arrays.asList(
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

    SOLR_FIELDS.put("Proxy/dc:title", "dc_title_txt");
    SOLR_FIELDS.put("Proxy/dcterms:alternative", "dcterms_alternative_txt");
    SOLR_FIELDS.put("Proxy/dc:description", "dc_description_txt");

    EMPTY_STRINGS.add("$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:title']");
    EMPTY_STRINGS.add("$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:description']");
    EMPTY_STRINGS.add("$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:subject']");
  }

  public EdmOaiPmhJsonSchema() {
    extractableFields.put("recordId", "$.identifier");
    extractableFields.put("dataset", "$.sets[0]");
    extractableFields.put("dataProvider", "$.['ore:Aggregation'][0]['edm:dataProvider'][0]");
    // extractableFields.put("country", "$.['edm:EuropeanaAggregation'][0]['edm:country'][0]");
    // extractableFields.put("language", "$.['edm:EuropeanaAggregation'][0]['edm:language'][0]");
  }

  @Override
  public List<FieldGroup> getFieldGroups() {
    return FIELD_GROUPS;
  }

  @Override
  public List<String> getNoLanguageFields() {
    return NO_LANGUAGE_FIELDS;
  }

  @Override
  public Map<String, String> getSolrFields() {
    return SOLR_FIELDS;
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
  public void addExtractableField(String label, String jsonPath) {
    extractableFields.put(label, jsonPath);
  }

  @Override
  public List<String> getEmptyStringPaths() {
    return EMPTY_STRINGS;
  }

  @Override
  public String getSubjectPath() {
    return LONG_SUBJECT_PATH;
  }

  @Override
  public String getTitlePath() {
    return TITLE_PATH;
  }

  @Override
  public String getDescriptionPath() {
    return DESCRIPTION_PATH;
  }

  @Override
  public Format getFormat() {
    return Format.JSON;
  }

  @Override
  public List<JsonBranch> getCollectionPaths() {
    return new ArrayList(COLLECTION_PATHS.values());
  }

  private static void addPath(JsonBranch branch) {
    PATHS.put(branch.getLabel(), branch);

    if (branch.isCollection())
      COLLECTION_PATHS.put(branch.getLabel(), branch);
  }

  @Override
  public List<JsonBranch> getPaths() {
    return new ArrayList(PATHS.values());
  }

  @Override
  public List<JsonBranch> getRootChildrenPaths() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public JsonBranch getPathByLabel(String label) {
    return PATHS.get(label);
  }

  @Override
  public List<String> getCategories() {
    if (categories == null) {
      categories = Category.extractCategories(PATHS.values(), true);
    }
    return categories;
  }

  @Override
  public List<RuleChecker> getRuleCheckers() {
    if (ruleCheckers == null) {
      ruleCheckers = SchemaUtils.getRuleCheckers(this);
    }
    return ruleCheckers;
  }
}
