package de.gwdg.metadataqa.api.schema.edm;

import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.schema.Format;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.gwdg.metadataqa.api.model.Category.BROWSING;
import static de.gwdg.metadataqa.api.model.Category.CONTEXTUALIZATION;
import static de.gwdg.metadataqa.api.model.Category.DESCRIPTIVENESS;
import static de.gwdg.metadataqa.api.model.Category.IDENTIFICATION;
import static de.gwdg.metadataqa.api.model.Category.MANDATORY;
import static de.gwdg.metadataqa.api.model.Category.MULTILINGUALITY;
import static de.gwdg.metadataqa.api.model.Category.REUSABILITY;
import static de.gwdg.metadataqa.api.model.Category.SEARCHABILITY;
import static de.gwdg.metadataqa.api.model.Category.VIEWING;

/**
 * The Europeana Data Model (EDM) representation of the metadata schema interface.
 * This class represents what fields will be analyzed in different measurements.
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class EdmOaiPmhJsonSchema extends EdmSchema implements Serializable {

  private static final long serialVersionUID = 5281918481768643599L;

  public EdmOaiPmhJsonSchema() {
    initialize();
  }

  private void initialize() {
    longSubjectPath = "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:subject']";
    titlePath = "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:title']";
    descriptionPath = "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:description']";

    var providedCHO = new DataElement("ProvidedCHO", "$.['edm:ProvidedCHO'][0]");
    providedCHO.setCollection(true);
    addPath(providedCHO);
    var providedCHOIdentifier = new DataElement("ProvidedCHO/rdf:about",
      providedCHO, "$.['@about']")
      .setCategories(MANDATORY)
      .setExtractable();
    providedCHO.setIdentifier(providedCHOIdentifier);
    addPath(providedCHOIdentifier);

    var proxy = new DataElement("Proxy",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]");
    proxy.setCollection(true);
    addPath(proxy);
    var proxyIdentifier = new DataElement("Proxy/rdf:about", proxy, "$.['@about']");
    proxy.setIdentifier(proxyIdentifier);
    addPath(proxyIdentifier);

    addPath(new DataElement("Proxy/dc:title", proxy, "$.['dc:title']")
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, IDENTIFICATION, MULTILINGUALITY)
      .setIndexField("dc_title_txt"));
    addPath(new DataElement("Proxy/dcterms:alternative", proxy, "$.['dcterms:alternative']")
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, IDENTIFICATION, MULTILINGUALITY)
      .setIndexField("dcterms_alternative_txt"));
    addPath(new DataElement("Proxy/dc:description", proxy, "$.['dc:description']")
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, CONTEXTUALIZATION, IDENTIFICATION, MULTILINGUALITY)
      .setIndexField("dc_description_txt"));
    addPath(new DataElement("Proxy/dc:creator", proxy, "$.['dc:creator']")
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/dc:publisher", proxy, "$.['dc:publisher']")
      .setCategories(SEARCHABILITY, REUSABILITY));
    addPath(new DataElement("Proxy/dc:contributor", proxy, "$.['dc:contributor']")
      .setCategories(SEARCHABILITY));
    addPath(new DataElement("Proxy/dc:type", proxy, "$.['dc:type']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, IDENTIFICATION, BROWSING));
    addPath(new DataElement("Proxy/dc:identifier", proxy, "$.['dc:identifier']")
      .setCategories(IDENTIFICATION));
    addPath(new DataElement("Proxy/dc:language", proxy, "$.['dc:language']")
      .setCategories(DESCRIPTIVENESS, MULTILINGUALITY));
    addPath(new DataElement("Proxy/dc:coverage", proxy, "$.['dc:coverage']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/dcterms:temporal", proxy, "$.['dcterms:temporal']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/dcterms:spatial", proxy, "$.['dcterms:spatial']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/dc:subject", proxy, "$.['dc:subject']")
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, CONTEXTUALIZATION, MULTILINGUALITY));
    addPath(new DataElement("Proxy/dc:date", proxy, "$.['dc:date']")
      .setCategories(IDENTIFICATION, BROWSING, REUSABILITY));
    addPath(new DataElement("Proxy/dcterms:created", proxy, "$.['dcterms:created']")
      .setCategories(IDENTIFICATION, REUSABILITY));
    addPath(new DataElement("Proxy/dcterms:issued", proxy, "$.['dcterms:issued']")
      .setCategories(IDENTIFICATION, REUSABILITY));
    addPath(new DataElement("Proxy/dcterms:extent", proxy, "$.['dcterms:extent']")
      .setCategories(DESCRIPTIVENESS, REUSABILITY));
    addPath(new DataElement("Proxy/dcterms:medium", proxy, "$.['dcterms:medium']")
      .setCategories(DESCRIPTIVENESS, REUSABILITY));
    addPath(new DataElement("Proxy/dcterms:provenance", proxy, "$.['dcterms:provenance']")
      .setCategories(DESCRIPTIVENESS));
    addPath(new DataElement("Proxy/dcterms:hasPart", proxy, "$.['dcterms:hasPart']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/dcterms:isPartOf", proxy, "$.['dcterms:isPartOf']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/dc:format", proxy, "$.['dc:format']")
      .setCategories(DESCRIPTIVENESS, REUSABILITY));
    addPath(new DataElement("Proxy/dc:source", proxy, "$.['dc:source']")
      .setCategories(DESCRIPTIVENESS));
    addPath(new DataElement("Proxy/dc:rights", proxy, "$.['dc:rights']")
      .setCategories(REUSABILITY));
    addPath(new DataElement("Proxy/dc:relation", proxy, "$.['dc:relation']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/edm:isNextInSequence", proxy, "$.['edm:isNextInSequence']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/edm:type", proxy, "$.['edm:type']")
      .setCategories(SEARCHABILITY, BROWSING));
    addPath(new DataElement("Proxy/edm:europeanaProxy", proxy, "$.['edm:europeanaProxy']"));
    addPath(new DataElement("Proxy/edm:year", proxy, "$.['edm:year']"));
    addPath(new DataElement("Proxy/edm:userTag", proxy, "$.['edm:userTag']"));
    addPath(new DataElement("Proxy/ore:proxyIn", proxy, "$.['ore:proxyIn']"));
    addPath(new DataElement("Proxy/ore:proxyFor", proxy, "$.['ore:proxyFor']"));
    addPath(new DataElement("Proxy/dcterms:conformsTo", proxy, "$.['dcterms:conformsTo']"));
    addPath(new DataElement("Proxy/dcterms:hasFormat", proxy, "$.['dcterms:hasFormat']"));
    addPath(new DataElement("Proxy/dcterms:hasVersion", proxy, "$.['dcterms:hasVersion']"));
    addPath(new DataElement("Proxy/dcterms:isFormatOf", proxy, "$.['dcterms:isFormatOf']"));
    addPath(new DataElement("Proxy/dcterms:isReferencedBy", proxy, "$.['dcterms:isReferencedBy']"));
    addPath(new DataElement("Proxy/dcterms:isReplacedBy", proxy, "$.['dcterms:isReplacedBy']"));
    addPath(new DataElement("Proxy/dcterms:isRequiredBy", proxy, "$.['dcterms:isRequiredBy']"));
    addPath(new DataElement("Proxy/dcterms:isVersionOf", proxy, "$.['dcterms:isVersionOf']"));
    addPath(new DataElement("Proxy/dcterms:references", proxy, "$.['dcterms:references']"));
    addPath(new DataElement("Proxy/dcterms:replaces", proxy, "$.['dcterms:replaces']"));
    addPath(new DataElement("Proxy/dcterms:requires", proxy, "$.['dcterms:requires']"));
    addPath(new DataElement("Proxy/dcterms:tableOfContents", proxy, "$.['dcterms:tableOfContents']"));
    addPath(new DataElement("Proxy/edm:currentLocation", proxy, "$.['edm:currentLocation']"));
    addPath(new DataElement("Proxy/edm:hasMet", proxy, "$.['edm:hasMet']"));
    addPath(new DataElement("Proxy/edm:hasType", proxy, "$.['edm:hasType']"));
    addPath(new DataElement("Proxy/edm:incorporates", proxy, "$.['edm:incorporates']"));
    addPath(new DataElement("Proxy/edm:isDerivativeOf", proxy, "$.['edm:isDerivativeOf']"));
    addPath(new DataElement("Proxy/edm:isRelatedTo", proxy, "$.['edm:isRelatedTo']"));
    addPath(new DataElement("Proxy/edm:isRepresentationOf", proxy, "$.['edm:isRepresentationOf']"));
    addPath(new DataElement("Proxy/edm:isSimilarTo", proxy, "$.['edm:isSimilarTo']"));
    addPath(new DataElement("Proxy/edm:isSuccessorOf", proxy, "$.['edm:isSuccessorOf']"));
    addPath(new DataElement("Proxy/edm:realizes", proxy, "$.['edm:realizes']"));
    addPath(new DataElement("Proxy/edm:wasPresentAt", proxy, "$.['edm:wasPresentAt']"));

    var aggregation = new DataElement("Aggregation", "$.['ore:Aggregation']");
    aggregation.setCollection(true);
    addPath(aggregation);
    var aggregationIdentifier = new DataElement("Aggregation/rdf:about", aggregation, "$.['@about']");
    addPath(aggregationIdentifier);
    aggregation.setIdentifier(aggregationIdentifier);

    addPath(new DataElement("Aggregation/edm:rights", aggregation, "$.['edm:rights']")
      .setCategories(MANDATORY, REUSABILITY));
    addPath(new DataElement("Aggregation/edm:provider", aggregation, "$.['edm:provider']")
      .setCategories(MANDATORY, SEARCHABILITY, IDENTIFICATION));
    addPath(new DataElement("Aggregation/edm:dataProvider", aggregation, "$.['edm:dataProvider']")
      .setCategories(MANDATORY, SEARCHABILITY, IDENTIFICATION));
    addPath(new DataElement("Aggregation/edm:isShownAt", aggregation, "$.['edm:isShownAt']")
      .setCategories(BROWSING, VIEWING));
    addPath(new DataElement("Aggregation/edm:isShownBy", aggregation, "$.['edm:isShownBy']")
      .setCategories(BROWSING, VIEWING, REUSABILITY));
    addPath(new DataElement("Aggregation/edm:object", aggregation, "$.['edm:object']")
      .setCategories(VIEWING, REUSABILITY));
    addPath(new DataElement("Aggregation/edm:hasView", aggregation, "$.['edm:hasView']")
      .setCategories(BROWSING, VIEWING));
    addPath(new DataElement("Aggregation/dc:rights", aggregation, "$.['dc:rights']"));
    addPath(new DataElement("Aggregation/edm:ugc", aggregation, "$.['edm:ugc']"));
    addPath(new DataElement("Aggregation/edm:aggregatedCHO", aggregation, "$.['edm:aggregatedCHO']"));
    addPath(new DataElement("Aggregation/edm:intermediateProvider", aggregation, "$.['edm:intermediateProvider']"));

    var place = new DataElement("Place", "$.['edm:Place']");
    place.setCollection(true);
    addPath(place);
    var placeIdentifier = new DataElement("Place/rdf:about", place, "$.['@about']");
    addPath(placeIdentifier);
    place.setIdentifier(placeIdentifier);

    addPath(new DataElement("Place/wgs84:lat", place, "$.['wgs84:lat']"));
    addPath(new DataElement("Place/wgs84:long", place, "$.['wgs84:long']"));
    addPath(new DataElement("Place/wgs84:alt", place, "$.['wgs84:alt']"));
    addPath(new DataElement("Place/dcterms:isPartOf", place, "$.['dcterms:isPartOf']"));
    addPath(new DataElement("Place/wgs84_pos:lat_long", place, "$.['wgs84_pos:lat_long']"));
    addPath(new DataElement("Place/dcterms:hasPart", place, "$.['dcterms:hasPart']"));
    addPath(new DataElement("Place/owl:sameAs", place, "$.['owl:sameAs']"));
    addPath(new DataElement("Place/skos:prefLabel", place, "$.['skos:prefLabel']"));
    addPath(new DataElement("Place/skos:altLabel", place, "$.['skos:altLabel']"));
    addPath(new DataElement("Place/skos:note", place, "$.['skos:note']"));

    var agent = new DataElement("Agent", "$.['edm:Agent']");
    agent.setCollection(true);
    addPath(agent);

    var agentIdentifier = new DataElement("Agent/rdf:about", agent, "$.['@about']");
    addPath(agentIdentifier);
    agent.setIdentifier(agentIdentifier);
    addPath(new DataElement("Agent/edm:begin", agent, "$.['edm:begin']"));
    addPath(new DataElement("Agent/edm:end", agent, "$.['edm:end']"));
    addPath(new DataElement("Agent/edm:hasMet", agent, "$.['edm:hasMet']"));
    addPath(new DataElement("Agent/edm:isRelatedTo", agent, "$.['edm:isRelatedTo']"));
    addPath(new DataElement("Agent/owl:sameAs", agent, "$.['owl:sameAs']"));
    addPath(new DataElement("Agent/foaf:name", agent, "$.['foaf:name']"));
    addPath(new DataElement("Agent/dc:date", agent, "$.['dc:date']"));
    addPath(new DataElement("Agent/dc:identifier", agent, "$.['dc:identifier']"));
    addPath(new DataElement("Agent/rdaGr2:dateOfBirth", agent, "$.['rdaGr2:dateOfBirth']"));
    addPath(new DataElement("Agent/rdaGr2:placeOfBirth", agent, "$.['rdaGr2:placeOfBirth']"));
    addPath(new DataElement("Agent/rdaGr2:dateOfDeath", agent, "$.['rdaGr2:dateOfDeath']"));
    addPath(new DataElement("Agent/rdaGr2:placeOfDeath", agent, "$.['rdaGr2:placeOfDeath']"));
    addPath(new DataElement("Agent/rdaGr2:dateOfEstablishment", agent, "$.['rdaGr2:dateOfEstablishment']"));
    addPath(new DataElement("Agent/rdaGr2:dateOfTermination", agent, "$.['rdaGr2:dateOfTermination']"));
    addPath(new DataElement("Agent/rdaGr2:gender", agent, "$.['rdaGr2:gender']"));
    addPath(new DataElement("Agent/rdaGr2:professionOrOccupation", agent, "$.['rdaGr2:professionOrOccupation']"));
    addPath(new DataElement("Agent/rdaGr2:biographicalInformation", agent, "$.['rdaGr2:biographicalInformation']"));
    addPath(new DataElement("Agent/skos:prefLabel", agent, "$.['skos:prefLabel']"));
    addPath(new DataElement("Agent/skos:altLabel", agent, "$.['skos:altLabel']"));
    addPath(new DataElement("Agent/skos:note", agent, "$.['skos:note']"));

    var timespan = new DataElement("Timespan", "$.['edm:TimeSpan']");
    timespan.setCollection(true);
    addPath(timespan);

    var timespanIdentifier = new DataElement("Timespan/rdf:about", timespan, "$.['@about']");
    addPath(timespanIdentifier);
    timespan.setIdentifier(timespanIdentifier);
    addPath(new DataElement("Timespan/edm:begin", timespan, "$.['edm:begin']"));
    addPath(new DataElement("Timespan/edm:end", timespan, "$.['edm:end']"));
    addPath(new DataElement("Timespan/dcterms:isPartOf", timespan, "$.['dcterms:isPartOf']"));
    addPath(new DataElement("Timespan/dcterms:hasPart", timespan, "$.['dcterms:hasPart']"));
    addPath(new DataElement("Timespan/edm:isNextInSequence", timespan, "$.['edm:isNextInSequence']"));
    addPath(new DataElement("Timespan/owl:sameAs", timespan, "$.['owl:sameAs']"));
    addPath(new DataElement("Timespan/skos:prefLabel", timespan, "$.['skos:prefLabel']"));
    addPath(new DataElement("Timespan/skos:altLabel", timespan, "$.['skos:altLabel']"));
    addPath(new DataElement("Timespan/skos:note", timespan, "$.['skos:note']"));

    var concept = new DataElement("Concept", "$.['skos:Concept']");
    concept.setCollection(true);
    addPath(concept);

    var conceptIdentifier = new DataElement("Concept/rdf:about", concept, "$.['@about']");
    addPath(conceptIdentifier);
    concept.setIdentifier(conceptIdentifier);
    addPath(new DataElement("Concept/skos:broader", concept, "$.['skos:broader']"));
    addPath(new DataElement("Concept/skos:narrower", concept, "$.['skos:narrower']"));
    addPath(new DataElement("Concept/skos:related", concept, "$.['skos:related']"));
    addPath(new DataElement("Concept/skos:broadMatch", concept, "$.['skos:broadMatch']"));
    addPath(new DataElement("Concept/skos:narrowMatch", concept, "$.['skos:narrowMatch']"));
    addPath(new DataElement("Concept/skos:relatedMatch", concept, "$.['skos:relatedMatch']"));
    addPath(new DataElement("Concept/skos:exactMatch", concept, "$.['skos:exactMatch']"));
    addPath(new DataElement("Concept/skos:closeMatch", concept, "$.['skos:closeMatch']"));
    addPath(new DataElement("Concept/skos:notation", concept, "$.['skos:notation']"));
    addPath(new DataElement("Concept/skos:inScheme", concept, "$.['skos:inScheme']"));
    addPath(new DataElement("Concept/skos:prefLabel", concept, "$.['skos:prefLabel']"));
    addPath(new DataElement("Concept/skos:altLabel", concept, "$.['skos:altLabel']"));
    addPath(new DataElement("Concept/skos:note", concept, "$.['skos:note']"));

    var europeanaAggregation = new DataElement("EuropeanaAggregation", "$.['edm:EuropeanaAggregation']")
      .setActive(false);
    europeanaAggregation.setCollection(true);
    addPath(europeanaAggregation);
    addPath(new DataElement("EuropeanaAggregation/edm:country", europeanaAggregation, "$.['edm:country']")
      .setActive(false));
    addPath(new DataElement("EuropeanaAggregation/edm:language", europeanaAggregation, "$.['edm:language']")
      .setActive(false));

    // extractableFields.put("country", "$.['edm:EuropeanaAggregation'][0]['edm:country'][0]");
    // extractableFields.put("language", "$.['edm:EuropeanaAggregation'][0]['edm:language'][0]");

    fieldGroups.add(
      new FieldGroup(
        MANDATORY,
        "Proxy/dc:title", "Proxy/dc:description"));
    fieldGroups.add(
      new FieldGroup(
        MANDATORY,
        "Proxy/dc:type", "Proxy/dc:subject", "Proxy/dc:coverage",
        "Proxy/dcterms:temporal", "Proxy/dcterms:spatial"));
    fieldGroups.add(
      new FieldGroup(
        MANDATORY,
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

    emptyStrings.add("$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:title']");
    emptyStrings.add("$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:description']");
    emptyStrings.add("$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:subject']");

    extractableFields.put("recordId", "$.identifier");
    extractableFields.put("dataset", "$.sets[0]");
    extractableFields.put("dataProvider", "$.['ore:Aggregation'][0]['edm:dataProvider'][0]");
    // extractableFields.put("country", "$.['edm:EuropeanaAggregation'][0]['edm:country'][0]");
    // extractableFields.put("language", "$.['edm:EuropeanaAggregation'][0]['edm:language'][0]");
  }

  @Override
  public Format getFormat() {
    return Format.JSON;
  }

  @Override
  public List<DataElement> getCollectionPaths() {
    return new ArrayList(collectionPaths.values());
  }
}
