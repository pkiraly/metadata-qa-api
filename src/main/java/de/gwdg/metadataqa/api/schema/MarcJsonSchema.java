package de.gwdg.metadataqa.api.schema;

import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.Category;
import de.gwdg.metadataqa.api.rule.RuleChecker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON based MARC21 schema
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class MarcJsonSchema implements Schema, ProblemCatalogSchema, Serializable {

  private static final long serialVersionUID = -351328095358861985L;
  private static final Map<String, DataElement> PATHS = new LinkedHashMap<>();
  private static final Map<String, DataElement> COLLECTION_PATHS = new LinkedHashMap<>();
  private static final Map<String, DataElement> DIRECT_CHILDREN = new LinkedHashMap<>();
  public static final String NOT_SUPPORTED_YET = "Not supported yet.";
  private static Map<String, String> extractableFields = new LinkedHashMap<>();
  private static List<String> categories = null;
  private static List<RuleChecker> ruleCheckers = null;

  public static final String DATAFIELD_PATTERN = "$.datafield[?(@.tag == '%s')].subfield[?(@.code == '%s')].content";
  public static final String DATAFIELD_PARENT_PATTERN = "$.datafield[?(@.tag == '%s')]";
  public static final String DATAFIELD_IND1_PATTERN = "$.ind1";
  public static final String DATAFIELD_IND2_PATTERN = "$.ind2";
  public static final String DATAFIELD_CHILDREN_PATTERN = "$.subfield[?(@.code == '%s')].content";

  static {
    addPath(new DataElement("leader", "$.leader"));
    addPath(new DataElement("001", "$.controlfield[?(@.tag == '001')].content"));
    addPath(new DataElement("003", "$.controlfield[?(@.tag == '003')].content"));
    addPath(new DataElement("005", "$.controlfield[?(@.tag == '005')].content"));
    addPath(new DataElement("006", "$.controlfield[?(@.tag == '006')].content"));
    addPath(new DataElement("007", "$.controlfield[?(@.tag == '007')].content"));
    addPath(new DataElement("008", "$.controlfield[?(@.tag == '008')].content"));

    registerDatafield("010", "a", "b", "z", "8", "z");
    registerDatafield("013", "a", "c");
    registerDatafield("015", "2", "a", "z");
    registerDatafield("016", "2", "a");
    registerDatafield("017", "a", "b");
    registerDatafield("018", "a");
    registerDatafield("019", "a"); // ??? not in the standard!!!
    registerDatafield("020", "6", "9", "a", "b", "c", "z");
    registerDatafield("022", "a", "c", "y", "z");
    registerDatafield("024", "2", "9", "a", "c", "d", "z");
    registerDatafield("025", "a");
    registerDatafield("026", "a", "e");
    registerDatafield("027", "a");
    registerDatafield("028", "a", "b");
    registerDatafield("029", "a", "b", "c"); // ??? not in the standard!
    registerDatafield("030", "a", "z");
    // 31
    registerDatafield("032", "a", "b");
    registerDatafield("033", "a", "b", "c");
    registerDatafield("034", "a", "b", "c", "d", "e", "f", "g");
    registerDatafield("035", "6", "a", "z");
    registerDatafield("036", "a");
    registerDatafield("037", "a", "b", "c", "f", "n");
    registerDatafield("038", "a");
    registerDatafield("040", "6", "a", "b", "c", "d", "e", "h", "t", "w");
    registerDatafield("041", "2", "a", "b", "d", "e", "f", "g", "h", "j", "k", "m", "n");
    registerDatafield("042", "a");
    registerDatafield("043", "2", "a", "b", "c");
    registerDatafield("044", "a", "h");
    registerDatafield("045", "a", "b");
    registerDatafield("046", "2", "a", "b", "d", "j", "k");
    registerDatafield("047", "a");
    registerDatafield("048", "a", "b");
    registerDatafield("049", "a"); // ???
    registerDatafield("050", "3", "a", "b", "i");
    registerDatafield("051", "a", "b", "c", "i");
    registerDatafield("052", "a", "b");
    registerDatafield("055", "2", "a", "b");
    registerDatafield("060", "b");
    registerDatafield("061", "a");
    registerDatafield("066", "a", "c");
    registerDatafield("070", "a", "b");
    // 71
    registerDatafield("072", "2", "a", "x");
    registerDatafield("074", "a", "z");
    registerDatafield("080", "2", "a", "b", "x");
    registerDatafield("082", "2", "9", "a", "b", "m", "n", "q");
    registerDatafield("083", "2", "a");
    registerDatafield("084", "a", "b");
    registerDatafield("085", "8", "a", "b", "s", "z");
    registerDatafield("086", "2", "6", "a", "z");
    registerDatafield("088", "a");
    registerDatafield("090", "a", "b", "v");

    registerDatafield("100", "0", "4", "6", "a", "b", "c", "d", "e", "f", "g", "h", "k", "l", "n", "p", "q", "t", "u");
    registerDatafield("110", "0", "4", "6", "9", "a", "b", "c", "e", "f", "k", "l", "n", "p", "s", "t", "u");
    registerDatafield("111", "0", "4", "6", "9", "a", "b", "c", "d", "e", "f", "g", "j", "k", "l", "n", "q", "p", "t");
    registerDatafield("130", "0", "6", "7", "9", "a", "d", "f", "g", "h", "k", "l", "m", "n", "o", "p", "r", "s", "t", "x", "v");

    registerDatafield("199", "9"); // ??

    registerDatafield("210", "a");
    registerDatafield("222", "a", "b");
    registerDatafield("240", "0", "6", "7", "9", "a", "d", "f", "g", "h", "k", "l", "m", "n", "o", "p", "r", "s", "t", "x");
    registerDatafield("241", "a"); // ??
    registerDatafield("242", "a", "b", "c", "y");
    registerDatafield("243", "a", "f", "k", "l");
    registerDatafield("245", "6", "a", "b", "c", "d", "e", "f", "g", "h", "k", "n", "p", "s", "v");
    registerDatafield("246", "5", "6", "a", "b", "d", "f", "h", "i", "n", "p");
    registerDatafield("247", "a", "b", "f", "g", "p");
    registerDatafield("249", "a", "b", "c", "v"); // ????
    registerDatafield("250", "6", "9", "a", "c");
    registerDatafield("254", "a");
    registerDatafield("255", "a", "b", "c", "e");
    // 256
    registerDatafield("257", "a");
    registerDatafield("258", "a");
    registerDatafield("260", "3", "6", "a", "b", "c", "d", "e", "f", "g", "z");
    // 263
    registerDatafield("264", "3", "a", "b", "c");
    registerDatafield("265", "a"); // ????
    registerDatafield("270", "a", "b", "c", "d", "e", "g", "h", "k", "l", "m", "n");
    registerDatafield("290", "a", "b"); // ????

    registerDatafield("300", "2", "3", "6", "a", "b", "c", "e", "f", "g", "x"); // " " <- ?????
    registerDatafield("306", "a");
    registerDatafield("307", "a");
    registerDatafield("310", "a", "b");
    // 321
    registerDatafield("336", "2", "3", "8", "a", "b");
    registerDatafield("337", "2", "3", "8", "a", "b");
    registerDatafield("338", "2", "3", "8", "a", "b");
    registerDatafield("340", "a");
    // 342
    // 343
    registerDatafield("344", "2", "a", "b", "g", "h");
    // 345
    registerDatafield("346", "2", "b");
    registerDatafield("347", "2", "3", "a", "b", "c", "d");
    // 348
    registerDatafield("350", "a"); // ???
    registerDatafield("351", "3", "a", "b");
    // 352
    // 355
    // 357
    registerDatafield("362", "a", "z");
    registerDatafield("363", "a", "b", "i", "j", "k", "u");
    registerDatafield("365", "2", "a");
    registerDatafield("366", "2", "a", "b", "c", "j", "k", "m");
    // 370
    registerDatafield("377", "2", "a");
    registerDatafield("380", "0", "2", "a");
    // 381
    registerDatafield("382", "0", "2", "a", "d", "g", "n", "p", "s", "v");
    registerDatafield("383", "a", "b", "c");
    registerDatafield("384", "a", "b");
    registerDatafield("385", "0", "a", "g");
    // 386
    // 388

    registerDatafield("400", "6", "a", "b", "c", "d", "e", "f", "l", "n", "p", "q", "t", "v", "x"); // ???
    registerDatafield("410", "6", "a", "b", "c", "d", "f", "g", "k", "n", "p", "t", "u", "x", "v"); // ???
    registerDatafield("411", "a", "c", "d", "e", "g", "n", "p", "q", "t", "v"); // ???
    registerDatafield("440", "a", "n", "p", "v", "x"); // ???
    registerDatafield("489", "a", "v"); // ???
    registerDatafield("490", "2", "3", "6", "a", "l", "n", "p", "x", "v");

    registerDatafield("500", "2", "3", "5", "6", "a", "g", "x");
    registerDatafield("501", "5", "6", "a");
    registerDatafield("502", "6", "a", "b", "c", "d", "6");
    registerDatafield("504", "6", "b");
    registerDatafield("505", "a", "g", "r", "t", "u");
    registerDatafield("506", "3", "5", "a", "c", "u");
    registerDatafield("507", "a");
    registerDatafield("508", "6", "a");
    registerDatafield("510", "3", "6", "b", "x");
    registerDatafield("511", "a");
    registerDatafield("513", "a", "b");
    registerDatafield("514", "a");
    registerDatafield("515", "a");
    registerDatafield("516", "a");
    // 517
    registerDatafield("518", "a");
    registerDatafield("520", "2", "3", "6", "a", "b", "c", "u");
    registerDatafield("521", "6", "a", "b");
    registerDatafield("522", "a");
    registerDatafield("524", "a");
    registerDatafield("525", "6", "a");
    registerDatafield("526", "a", "b", "c", "d", "z");
    registerDatafield("530", "3", "6", "a", "b", "c", "d", "u");
    registerDatafield("533", "3", "5", "6", "7", "a", "b", "c", "d", "e", "f", "m", "n", "x");
    registerDatafield("534", "6", "c", "e", "f", "k", "l", "p", "t", "z");
    registerDatafield("535", "3", "a", "b", "c", "g", "q");
    registerDatafield("536", "6", "a", "b", "c", "d", "e", "f", "g", "h");
    registerDatafield("538", "6", "a", "u");
    registerDatafield("540", "3", "5", "a", "b", "c", "u");
    registerDatafield("541", "3", "5", "a", "b", "c", "d", "e", "f", "h", "n", "o");
    registerDatafield("542", "a", "c", "d", "f", "g");
    registerDatafield("544", "3", "a", "b", "d", "e", "n");
    registerDatafield("545", "a", "b");
    registerDatafield("546", "3", "6", "a", "b");
    // 547
    registerDatafield("550", "a");
    registerDatafield("552", "a");
    registerDatafield("555", "a", "c", "u");
    registerDatafield("556", "a");
    registerDatafield("561", "3", "5", "6", "a");
    registerDatafield("562", "5", "a", "b", "c");
    registerDatafield("563", "5", "a");
    registerDatafield("565", "a");
    // 567
    registerDatafield("580", "a");
    registerDatafield("581", "3", "8", "a");
    registerDatafield("583", "3", "5", "a", "b", "c", "d", "e", "f", "h", "i", "j", "l", "u", "x");
    registerDatafield("584", "a");
    registerDatafield("585", "3", "5", "6", "a");
    registerDatafield("586", "a");
    registerDatafield("588", "5", "a");
    registerDatafield("589", "c", "d"); // ???
    registerDatafield("590", "a");
    registerDatafield("591", "2", "a", "x");
    registerDatafield("593", "a");

    registerDatafield("600", "0", "1", "2", "4", "6", "9", "a", "b", "c", "d", "f", "h", "m", "n", "o", "p", "r", "s", "t", "x", "v");
    registerDatafield("610", "0", "2", "3", "6", "9", "a", "b", "f", "g", "h", "n", "o", "t", "u", "x", "v");
    registerDatafield("611", "0", "2", "3", "6", "9", "a", "b", "c", "d", "f", "g", "k", "l", "n", "p", "t");
    registerDatafield("630", "0", "2", "3", "6", "9", "a", "f", "n", "o", "p", "t", "x");
    // 647
    registerDatafield("648", "2", "a", "x", "v");
    registerDatafield("650", "0", "2", "3", "6", "9", "a", "e", "k", "q", "x", "y", "z");
    registerDatafield("651", "0", "2", "3", "4", "6", "9", "a", "e", "k", "n", "t", "x", "v", "z");
    registerDatafield("653", "6", "a");
    registerDatafield("654", "2", "a", "b", "c");
    registerDatafield("655", "0", "2", "3", "a", "b", "g", "x", "y", "z");
    registerDatafield("656", "2", "a", "v", "x", "z");
    registerDatafield("657", "2", "a", "y");
    // 658
    registerDatafield("662", "2", "a", "d");
    registerDatafield("689", "0", "2", "5", "9", "A", "a", "b", "c", "D", "d", "f", "g", "m", "n", "p", "r", "s", "t", "x", "z"); // ????
    // 69X

    // 70X-75X: Added Entry Fields
    registerDatafield("700", "0", "3", "4", "5", "6", "a", "b", "c", "d", "e", "f", "g", "h", "i", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "T", "u", "U", "x");
    registerDatafield("710", "0", "3", "4", "5", "6", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "x", "v");
    registerDatafield("711", "0", "4", "6", "9", "a", "b", "c", "d", "e", "f", "g", "i", "j", "k", "l", "n", "p", "q", "s", "t");
    registerDatafield("720", "4", "6", "a", "e");
    registerDatafield("730", "0", "5", "6", "a", "d", "f", "g", "h", "i", "k", "l", "m", "n", "o", "p", "r", "s", "t", "T", "U", "x");
    registerDatafield("740", "5", "6", "a", "h", "n", "p", "v");
    registerDatafield("751", "4", "6", "a");
    registerDatafield("752", "a", "b", "c", "d", "f");
    registerDatafield("753", "a", "c");
    // 754

    // 76X-78X: Linking Entry Fields
    registerDatafield("760", "a", "d", "g", "t", "x", "y", "w");
    // 762
    registerDatafield("765", "6", "a", "b", "c", "d", "h", "i", "k", "o", "t", "w", "x", "z");
    registerDatafield("767", "a", "d", "i", "s", "t", "w", "z");
    registerDatafield("770", "6", "a", "b", "c", "d", "h", "i", "k", "n", "o", "s", "t", "w", "x", "z");
    registerDatafield("772", "7", "a", "b", "c", "g", "h", "i", "k", "n", "s", "t", "w", "x", "z");
    registerDatafield("773", "3", "6", "7", "a", "b", "d", "g", "h", "i", "n", "o", "q", "s", "t", "x", "w");
    registerDatafield("774", "a", "b", "c", "d", "g", "i", "s", "t", "w", "z");
    registerDatafield("775", "0", "6", "a", "b", "c", "d", "e", "f", "g", "h", "i", "k", "n", "o", "s", "t", "u", "w", "z");
    registerDatafield("776", "0", "4", "6", "9", "a", "b", "c", "d", "g", "h", "i", "k", "m", "n", "o", "q", "s", "t", "u", "x", "w", "z");
    registerDatafield("777", "6", "7", "a", "c", "h", "t", "w");
    registerDatafield("780", "0", "6", "a", "b", "c", "d", "g", "i", "k", "n", "s", "t", "x", "w", "z");
    registerDatafield("785", "0", "6", "a", "b", "c", "d", "i", "n", "x", "t", "x", "w", "z");
    // 786
    registerDatafield("787", "0", "6", "7", "a", "b", "d", "g", "h", "i", "n", "o", "r", "s", "t", "w");

    // 80X-83X: Series Added Entry Fields
    registerDatafield("800", "2", "4", "6", "a", "b", "c", "d", "e", "f", "g", "k", "l", "p", "q", "s", "t", "v", "w");
    registerDatafield("810", "4", "6", "a", "b", "c", "d", "f", "g", "k", "l", "p", "q", "r", "s", "t", "v", "w", "x");
    registerDatafield("811", "6", "a", "b", "c", "d", "e", "f", "g", "k", "l", "n", "p", "q", "t", "v", "w");
    registerDatafield("830", "3", "6", "a", "d", "f", "g", "h", "k", "l", "m", "o", "p", "q", "s", "t", "x", "v", "w");

    registerDatafield("840", "a", "v"); // ??

    // 841-88X: Holdings, Location, Alternate Graphics, etc. Fields
    // 841
    // 842
    // 843
    // 844
    // 845
    registerDatafield("850", "a");
    registerDatafield("852", "2", "3", "a", "b", "c", "e", "h", "i", "j", "k", "m", "n", "p", "t", "u", "x", "w", "z");
    // 853
    // 854
    // 855
    registerDatafield("856", "2", "3", "a", "b", "c", "d", "f", "h", "i", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "u", "x", "y", "v", "w", "z");
    // 863
    // 864
    registerDatafield("865", "a");
    registerDatafield("866", "a");
    // 867
    // 868
    // 876
    // 877
    // 878
    registerDatafield("880", "0", "1", "2", "3", "5", "6", "8", "a", "b", "c", "d", "f", "g", "i", "k", "l", "o", "q", "r", "s", "t", "T", "U", "x", "y", "v", "z");
    // 882
    // 883
    // 884
    // 885
    registerDatafield("886", "2", "a", "b", "c", "d", "e", "f", "h", "k", "x", "z");
    registerDatafield("887", "2", "a");
    registerDatafield("889", "w"); // ???

    registerDatafield("911", "a", "b", "9"); // OCLC
    registerDatafield("912", "a", "b", "9"); // OCLC
    registerDatafield("924", "9", "a", "b", "c", "d", "e", "g", "h", "i", "j", "k", "l", "m", "n", "q", "r", "s", "v", "w", "x", "y", "z");
    registerDatafield("935", "a", "b", "c", "d", "e", "m");
    registerDatafield("936", "0", "a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "m", "q", "y");
    registerDatafield("937", "a", "b", "c", "d", "e", "f");
    registerDatafield("938", "a", "b", "n");
    registerDatafield("987", "a", "b", "c", "d", "e", "f"); // ???
    registerDatafield("994", "a", "b");

    extractableFields.put("leader", "$.leader");
    extractableFields.put("recordId", "$.controlfield[?(@.tag == '001')].content");
    extractableFields.put("001", PATHS.get("001").getPath());
    extractableFields.put("007", "$.controlfield[?(@.tag == '007')].content");
    extractableFields.put("008", "$.controlfield[?(@.tag == '008')].content");
    extractableFields.put("020$a", createDatafieldPath("020", "a"));
    extractableFields.put("028$a", createDatafieldPath("028", "a"));
    extractableFields.put("035$a", createDatafieldPath("035", "a"));
    extractableFields.put("100$a", createDatafieldPath("100", "a"));
    extractableFields.put("110$a", createDatafieldPath("110", "a"));
    extractableFields.put("245$a", createDatafieldPath("245", "a"));
    extractableFields.put("245$n", createDatafieldPath("245", "n"));
    extractableFields.put("245$p", createDatafieldPath("245", "p"));
    extractableFields.put("254$a", createDatafieldPath("254", "a"));
    extractableFields.put("260$a", createDatafieldPath("260", "a"));
    extractableFields.put("260$b", createDatafieldPath("260", "b"));
    extractableFields.put("260$c", createDatafieldPath("260", "c"));
    extractableFields.put("300$a", createDatafieldPath("300", "a"));
    extractableFields.put("490$v", createDatafieldPath("490", "v"));
    extractableFields.put("700$a", createDatafieldPath("700", "a"));
    extractableFields.put("710$a", createDatafieldPath("710", "a"));
    extractableFields.put("773$g", createDatafieldPath("773", "g"));
    extractableFields.put("773$v", createDatafieldPath("773", "v"));

    extractableFields.put("029$a", createDatafieldPath("029", "a"));
    extractableFields.put("029$b", createDatafieldPath("029", "b"));
    extractableFields.put("040$a", createDatafieldPath("040", "a"));
    extractableFields.put("040$b", createDatafieldPath("040", "b"));
    extractableFields.put("040$c", createDatafieldPath("040", "c"));
    extractableFields.put("040$d", createDatafieldPath("040", "d"));
    extractableFields.put("650$a", createDatafieldPath("650", "a"));
    extractableFields.put("650$2", createDatafieldPath("650", "2"));

    extractableFields.put("911$9", createDatafieldPath("911", "9"));
    extractableFields.put("912$9", createDatafieldPath("911", "9"));
  }

  private static void registerDatafield(String tag, String... codes) {
    var parent = new DataElement(tag, createDatafieldParentPath(tag));
    parent.setCollection(true);
    addPath(parent);
    addPath(new DataElement(tag + "$ind1", parent, DATAFIELD_IND1_PATTERN));
    addPath(new DataElement(tag + "$ind2", parent, DATAFIELD_IND2_PATTERN));
    for (String code : codes) {
      addPath(new DataElement(tag + "$" + code, parent, createDatafieldChildrenPath(code)));
    }
  }

  private static DataElement createDatafieldBranch(String tag, String code) {
    return new DataElement(tag + "$" + code, createDatafieldPath(tag, code));
  }

  public static String createDatafieldPath(String tag, String code) {
    return String.format(DATAFIELD_PATTERN, tag, code);
  }

  private static String createDatafieldParentPath(String tag) {
    return String.format(DATAFIELD_PARENT_PATTERN, tag);
  }

  private static String createDatafieldChildrenPath(String code) {
    return String.format(DATAFIELD_CHILDREN_PATTERN, code);
  }

  @Override
  public Format getFormat() {
    return Format.JSON;
  }

  @Override
  public List<DataElement> getCollectionPaths() {
    return new ArrayList(COLLECTION_PATHS.values());
  }

  @Override
  public List<DataElement> getRootChildrenPaths() {
    return new ArrayList(DIRECT_CHILDREN.values());
  }

  @Override
  public List<DataElement> getPaths() {
    return new ArrayList(PATHS.values());
  }

  @Override
  public List<FieldGroup> getFieldGroups() {
    return new ArrayList<>();
  }

  @Override
  public List<String> getNoLanguageFields() {
    throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
  }

  @Override
  public List<DataElement> getIndexFields() {
    throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
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
  public void addExtractableField(String label, String path) {
    extractableFields.put(label, path);
  }

  private static void addPath(DataElement dataElement) {
    PATHS.put(dataElement.getLabel(), dataElement);

    if (dataElement.getParent() == null)
      DIRECT_CHILDREN.put(dataElement.getLabel(), dataElement);

    if (dataElement.isCollection())
      COLLECTION_PATHS.put(dataElement.getLabel(), dataElement);
  }

  @Override
  public List<String> getEmptyStringPaths() {
    throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
  }

  @Override
  public String getSubjectPath() {
    throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
  }

  @Override
  public String getTitlePath() {
    throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
  }

  @Override
  public String getDescriptionPath() {
    throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
  }

  @Override
  public DataElement getPathByLabel(String label) {
    return PATHS.get(label);
  }

  @Override
  public DataElement getRecordId() {
    return PATHS.get("001");
  }

  @Override
  public List<String> getCategories() {
    if (categories == null) {
      categories = Category.extractCategories(PATHS.values());
    }
    return categories;
  }

  @Override
  public List<RuleChecker> getRuleCheckers() {
    if (ruleCheckers == null) {
      ruleCheckers = SchemaUtils.getRuleCheckers(this);
    }
    return ruleCheckers;
  }

  @Override
  public void merge(Schema schemaB, boolean allowOverwrite) {
    throw new UnsupportedOperationException("The method in ot available in this class.");
  }
}
