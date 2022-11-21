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

    var providedCHO = new DataElement("ProvidedCHO", "$.['providedCHOs'][0]");
    providedCHO.setCollection(true);
    addPath(providedCHO);

    var providedCHOIdentifier = new DataElement("ProvidedCHO/rdf:about",
      providedCHO, ABOUT)
      .setCategories(MANDATORY);
    providedCHO.setIdentifier(providedCHOIdentifier);
    addPath(providedCHOIdentifier);

    var proxy = new DataElement("Proxy", "$.['proxies'][?(@['europeanaProxy'] == false)]");
    proxy.setCollection(true);
    addPath(proxy);

    var proxyIdentifier = new DataElement("Proxy/rdf:about", proxy, ABOUT);
    proxy.setIdentifier(proxyIdentifier);
    addPath(proxyIdentifier);

    addPath(new DataElement("Proxy/dc:title", proxy, "$.['dcTitle']")
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, IDENTIFICATION, MULTILINGUALITY)
      .setIndexField("dc_title_txt"));
    addPath(new DataElement("Proxy/dcterms:alternative", proxy, "$.['dctermsAlternative']")
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, IDENTIFICATION, MULTILINGUALITY)
      .setIndexField("dcterms_alternative_txt"));
    addPath(new DataElement("Proxy/dc:description", proxy, "$.['dcDescription']")
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, CONTEXTUALIZATION, IDENTIFICATION, MULTILINGUALITY)
      .setIndexField("dc_description_txt"));
    addPath(new DataElement("Proxy/dc:creator", proxy, "$.['dcCreator']")
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/dc:publisher", proxy, "$.['dcPublisher']")
      .setCategories(SEARCHABILITY, REUSABILITY));
    addPath(new DataElement("Proxy/dc:contributor", proxy, "$.['dcContributor']")
      .setCategories(SEARCHABILITY));
    addPath(new DataElement("Proxy/dc:type", proxy, "$.['dcType']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, IDENTIFICATION, BROWSING));
    addPath(new DataElement("Proxy/dc:identifier", proxy, "$.['dcIdentifier']")
      .setCategories(IDENTIFICATION));
    addPath(new DataElement("Proxy/dc:language", proxy, "$.['dcLanguage']")
      .setCategories(DESCRIPTIVENESS, MULTILINGUALITY));
    addPath(new DataElement("Proxy/dc:coverage", proxy, "$.['dcCoverage']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/dcterms:temporal", proxy, "$.['dctermsTemporal']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/dcterms:spatial", proxy, "$.['dctermsSpatial']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/dc:subject", proxy, "$.['dcSubject']")
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, CONTEXTUALIZATION, MULTILINGUALITY));
    addPath(new DataElement("Proxy/dc:date", proxy, "$.['dcDate']")
      .setCategories(IDENTIFICATION, BROWSING, REUSABILITY));
    addPath(new DataElement("Proxy/dcterms:created", proxy, "$.['dctermsCreated']")
      .setCategories(IDENTIFICATION, REUSABILITY));
    addPath(new DataElement("Proxy/dcterms:issued", proxy, "$.['dctermsIssued']")
      .setCategories(IDENTIFICATION, REUSABILITY));
    addPath(new DataElement("Proxy/dcterms:extent", proxy, "$.['dctermsExtent']")
      .setCategories(DESCRIPTIVENESS, REUSABILITY));
    addPath(new DataElement("Proxy/dcterms:medium", proxy, "$.['dctermsMedium']")
      .setCategories(DESCRIPTIVENESS, REUSABILITY));
    addPath(new DataElement("Proxy/dcterms:provenance", proxy, "$.['dctermsProvenance']")
      .setCategories(DESCRIPTIVENESS));
    addPath(new DataElement("Proxy/dcterms:hasPart", proxy, "$.['dctermsHasPart']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/dcterms:isPartOf", proxy, "$.['dctermsIsPartOf']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/dc:format", proxy, "$.['dcFormat']")
      .setCategories(DESCRIPTIVENESS, REUSABILITY));
    addPath(new DataElement("Proxy/dc:source", proxy, "$.['dcSource']")
      .setCategories(DESCRIPTIVENESS));
    addPath(new DataElement("Proxy/dc:rights", proxy, "$.['dcRights']")
      .setCategories(REUSABILITY));
    addPath(new DataElement("Proxy/dc:relation", proxy, "$.['dcRelation']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/edm:isNextInSequence", proxy, "$.['edmIsNextInSequence']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/edm:type", proxy, "$.['edmType']")
      .setCategories(SEARCHABILITY, BROWSING));
    /*
    addPath(new DataElement("Proxy/edm:rights", proxy, "$.['edm:rights']",
      Category.MANDATORY, Category.REUSABILITY));
    */

    addPath(new DataElement("Proxy/edm:europeanaProxy", proxy, "$.['europeanaProxy']"));
    addPath(new DataElement("Proxy/edm:year", proxy, "$.['year']"));
    addPath(new DataElement("Proxy/edm:userTag", proxy, "$.['userTags']"));
    addPath(new DataElement("Proxy/ore:proxyIn", proxy, "$.['proxyIn']"));
    addPath(new DataElement("Proxy/ore:proxyFor", proxy, "$.['proxyFor']"));
    addPath(new DataElement("Proxy/dcterms:conformsTo", proxy, "$.['dctermsConformsTo']"));
    addPath(new DataElement("Proxy/dcterms:hasFormat", proxy, "$.['dctermsHasFormat']"));
    addPath(new DataElement("Proxy/dcterms:hasVersion", proxy, "$.['dctermsHasVersion']"));
    addPath(new DataElement("Proxy/dcterms:isFormatOf", proxy, "$.['dctermsIsFormatOf']"));
    addPath(new DataElement("Proxy/dcterms:isReferencedBy", proxy, "$.['dctermsIsReferencedBy']"));
    addPath(new DataElement("Proxy/dcterms:isReplacedBy", proxy, "$.['dctermsIsReplacedBy']"));
    addPath(new DataElement("Proxy/dcterms:isRequiredBy", proxy, "$.['dctermsIsRequiredBy']"));
    addPath(new DataElement("Proxy/dcterms:isVersionOf", proxy, "$.['dctermsIsVersionOf']"));
    addPath(new DataElement("Proxy/dcterms:references", proxy, "$.['dctermsReferences']"));
    addPath(new DataElement("Proxy/dcterms:replaces", proxy, "$.['dctermsReplaces']"));
    addPath(new DataElement("Proxy/dcterms:requires", proxy, "$.['dctermsRequires']"));
    addPath(new DataElement("Proxy/dcterms:tableOfContents", proxy, "$.['dctermsTOC']"));
    addPath(new DataElement("Proxy/edm:currentLocation", proxy, "$.['edmCurrentLocation']"));
    addPath(new DataElement("Proxy/edm:hasMet", proxy, "$.['edmHasMet']"));
    addPath(new DataElement("Proxy/edm:hasType", proxy, "$.['edmHasType']"));
    addPath(new DataElement("Proxy/edm:incorporates", proxy, "$.['edmIncorporates']"));
    addPath(new DataElement("Proxy/edm:isDerivativeOf", proxy, "$.['edmIsDerivativeOf']"));
    addPath(new DataElement("Proxy/edm:isRelatedTo", proxy, "$.['edmIsRelatedTo']"));
    addPath(new DataElement("Proxy/edm:isRepresentationOf", proxy, "$.['edmIsRepresentationOf']"));
    addPath(new DataElement("Proxy/edm:isSimilarTo", proxy, "$.['edmIsSimilarTo']"));
    addPath(new DataElement("Proxy/edm:isSuccessorOf", proxy, "$.['edmIsSuccessorOf']"));
    addPath(new DataElement("Proxy/edm:realizes", proxy, "$.['edmRealizes']"));
    addPath(new DataElement("Proxy/edm:wasPresentAt", proxy, "$.['edmWasPresentAt']"));

    var aggregation = new DataElement("Aggregation", "$.['aggregations'][0]");
    aggregation.setCollection(true);
    addPath(aggregation);
    var aggregationIdentifier = new DataElement("Aggregation/rdf:about", aggregation, ABOUT);
    addPath(aggregationIdentifier);
    aggregation.setIdentifier(aggregationIdentifier);

    addPath(new DataElement("Aggregation/edm:rights", aggregation, "$.['edmRights']")
      .setCategories(MANDATORY, REUSABILITY));
    addPath(new DataElement("Aggregation/edm:provider", aggregation, "$.['edmProvider']")
      .setCategories(MANDATORY, SEARCHABILITY, IDENTIFICATION));
    addPath(new DataElement("Aggregation/edm:dataProvider", aggregation, "$.['edmDataProvider']")
      .setCategories(MANDATORY, SEARCHABILITY, IDENTIFICATION));
    addPath(new DataElement("Aggregation/edm:isShownAt", aggregation, "$.['edmIsShownAt']")
      .setCategories(BROWSING, VIEWING));
    addPath(new DataElement("Aggregation/edm:isShownBy", aggregation, "$.['edmIsShownBy']")
      .setCategories(BROWSING, VIEWING, REUSABILITY));
    addPath(new DataElement("Aggregation/edm:object", aggregation, "$.['edmObject']")
      .setCategories(VIEWING, REUSABILITY));
    addPath(new DataElement("Aggregation/edm:hasView", aggregation, "$.['hasView']")
      .setCategories(BROWSING, VIEWING));
    addPath(new DataElement("Aggregation/dc:rights", aggregation, "$.['dcRights']"));
    addPath(new DataElement("Aggregation/edm:ugc", aggregation, "$.['edmUgc']"));
    addPath(new DataElement("Aggregation/edm:aggregatedCHO", aggregation, "$.['aggregatedCHO']"));
    addPath(new DataElement("Aggregation/edm:intermediateProvider", aggregation, "$.['edmIntermediateProvider']"));

    var place = new DataElement("Place", "$.['places']");
    place.setCollection(true);
    addPath(place);
    var placeIdentifier = new DataElement("Place/rdf:about", place, ABOUT);
    addPath(placeIdentifier);
    place.setIdentifier(placeIdentifier);

    addPath(new DataElement("Place/rdf:about", place, ABOUT));
    addPath(new DataElement("Place/wgs84:lat", place, "$.['latitude']"));
    addPath(new DataElement("Place/wgs84:long", place, "$.['longitude']"));
    addPath(new DataElement("Place/wgs84:alt", place, "$.['altitude']"));
    addPath(new DataElement("Place/dcterms:isPartOf", place, "$.['isPartOf']"));
    addPath(new DataElement("Place/wgs84_pos:lat_long", place, "$.['position']"));
    addPath(new DataElement("Place/dcterms:hasPart", place, "$.['dctermsHasPart']"));
    addPath(new DataElement("Place/owl:sameAs", place, "$.['owlSameAs']"));
    addPath(new DataElement("Place/skos:prefLabel", place, "$.['prefLabel']"));
    addPath(new DataElement("Place/skos:altLabel", place, "$.['altLabel']"));
    addPath(new DataElement("Place/skos:note", place, "$.['note']"));

    var agent = new DataElement("Agent", "$.['agents']");
    agent.setCollection(true);
    addPath(agent);
    var agentIdentifier = new DataElement("Agent/rdf:about", agent, ABOUT);
    addPath(agentIdentifier);
    agent.setIdentifier(agentIdentifier);

    addPath(new DataElement("Agent/edm:begin", agent, "$.['begin']"));
    addPath(new DataElement("Agent/edm:end", agent, "$.['end']"));
    addPath(new DataElement("Agent/edm:hasMet", agent, "$.['edmHasMet']"));
    addPath(new DataElement("Agent/edm:isRelatedTo", agent, "$.['edmIsRelatedTo']"));
    addPath(new DataElement("Agent/owl:sameAs", agent, "$.['owlSameAs']"));
    addPath(new DataElement("Agent/foaf:name", agent, "$.['foafName']"));
    addPath(new DataElement("Agent/dc:date", agent, "$.['dcDate']"));
    addPath(new DataElement("Agent/dc:identifier", agent, "$.['dcIdentifier']"));
    addPath(new DataElement("Agent/rdaGr2:dateOfBirth", agent, "$.['rdaGr2DateOfBirth']"));
    addPath(new DataElement("Agent/rdaGr2:placeOfBirth", agent, "$.['rdaGr2PlaceOfBirth']"));
    addPath(new DataElement("Agent/rdaGr2:dateOfDeath", agent, "$.['rdaGr2DateOfDeath']"));
    addPath(new DataElement("Agent/rdaGr2:placeOfDeath", agent, "$.['rdaGr2PlaceOfDeath']"));
    addPath(new DataElement("Agent/rdaGr2:dateOfEstablishment", agent, "$.['rdaGr2DateOfEstablishment']"));
    addPath(new DataElement("Agent/rdaGr2:dateOfTermination", agent, "$.['rdaGr2DateOfTermination']"));
    addPath(new DataElement("Agent/rdaGr2:gender", agent, "$.['rdaGr2Gender']"));
    addPath(new DataElement("Agent/rdaGr2:professionOrOccupation", agent, "$.['rdaGr2ProfessionOrOccupation']"));
    addPath(new DataElement("Agent/rdaGr2:biographicalInformation", agent, "$.['rdaGr2BiographicalInformation']"));
    addPath(new DataElement("Agent/skos:prefLabel", agent, "$.['prefLabel']"));
    addPath(new DataElement("Agent/skos:altLabel", agent, "$.['altLabel']"));
    addPath(new DataElement("Agent/skos:note", agent, "$.['note']"));

    var timespan = new DataElement("Timespan", "$.['timespans']");
    timespan.setCollection(true);
    addPath(timespan);

    var timespanIdentifier = new DataElement("Timespan/rdf:about", timespan, ABOUT);
    addPath(timespanIdentifier);
    timespan.setIdentifier(timespanIdentifier);

    addPath(new DataElement("Timespan/rdf:about", timespan, ABOUT));
    addPath(new DataElement("Timespan/edm:begin", timespan, "$.['begin']"));
    addPath(new DataElement("Timespan/edm:end", timespan, "$.['end']"));
    addPath(new DataElement("Timespan/dcterms:isPartOf", timespan, "$.['isPartOf']"));
    addPath(new DataElement("Timespan/dcterms:hasPart", timespan, "$.['hasPart']"));
    addPath(new DataElement("Timespan/edm:isNextInSequence", timespan, "$.['edm:isNextInSequence']"));
    addPath(new DataElement("Timespan/owl:sameAs", timespan, "$.['owlSameAs']"));
    addPath(new DataElement("Timespan/skos:prefLabel", timespan, "$.['prefLabel']"));
    addPath(new DataElement("Timespan/skos:altLabel", timespan, "$.['altLabel']"));
    addPath(new DataElement("Timespan/skos:note", timespan, "$.['note']"));

    var concept = new DataElement("Concept", "$.['concepts']");
    concept.setCollection(true);
    addPath(concept);

    var conceptIdentifier = new DataElement("Concept/rdf:about", concept, ABOUT);
    addPath(conceptIdentifier);
    concept.setIdentifier(conceptIdentifier);

    addPath(new DataElement("Concept/rdf:about", concept, ABOUT));
    addPath(new DataElement("Concept/skos:broader", concept, "$.['broader']"));
    addPath(new DataElement("Concept/skos:narrower", concept, "$.['narrower']"));
    addPath(new DataElement("Concept/skos:related", concept, "$.['related']"));
    addPath(new DataElement("Concept/skos:broadMatch", concept, "$.['broadMatch']"));
    addPath(new DataElement("Concept/skos:narrowMatch", concept, "$.['narrowMatch']"));
    addPath(new DataElement("Concept/skos:relatedMatch", concept, "$.['relatedMatch']"));
    addPath(new DataElement("Concept/skos:exactMatch", concept, "$.['exactMatch']"));
    addPath(new DataElement("Concept/skos:closeMatch", concept, "$.['closeMatch']"));
    addPath(new DataElement("Concept/skos:notation", concept, "$.['notation']"));
    addPath(new DataElement("Concept/skos:inScheme", concept, "$.['inScheme']"));
    addPath(new DataElement("Concept/skos:prefLabel", concept, "$.['prefLabel']"));
    addPath(new DataElement("Concept/skos:altLabel", concept, "$.['altLabel']"));
    addPath(new DataElement("Concept/skos:note", concept, "$.['note']"));

    var europeanaAggregation = new DataElement("EuropeanaAggregation", "$.['europeanaAggregation']")
      .setActive(false);
    europeanaAggregation.setCollection(true);
    addPath(europeanaAggregation);
    addPath(new DataElement("EuropeanaAggregation/edm:country", europeanaAggregation, "$.['edmCountry']")
      .setActive(false));
    addPath(new DataElement("EuropeanaAggregation/edm:language", europeanaAggregation, "$.['edmLanguage']")
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
  public List<DataElement> getCollectionPaths() {
    return new ArrayList(collectionPaths.values());
  }

}
