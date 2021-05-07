package de.gwdg.metadataqa.api.schema.edm;

import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.Category;
import de.gwdg.metadataqa.api.schema.Format;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    addPath(new JsonBranch("edm:ProvidedCHO/@about",
      "$.['providedCHOs'][0]['about']")
      .setCategories(Category.MANDATORY));
    addPath(new JsonBranch("Proxy/dc:title", titlePath)
      .setCategories(Category.DESCRIPTIVENESS, Category.SEARCHABILITY,
        Category.IDENTIFICATION, Category.MULTILINGUALITY));
    addPath(new JsonBranch("Proxy/dcterms:alternative",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsAlternative']")
      .setCategories(Category.DESCRIPTIVENESS, Category.SEARCHABILITY,
        Category.IDENTIFICATION, Category.MULTILINGUALITY));
    addPath(new JsonBranch("Proxy/dc:description", descriptionPath)
      .setCategories(Category.DESCRIPTIVENESS, Category.SEARCHABILITY,
        Category.CONTEXTUALIZATION, Category.IDENTIFICATION,
        Category.MULTILINGUALITY));
    addPath(new JsonBranch("Proxy/dc:creator",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcCreator']")
      .setCategories(Category.DESCRIPTIVENESS, Category.SEARCHABILITY,
        Category.CONTEXTUALIZATION, Category.BROWSING));
    addPath(new JsonBranch("Proxy/dc:publisher",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcPublisher']")
      .setCategories(Category.SEARCHABILITY, Category.REUSABILITY));
    addPath(new JsonBranch("Proxy/dc:contributor",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcContributor']")
      .setCategories(Category.SEARCHABILITY));
    addPath(new JsonBranch("Proxy/dc:type",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcType']")
      .setCategories(Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
        Category.IDENTIFICATION, Category.BROWSING));
    addPath(new JsonBranch("Proxy/dc:identifier",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcIdentifier']")
      .setCategories(Category.IDENTIFICATION));
    addPath(new JsonBranch("Proxy/dc:language",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcLanguage']")
      .setCategories(Category.DESCRIPTIVENESS, Category.MULTILINGUALITY));
    addPath(new JsonBranch("Proxy/dc:coverage",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcCoverage']")
      .setCategories(Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
        Category.BROWSING));
    addPath(new JsonBranch("Proxy/dcterms:temporal",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsTemporal']")
      .setCategories(Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
        Category.BROWSING));
    addPath(new JsonBranch("Proxy/dcterms:spatial",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsSpatial']")
      .setCategories(Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
        Category.BROWSING));
    addPath(new JsonBranch("Proxy/dc:subject",
      longSubjectPath)
      .setCategories(Category.DESCRIPTIVENESS, Category.SEARCHABILITY,
        Category.CONTEXTUALIZATION, Category.MULTILINGUALITY));
    addPath(new JsonBranch("Proxy/dc:date",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcDate']")
      .setCategories(Category.IDENTIFICATION, Category.BROWSING,
        Category.REUSABILITY));
    addPath(new JsonBranch("Proxy/dcterms:created",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsCreated']")
      .setCategories(Category.IDENTIFICATION, Category.REUSABILITY));
    addPath(new JsonBranch("Proxy/dcterms:issued",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsIssued']")
      .setCategories(Category.IDENTIFICATION, Category.REUSABILITY));
    addPath(new JsonBranch("Proxy/dcterms:extent",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsExtent']")
      .setCategories(Category.DESCRIPTIVENESS, Category.REUSABILITY));
    addPath(new JsonBranch("Proxy/dcterms:medium",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsMedium']")
      .setCategories(Category.DESCRIPTIVENESS, Category.REUSABILITY));
    addPath(new JsonBranch("Proxy/dcterms:provenance",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsProvenance']")
      .setCategories(Category.DESCRIPTIVENESS));
    addPath(new JsonBranch("Proxy/dcterms:hasPart",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsHasPart']")
      .setCategories(Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
        Category.BROWSING));
    addPath(new JsonBranch("Proxy/dcterms:isPartOf",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsIsPartOf']")
      .setCategories(Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
        Category.BROWSING));
    addPath(new JsonBranch("Proxy/dc:format",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcFormat']")
      .setCategories(Category.DESCRIPTIVENESS, Category.REUSABILITY));
    addPath(new JsonBranch("Proxy/dc:source",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcSource']")
      .setCategories(Category.DESCRIPTIVENESS));
    addPath(new JsonBranch("Proxy/dc:rights",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcRights']")
      .setCategories(Category.REUSABILITY));
    addPath(new JsonBranch("Proxy/dc:relation",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcRelation']")
      .setCategories(Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
        Category.BROWSING));
    addPath(new JsonBranch("Proxy/edm:isNextInSequence",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['edmIsNextInSequence']")
      .setCategories(Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
        Category.BROWSING));
    addPath(new JsonBranch("Proxy/edm:type",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['edmType']")
      .setCategories(Category.SEARCHABILITY, Category.BROWSING));
  /*
  addPath(new JsonBranch("Proxy/edm:rights",
    "$.['proxies'][?(@['europeanaProxy'] == false)]['edm:rights']",
    Category.MANDATORY, Category.REUSABILITY));
  */
    addPath(new JsonBranch("Aggregation/edm:rights",
      "$.['aggregations'][0]['edmRights']")
      .setCategories(Category.MANDATORY, Category.REUSABILITY));
    addPath(new JsonBranch("Aggregation/edm:provider",
      "$.['aggregations'][0]['edmProvider']")
      .setCategories(Category.MANDATORY, Category.SEARCHABILITY,
        Category.IDENTIFICATION));
    addPath(new JsonBranch("Aggregation/edm:dataProvider",
      "$.['aggregations'][0]['edmDataProvider']")
      .setCategories(Category.MANDATORY, Category.SEARCHABILITY,
        Category.IDENTIFICATION));
    addPath(new JsonBranch("Aggregation/edm:isShownAt",
      "$.['aggregations'][0]['edmIsShownAt']")
      .setCategories(Category.BROWSING, Category.VIEWING));
    addPath(new JsonBranch("Aggregation/edm:isShownBy",
      "$.['aggregations'][0]['edmIsShownBy']")
      .setCategories(Category.BROWSING, Category.VIEWING,
        Category.REUSABILITY));
    addPath(new JsonBranch("Aggregation/edm:object",
      "$.['aggregations'][0]['edmObject']")
      .setCategories(Category.VIEWING, Category.REUSABILITY));
    addPath(new JsonBranch("Aggregation/edm:hasView",
      "$.['aggregations'][0]['hasView']")
      .setCategories(Category.BROWSING, Category.VIEWING));

    fieldGroups.add(
      new FieldGroup(
        Category.MANDATORY,
        "Proxy/dc:title", "Proxy/dc:description"));
    fieldGroups.add(
      new FieldGroup(
        Category.MANDATORY,
        "Proxy/dc:type", "Proxy/dc:subject", "Proxy/dc:coverage",
        "Proxy/dcterms:temporal", "Proxy/dcterms:spatial"));
    fieldGroups.add(
      new FieldGroup(
        Category.MANDATORY,
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
  public List<JsonBranch> getCollectionPaths() {
    return new ArrayList<>();
  }
}
