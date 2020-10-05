package de.gwdg.metadataqa.api.similarity;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Value object representing a row in the file which represents a profile.
 */
public class RecordPattern {

  private List<String> record;
  private String fields;
  private List<String> fieldList;
  private String binary;
  private String id;
  private Integer length;
  private Integer count;
  private Integer numberOfFields;
  private Integer total;
  private Double percent;

  public RecordPattern(BinaryMaker binaryMaker, List<String> rec) {
    record = rec;
    // "id", "fields", "nr-of-fields", "occurence", "percent"

    id = rec.get(0);
    fields = rec.get(1);
    // length = Integer.parseInt(rec.get(1));
    numberOfFields = Integer.parseInt(rec.get(2));
    count = Integer.parseInt(rec.get(3));
    percent = Double.parseDouble(rec.get(4));

    fieldList = Arrays.asList(fields.split(";"));
    binary = binaryMaker.fieldListToBinary(fieldList);
  }

  public String asCsv() {
    return StringUtils.join(record, ",");
  }

  public String getFields() {
    return fields;
  }

  public String getBinary() {
    return binary;
  }

  public List<String> getFieldList() {
    return fieldList;
  }

  public Integer getLength() {
    return length;
  }

  public Integer getCount() {
    return count;
  }

  public Double getPercent() {
    return percent;
  }

  public Integer getTotal() {
    return total;
  }

  public Integer getNumberOfFields() {
    return numberOfFields;
  }

  public String getId() {
    return id;
  }

  @Override
  public String toString() {
    return "RecordPattern{" +
      "record=" + record +
      ", fields='" + fields + '\'' +
      ", fieldList=" + fieldList +
      ", binary='" + binary + '\'' +
      ", id='" + id + '\'' +
      ", length=" + length +
      ", count=" + count +
      ", numberOfFields=" + numberOfFields +
      ", total=" + total +
      ", percent=" + percent +
      '}';
  }
}
