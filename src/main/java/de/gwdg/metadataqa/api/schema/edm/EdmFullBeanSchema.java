package de.gwdg.metadataqa.api.schema.edm;

import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.JsonBranch;
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
public class EdmFullBeanSchema extends EdmSchema implements Serializable {

  private static final long serialVersionUID = 3673596446212645981L;

  public static final String ABOUT = "$.['about']";

  public EdmFullBeanSchema() {
    initialize();
  }

  private void initialize() {
    longSubjectPath = "$.['proxies'][?(@['europeanaProxy'] == false)]['dcSubject']";
    titlePath = "$.['proxies'][?(@['europeanaProxy'] == false)]['dcTitle']";
    descriptionPath = "$.['proxies'][?(@['europeanaProxy'] == false)]['dcDescription']";

    var providedCHO = new JsonBranch("ProvidedCHO", "$.['providedCHOs'][0]");
    providedCHO.setCollection(true);
    addPath(providedCHO);

    var providedCHOIdentifier = new JsonBranch("ProvidedCHO/rdf:about",
      providedCHO, ABOUT)
      .setCategories(MANDATORY);
    providedCHO.setIdentifier(providedCHOIdentifier);
    addPath(providedCHOIdentifier);

    var proxy = new JsonBranch("Proxy", "$.['proxies'][?(@['europeanaProxy'] == false)]");
    proxy.setCollection(true);
    addPath(proxy);

    var proxyIdentifier = new JsonBranch("Proxy/rdf:about", proxy, ABOUT);
    proxy.setIdentifier(proxyIdentifier);
    addPath(proxyIdentifier);

    addPath(new JsonBranch("Proxy/dc:title", proxy, "$.['dcTitle']")
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, IDENTIFICATION, MULTILINGUALITY)
      .setIndexField("dc_title_txt"));
    addPath(new JsonBranch("Proxy/dcterms:alternative", proxy, "$.['dctermsAlternative']")
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, IDENTIFICATION, MULTILINGUALITY)
      .setIndexField("dcterms_alternative_txt"));
    addPath(new JsonBranch("Proxy/dc:description", proxy, "$.['dcDescription']")
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, CONTEXTUALIZATION, IDENTIFICATION, MULTILINGUALITY)
      .setIndexField("dc_description_txt"));
    addPath(new JsonBranch("Proxy/dc:creator", proxy, "$.['dcCreator']")
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new JsonBranch("Proxy/dc:publisher", proxy, "$.['dcPublisher']")
      .setCategories(SEARCHABILITY, REUSABILITY));
    addPath(new JsonBranch("Proxy/dc:contributor", proxy, "$.['dcContributor']")
      .setCategories(SEARCHABILITY));
    addPath(new JsonBranch("Proxy/dc:type", proxy, "$.['dcType']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, IDENTIFICATION, BROWSING));
    addPath(new JsonBranch("Proxy/dc:identifier", proxy, "$.['dcIdentifier']")
      .setCategories(IDENTIFICATION));
    addPath(new JsonBranch("Proxy/dc:language", proxy, "$.['dcLanguage']")
      .setCategories(DESCRIPTIVENESS, MULTILINGUALITY));
    addPath(new JsonBranch("Proxy/dc:coverage", proxy, "$.['dcCoverage']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new JsonBranch("Proxy/dcterms:temporal", proxy, "$.['dctermsTemporal']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new JsonBranch("Proxy/dcterms:spatial", proxy, "$.['dctermsSpatial']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new JsonBranch("Proxy/dc:subject", proxy, "$.['dcSubject']")
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, CONTEXTUALIZATION, MULTILINGUALITY));
    addPath(new JsonBranch("Proxy/dc:date", proxy, "$.['dcDate']")
      .setCategories(IDENTIFICATION, BROWSING, REUSABILITY));
    addPath(new JsonBranch("Proxy/dcterms:created", proxy, "$.['dctermsCreated']")
      .setCategories(IDENTIFICATION, REUSABILITY));
    addPath(new JsonBranch("Proxy/dcterms:issued", proxy, "$.['dctermsIssued']")
      .setCategories(IDENTIFICATION, REUSABILITY));
    addPath(new JsonBranch("Proxy/dcterms:extent", proxy, "$.['dctermsExtent']")
      .setCategories(DESCRIPTIVENESS, REUSABILITY));
    addPath(new JsonBranch("Proxy/dcterms:medium", proxy, "$.['dctermsMedium']")
      .setCategories(DESCRIPTIVENESS, REUSABILITY));
    addPath(new JsonBranch("Proxy/dcterms:provenance", proxy, "$.['dctermsProvenance']")
      .setCategories(DESCRIPTIVENESS));
    addPath(new JsonBranch("Proxy/dcterms:hasPart", proxy, "$.['dctermsHasPart']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new JsonBranch("Proxy/dcterms:isPartOf", proxy, "$.['dctermsIsPartOf']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new JsonBranch("Proxy/dc:format", proxy, "$.['dcFormat']")
      .setCategories(DESCRIPTIVENESS, REUSABILITY));
    addPath(new JsonBranch("Proxy/dc:source", proxy, "$.['dcSource']")
      .setCategories(DESCRIPTIVENESS));
    addPath(new JsonBranch("Proxy/dc:rights", proxy, "$.['dcRights']")
      .setCategories(REUSABILITY));
    addPath(new JsonBranch("Proxy/dc:relation", proxy, "$.['dcRelation']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new JsonBranch("Proxy/edm:isNextInSequence", proxy, "$.['edmIsNextInSequence']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new JsonBranch("Proxy/edm:type", proxy, "$.['edmType']")
      .setCategories(SEARCHABILITY, BROWSING));
    /*
    addPath(new JsonBranch("Proxy/edm:rights", proxy, "$.['edm:rights']",
      Category.MANDATORY, Category.REUSABILITY));
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

    var aggregation = new JsonBranch("Aggregation", "$.['aggregations'][0]");
    aggregation.setCollection(true);
    addPath(aggregation);
    var aggregationIdentifier = new JsonBranch("Aggregation/rdf:about", aggregation, ABOUT);
    addPath(aggregationIdentifier);
    aggregation.setIdentifier(aggregationIdentifier);

    addPath(new JsonBranch("Aggregation/edm:rights", aggregation, "$.['edmRights']")
      .setCategories(MANDATORY, REUSABILITY));
    addPath(new JsonBranch("Aggregation/edm:provider", aggregation, "$.['edmProvider']")
      .setCategories(MANDATORY, SEARCHABILITY, IDENTIFICATION));
    addPath(new JsonBranch("Aggregation/edm:dataProvider", aggregation, "$.['edmDataProvider']")
      .setCategories(MANDATORY, SEARCHABILITY, IDENTIFICATION));
    addPath(new JsonBranch("Aggregation/edm:isShownAt", aggregation, "$.['edmIsShownAt']")
      .setCategories(BROWSING, VIEWING));
    addPath(new JsonBranch("Aggregation/edm:isShownBy", aggregation, "$.['edmIsShownBy']")
      .setCategories(BROWSING, VIEWING, REUSABILITY));
    addPath(new JsonBranch("Aggregation/edm:object", aggregation, "$.['edmObject']")
      .setCategories(VIEWING, REUSABILITY));
    addPath(new JsonBranch("Aggregation/edm:hasView", aggregation, "$.['hasView']")
      .setCategories(BROWSING, VIEWING));
    addPath(new JsonBranch("Aggregation/dc:rights", aggregation, "$.['dcRights']"));
    addPath(new JsonBranch("Aggregation/edm:ugc", aggregation, "$.['edmUgc']"));
    addPath(new JsonBranch("Aggregation/edm:aggregatedCHO", aggregation, "$.['aggregatedCHO']"));
    addPath(new JsonBranch("Aggregation/edm:intermediateProvider", aggregation, "$.['edmIntermediateProvider']"));

    var place = new JsonBranch("Place", "$.['places']");
    place.setCollection(true);
    addPath(place);
    var placeIdentifier = new JsonBranch("Place/rdf:about", place, ABOUT);
    addPath(placeIdentifier);
    place.setIdentifier(placeIdentifier);

    addPath(new JsonBranch("Place/rdf:about", place, ABOUT));
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

    var agent = new JsonBranch("Agent", "$.['agents']");
    agent.setCollection(true);
    addPath(agent);
    var agentIdentifier = new JsonBranch("Agent/rdf:about", agent, ABOUT);
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

    var timespan = new JsonBranch("Timespan", "$.['timespans']");
    timespan.setCollection(true);
    addPath(timespan);

    var timespanIdentifier = new JsonBranch("Timespan/rdf:about", timespan, ABOUT);
    addPath(timespanIdentifier);
    timespan.setIdentifier(timespanIdentifier);

    addPath(new JsonBranch("Timespan/rdf:about", timespan, ABOUT));
    addPath(new JsonBranch("Timespan/edm:begin", timespan, "$.['begin']"));
    addPath(new JsonBranch("Timespan/edm:end", timespan, "$.['end']"));
    addPath(new JsonBranch("Timespan/dcterms:isPartOf", timespan, "$.['isPartOf']"));
    addPath(new JsonBranch("Timespan/dcterms:hasPart", timespan, "$.['hasPart']"));
    addPath(new JsonBranch("Timespan/edm:isNextInSequence", timespan, "$.['edm:isNextInSequence']"));
    addPath(new JsonBranch("Timespan/owl:sameAs", timespan, "$.['owlSameAs']"));
    addPath(new JsonBranch("Timespan/skos:prefLabel", timespan, "$.['prefLabel']"));
    addPath(new JsonBranch("Timespan/skos:altLabel", timespan, "$.['altLabel']"));
    addPath(new JsonBranch("Timespan/skos:note", timespan, "$.['note']"));

    var concept = new JsonBranch("Concept", "$.['concepts']");
    concept.setCollection(true);
    addPath(concept);

    var conceptIdentifier = new JsonBranch("Concept/rdf:about", concept, ABOUT);
    addPath(conceptIdentifier);
    concept.setIdentifier(conceptIdentifier);

    addPath(new JsonBranch("Concept/rdf:about", concept, ABOUT));
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

    var europeanaAggregation = new JsonBranch("EuropeanaAggregation", "$.['europeanaAggregation']")
      .setActive(false);
    europeanaAggregation.setCollection(true);
    addPath(europeanaAggregation);
    addPath(new JsonBranch("EuropeanaAggregation/edm:country", europeanaAggregation, "$.['edmCountry']")
      .setActive(false));
    addPath(new JsonBranch("EuropeanaAggregation/edm:language", europeanaAggregation, "$.['edmLanguage']")
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
        "Proxy/edm:year", "Proxy/edm:userTag", "Proxy/edm:hasMet", "Proxy/edm:incorporates",
        "Proxy/edm:isDerivativeOf", "Proxy/edm:isRepresentationOf", "Proxy/edm:isSimilarTo",
        "Proxy/edm:isSuccessorOf", "Proxy/edm:realizes", "Proxy/edm:wasPresentAt",
      "Aggregation",
        "Aggregation/edm:isShownAt", "Aggregation/edm:isShownBy", "Aggregation/edm:object",
        "Aggregation/edm:hasView", "Aggregation/rdf:about", "Aggregation/edm:rights",
        "Aggregation/edm:ugc", "Aggregation/edm:aggregatedCHO",
      "Agent",
        "Agent/rdf:about", "Agent/edm:hasMet", "Agent/edm:isRelatedTo", "Agent/owl:sameAs",
      "Concept",
        "Concept/rdf:about", "Concept/skos:broader", "Concept/skos:narrower", "Concept/skos:related",
        "Concept/skos:broadMatch", "Concept/skos:narrowMatch", "Concept/skos:relatedMatch",
        "Concept/skos:exactMatch", "Concept/skos:closeMatch", "Concept/skos:notation",
        "Concept/skos:inScheme",
      "Place",
        "Place/rdf:about", "Place/wgs84:lat", "Place/wgs84:long",
        "Place/wgs84:alt", "Place/wgs84_pos:lat_long", "Place/owl:sameAs",
      "Timespan",
        "Timespan/rdf:about", "Timespan/owl:sameAs"
    ));

    emptyStrings.add(titlePath);
    emptyStrings.add(descriptionPath);
    emptyStrings.add(longSubjectPath);

    extractableFields.put("recordId", "$.identifier");
    extractableFields.put("dataset", "$.sets[0]");
    extractableFields.put("dataProvider", "$.['aggregations'][0]['edmDataProvider'][0]");
    // extractableFields.put("country", "$.['europeanaAggregation'][0]['edmCountry'][0]");
    // extractableFields.put("language", "$.['europeanaAggregation'][0]['edmLanguage'][0]");
  }

  @Override
  public Format getFormat() {
    return Format.JSON;
  }

  @Override
  public List<JsonBranch> getCollectionPaths() {
    return new ArrayList(collectionPaths.values());
  }

}
