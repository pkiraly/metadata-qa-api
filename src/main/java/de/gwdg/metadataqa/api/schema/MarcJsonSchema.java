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
public class MarcJsonSchema implements Schema {

	private final static Map<String, JsonBranch> paths = new LinkedHashMap<>();
	private final static Map<String, JsonBranch> collectionPaths = new LinkedHashMap<>();
	private final static Map<String, JsonBranch> directChildren = new LinkedHashMap<>();
	private static final String DATAFIELD_PATTERN = "$.datafield[?(@.tag == '%s')].subfield[?(@.code == '%s')].content";
	private static final String DATAFIELD_PARENT_PATTERN = "$.datafield[?(@.tag == '%s')]";
	private static final String DATAFIELD_CHILDREN_PATTERN = "$.subfield[?(@.code == '%s')].content";

	static {
		addPath(new JsonBranch("leader", "$.leader"));
		addPath(new JsonBranch("001", "$.controlfield[?(@.tag == '001')].content"));
		addPath(new JsonBranch("003", "$.controlfield[?(@.tag == '003')].content"));
		addPath(new JsonBranch("005", "$.controlfield[?(@.tag == '005')].content"));
		addPath(new JsonBranch("007", "$.controlfield[?(@.tag == '007')].content"));
		addPath(new JsonBranch("008", "$.controlfield[?(@.tag == '008')].content"));

		registerDatafieldBranch("010", "a");
		registerDatafieldBranch("015", "2", "a");
		registerDatafieldBranch("016", "2", "a");
		registerDatafieldBranch("019", "a");
		registerDatafieldBranch("020", "9", "a", "c", "z");
		registerDatafieldBranch("022", "a", "c", "y");
		registerDatafieldBranch("024", "2", "9", "a", "z");
		registerDatafieldBranch("026", "e");
		registerDatafieldBranch("028", "a");
		registerDatafieldBranch("029", "a", "b", "c");
		registerDatafieldBranch("030", "a");
		registerDatafieldBranch("032", "a");
		registerDatafieldBranch("034", "a", "b");
		registerDatafieldBranch("035", "a", "z");
		registerDatafieldBranch("040", "a", "b", "c", "d", "e");
		registerDatafieldBranch("041", "a", "b", "h");
		registerDatafieldBranch("043", "a");
		registerDatafieldBranch("044", "a");
		registerDatafieldBranch("046", "2", "j");
		registerDatafieldBranch("049", "a");
		registerDatafieldBranch("050", "a", "b");
		registerDatafieldBranch("066", "c");
		registerDatafieldBranch("082", "a");
		registerDatafieldBranch("084", "a", "a");
		registerDatafieldBranch("088", "a");
		registerDatafieldBranch("090", "a", "b");

		registerDatafieldBranch("100", "0", "4", "6", "a", "b", "c", "d", "e");
		registerDatafieldBranch("110", "0", "6", "9", "a", "b", "n");
		registerDatafieldBranch("111", "0", "9", "a", "c", "d", "e", "n");
		registerDatafieldBranch("130", "0", "a", "g", "m", "n", "o", "p");

		registerDatafieldBranch("210", "a");
		registerDatafieldBranch("240", "0", "a", "f", "g", "k", "m", "n", "o", "p", "s");
		registerDatafieldBranch("243", "a");
		registerDatafieldBranch("245", "6", "a", "b", "c", "h", "n", "p");
		registerDatafieldBranch("246", "6", "a", "d", "i");
		registerDatafieldBranch("249", "a", "b");
		registerDatafieldBranch("250", "6", "9", "a");
		registerDatafieldBranch("255", "a", "b", "c");
		registerDatafieldBranch("260", "6", "a", "b", "c", "e", "f", "g");
		registerDatafieldBranch("264", "a", "b", "c");
		registerDatafieldBranch("290", "b");

		registerDatafieldBranch("300", "a", "b", "c", "e", "x");
		registerDatafieldBranch("336", "2", "a", "b");
		registerDatafieldBranch("337", "2", "a", "b");
		registerDatafieldBranch("338", "2", "a", "b");
		registerDatafieldBranch("340", "a");
		registerDatafieldBranch("362", "a");
		registerDatafieldBranch("363", "a", "b", "i", "j", "k", "u");

		registerDatafieldBranch("490", "6", "a", "v");

		registerDatafieldBranch("500", "6", "a", "x");
		registerDatafieldBranch("501", "a");
		registerDatafieldBranch("502", "a", "b", "c", "d", "6");
		registerDatafieldBranch("505", "a", "t");
		registerDatafieldBranch("511", "a");
		registerDatafieldBranch("515", "a");
		registerDatafieldBranch("520", "a");
		registerDatafieldBranch("521", "a");
		registerDatafieldBranch("530", "a");
		registerDatafieldBranch("533", "7", "a", "b", "c", "d", "e", "f", "n", "x");
		registerDatafieldBranch("534", "c");
		registerDatafieldBranch("535", "a", "g");
		registerDatafieldBranch("538", "a");
		registerDatafieldBranch("555", "a");
		registerDatafieldBranch("561", "a");
		registerDatafieldBranch("562", "a", "b");
		registerDatafieldBranch("563", "a");
		registerDatafieldBranch("581", "8", "a");
		registerDatafieldBranch("583", "a", "c", "h");
		registerDatafieldBranch("589", "c");
		registerDatafieldBranch("590", "a");
		registerDatafieldBranch("591", "2", "a", "x");
		registerDatafieldBranch("593", "a");

		registerDatafieldBranch("600", "0", "2", "6", "a", "b", "c", "d", "n", "t", "x", "v");
		registerDatafieldBranch("610", "0", "2", "6", "9", "a", "b", "x", "v");
		registerDatafieldBranch("611", "0", "2", "9", "a", "b", "c", "d", "n");
		registerDatafieldBranch("630", "0", "2", "9", "a", "n", "p", "t", "x");
		registerDatafieldBranch("648", "2", "a");
		registerDatafieldBranch("650", "0", "2", "9", "a", "x", "y", "z");
		registerDatafieldBranch("651", "0", "2", "6", "9", "a", "x", "v", "z");
		registerDatafieldBranch("653", "6", "a");
		registerDatafieldBranch("655", "0", "2", "a", "x");
		registerDatafieldBranch("689", "0", "2", "5", "9", "A", "a", "b", "c", "D", "d", "f", "g", "m", "n", "p", "t", "x", "z");

		registerDatafieldBranch("700", "0", "4", "6", "a", "b", "c", "d", "e");
		registerDatafieldBranch("710", "0", "4", "6", "9", "a", "b", "e", "n");
		registerDatafieldBranch("711", "0", "9", "a", "c", "d", "e", "n");
		registerDatafieldBranch("730", "0", "6", "a", "f", "g", "t");
		registerDatafieldBranch("740", "6", "a");
		registerDatafieldBranch("751", "4", "a");
		registerDatafieldBranch("770", "a", "i", "t", "w");
		registerDatafieldBranch("772", "a", "i", "t", "w");
		registerDatafieldBranch("773", "a", "d", "g", "i", "q", "w");
		registerDatafieldBranch("775", "a", "d", "i", "t", "w");
		registerDatafieldBranch("776", "9", "a", "b", "c", "d", "h", "i", "o", "q", "s", "t", "w", "z");
		registerDatafieldBranch("780", "a", "i", "t", "w");
		registerDatafieldBranch("785", "a", "i", "t", "w");
		registerDatafieldBranch("787", "0", "a", "d", "i", "t", "w");

		registerDatafieldBranch("800", "a", "b", "c", "d", "g", "p", "q", "t", "v", "w");
		registerDatafieldBranch("810", "a", "b", "c", "g", "p", "q", "t", "v", "w");
		registerDatafieldBranch("830", "6", "a", "g", "p", "q", "v", "w");
		registerDatafieldBranch("856", "2", "3", "a", "m", "n", "p", "q", "u", "x", "y", "v", "z");
		registerDatafieldBranch("866", "a");
		registerDatafieldBranch("880", "6", "a", "b", "c", "d", "f", "g", "v", "x");
		registerDatafieldBranch("889", "w");

		registerDatafieldBranch("912", "a", "b");
		registerDatafieldBranch("924", "b");
		registerDatafieldBranch("935", "a", "b", "c", "d", "e");
		registerDatafieldBranch("936", "0", "a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "y");
		registerDatafieldBranch("937", "a", "b", "c", "d", "e", "f");
		registerDatafieldBranch("938", "a", "b", "n");
		registerDatafieldBranch("994", "a", "b");
	}

	private static void registerDatafieldBranch(String tag, String... codes) {
		JsonBranch parent = new JsonBranch(tag, createDatafieldParentPath(tag));
		parent.setCollection(true);
		addPath(parent);
		for (String code : codes) {
			addPath(new JsonBranch(tag + "$" + code, parent, createDatafieldChildrenPath(code)));
		}
	}

	private static JsonBranch createDatafieldBranch(String tag, String code) {
		return new JsonBranch(tag + "$" + code, createDatafieldPath(tag, code));
	}

	
	private static String createDatafieldPath(String tag, String code) {
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
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<String> getNoLanguageFields() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Map<String, String> getSolrFields() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Map<String, String> getExtractableFields() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	private static void addPath(JsonBranch branch) {
		paths.put(branch.getLabel(), branch);
		if (branch.getParent() == null)
			directChildren.put(branch.getLabel(), branch);
		if (branch.isCollection())
			collectionPaths.put(branch.getLabel(), branch);
	}
}
