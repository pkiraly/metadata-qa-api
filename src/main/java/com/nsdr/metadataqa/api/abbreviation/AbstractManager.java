package com.nsdr.metadataqa.api.abbreviation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
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
	private static FileSystem fs;

	public AbstractManager() {
		data = new LinkedHashMap<>();
	}

	protected void initialize(String fileName) {
		Path path = null;
		try {
			path = getPath(fileName);
			List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
			int i = 1;
			for (String line : lines) {
				processLine(line, i);
			}
		} catch (URISyntaxException | IOException | FileSystemNotFoundException ex) {
			logger.severe(String.format("Error with file: %s, uri: %s, path: %s.", fileName, path));
			logger.severe(ex.getLocalizedMessage());
		}
	}

	private void processLine(String line, int i) throws NumberFormatException {
		if (line.contains(";")) {
			String[] parts = line.split(";", 2);
			data.put(parts[1], Integer.parseInt(parts[0]));
		} else {
			data.put(line, i++);
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

	private Path getPath(String fileName) throws IOException, URISyntaxException {
		Path path;
		URI uri = getClass().getClassLoader().getResource(fileName).toURI();
		Map<String, String> env = new HashMap<>();
		if (uri.toString().contains("!")) {
			String[] parts = uri.toString().split("!");
			if (fs == null) {
				fs = FileSystems.newFileSystem(URI.create(parts[0]), env);
			}
			path = fs.getPath(parts[1]);
		} else {
			path = Paths.get(uri);
		}
		return path;
	}

}
