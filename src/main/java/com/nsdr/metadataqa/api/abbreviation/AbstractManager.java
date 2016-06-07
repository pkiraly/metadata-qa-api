package com.nsdr.metadataqa.api.abbreviation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class AbstractManager implements Serializable {

	private static final Logger logger = Logger.getLogger(AbstractManager.class.getCanonicalName());
	protected Map<String, Integer> data;

	public AbstractManager() {
		data = new LinkedHashMap<>();
	}

	protected void initialize(String fileName) {
		Path path = null;
		try {
			path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
			List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
			int i = 1;
			for (String line : lines) {
				data.put(line, i++);
			}
		} catch (URISyntaxException | IOException ex) {
			logger.severe(String.format("Error with file: %s, path: %s.", fileName, path));
			logger.severe(ex.getLocalizedMessage());
		}
	}

	public Integer lookup(String entry) {
		if (!data.containsKey(entry)) {
			int oldsize = data.size();
			data.put(entry, data.size() + 1);
			logger.info(String.format("new entry: %s (size: %d -> %d)",
					  entry, oldsize, data.size()));
		}
		return data.get(entry);
	}

	public void save(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
		try (PrintWriter writer = new PrintWriter(fileName, "UTF-8")) {
			for (Map.Entry<String, Integer> entry : data.entrySet()) {
				writer.println(String.format("%d;%s", entry.getValue(), entry.getKey()));
			}
		}
	}
}
