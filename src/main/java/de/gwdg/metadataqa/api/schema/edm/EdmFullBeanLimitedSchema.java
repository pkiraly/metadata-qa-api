package de.gwdg.metadataqa.api.schema.edm;

import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.DataElement;
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

/**
 * The Europeana Data Model (EDM) representation of the metadata schema interface.
 * This class represents what fields will be analyzed in different measurements.
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class EdmFullBeanLimitedSchema extends EdmSchema implements Serializable {

  private static final long serialVersionUID = 5248200128650498403L;

  public EdmFullBeanLimitedSchema() {
    initialize();
  }

  private void initialize() {
    longSubjectPath = "$.['proxies'][?(@['europeanaProxy'] == false)]['dcSubject']";
    titlePath = "$.['proxies'][?(@['europeanaProxy'] == false)]['dcTitle']";
    descriptionPath = "$.['proxies'][?(@['europeanaProxy'] == false)]['dcDescription']";

    addPath(new DataElement("edm:ProvidedCHO/@about",
      "$.['providedCHOs'][0]['about']")
      .setCategories(MANDATORY));
    addPath(new DataElement("Proxy/dc:title", titlePath)
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, IDENTIFICATION, MULTILINGUALITY)
      .setIndexField("dc_title_txt"));
    addPath(new DataElement("Proxy/dcterms:alternative",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsAlternative']")
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, IDENTIFICATION, MULTILINGUALITY)
      .setIndexField("dcterms_alternative_txt"));
    addPath(new DataElement("Proxy/dc:description", descriptionPath)
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, CONTEXTUALIZATION, IDENTIFICATION, MULTILINGUALITY)
      .setIndexField("dc_description_txt"));
    addPath(new DataElement("Proxy/dc:creator",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcCreator']")
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/dc:publisher",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcPublisher']")
      .setCategories(SEARCHABILITY, REUSABILITY));
    addPath(new DataElement("Proxy/dc:contributor",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcContributor']")
      .setCategories(SEARCHABILITY));
    addPath(new DataElement("Proxy/dc:type",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcType']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, IDENTIFICATION, BROWSING));
    addPath(new DataElement("Proxy/dc:identifier",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcIdentifier']")
      .setCategories(IDENTIFICATION));
    addPath(new DataElement("Proxy/dc:language",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcLanguage']")
      .setCategories(DESCRIPTIVENESS, MULTILINGUALITY));
    addPath(new DataElement("Proxy/dc:coverage",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcCoverage']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/dcterms:temporal",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsTemporal']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/dcterms:spatial",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsSpatial']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/dc:subject", longSubjectPath)
      .setCategories(DESCRIPTIVENESS, SEARCHABILITY, CONTEXTUALIZATION, MULTILINGUALITY));
    addPath(new DataElement("Proxy/dc:date",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcDate']")
      .setCategories(IDENTIFICATION, BROWSING, REUSABILITY));
    addPath(new DataElement("Proxy/dcterms:created",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsCreated']")
      .setCategories(IDENTIFICATION, REUSABILITY));
    addPath(new DataElement("Proxy/dcterms:issued",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsIssued']")
      .setCategories(IDENTIFICATION, REUSABILITY));
    addPath(new DataElement("Proxy/dcterms:extent",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsExtent']")
      .setCategories(DESCRIPTIVENESS, REUSABILITY));
    addPath(new DataElement("Proxy/dcterms:medium",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsMedium']")
      .setCategories(DESCRIPTIVENESS, REUSABILITY));
    addPath(new DataElement("Proxy/dcterms:provenance",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsProvenance']")
      .setCategories(DESCRIPTIVENESS));
    addPath(new DataElement("Proxy/dcterms:hasPart",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsHasPart']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/dcterms:isPartOf",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsIsPartOf']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/dc:format",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcFormat']")
      .setCategories(DESCRIPTIVENESS, REUSABILITY));
    addPath(new DataElement("Proxy/dc:source",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcSource']")
      .setCategories(DESCRIPTIVENESS));
    addPath(new DataElement("Proxy/dc:rights",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcRights']")
      .setCategories(REUSABILITY));
    addPath(new DataElement("Proxy/dc:relation",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcRelation']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/edm:isNextInSequence",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['edmIsNextInSequence']")
      .setCategories(SEARCHABILITY, CONTEXTUALIZATION, BROWSING));
    addPath(new DataElement("Proxy/edm:type",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['edmType']")
      .setCategories(SEARCHABILITY, BROWSING));
  /*
  addPath(new DataElement("Proxy/edm:rights",
    "$.['proxies'][?(@['europeanaProxy'] == false)]['edm:rights']",
    Category.MANDATORY, Category.REUSABILITY));
  */
    addPath(new DataElement("Aggregation/edm:rights", "$.['aggregations'][0]['edmRights']")
      .setCategories(MANDATORY, REUSABILITY));
    addPath(new DataElement("Aggregation/edm:provider", "$.['aggregations'][0]['edmProvider']")
      .setCategories(MANDATORY, SEARCHABILITY, IDENTIFICATION));
    addPath(new DataElement("Aggregation/edm:dataProvider","$.['aggregations'][0]['edmDataProvider']")
      .setCategories(MANDATORY, SEARCHABILITY, IDENTIFICATION)
      .setExtractable());
    addPath(new DataElement("Aggregation/edm:isShownAt","$.['aggregations'][0]['edmIsShownAt']")
      .setCategories(BROWSING, Category.VIEWING));
    addPath(new DataElement("Aggregation/edm:isShownBy","$.['aggregations'][0]['edmIsShownBy']")
      .setCategories(BROWSING, Category.VIEWING, REUSABILITY));
    addPath(new DataElement("Aggregation/edm:object","$.['aggregations'][0]['edmObject']")
      .setCategories(Category.VIEWING, REUSABILITY));
    addPath(new DataElement("Aggregation/edm:hasView", "$.['aggregations'][0]['hasView']")
      .setCategories(BROWSING, Category.VIEWING));

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
      "edm:ProvidedCHO/@about", "Proxy/edm:isNextInSequence",
      "Proxy/edm:type", "Aggregation/edm:isShownAt",
      "Aggregation/edm:isShownBy", "Aggregation/edm:object",
      "Aggregation/edm:hasView"));

    extractableFields.put("recordId", "$.identifier");
    extractableFields.put("dataset", "$.sets[0]");
    extractableFields.put("dataProvider", "$.['aggregations'][0]['edmDataProvider'][0]");

    emptyStrings.add(titlePath);
    emptyStrings.add(descriptionPath);
    emptyStrings.add(longSubjectPath);
  }

  @Override
  public Format getFormat() {
    return Format.JSON;
  }

  @Override
  public List<DataElement> getCollectionPaths() {
    return new ArrayList<>();
  }
}
