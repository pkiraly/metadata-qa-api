package de.gwdg.metadataqa.api.schema;

import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.Category;
import de.gwdg.metadataqa.api.rule.RuleChecker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class MarcJsonSchema implements Schema, ProblemCatalogSchema, Serializable {

  private static final long serialVersionUID = -351328095358861985L;
  private static final Map<String, JsonBranch> PATHS = new LinkedHashMap<>();
  private static final Map<String, JsonBranch> COLLECTION_PATHS = new LinkedHashMap<>();
  private static final Map<String, JsonBranch> DIRECT_CHILDREN = new LinkedHashMap<>();
  public static final String NOT_SUPPORTED_YET = "Not supported yet.";
  private static Map<String, String> extractableFields = new LinkedHashMap<>();
  private static Map<String, String> echoFields = new LinkedHashMap<>();
  private static List<String> categories = null;
  private static List<RuleChecker> ruleCheckers = null;

  public static final String DATAFIELD_PATTERN = "$.datafield[?(@.tag == '%s')].subfield[?(@.code == '%s')].content";
  public static final String DATAFIELD_PARENT_PATTERN = "$.datafield[?(@.tag == '%s')]";
  public static final String DATAFIELD_IND1_PATTERN = "$.ind1";
  public static final String DATAFIELD_IND2_PATTERN = "$.ind2";
  public static final String DATAFIELD_CHILDREN_PATTERN = "$.subfield[?(@.code == '%s')].content";

  static {
    addPath(new JsonBranch("leader", "$.leader"));
    addPath(new JsonBranch("001", "$.controlfield[?(@.tag == '001')].content"));
    addPath(new JsonBranch("003", "$.controlfield[?(@.tag == '003')].content"));
    addPath(new JsonBranch("005", "$.controlfield[?(@.tag == '005')].content"));
    addPath(new JsonBranch("006", "$.controlfield[?(@.tag == '006')].content"));
    addPath(new JsonBranch("007", "$.controlfield[?(@.tag == '007')].content"));
    addPath(new JsonBranch("008", "$.controlfield[?(@.tag == '008')].content"));

    registerDatafieldBranch("010", "a", "b", "z", "8", "z");
    registerDatafieldBranch("013", "a", "c");
    registerDatafieldBranch("015", "2", "a", "z");
    registerDatafieldBranch("016", "2", "a");
    registerDatafieldBranch("017", "a", "b");
    registerDatafieldBranch("018", "a");
    registerDatafieldBranch("019", "a"); // ??? not in the standard!!!
    registerDatafieldBranch("020", "6", "9", "a", "b", "c", "z");
    registerDatafieldBranch("022", "a", "c", "y", "z");
    registerDatafieldBranch("024", "2", "9", "a", "c", "d", "z");
    registerDatafieldBranch("025", "a");
    registerDatafieldBranch("026", "a", "e");
    registerDatafieldBranch("027", "a");
    registerDatafieldBranch("028", "a", "b");
    registerDatafieldBranch("029", "a", "b", "c"); // ??? not in the standard!
    registerDatafieldBranch("030", "a", "z");
    // 31
    registerDatafieldBranch("032", "a", "b");
    registerDatafieldBranch("033", "a", "b", "c");
    registerDatafieldBranch("034", "a", "b", "c", "d", "e", "f", "g");
    registerDatafieldBranch("035", "6", "a", "z");
    registerDatafieldBranch("036", "a");
    registerDatafieldBranch("037", "a", "b", "c", "f", "n");
    registerDatafieldBranch("038", "a");
    registerDatafieldBranch("040", "6", "a", "b", "c", "d", "e", "h", "t", "w");
    registerDatafieldBranch("041", "2", "a", "b", "d", "e", "f", "g", "h", "j", "k", "m", "n");
    registerDatafieldBranch("042", "a");
    registerDatafieldBranch("043", "2", "a", "b", "c");
    registerDatafieldBranch("044", "a", "h");
    registerDatafieldBranch("045", "a", "b");
    registerDatafieldBranch("046", "2", "a", "b", "d", "j", "k");
    registerDatafieldBranch("047", "a");
    registerDatafieldBranch("048", "a", "b");
    registerDatafieldBranch("049", "a"); // ???
    registerDatafieldBranch("050", "3", "a", "b", "i");
    registerDatafieldBranch("051", "a", "b", "c", "i");
    registerDatafieldBranch("052", "a", "b");
    registerDatafieldBranch("055", "2", "a", "b");
    registerDatafieldBranch("060", "b");
    registerDatafieldBranch("061", "a");
    registerDatafieldBranch("066", "a", "c");
    registerDatafieldBranch("070", "a", "b");
    // 71
    registerDatafieldBranch("072", "2", "a", "x");
    registerDatafieldBranch("074", "a", "z");
    registerDatafieldBranch("080", "2", "a", "b", "x");
    registerDatafieldBranch("082", "2", "9", "a", "b", "m", "n", "q");
    registerDatafieldBranch("083", "2", "a");
    registerDatafieldBranch("084", "a", "b");
    registerDatafieldBranch("085", "8", "a", "b", "s", "z");
    registerDatafieldBranch("086", "2", "6", "a", "z");
    registerDatafieldBranch("088", "a");
    registerDatafieldBranch("090", "a", "b", "v");

    registerDatafieldBranch("100", "0", "4", "6", "a", "b", "c", "d", "e", "f", "g", "h", "k", "l", "n", "p", "q", "t", "u");
    registerDatafieldBranch("110", "0", "4", "6", "9", "a", "b", "c", "e", "f", "k", "l", "n", "p", "s", "t", "u");
    registerDatafieldBranch("111", "0", "4", "6", "9", "a", "b", "c", "d", "e", "f", "g", "j", "k", "l", "n", "q", "p", "t");
    registerDatafieldBranch("130", "0", "6", "7", "9", "a", "d", "f", "g", "h", "k", "l", "m", "n", "o", "p", "r", "s", "t", "x", "v");

    registerDatafieldBranch("199", "9"); // ??

    registerDatafieldBranch("210", "a");
    registerDatafieldBranch("222", "a", "b");
    registerDatafieldBranch("240", "0", "6", "7", "9", "a", "d", "f", "g", "h", "k", "l", "m", "n", "o", "p", "r", "s", "t", "x");
    registerDatafieldBranch("241", "a"); // ??
    registerDatafieldBranch("242", "a", "b", "c", "y");
    registerDatafieldBranch("243", "a", "f", "k", "l");
    registerDatafieldBranch("245", "6", "a", "b", "c", "d", "e", "f", "g", "h", "k", "n", "p", "s", "v");
    registerDatafieldBranch("246", "5", "6", "a", "b", "d", "f", "h", "i", "n", "p");
    registerDatafieldBranch("247", "a", "b", "f", "g", "p");
    registerDatafieldBranch("249", "a", "b", "c", "v"); // ????
    registerDatafieldBranch("250", "6", "9", "a", "c");
    registerDatafieldBranch("254", "a");
    registerDatafieldBranch("255", "a", "b", "c", "e");
    // 256
    registerDatafieldBranch("257", "a");
    registerDatafieldBranch("258", "a");
    registerDatafieldBranch("260", "3", "6", "a", "b", "c", "d", "e", "f", "g", "z");
    // 263
    registerDatafieldBranch("264", "3", "a", "b", "c");
    registerDatafieldBranch("265", "a"); // ????
    registerDatafieldBranch("270", "a", "b", "c", "d", "e", "g", "h", "k", "l", "m", "n");
    registerDatafieldBranch("290", "a", "b"); // ????

    registerDatafieldBranch("300", "2", "3", "6", "a", "b", "c", "e", "f", "g", "x"); // " " <- ?????
    registerDatafieldBranch("306", "a");
    registerDatafieldBranch("307", "a");
    registerDatafieldBranch("310", "a", "b");
    // 321
    registerDatafieldBranch("336", "2", "3", "8", "a", "b");
    registerDatafieldBranch("337", "2", "3", "8", "a", "b");
    registerDatafieldBranch("338", "2", "3", "8", "a", "b");
    registerDatafieldBranch("340", "a");
    // 342
    // 343
    registerDatafieldBranch("344", "2", "a", "b", "g", "h");
    // 345
    registerDatafieldBranch("346", "2", "b");
    registerDatafieldBranch("347", "2", "3", "a", "b", "c", "d");
    // 348
    registerDatafieldBranch("350", "a"); // ???
    registerDatafieldBranch("351", "3", "a", "b");
    // 352
    // 355
    // 357
    registerDatafieldBranch("362", "a", "z");
    registerDatafieldBranch("363", "a", "b", "i", "j", "k", "u");
    registerDatafieldBranch("365", "2", "a");
    registerDatafieldBranch("366", "2", "a", "b", "c", "j", "k", "m");
    // 370
    registerDatafieldBranch("377", "2", "a");
    registerDatafieldBranch("380", "0", "2", "a");
    // 381
    registerDatafieldBranch("382", "0", "2", "a", "d", "g", "n", "p", "s", "v");
    registerDatafieldBranch("383", "a", "b", "c");
    registerDatafieldBranch("384", "a", "b");
    registerDatafieldBranch("385", "0", "a", "g");
    // 386
    // 388

    registerDatafieldBranch("400", "6", "a", "b", "c", "d", "e", "f", "l", "n", "p", "q", "t", "v", "x"); // ???
    registerDatafieldBranch("410", "6", "a", "b", "c", "d", "f", "g", "k", "n", "p", "t", "u", "x", "v"); // ???
    registerDatafieldBranch("411", "a", "c", "d", "e", "g", "n", "p", "q", "t", "v"); // ???
    registerDatafieldBranch("440", "a", "n", "p", "v", "x"); // ???
    registerDatafieldBranch("489", "a", "v"); // ???
    registerDatafieldBranch("490", "2", "3", "6", "a", "l", "n", "p", "x", "v");

    registerDatafieldBranch("500", "2", "3", "5", "6", "a", "g", "x");
    registerDatafieldBranch("501", "5", "6", "a");
    registerDatafieldBranch("502", "6", "a", "b", "c", "d", "6");
    registerDatafieldBranch("504", "6", "b");
    registerDatafieldBranch("505", "a", "g", "r", "t", "u");
    registerDatafieldBranch("506", "3", "5", "a", "c", "u");
    registerDatafieldBranch("507", "a");
    registerDatafieldBranch("508", "6", "a");
    registerDatafieldBranch("510", "3", "6", "b", "x");
    registerDatafieldBranch("511", "a");
    registerDatafieldBranch("513", "a", "b");
    registerDatafieldBranch("514", "a");
    registerDatafieldBranch("515", "a");
    registerDatafieldBranch("516", "a");
    // 517
    registerDatafieldBranch("518", "a");
    registerDatafieldBranch("520", "2", "3", "6", "a", "b", "c", "u");
    registerDatafieldBranch("521", "6", "a", "b");
    registerDatafieldBranch("522", "a");
    registerDatafieldBranch("524", "a");
    registerDatafieldBranch("525", "6", "a");
    registerDatafieldBranch("526", "a", "b", "c", "d", "z");
    registerDatafieldBranch("530", "3", "6", "a", "b", "c", "d", "u");
    registerDatafieldBranch("533", "3", "5", "6", "7", "a", "b", "c", "d", "e", "f", "m", "n", "x");
    registerDatafieldBranch("534", "6", "c", "e", "f", "k", "l", "p", "t", "z");
    registerDatafieldBranch("535", "3", "a", "b", "c", "g", "q");
    registerDatafieldBranch("536", "6", "a", "b", "c", "d", "e", "f", "g", "h");
    registerDatafieldBranch("538", "6", "a", "u");
    registerDatafieldBranch("540", "3", "5", "a", "b", "c", "u");
    registerDatafieldBranch("541", "3", "5", "a", "b", "c", "d", "e", "f", "h", "n", "o");
    registerDatafieldBranch("542", "a", "c", "d", "f", "g");
    registerDatafieldBranch("544", "3", "a", "b", "d", "e", "n");
    registerDatafieldBranch("545", "a", "b");
    registerDatafieldBranch("546", "3", "6", "a", "b");
    // 547
    registerDatafieldBranch("550", "a");
    registerDatafieldBranch("552", "a");
    registerDatafieldBranch("555", "a", "c", "u");
    registerDatafieldBranch("556", "a");
    registerDatafieldBranch("561", "3", "5", "6", "a");
    registerDatafieldBranch("562", "5", "a", "b", "c");
    registerDatafieldBranch("563", "5", "a");
    registerDatafieldBranch("565", "a");
    // 567
    registerDatafieldBranch("580", "a");
    registerDatafieldBranch("581", "3", "8", "a");
    registerDatafieldBranch("583", "3", "5", "a", "b", "c", "d", "e", "f", "h", "i", "j", "l", "u", "x");
    registerDatafieldBranch("584", "a");
    registerDatafieldBranch("585", "3", "5", "6", "a");
    registerDatafieldBranch("586", "a");
    registerDatafieldBranch("588", "5", "a");
    registerDatafieldBranch("589", "c", "d"); // ???
    registerDatafieldBranch("590", "a");
    registerDatafieldBranch("591", "2", "a", "x");
    registerDatafieldBranch("593", "a");

    registerDatafieldBranch("600", "0", "1", "2", "4", "6", "9", "a", "b", "c", "d", "f", "h", "m", "n", "o", "p", "r", "s", "t", "x", "v");
    registerDatafieldBranch("610", "0", "2", "3", "6", "9", "a", "b", "f", "g", "h", "n", "o", "t", "u", "x", "v");
    registerDatafieldBranch("611", "0", "2", "3", "6", "9", "a", "b", "c", "d", "f", "g", "k", "l", "n", "p", "t");
    registerDatafieldBranch("630", "0", "2", "3", "6", "9", "a", "f", "n", "o", "p", "t", "x");
    // 647
    registerDatafieldBranch("648", "2", "a", "x", "v");
    registerDatafieldBranch("650", "0", "2", "3", "6", "9", "a", "e", "k", "q", "x", "y", "z");
    registerDatafieldBranch("651", "0", "2", "3", "4", "6", "9", "a", "e", "k", "n", "t", "x", "v", "z");
    registerDatafieldBranch("653", "6", "a");
    registerDatafieldBranch("654", "2", "a", "b", "c");
    registerDatafieldBranch("655", "0", "2", "3", "a", "b", "g", "x", "y", "z");
    registerDatafieldBranch("656", "2", "a", "v", "x", "z");
    registerDatafieldBranch("657", "2", "a", "y");
    // 658
    registerDatafieldBranch("662", "2", "a", "d");
    registerDatafieldBranch("689", "0", "2", "5", "9", "A", "a", "b", "c", "D", "d", "f", "g", "m", "n", "p", "r", "s", "t", "x", "z"); // ????
    // 69X

    // 70X-75X: Added Entry Fields
    registerDatafieldBranch("700", "0", "3", "4", "5", "6", "a", "b", "c", "d", "e", "f", "g", "h", "i", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "T", "u", "U", "x");
    registerDatafieldBranch("710", "0", "3", "4", "5", "6", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "x", "v");
    registerDatafieldBranch("711", "0", "4", "6", "9", "a", "b", "c", "d", "e", "f", "g", "i", "j", "k", "l", "n", "p", "q", "s", "t");
    registerDatafieldBranch("720", "4", "6", "a", "e");
    registerDatafieldBranch("730", "0", "5", "6", "a", "d", "f", "g", "h", "i", "k", "l", "m", "n", "o", "p", "r", "s", "t", "T", "U", "x");
    registerDatafieldBranch("740", "5", "6", "a", "h", "n", "p", "v");
    registerDatafieldBranch("751", "4", "6", "a");
    registerDatafieldBranch("752", "a", "b", "c", "d", "f");
    registerDatafieldBranch("753", "a", "c");
    // 754

    // 76X-78X: Linking Entry Fields
    registerDatafieldBranch("760", "a", "d", "g", "t", "x", "y", "w");
    // 762
    registerDatafieldBranch("765", "6", "a", "b", "c", "d", "h", "i", "k", "o", "t", "w", "x", "z");
    registerDatafieldBranch("767", "a", "d", "i", "s", "t", "w", "z");
    registerDatafieldBranch("770", "6", "a", "b", "c", "d", "h", "i", "k", "n", "o", "s", "t", "w", "x", "z");
    registerDatafieldBranch("772", "7", "a", "b", "c", "g", "h", "i", "k", "n", "s", "t", "w", "x", "z");
    registerDatafieldBranch("773", "3", "6", "7", "a", "b", "d", "g", "h", "i", "n", "o", "q", "s", "t", "x", "w");
    registerDatafieldBranch("774", "a", "b", "c", "d", "g", "i", "s", "t", "w", "z");
    registerDatafieldBranch("775", "0", "6", "a", "b", "c", "d", "e", "f", "g", "h", "i", "k", "n", "o", "s", "t", "u", "w", "z");
    registerDatafieldBranch("776", "0", "4", "6", "9", "a", "b", "c", "d", "g", "h", "i", "k", "m", "n", "o", "q", "s", "t", "u", "x", "w", "z");
    registerDatafieldBranch("777", "6", "7", "a", "c", "h", "t", "w");
    registerDatafieldBranch("780", "0", "6", "a", "b", "c", "d", "g", "i", "k", "n", "s", "t", "x", "w", "z");
    registerDatafieldBranch("785", "0", "6", "a", "b", "c", "d", "i", "n", "x", "t", "x", "w", "z");
    // 786
    registerDatafieldBranch("787", "0", "6", "7", "a", "b", "d", "g", "h", "i", "n", "o", "r", "s", "t", "w");

    // 80X-83X: Series Added Entry Fields
    registerDatafieldBranch("800", "2", "4", "6", "a", "b", "c", "d", "e", "f", "g", "k", "l", "p", "q", "s", "t", "v", "w");
    registerDatafieldBranch("810", "4", "6", "a", "b", "c", "d", "f", "g", "k", "l", "p", "q", "r", "s", "t", "v", "w", "x");
    registerDatafieldBranch("811", "6", "a", "b", "c", "d", "e", "f", "g", "k", "l", "n", "p", "q", "t", "v", "w");
    registerDatafieldBranch("830", "3", "6", "a", "d", "f", "g", "h", "k", "l", "m", "o", "p", "q", "s", "t", "x", "v", "w");

    registerDatafieldBranch("840", "a", "v"); // ??

    // 841-88X: Holdings, Location, Alternate Graphics, etc. Fields
    // 841
    // 842
    // 843
    // 844
    // 845
    registerDatafieldBranch("850", "a");
    registerDatafieldBranch("852", "2", "3", "a", "b", "c", "e", "h", "i", "j", "k", "m", "n", "p", "t", "u", "x", "w", "z");
    // 853
    // 854
    // 855
    registerDatafieldBranch("856", "2", "3", "a", "b", "c", "d", "f", "h", "i", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "u", "x", "y", "v", "w", "z");
    // 863
    // 864
    registerDatafieldBranch("865", "a");
    registerDatafieldBranch("866", "a");
    // 867
    // 868
    // 876
    // 877
    // 878
    registerDatafieldBranch("880", "0", "1", "2", "3", "5", "6", "8", "a", "b", "c", "d", "f", "g", "i", "k", "l", "o", "q", "r", "s", "t", "T", "U", "x", "y", "v", "z");
    // 882
    // 883
    // 884
    // 885
    registerDatafieldBranch("886", "2", "a", "b", "c", "d", "e", "f", "h", "k", "x", "z");
    registerDatafieldBranch("887", "2", "a");
    registerDatafieldBranch("889", "w"); // ???

    registerDatafieldBranch("911", "a", "b", "9"); // OCLC
    registerDatafieldBranch("912", "a", "b", "9"); // OCLC
    registerDatafieldBranch("924", "9", "a", "b", "c", "d", "e", "g", "h", "i", "j", "k", "l", "m", "n", "q", "r", "s", "v", "w", "x", "y", "z");
    registerDatafieldBranch("935", "a", "b", "c", "d", "e", "m");
    registerDatafieldBranch("936", "0", "a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "m", "q", "y");
    registerDatafieldBranch("937", "a", "b", "c", "d", "e", "f");
    registerDatafieldBranch("938", "a", "b", "n");
    registerDatafieldBranch("987", "a", "b", "c", "d", "e", "f"); // ???
    registerDatafieldBranch("994", "a", "b");

    extractableFields.put("leader", "$.leader");
    extractableFields.put("recordId", "$.controlfield[?(@.tag == '001')].content");
    extractableFields.put("001", PATHS.get("001").getJsonPath());
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


    /*
    */

  }

  private static void registerDatafieldBranch(String tag, String... codes) {
    var parent = new JsonBranch(tag, createDatafieldParentPath(tag));
    parent.setCollection(true);
    addPath(parent);
    addPath(new JsonBranch(tag + "$ind1", parent, DATAFIELD_IND1_PATTERN));
    addPath(new JsonBranch(tag + "$ind2", parent, DATAFIELD_IND2_PATTERN));
    for (String code : codes) {
      addPath(new JsonBranch(tag + "$" + code, parent, createDatafieldChildrenPath(code)));
    }
  }

  private static JsonBranch createDatafieldBranch(String tag, String code) {
    return new JsonBranch(tag + "$" + code, createDatafieldPath(tag, code));
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
  public List<JsonBranch> getCollectionPaths() {
    return new ArrayList(COLLECTION_PATHS.values());
  }

  @Override
  public List<JsonBranch> getRootChildrenPaths() {
    return new ArrayList(DIRECT_CHILDREN.values());
  }

  @Override
  public List<JsonBranch> getPaths() {
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
  public List<JsonBranch> getIndexFields() {
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
  public void addExtractableField(String label, String jsonPath) {
    extractableFields.put(label, jsonPath);
  }

  @Override
  public Map<String, String> getEchoFields() {
    return echoFields;
  }

  @Override
  public void setEchoFields(Map<String, String> echoFields) {
    this.echoFields = echoFields;
  }

  @Override
  public void addEchoField(String label, String jsonPath) {
    echoFields.put(label, jsonPath);
  }

  private static void addPath(JsonBranch branch) {
    PATHS.put(branch.getLabel(), branch);

    if (branch.getParent() == null)
      DIRECT_CHILDREN.put(branch.getLabel(), branch);

    if (branch.isCollection())
      COLLECTION_PATHS.put(branch.getLabel(), branch);
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
  public JsonBranch getPathByLabel(String label) {
    return PATHS.get(label);
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
}
