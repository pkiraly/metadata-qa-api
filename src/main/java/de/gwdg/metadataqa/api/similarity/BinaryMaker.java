package de.gwdg.metadataqa.api.similarity;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BinaryMaker {

  private List<String> canonicalFieldList;

  private static final List<String> MANDATORY_FIELDS = Arrays.asList(
      "dc:title", "dc:description", "dc:type", "dc:coverage",
      "dcterms:spatial", "dc:subject", "edm:rights",
      "Aggregation/edm:rights",
      "Aggregation/edm:provider", "Aggregation/edm:dataProvider",
      "Aggregation/edm:isShownAt", "Aggregation/edm:isShownBy"
  );

  private static final List<String> FUNCTIONAL_FIELDS = Arrays.asList(
      "dc:contributor", "dc:creator", "dc:date", "dc:format",
      "dc:identifier", "dc:language", "dc:publisher", "dc:relation",
      "dc:rights", "dc:source", "dcterms:alternative", "dcterms:created",
      "dcterms:extent", "dcterms:hasPart", "dcterms:isPartOf",
      "dcterms:issued", "dcterms:medium", "dcterms:provenance",
      "dcterms:temporal", "edm:isNextInSequence", "edm:type",
      "Aggregation/edm:hasView", "Aggregation/edm:object"
  );

  public BinaryMaker(List<String> canonicalFieldList) {
    this.canonicalFieldList = canonicalFieldList;
  }

  public String fieldListToBinary(List<String> actual) {
    List<String> binary = new ArrayList<>();
    for (String field : canonicalFieldList) {
      String bit = actual.contains(field) ? "1" : "0";
      if (MANDATORY_FIELDS.contains(field)) {
        bit = bit + bit + bit;
      } else if (FUNCTIONAL_FIELDS.contains(field)) {
        bit = bit + bit;
      }
      binary.add(bit);
    }
    return StringUtils.join(binary, "");
  }


}
