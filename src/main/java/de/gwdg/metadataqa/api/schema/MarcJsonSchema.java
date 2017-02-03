package de.gwdg.metadataqa.api.schema;

import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.JsonBranch;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class MarcJsonSchema implements Schema, ProblemCatalogSchema {

	private final static Map<String, JsonBranch> paths = new LinkedHashMap<>();
	private final static Map<String, JsonBranch> collectionPaths = new LinkedHashMap<>();
	private final static Map<String, JsonBranch> directChildren = new LinkedHashMap<>();
	private final static Map<String, String> extractableFields = new LinkedHashMap<>();

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

		registerDatafieldBranch("010", "a", "b", "z", "8");
		registerDatafieldBranch("013", "a", "c");
		registerDatafieldBranch("015", "2", "a");
		registerDatafieldBranch("016", "2", "a");
		// 17
		// 18
		registerDatafieldBranch("019", "a"); // ??? not in the standard!!!
		registerDatafieldBranch("020", "9", "a", "c", "z");
		registerDatafieldBranch("022", "a", "c", "y");
		registerDatafieldBranch("024", "2", "9", "a", "c", "z");
		// 25
		registerDatafieldBranch("026", "e");
		registerDatafieldBranch("027", "a");
		registerDatafieldBranch("028", "a");
		registerDatafieldBranch("029", "a", "b", "c"); // ??? not in the standard!
		registerDatafieldBranch("030", "a");
		// 31
		registerDatafieldBranch("032", "a");
		// 33
		registerDatafieldBranch("034", "a", "b");
		registerDatafieldBranch("035", "a", "z");
		// 36
		// 37
		// 38
		registerDatafieldBranch("040", "a", "b", "c", "d", "e", "h");
		registerDatafieldBranch("041", "a", "b", "h");
		// 42
		registerDatafieldBranch("043", "a");
		registerDatafieldBranch("044", "a", "h");
		// 45
		registerDatafieldBranch("046", "2", "j");
		// 47
		// 48
		registerDatafieldBranch("049", "a"); // ???
		registerDatafieldBranch("050", "a", "b");
		// 51
		// 52
		// 55
		// 60
		// 61
		registerDatafieldBranch("066", "c");
		// 70
		// 71
		// 72
		// 74
		// 80
		registerDatafieldBranch("082", "a", "n");
		// 83
		registerDatafieldBranch("084", "a", "a");
		// 85
		// 86
		registerDatafieldBranch("088", "a");
		registerDatafieldBranch("090", "a", "b", "v");

		registerDatafieldBranch("100", "0", "4", "6", "a", "b", "c", "d", "e", "h");
		registerDatafieldBranch("110", "0", "4", "6", "9", "a", "b", "c", "e", "n");
		registerDatafieldBranch("111", "0", "4", "9", "a", "c", "d", "e", "j", "n");
		registerDatafieldBranch("130", "0", "7", "9", "a", "f", "g", "k", "m", "n", "o", "p", "r", "s", "t", "x");

		registerDatafieldBranch("210", "a");
		// 222
		registerDatafieldBranch("240", "0", "7", "9", "a", "f", "g", "k", "m", "n", "o", "p", "r", "s", "t", "x");
		// 242
		registerDatafieldBranch("243", "a");
		registerDatafieldBranch("245", "6", "a", "b", "c", "h", "n", "p");
		registerDatafieldBranch("246", "6", "a", "b", "d", "i");
		registerDatafieldBranch("247", "a", "f");
		registerDatafieldBranch("249", "a", "b", "c", "v"); // ????
		registerDatafieldBranch("250", "6", "9", "a");
		// 254
		registerDatafieldBranch("255", "a", "b", "c", "e");
		// 256
		// 257
		// 258
		registerDatafieldBranch("260", "6", "a", "b", "c", "e", "f", "g");
		// 263
		registerDatafieldBranch("264", "a", "b", "c");
		// 270
		registerDatafieldBranch("290", "a", "b"); // ????

		registerDatafieldBranch("300", "2", "a", "b", "c", "e", "x");
		// 306
		// 307
		// 310
		// 321
		registerDatafieldBranch("336", "2", "3", "8", "a", "b");
		registerDatafieldBranch("337", "2", "3", "8", "a", "b");
		registerDatafieldBranch("338", "2", "3", "8", "a", "b");
		registerDatafieldBranch("340", "a");
		// 342
		// 343
		// 344
		// 345
		// 346
		// 347
		// 348
		// 351
		// 352
		// 355
		// 357
		registerDatafieldBranch("362", "a");
		registerDatafieldBranch("363", "a", "b", "i", "j", "k", "u");
		// 365
		// 366
		// 370
		// 377
		registerDatafieldBranch("380", "0", "2", "a");
		// 381
		registerDatafieldBranch("382", "0", "2", "a", "d", "g", "n", "p", "s", "v");
		registerDatafieldBranch("383", "a", "b", "c");
		registerDatafieldBranch("384", "a", "b");
		registerDatafieldBranch("385", "0", "a", "g");
		// 386
		// 388

		registerDatafieldBranch("490", "2", "6", "a", "x", "v");

		registerDatafieldBranch("500", "2", "6", "a", "g", "x");
		registerDatafieldBranch("501", "a");
		registerDatafieldBranch("502", "a", "b", "c", "d", "6");
		// 504
		registerDatafieldBranch("505", "a", "r", "t");
		// 506
		// 507
		registerDatafieldBranch("508", "a");
		// 510
		registerDatafieldBranch("511", "a");
		// 513
		// 514
		registerDatafieldBranch("515", "a");
		// 516
		// 517
		registerDatafieldBranch("518", "a");
		registerDatafieldBranch("520", "a");
		registerDatafieldBranch("521", "a");
		// 522
		// 524
		// 525
		// 526
		registerDatafieldBranch("530", "a");
		registerDatafieldBranch("533", "7", "a", "b", "c", "d", "e", "f", "n", "x");
		registerDatafieldBranch("534", "c");
		registerDatafieldBranch("535", "a", "g", "q");
		// 536
		registerDatafieldBranch("538", "a");
		// 540
		// 541
		// 542
		// 544
		// 545
		registerDatafieldBranch("546", "a");
		// 547
		// 550
		// 552
		registerDatafieldBranch("555", "a");
		// 556
		registerDatafieldBranch("561", "a");
		registerDatafieldBranch("562", "a", "b");
		registerDatafieldBranch("563", "a");
		// 565
		// 567
		// 580
		registerDatafieldBranch("581", "8", "a");
		registerDatafieldBranch("583", "a", "c", "h");
		// 584
		// 585
		// 586
		// 588
		registerDatafieldBranch("589", "c", "d"); // ???
		registerDatafieldBranch("590", "a");
		registerDatafieldBranch("591", "2", "a", "x");
		registerDatafieldBranch("593", "a");

		registerDatafieldBranch("600", "0", "2", "6", "9", "a", "b", "c", "d", "f", "m", "n", "p", "r", "s", "t", "x", "v");
		registerDatafieldBranch("610", "0", "2", "6", "9", "a", "b", "f", "g", "n", "t", "x", "v");
		registerDatafieldBranch("611", "0", "2", "9", "a", "b", "c", "d", "n", "t");
		registerDatafieldBranch("630", "0", "2", "9", "a", "f", "n", "p", "t", "x");
		// 647
		registerDatafieldBranch("648", "2", "a", "x");
		registerDatafieldBranch("650", "0", "2", "9", "a", "q", "x", "y", "z");
		registerDatafieldBranch("651", "0", "2", "6", "9", "a", "x", "v", "z");
		registerDatafieldBranch("653", "6", "a");
		// 654
		registerDatafieldBranch("655", "0", "2", "a", "g", "x", "y", "z");
		// 656
		// 657
		// 658
		// 662
		registerDatafieldBranch("689", "0", "2", "5", "9", "A", "a", "b", "c", "D", "d", "f", "g", "m", "n", "p", "r", "s", "t", "x", "z"); // ????
		// 69X

		// 70X-75X: Added Entry Fields
		registerDatafieldBranch("700", "0", "4", "6", "a", "b", "c", "d", "e", "f", "g", "k", "m", "n", "o", "p", "q", "r", "s", "t", "T", "U");
		registerDatafieldBranch("710", "0", "4", "6", "9", "a", "b", "c", "d", "e", "f", "g", "k", "m", "n", "o", "p", "r", "s", "t");
		registerDatafieldBranch("711", "0", "4", "9", "a", "c", "d", "e", "j", "n", "t");
		// 720
		registerDatafieldBranch("730", "0", "6", "a", "f", "g", "k", "m", "n", "o", "p", "r", "s", "t", "T", "U");
		registerDatafieldBranch("740", "6", "a");
		registerDatafieldBranch("751", "4", "a");
		// 752
		// 753
		// 754

		// 76X-78X: Linking Entry Fields
		// 760
		// 762
		// 765
		// 767
		registerDatafieldBranch("770", "a", "d", "h", "i", "n", "o", "t", "w");
		registerDatafieldBranch("772", "a", "i", "n", "t", "w");
		registerDatafieldBranch("773", "a", "d", "g", "h", "i", "n", "o", "q", "t", "x", "w");
		// 774
		registerDatafieldBranch("775", "0", "a", "d", "h", "i", "n", "o", "t", "w");
		registerDatafieldBranch("776", "0", "9", "a", "b", "c", "d", "h", "i", "k", "n", "o", "q", "s", "t", "x", "w", "z");
		// 777
		registerDatafieldBranch("780", "0", "a", "d", "i", "n", "t", "w");
		registerDatafieldBranch("785", "0", "a", "d", "i", "n", "t", "w");
		// 786
		registerDatafieldBranch("787", "0", "a", "d", "h", "i", "n", "o", "t", "w");

		// 80X-83X: Series Added Entry Fields
		registerDatafieldBranch("800", "a", "b", "c", "d", "g", "p", "q", "t", "v", "w");
		registerDatafieldBranch("810", "a", "b", "c", "g", "p", "q", "t", "v", "w");
		registerDatafieldBranch("810", "a", "c", "d", "e", "g", "n", "p", "q", "t", "v", "w");
		registerDatafieldBranch("830", "6", "a", "g", "p", "q", "v", "w");

		// 841-88X: Holdings, Location, Alternate Graphics, etc. Fields
		// 841
		// 842
		// 843
		// 844
		// 845
		// 850
		// 852
		// 853
		// 854
		// 855
		registerDatafieldBranch("856", "2", "3", "a", "b", "d", "f", "h", "i", "l", "m", "n", "p", "q", "s", "t", "u", "x", "y", "v", "z");
		// 863
		// 864
		// 865
		registerDatafieldBranch("866", "a");
		// 867
		// 868
		// 876
		// 877
		// 878
		registerDatafieldBranch("880", "0", "6", "a", "b", "c", "d", "f", "g", "T", "U", "v", "x");
		// 882
		// 883
		// 884
		// 885
		// 886
		// 887
		registerDatafieldBranch("889", "w"); // ???

		registerDatafieldBranch("912", "a", "b");
		registerDatafieldBranch("924", "9", "a", "b", "c", "d", "e", "g", "h", "i", "j", "k", "l", "m", "n", "q", "r", "s", "v", "w", "x", "y", "z");
		registerDatafieldBranch("935", "a", "b", "c", "d", "e", "m");
		registerDatafieldBranch("936", "0", "a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "m", "q", "y");
		registerDatafieldBranch("937", "a", "b", "c", "d", "e", "f");
		registerDatafieldBranch("938", "a", "b", "n");
		registerDatafieldBranch("994", "a", "b");

		extractableFields.put("leader", "$.leader");
		extractableFields.put("recordId", "$.controlfield[?(@.tag == '001')].content");
		extractableFields.put("007", "$.controlfield[?(@.tag == '007')].content");
		extractableFields.put("008", "$.controlfield[?(@.tag == '008')].content");
		extractableFields.put("035$a", createDatafieldPath("035", "a"));
		extractableFields.put("245$a", createDatafieldPath("245", "a"));
		extractableFields.put("100$a", createDatafieldPath("100", "a"));
		extractableFields.put("110$a", createDatafieldPath("110", "a"));
		extractableFields.put("700$a", createDatafieldPath("700", "a"));
		extractableFields.put("710$a", createDatafieldPath("710", "a"));
		extractableFields.put("260$c", createDatafieldPath("260", "c"));
		extractableFields.put("020$a", createDatafieldPath("020", "a"));
		extractableFields.put("028$a", createDatafieldPath("028", "a"));
		extractableFields.put("260$b", createDatafieldPath("260", "b"));
		extractableFields.put("245$n", createDatafieldPath("245", "n"));
		extractableFields.put("245$p", createDatafieldPath("245", "p"));
		extractableFields.put("300$a", createDatafieldPath("300", "a"));
		extractableFields.put("254$a", createDatafieldPath("254", "a"));
		extractableFields.put("490$v", createDatafieldPath("490", "v"));
		extractableFields.put("773$g", createDatafieldPath("773", "g"));
		extractableFields.put("773$v", createDatafieldPath("773", "v"));

	}

	private static void registerDatafieldBranch(String tag, String... codes) {
		JsonBranch parent = new JsonBranch(tag, createDatafieldParentPath(tag));
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
	public List<JsonBranch> getCollectionPaths() {
		return new ArrayList(collectionPaths.values());
	}

	@Override
	public List<JsonBranch> getRootChildrenPaths() {
		return new ArrayList(directChildren.values());
	}

	@Override
	public List<JsonBranch> getPaths() {
		return new ArrayList(paths.values());
	}

	@Override
	public List<FieldGroup> getFieldGroups() {
		return new ArrayList<FieldGroup>();
	}

	@Override
	public List<String> getNoLanguageFields() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Map<String, String> getSolrFields() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Map<String, String> getExtractableFields() {
		return extractableFields;
	}

	private static void addPath(JsonBranch branch) {
		paths.put(branch.getLabel(), branch);
		if (branch.getParent() == null)
			directChildren.put(branch.getLabel(), branch);
		if (branch.isCollection())
			collectionPaths.put(branch.getLabel(), branch);
	}

	@Override
	public List<String> getEmptyStringPaths() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getSubjectPath() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getTitlePath() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getDescriptionPath() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
