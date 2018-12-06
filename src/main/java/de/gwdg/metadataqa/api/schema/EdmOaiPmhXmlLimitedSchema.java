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
public class EdmOaiPmhXmlLimitedSchema extends EdmSchema implements Serializable {

  private static final List<JsonBranch> PATHS = new ArrayList<>();
  private static final List<FieldGroup> FIELD_GROUPS = new ArrayList<>();
  private static final List<String> NO_LANGUAGE_FIELDS = new ArrayList<>();
  private static final Map<String, String> SOLR_FIELDS = new LinkedHashMap<>();
  private static Map<String, String> extractableFields = new LinkedHashMap<>();
  private static final List<String> EMPTY_STRINGS = new ArrayList<>();
  private static final Map<String, JsonBranch> COLLECTION_PATHS = new LinkedHashMap<>();

  private static final String LONG_SUBJECT_PATH =
    "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:subject']";
  private static final String TITLE_PATH =
    "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:title']";
  private static final String DESCRIPTION_PATH =
    "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:description']";

  static {
    PATHS.add(new JsonBranch("edm:ProvidedCHO/@about",
      "$.['edm:ProvidedCHO'][0]['@about']",
      Category.MANDATORY));
    PATHS.add(new JsonBranch("Proxy/dc:title",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:title']",
      Category.DESCRIPTIVENESS, Category.SEARCHABILITY,
      Category.IDENTIFICATION, Category.MULTILINGUALITY));
    PATHS.add(new JsonBranch("Proxy/dcterms:alternative",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:alternative']",
      Category.DESCRIPTIVENESS, Category.SEARCHABILITY,
      Category.IDENTIFICATION, Category.MULTILINGUALITY));
    PATHS.add(new JsonBranch("Proxy/dc:description",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:description']",
      Category.DESCRIPTIVENESS, Category.SEARCHABILITY,
      Category.CONTEXTUALIZATION, Category.IDENTIFICATION,
      Category.MULTILINGUALITY));
    PATHS.add(new JsonBranch("Proxy/dc:creator",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:creator']",
      Category.DESCRIPTIVENESS, Category.SEARCHABILITY,
      Category.CONTEXTUALIZATION, Category.BROWSING));
    PATHS.add(new JsonBranch("Proxy/dc:publisher",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:publisher']",
      Category.SEARCHABILITY, Category.REUSABILITY));
    PATHS.add(new JsonBranch("Proxy/dc:contributor",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:contributor']",
      Category.SEARCHABILITY));
    PATHS.add(new JsonBranch("Proxy/dc:type",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:type']",
      Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.IDENTIFICATION, Category.BROWSING));
    PATHS.add(new JsonBranch("Proxy/dc:identifier",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:identifier']",
      Category.IDENTIFICATION));
    PATHS.add(new JsonBranch("Proxy/dc:language",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:language']",
      Category.DESCRIPTIVENESS, Category.MULTILINGUALITY));
    PATHS.add(new JsonBranch("Proxy/dc:coverage",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:coverage']",
      Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.BROWSING));
    PATHS.add(new JsonBranch("Proxy/dcterms:temporal",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:temporal']",
      Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.BROWSING));
    PATHS.add(new JsonBranch("Proxy/dcterms:spatial",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:spatial']",
      Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.BROWSING));
    PATHS.add(new JsonBranch("Proxy/dc:subject",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:subject']",
      Category.DESCRIPTIVENESS, Category.SEARCHABILITY,
      Category.CONTEXTUALIZATION, Category.MULTILINGUALITY));
    PATHS.add(new JsonBranch("Proxy/dc:date",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:date']",
      Category.IDENTIFICATION, Category.BROWSING,
      Category.REUSABILITY));
    PATHS.add(new JsonBranch("Proxy/dcterms:created",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:created']",
      Category.IDENTIFICATION, Category.REUSABILITY));
    PATHS.add(new JsonBranch("Proxy/dcterms:issued",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:issued']",
      Category.IDENTIFICATION, Category.REUSABILITY));
    PATHS.add(new JsonBranch("Proxy/dcterms:extent",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:extent']",
      Category.DESCRIPTIVENESS, Category.REUSABILITY));
    PATHS.add(new JsonBranch("Proxy/dcterms:medium",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:medium']",
      Category.DESCRIPTIVENESS, Category.REUSABILITY));
    PATHS.add(new JsonBranch("Proxy/dcterms:provenance",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:provenance']",
      Category.DESCRIPTIVENESS));
    PATHS.add(new JsonBranch("Proxy/dcterms:hasPart",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:hasPart']",
      Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.BROWSING));
    PATHS.add(new JsonBranch("Proxy/dcterms:isPartOf",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:isPartOf']",
      Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.BROWSING));
    PATHS.add(new JsonBranch("Proxy/dc:format",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:format']",
      Category.DESCRIPTIVENESS, Category.REUSABILITY));
    PATHS.add(new JsonBranch("Proxy/dc:source",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:source']",
      Category.DESCRIPTIVENESS));
    PATHS.add(new JsonBranch("Proxy/dc:rights",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:rights']",
      Category.REUSABILITY));
    PATHS.add(new JsonBranch("Proxy/dc:relation",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:relation']",
      Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.BROWSING));
    PATHS.add(new JsonBranch("Proxy/edm:isNextInSequence",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['edm:isNextInSequence']",
      Category.SEARCHABILITY, Category.CONTEXTUALIZATION,
      Category.BROWSING));
    PATHS.add(new JsonBranch("Proxy/edm:type",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['edm:type']",
      Category.SEARCHABILITY, Category.BROWSING));
    /*
    paths.add(new JsonBranch("Proxy/edm:rights",
      "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['edm:rights']",
      Category.MANDATORY, Category.REUSABILITY));
    */
    PATHS.add(new JsonBranch("Aggregation/edm:rights",
      "$.['ore:Aggregation'][0]['edm:rights']",
      Category.MANDATORY, Category.REUSABILITY));
    PATHS.add(new JsonBranch("Aggregation/edm:provider",
      "$.['ore:Aggregation'][0]['edm:provider']",
      Category.MANDATORY, Category.SEARCHABILITY, Category.IDENTIFICATION));
    PATHS.add(new JsonBranch("Aggregation/edm:dataProvider",
      "$.['ore:Aggregation'][0]['edm:dataProvider']",
      Category.MANDATORY, Category.SEARCHABILITY,
      Category.IDENTIFICATION));
    PATHS.add(new JsonBranch("Aggregation/edm:isShownAt",
      "$.['ore:Aggregation'][0]['edm:isShownAt']",
      Category.BROWSING, Category.VIEWING));
    PATHS.add(new JsonBranch("Aggregation/edm:isShownBy",
      "$.['ore:Aggregation'][0]['edm:isShownBy']",
      Category.BROWSING, Category.VIEWING,
      Category.REUSABILITY));
    PATHS.add(new JsonBranch("Aggregation/edm:object",
      "$.['ore:Aggregation'][0]['edm:object']",
      Category.VIEWING, Category.REUSABILITY));
    PATHS.add(new JsonBranch("Aggregation/edm:hasView",
      "$.['ore:Aggregation'][0]['edm:hasView']",
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
    extractableFields.put("dataProvider", "$.['ore:Aggregation'][0]['edm:dataProvider'][0]");

    EMPTY_STRINGS.add("$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:title']");
    EMPTY_STRINGS.add("$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:description']");
    EMPTY_STRINGS.add("$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:subject']");

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
