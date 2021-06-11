package de.gwdg.metadataqa.api.schema.edm;

import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.Category;
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
public class EdmOaiPmhXmlSchema extends EdmSchema implements Serializable {

  private static final long serialVersionUID = -3148099352945824132L;

  public EdmOaiPmhXmlSchema() {
    initialize();
  }

  private void initialize() {
    longSubjectPath = "//ore:Proxy[edm:europeanaProxy/text() = 'false']/dc:subject";
    titlePath = "//ore:Proxy[edm:europeanaProxy/text() = 'false']/dc:title";
    descriptionPath = "//ore:Proxy[edm:europeanaProxy/text() = 'false']/dc:description";

    var providedCHO = new JsonBranch("ProvidedCHO", "//edm:ProvidedCHO[1]");
    providedCHO.setCollection(true);
    addPath(providedCHO);
    var providedCHOIdentifier = new JsonBranch("ProvidedCHO/rdf:about",
      providedCHO, "@rdf:about")
      .setCategories(MANDATORY);
    providedCHO.setIdentifier(providedCHOIdentifier);
    addPath(providedCHOIdentifier);

    var proxy = new JsonBranch(
      "Proxy",
      "//ore:Proxy[edm:europeanaProxy/text() = 'false']"
    );
    proxy.setCollection(true);
    addPath(proxy);
    var proxyIdentifier = new JsonBranch("Proxy/rdf:about", proxy, "@rdf:about");
    proxy.setIdentifier(proxyIdentifier);
    addPath(proxyIdentifier);

    addPath(new JsonBranch("Proxy/dc:title", proxy, "dc:title")
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, IDENTIFICATION, MULTILINGUALITY)
      .setIndexField("dc_title_txt"));
    addPath(new JsonBranch("Proxy/dcterms:alternative", proxy, "dcterms:alternative")
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, IDENTIFICATION, MULTILINGUALITY)
      .setIndexField("dcterms_alternative_txt"));
    addPath(new JsonBranch("Proxy/dc:description", proxy, "dc:description")
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, CONTEXTUALIZATION, IDENTIFICATION, MULTILINGUALITY)
      .setIndexField("dc_description_txt"));
    addPath(new JsonBranch("Proxy/dc:creator", proxy, "dc:creator")
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new JsonBranch("Proxy/dc:publisher", proxy, "dc:publisher")
      .setCategories(SEARCHABILITY, REUSABILITY));
    addPath(new JsonBranch("Proxy/dc:contributor", proxy, "dc:contributor")
      .setCategories(SEARCHABILITY));
    addPath(new JsonBranch("Proxy/dc:type", proxy, "dc:type")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, IDENTIFICATION, BROWSING));
    addPath(new JsonBranch("Proxy/dc:identifier", proxy, "dc:identifier")
      .setCategories(IDENTIFICATION));
    addPath(new JsonBranch("Proxy/dc:language", proxy, "dc:language")
      .setCategories(DESCRIPTIVENESS, MULTILINGUALITY));
    addPath(new JsonBranch("Proxy/dc:coverage", proxy, "dc:coverage")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new JsonBranch("Proxy/dcterms:temporal", proxy, "dcterms:temporal")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new JsonBranch("Proxy/dcterms:spatial", proxy, "dcterms:spatial")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new JsonBranch("Proxy/dc:subject", proxy, "dc:subject")
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, CONTEXTUALIZATION, MULTILINGUALITY));
    addPath(new JsonBranch("Proxy/dc:date", proxy, "dc:date")
      .setCategories(IDENTIFICATION, BROWSING, REUSABILITY));
    addPath(new JsonBranch("Proxy/dcterms:created", proxy, "dcterms:created")
      .setCategories(IDENTIFICATION, REUSABILITY));
    addPath(new JsonBranch("Proxy/dcterms:issued", proxy, "dcterms:issued")
      .setCategories(IDENTIFICATION, REUSABILITY));
    addPath(new JsonBranch("Proxy/dcterms:extent", proxy, "dcterms:extent")
      .setCategories(DESCRIPTIVENESS, REUSABILITY));
    addPath(new JsonBranch("Proxy/dcterms:medium", proxy, "dcterms:medium")
      .setCategories(DESCRIPTIVENESS, REUSABILITY));
    addPath(new JsonBranch("Proxy/dcterms:provenance", proxy, "dcterms:provenance")
      .setCategories(DESCRIPTIVENESS));
    addPath(new JsonBranch("Proxy/dcterms:hasPart", proxy, "dcterms:hasPart")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new JsonBranch("Proxy/dcterms:isPartOf", proxy, "dcterms:isPartOf")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new JsonBranch("Proxy/dc:format", proxy, "dc:format")
      .setCategories(DESCRIPTIVENESS, REUSABILITY));
    addPath(new JsonBranch("Proxy/dc:source", proxy, "dc:source")
      .setCategories(DESCRIPTIVENESS));
    addPath(new JsonBranch("Proxy/dc:rights", proxy, "dc:rights")
      .setCategories(REUSABILITY));
    addPath(new JsonBranch("Proxy/dc:relation", proxy, "dc:relation")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new JsonBranch("Proxy/edm:isNextInSequence", proxy, "edm:isNextInSequence")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new JsonBranch("Proxy/edm:type", proxy, "edm:type")
      .setCategories(SEARCHABILITY, BROWSING));
    addPath(new JsonBranch("Proxy/edm:europeanaProxy", proxy, "edm:europeanaProxy"));
    addPath(new JsonBranch("Proxy/edm:year", proxy, "edm:year"));
    addPath(new JsonBranch("Proxy/edm:userTag", proxy, "edm:userTag"));
    addPath(new JsonBranch("Proxy/ore:proxyIn", proxy, "ore:proxyIn"));
    addPath(new JsonBranch("Proxy/ore:proxyFor", proxy, "ore:proxyFor"));
    addPath(new JsonBranch("Proxy/dcterms:conformsTo", proxy, "dcterms:conformsTo"));
    addPath(new JsonBranch("Proxy/dcterms:hasFormat", proxy, "dcterms:hasFormat"));
    addPath(new JsonBranch("Proxy/dcterms:hasVersion", proxy, "dcterms:hasVersion"));
    addPath(new JsonBranch("Proxy/dcterms:isFormatOf", proxy, "dcterms:isFormatOf"));
    addPath(new JsonBranch("Proxy/dcterms:isReferencedBy", proxy, "dcterms:isReferencedBy"));
    addPath(new JsonBranch("Proxy/dcterms:isReplacedBy", proxy, "dcterms:isReplacedBy"));
    addPath(new JsonBranch("Proxy/dcterms:isRequiredBy", proxy, "dcterms:isRequiredBy"));
    addPath(new JsonBranch("Proxy/dcterms:isVersionOf", proxy, "dcterms:isVersionOf"));
    addPath(new JsonBranch("Proxy/dcterms:references", proxy, "dcterms:references"));
    addPath(new JsonBranch("Proxy/dcterms:replaces", proxy, "dcterms:replaces"));
    addPath(new JsonBranch("Proxy/dcterms:requires", proxy, "dcterms:requires"));
    addPath(new JsonBranch("Proxy/dcterms:tableOfContents", proxy, "dcterms:tableOfContents"));
    addPath(new JsonBranch("Proxy/edm:currentLocation", proxy, "edm:currentLocation"));
    addPath(new JsonBranch("Proxy/edm:hasMet", proxy, "edm:hasMet"));
    addPath(new JsonBranch("Proxy/edm:hasType", proxy, "edm:hasType"));
    addPath(new JsonBranch("Proxy/edm:incorporates", proxy, "edm:incorporates"));
    addPath(new JsonBranch("Proxy/edm:isDerivativeOf", proxy, "edm:isDerivativeOf"));
    addPath(new JsonBranch("Proxy/edm:isRelatedTo", proxy, "edm:isRelatedTo"));
    addPath(new JsonBranch("Proxy/edm:isRepresentationOf", proxy, "edm:isRepresentationOf"));
    addPath(new JsonBranch("Proxy/edm:isSimilarTo", proxy, "edm:isSimilarTo"));
    addPath(new JsonBranch("Proxy/edm:isSuccessorOf", proxy, "edm:isSuccessorOf"));
    addPath(new JsonBranch("Proxy/edm:realizes", proxy, "edm:realizes"));
    addPath(new JsonBranch("Proxy/edm:wasPresentAt", proxy, "edm:wasPresentAt"));

    var aggregation = new JsonBranch("Aggregation", "//ore:Aggregation");
    aggregation.setCollection(true);
    addPath(aggregation);
    var aggregationIdentifier = new JsonBranch(
      "Aggregation/rdf:about", aggregation, "@rdf:about");
    addPath(aggregationIdentifier);
    aggregation.setIdentifier(aggregationIdentifier);

    addPath(new JsonBranch("Aggregation/edm:rights", aggregation, "edm:rights")
      .setCategories(MANDATORY, REUSABILITY));
    addPath(new JsonBranch("Aggregation/edm:provider", aggregation, "edm:provider")
      .setCategories(MANDATORY, SEARCHABILITY, IDENTIFICATION));
    addPath(new JsonBranch("Aggregation/edm:dataProvider", aggregation, "edm:dataProvider")
      .setCategories(MANDATORY, SEARCHABILITY, IDENTIFICATION));
    addPath(new JsonBranch("Aggregation/edm:isShownAt", aggregation, "edm:isShownAt")
      .setCategories(BROWSING, VIEWING));
    addPath(new JsonBranch("Aggregation/edm:isShownBy", aggregation, "edm:isShownBy")
      .setCategories(BROWSING, VIEWING, REUSABILITY));
    addPath(new JsonBranch("Aggregation/edm:object", aggregation, "edm:object")
      .setCategories(VIEWING, REUSABILITY));
    addPath(new JsonBranch("Aggregation/edm:hasView", aggregation, "edm:hasView")
      .setCategories(BROWSING, VIEWING));
    addPath(new JsonBranch("Aggregation/dc:rights", aggregation, "dc:rights"));
    addPath(new JsonBranch("Aggregation/edm:ugc", aggregation, "edm:ugc"));
    addPath(new JsonBranch("Aggregation/edm:aggregatedCHO", aggregation, "edm:aggregatedCHO"));
    addPath(new JsonBranch("Aggregation/edm:intermediateProvider", aggregation, "edm:intermediateProvider"));

    var place = new JsonBranch("Place", "//edm:Place");
    place.setCollection(true);
    addPath(place);
    var placeIdentifier = new JsonBranch("Place/rdf:about", place, "@rdf:about");
    addPath(placeIdentifier);
    place.setIdentifier(placeIdentifier);

    addPath(new JsonBranch("Place/wgs84:lat", place, "wgs84:lat"));
    addPath(new JsonBranch("Place/wgs84:long", place, "wgs84:long"));
    addPath(new JsonBranch("Place/wgs84:alt", place, "wgs84:alt"));
    addPath(new JsonBranch("Place/dcterms:isPartOf", place, "dcterms:isPartOf"));
    addPath(new JsonBranch("Place/wgs84_pos:lat_long", place, "wgs84_pos:lat_long"));
    addPath(new JsonBranch("Place/dcterms:hasPart", place, "dcterms:hasPart"));
    addPath(new JsonBranch("Place/owl:sameAs", place, "owl:sameAs"));
    addPath(new JsonBranch("Place/skos:prefLabel", place, "skos:prefLabel"));
    addPath(new JsonBranch("Place/skos:altLabel", place, "skos:altLabel"));
    addPath(new JsonBranch("Place/skos:note", place, "skos:note"));

    var agent = new JsonBranch("Agent", "//edm:Agent");
    agent.setCollection(true);
    addPath(agent);

    var agentIdentifier = new JsonBranch("Agent/rdf:about", agent, "@rdf:about");
    addPath(agentIdentifier);
    agent.setIdentifier(agentIdentifier);
    addPath(new JsonBranch("Agent/edm:begin", agent, "edm:begin"));
    addPath(new JsonBranch("Agent/edm:end", agent, "edm:end"));
    addPath(new JsonBranch("Agent/edm:hasMet", agent, "edm:hasMet"));
    addPath(new JsonBranch("Agent/edm:isRelatedTo", agent, "edm:isRelatedTo"));
    addPath(new JsonBranch("Agent/owl:sameAs", agent, "owl:sameAs"));
    addPath(new JsonBranch("Agent/foaf:name", agent, "foaf:name"));
    addPath(new JsonBranch("Agent/dc:date", agent, "dc:date"));
    addPath(new JsonBranch("Agent/dc:identifier", agent, "dc:identifier"));
    addPath(new JsonBranch("Agent/rdaGr2:dateOfBirth", agent, "rdaGr2:dateOfBirth"));
    addPath(new JsonBranch("Agent/rdaGr2:placeOfBirth", agent, "rdaGr2:placeOfBirth"));
    addPath(new JsonBranch("Agent/rdaGr2:dateOfDeath", agent, "rdaGr2:dateOfDeath"));
    addPath(new JsonBranch("Agent/rdaGr2:placeOfDeath", agent, "rdaGr2:placeOfDeath"));
    addPath(new JsonBranch("Agent/rdaGr2:dateOfEstablishment", agent, "rdaGr2:dateOfEstablishment"));
    addPath(new JsonBranch("Agent/rdaGr2:dateOfTermination", agent, "rdaGr2:dateOfTermination"));
    addPath(new JsonBranch("Agent/rdaGr2:gender", agent, "rdaGr2:gender"));
    addPath(new JsonBranch("Agent/rdaGr2:professionOrOccupation", agent, "rdaGr2:professionOrOccupation"));
    addPath(new JsonBranch("Agent/rdaGr2:biographicalInformation", agent, "rdaGr2:biographicalInformation"));
    addPath(new JsonBranch("Agent/skos:prefLabel", agent, "skos:prefLabel"));
    addPath(new JsonBranch("Agent/skos:altLabel", agent, "skos:altLabel"));
    addPath(new JsonBranch("Agent/skos:note", agent, "skos:note"));

    var timespan = new JsonBranch("Timespan", "//edm:TimeSpan");
    timespan.setCollection(true);
    addPath(timespan);

    var timespanIdentifier = new JsonBranch(
      "Timespan/rdf:about", timespan, "@rdf:about");
    addPath(timespanIdentifier);
    timespan.setIdentifier(timespanIdentifier);
    addPath(new JsonBranch("Timespan/edm:begin", timespan, "edm:begin"));
    addPath(new JsonBranch("Timespan/edm:end", timespan, "edm:end"));
    addPath(new JsonBranch("Timespan/dcterms:isPartOf", timespan, "dcterms:isPartOf"));
    addPath(new JsonBranch("Timespan/dcterms:hasPart", timespan, "dcterms:hasPart"));
    addPath(new JsonBranch("Timespan/edm:isNextInSequence", timespan, "edm:isNextInSequence"));
    addPath(new JsonBranch("Timespan/owl:sameAs", timespan, "owl:sameAs"));
    addPath(new JsonBranch("Timespan/skos:prefLabel", timespan, "skos:prefLabel"));
    addPath(new JsonBranch("Timespan/skos:altLabel", timespan, "skos:altLabel"));
    addPath(new JsonBranch("Timespan/skos:note", timespan, "skos:note"));

    var concept = new JsonBranch("Concept", "//skos:Concept");
    concept.setCollection(true);
    addPath(concept);

    var conceptIdentifier = new JsonBranch(
      "Concept/rdf:about", concept, "@rdf:about");
    addPath(conceptIdentifier);
    concept.setIdentifier(conceptIdentifier);
    addPath(new JsonBranch("Concept/skos:broader", concept, "skos:broader"));
    addPath(new JsonBranch("Concept/skos:narrower", concept, "skos:narrower"));
    addPath(new JsonBranch("Concept/skos:related", concept, "skos:related"));
    addPath(new JsonBranch("Concept/skos:broadMatch", concept, "skos:broadMatch"));
    addPath(new JsonBranch("Concept/skos:narrowMatch", concept, "skos:narrowMatch"));
    addPath(new JsonBranch("Concept/skos:relatedMatch", concept, "skos:relatedMatch"));
    addPath(new JsonBranch("Concept/skos:exactMatch", concept, "skos:exactMatch"));
    addPath(new JsonBranch("Concept/skos:closeMatch", concept, "skos:closeMatch"));
    addPath(new JsonBranch("Concept/skos:notation", concept, "skos:notation"));
    addPath(new JsonBranch("Concept/skos:inScheme", concept, "skos:inScheme"));
    addPath(new JsonBranch("Concept/skos:prefLabel", concept, "skos:prefLabel"));
    addPath(new JsonBranch("Concept/skos:altLabel", concept, "skos:altLabel"));
    addPath(new JsonBranch("Concept/skos:note", concept, "skos:note"));

    var europeanaAggregation = new JsonBranch(
        "EuropeanaAggregation", "//edm:EuropeanaAggregation"
      )
      .setActive(false);
    europeanaAggregation.setCollection(true);
    addPath(europeanaAggregation);
    addPath(new JsonBranch("EuropeanaAggregation/edm:country",
      europeanaAggregation, "edm:country")
      .setActive(false));
    addPath(new JsonBranch("EuropeanaAggregation/edm:language",
      europeanaAggregation, "edm:language")
      .setActive(false));

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

    emptyStrings.add("//ore:Proxy[edm:europeanaProxy/text() = 'false']/dc:title");
    emptyStrings.add("//ore:Proxy[edm:europeanaProxy/text() = 'false']/dc:description");
    emptyStrings.add("//ore:Proxy[edm:europeanaProxy/text() = 'false']/dc:subject");

    extractableFields.put("recordId", "//oai:identifier");
    extractableFields.put("dataset", "//edm:EuropeanaAggregation[1]/edm:datasetName[1]");
    extractableFields.put("dataProvider", "//ore:Aggregation[1]/edm:dataProvider[1]");
    // extractableFields.put("country", "edm:EuropeanaAggregation[1]['edm:country'][1]");
    // extractableFields.put("language", "edm:EuropeanaAggregation[1]['edm:language'][1]");
  }

  @Override
  public Format getFormat() {
    return Format.XML;
  }

  @Override
  public List<JsonBranch> getCollectionPaths() {
    return new ArrayList(collectionPaths.values());
  }
}
