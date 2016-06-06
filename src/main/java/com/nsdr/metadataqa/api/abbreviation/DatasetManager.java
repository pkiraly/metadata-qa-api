package com.nsdr.metadataqa.api.abbreviation;

import java.util.Map;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class DatasetManager extends AbstractManager {

	public DatasetManager() {
		super();
		initialize("abbreviations/datasets.txt");
	}

	public Map<String, Integer> getDatasets() {
		return data;
	}
}
