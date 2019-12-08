package de.gwdg.metadataqa.api.schema;

import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.Category;

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
public class EdmFullBeanLimitedSchema extends EdmSchema implements Serializable {

  private static final List<JsonBranch> PATHS = new ArrayList<>();
  private static final List<FieldGroup> FIELD_GROUPS = new ArrayList<>();
  private static final List<String> NO_LANGUAGE_FIELDS = new ArrayList<>();
  private static final Map<String, String> SOLR_FIELDS = new LinkedHashMap<>();
  private static Map<String, String> extractableFields = new LinkedHashMap<>();
  private static final List<String> EMPTY_STRINGS = new ArrayList<>();
  private static final String LONG_SUBJECT_PATH =
    "$.['proxies'][?(@['europeanaProxy'] == false)]['dcSubject']";
  private static final String TITLE_PATH =
    "$.['proxies'][?(@['europeanaProxy'] == false)]['dcTitle']";
  private static final String DESCRIPTION_PATH =
    "$.['proxies'][?(@['europeanaProxy'] == false)]['dcDescription']";

  static {
    PATHS.add(new JsonBranch("edm:ProvidedCHO/@about",
      "$.['providedCHOs'][0]['about']",
      Category.MANDATORY));
    PATHS.add(new JsonBranch("Proxy/dc:title",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcTitle']",
      Category.DESCRIPTIVENESS, Category.SEARCHABILITY,
      Category.IDENTIFICATION, Category.MULTILINGUALITY));
    PATHS.add(new JsonBranch("Proxy/dcterms:alternative",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsAlternative']",
      Category.DESCRIPTIVENESS, Category.SEARCHABILITY,
      Category.IDENTIFICATION, Category.MULTILINGUALITY));
    PATHS.add(new JsonBranch("Proxy/dc:description",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcDescription']",
      Category.DESCRIPTIVENESS, Category.SEARCHABILITY,
      Category.CONTEXTUALIZATION, Category.IDENTIFICATION,
      Category.MULTILINGUALITY));
    PATHS.add(new JsonBranch("Proxy/dc:creator",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcCreator']",
      Category.DESCRIPTIVENESS, Category.SEARCHABILITY,
      Category.CONTEXTUALIZATION, Category.BROWSING));
    PATHS.add(new JsonBranch("Proxy/dc:publisher",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcPublisher']",
      Category.SEARCHABILITY, Category.REUSABILITY));
    PATHS.add(new JsonBranch("Proxy/dc:contributor",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcContributor']",
      Category.SEARCHABILITY));
    PATHS.add(new JsonBranch("Proxy/dc:type",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcType']",
      Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.IDENTIFICATION, Category.BROWSING));
    PATHS.add(new JsonBranch("Proxy/dc:identifier",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcIdentifier']",
      Category.IDENTIFICATION));
    PATHS.add(new JsonBranch("Proxy/dc:language",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcLanguage']",
      Category.DESCRIPTIVENESS, Category.MULTILINGUALITY));
    PATHS.add(new JsonBranch("Proxy/dc:coverage",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcCoverage']",
      Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.BROWSING));
    PATHS.add(new JsonBranch("Proxy/dcterms:temporal",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsTemporal']",
      Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.BROWSING));
    PATHS.add(new JsonBranch("Proxy/dcterms:spatial",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsSpatial']",
      Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.BROWSING));
    PATHS.add(new JsonBranch("Proxy/dc:subject",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcSubject']",
      Category.DESCRIPTIVENESS, Category.SEARCHABILITY,
      Category.CONTEXTUALIZATION, Category.MULTILINGUALITY));
    PATHS.add(new JsonBranch("Proxy/dc:date",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcDate']",
      Category.IDENTIFICATION, Category.BROWSING,
      Category.REUSABILITY));
    PATHS.add(new JsonBranch("Proxy/dcterms:created",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsCreated']",
      Category.IDENTIFICATION, Category.REUSABILITY));
    PATHS.add(new JsonBranch("Proxy/dcterms:issued",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsIssued']",
      Category.IDENTIFICATION, Category.REUSABILITY));
    PATHS.add(new JsonBranch("Proxy/dcterms:extent",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsExtent']",
      Category.DESCRIPTIVENESS, Category.REUSABILITY));
    PATHS.add(new JsonBranch("Proxy/dcterms:medium",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsMedium']",
      Category.DESCRIPTIVENESS, Category.REUSABILITY));
    PATHS.add(new JsonBranch("Proxy/dcterms:provenance",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsProvenance']",
      Category.DESCRIPTIVENESS));
    PATHS.add(new JsonBranch("Proxy/dcterms:hasPart",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsHasPart']",
      Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.BROWSING));
    PATHS.add(new JsonBranch("Proxy/dcterms:isPartOf",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dctermsIsPartOf']",
      Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.BROWSING));
    PATHS.add(new JsonBranch("Proxy/dc:format",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcFormat']",
      Category.DESCRIPTIVENESS, Category.REUSABILITY));
    PATHS.add(new JsonBranch("Proxy/dc:source",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcSource']",
      Category.DESCRIPTIVENESS));
    PATHS.add(new JsonBranch("Proxy/dc:rights",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcRights']",
      Category.REUSABILITY));
    PATHS.add(new JsonBranch("Proxy/dc:relation",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['dcRelation']",
      Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.BROWSING));
    PATHS.add(new JsonBranch("Proxy/edm:isNextInSequence",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['edmIsNextInSequence']",
      Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.BROWSING));
    PATHS.add(new JsonBranch("Proxy/edm:type",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['edmType']",
      Category.SEARCHABILITY, Category.BROWSING));
    /*
    PATHS.add(new JsonBranch("Proxy/edm:rights",
      "$.['proxies'][?(@['europeanaProxy'] == false)]['edm:rights']",
      Category.MANDATORY, Category.REUSABILITY));
    */
    PATHS.add(new JsonBranch("Aggregation/edm:rights",
      "$.['aggregations'][0]['edmRights']",
      Category.MANDATORY, Category.REUSABILITY));
    PATHS.add(new JsonBranch("Aggregation/edm:provider",
      "$.['aggregations'][0]['edmProvider']",
      Category.MANDATORY, Category.SEARCHABILITY,
      Category.IDENTIFICATION));
    PATHS.add(new JsonBranch("Aggregation/edm:dataProvider",
      "$.['aggregations'][0]['edmDataProvider']",
      Category.MANDATORY, Category.SEARCHABILITY,
      Category.IDENTIFICATION));
    PATHS.add(new JsonBranch("Aggregation/edm:isShownAt",
      "$.['aggregations'][0]['edmIsShownAt']",
      Category.BROWSING, Category.VIEWING));
    PATHS.add(new JsonBranch("Aggregation/edm:isShownBy",
      "$.['aggregations'][0]['edmIsShownBy']",
      Category.BROWSING, Category.VIEWING,
      Category.REUSABILITY));
    PATHS.add(new JsonBranch("Aggregation/edm:object",
      "$.['aggregations'][0]['edmObject']",
      Category.VIEWING, Category.REUSABILITY));
    PATHS.add(new JsonBranch("Aggregation/edm:hasView",
      "$.['aggregations'][0]['hasView']",
      Category.BROWSING, Category.VIEWING));

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
      "edm:ProvidedCHO/@about", "Proxy/edm:isNextInSequence",
      "Proxy/edm:type", "Aggregation/edm:isShownAt",
      "Aggregation/edm:isShownBy", "Aggregation/edm:object",
      "Aggregation/edm:hasView"));

    SOLR_FIELDS.put("dc:title", "dc_title_txt");
    SOLR_FIELDS.put("dcterms:alternative", "dcterms_alternative_txt");
    SOLR_FIELDS.put("dc:description", "dc_description_txt");

    extractableFields.put("recordId", "$.identifier");
    extractableFields.put("dataset", "$.sets[0]");
    extractableFields.put("dataProvider", "$.['aggregations'][0]['edmDataProvider'][0]");

    EMPTY_STRINGS.add("$.['proxies'][?(@['europeanaProxy'] == false)]['dcTitle']");
    EMPTY_STRINGS.add("$.['proxies'][?(@['europeanaProxy'] == false)]['dcDescription']");
    EMPTY_STRINGS.add("$.['proxies'][?(@['europeanaProxy'] == false)]['dcSubject']");
  }

  @Override
  public List<JsonBranch> getPaths() {
    return PATHS;
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
    return new ArrayList();
  }

  @Override
  public List<JsonBranch> getRootChildrenPaths() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public JsonBranch getPathByLabel(String label) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
